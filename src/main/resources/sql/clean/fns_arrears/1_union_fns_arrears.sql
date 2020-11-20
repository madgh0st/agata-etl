with union_fns_arrears as (
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
         taxName,
         taxArrears,
         penalties,
         arrearsFine,
         totalArrears
     from {dbSchema}.fns_arrears_sort_do
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
                    coalesce(res.taxName, 'null'),
                    coalesce(res.taxArrears, 'null'),
                    coalesce(res.penalties, 'null'),
                    coalesce(res.arrearsFine, 'null'),
                    coalesce(res.totalArrears, 'null')
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
            taxName,
            taxArrears,
            penalties,
            arrearsFine,
            totalArrears
     from union_fns_arrears
 ) res
