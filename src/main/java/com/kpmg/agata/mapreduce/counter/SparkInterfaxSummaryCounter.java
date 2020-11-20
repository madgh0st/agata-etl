package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanSparkInterfaxSummaryModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_CAUTION;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_CL_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_DISHONEST_SUPPLIERS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_FIN_RISK;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_LC_CLAIMS_2Y;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_LC_COUNT_2Y;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_LC_DECISIONS_2Y;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_NEWS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_PAYMENT_DISC;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_PLEDGES;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_RISK_FACTORS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_STATUS;
import static com.kpmg.agata.utils.Utils.putNotNull;
import static com.kpmg.agata.utils.Utils.toStringOrNull;

/**
 * Depends on LegalCasesCounter
 */
public class SparkInterfaxSummaryCounter implements EventLogCounter {

    private static final Map<String, String> REGISTRIES = new HashMap<>();

    static {
        REGISTRIES.put("sp_ifax_reg_монополии", "Реестр монополий по данным ФАС");
        REGISTRIES.put("sp_ifax_reg_ит_организации", "Реестр аккредитованных организаций, осуществляющих деятельность" +
                " в области информационных технологий");
        REGISTRIES.put("sp_ifax_reg_системообразующие_предпр", "Перечень системообразующих предприятий");
        REGISTRIES.put("sp_ifax_reg_пробирная_палата", "Реестр свидетельств о постановке на спецучет в Пробирной " +
                "Палате РФ");
        REGISTRIES.put("sp_ifax_reg_доверительные_управл", "Реестр доверительных управляющих");
        REGISTRIES.put("sp_ifax_reg_надежные_партнеры_ттп", "Реестр надежных партнеров ТПП");
        REGISTRIES.put("sp_ifax_reg_сертификатор_лаб_инспекторов_провайдеров", "Реестр аккредитованных органов по " +
                "сертификации, испытательных лабораторий (центров), органов инспекции, провайдеров МСИ");
        REGISTRIES.put("sp_ifax_reg_депозитарии", "Реестр депозитариев");
        REGISTRIES.put("sp_ifax_reg_диллеры", "Реестр дилеров");
        REGISTRIES.put("sp_ifax_reg_мал_сред_предприниматели_москвы", "Реестр субъектов малого и среднего " +
                "предпринимательства г. Москвы");
        REGISTRIES.put("sp_ifax_reg_мал_сред_предприниматели", "Реестр субъектов малого и среднего " +
                "предпринимательства");
        REGISTRIES.put("sp_ifax_reg_стратегич_предпр", "Перечень стратегических предприятий");
        REGISTRIES.put("sp_ifax_reg_гарант_банки", "Банки отвечающие требованиям для принятия банковских гарантий в " +
                "целях налогообложения");
        REGISTRIES.put("sp_ifax_reg_коллекторы", "Реестр коллекторских организаций");
        REGISTRIES.put("sp_ifax_reg_союз_промышленников_и_предпринимателей", "Российский союз промышленников и " +
                "предпринимателей");
        REGISTRIES.put("sp_ifax_reg_нко", "Реестр НКО");
        REGISTRIES.put("sp_ifax_reg_в_особой_эконом_зоне", "Реестр резидентов особых экономических зон");
        REGISTRIES.put("sp_ifax_reg_золотая_акция",
                "Перечень АО по Распоряжению Правительства 1870-р (\"золотая акция\")");
        REGISTRIES.put("sp_ifax_reg_руководители_нескольких_юр_лиц", "Реестр физических лиц, являющихся " +
                "руководителями нескольких юридических лиц по данным ФНС");
        REGISTRIES.put("sp_ifax_reg_удостовер_центры", "Реестр аккредитованных удостоверяющих центров");
        REGISTRIES.put("sp_ifax_reg_оао", "Перечень ОАО по Распоряжению Правительства № 91-Р");
        REGISTRIES.put("sp_ifax_reg_общество_стратегич_знач", "Реестр обществ, имеющих стратегическое значение " +
                "(Распоряжение Правительства РФ от 25.12.2018 №2930-Р, 213-ФЗ от 21.07.2014)8 №2930-Р");
        REGISTRIES.put("sp_ifax_reg_учредители_нескольких_юр_лиц", "Реестр физических лиц, являющихся учредителями " +
                "(участниками) нескольких юридических лиц по данным ФНС");
        REGISTRIES.put("sp_ifax_reg_фгуп_существенные", "Перечень ФГУП, имеющих существенное значение");
        REGISTRIES.put("sp_ifax_reg_предприниматели_москвы_с_поддержкой", "Перечень субъектов предпринимательской " +
                "деятельности - получателей поддержки г. Москвы");
        REGISTRIES.put("sp_ifax_reg_эмитенты_ценных_бумаг", "Эмитенты ценных бумаг, допущенных к организованным " +
                "торгам");
        REGISTRIES.put("sp_ifax_reg_обработчики_персональных_данных", "Реестр операторов, осуществляющих обработку " +
                "персональных данных");
        REGISTRIES.put("sp_ifax_reg_сдают_отчет_мсфо", "Список сдающих отчетность МСФО");
        REGISTRIES.put("sp_ifax_reg_гос_реестр_микрофин_орг", "Государственный реестр микрофинансовых организаций");
        REGISTRIES.put("sp_ifax_reg_реестродержатели", "Реестр реестродержателей");
        REGISTRIES.put("sp_ifax_reg_стратегические_орг", "Перечень стратегических организаций по Распоряжению " +
                "Правительства 1226-р");
        REGISTRIES.put("sp_ifax_reg_оборонная_пром", "Реестр оборонно-промышленного комплекса");
        REGISTRIES.put("sp_ifax_reg_брокеры", "Реестр брокеров");
        REGISTRIES.put("sp_ifax_reg_туроператоры", "Реестр туроператоров");
        REGISTRIES.put("sp_ifax_reg_исключенные_микрофин_орг", "Микрофинансовые организации, исключенные из реестра");
        // Risk registries
        REGISTRIES.put("sp_ifax_risk_reg_нет_по_юр_адресу", "Компании, отсутствующие по юр. адресу по данным ФНС");
        REGISTRIES.put("sp_ifax_risk_reg_недобросовестные_поставщики", "Реестр недобросовестных поставщиков");
        REGISTRIES.put("sp_ifax_risk_reg_проверка_с_осмотрительностью", "Рекомендована проверка в рамках должной " +
                "осмотрительности");
        REGISTRIES.put("sp_ifax_risk_reg_проверка_2ндфл", "Рекомендована проверка справок 2-НДФЛ");
        REGISTRIES.put("sp_ifax_risk_reg_налоговые_долги", "Юр. лица, имеющие задолженность по уплате налогов");
        REGISTRIES.put("sp_ifax_risk_reg_нет_налогового_отчета", "Юр. лица, не предоставляющие налоговую отчетность " +
                "более года");
        REGISTRIES.put("sp_ifax_risk_reg_с_дисквал_лицами", "Юридические лица, в состав исполнительных органов " +
                "которых входят дисквалифицированные лица");
    }

