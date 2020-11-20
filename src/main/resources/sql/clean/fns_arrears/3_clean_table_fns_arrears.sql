select
  x.code,
  udf_convert_to_local_date(from_unixtime((unix_timestamp(x.creationDate, 'dd.MM.yyyy')),'yyyy-MM-dd')) as eventdate,
  x.orgName as counterpartyName,
  x.innul as counterpartyInn,
  x.sheetname,
  s.name_do,
  x.loaddate,
  x.modificationdate,
  x.filename,
  x.error_status,
  x.id_sha2,
  x.taxName,
  x.taxArrears,
  x.penalties,
  x.arrearsFine,
  x.totalArrears
from (
  select r.*
  from {dbSchema}.collisions_report_fns_arrears r
  WHERE r.error_status = 'OK'
) x
cross join (
  {union_replacer}
) s
