package org.example.BackPressure;

import java.io.BufferedWriter;
import java.io.IOException;

public class Consumer extends Thread {

    private ThreadSafeQueue threadSafeQueue;
    private BufferedWriter bufferedWriter;

    public Consumer(ThreadSafeQueue threadSafeQueue, BufferedWriter bufferedWriter) throws IOException {
        this.threadSafeQueue = threadSafeQueue;
        this.bufferedWriter = bufferedWriter;
    }

    @Override
    public void run() {
        try {
            storeMatrixMultiplicationInFile();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeMatrixMultiplicationInFile() throws IOException, InterruptedException {
        MatrixPair matrixPair;
        while ((matrixPair = getNexPair()) != null){
            storeMatrixInFile(getMatrixMul(matrixPair));
            Thread.sleep(100);
        }
    }

    private double[][] getMatrixMul(MatrixPair matrixPair) {
        double[][] matrix1 = matrixPair.matrix1;
        double[][] matrix2 = matrixPair.matrix2;
        double[][] result = new double[10][10];
        for(int i = 0; i<10;i++){
            for(int j=0;j<10;j++){
                for(int k=0;k<10;k++){
                    result[i][j] += matrix1[i][k]*matrix2[k][j];
                }
            }
        }
        return result;
    }

    private void storeMatrixInFile(double[][] matrix) throws IOException {
        StringBuilder csv = new StringBuilder();
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                csv.append(matrix[i][j] + "");
                if(j != 9) csv.append(",");
            }
            csv.append("\n");
        }
        bufferedWriter.write(csv.toString());
    }
    private synchronized MatrixPair getNexPair(){
        return threadSafeQueue.removeMatrixPair();
    }
}
