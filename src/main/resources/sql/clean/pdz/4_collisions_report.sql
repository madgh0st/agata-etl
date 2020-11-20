SELECT res.*, case when res.code is null then 'NOT_MAPPING' else 'OK' end as error_status
    from (
    SELECT r.*,
           coalesce(r.md_1_code, r.md_2_code) as code,
           from_unixtime(unix_timestamp()) AS ts_update
    FROM (
        SELECT
            max(md_1.code) as md_1_code,
            max(md_2.code) as md_2_code,
            pdz.source,
            pdz.belongingtogroupccompanies,
            pdz.blockkbe,
            pdz.comments,
            pdz.contractname,
            pdz.counterpartyinn,
            pdz.counterpartyname,
            pdz.inn_clean,
            pdz.name_clean,
            pdz.is_custom_contractor_dict,
            pdz.currentdebt,
            pdz.filename,
            pdz.groupdo,
            pdz.kinddebt,
            pdz.loaddate,
            pdz.modificationdate,
            pdz.num,
            pdz.ondate,
            pdz.overduedebt,
            pdz.overduedebtbetween5and30days,
            pdz.overduedebtless5days,
            pdz.overduedebtmore30days,
            pdz.daysOverdue,
            pdz.plannedrepaymentmonth,
            pdz.propotionoverduedebtintotaldebt,
            pdz.sheetname,
            pdz.subdepartmentowner,
            pdz.totaldebtwithoutreserve,
            m.name_do,
            pdz.id_sha2
        FROM {dbSchema}.union_pdz pdz
        left join {dbSchema}.white_list_do m
        on trim(pdz.groupdo)=m.string_name_do
        left join (
            select distinct inn_clean, code FROM  {dbSchema}.md_contractor_dict where inn_clean is not null and code is not null
        ) as md_1
        on pdz.inn_clean = md_1.inn_clean
        left join (
            select distinct name_clean, code FROM  {dbSchema}.md_contractor_dict where inn_clean is null and code is not null
        ) as md_2
        on pdz.name_clean = md_2.name_clean
        GROUP BY
            pdz.source,
            pdz.belongingtogroupccompanies,
            pdz.blockkbe,
            pdz.comments,
            pdz.contractname,
            pdz.counterpartyinn,
            pdz.counterpartyname,
            pdz.inn_clean,
            pdz.name_clean,
            pdz.is_custom_contractor_dict,
            pdz.currentdebt,
            pdz.filename,
            pdz.groupdo,
            pdz.kinddebt,
            pdz.loaddate,
            pdz.modificationdate,
            pdz.num,
            pdz.ondate,
            pdz.overduedebt,
            pdz.overduedebtbetween5and30days,
            pdz.overduedebtless5days,
            pdz.overduedebtmore30days,
            pdz.daysOverdue,
            pdz.plannedrepaymentmonth,
            pdz.propotionoverduedebtintotaldebt,
            pdz.sheetname,
            pdz.subdepartmentowner,
            pdz.totaldebtwithoutreserve,
            m.name_do,
            pdz.id_sha2
    ) r
) res
