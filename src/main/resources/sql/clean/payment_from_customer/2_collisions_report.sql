SELECT
r.*,
case when r.code is null then 'NOT_MAPPING' else 'OK' end as error_status,
from_unixtime(unix_timestamp()) AS ts_update
FROM (
    SELECT
        max(y.code) as code,
        y.name_do,
        y.paymentDate,
        y.number,
        y.operationType,
        y.amount,
        y.currency,
        y.organisationBankingAccount,
        y.organisation,
        y.incomingDocNumber,
        y.incomingDocDate,
        y.cashFlowLine,
        y.isPayed,
        y.counterpartyCode,
        y.contractCode,
        y.comment,
        y.baseDoc,
        y.paymentReference,
        y.baseDoc2,
        y.organisation1,
        y.posted,
        y.deleted,
        y.cashFlowLineLocal,
        y.counterparty,
        y.paymentID,
        y.contractNumber,
        y.fileName,
        y.sheetName,
        y.modificationDate,
        y.loadDate,
        y.id_sha2
    FROM (
        SELECT m.*, d.code
        FROM (
            SELECT *, udf_normalized(counterparty) as name_clean FROM {dbSchema}.union_payment
        ) m
        LEFT JOIN (
            select distinct code, name_do, erpcode, name_clean from {dbSchema}.md_contractor_dict where length(erpcode)>0 and code is not null
        ) d
        on m.name_do=d.name_do and m.counterpartyCode=d.erpcode and m.name_clean=d.name_clean
    ) y
    GROUP BY
        y.name_do,
        y.paymentDate,
        y.number,
        y.operationType,
        y.amount,
        y.currency,
        y.organisationBankingAccount,
        y.organisation,
        y.incomingDocNumber,
        y.incomingDocDate,
        y.cashFlowLine,
        y.isPayed,
        y.counterpartyCode,
        y.contractCode,
        y.comment,
        y.baseDoc,
        y.paymentReference,
        y.baseDoc2,
        y.organisation1,
        y.posted,
        y.deleted,
        y.cashFlowLineLocal,
        y.counterparty,
        y.paymentID,
        y.contractNumber,
        y.fileName,
        y.sheetName,
        y.modificationDate,
        y.loadDate,
        y.id_sha2
) r
