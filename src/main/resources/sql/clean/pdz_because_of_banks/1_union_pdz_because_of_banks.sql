SELECT res.*, sha2(concat(
         coalesce(res.counterpartyName,'null'),
         coalesce(res.name_clean,'null')
      ), 256) as id_sha2

FROM (
    select distinct trim(r.counterpartyName) as counterpartyName, udf_normalized(trim(r.counterpartyName)) as name_clean
    from (
    {union_replacer}
    ) r
) res
