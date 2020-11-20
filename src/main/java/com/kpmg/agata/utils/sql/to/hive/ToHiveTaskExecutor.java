package com.kpmg.agata.utils.sql.to.hive;

import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.parser.ConfigType;
import com.kpmg.agata.utils.sql.ReplaceModel;
import com.kpmg.agata.utils.sql.SqlLoader;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import static com.kpmg.agata.config.Environment.getSchemaPrefix;
import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.START_OF;
import static com.kpmg.agata.constant.LogConstants.TO_HIVE_EXEC;
import static com.kpmg.agata.constant.LogConstants.TO_HIVE_MODEL;
import static com.kpmg.agata.constant.database.BaseConstants.NAME_DO;
import static com.kpmg.agata.constant.database.BaseConstants.NAME_DO_TABLE;
import static com.kpmg.agata.constant.database.BaseConstants.PARQUET;
import static com.kpmg.agata.constant.database.BaseConstants.SORT_DO;
import static com.kpmg.agata.constant.database.MdTablesConstants.MD_CONTRACTOR_DICT_TABLE;

public class ToHiveTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(ToHiveTaskExecutor.class);
    private final SparkSession sparkSession;
    private final List<ModelTypeConfiguration> config;
    private final String schemaPrefix = getSchemaPrefix();
    private final String schema = Environment.getConfig().getProperty("db.schema");
    private final String outputDir = Environment.getConfig().getProperty("hdfs.dir.parquet.aggregate");
    private final EnumSet<ConfigType> sortDoConfigs = EnumSet.of(ConfigType.LEGAL_CASES,
            ConfigType.SPARK_INTERFAX_SUMMARY,
            ConfigType.FNS_TAX_VIOLATION,
            ConfigType.FNS_ARREARS,
            ConfigType.FNS_DISQ,
            ConfigType.FSSP,
            ConfigType.GENPROC,
            ConfigType.SECTORAL_INDICES);

    public ToHiveTaskExecutor(SparkSession sparkSession, List<ModelTypeConfiguration> config) {
        this.sparkSession = sparkSession;
        this.config = config;
    }

    private String loadSql(SqlToHiveTaskModel model) {
        Map<String, String> injections = model.getReplacers()
                                              .stream()
                                              .collect(toMap(ReplaceModel::getPlaceholder,
                                                      replacer -> getSqlUnionForSource(replacer.getReplaceType())));

        return new SqlLoader().fromResources(model.getPathToSqlFile())
                              .inject("dbSchema", schema)
                              .inject(injections)
                              .get();
    }

    public void run(ToHiveTaskModel model) {
        try {
            log.info("{}{}: {}={}", START_OF, TO_HIVE_EXEC, TO_HIVE_MODEL, model);

            if (model instanceof SqlToHiveTaskModel) {
                run((SqlToHiveTaskModel) model);
            } else if (model instanceof DataFrameToHiveTaskModel) {
                run((DataFrameToHiveTaskModel) model);
            } else {
                throw new IllegalArgumentException("Model is not supported: " + model);
            }

            log.info("{}{}: {}={}", END_OF, TO_HIVE_EXEC, TO_HIVE_MODEL, model);

            if (model.getHiveTableName().equals(MD_CONTRACTOR_DICT_TABLE)) {
                sparkSession.sqlContext().cacheTable(schemaPrefix + MD_CONTRACTOR_DICT_TABLE);
            }
        } catch (Exception e) {
            log.error("{} error: {}={}, {}", TO_HIVE_EXEC, TO_HIVE_MODEL, model, e.getMessage());
            sparkSession.close();
            throw new RuntimeException(TO_HIVE_EXEC + " error: ", e);
        }
    }

    private void run(SqlToHiveTaskModel model) {
        Dataset<Row> df = sparkSession.sql(loadSql(model));
        saveToHive(df, model);
    }

    private void run(DataFrameToHiveTaskModel model) {
        saveToHive(model.getDataFrame(), model);
    }

    private void saveToHive(Dataset<Row> df, ToHiveTaskModel model) {
        df.write()
          .mode(SaveMode.Overwrite)
          .format(PARQUET)
          .option("path", outputDir + model.getHiveTableName())
          .saveAsTable(schemaPrefix + model.getHiveTableName());
    }

    // todo: refactor this
    private Stream<String> nameDoBySourceStream(ConfigType configType) {
        return config
                .stream()
                .filter(x -> sortDoConfigs.contains(configType) ||
                        configType.equals(x.getConfigType()))
                .map(ModelTypeConfiguration::getNameDo)
                .filter(StringUtils::isNotBlank)
                .distinct();
    }

    // todo: refactor this
    private String getSqlUnionForSource(ConfigType configType) {
        if (sortDoConfigs.contains(configType)) {
            return nameDoBySourceStream(configType)
                    .filter(x -> !x.equals(SORT_DO))
                    .map(nameDo -> format("SELECT '%s' AS %s", nameDo, NAME_DO))
                    .collect(joining("\n UNION \n"));
        } else {
            return nameDoBySourceStream(configType)
                    .map(nameDo -> format(
                            "SELECT '%s' AS %s, * FROM %s",
                            nameDo, NAME_DO_TABLE, schemaPrefix + configType.name().toLowerCase().trim() + "_" + nameDo
                    ))
                    .collect(joining("\n UNION \n"));
        }
    }
}
