package org.example.BackPressure;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadSafeQueue {
        Queue<MatrixPair> matrixQueue;
        private final int QUEUE_CAPACITY = 15;
        private int counter;
        public boolean terminated = false;

        public ThreadSafeQueue() {
            this.matrixQueue = new LinkedList<>();
            this.counter = 0;
            this.terminated = false;
        }

        public synchronized void addMatrixPair(MatrixPair matrixPair){
            while(isQueueFull()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            counter++;
            matrixQueue.add(matrixPair);
            this.notifyAll();
        }

        public synchronized MatrixPair removeMatrixPair(){
            while(isQueueEmpty() && !isOperationTerminated()){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(isQueueEmpty()) return null;
            counter--;
            MatrixPair matrixPair = matrixQueue.remove();
            this.notifyAll();
            return matrixPair;
        }

        public synchronized void terminateOperation(){
            terminated = true;
            this.notifyAll();
        }
        public synchronized boolean isOperationTerminated(){
            return terminated == true;
        }
        private synchronized boolean isQueueFull(){
            return counter == QUEUE_CAPACITY;
        }
        private synchronized boolean isQueueEmpty(){
            return counter == 0;
        }
    }
