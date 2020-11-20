package com.kpmg.agata.constant.database;

import static com.kpmg.agata.constant.database.TablesConstants.COUNTERPARTY_CONTRACT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.CREDIT_CORRECTION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.CREDIT_LIMIT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.EXPRESS_ANALYSIS_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_ARREARS_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_DISQ_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_TAX_VIOLATION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.GENPROC_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.LEGAL_CASES_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PAYMENT_FROM_CUSTOMER_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PDZ_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.REALIZATION_SERVICES_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.SPARK_INTERFAX_SUMMARY_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FSSP_TABLE;

public class ValidationTablesConstants {

    private static final String VALIDATION_PREFIX = "validation_";
    public static final String VALIDATION_CREDIT_CORRECTION_TABLE = VALIDATION_PREFIX + CREDIT_CORRECTION_TABLE;
    public static final String VALIDATION_CREDIT_LIMIT_TABLE = VALIDATION_PREFIX + CREDIT_LIMIT_TABLE;
    public static final String VALIDATION_PAYMENT_FROM_CUSTOMER_TABLE = VALIDATION_PREFIX + PAYMENT_FROM_CUSTOMER_TABLE;
    public static final String VALIDATION_REALIZATION_SERVICES_TABLE = VALIDATION_PREFIX + REALIZATION_SERVICES_TABLE;
    public static final String VALIDATION_EXPRESS_ANALYSIS_TABLE = VALIDATION_PREFIX + EXPRESS_ANALYSIS_TABLE;
    public static final String VALIDATION_LEGAL_CASES_TABLE = VALIDATION_PREFIX + LEGAL_CASES_TABLE;
    public static final String VALIDATION_PDZ_TABLE = VALIDATION_PREFIX + PDZ_TABLE;
    public static final String VALIDATION_SPARK_INTERFAX_SUMMARY_TABLE =
            VALIDATION_PREFIX + SPARK_INTERFAX_SUMMARY_TABLE;
    public static final String VALIDATION_FNS_TAX_VIOLATION_TABLE = VALIDATION_PREFIX + FNS_TAX_VIOLATION_TABLE;
    public static final String VALIDATION_FNS_ARREARS_TABLE = VALIDATION_PREFIX + FNS_ARREARS_TABLE;
    public static final String VALIDATION_GENPROC_TABLE = VALIDATION_PREFIX + GENPROC_TABLE;
    public static final String VALIDATION_FSSP_TABLE = VALIDATION_PREFIX + FSSP_TABLE;
    public static final String VALIDATION_FNS_DISQ_TABLE = VALIDATION_PREFIX + FNS_DISQ_TABLE;
    public static final String VALIDATION_COUNTERPARTY_CONTRACT_TABLE = VALIDATION_PREFIX + COUNTERPARTY_CONTRACT_TABLE;

    private ValidationTablesConstants() {
    }
}
