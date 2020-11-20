select distinct
    coalesce(custom.inn_clean, d.inn) as inn_clean,
    d.name_orig,
    coalesce(custom.name_clean_norm, d.name) as name_clean,
    d.erpcode,
    d.ogrn,
    d.kpp
    from (
        select distinct r.*
        from (
            select
            inn,
            name_orig,
            name,
            erpcode,
            ogrn,
            kpp
            from {dbSchema}.md_union_1c_dict
            union
            select
            inn,
            fullname_orig as name_orig,
            fullname as name,
            erpcode,
            ogrn,
            kpp
            from {dbSchema}.md_union_1c_dict
        ) r
        where r.name not like '%НЕ ИСПОЛЬЗ%'
) d
left join {dbSchema}.md_contractor_custom_dict custom
on d.inn = custom.inn_raw and d.name_orig = custom.name_raw