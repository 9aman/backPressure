package org.example.BackPressure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Producer extends Thread{
    private final String filePath = "/Users/aman.khanchandani/testout.txt";
    private BufferedReader bufferedReader;
    private ThreadSafeQueue threadSafeQueue;

    public Producer(ThreadSafeQueue threadSafeQueue, BufferedReader bufferedReader) throws FileNotFoundException {
        this.bufferedReader = new BufferedReader(new FileReader(filePath));
        this.threadSafeQueue = threadSafeQueue;
    }

    @Override
    public void run() {
        try {
            pushElementsInQueue();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushElementsInQueue() throws IOException, InterruptedException {
        MatrixPair matrixPair;
        while (true) {
            matrixPair = getNextMatrixPair();
            if (matrixPair == null) {
                threadSafeQueue.terminateOperation();
                break;
            }
            threadSafeQueue.addMatrixPair(matrixPair);
        }
    }

    private synchronized MatrixPair getNextMatrixPair() {
        try {
            double[][] matrix1 = getNextMatrix();
            double[][] matrix2 = getNextMatrix();
            if (matrix1 == null || matrix2 == null) return null;
            return new MatrixPair(matrix1, matrix2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private double[][] getNextMatrix() throws IOException {
        double[][] matrix = null;
        int row = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if(row == 0) matrix = new double[10][10];
            String[] columns = line.split(",");
            int col = 0;
            for (String column : columns) {
                matrix[row][col] = Double.parseDouble(column);
                col++;
            }
            row++;
            if (row == 10) break;
        }
        return matrix;
    }


//    public static void main(String []args) {
//        try {
//            bufferedReader = new BufferedReader(new FileReader(filePath));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        for(int i=0;i<2;i++){
//            try {
//                printMatrix(getNextMatrix());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//    private static void printMatrix(double[][] matrix){
//        for(int i=0;i<10;i++){
//            for(int j=0;j<10;j++){
//                System.out.print(matrix[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
}