SELECT r.*, CASE WHEN r.code IS NULL THEN 'NOT_MAPPING' ELSE 'OK' END AS error_status
FROM (
         SELECT max(md.code) AS code,
                genproc.name_do,
                genproc.fileName,
                genproc.sheetName,
                genproc.modificationDate,
                genproc.loadDate,
                genproc.companyName,
                genproc.normCompanyName,
                genproc.inn,
                genproc.decisionDate,
                genproc.id_sha2
         FROM {dbSchema}.union_genproc genproc
             LEFT JOIN (SELECT DISTINCT code, inn_clean FROM {dbSchema}.md_contractor_dict WHERE inn_clean IS NOT null) md
         ON genproc.inn = md.inn_clean
         GROUP BY
             genproc.name_do,
             genproc.fileName,
             genproc.sheetName,
             genproc.modificationDate,
             genproc.loadDate,
             genproc.companyName,
             genproc.normCompanyName,
             genproc.inn,
             genproc.decisionDate,
             genproc.id_sha2
     ) r
