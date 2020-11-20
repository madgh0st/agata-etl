SELECT *
FROM (SELECT DISTINCT id,
                      cast(close AS DOUBLE)               close,
                      to_date(tradeDate, "yyyy-MM-dd") AS eventDate
      FROM {dbSchema}.sectoral_indices_sort_do)
WHERE close IS NOT NULL
