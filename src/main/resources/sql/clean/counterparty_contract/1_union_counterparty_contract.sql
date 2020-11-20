SELECT res.*,
       sha2(concat(
                    coalesce(res.fileName, 'null'),
                    coalesce(res.sheetName, 'null'),
                    coalesce(res.loadDate, 'null'),
                    coalesce(res.eventDate, 'null'),
                    coalesce(res.name_do, 'null'),
                    coalesce(res.ownerCode, 'null'),
                    coalesce(res.erpCode, 'null'),
                    coalesce(res.number, 'null'),
                    coalesce(res.name, 'null'),
                    coalesce(res.date, 'null'),
                    coalesce(res.inn, 'null'),
                    coalesce(res.validityPeriod, 'null'),
                    coalesce(res.contractKind, 'null'),
                    coalesce(res.settlementCurrency, 'null'),
                    coalesce(res.settlementManagement, 'null'),
                    coalesce(res.markRemove, 'null'),
                    coalesce(res.paymentTerm, 'null')
                ), 256) AS id_sha2
FROM (
         SELECT sub.fileName,
                sub.sheetName,
                sub.modificationDate,
                sub.loadDate,
                udf_convert_to_local_date(
                        coalesce(
                                from_unixtime(unix_timestamp(sub.date, 'MM/dd/yy'), 'yyyy-MM-dd'),
                                from_unixtime(unix_timestamp(sub.date, 'dd.MM.yyyy'), 'yyyy-MM-dd')
                            )
                    )             AS eventDate,
                sub.name_do_table AS name_do,
                sub.ownerCode,
                sub.erpCode,
                sub.number,
                sub.name,
                sub.date,
                sub.inn,
                sub.validityPeriod,
                sub.contractKind,
                sub.settlementCurrency,
                sub.settlementManagement,
                sub.markRemove,
                sub.paymentTerm
         FROM ({union_replacer}) AS sub
     ) res