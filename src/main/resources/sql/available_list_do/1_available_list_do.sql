SELECT distinct t.*
FROM (
  SELECT distinct r.string_name_do
  FROM (
    SELECT trim(doName) as string_name_do FROM {dbSchema}.union_credit_limit
    UNION
    SELECT trim(groupdo) as string_name_do FROM {dbSchema}.union_pdz
  ) r
  WHERE r.string_name_do is not null and r.string_name_do != ""
) t
LEFT JOIN {dbSchema}.white_list_do w
ON w.string_name_do = t.string_name_do
WHERE w.string_name_do is null
