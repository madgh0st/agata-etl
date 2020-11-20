select
  x.code,
  udf_convert_to_local_date(from_unixtime((unix_timestamp(x.docDate, 'dd.MM.yyyy')),'yyyy-MM-dd')) as eventdate,
  x.orgName as counterpartyName,
  x.innul as counterpartyInn,
  x.fine,
  x.sheetname,
  s.name_do,
  x.loaddate,
  x.modificationdate,
  x.filename,
  x.error_status,
  x.id_sha2
from (
  select r.*
  from {dbSchema}.collisions_report_fns_tax_violation r
  WHERE r.error_status = 'OK'
) x
cross join (
  {union_replacer}
) s
