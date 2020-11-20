SELECT genproc.code,
       udf_convert_to_local_date(
               from_unixtime((unix_timestamp(genproc.decisionDate, 'dd.MM.yyyy')), 'yyyy-MM-dd')) AS eventdate,
       genproc.sheetname,
       unions.name_do,
       genproc.loaddate,
       genproc.modificationdate,
       genproc.filename,
       genproc.error_status,
       genproc.companyName,
       genproc.normCompanyName,
       genproc.id_sha2
FROM (
         SELECT *
         FROM {dbSchema}.collisions_report_genproc
         WHERE error_status = 'OK'
     ) genproc
         CROSS JOIN ( {union_replacer}
) unions
