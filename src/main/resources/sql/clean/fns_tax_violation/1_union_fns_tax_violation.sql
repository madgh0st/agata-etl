with union_fns_tax_violation as (
    select x.*
    from (
             select distinct 'sort_do' as name_do,
                             fileName,
                             sheetName,
                             modificationDate,
                             loadDate,
                             docId,
                             docDate,
                             creationDate,
                             orgName,
                             innul,
                             fine
             from {dbSchema}.fns_tax_violation_sort_do
         ) x
)
SELECT res.*,
       sha2(concat(
                    coalesce(res.name_do, 'null'),
                    coalesce(res.fileName, 'null'),
                    coalesce(res.sheetName, 'null'),
                    coalesce(res.modificationDate, 'null'),
                    coalesce(res.loadDate, 'null'),
                    coalesce(res.docId, 'null'),
                    coalesce(res.docDate, 'null'),
                    coalesce(res.creationDate, 'null'),
                    coalesce(res.orgName, 'null'),
                    coalesce(res.innul, 'null'),
                    coalesce(res.fine, 'null')
                ), 256) as id_sha2
FROM (
     select name_do,
            fileName,
            sheetName,
            modificationDate,
            loadDate,
            docId,
            docDate,
            creationDate,
            orgName,
            udf_normalized(orgName) as normOrgName,
            innul,
            fine
     from union_fns_tax_violation
) res
