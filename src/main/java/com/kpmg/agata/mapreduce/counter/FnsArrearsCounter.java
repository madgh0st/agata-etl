package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanFnsArrearsModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.FNS_ARREARS_PREFIX;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_180_DAYS;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.DESC;

public class FnsArrearsCounter implements EventLogCounter {

    private static Logger log = LoggerFactory.getLogger(FnsArrearsCounter.class);

    private HashMap<String, WindowBuffer<String>> buffer = new HashMap<>();

    private static final String TOTAL = "total";

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        Map<String, String> result;
        LocalDate currentDate = model.getEventDate().toLocalDate();
        if (model instanceof CleanFnsArrearsModel) {
            CleanFnsArrearsModel arrearsModel = (CleanFnsArrearsModel) model;
            String signal;
            try {
                signal = ArrearsTaxName.getSignal(arrearsModel.getTaxName());
            } catch (IllegalArgumentException e) {
                log.warn(e.getMessage(), e);
                return Collections.emptyMap();
            }
            buffer.computeIfAbsent(signal, k -> new WindowBuffer<>(RANGE_180_DAYS, ASC));
            buffer.get(signal).put(currentDate, arrearsModel.getTotalArrears());
            result = Collections.emptyMap();
        } else if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            result = populateResult(currentDate);
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    private Map<String, String> populateResult(LocalDate currentDate) {
        Map<String, String> result = new HashMap<>();
        double total = 0;
        for (ArrearsTaxName value : ArrearsTaxName.values()) {
            String signal = ArrearsTaxName.getSignal(value.getRawName());
            WindowBuffer<String> signalBuffer = buffer.get(signal);
            if (signalBuffer != null) {
                LocalDate from = currentDate.minusDays(RANGE_180_DAYS).plusDays(1);
                String lastArrearsValue =
                        signalBuffer.streamWithinRange(from, currentDate, DESC).findFirst().orElse("0");
                result.put(signal, lastArrearsValue);
                total += Double.parseDouble(lastArrearsValue);
            }
            result.put(FNS_ARREARS_PREFIX + TOTAL, Double.toString(total));
        }
        return result;
    }

    public enum ArrearsTaxName {
        MINERAL_PRODUCTION("Налог на добычу полезных ископаемых"),
        DISPOSAL_FEE("Утилизационный сбор"),
        SIMPLIFIED("Налог, взимаемый в связи с  применением упрощенной  системы налогообложения"),
        SOCIAL_INSURANCE(
                "Страховые взносы на обязательное социальное страхование на случай временной нетрудоспособности и в связи с материнством"),
        SALES("Торговый сбор"),
        AGRICULTURAL("Единый сельскохозяйственный налог"),
        CANCELLED(
                "Задолженность и перерасчеты по ОТМЕНЕННЫМ НАЛОГАМ  и сборам и иным обязательным платежам  (кроме ЕСН, страх. Взносов)"),
        PROPERTY("Налог на имущество организаций"),
        WILDLIFE("Сборы за пользование объектами животного мира  и за пользование объектами ВБР"),
        PENSION("Страховые и другие взносы на обязательное пенсионное страхование, зачисляемые в Пенсионный фонд Российской Федерации"),
        VALUE_ADDED("Налог на добавленную стоимость"),
        PERSONAL_INCOME("Налог на доходы физических лиц"),
        PROFIT("Налог на прибыль"),
        EXCISE("Акцизы, всего"),
        GAMBLING("Налог на игорный"),
        MINERAL_PRODUCTION_ROYALTY(
                "Регулярные платежи за добычу полезных ископаемых (роялти) при выполнении соглашений о разделе продукции"),
        LABOUR_HEALTH_INSURANCE(
                "Страховые взносы на обязательное медицинское страхование работающего населения, зачисляемые в бюджет Федерального фонда обязательного медицинского страхования"),
        WATER("Водный налог"),
        NON_TAX_INCOME("НЕНАЛОГОВЫЕ ДОХОДЫ, администрируемые налоговыми органами"),
        NATIONAL_DUTY("Государственная пошлина"),
        IMPUTED("Единый налог на вмененный доход для отдельных видов  деятельности"),
        LAND("Земельный налог"),
        TRANSPORT("Транспортный налог");

        String rawName;

        ArrearsTaxName(String rawName) {
            this.rawName = rawName;
        }

        public static ArrearsTaxName getArrearsTaxByRawName(String rawName) {
            return Arrays.stream(ArrearsTaxName.values())
                    .filter(x -> x.rawName.equals(rawName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Tax with name: %s can't be found", rawName)));
        }

        public static String getSignal(String rawName) {
            ArrearsTaxName arrearsTaxName = ArrearsTaxName.getArrearsTaxByRawName(rawName);
            return FNS_ARREARS_PREFIX + arrearsTaxName.name().toLowerCase();
        }

        public String getRawName() {
            return rawName;
        }
    }
}
