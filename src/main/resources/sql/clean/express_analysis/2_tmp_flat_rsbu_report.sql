with tmp_union_rsbu_reports as (
	select x.rowid, x.name_do_table as name_do, x.filename, x.col_0, x.col_2, x.col_3, x.col_4, x.col_5, x.col_6, x.col_7
	from (
		{union_replacer}
	) x
),rsbu_report_3_date_flat as (
	select
		r.filename, r.year, r.name_do,
		max(r.rsbu_financial_statements) as rsbu_financial_statements,
		max(r.assetsNonCurrentIntangibleAssets) as assets_Non_Current_Intangible_Assets,
		max(r.assetsNonCurrentRNDResults) as assets_Non_Current_RND_Results,
		max(r.assetsNonCurrentNonMaterialResearchAssets) as assets_Non_Current_Non_Material_Research_Assets,
		max(r.assetsNonCurrentMaterialResearchAssets) as assets_Non_Current_Material_Research_Assets,
		max(r.assetsNonCurrentFixedAssets) as assets_Non_Current_Fixed_Assets,
		max(r.assetsNonCurrentIBPIntoTangibleAssets) as assets_Non_Current_IBP_Into_Tangible_Assets,
		max(r.assetsNonCurrentFinancialInvestments) as assets_Non_Current_Financial_Investments,
		max(r.assetsNonCurrentDeferredTaxAsset) as assets_Non_Current_Deferred_Tax_Asset,
		max(r.assetsNonCurrentOtherAssets) as assets_Non_Current_Other_Assets,
		max(r.assetsCurrentInventory) as assets_Current_Inventory,
		max(r.assetsCurrentVATOnAcquiredValuables) as assets_Current_VAT_On_Acquired_Valuables,
		max(r.assetsCurrentReceivables) as assets_Current_Receivables,
		max(r.assetsCurrentLongTermReceivables) as assets_Current_Long_Term_Receivables,
		max(r.assetsCurrentdebtsOfFounders) as assets_Currentdebts_Of_Founders,
		max(r.assetsCurrentFinancialInvestment) as assets_Current_Financial_Investment,
		max(r.assetsCurrentCash) as assets_Current_Cash,
		max(r.assetsCurrentOther) as assets_Current_Other,
		max(r.liabilityEquityStatutoryCapital) as liability_Equity_Statutory_Capital,
		max(r.liabilityEquityOwnStock) as liability_Equity_Own_Stock,
		max(r.liabilityEquityNonCurrentAssetsRevaluation) as liability_Equity_Non_Current_Assets_Revaluation,
		max(r.liabilityEquityEquityReserve) as liability_Equity_Equity_Reserve,
		max(r.liabilityEquityRetainedEarnings) as liability_Equity_Retained_Earnings,
		max(r.liabilityLongTermLiabilitiesLoansAndBorrowings) as liability_Long_Term_Liabilities_Loans_And_Borrowings,
		max(r.liabilityLongTermLiabilitiesDeferredTaxLiabilities) as liability_Long_Term_Liabilities_Deferred_Tax_Liabilities,
		max(r.liabilityLongTermLiabilitiesEstimatedLiabilities) as liability_Long_Term_Liabilities_Estimated_Liabilities,
		max(r.liabilityLongTermLiabilitiesReceivedAdvancesPayables) as liability_Long_Term_Liabilities_Received_Advances_Payables,
		max(r.liabilityLongTermLiabilitiesOtherLiabilities) as liability_Long_Term_Liabilities_Other_Liabilities,
		max(r.liabilityShortTermLiabilitiesLoansAndBorrowings) as liability_Short_Term_Liabilities_Loans_And_Borrowings,
		max(r.liabilityShortTermLiabilitiesAccountsPayable) as liability_Short_Term_Liabilities_Accounts_Payable,
		max(r.liabilityShortTermLiabilitiesDeferredIncome) as liability_Short_Term_Liabilities_Deferred_Income,
		max(r.liabilityShortTermLiabilitiesEstimatedLiabilities) as liability_Short_Term_Liabilities_Estimated_Liabilities,
		max(r.liabilityShortTermLiabilitiesOtherLiabilities) as liability_Short_Term_Liabilities_Other_Liabilities,
		max(r.pnlNetRevenue) as pnl_Net_Revenue,
		max(r.pnlOperatingProfit) as pnl_Operating_Profit,
		max(r.pnlYearNetProfit) as pnl_Year_Net_Profit,
		max(r.statementOfChangesInEquityNetAssets) as statement_Of_Changes_In_Equity_Net_Assets,
		max(r.assetsCurrentTotal) as assets_Current_Total,
		max(r.liabilityShortTermLiabilitiesTotal) as liability_Short_Term_Liabilities_Total,
		max(r.liabilityLongTermLiabilitiesTotal) as liability_Long_Term_Liabilities_Total,
		max(r.liabilityEquityTotal) as liability_Equity_Total,
		max(r.assetsNonCurrentTotal) as assets_Non_Current_Total,
		max(r.liabilitiesTotal) as liabilities_Total,
		max(r.assetsTotal) as assets_Total,
        cast(max(r.coverRatio) as double) as cover_Ratio,
        cast(max(r.quickLiquidityRatio) as double) as quick_Liquidity_Ratio,
        cast(max(r.workingCapitalTurnover) as double) as working_Capital_Turnover,
        cast(max(r.financialStabilityIndex) as double) as financial_Stability_Index,
        cast(max(r.marginOnSales) as double) as margin_On_Sales,
        cast(max(r.returnOnAssets) as double) as return_On_Assets
		from (
		select filename, 'year_1' as year, name_do,
		CASE WHEN rowid = '4' THEN col_4 END as rsbu_financial_statements,

        CASE WHEN col_0 = 'I. Внеоборотные активы:' THEN col_4 END as assetsNonCurrentTotal,
		CASE WHEN col_3 = '1110' THEN col_4 END as assetsNonCurrentIntangibleAssets,
		CASE WHEN col_3 = '1120' THEN col_4 END as assetsNonCurrentRNDResults,
		CASE WHEN col_3 = '1130' THEN col_4 END as assetsNonCurrentNonMaterialResearchAssets,
		CASE WHEN col_3 = '1140' THEN col_4 END as assetsNonCurrentMaterialResearchAssets,
		CASE WHEN col_3 = '1150' THEN col_4 END as assetsNonCurrentFixedAssets,
		CASE WHEN col_3 = '1160' THEN col_4 END as assetsNonCurrentIBPIntoTangibleAssets,
		CASE WHEN col_3 = '1170' THEN col_4 END as assetsNonCurrentFinancialInvestments,
		CASE WHEN col_3 = '1180' THEN col_4 END as assetsNonCurrentDeferredTaxAsset,
		CASE WHEN col_3 = '1190' THEN col_4 END as assetsNonCurrentOtherAssets,

	    CASE WHEN col_0 = 'II. Оборотные активы' THEN col_4 END as assetsCurrentTotal,
		CASE WHEN col_3 = '1210' THEN col_4 END as assetsCurrentInventory,
		CASE WHEN col_3 = '1220' THEN col_4 END as assetsCurrentVATOnAcquiredValuables,
		CASE WHEN col_3 = '1230' THEN col_4 END as assetsCurrentReceivables,
		CASE WHEN col_3 = '1231' THEN col_4 END as assetsCurrentLongTermReceivables,
		CASE WHEN col_3 = '1237' THEN col_4 END as assetsCurrentdebtsOfFounders,
		CASE WHEN col_3 = '1240' THEN col_4 END as assetsCurrentFinancialInvestment,
		CASE WHEN col_3 = '1250' THEN col_4 END as assetsCurrentCash,
		CASE WHEN col_3 = '1260' THEN col_4 END as assetsCurrentOther,

		CASE WHEN col_0 = 'ИТОГО АКТИВ' THEN col_4 END as assetsTotal,

		CASE WHEN col_0 = 'III. Капитал и резервы:' THEN col_4 END as liabilityEquityTotal,
		CASE WHEN col_3 = '1310' THEN col_4 END as liabilityEquityStatutoryCapital,
		CASE WHEN col_3 = '1320' THEN col_4 END as liabilityEquityOwnStock,
		CASE WHEN col_3 = '1340' THEN col_4 END as liabilityEquityNonCurrentAssetsRevaluation,
		CASE WHEN col_3 = '1360' THEN col_4 END as liabilityEquityEquityReserve,
		CASE WHEN col_3 = '1370' THEN col_4 END as liabilityEquityRetainedEarnings,

		CASE WHEN col_0 = 'IV. Долгосрочные обязательства' THEN col_4 END as liabilityLongTermLiabilitiesTotal,
		CASE WHEN col_3 = '1410' THEN col_4 END as liabilityLongTermLiabilitiesLoansAndBorrowings,
		CASE WHEN col_3 = '1420' THEN col_4 END as liabilityLongTermLiabilitiesDeferredTaxLiabilities,
		CASE WHEN col_3 = '1430' THEN col_4 END as liabilityLongTermLiabilitiesEstimatedLiabilities,
		CASE WHEN col_3 = '1440' THEN col_4 END as liabilityLongTermLiabilitiesReceivedAdvancesPayables,
		CASE WHEN col_3 = '1450' THEN col_4 END as liabilityLongTermLiabilitiesOtherLiabilities,

		CASE WHEN col_0 = 'V. Краткосрочные обязательства:' THEN col_4 END as liabilityShortTermLiabilitiesTotal,
		CASE WHEN col_3 = '1510' THEN col_4 END as liabilityShortTermLiabilitiesLoansAndBorrowings,
		CASE WHEN col_3 = '1520' THEN col_4 END as liabilityShortTermLiabilitiesAccountsPayable,
		CASE WHEN col_3 = '1530' THEN col_4 END as liabilityShortTermLiabilitiesDeferredIncome,
		CASE WHEN col_3 = '1540' THEN col_4 END as liabilityShortTermLiabilitiesEstimatedLiabilities,
		CASE WHEN col_3 = '1550' THEN col_4 END as liabilityShortTermLiabilitiesOtherLiabilities,

        CASE WHEN col_0 = 'ИТОГО ПАССИВ' THEN col_4 END as liabilitiesTotal,

		CASE WHEN col_3 = '2110' THEN col_4 END as pnlNetRevenue,
		CASE WHEN col_3 = '2200' THEN col_4 END as pnlOperatingProfit,
		CASE WHEN col_3 = '2400' THEN col_4 END as pnlYearNetProfit,
		CASE WHEN col_3 = '3600' THEN col_4 END as statementOfChangesInEquityNetAssets,

		CASE WHEN col_0 = 'Кэффициент покрытия' THEN col_4 END as coverRatio,
		CASE WHEN col_0 = 'Коэффициент быстрой ликвидности' THEN col_4 END as quickLiquidityRatio,
		CASE WHEN col_0 = 'Оборачиваемость оборотного капитала' THEN col_4 END as workingCapitalTurnover,
		CASE WHEN col_0 = 'Коэффициент финансовой устойчивости' THEN col_4 END as financialStabilityIndex,
		CASE WHEN col_0 = 'Рентабельность продаж' THEN col_4 END as marginOnSales,
		CASE WHEN col_0 = 'Рентабельность активов' THEN col_4 END as returnOnAssets

		from tmp_union_rsbu_reports
		
			union
		select filename, 'year_2' as year, name_do,
		CASE WHEN rowid = '4' THEN col_5 END as rsbu_financial_statements,

        CASE WHEN col_0 = 'I. Внеоборотные активы:' THEN col_5 END as assetsNonCurrentTotal,
        CASE WHEN col_3 = '1110' THEN col_5 END as assetsNonCurrentIntangibleAssets,
        CASE WHEN col_3 = '1120' THEN col_5 END as assetsNonCurrentRNDResults,
        CASE WHEN col_3 = '1130' THEN col_5 END as assetsNonCurrentNonMaterialResearchAssets,
        CASE WHEN col_3 = '1140' THEN col_5 END as assetsNonCurrentMaterialResearchAssets,
        CASE WHEN col_3 = '1150' THEN col_5 END as assetsNonCurrentFixedAssets,
        CASE WHEN col_3 = '1160' THEN col_5 END as assetsNonCurrentIBPIntoTangibleAssets,
        CASE WHEN col_3 = '1170' THEN col_5 END as assetsNonCurrentFinancialInvestments,
        CASE WHEN col_3 = '1180' THEN col_5 END as assetsNonCurrentDeferredTaxAsset,
        CASE WHEN col_3 = '1190' THEN col_5 END as assetsNonCurrentOtherAssets,

        CASE WHEN col_0 = 'II. Оборотные активы' THEN col_5 END as assetsCurrentTotal,
        CASE WHEN col_3 = '1210' THEN col_5 END as assetsCurrentInventory,
        CASE WHEN col_3 = '1220' THEN col_5 END as assetsCurrentVATOnAcquiredValuables,
        CASE WHEN col_3 = '1230' THEN col_5 END as assetsCurrentReceivables,
        CASE WHEN col_3 = '1231' THEN col_5 END as assetsCurrentLongTermReceivables,
        CASE WHEN col_3 = '1237' THEN col_5 END as assetsCurrentdebtsOfFounders,
        CASE WHEN col_3 = '1240' THEN col_5 END as assetsCurrentFinancialInvestment,
        CASE WHEN col_3 = '1250' THEN col_5 END as assetsCurrentCash,
        CASE WHEN col_3 = '1260' THEN col_5 END as assetsCurrentOther,

        CASE WHEN col_0 = 'ИТОГО АКТИВ' THEN col_5 END as assetsTotal,

        CASE WHEN col_0 = 'III. Капитал и резервы:' THEN col_5 END as liabilityEquityTotal,
        CASE WHEN col_3 = '1310' THEN col_5 END as liabilityEquityStatutoryCapital,
        CASE WHEN col_3 = '1320' THEN col_5 END as liabilityEquityOwnStock,
        CASE WHEN col_3 = '1340' THEN col_5 END as liabilityEquityNonCurrentAssetsRevaluation,
        CASE WHEN col_3 = '1360' THEN col_5 END as liabilityEquityEquityReserve,
        CASE WHEN col_3 = '1370' THEN col_5 END as liabilityEquityRetainedEarnings,

        CASE WHEN col_0 = 'IV. Долгосрочные обязательства' THEN col_5 END as liabilityLongTermLiabilitiesTotal,
        CASE WHEN col_3 = '1410' THEN col_5 END as liabilityLongTermLiabilitiesLoansAndBorrowings,
        CASE WHEN col_3 = '1420' THEN col_5 END as liabilityLongTermLiabilitiesDeferredTaxLiabilities,
        CASE WHEN col_3 = '1430' THEN col_5 END as liabilityLongTermLiabilitiesEstimatedLiabilities,
        CASE WHEN col_3 = '1440' THEN col_5 END as liabilityLongTermLiabilitiesReceivedAdvancesPayables,
        CASE WHEN col_3 = '1450' THEN col_5 END as liabilityLongTermLiabilitiesOtherLiabilities,

        CASE WHEN col_0 = 'V. Краткосрочные обязательства:' THEN col_5 END as liabilityShortTermLiabilitiesTotal,
        CASE WHEN col_3 = '1510' THEN col_5 END as liabilityShortTermLiabilitiesLoansAndBorrowings,
        CASE WHEN col_3 = '1520' THEN col_5 END as liabilityShortTermLiabilitiesAccountsPayable,
        CASE WHEN col_3 = '1530' THEN col_5 END as liabilityShortTermLiabilitiesDeferredIncome,
        CASE WHEN col_3 = '1540' THEN col_5 END as liabilityShortTermLiabilitiesEstimatedLiabilities,
        CASE WHEN col_3 = '1550' THEN col_5 END as liabilityShortTermLiabilitiesOtherLiabilities,

        CASE WHEN col_0 = 'ИТОГО ПАССИВ' THEN col_5 END as liabilitiesTotal,

        CASE WHEN col_3 = '2110' THEN col_5 END as pnlNetRevenue,
        CASE WHEN col_3 = '2200' THEN col_5 END as pnlOperatingProfit,
        CASE WHEN col_3 = '2400' THEN col_5 END as pnlYearNetProfit,
        CASE WHEN col_3 = '3600' THEN col_5 END as statementOfChangesInEquityNetAssets,

        CASE WHEN col_0 = 'Кэффициент покрытия' THEN col_5 END as coverRatio,
        CASE WHEN col_0 = 'Коэффициент быстрой ликвидности' THEN col_5 END as quickLiquidityRatio,
        CASE WHEN col_0 = 'Оборачиваемость оборотного капитала' THEN col_5 END as workingCapitalTurnover,
        CASE WHEN col_0 = 'Коэффициент финансовой устойчивости' THEN col_5 END as financialStabilityIndex,
        CASE WHEN col_0 = 'Рентабельность продаж' THEN col_5 END as marginOnSales,
        CASE WHEN col_0 = 'Рентабельность активов' THEN col_5 END as returnOnAssets

         from tmp_union_rsbu_reports
		
			union
		select filename, 'year_3' as year, name_do,
		CASE WHEN rowid = '4' THEN col_6 END as rsbu_financial_statements,

        CASE WHEN col_0 = 'I. Внеоборотные активы:' THEN col_6 END as assetsNonCurrentTotal,
        CASE WHEN col_3 = '1110' THEN col_6 END as assetsNonCurrentIntangibleAssets,
        CASE WHEN col_3 = '1120' THEN col_6 END as assetsNonCurrentRNDResults,
        CASE WHEN col_3 = '1130' THEN col_6 END as assetsNonCurrentNonMaterialResearchAssets,
        CASE WHEN col_3 = '1140' THEN col_6 END as assetsNonCurrentMaterialResearchAssets,
        CASE WHEN col_3 = '1150' THEN col_6 END as assetsNonCurrentFixedAssets,
        CASE WHEN col_3 = '1160' THEN col_6 END as assetsNonCurrentIBPIntoTangibleAssets,
        CASE WHEN col_3 = '1170' THEN col_6 END as assetsNonCurrentFinancialInvestments,
        CASE WHEN col_3 = '1180' THEN col_6 END as assetsNonCurrentDeferredTaxAsset,
        CASE WHEN col_3 = '1190' THEN col_6 END as assetsNonCurrentOtherAssets,
        
        CASE WHEN col_0 = 'II. Оборотные активы' THEN col_6 END as assetsCurrentTotal,
        CASE WHEN col_3 = '1210' THEN col_6 END as assetsCurrentInventory,
        CASE WHEN col_3 = '1220' THEN col_6 END as assetsCurrentVATOnAcquiredValuables,
        CASE WHEN col_3 = '1230' THEN col_6 END as assetsCurrentReceivables,
        CASE WHEN col_3 = '1231' THEN col_6 END as assetsCurrentLongTermReceivables,
        CASE WHEN col_3 = '1237' THEN col_6 END as assetsCurrentdebtsOfFounders,
        CASE WHEN col_3 = '1240' THEN col_6 END as assetsCurrentFinancialInvestment,
        CASE WHEN col_3 = '1250' THEN col_6 END as assetsCurrentCash,
        CASE WHEN col_3 = '1260' THEN col_6 END as assetsCurrentOther,
        
        CASE WHEN col_0 = 'ИТОГО АКТИВ' THEN col_6 END as assetsTotal,
        
        CASE WHEN col_0 = 'III. Капитал и резервы:' THEN col_6 END as liabilityEquityTotal,
        CASE WHEN col_3 = '1310' THEN col_6 END as liabilityEquityStatutoryCapital,
        CASE WHEN col_3 = '1320' THEN col_6 END as liabilityEquityOwnStock,
        CASE WHEN col_3 = '1340' THEN col_6 END as liabilityEquityNonCurrentAssetsRevaluation,
        CASE WHEN col_3 = '1360' THEN col_6 END as liabilityEquityEquityReserve,
        CASE WHEN col_3 = '1370' THEN col_6 END as liabilityEquityRetainedEarnings,
        
        CASE WHEN col_0 = 'IV. Долгосрочные обязательства' THEN col_6 END as liabilityLongTermLiabilitiesTotal,
        CASE WHEN col_3 = '1410' THEN col_6 END as liabilityLongTermLiabilitiesLoansAndBorrowings,
        CASE WHEN col_3 = '1420' THEN col_6 END as liabilityLongTermLiabilitiesDeferredTaxLiabilities,
        CASE WHEN col_3 = '1430' THEN col_6 END as liabilityLongTermLiabilitiesEstimatedLiabilities,
        CASE WHEN col_3 = '1440' THEN col_6 END as liabilityLongTermLiabilitiesReceivedAdvancesPayables,
        CASE WHEN col_3 = '1450' THEN col_6 END as liabilityLongTermLiabilitiesOtherLiabilities,
        
        CASE WHEN col_0 = 'V. Краткосрочные обязательства:' THEN col_6 END as liabilityShortTermLiabilitiesTotal,
        CASE WHEN col_3 = '1510' THEN col_6 END as liabilityShortTermLiabilitiesLoansAndBorrowings,
        CASE WHEN col_3 = '1520' THEN col_6 END as liabilityShortTermLiabilitiesAccountsPayable,
        CASE WHEN col_3 = '1530' THEN col_6 END as liabilityShortTermLiabilitiesDeferredIncome,
        CASE WHEN col_3 = '1540' THEN col_6 END as liabilityShortTermLiabilitiesEstimatedLiabilities,
        CASE WHEN col_3 = '1550' THEN col_6 END as liabilityShortTermLiabilitiesOtherLiabilities,
        
        CASE WHEN col_0 = 'ИТОГО ПАССИВ' THEN col_6 END as liabilitiesTotal,
        
        CASE WHEN col_3 = '2110' THEN col_6 END as pnlNetRevenue,
        CASE WHEN col_3 = '2200' THEN col_6 END as pnlOperatingProfit,
        CASE WHEN col_3 = '2400' THEN col_6 END as pnlYearNetProfit,
        CASE WHEN col_3 = '3600' THEN col_6 END as statementOfChangesInEquityNetAssets,
        
        CASE WHEN col_0 = 'Кэффициент покрытия' THEN col_6 END as coverRatio,
        CASE WHEN col_0 = 'Коэффициент быстрой ликвидности' THEN col_6 END as quickLiquidityRatio,
        CASE WHEN col_0 = 'Оборачиваемость оборотного капитала' THEN col_6 END as workingCapitalTurnover,
        CASE WHEN col_0 = 'Коэффициент финансовой устойчивости' THEN col_6 END as financialStabilityIndex,
        CASE WHEN col_0 = 'Рентабельность продаж' THEN col_6 END as marginOnSales,
        CASE WHEN col_0 = 'Рентабельность активов' THEN col_6 END as returnOnAssets

		from tmp_union_rsbu_reports
	) r
	GROUP BY r.filename, r.year, r.name_do
)
select f.*, t.rsbu_financial_statements_currency
from rsbu_report_3_date_flat f
left join (
	select filename, col_2 as rsbu_financial_statements_currency from tmp_union_rsbu_reports where rowid='2'
) t
on f.filename = t.filename
