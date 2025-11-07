
// ParallelComputation.java
import java.util.List;
import java.util.ArrayList;

public class ParallelComputation {

    // ---------------------------
    // 1. THREAD NAMING
    // ---------------------------
    static class ThreadNamingExample {
        public static void run() {
            // Naming during thread creation
            Thread t1 = new Thread(() -> {
                System.out.println("Running: " + Thread.currentThread().getName());
            }, "CustomThread-1");

            // Using setName() method
            Thread t2 = new Thread(() -> {
                System.out.println("Running: " + Thread.currentThread().getName());
            });
            t2.setName("RenamedThread-2");

            // Naming threads with Runnable
            Runnable task = () -> System.out.println("Running: " + Thread.currentThread().getName());
            Thread t3 = new Thread(task, "RunnableThread-3");

            t1.start();
            t2.start();
            t3.start();

            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------------------
    // 2. THREAD PRIORITY
    // ---------------------------
    static class ThreadPriorityExample {
        public static void run() {
            Thread high = new Thread(() -> System.out.println("High priority: " + Thread.currentThread().getPriority()), "High");
            Thread low = new Thread(() -> System.out.println("Low priority: " + Thread.currentThread().getPriority()), "Low");

            high.setPriority(Thread.MAX_PRIORITY);
            low.setPriority(Thread.MIN_PRIORITY);

            high.start();
            low.start();

            try {
                high.join();
                low.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------------------
    // 3. THREAD STATES
    // ---------------------------
    static class ThreadStatesExample {
        public static void run() {
            Thread t = new Thread(() -> {
                System.out.println("Thread running: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("State before start: " + t.getState());
            t.start();
            System.out.println("State after start: " + t.getState());

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("State after finish: " + t.getState());
        }
    }

    // ---------------------------
    // 4. THREAD GROUP
    // ---------------------------
    static class ThreadGroupExample {
        public static void run() {
            ThreadGroup group = new ThreadGroup("ExampleGroup");

            Runnable task = () -> {
                System.out.println("Running in group: " + Thread.currentThread().getThreadGroup().getName() +
                                   " - " + Thread.currentThread().getName());
            };

            Thread t1 = new Thread(group, task, "GroupThread-1");
            Thread t2 = new Thread(group, task, "GroupThread-2");

            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Active threads in group: " + group.activeCount());
        }
    }

    // ---------------------------
    // 5. DAEMON VS USER THREADS
    // ---------------------------
    static class DaemonVsUserExample {
        public static void run() {
            Thread userThread = new Thread(() -> {
                System.out.println("User thread running...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("User thread finished.");
            });

            Thread daemonThread = new Thread(() -> {
                while (true) {
                    System.out.println("Daemon thread running...");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            daemonThread.setDaemon(true);

            userThread.start();
            daemonThread.start();

            try {
                userThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Main thread ending - JVM will exit now (daemon stops).");
        }
    }

    // ---------------------------
    // 6. LAB EXERCISE: MULTIEXECUTOR
    // ---------------------------
    static class MultiExecutor {
        private final List<Runnable> tasks;

        public MultiExecutor(List<Runnable> tasks) {
            this.tasks = tasks;
        }

        public void executeAll() {
            List<Thread> threads = new ArrayList<>();
            for (Runnable task : tasks) {
                Thread thread = new Thread(task);
                threads.add(thread);
                thread.start();
            }

            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ---------------------------
    // MAIN METHOD - RUN ALL
    // ---------------------------
    public static void main(String[] args) {
        System.out.println("=== THREAD NAMING ===");
        ThreadNamingExample.run();

        System.out.println("\n=== THREAD PRIORITY ===");
        ThreadPriorityExample.run();

        System.out.println("\n=== THREAD STATES ===");
        ThreadStatesExample.run();

        System.out.println("\n=== THREAD GROUP ===");
        ThreadGroupExample.run();

        System.out.println("\n=== DAEMON VS USER THREADS ===");
        DaemonVsUserExample.run();

        System.out.println("\n=== MULTIEXECUTOR LAB ===");
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(() -> System.out.println("Task 1 running"));
        tasks.add(() -> System.out.println("Task 2 running"));
        tasks.add(() -> System.out.println("Task 3 running"));

        MultiExecutor executor = new MultiExecutor(tasks);
        executor.executeAll();
    }
}
