/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */

/*
 * This program utilizes three separate threads to print a sequence of numbers in the following interleaved order: "0102030405...".
 * The main logic is divided into the following components:
 *  - `NumberPrinter`: This class is responsible for printing zeros, even numbers, and odd numbers individually.
 *  - `ThreadController`: This class manages synchronization between the three threads.
 *    - `printZero()`: Prints '0' before each number.
 *    - `printEven()`: Prints even numbers in sequential order.
 *    - `printOdd()`: Prints odd numbers in sequential order.
 *  - The threads are synchronized using `wait()` and `notifyAll()` to ensure they execute in the correct order without conflict.
 */

class NumberPrinter {
    // This method prints the zero character.
    public void printZero() {
        System.out.print(0);
    }

    // This method prints even numbers in the sequence.
    public void printEven(int num) {
        System.out.print(num);
    }

    // This method prints odd numbers in the sequence.
    public void printOdd(int num) {
        System.out.print(num);
    }
}

class ThreadController {
    private final int n;                // The upper limit for the number sequence
    private final NumberPrinter printer; // The printer instance to print numbers
    private int counter = 1;            // Counter to manage synchronization and track the sequence
    private final Object lock = new Object();  // Lock object used for synchronization between threads
    private boolean printZero = true;   // Flag to control when to print zeros

    // Constructor for initializing the ThreadController with the number limit and the printer instance
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    // This method is executed by the thread responsible for printing zeros
    public void printZero() {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (!printZero) { // Wait if it's not the turn to print zero
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printZero(); // Print zero
                printZero = false; // Set flag to print numbers next
                lock.notifyAll();   // Notify other threads to proceed
            }
        }
    }

    // This method is executed by the thread responsible for printing even numbers
    public void printEven() {
        for (int i = 2; i <= n; i += 2) {
            synchronized (lock) {
                while (printZero || counter % 2 == 1) { // Wait if it's not the turn for even numbers
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printEven(i); // Print even number
                counter++;             // Update the counter after printing
                printZero = true;      // Set flag to print zero next
                lock.notifyAll();      // Notify other threads to continue
            }
        }
    }

    // This method is executed by the thread responsible for printing odd numbers
    public void printOdd() {
        for (int i = 1; i <= n; i += 2) {
            synchronized (lock) {
                while (printZero || counter % 2 == 0) { // Wait if it's not the turn for odd numbers
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printOdd(i); // Print odd number
                counter++;            // Update the counter after printing
                printZero = true;     // Set flag to print zero next
                lock.notifyAll();     // Notify other threads to continue
            }
        }
    }
}

public class NumberPrinter_que6a {
    public static void main(String[] args) {
        int n = 8;  // Define the number up to which the sequence should be printed
        NumberPrinter printer = new NumberPrinter(); // Create the printer instance
        ThreadController controller = new ThreadController(n, printer); // Initialize the controller

        // Create threads for printing zero, even numbers, and odd numbers
        Thread zeroThread = new Thread(controller::printZero);
        Thread evenThread = new Thread(controller::printEven);
        Thread oddThread = new Thread(controller::printOdd);

        // Start all threads
        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}
