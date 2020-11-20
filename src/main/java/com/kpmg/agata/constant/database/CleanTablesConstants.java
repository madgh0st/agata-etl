package com.kpmg.agata.constant.database;

import static com.kpmg.agata.constant.database.TablesConstants.AFFILATION_TABLE;
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
import static com.kpmg.agata.constant.database.TablesConstants.PAYMENT_FROM_CUSTOMER_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PDZ_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.REALIZATION_SERVICES_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.SECTORAL_INDICES_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.SPARK_INTERFAX_SUMMARY_TABLE;

public class CleanTablesConstants {

    private static final String CLEAN_PREFIX = "clean_";
    public static final String CLEAN_CREDIT_CORRECTION_TABLE = CLEAN_PREFIX + CREDIT_CORRECTION_TABLE;
    public static final String CLEAN_CREDIT_LIMIT_TABLE = CLEAN_PREFIX + CREDIT_LIMIT_TABLE;
    public static final String CLEAN_PAYMENT_FROM_CUSTOMER_TABLE = CLEAN_PREFIX + PAYMENT_FROM_CUSTOMER_TABLE;
    public static final String CLEAN_PDZ_TABLE = CLEAN_PREFIX + PDZ_TABLE;
    public static final String CLEAN_REALIZATION_SERVICES_TABLE = CLEAN_PREFIX + REALIZATION_SERVICES_TABLE;
    public static final String CLEAN_EXPRESS_ANALYSIS_TABLE = CLEAN_PREFIX + EXPRESS_ANALYSIS_TABLE;
    public static final String CLEAN_LEGAL_CASES_TABLE = CLEAN_PREFIX + LEGAL_CASES_TABLE;
    public static final String CLEAN_WEEKLY_PDZ_FLAT_TABLE = CLEAN_PREFIX + "weekly_pdz_flat";
    public static final String CLEAN_SPARK_INTERFAX_SUMMARY_TABLE = CLEAN_PREFIX + SPARK_INTERFAX_SUMMARY_TABLE;
    public static final String CLEAN_FNS_TAX_VIOLATION_TABLE = CLEAN_PREFIX + FNS_TAX_VIOLATION_TABLE;
    public static final String CLEAN_FNS_ARREARS_TABLE = CLEAN_PREFIX + FNS_ARREARS_TABLE;
    public static final String CLEAN_GENPROC_TABLE = CLEAN_PREFIX + GENPROC_TABLE;
    public static final String CLEAN_FSSP_TABLE = CLEAN_PREFIX + FSSP_TABLE;
    public static final String CLEAN_FNS_DISQ_TABLE = CLEAN_PREFIX + FNS_DISQ_TABLE;
    public static final String CLEAN_AFFILATION_TABLE = CLEAN_PREFIX + AFFILATION_TABLE;
    public static final String CLEAN_SECTORAL_INDICES_TABLE = CLEAN_PREFIX + SECTORAL_INDICES_TABLE;
    public static final String CLEAN_COUNTERPARTY_CONTRACT_TABLE = CLEAN_PREFIX + COUNTERPARTY_CONTRACT_TABLE;

    private CleanTablesConstants() {
    }
}
