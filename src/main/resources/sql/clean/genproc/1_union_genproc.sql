WITH distinct_normalized_genproc AS (
    SELECT DISTINCT name_do,
                    fileName,
                    sheetName,
                    modificationDate,
                    loadDate,
                    companyName,
                    udf_normalized(companyName) AS normCompanyName,
                    inn,
                    decisionDate
    FROM {dbSchema}.genproc_sort_do
)
-- todo: create udf function of generating "id_sha2" using list of required fields (or all fields)
SELECT distinct_normalized_genproc.*,
       sha2(concat(
                    coalesce(name_do, 'null'),
                    coalesce(fileName, 'null'),
                    coalesce(sheetName, 'null'),
                    coalesce(modificationDate, 'null'),
                    coalesce(loadDate, 'null'),
                    coalesce(companyName, 'null'),
                    coalesce(normCompanyName, 'null'),
                    coalesce(inn, 'null'),
                    coalesce(decisionDate, 'null')
                ), 256) AS id_sha2
FROM distinct_normalized_genproc
