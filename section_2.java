
// File: ThreadExamples.java
// Topic: Creating Threads in Java

// Example 1: Extending the Thread class
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Hello from MyThread");
    }
}

// Example 2: Implementing the Runnable interface
class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello from MyTask");
    }
}

public class ThreadExamples {
    public static void main(String[] args) {
        // Extending Thread example
        Thread t1 = new MyThread();
        t1.start();

        // Implementing Runnable example
        Runnable task = new MyTask();
        Thread t2 = new Thread(task);
        t2.start();
    }
}
