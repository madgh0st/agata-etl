SELECT r.*, CASE WHEN r.code IS NULL THEN 'NOT_MAPPING' ELSE 'OK' END AS error_status
FROM (
         SELECT max(md_1.code) AS code,
                union_cc.id_sha2,
                union_cc.fileName,
                union_cc.sheetName,
                union_cc.modificationDate,
                union_cc.loadDate,
                union_cc.eventDate,
                union_cc.name_do,
                union_cc.ownerCode,
                union_cc.erpCode,
                union_cc.number,
                union_cc.name,
                union_cc.date,
                union_cc.inn,
                union_cc.validityPeriod,
                union_cc.contractKind,
                union_cc.settlementCurrency,
                union_cc.settlementManagement,
                union_cc.markRemove,
                union_cc.paymentTerm
         FROM {dbSchema}.union_counterparty_contract union_cc
             LEFT JOIN (SELECT DISTINCT code, inn_clean FROM {dbSchema}.md_contractor_dict WHERE inn_clean IS NOT NULL) md_1
         ON union_cc.inn = md_1.inn_clean
         GROUP BY
             union_cc.id_sha2,
             union_cc.fileName,
             union_cc.sheetName,
             union_cc.modificationDate,
             union_cc.loadDate,
             union_cc.eventDate,
             union_cc.name_do,
             union_cc.ownerCode,
             union_cc.erpCode,
             union_cc.number,
             union_cc.name,
             union_cc.date,
             union_cc.inn,
             union_cc.validityPeriod,
             union_cc.contractKind,
             union_cc.settlementCurrency,
             union_cc.settlementManagement,
             union_cc.markRemove,
             union_cc.paymentTerm
     ) r