package com.kpmg.agata.mapreduce;

import com.kpmg.agata.mapreduce.first.EventLogKey;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class EventLogPartitioner extends Partitioner<EventLogKey, Text> {
    @Override
    public int getPartition(EventLogKey key, Text value, int numPartitions) {
        int hash = key.getCode().hashCode() + key.getDoName().hashCode();
        return Math.abs(hash % numPartitions);
    }
}
