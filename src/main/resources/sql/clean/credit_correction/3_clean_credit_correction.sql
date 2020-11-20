SELECT udf_convert_to_local_date(from_unixtime((unix_timestamp(x.docDate, 'dd.MM.yyyy')),'yyyy-MM-dd')) as eventdate, x.*
FROM {dbSchema}.collisions_report_credit_correction x
WHERE x.error_status = 'OK'
