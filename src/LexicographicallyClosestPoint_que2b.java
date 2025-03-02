/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */

import java.util.*;

/**
 * This Java program finds the pair of points in a 2D plane that are closest
 * to each other based on the Manhattan distance and are also the smallest in 
 * lexicographical order when distances are equal.
 */
public class LexicographicallyClosestPoint_que2b {
    
    /**
     * This method finds the closest pair of points based on Manhattan distance
     * and returns the indices of the pair that is closest and lexicographically smallest.
     * 
     * @param x_coords Array of x-coordinates of the points.
     * @param y_coords Array of y-coordinates of the points.
     * @return An array containing the indices of the two closest points.
     */
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length; // Get the total number of points
        int minDistance = Integer.MAX_VALUE; // Initialize minimum distance as a very large value
        int[] result = new int[2]; // This will hold the indices of the closest pair
        
        // Compare each pair of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance between point i and point j
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);
                
                // If a new minimum distance is found or if distances are the same but the pair is lexicographically smaller
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance; // Update minimum distance
                    result[0] = i; // Store index of first point
                    result[1] = j; // Store index of second point
                }
            }
        }
        
        return result; // Return the indices of the closest pair
    }
    
    /**
     * This is the main method to run the test and demonstrate the function.
     */
    public static void main(String[] args) {
    // Define some new test input coordinates
    int[] x_coords = {1, 3, 2, 5, 4};
    int[] y_coords = {1, 2, 3, 4, 5};
    
    // Find the closest pair of points based on Manhattan distance
    int[] closestPair = findClosestPair(x_coords, y_coords);
    
    // Print the result, which should return the indices of the closest pair
    System.out.println(Arrays.toString(closestPair)); // The Output [1,2]
}
}
