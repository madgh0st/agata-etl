SELECT res.*,
    case
        when
            res.in_black_list=false
            and
            res.in_white_list=true
            and
            res.is_corrupted_field=false
            and
            res.name_do is not null
            and
            res.code is not null
        then 'OK'
        else 'NOT_MAPPING'
    end as error_status
FROM (
    select t.*,
    coalesce(t.md_1_code, t.md_2_code) as code,
    from_unixtime(unix_timestamp()) AS ts_update,
    isnotnull(t.name_do) as in_white_list,
    CASE
      WHEN t.reportDate IS NULL THEN true
      WHEN t.limitInRub IS NULL THEN true
      ELSE false
    END AS is_corrupted_field
    FROM (
        SELECT
              q.name_do,
              q.in_black_list,
              q.id_sha2,
              q.doName,
              q.block,
              q.counterpartyName,
              q.counterpartyInn,
              q.inn_clean,
              q.name_clean,
              q.is_custom_contractor_dict,
              q.approvalAuthority,
              q.approvalDocumentDetails,
              q.collateral,
              q.collateralComment,
              q.limitCurrency,
              q.limitInCurrency,
              q.limitInRub,
              q.loadDate,
              q.modificationDate,
              q.paymentDefermentDays,
              q.segment,
              q.fileName,
              q.sheetName,
              q.reportDate,
              q.approvalDate,
              q.expirationDate,
              max(md_1.code) as md_1_code,
              max(md_2.code) as md_2_code
        FROM {dbSchema}.union_credit_limit q
        left join (
            select distinct inn_clean, code FROM  {dbSchema}.md_contractor_dict where inn_clean is not null and code is not null
        ) as md_1
        on q.inn_clean = md_1.inn_clean
        left join (
            select distinct name_clean, code FROM  {dbSchema}.md_contractor_dict where inn_clean is null and code is not null
        ) as md_2
        on q.name_clean = md_2.name_clean
        GROUP BY
              q.name_do,
              q.in_black_list,
              q.id_sha2,
              q.doName,
              q.block,
              q.counterpartyName,
              q.counterpartyInn,
              q.inn_clean,
              q.name_clean,
              q.is_custom_contractor_dict,
              q.approvalAuthority,
              q.approvalDocumentDetails,
              q.collateral,
              q.collateralComment,
              q.limitCurrency,
              q.limitInCurrency,
              q.limitInRub,
              q.loadDate,
              q.modificationDate,
              q.paymentDefermentDays,
              q.segment,
              q.fileName,
              q.sheetName,
              q.reportDate,
              q.approvalDate,
              q.expirationDate
    ) t
) res