SELECT r.*,
       CASE
           WHEN r.code IS NULL
               THEN 'NOT_MAPPING'
           ELSE
               'OK'
           END AS error_status
FROM (
         SELECT max(md.code) AS code,
                fssp.name_do,
                fssp.fileName,
                fssp.sheetName,
                fssp.modificationDate,
                fssp.loadDate,
                fssp.debtorName,
                fssp.normDebtorName,
                fssp.proceedingInstitutionDate,
                fssp.executiveDocumentType,
                fssp.executiveDocumentDate,
                fssp.executiveDocumentObject,
                fssp.executionObject,
                fssp.debt,
                fssp.remainingDebt,
                fssp.dateAndCompletionReason,
                fssp.id_sha2
         FROM {dbSchema}.union_fssp AS fssp
             LEFT JOIN (SELECT DISTINCT code, name_clean FROM {dbSchema}.md_contractor_dict WHERE name_clean IS NOT NULL) md
         ON fssp.normDebtorName = md.name_clean
         GROUP BY
             fssp.name_do,
             fssp.fileName,
             fssp.sheetName,
             fssp.modificationDate,
             fssp.loadDate,
             fssp.id_sha2,
             fssp.debtorName,
             fssp.normDebtorName,
             fssp.proceedingInstitutionDate,
             fssp.executiveDocumentType,
             fssp.executiveDocumentDate,
             fssp.executiveDocumentObject,
             fssp.executionObject,
             fssp.debt,
             fssp.remainingDebt,
             fssp.dateAndCompletionReason
     ) AS r
