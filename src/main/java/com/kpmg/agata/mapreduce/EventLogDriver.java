package com.kpmg.agata.mapreduce;

import com.kpmg.agata.mapreduce.first.EventLogKey;
import com.kpmg.agata.mapreduce.first.FirstEventLogMapper;
import com.kpmg.agata.mapreduce.first.FirstEventLogReducer;
import com.kpmg.agata.mapreduce.second.ReverseEventLogKey;
import com.kpmg.agata.mapreduce.second.SecondEventLogMapper;
import com.kpmg.agata.mapreduce.second.SecondEventLogReducer;
import com.kpmg.agata.mapreduce.third.ThirdEventLogMapper;
import com.kpmg.agata.mapreduce.third.ThirdEventLogReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.START_OF;

/**
 * MapReduce, that filters event log and applies counters.
 * It consists of jobs, running in chain.
 * The first job runs all simple counters.
 * The second job is for targets only.
 * The third job takes top N pdz models from each group
 */
public class EventLogDriver extends Configured implements Tool {
    private static final Logger log = LoggerFactory.getLogger(EventLogDriver.class);

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

        int status = ToolRunner.run(conf, new EventLogDriver(), otherArgs);

        if (status == 0) {
            log.info("Jobs were successful");
        } else {
            log.error("Jobs failed with code {}", status);
        }
        System.exit(status);
    }

    @Override
    public int run(String[] args) throws Exception {
        log.info(START_OF + "first job");
        Job firstJob = createFirstJob(args[0], args[1]);
        firstJob.submit();
        firstJob.waitForCompletion(true);
        log.info(END_OF + "first job");

        if (!firstJob.isSuccessful()) return 1;

        log.info(START_OF + "second job");
        Job secondJob = createSecondJob(args[1], args[2]);
        secondJob.submit();
        secondJob.waitForCompletion(true);
        log.info(END_OF + "second job");

        if (!secondJob.isSuccessful()) return 1;

        log.info(START_OF + "third job");
        Job thirdJob = createThirdJob(args[2], args[3]);
        thirdJob.submit();
        thirdJob.waitForCompletion(true);
        log.info(END_OF + "third job");

        return thirdJob.isSuccessful() ? 0 : 1;
    }

    private Configuration getConfigurationForJob(String namePrefix) {
        Configuration originConfig = getConf();
        Configuration jobConfig = new Configuration(originConfig);

        String prefixToReplace = namePrefix + ".";

        originConfig.iterator().forEachRemaining(
                entry -> {
                    if (entry.getKey().startsWith(namePrefix)) {
                        jobConfig.set(
                                entry.getKey().replace(prefixToReplace, ""),
                                entry.getValue()
                        );
                    }
                }
        );
        return jobConfig;
    }

    private Job createFirstJob(String input, String output) throws IOException {
        Job job = Job.getInstance(getConfigurationForJob("first"));
        job.setJarByClass(EventLogDriver.class);

        job.setMapOutputKeyClass(EventLogKey.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setMapperClass(FirstEventLogMapper.class);
        job.setReducerClass(FirstEventLogReducer.class);
        job.setPartitionerClass(EventLogPartitioner.class);
        job.setGroupingComparatorClass(EventLogGroupingComparator.class);

        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }

    private Job createSecondJob(String input, String output) throws IOException {
        Job job = Job.getInstance(getConfigurationForJob("second"));
        job.setJarByClass(EventLogDriver.class);

        job.setMapOutputKeyClass(ReverseEventLogKey.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setMapperClass(SecondEventLogMapper.class);
        job.setReducerClass(SecondEventLogReducer.class);
        job.setPartitionerClass(EventLogPartitioner.class);
        job.setGroupingComparatorClass(EventLogGroupingComparator.class);

        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }

    private Job createThirdJob(String input, String output) throws IOException {
        Job job = Job.getInstance(getConfigurationForJob("third"));
        job.setJarByClass(EventLogDriver.class);

        job.setMapOutputKeyClass(ReverseEventLogKey.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setMapperClass(ThirdEventLogMapper.class);
        job.setReducerClass(ThirdEventLogReducer.class);
        job.setPartitionerClass(EventLogPartitioner.class);
        job.setGroupingComparatorClass(EventLogGroupingComparator.class);

        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }
}
