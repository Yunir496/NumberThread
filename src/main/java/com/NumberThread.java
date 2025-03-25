package com;
//коммит для проверки
public class NumberThread {

    private static final int MAX_NUMBER = 20;
    private static final Object lock = new Object();
    private static boolean isEvenTurn = true;

    public static void main(String[] args) {
        Thread evenThread  = new Thread(()->{
            for (int i = 2; i < MAX_NUMBER; i+=2) {
                printNumber(i,true);
            }
        });
        Thread oddThread = new Thread(()->{
            for (int i = 1; i < MAX_NUMBER; i+=2) {
                printNumber(i,false);
            }
        });
        evenThread.start();
        oddThread.start();

        try{
            evenThread.join();
            oddThread.join();
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("Ожидание завершения потоков прервано");
        }
        System.out.println("Программа завершена");
    }

    private static void printNumber(int number, boolean isEven) {
        synchronized (lock) {
            while ((isEven && !isEvenTurn) || (!isEven && isEvenTurn)) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println(isEven ? "Четное: " + number : "Нечетное " + number);
            isEvenTurn = !isEvenTurn;
            lock.notify();
        }
    }
}
