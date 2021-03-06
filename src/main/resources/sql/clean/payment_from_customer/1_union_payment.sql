SELECT res.*, sha2(concat(
            coalesce(res.fileName,'null'),
            coalesce(res.sheetName,'null'),
            coalesce(res.loadDate,'null'),
            coalesce(res.eventDate,'null'),
            coalesce(res.name_do,'null'),
            coalesce(res.amount,'null'),
            coalesce(res.baseDoc,'null'),
            coalesce(res.baseDoc2,'null'),
            coalesce(res.cashFlowLine,'null'),
            coalesce(res.cashFlowLineLocal,'null'),
            coalesce(res.comment,'null'),
            coalesce(res.contractCode,'null'),
            coalesce(res.contractNumber,'null'),
            coalesce(res.counterparty,'null'),
            coalesce(res.counterpartyCode,'null'),
            coalesce(res.currency,'null'),
            coalesce(res.deleted,'null'),
            coalesce(res.incomingDocDate,'null'),
            coalesce(res.incomingDocNumber,'null'),
            coalesce(res.isPayed,'null'),
            coalesce(res.number,'null'),
            coalesce(res.operationType,'null'),
            coalesce(res.organisation,'null'),
            coalesce(res.organisation1,'null'),
            coalesce(res.organisationBankingAccount,'null'),
            coalesce(res.paymentDate,'null'),
            coalesce(res.paymentID,'null'),
            coalesce(res.paymentReference,'null'),
            coalesce(res.posted,'null')
         ), 256) as id_sha2
FROM (
    SELECT
           sub.fileName,
           sub.sheetName,
           sub.modificationDate,
           sub.loadDate,
           sub.eventDate,
           sub.name_do_table AS name_do,
           sub.amount,
           sub.baseDoc,
           sub.baseDoc2,
           sub.cashFlowLine,
           sub.cashFlowLineLocal,
           sub.comment,
           sub.contractCode,
           sub.contractNumber,
           sub.counterparty,
           sub.counterpartyCode,
           sub.currency,
           sub.deleted,
           sub.incomingDocDate,
           sub.incomingDocNumber,
           sub.isPayed,
           sub.number,
           sub.operationType,
           sub.organisation,
           sub.organisation1,
           sub.organisationBankingAccount,
           sub.paymentDate,
           sub.paymentID,
           sub.paymentReference,
           sub.posted
    FROM (
      {union_replacer}
    ) AS sub
) res
