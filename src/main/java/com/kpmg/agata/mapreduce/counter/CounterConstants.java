package com.kpmg.agata.mapreduce.counter;

public class CounterConstants {

    private CounterConstants() {
    }

    static final int RANGE_21_DAYS = 21;
    static final int RANGE_30_DAYS = 30;
    static final int RANGE_60_DAYS = 60;
    static final int RANGE_90_DAYS = 90;
    static final int RANGE_180_DAYS = 180;

    /* Support features */
    public static final String HAS_PDZ = "has_pdz";
    public static final String PDZ_21 = "pdz_21";

    /* Pdz features */

    /**
     * Share (percent) of PDZ reports in last 21 days where overdueDebt > 0.
     * If current PDZ report has overdueDebt == 0 then it equals 0.
     * Range of values: 0 - 1
     * Default value 0
     */
    public static final String PCT_21D = "pdz_pct_21d";

    /**
     * Share (percent) of PDZ reports in last 21 days where overdueDebt > 0.
     * Range of values: 0 - 1
     * Default value 0
     */
    public static final String DIST_21D = "pdz_dist_21d";

    /**
     * Equals 1 if was any overdue in last 90 days and 0 if not
     */
    public static final String IS_OVERDUE_90D = "pdz_is_any_overdue_in_90_days";

    /**
     * Amount of PDZ reports where overdue more than 0 in last 90 days
     */
    public static final String OVERDUE_NUM_90D = "pdz_num_overdue_in_90_days";

    /**
     * Amount of days since last overdue more than 0 in last 90 days
     */
    public static final String DAYS_SINCE_LAST_OVERDUE_90D = "pdz_num_days_since_last_overdue_in_90_days";
    public static final String PDZ_COMMENTS = "pdz_comments";
    public static final String PDZ_SEASONALITY = "pdz_seasonality";


    /* Credit Limit features */
    public static final String CL_LIMIT_IN_RUB = "cl_limit_in_rub";
    public static final String CL_HAS_EFF_LIMIT = "cl_has_effective_limit";
    public static final String CL_LIMIT_UTIL = "cl_limit_utilization";
    public static final String CL_OVERDUE_LIMIT_UTIL = "cl_overdue_limit_utilization";
    public static final String CL_EA_RATIO = "cl_ea_ratio";
    public static final String CL_PAY_DEFER_AVG_IN_90_DAYS = "cl_payment_deferment_avg_in_90_days";

    /* Payments and Realization features */

    /**
     * Sum of all payments within ranges
     */
    public static final String PR_PAY_SUM_90 = "pr_payments_sum_in_90_days";
    public static final String PR_PAY_SUM_90_60 = "pr_sum_payments_between_90_and_60_days_ago";
    public static final String PR_PAY_SUM_60_30 = "pr_sum_payments_between_60_and_30_days_ago";
    public static final String PR_PAY_SUM_30 = "pr_sum_payments_between_30_and_0_days_ago";

    /**
     * Sum of all realizations within ranges
     */
    public static final String PR_REAL_SUM_90 = "pr_realizations_sum_in_90_days";
    public static final String PR_REAL_SUM_90_60 = "pr_sum_realizations_between_90_and_60_days_ago";
    public static final String PR_REAL_SUM_60_30 = "pr_sum_realizations_between_60_and_30_days_ago";
    public static final String PR_REAL_SUM_30 = "pr_sum_realizations_between_30_and_0_days_ago";

    /**
     * All realizations minus all payments for the last 90 days
     */
    public static final String PR_DIFF_90 = "pr_diff_in_90_days";
    /**
     * All realizations minus all payments for the last 90 days divided by actual credit limit
     */
    public static final String PR_REL_DIFF_90 = "pr_relative_diff_in_90_days";
    public static final String PR_CUR_TO_LAST_MONTH_REAL_RATIO = "pr_cur_to_last_month_real_ratio";


    /* Express Analysis features */

    public static final String EXP_DAYS_SINCE_PUB = "express_days_since_publication";
    public static final String EXP_INDUSTRY = "express_industry";
    public static final String EXP_CONTRACTOR_CLASS = "express_contractor_class";
    public static final String EXP_SOLVENCY_GROUP = "express_solvency_group";
    public static final String EXP_POINTS = "express_points";

    public static final String EXP_FIN_COVERAGE_COEF_POINTS = "express_fin_metr_coverage_coefficient_points";
    public static final String EXP_FIN_HOT_LIQ_COEF_POINTS = "express_fin_metr_hot_liquidity_coefficient_points";
    public static final String EXP_FIN_CAPITAL_TURNOVER_POINTS = "express_fin_metr_capital_turnover_points";
    public static final String EXP_FIN_SELL_PROFIT_POINTS = "express_fin_metr_selling_profitability_points";
    public static final String EXP_FIN_ASSETS_PROFIT_POINTS = "express_fin_metr_assets_profitability_points";
    public static final String EXP_FIN_STABILITY_COEF_POINTS = "express_fin_metr_financial_stability_coefficient_points";

    public static final String EXP_BUS_COOP_EXP_CATEGORY = "express_bus_rep_cooperation_experience_category";
    public static final String EXP_BUS_COOP_EXP_POINTS = "express_bus_rep_cooperation_experience_points";
    public static final String EXP_BUS_PAY_DISCIPLINE_CATEGORY = "express_bus_rep_payment_discipline_category";
    public static final String EXP_BUS_PAY_DISCIPLINE_POINTS = "express_bus_rep_payment_discipline_points";

