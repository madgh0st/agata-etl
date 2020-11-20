package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanExpressAnalysisModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.math.RoundingMode.HALF_UP;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyMap;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_COOP_EXP_CATEGORY;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_COOP_EXP_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_PAY_DISCIPLINE_CATEGORY;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_PAY_DISCIPLINE_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_CURR_ASSETS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_CURR_LIABILITIES;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_NET_ASSETS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_NON_CURR_ASSETS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CAPITALIZATION_COEFFICIENT;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CONTRACTOR_CLASS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_COVER_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CURRENT_ASSETS_EQUITY_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CURRENT_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CUR_TO_FIXED_ASSETS_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_DAYS_SINCE_PUB;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_EQUITY_TOTAL_ASSETS_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_EXPRESS_DEBT_EQUITY_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FINANCIAL_STABILITY_INDEX;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_ASSETS_PROFIT_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_CAPITAL_TURNOVER_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_COVERAGE_COEF_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_HOT_LIQ_COEF_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_SELL_PROFIT_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_STABILITY_COEF_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_INDUSTRY;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_MARGIN_ON_SALES;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_OPERATING_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_QUICK_LIQUIDITY_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_RETURN_ON_ASSETS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_SOLVENCY_GROUP;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_VALUE_OF_PROPERTY_ASSETS_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_WORKING_CAPITAL_TURNOVER;

/**
 * Collects most actual CleanExpressAnalysisModel
 * If collected and current CleanExpressAnalysisModel are from the same date then select with biggest limit_estimation_rsbu
 * When CleanWeeklyStatusPDZFlatModel provided, report some data from collected CleanExpressAnalysisModel
 */
public class ExpressAnalysisCounter implements EventLogCounter {

    private CleanExpressAnalysisModel actualEa = null;

    public static CleanExpressAnalysisModel selectActualExpressAnalysis(CleanExpressAnalysisModel actualEa,
                                                                        CleanExpressAnalysisModel currentEa) {
        if (actualEa == null) {
            return currentEa;
        }

        if (!currentEa.getEventDate().equals(actualEa.getEventDate())) {
            return currentEa;
        }


        if (currentEa.getLimit_estimation_rsbu() != null && actualEa.getLimit_estimation_rsbu() != null) {
            LocalDate currentRsbuDate = currentEa.getLimit_estimation_rsbu().toLocalDate();
            LocalDate actualRsbuDate = actualEa.getLimit_estimation_rsbu().toLocalDate();

            if (currentRsbuDate.isAfter(actualRsbuDate)) {
                return currentEa;
            } else if (currentRsbuDate.isBefore(actualRsbuDate)) {
                return actualEa;
            }
        }

        String currentYear = currentEa.getYear();
        String actualYear = actualEa.getYear();

        if (currentYear != null && actualYear != null) {
            if (currentYear.compareTo(actualYear) > 0) {
                return currentEa;
            } else if (currentYear.compareTo(actualYear) < 0) {
                return actualEa;
            }
        }

        return currentEa;
    }

    private static void putSimpleReports(Map<String, String> reports, CleanExpressAnalysisModel ea) {
        reports.put(EXP_INDUSTRY, ea.getIndustry());
        reports.put(EXP_CONTRACTOR_CLASS, ea.getContractor_class());
        reports.put(EXP_SOLVENCY_GROUP, ea.getSolvency_group());
        reports.put(EXP_POINTS, ea.getPoints());

        reports.put(EXP_FIN_COVERAGE_COEF_POINTS, ea.getFin_metr_coverage_coefficient_points());
        reports.put(EXP_FIN_HOT_LIQ_COEF_POINTS, ea.getFin_metr_hot_liquidity_coefficient_points());
        reports.put(EXP_FIN_CAPITAL_TURNOVER_POINTS, ea.getFin_metr_capital_turnover_points());
        reports.put(EXP_FIN_SELL_PROFIT_POINTS, ea.getFin_metr_selling_profitability_points());
        reports.put(EXP_FIN_ASSETS_PROFIT_POINTS, ea.getFin_metr_assets_profitability_points());
        reports.put(EXP_FIN_STABILITY_COEF_POINTS, ea.getFin_metr_financial_stability_coefficient_points());

        reports.put(EXP_BUS_COOP_EXP_CATEGORY, ea.getBus_rep_cooperation_experience_category());
        reports.put(EXP_BUS_COOP_EXP_POINTS, ea.getBus_rep_cooperation_experience_points());
        reports.put(EXP_BUS_PAY_DISCIPLINE_CATEGORY, ea.getBus_rep_payment_discipline_category());
        reports.put(EXP_BUS_PAY_DISCIPLINE_POINTS, ea.getBus_rep_payment_discipline_points());

        reports.put(EXP_CALC_NON_CURR_ASSETS, ea.getCalculation_non_current_assets());
        reports.put(EXP_CALC_NET_ASSETS, ea.getCalculation_net_assets());
        reports.put(EXP_CALC_CURR_ASSETS, ea.getCalculation_current_assets());
        reports.put(EXP_CALC_CURR_LIABILITIES, ea.getCalculation_current_liabilities());

        reports.put(EXP_COVER_RATIO, String.valueOf(ea.getCover_Ratio()));
        reports.put(EXP_QUICK_LIQUIDITY_RATIO, String.valueOf(ea.getQuick_Liquidity_Ratio()));
        reports.put(EXP_WORKING_CAPITAL_TURNOVER, String.valueOf(ea.getWorking_Capital_Turnover()));
        reports.put(EXP_FINANCIAL_STABILITY_INDEX, String.valueOf(ea.getFinancial_Stability_Index()));
        reports.put(EXP_MARGIN_ON_SALES, String.valueOf(ea.getMargin_On_Sales()));
        reports.put(EXP_RETURN_ON_ASSETS, String.valueOf(ea.getReturn_On_Assets()));
    }

