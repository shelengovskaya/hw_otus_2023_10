package ru.otus;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintingSequence {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int lastThread = 2;

    private void action(int currentThread, int start, int end, int step) {
        int currentPosition = start;
        while (currentPosition-step != end) {
            lock.lock();
            try {
                while (lastThread == currentThread) {
                    condition.await();
                }
                System.out.println(Thread.currentThread().getName() + " --- " + currentPosition);
                lastThread = currentThread;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
            currentPosition += step;
        }
    }

    public void print() {
        new Thread(() -> {
            action(1, 1, 10, 1);
            action(1, 9, 1, -1);
        }).start();
        new Thread(() -> {
            action(2, 1, 10, 1);
            action(2, 9, 1, -1);
        }).start();
    }
}
