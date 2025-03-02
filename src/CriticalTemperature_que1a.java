/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Acer
 */
/**
 * This program calculates the minimum number of steps required to reach a specific measurement target.
 * The computation is based on a recursive relationship where each step depends on the sum of previous values.
 */

public class CriticalTemperature_que1a {
    /**
     * Que 1(A)
     * Determines the minimum number of steps required to reach at least 'n' total measurements.
     * The calculation follows a recursive pattern where each step depends on the previous two values plus one.
     *
     * @param k The number of available measurement levels.
     * @param n The target total number of measurements.
     * @return The minimum number of steps needed to meet or exceed 'n'.
     */
    public static int minMeasurements(int k, int n) {
        // Array to store intermediate measurement counts, indexed from 0 to k.
        int[] measurements = new int[k + 1];
        
        // Counter to keep track of the number of iterations.
        int steps = 0;
        
        // Keep looping until we reach the required number of total measurements.
        while (measurements[k] < n) {
            steps++;
            
            // Update measurement counts in reverse to prevent overwriting values prematurely.
            for (int i = k; i >= 1; i--) {
                measurements[i] = measurements[i] + measurements[i - 1] + 1;
            }
        }
        
        return steps;
    }

    public static void main(String[] args) {
        // Running the function with different values to test various cases.
        System.out.println(minMeasurements(2, 5));   // The output: 3
        System.out.println(minMeasurements(3, 25));  // The output: 5
        System.out.println(minMeasurements(5, 100)); // The output: 8 
    }
}
