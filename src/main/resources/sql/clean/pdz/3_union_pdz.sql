with weekly_and_monthly as (
	select
        'weekly' as source,
        'null' as belongingtogroupccompanies,
        pdz.blockkbe,
        pdz.comments,
        pdz.contractname,
        pdz.counterpartyname,
        pdz.counterpartyInn,
        case
            when
                coalesce(custom.inn_clean, pdz.counterpartyInn) is null
                or
                trim(coalesce(custom.inn_clean, pdz.counterpartyInn))='-'
                or
                trim(coalesce(custom.inn_clean, pdz.counterpartyInn))='0'
                or
                trim(coalesce(custom.inn_clean, pdz.counterpartyInn))='(пусто)'
                then null
            else coalesce(custom.inn_clean, pdz.counterpartyInn)
        end as inn_clean,
        coalesce(custom.name_clean_norm, udf_normalized(pdz.counterpartyName)) as name_clean,
        CASE WHEN custom.inn_clean IS NOT NULL or custom.name_clean_norm is not null THEN true ELSE false END AS is_custom_contractor_dict,
        pdz.currentdebt,
        pdz.filename,
        pdz.groupdo,
        'null' as kinddebt,
        pdz.loaddate,
        pdz.modificationdate,
        pdz.num,
        pdz.ondate,
        pdz.overduedebt,
        pdz.overduedebtbetween5and30days,
        pdz.overduedebtless5days,
        pdz.overduedebtmore30days,
        pdz.daysOverdue,
        'null' as plannedrepaymentmonth,
        'null' as propotionoverduedebtintotaldebt,
        pdz.sheetname,
        'null' as subdepartmentowner,
        pdz.totaldebtwithoutreserve
	from {dbSchema}.union_pdz_weekly pdz
	left join {dbSchema}.md_contractor_custom_dict custom
    on pdz.counterpartyinn = custom.inn_raw and pdz.counterpartyname = custom.name_raw
)
select res.*, sha2(concat(
        coalesce(res.source,'null'),
        coalesce(res.belongingtogroupccompanies,'null'),
        coalesce(res.blockkbe,'null'),
        coalesce(res.comments,'null'),
        coalesce(res.contractname,'null'),
        coalesce(res.counterpartyinn,'null'),
        coalesce(res.counterpartyname,'null'),
        coalesce(res.inn_clean,'null'),
        coalesce(res.name_clean,'null'),
        coalesce(res.is_custom_contractor_dict,false),
        coalesce(res.currentdebt,'null'),
        coalesce(res.filename,'null'),
        coalesce(res.groupdo,'null'),
        coalesce(res.kinddebt,'null'),
        coalesce(res.loaddate,'null'),
        coalesce(res.num,'null'),
        coalesce(res.ondate,'null'),
        coalesce(res.overduedebt,'null'),
        coalesce(res.overduedebtbetween5and30days,'null'),
        coalesce(res.overduedebtless5days,'null'),
        coalesce(res.overduedebtmore30days,'null'),
        coalesce(res.daysOverdue,'null'),
        coalesce(res.plannedrepaymentmonth,'null'),
        coalesce(res.propotionoverduedebtintotaldebt,'null'),
        coalesce(res.sheetname,'null'),
        coalesce(res.subdepartmentowner,'null'),
        coalesce(res.totaldebtwithoutreserve,'null')
     ), 256) as id_sha2
from weekly_and_monthly res
