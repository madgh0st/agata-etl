SELECT
    y.code,
    udf_convert_to_local_date(coalesce(
        from_unixtime(unix_timestamp(y.eventdate_non_cast, 'dd.MM.yyyy'), 'yyyy-MM-dd'),
        from_unixtime(unix_timestamp(y.eventdate_non_cast, 'MM/dd/yy'), 'yyyy-MM-dd')
    )) as eventdate,
    y.name_do,
    udf_convert_to_local_date(coalesce(
        from_unixtime(unix_timestamp(y.paymentDate, 'dd.MM.yyyy'), 'yyyy-MM-dd'),
        from_unixtime(unix_timestamp(y.paymentDate, 'MM/dd/yy'), 'yyyy-MM-dd')
    )) as paymentDate,
    y.number,
    y.operationType,
    cast(y.amount as double) as amount,
    case
        when y.currency is null or lower(trim(y.currency))='null' then null
        when trim(y.currency)='RUB' or trim(y.currency)='руб' or trim(y.currency)='руб.' then 'RUB'
        else trim(y.currency)
    end as currency,
    y.organisationBankingAccount,
    y.organisation,
    y.incomingDocNumber,
    udf_convert_to_local_date(coalesce(
        from_unixtime(unix_timestamp(y.incomingDocDate, 'dd.MM.yyyy'), 'yyyy-MM-dd'),
        from_unixtime(unix_timestamp(y.incomingDocDate, 'MM/dd/yy'), 'yyyy-MM-dd')
    )) as incomingDocDate,
    y.cashFlowLine,
    case
        when y.isPayed is null or lower(trim(y.isPayed))='null' then null
        when lower(trim(y.isPayed))='да' then true
        when lower(trim(y.isPayed))='нет' then false
        else false
    end as isPayed,
    y.counterpartyCode,
    y.contractCode,
    y.comment,
    y.baseDoc,
    y.paymentReference,
    y.baseDoc2,
    y.organisation1,
    case
        when y.posted is null or lower(trim(y.posted))='null' then null
        when lower(trim(y.posted))='да' then true
        when lower(trim(y.posted))='нет' then false
        else false
    end as posted,
    case
        when y.deleted is null or lower(trim(y.deleted))='null' then null
        when lower(trim(y.deleted))='да' then true
        when lower(trim(y.deleted))='нет' then false
        else false
    end as deleted,
    y.cashFlowLineLocal,
    y.counterparty as counterpartyName,
    y.paymentID,
    y.contractNumber,
    y.fileName,
    y.sheetName,
    y.modificationDate,
    y.loadDate,
    y.error_status,
    y.ts_update,
    y.id_sha2
FROM (
    SELECT r.*, case when r.name_do = 'sm' then r.incomingDocDate else r.paymentDate end as eventdate_non_cast
    FROM {dbSchema}.collisions_report_payment r
    WHERE error_status = 'OK'
) y
