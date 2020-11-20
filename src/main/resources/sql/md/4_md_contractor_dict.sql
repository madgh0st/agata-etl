SELECT distinct
    case
        when res.inn_clean is not null then sha2(res.inn_clean, 256)
        when res.name_clean is not null and res.inn_clean is null then sha2(res.name_clean, 256)
        else null
    end as code,
    res.counterpartyName as name_orig,
    res.counterpartyInn as inn_orig,
    res.name_clean,
    res.inn_clean,
    res.is_custom_contractor_dict,
    res.name_do,
    res.inn_1c,
    res.name_orig_1c,
    res.name_1c,
    res.erpcode,
    res.ogrn,
    res.kpp,
    case when res.pdz_because_of_banks is null then false else true end as is_pdz_because_of_banks
FROM (
    select distinct
        CL.counterpartyName,
        CL.counterpartyInn,
        CL.name_clean,
        CL.inn_clean,
        CL.is_custom_contractor_dict,
        CL.name_do,
        coalesce(d1.inn_clean, d2.inn_clean, d3.inn_clean) as inn_1c,
        coalesce(d1.name_orig, d2.name_orig, d3.name_orig) as name_orig_1c,
        coalesce(d1.name_clean, d2.name_clean, d3.name_clean) as name_1c,
        coalesce(d1.erpcode, d2.erpcode, d3.erpcode) as erpcode,
        coalesce(d1.ogrn, d2.ogrn, d3.ogrn) as ogrn,
        coalesce(d1.kpp, d2.kpp, d3.kpp) as kpp,
        b.name_clean as pdz_because_of_banks
    FROM (
        select distinct
               counterpartyName,
               counterpartyInn,
               name_clean,
               inn_clean,
               is_custom_contractor_dict,
               name_do
        from {dbSchema}.union_credit_limit
        where name_do is not null and in_black_list = false
    ) CL
    LEFT JOIN (
        select * from {dbSchema}.md_1c_to_flat where inn_clean is not null and name_clean is not null
    ) d1
    ON CL.inn_clean = d1.inn_clean and CL.name_clean = d1.name_clean
    LEFT JOIN (
        select * from {dbSchema}.md_1c_to_flat where inn_clean is not null
    ) d2
    ON CL.inn_clean = d2.inn_clean
    LEFT JOIN (
        select * from {dbSchema}.md_1c_to_flat where inn_clean is null and name_clean is not null
    ) d3
    ON CL.name_clean = d3.name_clean
    left join {dbSchema}.union_pdz_because_of_banks b
    on CL.name_clean=b.name_clean
) res


