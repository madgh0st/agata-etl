select
code,
error_status,
udf_convert_to_local_date(coalesce (
    from_unixtime((unix_timestamp(ondate, 'MM/dd/yy')),'yyyy-MM-dd'),
    from_unixtime((unix_timestamp(ondate, 'dd.MM.yyyy')),'yyyy-MM-dd')
)) as eventdate,
source,
name_do,
belongingtogroupccompanies,
blockkbe,
coalesce(cast(overdueDebtLess5Days as double),0) as overdueDebtLess5Days,
comments,
contractname,
counterpartyinn,
counterpartyname,
inn_clean,
name_clean,
is_custom_contractor_dict,
coalesce(cast(currentdebt as double),0) AS currentdebt,
filename,
groupdo,
kinddebt,
loaddate,
modificationdate,
num,
ondate,
coalesce(cast(overduedebt as double),0) as overduedebt,
coalesce(cast(overduedebtbetween5and30days as double),0) AS overduedebtbetween5and30days,
coalesce(cast(overduedebtmore30days as double),0) AS overduedebtmore30days,
cast(daysoverdue as integer) as daysoverdue,
plannedrepaymentmonth,
propotionoverduedebtintotaldebt,
sheetname,
subdepartmentowner,
coalesce(cast(totaldebtwithoutreserve as double),0) AS totaldebtwithoutreserve,
ts_update,
id_sha2,
CASE
  WHEN
    cast(overdueDebtLess5Days as double) IS NULL or
    cast(currentdebt as double) IS NULL or
    cast(overduedebt as double) IS NULL or
    cast(overduedebtbetween5and30days as double) IS NULL or
    cast(overduedebtmore30days as double) IS NULL or
    cast(totaldebtwithoutreserve as double) IS NULL
  THEN false
  ELSE true
END AS is_valid
from {dbSchema}.collisions_report_pdz
where counterpartyname!='x' and error_status = 'OK'