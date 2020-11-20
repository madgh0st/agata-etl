SELECT DISTINCT res.*, m.name_do,
CASE WHEN
    res.name_clean LIKE '%ГАЗПРОМ%НЕФТЬ%' OR
    res.name_clean LIKE '%ГПНТ%СМАЗОЧНЫЕ%МАТЕРИАЛЫ%' OR 
    res.name_clean LIKE '%ГПН%-%'
THEN true ELSE false END AS in_black_list,
sha2(concat(
     coalesce(res.doName,'null'),
     coalesce(res.block,'null'),
     coalesce(res.counterpartyName,'null'),
     coalesce(res.counterpartyInn,'null'),
     coalesce(res.inn_clean,'null'),
     coalesce(res.name_clean,'null'),
     coalesce(res.is_custom_contractor_dict,false),
     coalesce(res.approvalAuthority,'null'),
     coalesce(res.approvalDocumentDetails,'null'),
     coalesce(res.collateral,'null'),
     coalesce(res.collateralComment,'null'),
     coalesce(res.limitCurrency,'null'),
     coalesce(res.limitInCurrency,'null'),
     coalesce(res.limitInRub,'null'),
     coalesce(res.loadDate,'null'),
     coalesce(res.paymentDefermentDays,'null'),
     coalesce(res.segment,'null'),
     coalesce(res.fileName,'null'),
     coalesce(res.sheetName,'null'),
     coalesce(res.reportDate,'null'),
     coalesce(res.approvalDate,'null'),
     coalesce(res.expirationDate,'null')
 ), 256) as id_sha2
FROM (
    SELECT
            trim(un.doName) as doName,
            un.block,
            un.counterpartyName,
            un.counterpartyInn,
            case
                when
                    coalesce(custom.inn_clean, un.counterpartyInn) is null
                    or
                    trim(coalesce(custom.inn_clean, un.counterpartyInn))='-'
                    or
                    trim(coalesce(custom.inn_clean, un.counterpartyInn))='0'
                    or
                    trim(coalesce(custom.inn_clean, un.counterpartyInn))='(пусто)'
                    then null
                else coalesce(custom.inn_clean, un.counterpartyInn)
            end as inn_clean,
	        coalesce(custom.name_clean_norm, udf_normalized(un.counterpartyName)) as name_clean,
	        CASE WHEN custom.inn_clean IS NOT NULL or custom.name_clean_norm is not null THEN true ELSE false END AS is_custom_contractor_dict,
            un.approvalAuthority,
            un.approvalDocumentDetails,
            un.collateral,
            un.collateralComment,
            un.limitCurrency,
            un.limitInCurrency,
            un.limitInRub,
            un.loadDate,
            un.modificationDate,
            un.paymentDefermentDays,
            un.segment,
            un.fileName,
            un.sheetName,
            udf_convert_to_local_date(coalesce(
                from_unixtime(unix_timestamp(un.reportDate, 'MM/dd/yy'), 'yyyy-MM-dd'),
                from_unixtime(unix_timestamp(un.reportDate, 'dd.MM.yyyy'), 'yyyy-MM-dd')
            )) as reportDate,
            udf_convert_to_local_date(coalesce(
                from_unixtime(unix_timestamp(un.approvalDate, 'MM/dd/yy'), 'yyyy-MM-dd'),
                from_unixtime(unix_timestamp(un.approvalDate, 'dd.MM.yyyy'), 'yyyy-MM-dd')
            )) as approvalDate,
            udf_convert_to_local_date(coalesce(
                from_unixtime(unix_timestamp(un.expirationDate, 'MM/dd/yy'), 'yyyy-MM-dd'),
                from_unixtime(unix_timestamp(un.expirationDate, 'dd.MM.yyyy'), 'yyyy-MM-dd')
            )) as expirationDate
     FROM (
          {union_replacer}
     ) as un
     left join {dbSchema}.md_contractor_custom_dict custom
     on un.counterpartyInn = custom.inn_raw and un.counterpartyName = custom.name_raw
) res
LEFT JOIN {dbSchema}.white_list_do m
ON res.doName = m.string_name_do