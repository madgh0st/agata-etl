SELECT fns_disq.code,
       cast(fns_disq.eventdate AS DATE) as eventdate,
       unions.name_do,
       fns_disq.fileName,
       fns_disq.sheetName,
       fns_disq.loadDate,
       fns_disq.organizationFullName,
       fns_disq.normOrganizationFullName,
       fns_disq.ogrn,
       fns_disq.inn,
       fns_disq.kpp,
       fns_disq.address,
       fns_disq.id_sha2
FROM (
         SELECT *
         FROM {dbSchema}.collisions_report_fns_disq
         WHERE error_status = 'OK'
     ) fns_disq
         CROSS JOIN ( {union_replacer}
) unions
