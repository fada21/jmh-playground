package com.fada21.jmh;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VariantsBenchmark {

    @Benchmark
    public void sanityTest() throws InterruptedException {
        List<Long> list = new ArrayList<>();
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
            list.add(sum);
        }
    }

    @Benchmark
    public void sanityTestBig() throws InterruptedException {
        List<Long> list = new ArrayList<>();
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                sum += i + j;
                list.add(sum);
            }
        }
    }
}