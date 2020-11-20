SELECT
       udf_convert_to_local_date(coalesce(from_unixtime((unix_timestamp(r.docDate, 'dd.MM.yyyy')),'yyyy-MM-dd'), udf_convert_valuedate_to_string(r.docDate))) as eventdate,
       r.code,
       r.name_do,
       r.docDate,
       r.counterparty as counterpartyName,
       r.contract,
       cast(r.amount as double) as amount,
       case
              when r.currency is null or lower(trim(r.currency))='null' then null
              when trim(r.currency)='RUB' or trim(r.currency)='руб' or trim(r.currency)='руб.' then 'RUB'
              else trim(r.currency)
       end as currency,
       r.number,
       r.operationType,
       r.counterpartyCode,
       r.contractCode,
       r.comment,
       r.baseDoc,
       r.noSettlementControl,
       r.organisation,
       case
              when r.posted is null or lower(trim(r.posted))='null' then null
              when lower(trim(r.posted))='да' then true
              when lower(trim(r.posted))='нет' then false
              else false
       end as posted,
       case
              when r.deleted is null or lower(trim(r.deleted))='null' then null
              when lower(trim(r.deleted))='да' then true
              when lower(trim(r.deleted))='нет' then false
              else false
       end as deleted,
       r.affectsAccounting,
       r.affectsManagement,
       r.exportSelling,
       r.fileName,
       r.sheetName,
       r.modificationDate,
       r.loadDate,
       r.error_status,
       r.ts_update,
       r.id_sha2
FROM {dbSchema}.collisions_report_realization r
WHERE r.error_status = 'OK'