    private void putComplexReports(Map<String, String> reports, CleanExpressAnalysisModel actualEa) {
        reports.put(EXP_CURRENT_RATIO, countCurrentRatio(actualEa));
        reports.put(EXP_CAPITALIZATION_COEFFICIENT, countCapitalizationCoefficient(actualEa));
        reports.put(EXP_CURRENT_ASSETS_EQUITY_RATIO, countCurrentAssetsEquity(actualEa));
        reports.put(EXP_OPERATING_RATIO, countOperatingRatio(actualEa));
        reports.put(EXP_EXPRESS_DEBT_EQUITY_RATIO, countExpressDebtEquityRatio(actualEa));
        reports.put(EXP_VALUE_OF_PROPERTY_ASSETS_RATIO, countValueOfPropertyAssetsRatio(actualEa));
        reports.put(EXP_EQUITY_TOTAL_ASSETS_RATIO, countEquityTotalAssetsRatio(actualEa));
        reports.put(EXP_CUR_TO_FIXED_ASSETS_RATIO, countCurToFixedAssetsRatio(actualEa));
    }

    private String countEquityTotalAssetsRatio(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1300 Капитал и резервы
            BigDecimal liability_Equity_Total = new BigDecimal(actualEa.getLiability_Equity_Total());
            // 1600 ИТОГО АКТИВ
            BigDecimal assetsTotal = new BigDecimal(actualEa.getAssets_Total());
            result = liability_Equity_Total.divide(assetsTotal, 2, HALF_UP).toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private String countValueOfPropertyAssetsRatio(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1150 Основные средства (в т.ч. НЗ)
            BigDecimal assets_Non_Current_Fixed_Assets = new BigDecimal(actualEa.getAssets_non_current_fixed_assets());
            // 1210 Запасы
            BigDecimal assets_Current_Inventory = new BigDecimal(actualEa.getAssets_current_inventory());
            // 1700 ИТОГО ПАССИВ
            BigDecimal liabilities_Total = new BigDecimal(actualEa.getLiabilities_Total());
            result = assets_Current_Inventory
                    .divide(liabilities_Total, 2, HALF_UP)
                    .add(assets_Non_Current_Fixed_Assets)
                    .toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private String countExpressDebtEquityRatio(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1200 Оборотные активы
            BigDecimal assets_Current_Total = new BigDecimal(actualEa.getAssets_Current_Total());
            // 1500 Краткосрочные обязательства
            BigDecimal liability_Short_Term_Liabilities_Total =
                    new BigDecimal(actualEa.getLiability_Short_Term_Liabilities_Total());
            // 1530 Доходы будущих периодов
            BigDecimal liability_Short_Term_Liabilities_Deferred_Income =
                    new BigDecimal(actualEa.getLiability_short_term_liabilities_deferred_income());
            // 1540 Оценочные обязательства
            BigDecimal liability_Short_Term_Liabilities_Estimated_Liabilities =
                    new BigDecimal(actualEa.getLiability_short_term_liabilities_estimated_liabilities());
            BigDecimal tmp = liability_Short_Term_Liabilities_Total
                    .subtract(liability_Short_Term_Liabilities_Deferred_Income)
                    .subtract(liability_Short_Term_Liabilities_Estimated_Liabilities);
            result = assets_Current_Total.divide(tmp, 2, HALF_UP).toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private String countOperatingRatio(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1300 Капитал и резервы
            BigDecimal liability_Equity_Total = new BigDecimal(actualEa.getLiability_Equity_Total());
            // 1400 Долгосрочные обязательства
            BigDecimal assets_Current_Total = new BigDecimal(actualEa.getAssets_Current_Total());
            // 1500 Краткосрочные обязательства
            BigDecimal liability_Short_Term_Liabilities_Total =
                    new BigDecimal(actualEa.getLiability_Short_Term_Liabilities_Total());
            result = liability_Equity_Total.divide(assets_Current_Total, 2, HALF_UP)
                                           .add(liability_Short_Term_Liabilities_Total)
                                           .toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private String countCurrentAssetsEquity(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1300 Капитал и резервы
            BigDecimal liability_Equity_Total = new BigDecimal(actualEa.getLiability_Equity_Total());
            // 1100 Внеоборотные активы
            BigDecimal assets_Non_Current_Total = new BigDecimal(actualEa.getAssets_Non_Current_Total());
            result = liability_Equity_Total.subtract(assets_Non_Current_Total)
                                           .divide(liability_Equity_Total, 2, HALF_UP)
                                           .toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private String countCapitalizationCoefficient(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1400 Долгосрочные обязательства
            BigDecimal liability_Long_Term_Liabilities_Total =
                    new BigDecimal(actualEa.getLiability_Long_Term_Liabilities_Total());
            // 1500 Краткосрочные обязательства
            BigDecimal liability_Short_Term_Liabilities_Total =
                    new BigDecimal(actualEa.getLiability_Short_Term_Liabilities_Total());
            // 1300 Капитал и резервы
            BigDecimal liability_Equity_Total = new BigDecimal(actualEa.getLiability_Equity_Total());
            result = liability_Long_Term_Liabilities_Total.add(liability_Short_Term_Liabilities_Total)
                                                          .divide(liability_Equity_Total, 2, HALF_UP)
                                                          .toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private String countCurrentRatio(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1200 Оборотные активы
            BigDecimal assets_Current_Total = new BigDecimal(actualEa.getAssets_Current_Total());
            // 1500 Краткосрочные обязательства
            BigDecimal liability_Short_Term_Liabilities_Total =
                    new BigDecimal(actualEa.getLiability_Short_Term_Liabilities_Total());
            // 1231 Долгосрочная дебиторская задолженность
            BigDecimal assets_Current_Long_Term_Receivables =
                    new BigDecimal(actualEa.getAssets_current_long_term_receivables());
            result = assets_Current_Total.divide(liability_Short_Term_Liabilities_Total, 2, HALF_UP)
                                         .subtract(assets_Current_Long_Term_Receivables)
                                         .toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private String countCurToFixedAssetsRatio(CleanExpressAnalysisModel actualEa) {
        String result;
        try {
            // 1200 Оборотные активы
            BigDecimal assets_Current_Total = new BigDecimal(actualEa.getAssets_Current_Total());
            // 1150 Основные средства (в т.ч. НЗ)
            BigDecimal assets_non_current_fixed_assets =
                    new BigDecimal(actualEa.getAssets_non_current_fixed_assets());
            result = assets_Current_Total.divide(assets_non_current_fixed_assets, 2, HALF_UP)
                                         .toString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanExpressAnalysisModel) {
            CleanExpressAnalysisModel currentEa = (CleanExpressAnalysisModel) model;
            actualEa = selectActualExpressAnalysis(actualEa, currentEa);
            return emptyMap();
        }

        if (actualEa != null && model instanceof CleanWeeklyStatusPDZFlatModel) {
            Map<String, String> reports = new HashMap<>();

            CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
            LocalDate lastEaDate = actualEa.getEventDate().toLocalDate();
            LocalDate currentEaDate = pdz.getEventDate().toLocalDate();
            long deltaDays = DAYS.between(lastEaDate, currentEaDate);
            reports.put(EXP_DAYS_SINCE_PUB, Long.toString(deltaDays));
            putSimpleReports(reports, actualEa);
            putComplexReports(reports, actualEa);
            reports.values().removeIf(item -> (item == null || "null".equals(item)));
            return reports;
        }

        return emptyMap();
    }
}
