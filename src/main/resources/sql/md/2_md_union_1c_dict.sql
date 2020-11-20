SELECT distinct
    d.name_do_table as name_do,
    d.deletemark,
    d.erpcode,
    d.fullname as fullname_orig,
    udf_normalized(d.fullname) as fullname,
    d.gpnclass,
    d.inn,
    d.isbuyer,
    d.isforeignresident,
    d.issupplier,
    d.kpp,
    d.name as name_orig,
    udf_normalized(d.name) as name,
    d.ogrn,
    d.persontype,
    d.region,
    d.status
FROM (
    {union_replacer}
) d
