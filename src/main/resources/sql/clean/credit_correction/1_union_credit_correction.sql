SELECT
    res.*, sha2(concat(
            coalesce(res.fileName,'null'),
            coalesce(res.sheetName,'null'),
            coalesce(res.loadDate,'null'),
            coalesce(res.eventDate,'null'),
            coalesce(res.name_do,'null'),
            coalesce(res.affectsAccounting,'null'),
            coalesce(res.affectsManagement,'null'),
            coalesce(res.amount,'null'),
            coalesce(res.baseDoc,'null'),
            coalesce(res.comment,'null'),
            coalesce(res.contractCode,'null'),
            coalesce(res.counterpartyCode,'null'),
            coalesce(res.currency,'null'),
            coalesce(res.debitorContractCode,'null'),
            coalesce(res.debitorContractorCode,'null'),
            coalesce(res.debtWritOff,'null'),
            coalesce(res.deleted,'null'),
            coalesce(res.department,'null'),
            coalesce(res.docDate,'null'),
            coalesce(res.documentType,'null'),
            coalesce(res.isBeingPosted,'null'),
            coalesce(res.liabilityType,'null'),
            coalesce(res.number,'null'),
            coalesce(res.operationType,'null'),
            coalesce(res.organisation,'null'),
            coalesce(res.penaltyCorrection,'null'),
            coalesce(res.posted,'null')
        ), 256) as id_sha2
FROM (
    SELECT sub.fileName,
           sub.sheetName,
           sub.modificationDate,
           sub.loadDate,
           sub.eventDate,
           sub.name_do_table AS name_do,
           sub.affectsAccounting,
           sub.affectsManagement,
           sub.amount,
           sub.baseDoc,
           sub.comment,
           sub.contractCode,
           sub.counterpartyCode,
           sub.currency,
           sub.debitorContractCode,
           sub.debitorContractorCode,
           sub.debtWritOff,
           sub.deleted,
           sub.department,
           sub.docDate,
           sub.documentType,
           sub.isBeingPosted,
           sub.liabilityType,
           sub.number,
           sub.operationType,
           sub.organisation,
           sub.penaltyCorrection,
           sub.posted
    FROM (
          {union_replacer}
    ) AS sub
) res
