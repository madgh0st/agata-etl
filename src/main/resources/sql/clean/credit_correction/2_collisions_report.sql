select res.*,
    case
    when res.count_code = 1 then 'OK'
    when res.max_code is null then 'NOT_MAPPING'
    when res.count_code > 1 then 'COLLISION'
    else 'OK'
    end as error_status
FROM (
    SELECT
        case when y.count_code = 1 then y.max_code else null end as code,
        y.*,
        from_unixtime(unix_timestamp()) AS ts_update
    FROM (
        SELECT
            max(r.code) as max_code,
            count(r.code) as count_code,
            r.name_do,
            r.documentType,
            r.number,
            r.docDate,
            r.posted,
            r.operationType,
            r.counterpartyCode,
            r.contractCode,
            r.amount,
            r.currency,
            r.comment,
            r.organisation,
            r.deleted,
            r.affectsAccounting,
            r.affectsManagement,
            r.debitorContractorCode,
            r.debitorContractCode,
            r.baseDoc,
            r.liabilityType,
            r.department,
            r.isBeingPosted,
            r.penaltyCorrection,
            r.debtWritOff,
            r.fileName,
            r.sheetName,
            r.modificationDate,
            r.loadDate,
            r.id_sha2
        FROM (
            SELECT d.code, m.*
            FROM {dbSchema}.union_credit_correction m
            LEFT JOIN (
                select distinct code, name_do, erpcode from {dbSchema}.md_contractor_dict where length(erpcode)>0 and code is not null
            ) d
            on m.name_do=d.name_do and m.counterpartyCode=d.erpcode
        ) r
        group by
            r.name_do,
            r.documentType,
            r.number,
            r.docDate,
            r.posted,
            r.operationType,
            r.counterpartyCode,
            r.contractCode,
            r.amount,
            r.currency,
            r.comment,
            r.organisation,
            r.deleted,
            r.affectsAccounting,
            r.affectsManagement,
            r.debitorContractorCode,
            r.debitorContractCode,
            r.baseDoc,
            r.liabilityType,
            r.department,
            r.isBeingPosted,
            r.penaltyCorrection,
            r.debtWritOff,
            r.fileName,
            r.sheetName,
            r.modificationDate,
            r.loadDate,
            r.id_sha2
    ) y
) res