    private String lastStatus = null;
    private Integer lastCautionIndex = null;
    private Integer lastFinancialRiskIndex = null;
    private Integer lastPaymentDisciplineIndex = null;
    private String lastRiskFactors = null;
    private Boolean lastPledges = null;
    private Integer lastLegalCasesCountTwoYears = null;
    private Double lastLegalCasesClaimsSumTwoYears = null;
    private Double lastLegalCasesDecisionsSumTwoYears = null;
    private String lastNews = null;
    private Boolean lastIsDishonestSupplier = null;
    private Double lastSparkInterfaxCreditLimit = null;
    private Set<String> lastNegativeRegistries = emptySet();
    private Double lastOriginalCreditLimit = null;


    private static Set<String> cleanNegativeRegistries(String raw) {
        if (raw == null) return emptySet();

        return REGISTRIES.entrySet()
                         .stream()
                         .filter(pair -> raw.contains(pair.getValue()))
                         .map(Map.Entry::getKey)
                         .collect(toSet());
    }

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanSparkInterfaxSummaryModel) {
            return processSpIfax((CleanSparkInterfaxSummaryModel) model);
        }

        if (model instanceof CleanCreditLimitModel) {
            return processCreditLimit((CleanCreditLimitModel) model);
        }

        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            return processPdz();
        }

        return emptyMap();
    }

    private Map<String, String> processSpIfax(CleanSparkInterfaxSummaryModel spIfax) {
        lastStatus = spIfax.getStatus();
        lastCautionIndex = spIfax.getCautionIndex();
        lastFinancialRiskIndex = spIfax.getFinancialRiskIndex();
        lastPaymentDisciplineIndex = spIfax.getPaymentDisciplineIndex();
        lastRiskFactors = spIfax.getRiskFactors();
        lastPledges = spIfax.getPledges();
        lastLegalCasesCountTwoYears = spIfax.getLegalCasesCountTwoYears();
        lastLegalCasesClaimsSumTwoYears = spIfax.getLegalCasesClaimsSumTwoYears();
        lastLegalCasesDecisionsSumTwoYears = spIfax.getLegalCasesDecisionsSumTwoYears();
        lastNews = spIfax.getNews();
        lastIsDishonestSupplier = spIfax.getIsDishonestSupplier();
        lastSparkInterfaxCreditLimit = spIfax.getSparkInterfaxCreditLimit();
        lastNegativeRegistries = cleanNegativeRegistries(spIfax.getNegativeRegistries());

        return emptyMap();
    }

    private Map<String, String> processCreditLimit(CleanCreditLimitModel cl) {
        lastOriginalCreditLimit = cl.getLimitInRub();
        return emptyMap();
    }

    private Map<String, String> processPdz() {
        Map<String, String> reports = new HashMap<>();
        putNotNull(reports, SP_IFAX_STATUS, lastStatus);
        putNotNull(reports, SP_IFAX_CAUTION, toStringOrNull(lastCautionIndex));
        putNotNull(reports, SP_IFAX_FIN_RISK, toStringOrNull(lastFinancialRiskIndex));
        putNotNull(reports, SP_IFAX_PAYMENT_DISC, toStringOrNull(lastPaymentDisciplineIndex));
        putNotNull(reports, SP_IFAX_RISK_FACTORS, lastRiskFactors);
        putNotNull(reports, SP_IFAX_PLEDGES, toStringOrNull(lastPledges));
        putNotNull(reports, SP_IFAX_LC_COUNT_2Y, toStringOrNull(lastLegalCasesCountTwoYears));
        putNotNull(reports, SP_IFAX_LC_CLAIMS_2Y, toStringOrNull(lastLegalCasesClaimsSumTwoYears));
        putNotNull(reports, SP_IFAX_LC_DECISIONS_2Y, toStringOrNull(lastLegalCasesDecisionsSumTwoYears));
        putNotNull(reports, SP_IFAX_NEWS, lastNews);
        putNotNull(reports, SP_IFAX_DISHONEST_SUPPLIERS, toStringOrNull(lastIsDishonestSupplier));
        putNotNull(reports, SP_IFAX_CL_RATIO, calculateSpIfaxCreditLimit());

        for (String registry : lastNegativeRegistries) {
            putNotNull(reports, registry, "1");
        }

        return reports;
    }

    private String calculateSpIfaxCreditLimit() {
        if (lastOriginalCreditLimit == null
                || lastOriginalCreditLimit == 0.0
                || lastSparkInterfaxCreditLimit == null) {
            return null;
        }

        return Double.toString(lastSparkInterfaxCreditLimit / lastOriginalCreditLimit);
    }
}
