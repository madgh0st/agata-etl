package com.kpmg.agata.parser;

import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.models.AffilationModel;
import com.kpmg.agata.models.CounterpartyContractModel;
import com.kpmg.agata.models.CounterpartyDictionaryModel;
import com.kpmg.agata.models.CreditCorrectionModel;
import com.kpmg.agata.models.CreditLimitModel;
import com.kpmg.agata.models.FnsArrearsModel;
import com.kpmg.agata.models.FnsDisqModel;
import com.kpmg.agata.models.FnsTaxViolationModel;
import com.kpmg.agata.models.FsspModel;
import com.kpmg.agata.models.GenprocModel;
import com.kpmg.agata.models.LegalCasesModel;
import com.kpmg.agata.models.MdGpnCustomModel;
import com.kpmg.agata.models.MonthlyStatusPDZModel;
import com.kpmg.agata.models.PaymentFromCustomerModel;
import com.kpmg.agata.models.PdzBecauseOfBanksModel;
import com.kpmg.agata.models.RealizationServicesModel;
import com.kpmg.agata.models.SeasonalityModel;
import com.kpmg.agata.models.SectoralIndexModel;
import com.kpmg.agata.models.SparkInterfaxSummaryModel;
import com.kpmg.agata.models.WeeklyStatusPDZModel;
import com.kpmg.agata.models.WeeklyStatusPDZModelMB;
import com.kpmg.agata.models.combinable.AeroModel;
import com.kpmg.agata.models.combinable.BmModel;
import com.kpmg.agata.models.combinable.KpModel;
import com.kpmg.agata.models.combinable.MbModel;
import com.kpmg.agata.models.combinable.RpModel;
import com.kpmg.agata.models.combinable.Sm2016Model;
import com.kpmg.agata.models.combinable.Sm2017Model;
import java.util.Arrays;

public enum ConfigType {
    COUNTERPARTY_CONTRACT("Договоры контрагентов", CounterpartyContractModel.class),
    COUNTERPARTY_DICTIONARY("Справочник контрагентов", CounterpartyDictionaryModel.class),
    COUNTERPARTY_DICTIONARY_CUSTOM("Справочник контрагентов ручное заполнение", MdGpnCustomModel.class),
    CREDIT_CORRECTION("Корректировки или взаимозачет задолженности", CreditCorrectionModel.class),
    CREDIT_LIMIT("Кредитные лимиты реестр", CreditLimitModel.class),
    EXPRESS_ANALYSIS_RSBU_LIMIT("Анкеты экспресс-анализа РСБУ расчет лимита"),
    EXPRESS_ANALYSIS_RSBU_REPORT("Анкеты экспресс-анализа РСБУ отчетность"),
    EXPRESS_ANALYSIS_MSFO_REPORT("Анкеты экспресс-анализа МСФО отчетность"),
    EXPRESS_ANALYSIS_MSFO_LIMIT("Анкеты экспресс-анализа МСФО расчет лимита"),
    MONTHLY_STATUS_PDZ("Ежемесячный статус ПДЗ", MonthlyStatusPDZModel.class),
    PAYMENT_FROM_CUSTOMER("Оплата от покупателя", PaymentFromCustomerModel.class),
    REALIZATION_SERVICES("Реализации товаров и услуг", RealizationServicesModel.class),
    WEEKLY_STATUS_PDZ("Еженедельный статус ПДЗ", WeeklyStatusPDZModel.class),
    WEEKLY_STATUS_PDZ_MB("Еженедельный статус ПДЗ mb", WeeklyStatusPDZModelMB.class),
    LEGAL_CASES("Судебные дела", LegalCasesModel.class),
    SPARK_INTERFAX_SUMMARY("Спарк-интерфакс суммарная информация", SparkInterfaxSummaryModel.class),
    PDZ_BEFORE_2018_BM("ПДЗ до 2018 года BM", BmModel.class),
    PDZ_BEFORE_2018_KP("ПДЗ до 2018 года KP", KpModel.class),
    PDZ_BEFORE_2018_MB("ПДЗ до 2018 года MB", MbModel.class),
    PDZ_BEFORE_2018_RP("ПДЗ до 2018 года RP", RpModel.class),
    PDZ_BEFORE_2018_AERO("ПДЗ до 2018 года AERO", AeroModel.class),
    PDZ_BEFORE_2018_SM_2016("ПДЗ до 2018 года SM 2016", Sm2016Model.class),
    PDZ_BEFORE_2018_SM_2017("ПДЗ до 2018 года SM 2017", Sm2017Model.class),
    PDZ_BECAUSE_OF_BANKS("ПДЗ по причине банков", PdzBecauseOfBanksModel.class),
    FNS_TAX_VIOLATION("Сведения о налоговых правонарушениях", FnsTaxViolationModel.class),
    FNS_ARREARS("Сведения о суммах недоимки и задолженности", FnsArrearsModel.class),
    FSSP("Исполнительные производства в отношении юридических лиц", FsspModel.class),
    GENPROC("Незаконное вознаграждение", GenprocModel.class),
    FNS_DISQ("Дисквалификация", FnsDisqModel.class),
    AFFILATION("Аффилированность", AffilationModel.class),
    SECTORAL_INDICES("Отраслевые индексы полной доходности", SectoralIndexModel.class),
    SEASONALITY("Сезонность", SeasonalityModel.class);

    private final String rawValue;
    private final Class<? extends AbstractClientDataModel> modelClass;

    ConfigType(String rawValue, Class<? extends AbstractClientDataModel> modelClassName) {
        this.rawValue = rawValue;
        this.modelClass = modelClassName;
    }

    ConfigType(String rawValue) {
        this.rawValue = rawValue;
        this.modelClass = null;
    }

    public static ConfigType getConfigTypeForRawValue(String rawValueType) {

        return Arrays.stream(ConfigType.values())
                     .filter(x -> rawValueType.trim().equalsIgnoreCase(x.rawValue.trim()))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(
                             String.format("Type with value: %s can't be found", rawValueType)));
    }

    public Class<? extends AbstractClientDataModel> getModelClass() {
        return modelClass;
    }
}
