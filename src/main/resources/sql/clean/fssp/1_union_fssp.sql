WITH union_fssp AS (
    SELECT x.*
    FROM (
             SELECT DISTINCT 'sort_do' AS name_do,
                             fileName,
                             sheetName,
                             modificationDate,
                             loadDate,
                             debtorName,
                             proceedingInstitutionDate,
                             executiveDocumentType,
                             executiveDocumentDate,
                             executiveDocumentObject,
                             executionObject,
                             debt,
                             remainingDebt,
                             dateAndCompletionReason
             FROM {dbSchema}.fssp_sort_do
         ) x
)
SELECT res.*,
       sha2(
               concat(
                       coalesce(res.name_do, 'null'),
                       coalesce(res.fileName, 'null'),
                       coalesce(res.sheetName, 'null'),
                       coalesce(res.modificationDate, 'null'),
                       coalesce(res.loadDate, 'null'),
                       coalesce(res.debtorName, 'null'),
                       coalesce(res.proceedingInstitutionDate, 'null'),
                       coalesce(res.executiveDocumentType, 'null'),
                       coalesce(res.executiveDocumentDate, 'null'),
                       coalesce(res.executiveDocumentObject, 'null'),
                       coalesce(res.executionObject, 'null'),
                       coalesce(res.debt, 'null'),
                       coalesce(res.remainingDebt, 'null'),
                       coalesce(res.dateAndCompletionReason, 'null')
                   ), 256) AS id_sha2
FROM (
         SELECT name_do,
                fileName,
                sheetName,
                modificationDate,
                loadDate,
                debtorName,
                udf_normalized(debtorName) AS normDebtorName,
                proceedingInstitutionDate,
                executiveDocumentType,
                executiveDocumentDate,
                executiveDocumentObject,
                executionObject,
                debt,
                remainingDebt,
                dateAndCompletionReason
         FROM union_fssp
     ) res
