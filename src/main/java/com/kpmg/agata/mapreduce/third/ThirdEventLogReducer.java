package com.kpmg.agata.mapreduce.third;

import com.kpmg.agata.mapreduce.second.ReverseEventLogKey;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.time.LocalDate;

public class ThirdEventLogReducer extends Reducer<ReverseEventLogKey, Text, Text, NullWritable> {
    private static final int LIMIT_MONTH = 3;

    @Override
    public void reduce(ReverseEventLogKey key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        LocalDate actualPdzDate = LocalDate.parse(key.getDate().toString());
        LocalDate limitDate = actualPdzDate.minusMonths(LIMIT_MONTH);

        for (Text value : values) {
            LocalDate currentDate = LocalDate.parse(key.getDate().toString());
            if (currentDate.isBefore(limitDate) || currentDate.equals(limitDate)) return;

            context.write(value, NullWritable.get());
        }
    }
}
