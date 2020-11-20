package com.kpmg.agata.controllers;

import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.constant.database.BaseConstants;
import com.kpmg.agata.constant.database.CleanTablesConstants;
import com.kpmg.agata.constant.database.CollisionsReportTableConstants;
import com.kpmg.agata.constant.database.MdTablesConstants;
import com.kpmg.agata.constant.database.TempTablesConstants;
import com.kpmg.agata.constant.database.UnionTableConstants;
import com.kpmg.agata.constant.database.ValidationTablesConstants;
import com.kpmg.agata.parser.ConfigType;
import com.kpmg.agata.utils.runner.TaskQueueBuilder;
import com.kpmg.agata.utils.sql.ReplaceModel;
import com.kpmg.agata.utils.sql.UdfRegistrar;
import com.kpmg.agata.utils.sql.to.hive.SqlToHiveTaskModel;
import com.kpmg.agata.utils.sql.to.hive.ToHiveTaskExecutor;
import com.kpmg.agata.utils.sql.to.hive.ToHiveTaskModel;
import java.util.List;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;

public class SparkCleanDataController extends AbstractController {

    private static final String UNION_REPLACER_KEY = "union_replacer";

    // MD
    private static final ToHiveTaskModel MD_CONTRACTOR_CUSTOM_DICT = new SqlToHiveTaskModel(
            "sql/md/1_md_contractor_custom_dict.sql",
            MdTablesConstants.MD_CONTRACTOR_CUSTOM_DICT_TABLE
    );
    private static final ToHiveTaskModel MD_UNION_1C_DICT = new SqlToHiveTaskModel(
            "sql/md/2_md_union_1c_dict.sql",
            MdTablesConstants.MD_UNION_1C_DICT_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.COUNTERPARTY_DICTIONARY)
    );
    private static final ToHiveTaskModel MD_1C_TO_FLAT = new SqlToHiveTaskModel(
            "sql/md/3_md_1c_to_flat.sql",
            MdTablesConstants.MD_1C_TO_FLAT_TABLE
    );

    private static final ToHiveTaskModel MD_CONTRACTOR_DICT_TABLE = new SqlToHiveTaskModel(
            "sql/md/4_md_contractor_dict.sql",
            MdTablesConstants.MD_CONTRACTOR_DICT_TABLE
    );

    // CREDIT_CORRECTION
    private static final ToHiveTaskModel UNION_CREDIT_CORRECTION = new SqlToHiveTaskModel(
            "sql/clean/credit_correction/1_union_credit_correction.sql",
            UnionTableConstants.UNION_CREDIT_CORRECTION_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.CREDIT_CORRECTION)
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_CREDIT_CORRECTION = new SqlToHiveTaskModel(
            "sql/clean/credit_correction/2_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_CREDIT_CORRECTION_TABLE
    );
    private static final ToHiveTaskModel CLEAN_CREDIT_CORRECTION = new SqlToHiveTaskModel(
            "sql/clean/credit_correction/3_clean_credit_correction.sql",
            CleanTablesConstants.CLEAN_CREDIT_CORRECTION_TABLE
    );
    private static final ToHiveTaskModel VALIDATION_CREDIT_CORRECTION_TABLE = new SqlToHiveTaskModel(
            "sql/clean/credit_correction/4_validation.sql",
            ValidationTablesConstants.VALIDATION_CREDIT_CORRECTION_TABLE
    );

    // COUNTERPARTY_CONTRACT
    public static final ToHiveTaskModel UNION_COUNTERPARTY_CONTRACT = new SqlToHiveTaskModel(
            "sql/clean/counterparty_contract/1_union_counterparty_contract.sql",
            UnionTableConstants.UNION_COUNTERPARTY_CONTRACT_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.COUNTERPARTY_CONTRACT)
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_COUNTERPARTY_CONTRACT = new SqlToHiveTaskModel(
            "sql/clean/counterparty_contract/2_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_COUNTERPARTY_CONTRACT_TABLE
    );
    private static final ToHiveTaskModel CLEAN_COUNTERPARTY_CONTRACT = new SqlToHiveTaskModel(
            "sql/clean/counterparty_contract/3_clean_counterparty_contract.sql",
            CleanTablesConstants.CLEAN_COUNTERPARTY_CONTRACT_TABLE
    );
    private static final ToHiveTaskModel VALIDATION_COUNTERPARTY_CONTRACT_TABLE = new SqlToHiveTaskModel(
            "sql/clean/counterparty_contract/4_validation.sql",
            ValidationTablesConstants.VALIDATION_COUNTERPARTY_CONTRACT_TABLE
    );

    // CREDIT_LIMIT
    private static final ToHiveTaskModel UNION_CREDIT_LIMIT = new SqlToHiveTaskModel(
            "sql/clean/credit_limit/1_union_credit_limit.sql",
            UnionTableConstants.UNION_CREDIT_LIMIT_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.CREDIT_LIMIT)
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_CREDIT_LIMIT = new SqlToHiveTaskModel(
            "sql/clean/credit_limit/2_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_CREDIT_LIMIT_TABLE
    );
    private static final ToHiveTaskModel CLEAN_CREDIT_LIMIT = new SqlToHiveTaskModel(
            "sql/clean/credit_limit/3_clean_credit_limit.sql",
            CleanTablesConstants.CLEAN_CREDIT_LIMIT_TABLE
    );
    private static final ToHiveTaskModel VALIDATION_CREDIT_LIMIT_TABLE = new SqlToHiveTaskModel(
            "sql/clean/credit_limit/4_validation.sql",
            ValidationTablesConstants.VALIDATION_CREDIT_LIMIT_TABLE
    );

    // PAYMENT_FROM_CUSTOMER
    private static final ToHiveTaskModel UNION_PAYMENT = new SqlToHiveTaskModel(
            "sql/clean/payment_from_customer/1_union_payment.sql",
            UnionTableConstants.UNION_PAYMENT_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.PAYMENT_FROM_CUSTOMER)
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_PAYMENT = new SqlToHiveTaskModel(
            "sql/clean/payment_from_customer/2_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_PAYMENT_TABLE
    );
    private static final ToHiveTaskModel CLEAN_PAYMENT_FROM_CUSTOMER = new SqlToHiveTaskModel(
            "sql/clean/payment_from_customer/3_clean_payment.sql",
            CleanTablesConstants.CLEAN_PAYMENT_FROM_CUSTOMER_TABLE
    );
    private static final ToHiveTaskModel VALIDATION_PAYMENT_FROM_CUSTOMER_TABLE = new SqlToHiveTaskModel(
            "sql/clean/payment_from_customer/4_validation.sql",
            ValidationTablesConstants.VALIDATION_PAYMENT_FROM_CUSTOMER_TABLE
    );
    // PDZ
    private static final ToHiveTaskModel UNION_PDZ_MONTHLY = new SqlToHiveTaskModel(
            "sql/clean/pdz/1_union_pdz_monthly.sql",
            UnionTableConstants.UNION_PDZ_MONTHLY_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.MONTHLY_STATUS_PDZ)
    );
    private static final ToHiveTaskModel UNION_PDZ_WEEKLY = new SqlToHiveTaskModel(
            "sql/clean/pdz/2_union_pdz_weekly.sql",
            UnionTableConstants.UNION_PDZ_WEEKLY_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.WEEKLY_STATUS_PDZ)
    );
    private static final ToHiveTaskModel UNION_PDZ = new SqlToHiveTaskModel(
            "sql/clean/pdz/3_union_pdz.sql",
            UnionTableConstants.UNION_PDZ_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_PDZ = new SqlToHiveTaskModel(
            "sql/clean/pdz/4_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_PDZ_TABLE
    );
    private static final ToHiveTaskModel CLEAN_PDZ = new SqlToHiveTaskModel(
            "sql/clean/pdz/5_clean_pdz.sql",
            CleanTablesConstants.CLEAN_PDZ_TABLE
    );
    private static final ToHiveTaskModel VALIDATION_PDZ_TABLE = new SqlToHiveTaskModel(
            "sql/clean/pdz/7_validation_pdz.sql",
            ValidationTablesConstants.VALIDATION_PDZ_TABLE
    );

    // REALIZATION_SERVICES
    private static final ToHiveTaskModel UNION_REALIZATION = new SqlToHiveTaskModel(
            "sql/clean/realization_services/1_union_realization.sql",
            UnionTableConstants.UNION_REALIZATION_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.REALIZATION_SERVICES)
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_REALIZATION = new SqlToHiveTaskModel(
            "sql/clean/realization_services/2_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_REALIZATION_TABLE
    );
    private static final ToHiveTaskModel CLEAN_REALIZATION_SERVICES = new SqlToHiveTaskModel(
            "sql/clean/realization_services/3_clean_realization.sql",
            CleanTablesConstants.CLEAN_REALIZATION_SERVICES_TABLE
    );
    private static final ToHiveTaskModel VALIDATION_REALIZATION_SERVICES_TABLE = new SqlToHiveTaskModel(
            "sql/clean/realization_services/4_validation.sql",
            ValidationTablesConstants.VALIDATION_REALIZATION_SERVICES_TABLE
    );
    // EXPRESS_ANALYSIS
    private static final ToHiveTaskModel TMP_FLAT_RSBU_LIMIT = new SqlToHiveTaskModel(
            "sql/clean/express_analysis/1_tmp_flat_rsbu_limit.sql",
            TempTablesConstants.TMP_FLAT_RSBU_LIMIT_TABLE,
            asList(new ReplaceModel("replacer_union_rsbu_limit", ConfigType.EXPRESS_ANALYSIS_RSBU_LIMIT),
                    new ReplaceModel("replacer_union_msfo_limit", ConfigType.EXPRESS_ANALYSIS_MSFO_LIMIT))
    );
    private static final ToHiveTaskModel TMP_FLAT_RSBU_REPORT = new SqlToHiveTaskModel(
            "sql/clean/express_analysis/2_tmp_flat_rsbu_report.sql",
            TempTablesConstants.TMP_FLAT_RSBU_REPORT_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.EXPRESS_ANALYSIS_RSBU_REPORT)
    );
    private static final ToHiveTaskModel UNION_FLAT_RSBU = new SqlToHiveTaskModel(
            "sql/clean/express_analysis/3_union_flat_rsbu.sql",
            UnionTableConstants.UNION_FLAT_RSBU_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_EXPRESS_ANALYSIS = new SqlToHiveTaskModel(
            "sql/clean/express_analysis/4_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_EXPRESS_ANALYSIS_TABLE
    );
    private static final ToHiveTaskModel CLEAN_EXPRESS_ANALYSIS = new SqlToHiveTaskModel(
            "sql/clean/express_analysis/5_clean_express_analysis.sql",
            CleanTablesConstants.CLEAN_EXPRESS_ANALYSIS_TABLE
    );
    private static final ToHiveTaskModel VALIDATION_EXPRESS_ANALYSIS_TABLE = new SqlToHiveTaskModel(
            "sql/clean/express_analysis/6_validation.sql",
            ValidationTablesConstants.VALIDATION_EXPRESS_ANALYSIS_TABLE
    );
    // LEGAL_CASES
    private static final ToHiveTaskModel UNION_LEGAL_CASES = new SqlToHiveTaskModel(
            "sql/clean/legal_cases/1_union_legal_cases.sql",
            UnionTableConstants.UNION_LEGAL_CASES_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_LEGAL_CASES = new SqlToHiveTaskModel(
            "sql/clean/legal_cases/2_collisions_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_LEGAL_CASES_TABLE
    );
    private static final ToHiveTaskModel CLEAN_LEGAL_CASES = new SqlToHiveTaskModel(
            "sql/clean/legal_cases/3_clean_table.sql",
            CleanTablesConstants.CLEAN_LEGAL_CASES_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.LEGAL_CASES)
    );
    private static final ToHiveTaskModel VALIDATION_LEGAL_CASES_TABLE = new SqlToHiveTaskModel(
            "sql/clean/legal_cases/4_validation.sql",
            ValidationTablesConstants.VALIDATION_LEGAL_CASES_TABLE
    );

    // SPARK_INTERFAX_SUMMARY
    private static final ToHiveTaskModel UNION_SPARK_INTERFAX_SUMMARY = new SqlToHiveTaskModel(
            "sql/clean/spark_interfax_summary/1_union_spark_interfax_summary.sql",
            UnionTableConstants.UNION_SPARK_INTERFAX_SUMMARY_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_SPARK_INTERFAX_SUMMARY = new SqlToHiveTaskModel(
            "sql/clean/spark_interfax_summary/2_collision_report.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_SPARK_INTERFAX_SUMMARY_TABLE
    );
    private static final ToHiveTaskModel CLEAN_SPARK_INTERFAX_SUMMARY = new SqlToHiveTaskModel(
            "sql/clean/spark_interfax_summary/3_clean_table.sql",
            CleanTablesConstants.CLEAN_SPARK_INTERFAX_SUMMARY_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.SPARK_INTERFAX_SUMMARY)
    );
    private static final ToHiveTaskModel VALIDATION_SPARK_INTERFAX_SUMMARY_TABLE = new SqlToHiveTaskModel(
            "sql/clean/spark_interfax_summary/4_validation.sql",
            ValidationTablesConstants.VALIDATION_SPARK_INTERFAX_SUMMARY_TABLE
    );

    // PDZ_FLAT
    private static final ToHiveTaskModel CLEAN_WEEKLY_PDZ_FLAT = new SqlToHiveTaskModel(
            "sql/clean/pdz/6_clean_weekly_pdz_flat.sql",
            CleanTablesConstants.CLEAN_WEEKLY_PDZ_FLAT_TABLE
    );

    // AVAILABLE_LIST_DO
    private static final ToHiveTaskModel AVAILABLE_LIST_DO = new SqlToHiveTaskModel(
            "sql/available_list_do/1_available_list_do.sql",
            BaseConstants.AVAILABLE_LIST_DO_TABLE
    );

    // AVAILABLE_LIST_DO
    private static final ToHiveTaskModel PDZ_BECAUSE_OF_BANKS = new SqlToHiveTaskModel(
            "sql/clean/pdz_because_of_banks/1_union_pdz_because_of_banks.sql",
            UnionTableConstants.UNION_PDZ_BECAUSE_OF_BANKS_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.PDZ_BECAUSE_OF_BANKS)
    );

    // FNS TAX VIOLATION
    private static final ToHiveTaskModel UNION_FNS_TAX_VIOLATION = new SqlToHiveTaskModel(
            "sql/clean/fns_tax_violation/1_union_fns_tax_violation.sql",
            UnionTableConstants.UNION_FNS_TAX_VIOLATION_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_FNS_TAX_VIOLATION = new SqlToHiveTaskModel(
            "sql/clean/fns_tax_violation/2_collisions_report_fns_tax_violation.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_FNS_TAX_VIOLATION_TABLE
    );
    private static final ToHiveTaskModel CLEAN_FNS_TAX_VIOLATION = new SqlToHiveTaskModel(
            "sql/clean/fns_tax_violation/3_clean_table_fns_tax_violation.sql",
            CleanTablesConstants.CLEAN_FNS_TAX_VIOLATION_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.FNS_TAX_VIOLATION)
    );
    private static final ToHiveTaskModel VALIDATION_FNS_TAX_VIOLATION = new SqlToHiveTaskModel(
            "sql/clean/fns_tax_violation/4_validation_fns_tax_violation.sql",
            ValidationTablesConstants.VALIDATION_FNS_TAX_VIOLATION_TABLE
    );

    // FNS ARREARS
    private static final ToHiveTaskModel UNION_FNS_ARREARS = new SqlToHiveTaskModel(
            "sql/clean/fns_arrears/1_union_fns_arrears.sql",
            UnionTableConstants.UNION_FNS_ARREARS_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_FNS_ARREARS = new SqlToHiveTaskModel(
            "sql/clean/fns_arrears/2_collisions_report_fns_arrears.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_FNS_ARREARS_TABLE
    );
    private static final ToHiveTaskModel CLEAN_FNS_ARREARS = new SqlToHiveTaskModel(
            "sql/clean/fns_arrears/3_clean_table_fns_arrears.sql",
            CleanTablesConstants.CLEAN_FNS_ARREARS_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.FNS_ARREARS)
    );
    private static final ToHiveTaskModel VALIDATION_FNS_ARREARS = new SqlToHiveTaskModel(
            "sql/clean/fns_arrears/4_validation_fns_arrears.sql",
            ValidationTablesConstants.VALIDATION_FNS_ARREARS_TABLE
    );

    // GENPROC
    private static final ToHiveTaskModel UNION_GENPROC = new SqlToHiveTaskModel(
            "sql/clean/genproc/1_union_genproc.sql",
            UnionTableConstants.UNION_GENPROC_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_GENPROC = new SqlToHiveTaskModel(
            "sql/clean/genproc/2_collisions_report_genproc.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_GENPROC_TABLE
    );
    private static final ToHiveTaskModel CLEAN_GENPROC = new SqlToHiveTaskModel(
            "sql/clean/genproc/3_clean_table_genproc.sql",
            CleanTablesConstants.CLEAN_GENPROC_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.GENPROC)
    );
    private static final ToHiveTaskModel VALIDATION_GENPROC = new SqlToHiveTaskModel(
            "sql/clean/genproc/4_validation_genproc.sql",
            ValidationTablesConstants.VALIDATION_GENPROC_TABLE
    );

    // FNS_DISQ
    private static final ToHiveTaskModel UNION_FNS_DISQ = new SqlToHiveTaskModel(
            "sql/clean/fns_disq/1_union_fns_disq.sql",
            UnionTableConstants.UNION_FNS_DISQ_TABLE
    );
    private static final ToHiveTaskModel COLLISIONS_REPORT_FNS_DISQ = new SqlToHiveTaskModel(
            "sql/clean/fns_disq/2_collisions_report_fns_disq.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_FNS_DISQ_TABLE
    );
    private static final ToHiveTaskModel CLEAN_FNS_DISQ = new SqlToHiveTaskModel(
            "sql/clean/fns_disq/3_clean_table_fns_disq.sql",
            CleanTablesConstants.CLEAN_FNS_DISQ_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.FNS_DISQ)
    );
    private static final ToHiveTaskModel VALIDATION_FNS_DISQ = new SqlToHiveTaskModel(
            "sql/clean/fns_disq/4_validation_fns_disq.sql",
            ValidationTablesConstants.VALIDATION_FNS_DISQ_TABLE
    );

    // FSSP
    private static final ToHiveTaskModel UNION_FSSP = new SqlToHiveTaskModel(
            "sql/clean/fssp/1_union_fssp.sql",
            UnionTableConstants.UNION_FSSP_TABLE
    );

    private static final ToHiveTaskModel COLLISIONS_REPORT_FSSP = new SqlToHiveTaskModel(
            "sql/clean/fssp/2_collisions_report_fssp.sql",
            CollisionsReportTableConstants.COLLISIONS_REPORT_FSSP_TABLE
    );

    private static final ToHiveTaskModel CLEAN_FSSP = new SqlToHiveTaskModel(
            "sql/clean/fssp/3_clean_table_fssp.sql",
            CleanTablesConstants.CLEAN_FSSP_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.FSSP)
    );
    private static final ToHiveTaskModel VALIDATION_FSSP = new SqlToHiveTaskModel(
            "sql/clean/fssp/4_validation_fssp.sql",
            ValidationTablesConstants.VALIDATION_FSSP_TABLE
    );

    // Affilation
    private static final ToHiveTaskModel CLEAN_AFFILATION = new SqlToHiveTaskModel(
            "sql/clean/affilation/1_clean.sql",
            CleanTablesConstants.CLEAN_AFFILATION_TABLE
    );

    // Sectoral indices
    private static final ToHiveTaskModel UNION_SECTORAL_INDICES = new SqlToHiveTaskModel(
            "sql/clean/sectoral_indices/1_union.sql",
            UnionTableConstants.UNION_SECTORAL_INDICES_TABLE
    );
    private static final ToHiveTaskModel CLEAN_SECTORAL_INDICES = new SqlToHiveTaskModel(
            "sql/clean/sectoral_indices/2_clean.sql",
            CleanTablesConstants.CLEAN_SECTORAL_INDICES_TABLE,
            new ReplaceModel(UNION_REPLACER_KEY, ConfigType.SECTORAL_INDICES)
    );

    private static final Logger log = LoggerFactory.getLogger(SparkCleanDataController.class);
    private final ToHiveTaskExecutor toHiveTaskExecutor;
    private final SparkSession sparkSession;

    public SparkCleanDataController(SparkSession sparkSession, List<ModelTypeConfiguration> config) {
        this.sparkSession = sparkSession;
        this.toHiveTaskExecutor = new ToHiveTaskExecutor(sparkSession, config);
    }

    @Override
    protected void startAction() {
        UdfRegistrar.register(sparkSession);

        log.info("Create MD and sources to Hive");

        newBuilder()
                .addAction(toHiveTaskExecutor::run)
                .sequentialOf(MD_CONTRACTOR_CUSTOM_DICT, UNION_CREDIT_LIMIT)
                // MD block
                .parallelOf(MD_UNION_1C_DICT, PDZ_BECAUSE_OF_BANKS)
                .sequentialOf(MD_1C_TO_FLAT, MD_CONTRACTOR_DICT_TABLE, CLEAN_AFFILATION)
                // main block
                .parallelOf(mainSqlCreationPipeline())
                .sequentialOf(AVAILABLE_LIST_DO, CLEAN_WEEKLY_PDZ_FLAT)
                .run();
    }

    private TaskQueueBuilder<ToHiveTaskModel> mainSqlCreationPipeline() {
        return newBuilder()
                // block 1 - credit correction
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_CREDIT_CORRECTION, COLLISIONS_REPORT_CREDIT_CORRECTION,
                                CLEAN_CREDIT_CORRECTION, VALIDATION_CREDIT_CORRECTION_TABLE)
                )
                // block 2 - payment from customer
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_PAYMENT, COLLISIONS_REPORT_PAYMENT, CLEAN_PAYMENT_FROM_CUSTOMER,
                                VALIDATION_PAYMENT_FROM_CUSTOMER_TABLE)
                )
                // block 3 - pdz
                .sequentialOf(newBuilder()
                        .parallelOf(UNION_PDZ_MONTHLY, UNION_PDZ_WEEKLY)
                        .sequentialOf(UNION_PDZ, COLLISIONS_REPORT_PDZ, CLEAN_PDZ, VALIDATION_PDZ_TABLE))
                // block 4 - realization services
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_REALIZATION, COLLISIONS_REPORT_REALIZATION,
                                CLEAN_REALIZATION_SERVICES, VALIDATION_REALIZATION_SERVICES_TABLE)
                )
                // block 5 - express analysis
                .sequentialOf(newBuilder()
                        .parallelOf(TMP_FLAT_RSBU_LIMIT, TMP_FLAT_RSBU_REPORT)
                        .sequentialOf(UNION_FLAT_RSBU, COLLISIONS_REPORT_EXPRESS_ANALYSIS, CLEAN_EXPRESS_ANALYSIS,
                                VALIDATION_EXPRESS_ANALYSIS_TABLE)
                )
                // block 6 - legal cases
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_LEGAL_CASES, COLLISIONS_REPORT_LEGAL_CASES, CLEAN_LEGAL_CASES,
                                VALIDATION_LEGAL_CASES_TABLE)
                ) // block 7 - spark_interfax_summary
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_SPARK_INTERFAX_SUMMARY, COLLISIONS_REPORT_SPARK_INTERFAX_SUMMARY,
                                CLEAN_SPARK_INTERFAX_SUMMARY, VALIDATION_SPARK_INTERFAX_SUMMARY_TABLE)
                )
                // block 8 - fns tax violation
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_FNS_TAX_VIOLATION, COLLISIONS_REPORT_FNS_TAX_VIOLATION,
                                CLEAN_FNS_TAX_VIOLATION, VALIDATION_FNS_TAX_VIOLATION))
                // block 9 - credit limit
                .sequentialOf(newBuilder()
                        .sequentialOf(COLLISIONS_REPORT_CREDIT_LIMIT, CLEAN_CREDIT_LIMIT,
                                VALIDATION_CREDIT_LIMIT_TABLE))
                // block 10 - fns arrears
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_FNS_ARREARS, COLLISIONS_REPORT_FNS_ARREARS,
                                CLEAN_FNS_ARREARS, VALIDATION_FNS_ARREARS))
                // block 11 - genproc
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_GENPROC, COLLISIONS_REPORT_GENPROC, CLEAN_GENPROC, VALIDATION_GENPROC))
                // block 12 - fns disqualification
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_FNS_DISQ, COLLISIONS_REPORT_FNS_DISQ,
                                CLEAN_FNS_DISQ, VALIDATION_FNS_DISQ))
                // block 13 - fssp
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_FSSP, COLLISIONS_REPORT_FSSP, CLEAN_FSSP, VALIDATION_FSSP))
                // block 14 - sectoral indices
                .sequentialOf(newBuilder()
                        .sequentialOf(UNION_SECTORAL_INDICES, CLEAN_SECTORAL_INDICES))
                // block 15 - counterparty contract
                .sequentialOf(newBuilder().sequentialOf(UNION_COUNTERPARTY_CONTRACT,
                        COLLISIONS_REPORT_COUNTERPARTY_CONTRACT, CLEAN_COUNTERPARTY_CONTRACT,
                        VALIDATION_COUNTERPARTY_CONTRACT_TABLE));
    }

    private TaskQueueBuilder<ToHiveTaskModel> newBuilder() {
        return new TaskQueueBuilder<>();
    }
}
