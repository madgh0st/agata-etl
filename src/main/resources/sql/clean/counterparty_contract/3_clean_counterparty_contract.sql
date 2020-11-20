SELECT * FROM (
      SELECT
          x.code,
          x.id_sha2,
          x.fileName,
          x.sheetName,
          x.modificationDate,
          x.loadDate,
          x.eventDate,
          x.name_do,
          x.ownerCode,
          x.erpCode,
          x.number,
          x.name,
          x.date,
          x.inn,
          x.validityPeriod,
          x.contractKind,
          x.settlementCurrency,
          x.settlementManagement,
          x.markRemove,
          cast(regexp_extract(x.paymentTerm, '\\d+', 0) as int) as paymentTerm
      FROM {dbSchema}.collisions_report_counterparty_contract x
      WHERE x.error_status = 'OK'
) WHERE paymentTerm IS NOT NULL

