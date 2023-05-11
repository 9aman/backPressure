package org.example.BackPressure;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String outputFilePath = "/Users/aman.khanchandani/output.txt";
    private static final String inputFilePath = "/Users/aman.khanchandani/testout.txt";
    private static final int numberOfProducers = 1;
    private static final int numberOfConsumers = 5;


    public static void main(String[] args) throws IOException, InterruptedException {
        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFilePath));
        List<Thread> producerThreads = new ArrayList<>();
        List<Thread> consumerThreads = new ArrayList<>();
        for(int i=0;i<numberOfProducers;i++) {
            try {
                producerThreads.add(new Producer(threadSafeQueue, bufferedReader));
            } catch (FileNotFoundException e) {
                System.out.println("File not found by the Producer");
                return;
            }
        }
        for(int i=0;i<numberOfConsumers;i++) {
            consumerThreads.add(new Consumer(threadSafeQueue, bufferedWriter));
        }
        long startTime = System.currentTimeMillis();
        for(Thread producerThread: producerThreads) producerThread.start();
        for(Thread consumerThread: consumerThreads) consumerThread.start();
        for(Thread producerThread: producerThreads) producerThread.join();
        for(Thread consumerThread: consumerThreads) consumerThread.join();
        long endTime = System.currentTimeMillis();
        bufferedReader.close();
        bufferedWriter.close();
        System.out.println("Execution time in milis: " + (endTime-startTime));
    }
}
