with tmp_union_rsbu_limit as (
	select x.rowid, x.name_do_table as name_do, x.col_1,x.col_2,x.col_3,x.col_4,x.col_5,x.col_6,x.col_7,x.col_8,x.col_10,x.col_11,x.col_12,x.filename,x.loaddate,x.modificationdate,x.sheetname
	from (
		{replacer_union_rsbu_limit}
	) x
),tmp_union_msfo_limit as (
	select r.filename, r.col_4 as msfo_counterparty_name
	from (
		{replacer_union_msfo_limit}
	) r
	where r.rowid=5 and r.col_4 is not null
), rsbu_limit_one_cell as (
	select 
		r.filename, r.name_do, 
		max(r.name) as name,
		max(r.inn) as inn,
		max(r.contractor_code) as contractor_code,
		max(r.member_of_special_group) as member_of_special_group,
		max(r.department) as department,
		max(r.industry) as industry,
		max(r.slice_date_1) as slice_date_1,
		max(r.slice_date_2) as slice_date_2,
		max(r.contractor_class) as contractor_class,
		max(r.solvency_group) as solvency_group,
		max(r.points) as points,
		max(r.credit_limit) as credit_limit
	from (
		select filename, name_do,
			CASE WHEN rowid=5 THEN
				COALESCE(
					col_4,
					col_5,
					col_6,
					col_7,
					col_8,
					case when trim(replace(col_1, '1.1 Наименование контрагента:', '')) = '' THEN null else trim(replace(col_1, '1.1 Наименование контрагента:', '')) end
				) END as name,
			CASE WHEN rowid=6 THEN COALESCE(col_4,col_5,col_6,col_7,col_8) END as inn,
			CASE WHEN rowid=7 THEN COALESCE(col_4,col_5,col_6,col_7,col_8) END as contractor_code,
			CASE WHEN rowid=8 THEN col_7 END as member_of_special_group,
			CASE WHEN rowid=9 THEN col_3 END as department,
			CASE WHEN rowid=9 THEN col_7 END as industry,
			CASE WHEN rowid=12 THEN col_6 END as slice_date_1,
			CASE WHEN rowid=12 THEN col_12 END as slice_date_2,
			CASE WHEN rowid=57 THEN col_6 END as contractor_class,
			CASE WHEN rowid=56 THEN col_6 END as solvency_group,
			CASE WHEN rowid=53 THEN col_6 END as points,
			CASE WHEN rowid=75 THEN col_6 END as credit_limit
		from tmp_union_rsbu_limit
	) r
	GROUP BY r.filename, r.name_do
), rsbu_limit_3_date_flat as (
	select 
		r.year,
		r.filename,
		max(r.limitEstimationRSBU) as limit_Estimation_RSBU,  
		max(r.finMetrCoverageCoefficientPoints) as fin_Metr_Coverage_Coefficient_Points,   
		max(r.finMetrHotLiquidityCoefficientPoints) as fin_Metr_Hot_Liquidity_Coefficient_Points,    
		max(r.finMetrCapitalTurnoverPoints) as fin_Metr_Capital_Turnover_Points,     
		max(r.finMetrSellingProfitabilityPoints) as fin_Metr_Selling_Profitability_Points,     
		max(r.finMetrAssetsProfitabilityPoints) as fin_Metr_Assets_Profitability_Points,     
		max(r.finMetrFinancialStabilityCoefficientPoints) as fin_Metr_Financial_Stability_Coefficient_Points,     
		max(r.busRepCooperationExperienceCategory) as bus_Rep_Cooperation_Experience_Category,
		max(r.busRepCooperationExperiencePoints) as bus_Rep_Cooperation_Experience_Points,
		max(r.busRepPaymentDisciplineCategory) as bus_Rep_Payment_Discipline_Category,
		max(r.busRepPaymentDisciplinePoints) as bus_Rep_Payment_Discipline_Points,
		max(r.calculationNonCurrentAssets) as calculation_Non_Current_Assets,
		max(r.calculationNetAssets) as calculation_Net_Assets,   
		max(r.calculationCurrentAssets) as calculation_Current_Assets,     
		max(r.calculationCurrentLiabilities) as calculation_Current_Liabilities
	from (
		select filename, 'year_1' as year,
			CASE WHEN rowid=35 THEN col_6 END as limitEstimationRSBU, 
			CASE WHEN rowid=37 THEN col_6 END as finMetrCoverageCoefficientPoints, 
			CASE WHEN rowid=38 THEN col_6 END as finMetrHotLiquidityCoefficientPoints, 
			CASE WHEN rowid=39 THEN col_6 END as finMetrCapitalTurnoverPoints, 
			CASE WHEN rowid=41 THEN col_6 END as finMetrSellingProfitabilityPoints, 
			CASE WHEN rowid=42 THEN col_6 END as finMetrAssetsProfitabilityPoints, 
			CASE WHEN rowid=40 THEN col_6 END as finMetrFinancialStabilityCoefficientPoints, 
			CASE WHEN rowid=47 THEN col_6 END as busRepCooperationExperienceCategory,
			CASE WHEN rowid=48 THEN col_6 END as busRepCooperationExperiencePoints,
			CASE WHEN rowid=49 THEN col_6 END as busRepPaymentDisciplineCategory,
			CASE WHEN rowid=50 THEN col_6 END as busRepPaymentDisciplinePoints,
			CASE WHEN rowid=63 THEN col_6 END as calculationNonCurrentAssets, 
			CASE WHEN rowid=64 THEN col_6 END as calculationNetAssets, 
			CASE WHEN rowid=66 THEN col_6 END as calculationCurrentAssets, 
			CASE WHEN rowid=67 THEN col_6 END as calculationCurrentLiabilities
		from tmp_union_rsbu_limit
			union
		select filename, 'year_2' as year,
			CASE WHEN rowid=35 THEN col_7 END as limitEstimationRSBU, 
			CASE WHEN rowid=37 THEN col_7 END as finMetrCoverageCoefficientPoints, 
			CASE WHEN rowid=38 THEN col_7 END as finMetrHotLiquidityCoefficientPoints, 
			CASE WHEN rowid=39 THEN col_7 END as finMetrCapitalTurnoverPoints, 
			CASE WHEN rowid=41 THEN col_7 END as finMetrSellingProfitabilityPoints, 
			CASE WHEN rowid=42 THEN col_7 END as finMetrAssetsProfitabilityPoints, 
			CASE WHEN rowid=40 THEN col_7 END as finMetrFinancialStabilityCoefficientPoints,
			CASE WHEN rowid=47 THEN col_7 END as busRepCooperationExperienceCategory,
			CASE WHEN rowid=48 THEN col_7 END as busRepCooperationExperiencePoints,
			CASE WHEN rowid=49 THEN col_7 END as busRepPaymentDisciplineCategory,
			CASE WHEN rowid=50 THEN col_7 END as busRepPaymentDisciplinePoints,
			CASE WHEN rowid=63 THEN col_7 END as calculationNonCurrentAssets, 
			CASE WHEN rowid=64 THEN col_7 END as calculationNetAssets, 
			CASE WHEN rowid=66 THEN col_7 END as calculationCurrentAssets, 
			CASE WHEN rowid=67 THEN col_7 END as calculationCurrentLiabilities
		from tmp_union_rsbu_limit
			union
		select filename, 'year_3' as year,
			CASE WHEN rowid=35 THEN col_8 END as limitEstimationRSBU, 
			CASE WHEN rowid=37 THEN col_8 END as finMetrCoverageCoefficientPoints, 
			CASE WHEN rowid=38 THEN col_8 END as finMetrHotLiquidityCoefficientPoints, 
			CASE WHEN rowid=39 THEN col_8 END as finMetrCapitalTurnoverPoints, 
			CASE WHEN rowid=41 THEN col_8 END as finMetrSellingProfitabilityPoints, 
			CASE WHEN rowid=42 THEN col_8 END as finMetrAssetsProfitabilityPoints, 
			CASE WHEN rowid=40 THEN col_8 END as finMetrFinancialStabilityCoefficientPoints,
			CASE WHEN rowid=47 THEN col_8 END as busRepCooperationExperienceCategory,
			CASE WHEN rowid=48 THEN col_8 END as busRepCooperationExperiencePoints,
			CASE WHEN rowid=49 THEN col_8 END as busRepPaymentDisciplineCategory,
			CASE WHEN rowid=50 THEN col_8 END as busRepPaymentDisciplinePoints,
			CASE WHEN rowid=63 THEN col_8 END as calculationNonCurrentAssets, 
			CASE WHEN rowid=64 THEN col_8 END as calculationNetAssets, 
			CASE WHEN rowid=66 THEN col_8 END as calculationCurrentAssets, 
			CASE WHEN rowid=67 THEN col_8 END as calculationCurrentLiabilities
		from tmp_union_rsbu_limit
	) r
	group by r.year, r.filename
)
select 
	cell.filename, 
	cell.name_do, 
	d3.year,
	coalesce(cell.name, msfo.msfo_counterparty_name) as name,
	cell.inn,
	cell.contractor_code,
	cell.member_of_special_group,
	cell.department,
	cell.industry,
	cell.slice_date_1,
	cell.slice_date_2,
	cell.contractor_class,
	cell.solvency_group,
	cell.points,
	cell.credit_limit,
	d3.limit_Estimation_RSBU,  
	d3.fin_Metr_Coverage_Coefficient_Points,   
	d3.fin_Metr_Hot_Liquidity_Coefficient_Points,    
	d3.fin_Metr_Capital_Turnover_Points,     
	d3.fin_Metr_Selling_Profitability_Points,     
	d3.fin_Metr_Assets_Profitability_Points,     
	d3.fin_Metr_Financial_Stability_Coefficient_Points,
	d3.bus_Rep_Cooperation_Experience_Category,
	d3.bus_Rep_Cooperation_Experience_Points,
	d3.bus_Rep_Payment_Discipline_Category,
	d3.bus_Rep_Payment_Discipline_Points,
	d3.calculation_Non_Current_Assets,
	d3.calculation_Net_Assets,   
	d3.calculation_Current_Assets,     
	d3.calculation_Current_Liabilities
from rsbu_limit_3_date_flat d3
left join rsbu_limit_one_cell cell
on d3.filename = cell.filename
left join tmp_union_msfo_limit msfo
on cell.filename = msfo.filename