    public static final String EXP_CALC_NON_CURR_ASSETS = "express_calculation_non_current_assets";
    public static final String EXP_CALC_NET_ASSETS = "express_calculation_net_assets";
    public static final String EXP_CALC_CURR_ASSETS = "express_calculation_current_assets";
    public static final String EXP_CALC_CURR_LIABILITIES = "express_calculation_current_liabilities";

    // Коэффициент покрытия
    public static final String EXP_COVER_RATIO = "express_cover_ratio";
    // Коэффициент быстрой ликвидности
    public static final String EXP_QUICK_LIQUIDITY_RATIO = "express_quick_liquidity_ratio";
    // Оборачиваемость оборотного капитала
    public static final String EXP_WORKING_CAPITAL_TURNOVER = "express_working_capital_turnover";
    // Коэффициент финансовой устойчивости
    public static final String EXP_FINANCIAL_STABILITY_INDEX = "express_financial_stability_index";
    // Рентабельность продаж
    public static final String EXP_MARGIN_ON_SALES = "express_margin_on_sales";
    // Рентабельность активов
    public static final String EXP_RETURN_ON_ASSETS = "express_return_on_assets";

    // Коэффициент текущей ликвидности
    public static final String EXP_CURRENT_RATIO = "express_current_ratio";
    // Коэффициент капитализации
    public static final String EXP_CAPITALIZATION_COEFFICIENT = "express_capitalization_coefficient";
    // Коэффициент маневренности собственных средств
    public static final String EXP_CURRENT_ASSETS_EQUITY_RATIO = "express_current_assets_equity_ratio";
    // Коэффициент финансирования
    public static final String EXP_OPERATING_RATIO = "express_operating_ratio";
    // Коэффициент задолженности / покрытия задолженности
    public static final String EXP_EXPRESS_DEBT_EQUITY_RATIO = "express_debt_equity_ratio";
    // Коэффициент реальной стоимости имущества / активов
    public static final String EXP_VALUE_OF_PROPERTY_ASSETS_RATIO = "express_value_of_property_assets_ratio";
    // Коэффициент автономии / независимости
    public static final String EXP_EQUITY_TOTAL_ASSETS_RATIO = "express_equity_total_assets_ratio";

    // Отношение оборотных средств (активов) к основным средствам
    public static final String EXP_CUR_TO_FIXED_ASSETS_RATIO = "express_current_to_fixed_assets_ratio";

    /* Legal Cases features */

    /**
     * Amount of lawsuits in last 60 days.
     * Default = 0
     */
    public static final String LC_NUMBER_90D = "lc_number_in_90_days";

    /**
     * If there was at least on lawsuit (1 or 0).
     */
    public static final String LC_IS_ANY_CASE_IN_PAST = "lc_in_past";
    /**
     * If there was at least one lawsuit in 60 days.
     */
    public static final String LC_IS_ANY_CASE_IN_90D = "lc_in_90_days";
    public static final String LC_UNCLOSED_COUNT_90D = "lc_unclosed_count_in_90_days";
    public static final String LC_UPHELD_CLAIMS_90D = "lc_upheld_claims_in_90_days";

    /* Targets */
    public static final String TARGET_1M = "target_pdz_21_1m";
    public static final String TARGET_3M = "target_pdz_21_3m";
    public static final String TARGET_6M = "target_pdz_21_6m";

    static final String FNS_TAX_VIOLATION = "fns_tax_violation_fine";

    static final String FNS_ARREARS_PREFIX = "fns_arrears_";

    /* Genproc features */
    public static final String GENPROC_DAYS_IN_REGISTRY = "genproc_days_in_reg";

    /* FNS Disqualification */
    public static final String FNS_DISQ = "fns_disq";
    public static final String FNS_DISQ_DAYS_SINCE = "fns_disq_days_since";

    /* Spark Interfax Summary */
    public static final String SP_IFAX_STATUS = "sp_ifax_status";
    public static final String SP_IFAX_CL_RATIO = "sp_ifax_cl_ratio";
    public static final String SP_IFAX_CAUTION = "sp_ifax_caution";
    public static final String SP_IFAX_FIN_RISK = "sp_ifax_fin_risk";
    public static final String SP_IFAX_PAYMENT_DISC = "sp_ifax_payment_disc";
    public static final String SP_IFAX_RISK_FACTORS = "sp_ifax_risk_factors";
    public static final String SP_IFAX_PLEDGES = "sp_ifax_pledges";
    public static final String SP_IFAX_LC_COUNT_2Y = "sp_ifax_lc_count_2y";
    public static final String SP_IFAX_LC_CLAIMS_2Y = "sp_ifax_lc_claims_2y";
    public static final String SP_IFAX_LC_DECISIONS_2Y = "sp_ifax_lc_decisions_2y";
    public static final String SP_IFAX_NEWS = "sp_ifax_news";
    public static final String SP_IFAX_DISHONEST_SUPPLIERS = "sp_ifax_dishonest_suppliers";

    /* Fssp features */
    public static final String FSSP_NUM_IN_PAST = "fssp_num_in_past";

    /* Sectoral indices */
    public static final String SECTORAL_INDICES_AVERAGES = "sectoral_indices_averages";

    /* Counterparty Contracts */
    public static final String CONTRACT_DAYS_FROM_EXPECTED_PAYMENT = "contract_days_from_expected_payment";
}
