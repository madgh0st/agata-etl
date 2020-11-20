package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanLegalCasesModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_IS_ANY_CASE_IN_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_IS_ANY_CASE_IN_PAST;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_NUMBER_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_UNCLOSED_COUNT_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_UPHELD_CLAIMS_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_90_DAYS;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;

public class LegalCasesCounter implements EventLogCounter {


    private static final String[] UPHELD_CLAIM_OUTCOMES = {"Иск удовлетворен полностью", "Иск удовлетворен частично"};
    private static final Map<String, String> CATEGORY_GROUPS = new HashMap<>();
    private static final Set<String> UNIQUE_CATEGORY_GROUPS;

    private static final int CASES_COUNT_RANGE = RANGE_90_DAYS;
    private static final int ANY_CASES_RANGE = RANGE_90_DAYS;
    private static final int UNCLOSED_CASES_RANGE = RANGE_90_DAYS;
    private static final int UPHELD_CLAIMS_RANGE = RANGE_90_DAYS;
    private static final int CAT_GROUPS_RANGE = RANGE_90_DAYS;

    static {
        Stream.of("Принадлежность акций и долей участия",
                "Споры о ценных бумагах")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_shares"));
        Stream.of("Банкротство",
                "Создание, реорганизация и ликвидация организаций")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_bankruptcy_liquidation"));
        Stream.of("Взыскание обязательных платежей и санкций",
                "Возмещение вреда")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_compensations"));
        Stream.of("Внедоговорные обязательства",
                "Заключение договоров",
                "Исполнение обязательств по договорам",
                "Исполнение обязательств по договорам агентирования",
                "Исполнение обязательств по договорам аренды",
                "Исполнение обязательств по договорам аренды зданий и сооружений",
                "Исполнение обязательств по договорам аренды предприятий",
                "Исполнение обязательств по договорам аренды транспортных средств",
                "Исполнение обязательств по договорам банковского обслуживания (вклады, счета, расчеты)",
                "Исполнение обязательств по договорам безвозмездного пользования",
                "Исполнение обязательств по договорам возмездного оказания услуг",
                "Исполнение обязательств по договорам выполнения научно-изыскательских, опытно-конструкторских и технологических работ",
                "Исполнение обязательств по договорам дарения",
                "Исполнение обязательств по договорам доверительного управления имуществом",
                "Исполнение обязательств по договорам займа",
                "Исполнение обязательств по договорам займа и кредита",
                "Исполнение обязательств по договорам комиссии",
                "Исполнение обязательств по договорам коммерческой концессии",
                "Исполнение обязательств по договорам контрактации",
                "Исполнение обязательств по договорам купли-продажи",
                "Исполнение обязательств по договорам мены",
                "Исполнение обязательств по договорам перевозки",
                "Исполнение обязательств по договорам подряда",
                "Исполнение обязательств по договорам подряда на выполнение проектных и изыскательских работ",
                "Исполнение обязательств по договорам подряда на выполнение работ для государственных или муниципальных нужд",
                "Исполнение обязательств по договорам поручения",
                "Исполнение обязательств по договорам поставки товаров",
                "Исполнение обязательств по договорам поставки товаров для государственных или муниципальных нужд",
                "Исполнение обязательств по договорам продажи недвижимости",
                "Исполнение обязательств по договорам продажи предприятия",
                "Исполнение обязательств по договорам проката",
                "Исполнение обязательств по договорам простого товарищества",
                "Исполнение обязательств по договорам розничной торговли",
                "Исполнение обязательств по договорам страхования",
                "Исполнение обязательств по договорам строительного подряда",
                "Исполнение обязательств по договорам транспортно-экспедиционной деятельности",
                "Исполнение обязательств по договорам транспортной экспедиции",
                "Исполнение обязательств по договорам участия в долевом строительстве",
                "Исполнение обязательств по договорам финансирования под уступку денежного требования",
                "Исполнение обязательств по договорам финансовой аренды (лизингу)",
                "Исполнение обязательств по договорам хранения",
                "Исполнение обязательств по договорам энергоснабжения",
                "Исполнение обязательств по кредитным договорам",
                "Признание договоров недействительными")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_commitment"));
        Stream.of("Защита деловой репутации",
                "Злоупотребление доминирующим положением",
                "Корпоративные споры",
                "Недобросовестная конкуренция",
                "Незаконное ограничение конкуренции")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_concurrency"));
        Stream.of("Оспаривание и исполнение решений третейских судов",
                "Оспаривание ненормативных правовых актов, связанных с  бюджетным законодательством",
                "Оспаривание нормативных правовых актов",
                "Оспаривание решений государственных органов и действий (бездействий) должностных лиц",
                "Оспаривание решений налоговых органов и действий (бездействий) должностных лиц",
                "Оспаривание решений о государственной регистрации ЮЛ и ИП",
                "Оспаривание решений о государственной регистрации прав на недвижимое имущество")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_contest"));
        Stream.of("Полномочия и ответственность лиц, входящих в органы  юридического лица",
                "Полномочия общего собрания участников юридического лица",
                "Право на судопроизводство или исполнение в разумный срок")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_permissions"));
        Stream.of("Авторские права",
                "Аренда земельных участков",
                "Безвозмездное пользование земельным участком",
                "Защита прав собственности и иных вещных прав",
                "Использование земель по иным основаниям",
                "Истребование имущества из чужого незаконного владения",
                "Неосновательное обогащение",
                "Ограниченное пользование (сервитут)",
                "Права на землю и иные споры, связанные с землей",
                "Право собственности",
                "Право собственности на жилые помещения",
                "Право собственности на землю",
                "Право хозяйственного ведения и оперативного управления")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_property"));
        Stream.of(
                "Привлечение к административной ответственности",
                "Привлечение к административной ответственности в области дорожного движения",
                "Привлечение к административной ответственности в области охраны окружающей среды",
                "Привлечение к административной ответственности в области охраны собственности",
                "Привлечение к административной ответственности в области предпринимательской деятельности и СРО",
                "Привлечение к административной ответственности в области связи и информации",
                "Привлечение к административной ответственности в области финансов, налогов и сборов, страхования, " +
                        "рынка ценных бумаг",
                "Привлечение к административной ответственности в промышленности, строительстве и энергетике",
                "Привлечение к административной ответственности в сельском хозяйстве, ветеринарии и мелиорации земель",
                "Привлечение к административной ответственности за нарушение гос.границы РФ",
                "Привлечение к административной ответственности за нарушение законодательства о гос.регистрации",
                "Привлечение к административной ответственности за нарушение законодательства о рекламе и требований " +
                        "к установке рекламных конструкций",
                "Привлечение к административной ответственности за нарушение порядка ценообразования",
                "Привлечение к административной ответственности за нарушение правил продажи товаров",
                "Привлечение к административной ответственности за нарушение таможенных правил",
                "Привлечение к административной ответственности за нарушение требований безопасности и общественного " +
                        "порядка",
                "Привлечение к административной ответственности за нарушение требований к маркировке, декларированию " +
                        "и сертификации",
                "Привлечение к административной ответственности за нарушение требований к производству, обороту, " +
                        "продаже спиртосодержащей продукции",
                "Привлечение к административной ответственности за нарушения на транспорте",
                "Привлечение к административной ответственности за нарушения при получении кредита или займа",
                "Привлечение к административной ответственности за недобросовестную конкуренцию или ограничение " +
                        "конкуренции",
                "Привлечение к административной ответственности за незаконное использование средств индивидуализации",
                "Привлечение к административной ответственности за незаконную продажу товаров",
                "Привлечение к административной ответственности за ненадлежащее качество товаров, работ или услуг",
                "Привлечение к административной ответственности за непредставление документов о споре, связанном с " +
                        "созданием юр.лица, управлением им или участием в нем",
                "Привлечение к административной ответственности за обман и другие нарушения прав потребителей",
                "Привлечение к административной ответственности за посягание на институты государственной власти",
                "Привлечение к административной ответственности за правонарушения, связанные с банкротством",
                "Привлечение к административной ответственности за самоуправство",
                "Привлечение к административной ответственности по причине нарушения использования бюджетных средств")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_administrative_liability"));
        Stream.of("Признание решений иностранных судов и арбитражных решений",
                "Применение антимонопольного законодательства",
                "Применение бюджетного законодательства",
                "Применение законодательства о земле",
                "Применение законодательства об окружающей среде",
                "Применение таможенного законодательства")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_law"));
        Stream.of("Коммерческое обозначение",
                "Места происхождения товаров",
                "Нарушения закона о рекламе",
                "Нарушения при закупках согласно 44-ФЗ и 223-ФЗ",
                "Нарушения процедуры торгов",
                "Охрана интеллектуальной собственности",
                "Патентные права",
                "Прочее",
                "Секреты производства (ноу-хау)",
                "Селекционные достижения",
                "Смежные права",
                "Споры, связанные с предоставлением субсидий",
                "Средства индивидуализации",
                "Товарные знаки и знаки обслуживания",
                "Установление фактов, имеющих юридическое значение",
                "Фирменные наименования")
              .forEach(category -> CATEGORY_GROUPS.put(category, "lc_cat_commerce"));
        UNIQUE_CATEGORY_GROUPS = new HashSet<>(CATEGORY_GROUPS.values());
    }

