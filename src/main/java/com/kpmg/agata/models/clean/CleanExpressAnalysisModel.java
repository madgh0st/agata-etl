package com.kpmg.agata.models.clean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;
import java.util.Objects;

public class CleanExpressAnalysisModel extends AbstractCleanDataModel {

    private String year;
    private String name;
    private String norm_name;
    //Long
    private String inn;
    private String contractor_code;
    private String member_of_special_group;
    private String department;
    private String industry;
    //String MM/dd/YY
    private String slice_date_1;
    //String MM/dd/YY
    private String slice_date_2;
    private String contractor_class;
    private String solvency_group;
    //Long
    private String points;
    //Double
    private String credit_limit;
    //Date dd/MM/YY
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date limit_estimation_rsbu;
    //Long
    private String fin_metr_coverage_coefficient_points;
    //Long
    private String fin_metr_hot_liquidity_coefficient_points;
    //Long
    private String fin_metr_capital_turnover_points;
    //Long
    private String fin_metr_selling_profitability_points;
    //Long
    private String fin_metr_assets_profitability_points;
    //Long
    private String fin_metr_financial_stability_coefficient_points;
    private String bus_rep_cooperation_experience_category;
    //Long
    private String bus_rep_cooperation_experience_points;
    private String bus_rep_payment_discipline_category;
    //Long
    private String bus_rep_payment_discipline_points;
    //Long
    private String calculation_non_current_assets;
    //Long
    private String calculation_net_assets;
    //Long
    private String calculation_current_assets;
    //Long
    private String calculation_current_liabilities;
    //Date dd/MM/YY
    private String rsbu_financial_statements;
    //Long
    private String assets_non_current_intangible_assets;
    //Long nullable
    private String assets_non_current_rnd_results;
    //Long nullable
    private String assets_non_current_non_material_research_assets;
    //Long nullable
    private String assets_non_current_material_research_assets;
    // 1150 Основные средства (в т.ч. НЗ)
    private String assets_non_current_fixed_assets;
    //Long
    private String assets_non_current_ibp_into_tangible_assets;
    //Long
    private String assets_non_current_financial_investments;
    //Long
    private String assets_non_current_deferred_tax_asset;
    //Long
    private String assets_non_current_other_assets;
    //Long
    private String assets_current_inventory;
    //Long
    private String assets_current_vat_on_acquired_valuables;
    //Long
    private String assets_current_receivables;
    //Long
    private String assets_current_long_term_receivables;
    //Long nullable
    private String assets_currentdebts_of_founders;
    //Long
    private String assets_current_financial_investment;
    //Long
    private String assets_current_cash;
    //Long
    private String assets_current_other;
    //Long
    private String liability_equity_statutory_capital;
    //Long nullable
    private String liability_equity_own_stock;
    //Long nullable
    private String liability_equity_non_current_assets_revaluation;
    //Long nullable
    private String liability_equity_equity_reserve;
    //Long negative
    private String liability_equity_retained_earnings;
    //long
    private String liability_long_term_liabilities_loans_and_borrowings;
    //Long
    private String liability_long_term_liabilities_deferred_tax_liabilities;
    //Long nullable
    private String liability_long_term_liabilities_estimated_liabilities;
    private String liability_long_term_liabilities_received_advances_payables;
    //Long nullable
    private String liability_long_term_liabilities_other_liabilities;
    //Long
    private String liability_short_term_liabilities_loans_and_borrowings;
    private String liability_short_term_liabilities_accounts_payable;
    private String liability_short_term_liabilities_deferred_income;
    //Long nullable
    private String liability_short_term_liabilities_estimated_liabilities;
    //Long nullable
    private String liability_short_term_liabilities_other_liabilities;
    //Long
    private String pnl_net_revenue;
    //Long
    private String pnl_operating_profit;
    //Long
    private String pnl_year_net_profit;
    //Long
    private String statement_of_changes_in_equity_net_assets;
    private String rsbu_financial_statements_currency;
    private String error_status;
    // 1200 Оборотные активы
    private String assets_Current_Total;
    // 1500 Краткосрочные обязательства
    private String liability_Short_Term_Liabilities_Total;
    // 1400 Долгосрочные обязательства
    private String liability_Long_Term_Liabilities_Total;
    // 1300 Капитал и резервы
    private String liability_Equity_Total;
    // 1100 Внеоборотные активы
    private String assets_Non_Current_Total;
    // 1600 ИТОГО АКТИВ
    private String liabilities_Total;
    private String assets_Total;
    private Double cover_Ratio;
    private Double quick_Liquidity_Ratio;
    private Double working_Capital_Turnover;
    private Double financial_Stability_Index;
    private Double margin_On_Sales;
    private Double return_On_Assets;

    public CleanExpressAnalysisModel() {
    }

    public Double getQuick_Liquidity_Ratio() {
        return quick_Liquidity_Ratio;
    }

    public Double getCover_Ratio() {
        return cover_Ratio;
    }

    public void setCover_Ratio(Double cover_Ratio) {
        this.cover_Ratio = cover_Ratio;
    }

    public void setQuick_Liquidity_Ratio(Double quick_Liquidity_Ratio) {
        this.quick_Liquidity_Ratio = quick_Liquidity_Ratio;
    }

    public Double getWorking_Capital_Turnover() {
        return working_Capital_Turnover;
    }

