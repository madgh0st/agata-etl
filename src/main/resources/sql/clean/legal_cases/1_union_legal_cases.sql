with union_legal_cases as (
  select x.*
  from (
    select distinct 'sort_do' as name_do, claimdate, inn, name, namespark, outcomedate, casenumber, category, claimAmount, claimcharge, contractorid, dictum, outcome, outcomeamount, side, status, sheetname, loaddate, modificationdate, filename
    from {dbSchema}.legal_cases_sort_do
  ) x
)
SELECT res.*, sha2(concat(
             coalesce(res.inn,'null'),
             coalesce(res.name,'null'),
             coalesce(res.norm_name,'null'),
             coalesce(res.namespark,'null'),
             coalesce(res.norm_namespark,'null'),
             coalesce(res.name_do,'null'),
             coalesce(res.claimdate,'null'),
             coalesce(res.outcomedate,'null'),
             coalesce(res.casenumber,'null'),
             coalesce(res.category,'null'),
             coalesce(res.claimAmount,'null'),
             coalesce(res.claimcharge,'null'),
             coalesce(res.contractorid,'null'),
             coalesce(res.dictum,'null'),
             coalesce(res.outcome,'null'),
             coalesce(res.outcomeamount,'null'),
             coalesce(res.side,'null'),
             coalesce(res.status,'null'),
             coalesce(res.sheetname,'null'),
             coalesce(res.loaddate,'null'),
             coalesce(res.filename,'null')
         ), 256) as id_sha2
FROM (
    select
      inn,
      name,
      udf_normalized(name) as norm_name,
      namespark,
      udf_normalized(namespark) as norm_namespark,
      name_do,
      claimdate,
      outcomedate,
      casenumber,
      category,
      claimAmount,
      claimcharge,
      contractorid,
      dictum,
      outcome,
      outcomeamount,
      side,
      status,
      sheetname,
      loaddate,
      modificationdate,
      filename
    from union_legal_cases
) res
