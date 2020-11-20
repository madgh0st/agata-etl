SELECT r.*, CASE WHEN r.code IS NULL THEN 'NOT_MAPPING' ELSE 'OK' END AS error_status
FROM (
         SELECT max(md.code) AS code,
                fns_disq.name_do,
                fns_disq.fileName,
                fns_disq.sheetName,
                fns_disq.eventdate,
                fns_disq.modificationDate,
                fns_disq.loadDate,
                fns_disq.organizationFullName,
                fns_disq.normOrganizationFullName,
                fns_disq.ogrn,
                fns_disq.inn,
                fns_disq.kpp,
                fns_disq.address,
                fns_disq.id_sha2
         FROM {dbSchema}.union_fns_disq fns_disq
             LEFT JOIN (SELECT DISTINCT code, inn_clean FROM {dbSchema}.md_contractor_dict WHERE inn_clean IS NOT null) md
         ON fns_disq.inn = md.inn_clean
         GROUP BY
             fns_disq.name_do,
             fns_disq.fileName,
             fns_disq.sheetName,
             fns_disq.eventdate,
             fns_disq.modificationDate,
             fns_disq.loadDate,
             fns_disq.organizationFullName,
             fns_disq.normOrganizationFullName,
             fns_disq.ogrn,
             fns_disq.inn,
             fns_disq.kpp,
             fns_disq.address,
             fns_disq.id_sha2
     ) r