    public void setWorking_Capital_Turnover(Double working_Capital_Turnover) {
        this.working_Capital_Turnover = working_Capital_Turnover;
    }

    public Double getFinancial_Stability_Index() {
        return financial_Stability_Index;
    }

    public void setFinancial_Stability_Index(Double financial_Stability_Index) {
        this.financial_Stability_Index = financial_Stability_Index;
    }

    public Double getMargin_On_Sales() {
        return margin_On_Sales;
    }

    public void setMargin_On_Sales(Double margin_On_Sales) {
        this.margin_On_Sales = margin_On_Sales;
    }

    public Double getReturn_On_Assets() {
        return return_On_Assets;
    }

    public void setReturn_On_Assets(Double return_On_Assets) {
        this.return_On_Assets = return_On_Assets;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNorm_name() {
        return norm_name;
    }

    public void setNorm_name(String norm_name) {
        this.norm_name = norm_name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getContractor_code() {
        return contractor_code;
    }

    public void setContractor_code(String contractor_code) {
        this.contractor_code = contractor_code;
    }

    public String getMember_of_special_group() {
        return member_of_special_group;
    }

    public void setMember_of_special_group(String member_of_special_group) {
        this.member_of_special_group = member_of_special_group;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSlice_date_1() {
        return slice_date_1;
    }

    public void setSlice_date_1(String slice_date_1) {
        this.slice_date_1 = slice_date_1;
    }

    public String getSlice_date_2() {
        return slice_date_2;
    }

    public void setSlice_date_2(String slice_date_2) {
        this.slice_date_2 = slice_date_2;
    }

    public String getContractor_class() {
        return contractor_class;
    }

    public void setContractor_class(String contractor_class) {
        this.contractor_class = contractor_class;
    }

    public String getSolvency_group() {
        return solvency_group;
    }

    public void setSolvency_group(String solvency_group) {
        this.solvency_group = solvency_group;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(String credit_limit) {
        this.credit_limit = credit_limit;
    }

    public Date getLimit_estimation_rsbu() {
        return limit_estimation_rsbu;
    }

    public void setLimit_estimation_rsbu(Date limit_estimation_rsbu) {
        this.limit_estimation_rsbu = limit_estimation_rsbu;
    }

    public String getFin_metr_coverage_coefficient_points() {
        return fin_metr_coverage_coefficient_points;
    }

    public void setFin_metr_coverage_coefficient_points(String fin_metr_coverage_coefficient_points) {
        this.fin_metr_coverage_coefficient_points = fin_metr_coverage_coefficient_points;
    }

    public String getFin_metr_hot_liquidity_coefficient_points() {
        return fin_metr_hot_liquidity_coefficient_points;
    }

    public void setFin_metr_hot_liquidity_coefficient_points(String fin_metr_hot_liquidity_coefficient_points) {
        this.fin_metr_hot_liquidity_coefficient_points = fin_metr_hot_liquidity_coefficient_points;
    }

    public String getFin_metr_capital_turnover_points() {
        return fin_metr_capital_turnover_points;
    }

    public void setFin_metr_capital_turnover_points(String fin_metr_capital_turnover_points) {
        this.fin_metr_capital_turnover_points = fin_metr_capital_turnover_points;
    }

    public String getFin_metr_selling_profitability_points() {
        return fin_metr_selling_profitability_points;
    }

    public void setFin_metr_selling_profitability_points(String fin_metr_selling_profitability_points) {
        this.fin_metr_selling_profitability_points = fin_metr_selling_profitability_points;
    }

    public String getFin_metr_assets_profitability_points() {
        return fin_metr_assets_profitability_points;
    }

    public void setFin_metr_assets_profitability_points(String fin_metr_assets_profitability_points) {
        this.fin_metr_assets_profitability_points = fin_metr_assets_profitability_points;
    }

    public String getFin_metr_financial_stability_coefficient_points() {
        return fin_metr_financial_stability_coefficient_points;
    }

    public void setFin_metr_financial_stability_coefficient_points(
            String fin_metr_financial_stability_coefficient_points) {
        this.fin_metr_financial_stability_coefficient_points = fin_metr_financial_stability_coefficient_points;
    }

    public String getBus_rep_cooperation_experience_category() {
        return bus_rep_cooperation_experience_category;
    }

    public void setBus_rep_cooperation_experience_category(String bus_rep_cooperation_experience_category) {
        this.bus_rep_cooperation_experience_category = bus_rep_cooperation_experience_category;
    }

    public String getBus_rep_cooperation_experience_points() {
        return bus_rep_cooperation_experience_points;
    }

    public void setBus_rep_cooperation_experience_points(String bus_rep_cooperation_experience_points) {
        this.bus_rep_cooperation_experience_points = bus_rep_cooperation_experience_points;
    }

    public String getBus_rep_payment_discipline_category() {
        return bus_rep_payment_discipline_category;
    }

    public void setBus_rep_payment_discipline_category(String bus_rep_payment_discipline_category) {
        this.bus_rep_payment_discipline_category = bus_rep_payment_discipline_category;
    }

    public String getBus_rep_payment_discipline_points() {
        return bus_rep_payment_discipline_points;
    }

    public void setBus_rep_payment_discipline_points(String bus_rep_payment_discipline_points) {
        this.bus_rep_payment_discipline_points = bus_rep_payment_discipline_points;
    }

    public String getCalculation_non_current_assets() {
        return calculation_non_current_assets;
    }

    public void setCalculation_non_current_assets(String calculation_non_current_assets) {
        this.calculation_non_current_assets = calculation_non_current_assets;
    }

    public String getCalculation_net_assets() {
        return calculation_net_assets;
    }

    public void setCalculation_net_assets(String calculation_net_assets) {
        this.calculation_net_assets = calculation_net_assets;
    }

    public String getCalculation_current_assets() {
        return calculation_current_assets;
    }

    public void setCalculation_current_assets(String calculation_current_assets) {
        this.calculation_current_assets = calculation_current_assets;
    }

    public String getCalculation_current_liabilities() {
        return calculation_current_liabilities;
    }

    public void setCalculation_current_liabilities(String calculation_current_liabilities) {
        this.calculation_current_liabilities = calculation_current_liabilities;
    }

    public String getRsbu_financial_statements() {
        return rsbu_financial_statements;
    }

    public void setRsbu_financial_statements(String rsbu_financial_statements) {
        this.rsbu_financial_statements = rsbu_financial_statements;
    }

    public String getAssets_non_current_intangible_assets() {
        return assets_non_current_intangible_assets;
    }

    public void setAssets_non_current_intangible_assets(String assets_non_current_intangible_assets) {
        this.assets_non_current_intangible_assets = assets_non_current_intangible_assets;
    }

    public String getAssets_non_current_rnd_results() {
        return assets_non_current_rnd_results;
    }

    public void setAssets_non_current_rnd_results(String assets_non_current_rnd_results) {
        this.assets_non_current_rnd_results = assets_non_current_rnd_results;
    }

    public String getAssets_non_current_non_material_research_assets() {
        return assets_non_current_non_material_research_assets;
    }

    public void setAssets_non_current_non_material_research_assets(
            String assets_non_current_non_material_research_assets) {
        this.assets_non_current_non_material_research_assets = assets_non_current_non_material_research_assets;
    }

    public String getAssets_non_current_material_research_assets() {
        return assets_non_current_material_research_assets;
    }

    public void setAssets_non_current_material_research_assets(String assets_non_current_material_research_assets) {
        this.assets_non_current_material_research_assets = assets_non_current_material_research_assets;
    }

    public String getAssets_non_current_fixed_assets() {
        return assets_non_current_fixed_assets;
    }

    public void setAssets_non_current_fixed_assets(String assets_non_current_fixed_assets) {
        this.assets_non_current_fixed_assets = assets_non_current_fixed_assets;
    }

    public String getAssets_non_current_ibp_into_tangible_assets() {
        return assets_non_current_ibp_into_tangible_assets;
    }

    public void setAssets_non_current_ibp_into_tangible_assets(String assets_non_current_ibp_into_tangible_assets) {
        this.assets_non_current_ibp_into_tangible_assets = assets_non_current_ibp_into_tangible_assets;
    }

    public String getAssets_non_current_financial_investments() {
        return assets_non_current_financial_investments;
    }

    public void setAssets_non_current_financial_investments(String assets_non_current_financial_investments) {
        this.assets_non_current_financial_investments = assets_non_current_financial_investments;
    }

    public String getAssets_non_current_deferred_tax_asset() {
        return assets_non_current_deferred_tax_asset;
    }

    public void setAssets_non_current_deferred_tax_asset(String assets_non_current_deferred_tax_asset) {
        this.assets_non_current_deferred_tax_asset = assets_non_current_deferred_tax_asset;
    }

    public String getAssets_non_current_other_assets() {
        return assets_non_current_other_assets;
    }

    public void setAssets_non_current_other_assets(String assets_non_current_other_assets) {
        this.assets_non_current_other_assets = assets_non_current_other_assets;
    }

    public String getAssets_current_inventory() {
        return assets_current_inventory;
    }

    public void setAssets_current_inventory(String assets_current_inventory) {
        this.assets_current_inventory = assets_current_inventory;
    }

    public String getAssets_current_vat_on_acquired_valuables() {
        return assets_current_vat_on_acquired_valuables;
    }

    public void setAssets_current_vat_on_acquired_valuables(String assets_current_vat_on_acquired_valuables) {
        this.assets_current_vat_on_acquired_valuables = assets_current_vat_on_acquired_valuables;
    }

    public String getAssets_current_receivables() {
        return assets_current_receivables;
    }

    public void setAssets_current_receivables(String assets_current_receivables) {
        this.assets_current_receivables = assets_current_receivables;
    }

    public String getAssets_current_long_term_receivables() {
        return assets_current_long_term_receivables;
    }

    public void setAssets_current_long_term_receivables(String assets_current_long_term_receivables) {
        this.assets_current_long_term_receivables = assets_current_long_term_receivables;
    }

    public String getAssets_currentdebts_of_founders() {
        return assets_currentdebts_of_founders;
    }

    public void setAssets_currentdebts_of_founders(String assets_currentdebts_of_founders) {
        this.assets_currentdebts_of_founders = assets_currentdebts_of_founders;
    }

    public String getAssets_current_financial_investment() {
        return assets_current_financial_investment;
    }

    public void setAssets_current_financial_investment(String assets_current_financial_investment) {
        this.assets_current_financial_investment = assets_current_financial_investment;
    }

    public String getAssets_current_cash() {
        return assets_current_cash;
    }

    public void setAssets_current_cash(String assets_current_cash) {
        this.assets_current_cash = assets_current_cash;
    }

    public String getAssets_current_other() {
        return assets_current_other;
    }

    public void setAssets_current_other(String assets_current_other) {
        this.assets_current_other = assets_current_other;
    }

    public String getLiability_equity_statutory_capital() {
        return liability_equity_statutory_capital;
    }

    public void setLiability_equity_statutory_capital(String liability_equity_statutory_capital) {
        this.liability_equity_statutory_capital = liability_equity_statutory_capital;
    }

    public String getLiability_equity_own_stock() {
        return liability_equity_own_stock;
    }

    public void setLiability_equity_own_stock(String liability_equity_own_stock) {
        this.liability_equity_own_stock = liability_equity_own_stock;
    }

    public String getLiability_equity_non_current_assets_revaluation() {
        return liability_equity_non_current_assets_revaluation;
    }

    public void setLiability_equity_non_current_assets_revaluation(
            String liability_equity_non_current_assets_revaluation) {
        this.liability_equity_non_current_assets_revaluation = liability_equity_non_current_assets_revaluation;
    }

    public String getLiability_equity_equity_reserve() {
        return liability_equity_equity_reserve;
    }

    public void setLiability_equity_equity_reserve(String liability_equity_equity_reserve) {
        this.liability_equity_equity_reserve = liability_equity_equity_reserve;
    }

    public String getLiability_equity_retained_earnings() {
        return liability_equity_retained_earnings;
    }

    public void setLiability_equity_retained_earnings(String liability_equity_retained_earnings) {
        this.liability_equity_retained_earnings = liability_equity_retained_earnings;
    }

    public String getLiability_long_term_liabilities_loans_and_borrowings() {
        return liability_long_term_liabilities_loans_and_borrowings;
    }

    public void setLiability_long_term_liabilities_loans_and_borrowings(
            String liability_long_term_liabilities_loans_and_borrowings) {
        this.liability_long_term_liabilities_loans_and_borrowings =
                liability_long_term_liabilities_loans_and_borrowings;
    }

    public String getLiability_long_term_liabilities_deferred_tax_liabilities() {
        return liability_long_term_liabilities_deferred_tax_liabilities;
    }

    public void setLiability_long_term_liabilities_deferred_tax_liabilities(
            String liability_long_term_liabilities_deferred_tax_liabilities) {
        this.liability_long_term_liabilities_deferred_tax_liabilities =
                liability_long_term_liabilities_deferred_tax_liabilities;
    }

    public String getLiability_long_term_liabilities_estimated_liabilities() {
        return liability_long_term_liabilities_estimated_liabilities;
    }

    public void setLiability_long_term_liabilities_estimated_liabilities(
            String liability_long_term_liabilities_estimated_liabilities) {
        this.liability_long_term_liabilities_estimated_liabilities =
                liability_long_term_liabilities_estimated_liabilities;
    }

    public String getLiability_long_term_liabilities_received_advances_payables() {
        return liability_long_term_liabilities_received_advances_payables;
    }

    public void setLiability_long_term_liabilities_received_advances_payables(
            String liability_long_term_liabilities_received_advances_payables) {
        this.liability_long_term_liabilities_received_advances_payables =
                liability_long_term_liabilities_received_advances_payables;
    }

    public String getLiability_long_term_liabilities_other_liabilities() {
        return liability_long_term_liabilities_other_liabilities;
    }

    public void setLiability_long_term_liabilities_other_liabilities(
            String liability_long_term_liabilities_other_liabilities) {
        this.liability_long_term_liabilities_other_liabilities = liability_long_term_liabilities_other_liabilities;
    }

    public String getLiability_short_term_liabilities_loans_and_borrowings() {
        return liability_short_term_liabilities_loans_and_borrowings;
    }

    public void setLiability_short_term_liabilities_loans_and_borrowings(
            String liability_short_term_liabilities_loans_and_borrowings) {
        this.liability_short_term_liabilities_loans_and_borrowings =
                liability_short_term_liabilities_loans_and_borrowings;
    }

    public String getLiability_short_term_liabilities_accounts_payable() {
        return liability_short_term_liabilities_accounts_payable;
    }

    public void setLiability_short_term_liabilities_accounts_payable(
            String liability_short_term_liabilities_accounts_payable) {
        this.liability_short_term_liabilities_accounts_payable = liability_short_term_liabilities_accounts_payable;
    }

    public String getLiability_short_term_liabilities_deferred_income() {
        return liability_short_term_liabilities_deferred_income;
    }

    public void setLiability_short_term_liabilities_deferred_income(
            String liability_short_term_liabilities_deferred_income) {
        this.liability_short_term_liabilities_deferred_income = liability_short_term_liabilities_deferred_income;
    }

    public String getLiability_short_term_liabilities_estimated_liabilities() {
        return liability_short_term_liabilities_estimated_liabilities;
    }

    public void setLiability_short_term_liabilities_estimated_liabilities(
            String liability_short_term_liabilities_estimated_liabilities) {
        this.liability_short_term_liabilities_estimated_liabilities =
                liability_short_term_liabilities_estimated_liabilities;
    }

    public String getLiability_short_term_liabilities_other_liabilities() {
        return liability_short_term_liabilities_other_liabilities;
    }

    public void setLiability_short_term_liabilities_other_liabilities(
            String liability_short_term_liabilities_other_liabilities) {
        this.liability_short_term_liabilities_other_liabilities = liability_short_term_liabilities_other_liabilities;
    }

    public String getPnl_net_revenue() {
        return pnl_net_revenue;
    }

    public void setPnl_net_revenue(String pnl_net_revenue) {
        this.pnl_net_revenue = pnl_net_revenue;
    }

    public String getPnl_operating_profit() {
        return pnl_operating_profit;
    }

    public void setPnl_operating_profit(String pnl_operating_profit) {
        this.pnl_operating_profit = pnl_operating_profit;
    }

    public String getPnl_year_net_profit() {
        return pnl_year_net_profit;
    }

    public void setPnl_year_net_profit(String pnl_year_net_profit) {
        this.pnl_year_net_profit = pnl_year_net_profit;
    }

    public String getStatement_of_changes_in_equity_net_assets() {
        return statement_of_changes_in_equity_net_assets;
    }

    public void setStatement_of_changes_in_equity_net_assets(String statement_of_changes_in_equity_net_assets) {
        this.statement_of_changes_in_equity_net_assets = statement_of_changes_in_equity_net_assets;
    }

    public String getRsbu_financial_statements_currency() {
        return rsbu_financial_statements_currency;
    }

    public void setRsbu_financial_statements_currency(String rsbu_financial_statements_currency) {
        this.rsbu_financial_statements_currency = rsbu_financial_statements_currency;
    }

    public String getError_status() {
        return error_status;
    }

    public void setError_status(String error_status) {
        this.error_status = error_status;
    }

    public String getLiability_Short_Term_Liabilities_Total() {
        return liability_Short_Term_Liabilities_Total;
    }

    public void setLiability_Short_Term_Liabilities_Total(String liability_Short_Term_Liabilities_Total) {
        this.liability_Short_Term_Liabilities_Total = liability_Short_Term_Liabilities_Total;
    }

    public String getLiability_Long_Term_Liabilities_Total() {
        return liability_Long_Term_Liabilities_Total;
    }

    public void setLiability_Long_Term_Liabilities_Total(String liability_Long_Term_Liabilities_Total) {
        this.liability_Long_Term_Liabilities_Total = liability_Long_Term_Liabilities_Total;
    }

    public String getLiability_Equity_Total() {
        return liability_Equity_Total;
    }

    public void setLiability_Equity_Total(String liability_Equity_Total) {
        this.liability_Equity_Total = liability_Equity_Total;
    }

    public String getAssets_Non_Current_Total() {
        return assets_Non_Current_Total;
    }

    public void setAssets_Non_Current_Total(String assets_Non_Current_Total) {
        this.assets_Non_Current_Total = assets_Non_Current_Total;
    }

    public String getAssets_Total() {
        return assets_Total;
    }

    public void setAssets_Total(String assets_Total) {
        this.assets_Total = assets_Total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanExpressAnalysisModel that = (CleanExpressAnalysisModel) o;
        return Objects.equals(year, that.year) &&
                Objects.equals(name, that.name) &&
                Objects.equals(norm_name, that.norm_name) &&
                Objects.equals(inn, that.inn) &&
                Objects.equals(contractor_code, that.contractor_code) &&
                Objects.equals(member_of_special_group, that.member_of_special_group) &&
                Objects.equals(department, that.department) &&
                Objects.equals(industry, that.industry) &&
                Objects.equals(slice_date_1, that.slice_date_1) &&
                Objects.equals(slice_date_2, that.slice_date_2) &&
                Objects.equals(contractor_class, that.contractor_class) &&
                Objects.equals(solvency_group, that.solvency_group) &&
                Objects.equals(points, that.points) &&
                Objects.equals(credit_limit, that.credit_limit) &&
                Objects.equals(limit_estimation_rsbu, that.limit_estimation_rsbu) &&
                Objects.equals(fin_metr_coverage_coefficient_points, that.fin_metr_coverage_coefficient_points) &&
                Objects.equals(fin_metr_hot_liquidity_coefficient_points,
                        that.fin_metr_hot_liquidity_coefficient_points) &&
                Objects.equals(fin_metr_capital_turnover_points, that.fin_metr_capital_turnover_points) &&
                Objects
                        .equals(fin_metr_selling_profitability_points, that.fin_metr_selling_profitability_points) &&
                Objects.equals(fin_metr_assets_profitability_points, that.fin_metr_assets_profitability_points) &&
                Objects.equals(fin_metr_financial_stability_coefficient_points,
                        that.fin_metr_financial_stability_coefficient_points) &&
                Objects
                        .equals(bus_rep_cooperation_experience_category,
                                that.bus_rep_cooperation_experience_category) &&
                Objects
                        .equals(bus_rep_cooperation_experience_points, that.bus_rep_cooperation_experience_points) &&
                Objects.equals(bus_rep_payment_discipline_category, that.bus_rep_payment_discipline_category) &&
                Objects.equals(bus_rep_payment_discipline_points, that.bus_rep_payment_discipline_points) &&
                Objects.equals(calculation_non_current_assets, that.calculation_non_current_assets) &&
                Objects.equals(calculation_net_assets, that.calculation_net_assets) &&
                Objects.equals(calculation_current_assets, that.calculation_current_assets) &&
                Objects.equals(calculation_current_liabilities, that.calculation_current_liabilities) &&
                Objects.equals(rsbu_financial_statements, that.rsbu_financial_statements) &&
                Objects.equals(assets_non_current_intangible_assets, that.assets_non_current_intangible_assets) &&
                Objects.equals(assets_non_current_rnd_results, that.assets_non_current_rnd_results) &&
                Objects.equals(assets_non_current_non_material_research_assets,
                        that.assets_non_current_non_material_research_assets) &&
                Objects.equals(assets_non_current_material_research_assets,
                        that.assets_non_current_material_research_assets) &&
                Objects.equals(assets_non_current_fixed_assets, that.assets_non_current_fixed_assets) &&
                Objects.equals(assets_non_current_ibp_into_tangible_assets,
                        that.assets_non_current_ibp_into_tangible_assets) &&
                Objects
                        .equals(assets_non_current_financial_investments,
                                that.assets_non_current_financial_investments) &&
                Objects
                        .equals(assets_non_current_deferred_tax_asset, that.assets_non_current_deferred_tax_asset) &&
                Objects.equals(assets_non_current_other_assets, that.assets_non_current_other_assets) &&
                Objects.equals(assets_current_inventory, that.assets_current_inventory) &&
                Objects
                        .equals(assets_current_vat_on_acquired_valuables,
                                that.assets_current_vat_on_acquired_valuables) &&
                Objects.equals(assets_current_receivables, that.assets_current_receivables) &&
                Objects.equals(assets_current_long_term_receivables, that.assets_current_long_term_receivables) &&
                Objects.equals(assets_currentdebts_of_founders, that.assets_currentdebts_of_founders) &&
                Objects.equals(assets_current_financial_investment, that.assets_current_financial_investment) &&
                Objects.equals(assets_current_cash, that.assets_current_cash) &&
                Objects.equals(assets_current_other, that.assets_current_other) &&
                Objects.equals(liability_equity_statutory_capital, that.liability_equity_statutory_capital) &&
                Objects.equals(liability_equity_own_stock, that.liability_equity_own_stock) &&
                Objects.equals(liability_equity_non_current_assets_revaluation,
                        that.liability_equity_non_current_assets_revaluation) &&
                Objects.equals(liability_equity_equity_reserve, that.liability_equity_equity_reserve) &&
                Objects.equals(liability_equity_retained_earnings, that.liability_equity_retained_earnings) &&
                Objects.equals(liability_long_term_liabilities_loans_and_borrowings,
                        that.liability_long_term_liabilities_loans_and_borrowings) &&
                Objects.equals(liability_long_term_liabilities_deferred_tax_liabilities,
                        that.liability_long_term_liabilities_deferred_tax_liabilities) &&
                Objects.equals(liability_long_term_liabilities_estimated_liabilities,
                        that.liability_long_term_liabilities_estimated_liabilities) &&
                Objects.equals(liability_long_term_liabilities_received_advances_payables,
                        that.liability_long_term_liabilities_received_advances_payables) &&
                Objects.equals(liability_long_term_liabilities_other_liabilities,
                        that.liability_long_term_liabilities_other_liabilities) &&
                Objects.equals(liability_short_term_liabilities_loans_and_borrowings,
                        that.liability_short_term_liabilities_loans_and_borrowings) &&
                Objects.equals(liability_short_term_liabilities_accounts_payable,
                        that.liability_short_term_liabilities_accounts_payable) &&
                Objects.equals(liability_short_term_liabilities_deferred_income,
                        that.liability_short_term_liabilities_deferred_income) &&
                Objects.equals(liability_short_term_liabilities_estimated_liabilities,
                        that.liability_short_term_liabilities_estimated_liabilities) &&
                Objects.equals(liability_short_term_liabilities_other_liabilities,
                        that.liability_short_term_liabilities_other_liabilities) &&
                Objects.equals(pnl_net_revenue, that.pnl_net_revenue) &&
                Objects.equals(pnl_operating_profit, that.pnl_operating_profit) &&
                Objects.equals(pnl_year_net_profit, that.pnl_year_net_profit) &&
                Objects.equals(statement_of_changes_in_equity_net_assets,
                        that.statement_of_changes_in_equity_net_assets) &&
                Objects.equals(rsbu_financial_statements_currency, that.rsbu_financial_statements_currency) &&
                Objects.equals(error_status, that.error_status) &&
                Objects.equals(assets_Current_Total, that.assets_Current_Total) &&
                Objects
                        .equals(liability_Short_Term_Liabilities_Total, that.liability_Short_Term_Liabilities_Total) &&
                Objects
                        .equals(liability_Long_Term_Liabilities_Total, that.liability_Long_Term_Liabilities_Total) &&
                Objects.equals(liability_Equity_Total, that.liability_Equity_Total) &&
                Objects.equals(assets_Non_Current_Total, that.assets_Non_Current_Total) &&
                Objects.equals(liabilities_Total, that.liabilities_Total) &&
                Objects.equals(assets_Total, that.assets_Total) &&
                Objects.equals(cover_Ratio, that.cover_Ratio) &&
                Objects.equals(quick_Liquidity_Ratio, that.quick_Liquidity_Ratio) &&
                Objects.equals(working_Capital_Turnover, that.working_Capital_Turnover) &&
                Objects.equals(financial_Stability_Index, that.financial_Stability_Index) &&
                Objects.equals(margin_On_Sales, that.margin_On_Sales) &&
                Objects.equals(return_On_Assets, that.return_On_Assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, name, norm_name, inn, contractor_code, member_of_special_group, department, industry,
                slice_date_1, slice_date_2, contractor_class, solvency_group, points, credit_limit,
                limit_estimation_rsbu,
                fin_metr_coverage_coefficient_points, fin_metr_hot_liquidity_coefficient_points,
                fin_metr_capital_turnover_points, fin_metr_selling_profitability_points,
                fin_metr_assets_profitability_points, fin_metr_financial_stability_coefficient_points,
                bus_rep_cooperation_experience_category, bus_rep_cooperation_experience_points,
                bus_rep_payment_discipline_category, bus_rep_payment_discipline_points, calculation_non_current_assets,
                calculation_net_assets, calculation_current_assets, calculation_current_liabilities,
                rsbu_financial_statements, assets_non_current_intangible_assets, assets_non_current_rnd_results,
                assets_non_current_non_material_research_assets, assets_non_current_material_research_assets,
                assets_non_current_fixed_assets, assets_non_current_ibp_into_tangible_assets,
                assets_non_current_financial_investments, assets_non_current_deferred_tax_asset,
                assets_non_current_other_assets, assets_current_inventory, assets_current_vat_on_acquired_valuables,
                assets_current_receivables, assets_current_long_term_receivables, assets_currentdebts_of_founders,
                assets_current_financial_investment, assets_current_cash, assets_current_other,
                liability_equity_statutory_capital, liability_equity_own_stock,
                liability_equity_non_current_assets_revaluation, liability_equity_equity_reserve,
                liability_equity_retained_earnings, liability_long_term_liabilities_loans_and_borrowings,
                liability_long_term_liabilities_deferred_tax_liabilities,
                liability_long_term_liabilities_estimated_liabilities,
                liability_long_term_liabilities_received_advances_payables,
                liability_long_term_liabilities_other_liabilities,
                liability_short_term_liabilities_loans_and_borrowings,
                liability_short_term_liabilities_accounts_payable, liability_short_term_liabilities_deferred_income,
                liability_short_term_liabilities_estimated_liabilities,
                liability_short_term_liabilities_other_liabilities,
                pnl_net_revenue, pnl_operating_profit, pnl_year_net_profit, statement_of_changes_in_equity_net_assets,
                rsbu_financial_statements_currency, error_status, assets_Current_Total,
                liability_Short_Term_Liabilities_Total, liability_Long_Term_Liabilities_Total, liability_Equity_Total,
                assets_Non_Current_Total, liabilities_Total, assets_Total, cover_Ratio, quick_Liquidity_Ratio,
                working_Capital_Turnover, financial_Stability_Index, margin_On_Sales, return_On_Assets);
    }

    public String getAssets_Current_Total() {
        return assets_Current_Total;
    }

    public void setAssets_Current_Total(String assets_Current_Total) {
        this.assets_Current_Total = assets_Current_Total;
    }

    public String getLiabilities_Total() {
        return liabilities_Total;
    }

    public void setLiabilities_Total(String liabilities_Total) {
        this.liabilities_Total = liabilities_Total;
    }

    @Override
    public String toString() {
        return "CleanExpressAnalysisModel{" +
                "year='" + year + '\'' +
                ", name='" + name + '\'' +
                ", norm_name='" + norm_name + '\'' +
                ", inn='" + inn + '\'' +
                ", contractor_code='" + contractor_code + '\'' +
                ", member_of_special_group='" + member_of_special_group + '\'' +
                ", department='" + department + '\'' +
                ", industry='" + industry + '\'' +
                ", slice_date_1='" + slice_date_1 + '\'' +
                ", slice_date_2='" + slice_date_2 + '\'' +
                ", contractor_class='" + contractor_class + '\'' +
                ", solvency_group='" + solvency_group + '\'' +
                ", points='" + points + '\'' +
                ", credit_limit='" + credit_limit + '\'' +
                ", limit_estimation_rsbu=" + limit_estimation_rsbu +
                ", fin_metr_coverage_coefficient_points='" + fin_metr_coverage_coefficient_points + '\'' +
                ", fin_metr_hot_liquidity_coefficient_points='" + fin_metr_hot_liquidity_coefficient_points + '\'' +
                ", fin_metr_capital_turnover_points='" + fin_metr_capital_turnover_points + '\'' +
                ", fin_metr_selling_profitability_points='" + fin_metr_selling_profitability_points + '\'' +
                ", fin_metr_assets_profitability_points='" + fin_metr_assets_profitability_points + '\'' +
                ", fin_metr_financial_stability_coefficient_points='" + fin_metr_financial_stability_coefficient_points + '\'' +
                ", bus_rep_cooperation_experience_category='" + bus_rep_cooperation_experience_category + '\'' +
                ", bus_rep_cooperation_experience_points='" + bus_rep_cooperation_experience_points + '\'' +
                ", bus_rep_payment_discipline_category='" + bus_rep_payment_discipline_category + '\'' +
                ", bus_rep_payment_discipline_points='" + bus_rep_payment_discipline_points + '\'' +
                ", calculation_non_current_assets='" + calculation_non_current_assets + '\'' +
                ", calculation_net_assets='" + calculation_net_assets + '\'' +
                ", calculation_current_assets='" + calculation_current_assets + '\'' +
                ", calculation_current_liabilities='" + calculation_current_liabilities + '\'' +
                ", rsbu_financial_statements='" + rsbu_financial_statements + '\'' +
                ", assets_non_current_intangible_assets='" + assets_non_current_intangible_assets + '\'' +
                ", assets_non_current_rnd_results='" + assets_non_current_rnd_results + '\'' +
                ", assets_non_current_non_material_research_assets='" + assets_non_current_non_material_research_assets + '\'' +
                ", assets_non_current_material_research_assets='" + assets_non_current_material_research_assets + '\'' +
                ", assets_non_current_fixed_assets='" + assets_non_current_fixed_assets + '\'' +
                ", assets_non_current_ibp_into_tangible_assets='" + assets_non_current_ibp_into_tangible_assets + '\'' +
                ", assets_non_current_financial_investments='" + assets_non_current_financial_investments + '\'' +
                ", assets_non_current_deferred_tax_asset='" + assets_non_current_deferred_tax_asset + '\'' +
                ", assets_non_current_other_assets='" + assets_non_current_other_assets + '\'' +
                ", assets_current_inventory='" + assets_current_inventory + '\'' +
                ", assets_current_vat_on_acquired_valuables='" + assets_current_vat_on_acquired_valuables + '\'' +
                ", assets_current_receivables='" + assets_current_receivables + '\'' +
                ", assets_current_long_term_receivables='" + assets_current_long_term_receivables + '\'' +
                ", assets_currentdebts_of_founders='" + assets_currentdebts_of_founders + '\'' +
                ", assets_current_financial_investment='" + assets_current_financial_investment + '\'' +
                ", assets_current_cash='" + assets_current_cash + '\'' +
                ", assets_current_other='" + assets_current_other + '\'' +
                ", liability_equity_statutory_capital='" + liability_equity_statutory_capital + '\'' +
                ", liability_equity_own_stock='" + liability_equity_own_stock + '\'' +
                ", liability_equity_non_current_assets_revaluation='" + liability_equity_non_current_assets_revaluation + '\'' +
                ", liability_equity_equity_reserve='" + liability_equity_equity_reserve + '\'' +
                ", liability_equity_retained_earnings='" + liability_equity_retained_earnings + '\'' +
                ", liability_long_term_liabilities_loans_and_borrowings='" + liability_long_term_liabilities_loans_and_borrowings + '\'' +
                ", liability_long_term_liabilities_deferred_tax_liabilities='" + liability_long_term_liabilities_deferred_tax_liabilities + '\'' +
                ", liability_long_term_liabilities_estimated_liabilities='" + liability_long_term_liabilities_estimated_liabilities + '\'' +
                ", liability_long_term_liabilities_received_advances_payables='" + liability_long_term_liabilities_received_advances_payables + '\'' +
                ", liability_long_term_liabilities_other_liabilities='" + liability_long_term_liabilities_other_liabilities + '\'' +
                ", liability_short_term_liabilities_loans_and_borrowings='" + liability_short_term_liabilities_loans_and_borrowings + '\'' +
                ", liability_short_term_liabilities_accounts_payable='" + liability_short_term_liabilities_accounts_payable + '\'' +
                ", liability_short_term_liabilities_deferred_income='" + liability_short_term_liabilities_deferred_income + '\'' +
                ", liability_short_term_liabilities_estimated_liabilities='" + liability_short_term_liabilities_estimated_liabilities + '\'' +
                ", liability_short_term_liabilities_other_liabilities='" + liability_short_term_liabilities_other_liabilities + '\'' +
                ", pnl_net_revenue='" + pnl_net_revenue + '\'' +
                ", pnl_operating_profit='" + pnl_operating_profit + '\'' +
                ", pnl_year_net_profit='" + pnl_year_net_profit + '\'' +
                ", statement_of_changes_in_equity_net_assets='" + statement_of_changes_in_equity_net_assets + '\'' +
                ", rsbu_financial_statements_currency='" + rsbu_financial_statements_currency + '\'' +
                ", error_status='" + error_status + '\'' +
                ", assets_Current_Total='" + assets_Current_Total + '\'' +
                ", liability_Short_Term_Liabilities_Total='" + liability_Short_Term_Liabilities_Total + '\'' +
                ", liability_Long_Term_Liabilities_Total='" + liability_Long_Term_Liabilities_Total + '\'' +
                ", liability_Equity_Total='" + liability_Equity_Total + '\'' +
                ", assets_Non_Current_Total='" + assets_Non_Current_Total + '\'' +
                ", liabilities_Total='" + liabilities_Total + '\'' +
                ", assets_Total='" + assets_Total + '\'' +
                ", cover_Ratio='" + cover_Ratio + '\'' +
                ", quick_Liquidity_Ratio='" + quick_Liquidity_Ratio + '\'' +
                ", working_Capital_Turnover='" + working_Capital_Turnover + '\'' +
                ", financial_Stability_Index='" + financial_Stability_Index + '\'' +
                ", margin_On_Sales='" + margin_On_Sales + '\'' +
                ", return_On_Assets='" + return_On_Assets + '\'' +
                "} " + super.toString();
    }
}
