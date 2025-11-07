// File: ParallelComputationExamples.java

public class ParallelComputationExamples {

    // ====================================================================
    // 1. TRY-CATCH EXCEPTION HANDLING (Based on MultiThreadExceptionExample)
    // ====================================================================

    static class WorkerTryCatch extends Thread {
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " started.");
                // Intentional division by zero to throw ArithmeticException
                int result = 100 / 0; 
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " caught: " + e.getMessage());
            }
            System.out.println(Thread.currentThread().getName() + " finished.");
        }
    }

    private static void runTryCatchExample() throws InterruptedException {
        System.out.println("\n--- 1. TRY-CATCH Exception Handling Example ---");
        WorkerTryCatch t1 = new WorkerTryCatch();
        WorkerTryCatch t2 = new WorkerTryCatch();
        t1.setName("TryCatch-1");
        t2.setName("TryCatch-2");
        
        t1.start();
        t2.start();

        // Wait for threads to finish before starting the next example
        t1.join();
        t2.join();
    }
    
    // ====================================================================
    // 2. UNCAUGHTEXCEPTIONHANDLER (Based on Example_UncaughtHandler)
    // ====================================================================

    static class WorkerUncaught extends Thread {
        public void run() {
            // No try-catch here - exception will be uncaught
            System.out.println("Thread started: " + getName());
            int x = 10 / 0; // This throws ArithmeticException
        }
    }

    private static void runUncaughtHandlerExample() throws InterruptedException {
        System.out.println("\n--- 2. UncaughtExceptionHandler Example ---");
        WorkerUncaught t1 = new WorkerUncaught();
        t1.setName("UncaughtHandler-1");
        
        // Setting the thread-specific handler
        t1.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("Exception in " + thread.getName() + " :" +
                    exception.getMessage());
        });
        
        t1.start();
        t1.join();
    }

    // ====================================================================
    // 3. DEFAULTUNCAUGHTEXCEPTIONHANDLER (Based on DefaultHandlerExample)
    // ====================================================================

    private static void runDefaultUncaughtHandlerExample() throws InterruptedException {
        System.out.println("\n--- 3. DefaultUncaughtExceptionHandler Example ---");

        // Set the global default handler
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("Global handler caught exception in: " +
                    thread.getName());
            System.out.println("Error: " + exception.getMessage());
        });

        // T1 will use the default handler
        Thread t1 = new Thread(() -> {
            throw new RuntimeException("Thread crashed!");
        }, "DefaultHandler-1"); 

        // T2 will also use the default handler
        Thread t2 = new Thread(() -> {
            throw new ArithmeticException("Division by zero!");
        }, "DefaultHandler-2");
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // IMPORTANT: Resetting the default handler to null after the example 
        // to prevent it from affecting the final race condition printout.
        Thread.setDefaultUncaughtExceptionHandler(null);
    }
    
    // ====================================================================
    // 4. RACE CONDITION EXAMPLE (Based on RaceConditionExample)
    // ====================================================================

    private static int counter = 0; // Shared resource

    static class MyTask implements Runnable {
        public void run() {
            for (int i = 0; i < 5; i++) {
                int current = counter; // Read shared variable
                
                try {
                    // Small delay to increase overlap and make race condition more likely
                    Thread.sleep(10); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Unsynchronized Read-Modify-Write cycle
                counter = current + 1; 
                
                System.out.println(Thread.currentThread().getName()
                        + " -> Current (Read): " + current + ", Updated (Write): " + counter);
            }
        }
    }

    private static void runRaceConditionExample() throws InterruptedException {
        System.out.println("\n--- 4. Race Condition Example ---");
        // Reset counter for a fresh run
        counter = 0;

        Thread t1 = new Thread(new MyTask(), "RaceThread-1");
        Thread t2 = new Thread(new MyTask(), "RaceThread-2");
        
        t1.start();
        t2.start();

        // Wait for both threads to finish
        t1.join(); 
        t2.join();
        System.out.println("\nFinal Counter Value (Should be 10, may be less due to race): " + counter);
    }

    // ====================================================================
    // MAIN METHOD TO RUN ALL EXAMPLES
    // ====================================================================

    public static void main(String[] args) throws InterruptedException {
        runTryCatchExample();
        runUncaughtHandlerExample();
        runDefaultUncaughtHandlerExample();
        runRaceConditionExample();
    }
}
