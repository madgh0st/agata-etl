package com.kpmg.agata.constant.database;

import static com.kpmg.agata.constant.database.TablesConstants.COUNTERPARTY_CONTRACT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.CREDIT_CORRECTION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.CREDIT_LIMIT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FLAT_RSBU_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_ARREARS_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_DISQ_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FNS_TAX_VIOLATION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.FSSP_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.GENPROC_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.LEGAL_CASES_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PAYMENT_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PDZ_BECAUSE_OF_BANKS_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PDZ_MONTHLY_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PDZ_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.PDZ_WEEKLY_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.REALIZATION_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.SECTORAL_INDICES_TABLE;
import static com.kpmg.agata.constant.database.TablesConstants.SPARK_INTERFAX_SUMMARY_TABLE;

public class UnionTableConstants {


    private static final String UNION_PREFIX = "union_";
    public static final String UNION_CREDIT_CORRECTION_TABLE = UNION_PREFIX + CREDIT_CORRECTION_TABLE;
    public static final String UNION_CREDIT_LIMIT_TABLE = UNION_PREFIX + CREDIT_LIMIT_TABLE;
    public static final String UNION_PAYMENT_TABLE = UNION_PREFIX + PAYMENT_TABLE;
    public static final String UNION_PDZ_TABLE = UNION_PREFIX + PDZ_TABLE;
    public static final String UNION_REALIZATION_TABLE = UNION_PREFIX + REALIZATION_TABLE;
    public static final String UNION_FLAT_RSBU_TABLE = UNION_PREFIX + FLAT_RSBU_TABLE;
    public static final String UNION_PDZ_MONTHLY_TABLE = UNION_PREFIX + PDZ_MONTHLY_TABLE;
    public static final String UNION_LEGAL_CASES_TABLE = UNION_PREFIX + LEGAL_CASES_TABLE;
    public static final String UNION_PDZ_WEEKLY_TABLE = UNION_PREFIX + PDZ_WEEKLY_TABLE;
    public static final String UNION_PDZ_BECAUSE_OF_BANKS_TABLE = UNION_PREFIX + PDZ_BECAUSE_OF_BANKS_TABLE;
    public static final String UNION_SPARK_INTERFAX_SUMMARY_TABLE = UNION_PREFIX + SPARK_INTERFAX_SUMMARY_TABLE;
    public static final String UNION_FNS_TAX_VIOLATION_TABLE = UNION_PREFIX + FNS_TAX_VIOLATION_TABLE;
    public static final String UNION_FNS_ARREARS_TABLE = UNION_PREFIX + FNS_ARREARS_TABLE;
    public static final String UNION_GENPROC_TABLE = UNION_PREFIX + GENPROC_TABLE;
    public static final String UNION_FSSP_TABLE = UNION_PREFIX + FSSP_TABLE;
    public static final String UNION_FNS_DISQ_TABLE = UNION_PREFIX + FNS_DISQ_TABLE;
    public static final String UNION_SECTORAL_INDICES_TABLE = UNION_PREFIX + SECTORAL_INDICES_TABLE;
    public static final String UNION_COUNTERPARTY_CONTRACT_TABLE = UNION_PREFIX + COUNTERPARTY_CONTRACT_TABLE;
    private UnionTableConstants() {
    }
}
