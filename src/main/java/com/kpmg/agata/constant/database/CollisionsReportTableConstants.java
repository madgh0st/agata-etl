package com.kpmg.agata.constant.database;

import static com.kpmg.agata.constant.database.TablesConstants.COUNTERPARTY_CONTRACT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.CREDIT_CORRECTION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.CREDIT_LIMIT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.EXPRESS_ANALYSIS_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_ARREARS_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_DISQ_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_TAX_VIOLATION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FSSP_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.GENPROC_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.LEGAL_CASES_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PAYMENT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PDZ_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.REALIZATION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.SPARK_INTERFAX_SUMMARY_TABLE;

public class CollisionsReportTableConstants {

    public static final String COLLISIONS_PREFIX = "collisions_report_";
    public static final String COLLISIONS_REPORT_CREDIT_CORRECTION_TABLE = COLLISIONS_PREFIX +
            CREDIT_CORRECTION_TABLE;
    public static final String COLLISIONS_REPORT_CREDIT_LIMIT_TABLE = COLLISIONS_PREFIX + CREDIT_LIMIT_TABLE;
    public static final String COLLISIONS_REPORT_PAYMENT_TABLE = COLLISIONS_PREFIX + PAYMENT_TABLE;
    public static final String COLLISIONS_REPORT_PDZ_TABLE = COLLISIONS_PREFIX + PDZ_TABLE;
    public static final String COLLISIONS_REPORT_REALIZATION_TABLE = COLLISIONS_PREFIX + REALIZATION_TABLE;
    public static final String COLLISIONS_REPORT_EXPRESS_ANALYSIS_TABLE = COLLISIONS_PREFIX + EXPRESS_ANALYSIS_TABLE;
    public static final String COLLISIONS_REPORT_LEGAL_CASES_TABLE = COLLISIONS_PREFIX + LEGAL_CASES_TABLE;
    public static final String COLLISIONS_REPORT_SPARK_INTERFAX_SUMMARY_TABLE =
            COLLISIONS_PREFIX + SPARK_INTERFAX_SUMMARY_TABLE;
    public static final String COLLISIONS_REPORT_FNS_TAX_VIOLATION_TABLE = COLLISIONS_PREFIX + FNS_TAX_VIOLATION_TABLE;
    public static final String COLLISIONS_REPORT_FNS_ARREARS_TABLE = COLLISIONS_PREFIX + FNS_ARREARS_TABLE;
    public static final String COLLISIONS_REPORT_GENPROC_TABLE = COLLISIONS_PREFIX + GENPROC_TABLE;
    public static final String COLLISIONS_REPORT_FSSP_TABLE = COLLISIONS_PREFIX + FSSP_TABLE;
    public static final String COLLISIONS_REPORT_FNS_DISQ_TABLE = COLLISIONS_PREFIX + FNS_DISQ_TABLE;
    public static final String COLLISIONS_REPORT_COUNTERPARTY_CONTRACT_TABLE =
            COLLISIONS_PREFIX + COUNTERPARTY_CONTRACT_TABLE;

    private CollisionsReportTableConstants() {
    }
}
