SELECT res.*, case when res.code is null then 'NOT_MAPPING' else 'OK' end as error_status
FROM (
    SELECT
    r.*,
    from_unixtime(unix_timestamp()) AS ts_update
    FROM (
        SELECT
            max(y.code) as code,
            y.name_do,
            y.docDate,
            y.counterparty,
            y.contract,
            y.amount,
            y.currency,
            y.number,
            y.operationType,
            y.counterpartyCode,
            y.contractCode,
            y.comment,
            y.baseDoc,
            y.noSettlementControl,
            y.organisation,
            y.posted,
            y.deleted,
            y.affectsAccounting,
            y.affectsManagement,
            y.exportSelling,
            y.fileName,
            y.sheetName,
            y.modificationDate,
            y.loadDate,
            y.id_sha2
        FROM (
            SELECT m.*, d.code
            FROM (
                SELECT *, udf_normalized(counterparty) as name_clean  FROM {dbSchema}.union_realization
            ) m
            LEFT JOIN (
                select distinct code, name_do, erpcode, name_clean from {dbSchema}.md_contractor_dict where length(erpcode)>0 and code is not null
            ) d
            on m.name_do=d.name_do and m.counterpartyCode=d.erpcode  and m.name_clean=d.name_clean
        ) y
        GROUP BY
            y.name_do,
            y.docDate,
            y.counterparty,
            y.contract,
            y.amount,
            y.currency,
            y.number,
            y.operationType,
            y.counterpartyCode,
            y.contractCode,
            y.comment,
            y.baseDoc,
            y.noSettlementControl,
            y.organisation,
            y.posted,
            y.deleted,
            y.affectsAccounting,
            y.affectsManagement,
            y.exportSelling,
            y.fileName,
            y.sheetName,
            y.modificationDate,
            y.loadDate,
            y.id_sha2
    ) r
) res