    private final WindowBuffer<Integer> casesCountBuffer = new WindowBuffer<>(RANGE_90_DAYS, ASC);
    private final WindowBuffer<Integer> upheldClaimsBuffer = new WindowBuffer<>(UPHELD_CLAIMS_RANGE, ASC);
    private final WindowBuffer<Set<String>> unclosedCasesBuffer = new WindowBuffer<>(UNCLOSED_CASES_RANGE, ASC);
    private final WindowBuffer<Map<String, Integer>> catGroupsBuffer = new WindowBuffer<>(CAT_GROUPS_RANGE, ASC);
    private boolean caseInPast = false;

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanLegalCasesModel) {
            return processLegalCases((CleanLegalCasesModel) model);
        }
        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            return processPdz(model);
        }
        return emptyMap();
    }

    private Map<String, String> processLegalCases(CleanLegalCasesModel legalCase) {
        LocalDate currentDate = legalCase.getEventDate().toLocalDate();
        casesCountBuffer.put(currentDate, 1, Integer::sum);
        caseInPast = true;

        processUnclosedCases(legalCase, currentDate);

        if (ArrayUtils.contains(UPHELD_CLAIM_OUTCOMES, legalCase.getOutcome())) {
            upheldClaimsBuffer.put(currentDate, 1, Integer::sum);
        }

        processCategoryGroups(legalCase, currentDate);

        return emptyMap();
    }

    private Map<String, String> processPdz(AbstractCleanDataModel pdzModel) {
        LocalDate currentDate = pdzModel.getEventDate().toLocalDate();
        Map<String, String> reports = new HashMap<>();

        reports.put(LC_NUMBER_90D, countCasesInRange(currentDate));
        reports.put(LC_IS_ANY_CASE_IN_90D, isAnyCaseInRange(currentDate));
        reports.put(LC_IS_ANY_CASE_IN_PAST, caseInPast ? "1" : "0");
        reports.put(LC_UNCLOSED_COUNT_90D, countUnclosedCasesInRange(currentDate));
        reports.put(LC_UPHELD_CLAIMS_90D, countUpheldClaimsInRange(currentDate));
        reports.putAll(countCategoryGroupsInRange(currentDate));
        return reports;
    }

    private String isAnyCaseInRange(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(ANY_CASES_RANGE).plusDays(1);
        boolean casePresent = casesCountBuffer.streamWithinRange(from, currentDate)
                                              .anyMatch(Objects::nonNull);
        return casePresent ? "1" : "0";
    }

    private String countCasesInRange(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(CASES_COUNT_RANGE).plusDays(1);
        int casesCount = casesCountBuffer.streamWithinRange(from, currentDate)
                                         .filter(Objects::nonNull)
                                         .mapToInt(i -> i)
                                         .sum();
        return String.valueOf(casesCount);
    }

    private String countUpheldClaimsInRange(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(UPHELD_CLAIMS_RANGE).plusDays(1);
        int count = upheldClaimsBuffer.streamWithinRange(from, currentDate)
                                      .mapToInt(i -> i)
                                      .sum();
        return String.valueOf(count);
    }

    private void processUnclosedCases(CleanLegalCasesModel legalCase, LocalDate currentDate) {
        if (legalCase.getCaseNumber() != null) {
            if ("Завершено".equals(legalCase.getStatus())) {
                LocalDate from = currentDate.minusDays(UNCLOSED_CASES_RANGE).plusDays(1);
                unclosedCasesBuffer.streamWithinRange(from, currentDate)
                                   .forEach(casesByDay -> casesByDay.remove(legalCase.getCaseNumber()));
            } else {
                Set<String> currentDayUnclosedCases = unclosedCasesBuffer.get(currentDate);
                if (currentDayUnclosedCases == null) {
                    currentDayUnclosedCases = new HashSet<>();
                    unclosedCasesBuffer.put(currentDate, currentDayUnclosedCases);
                }
                currentDayUnclosedCases.add(legalCase.getCaseNumber());
            }
        }
    }

    private String countUnclosedCasesInRange(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(UNCLOSED_CASES_RANGE).plusDays(1);
        long unclosedCasesCount = unclosedCasesBuffer.streamWithinRange(from, currentDate)
                                                     .flatMap(Set::stream)
                                                     .distinct()
                                                     .count();
        return String.valueOf(unclosedCasesCount);
    }

    private void processCategoryGroups(CleanLegalCasesModel legalCase, LocalDate currentDate) {
        if (legalCase.getCategory() == null) return;
        String catGroup = CATEGORY_GROUPS.get(legalCase.getCategory());
        if (catGroup == null) return;

        Map<String, Integer> currentDayCatGroupsCounts = catGroupsBuffer.get(currentDate);
        if (currentDayCatGroupsCounts == null) {
            currentDayCatGroupsCounts = new HashMap<>();
            catGroupsBuffer.put(currentDate, currentDayCatGroupsCounts);
        }

        Integer lastCatGroupCount = currentDayCatGroupsCounts.get(catGroup);
        int actualCatGroupCount = lastCatGroupCount == null ? 1 : lastCatGroupCount + 1;

        currentDayCatGroupsCounts.put(catGroup, actualCatGroupCount);
    }

    private Map<String, String> countCategoryGroupsInRange(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(CAT_GROUPS_RANGE).plusDays(1);
        Map<String, Integer> groupsCounts =
                catGroupsBuffer.streamWithinRange(from, currentDate)
                               .flatMap(map -> map.entrySet().stream())
                               .collect(groupingBy(Map.Entry::getKey,
                                       mapping(Map.Entry::getValue, summingInt(i -> i))));

        return UNIQUE_CATEGORY_GROUPS.stream()
                                     .collect(toMap(i -> i, i -> String.valueOf(groupsCounts.getOrDefault(i, 0))));
    }
}
