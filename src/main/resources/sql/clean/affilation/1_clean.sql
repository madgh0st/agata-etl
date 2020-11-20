SELECT DISTINCT fullName, shortName, inn, previousInns, okved
FROM {dbSchema}.affilation_sort_do
WHERE inn is not null
