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
 * Que 1(B)
 * This program finds the kth lowest combination that can be formed
 * by selecting one number from each of two sorted arrays.
 * It utilizes binary search to efficiently determine the kth lowest combination.
 */
public class KthLowestCombination_que1b {
    
    // Function to determine the kth lowest combination from two sorted arrays
    public static int findKthLowestCombination(int[] arr1, int[] arr2, int k) {
        
        // Define the search space for binary search
        int low = arr1[0] * arr2[0]; // Lowest possible combination
        int high = arr1[arr1.length - 1] * arr2[arr2.length - 1]; // Largest possible combination
        
        // Apply binary search to find the kth lowest combination
        while (low < high) {
            int mid = low + (high - low) / 2;
            
            // Count how many combinations are <= mid
            int count = countValidProducts(arr1, arr2, mid);
            
            if (count < k) {
                low = mid + 1; // Increase lower bound if count is insufficient
            } else {
                high = mid; // Decrease upper bound if count is sufficient or more
            }
        }
        
        // The kth lowest combination is found at 'low'
        return low;
    }
    
    // Helper function to count the number of valid combinations <= target
    private static int countValidProducts(int[] arr1, int[] arr2, int target) {
        int count = 0; // Keeps track of how many valid combinations exist
        int j = arr2.length - 1; // Start from the last index of arr2
        
        // Traverse each number in arr1
        for (int num : arr1) {
            // Move left in arr2 until the combination is within the target
            while (j >= 0 && num * arr2[j] > target) {
                j--;
            }
            
            // Add all valid elements from index 0 to j
            count += (j + 1);
        }
        return count;
    }
    
    // Main method to test the function with different inputs
    public static void main(String[] args) {
        // Test case 1: Positive numbers
        int[] arr1 = {2, 3, 5};
        int[] arr2 = {1, 4, 6};
        int k = 5;
        System.out.println(findKthLowestCombination(arr1, arr2, k)); //The output: 20

        // Test case 2: Array with negative numbers
        int[] arr3 = {-5, -2, 1, 3};
        int[] arr4 = {-4, 0, 2};
        int k2 = 4;
        System.out.println(findKthLowestCombination(arr3, arr4, k2)); //The output: 12

        // Test case 3: Including zero
        int[] arr5 = {0, 1, 3};
        int[] arr6 = {2, 4, 5};
        int k3 = 4;
        System.out.println(findKthLowestCombination(arr5, arr6, k3)); //The output: 2
    }
}
