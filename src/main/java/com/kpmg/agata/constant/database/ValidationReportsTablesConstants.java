package com.kpmg.agata.constant.database;

public class ValidationReportsTablesConstants {
    private static final String VAL_PREFIX = "val_";
    public static final String DATA_IMPORT_FULL_REPORT = VAL_PREFIX + "data_import_full_report";
    public static final String DATA_IMPORT_SUMMARY_REPORT = VAL_PREFIX + "data_import_summary_report";
    public static final String DATA_IMPORT_MISSING_REPORT = VAL_PREFIX + "data_import_missing_report";
    public static final String MAPPING_STATUS_CREDIT_LIMIT_REPORT = VAL_PREFIX + "mapping_status_credit_limit_report";
    public static final String INTERSECTION_CL_WITH_CATEGORIES_ROWS = VAL_PREFIX +
            "intersection_cl_with_categories_rows";
    public static final String INTERSECTION_CL_WITH_CATEGORIES_COUNTERPARTY = VAL_PREFIX +
            "intersection_cl_with_categories_counterparty";
    public static final String CLEAN_TABLE_WITHOUT_EVENT_DATE = VAL_PREFIX + "clean_table_without_eventdate";
    public static final String AVAILABLE_DO_FROM_CREDIT_LIMIT_AND_PDZ = VAL_PREFIX +
            "available_do_from_credit_limit_and_pdz";
    public static final String RAW_DUPLICATE_FILES = VAL_PREFIX + "raw_dublicate_files";
    public static final String VAL_PIPELINE = VAL_PREFIX + "pipeline_time";
    public static final String WITHOUT_TARGET_SHEET = VAL_PREFIX + "xls_without_target_sheet";
    public static final String BUILD_VALIDATION_REPORT = VAL_PREFIX + "build_validation_report_time";
    public static final String STACKTRACE = VAL_PREFIX + "stacktrace";
    public static final String XLS_PARSING_TIME = VAL_PREFIX + "xls_parsing_time";
    public static final String CLEAN_TABLE_REPORT_TIME = VAL_PREFIX + "clean_table_report_time";
    public static final String VALIDATION_DATA_FOR_ALL_STEPS = VAL_PREFIX + "VALIDATION_DATA_FOR_ALL_STEPS";

    private ValidationReportsTablesConstants() {
    }
}
