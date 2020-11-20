select
  x.code,
  udf_convert_to_local_date(from_unixtime((unix_timestamp(x.claimdate, 'MM/dd/yy')),'yyyy-MM-dd')) as eventdate,
  x.inn as counterpartyInn,
  x.name as counterpartyName,
  x.norm_name,
  x.namespark,
  x.norm_namespark,
  s.name_do,
  udf_convert_to_local_date(from_unixtime((unix_timestamp(x.claimdate, 'MM/dd/yy')),'yyyy-MM-dd')) as claimdate,
  udf_convert_to_local_date(from_unixtime((unix_timestamp(x.outcomedate, 'MM/dd/yy')),'yyyy-MM-dd')) as outcomedate,
  x.casenumber,
  x.category,
  cast(x.claimAmount as double) as claimAmount,
  x.claimcharge,
  x.contractorid,
  x.dictum,
  x.outcome,
  cast(x.outcomeamount as double) as outcomeamount,
  x.side,
  x.status,
  x.sheetname,
  x.loaddate,
  x.modificationdate,
  x.filename,
  x.error_status,
  x.id_sha2
from (
  select r.*
  from {dbSchema}.collisions_report_legal_cases r
  WHERE r.error_status = 'OK'
) x
cross join (
  {union_replacer}
) s
