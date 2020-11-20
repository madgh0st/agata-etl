SELECT x.code,
       x.eventdate,
       x.name                                                    AS counterpartyName,
       x.norm_name,
       x.inn                                                     AS counterpartyInn,
       s.name_do,
       CASE
           WHEN x.status = 'Действующая' OR x.status = 'Действующее' THEN 'ACTIVE'
           WHEN x.status = 'Недействующая' THEN 'NOT_ACTIVE'
           WHEN x.status = 'Реорганизуется' THEN 'REORGANIZATION'
           WHEN x.status = 'Ликвидируется' THEN 'LIQUIDATION'
           WHEN x.status = 'Ликвидированное' THEN 'LIQUIDATED'
           WHEN x.status = 'В состоянии банкротства' THEN 'BANKRUPT'
           END                                                   AS status,
       cast(regexp_replace(if(x.sparkInterfaxCreditLimit = 'Менее 100000', '50000', x.sparkInterfaxCreditLimit),
                           ' ', '') AS DOUBLE)                   AS sparkInterfaxCreditLimit,
       cast(x.cautionIndex AS INT)                               AS cautionIndex,
       cast(x.financialRiskIndex AS INT)                         AS financialRiskIndex,
       cast(x.paymentDisciplineIndex AS INT)                     AS paymentDisciplineIndex,
       CASE
           WHEN x.riskFactors = 'Низкий риск' THEN 'LOW'
           WHEN x.riskFactors = 'Средний риск' THEN 'MEDIUM'
           WHEN x.riskFactors = 'Высокий риск' THEN 'HIGH'
           END                                                   AS riskFactors,
       x.negativeRegistries,
       CASE
           WHEN x.pledges = 'Есть' THEN TRUE
           WHEN x.pledges = 'Нет' THEN FALSE
           END                                                   AS pledges,
       cast(x.legalCasesCountTwoYears AS INT)                    AS legalCasesCountTwoYears,
       cast(x.legalCasesClaimsSumTwoYears AS DOUBLE)             AS legalCasesClaimsSumTwoYears,
       cast(x.legalCasesDecisionsSumTwoYears AS DOUBLE)          AS legalCasesDecisionsSumTwoYears,
       concat(x.news1, ',', x.news2, ',', x.news3, ',', x.news4) AS news,
       CASE
           WHEN x.isDishonestSupplier IS NULL THEN NULL
           WHEN x.isDishonestSupplier = 'В реестре не значится' THEN FALSE
           ELSE TRUE
           END                                                   AS isDishonestSupplier,
       x.sheetname,
       x.loaddate,
       x.modificationdate,
       x.filename,
       x.error_status,
       x.id_sha2
FROM (
         SELECT r.*
         FROM {dbSchema}.collisions_report_spark_interfax_summary r
         WHERE r.error_status = 'OK'
     ) x
         CROSS JOIN ( {union_replacer}
) s
