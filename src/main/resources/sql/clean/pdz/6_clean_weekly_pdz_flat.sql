WITH
    credit_limit AS (
        SELECT *
        FROM {dbSchema}.clean_credit_limit
        WHERE name_do IS NOT NULL AND code IS NOT NULL
    ),
    pdz_weekly AS (
        SELECT
               pdz.*
        FROM (
                 SELECT * FROM {dbSchema}.clean_pdz
                 WHERE code IS NOT NULL AND name_do is not null and eventdate is not null AND source = 'weekly'
             ) AS pdz
        INNER JOIN (
            SELECT DISTINCT code, name_do
            FROM credit_limit
        ) AS cl
         ON pdz.code = cl.code AND pdz.name_do = cl.name_do
    ),
    date_range AS (
        SELECT DISTINCT eventdate FROM pdz_weekly
    ),
    all_counterparty AS (
        SELECT DISTINCT name_do, code FROM credit_limit
    ),
    cartesian_date AS (
        SELECT DISTINCT
            b.name_do,
            a.eventdate,
            b.code
        FROM date_range AS a
        CROSS JOIN all_counterparty AS b
    ),
    md_contractor_dict as (
        select code, max(inn_clean) as counterpartyinn, max(name_orig) as counterpartyname,
        max(is_pdz_because_of_banks) as is_pdz_because_of_banks
        FROM  {dbSchema}.md_contractor_dict GROUP BY code
    ),
    seasonality as (
        SELECT DISTINCT seasonalityOfDo, cast(month as INT) as month, seasonality
        FROM {dbSchema}.seasonality_sort_do
    ),
    validation_weekly_pdz_flat as (
        SELECT
            cl.name_do AS name_do,
            cl.code AS code,
            cl.eventdate AS eventdate,
            CASE WHEN pdz.eventdate IS NOT NULL THEN "1" ELSE "0" END AS fromWeeklyStatus,
            pdz.source,
            pdz.belongingtogroupccompanies,
            pdz.blockkbe,
            pdz.comments,
            pdz.contractname,
            d.counterpartyinn,
            d.counterpartyname,
            coalesce(pdz.currentdebt, 0) as currentdebt,
            pdz.filename,
            pdz.groupdo,
            pdz.kinddebt,
            pdz.loaddate,
            pdz.modificationdate,
            pdz.num,
            pdz.ondate,
            coalesce(pdz.overduedebt, 0) as overduedebt,
            coalesce(pdz.overduedebtbetween5and30days, 0) as overduedebtbetween5and30days,
            coalesce(pdz.overduedebtless5days, 0) as overduedebtless5days,
            coalesce(pdz.overduedebtmore30days, 0) as overduedebtmore30days,
            pdz.daysoverdue,
            pdz.plannedrepaymentmonth,
            pdz.propotionoverduedebtintotaldebt,
            pdz.sheetname,
            pdz.subdepartmentowner,
            coalesce(pdz.totaldebtwithoutreserve, 0) as totaldebtwithoutreserve,
            d.is_pdz_because_of_banks,
            pdz.id_sha2,
            pdz.is_valid,
            seasonality.seasonality as seasonality
        FROM cartesian_date as cl
        LEFT JOIN pdz_weekly as pdz
            ON cl.name_do = pdz.name_do AND cl.code = pdz.code AND cl.eventdate = pdz.eventdate
        LEFT JOIN md_contractor_dict d
            ON cl.code = d.code
        LEFT JOIN seasonality
            ON cl.name_do = seasonality.seasonalityOfDo AND month(cl.eventdate) = seasonality.month
    )
select
    name_do,
    code,
    eventdate,
    max(fromWeeklyStatus) as fromWeeklyStatus,
    max(source) as source,
    max(belongingtogroupccompanies) as belongingtogroupccompanies,
    max(blockkbe) as blockkbe,
    max(comments) as comments,
    max(contractname) as contractname,
    max(counterpartyinn) as counterpartyinn,
    max(counterpartyname) as counterpartyname,
    sum(currentdebt) as currentdebt,
    max(filename) as filename,
    max(groupdo) as groupdo,
    max(kinddebt) as kinddebt,
    max(loaddate) as loaddate,
    max(modificationdate) as modificationdate,
    max(num) as num,
    max(ondate) as ondate,
    sum(overduedebt) as overduedebt,
    sum(overduedebtbetween5and30days) as overduedebtbetween5and30days,
    sum(overduedebtless5days) as overduedebtless5days,
    sum(overduedebtmore30days) as overduedebtmore30days,
    max(daysoverdue) as daysoverdue,
    max(plannedrepaymentmonth) as plannedrepaymentmonth,
    max(propotionoverduedebtintotaldebt) as propotionoverduedebtintotaldebt,
    max(sheetname) as sheetname,
    max(subdepartmentowner) as subdepartmentowner,
    sum(totaldebtwithoutreserve) as totaldebtwithoutreserve,
    max(is_pdz_because_of_banks) as is_pdz_because_of_banks,
    max(id_sha2) as id_sha2,
    max(is_valid) as is_valid,
    max(seasonality) as seasonality
from validation_weekly_pdz_flat
group by
    name_do,
    code,
    eventdate
