select r.*, case when r.code is null then 'NOT_MAPPING' else 'OK' end as error_status
from (
    select
        max(md_1.code) as code,
        fns.name_do,
        fns.fileName,
        fns.sheetName,
        fns.modificationDate,
        fns.loadDate,
        fns.docId,
        fns.docDate,
        fns.creationDate,
        fns.orgName,
        fns.normOrgName,
        fns.innul,
        fns.taxName,
        fns.taxArrears,
        fns.penalties,
        fns.arrearsFine,
        fns.totalArrears,
        fns.id_sha2
    from {dbSchema}.union_fns_arrears fns
    left join (select distinct code, inn_clean FROM {dbSchema}.md_contractor_dict where inn_clean is not null) md_1
    on fns.innul = md_1.inn_clean
    group by
        fns.name_do,
        fns.fileName,
        fns.sheetName,
        fns.modificationDate,
        fns.loadDate,
        fns.docId,
        fns.docDate,
        fns.creationDate,
        fns.orgName,
        fns.normOrgName,
        fns.innul,
        fns.taxName,
        fns.taxArrears,
        fns.penalties,
        fns.arrearsFine,
        fns.totalArrears,
        fns.id_sha2
) r