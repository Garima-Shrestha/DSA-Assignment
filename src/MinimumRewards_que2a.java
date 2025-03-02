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
 * This program determines the minimum number of rewards needed
 * to distribute among employees based on their performance scores.
 * Each employee must receive at least one reward, and an employee
 * with a higher score than their adjacent colleagues must receive more rewards.
 */
public class MinimumRewards_que2a {
    
    // Method to calculate the minimum rewards required
    public static int minRewards(int[] scores) {
        int n = scores.length;
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1); // Everyone gets at least one reward
        
        // Left-to-right pass: Give more rewards if the score is increasing
        for (int i = 1; i < n; i++) {
            if (scores[i] > scores[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        // Right-to-left pass: Adjust rewards to maintain relative order
        for (int i = n - 2; i >= 0; i--) {
            if (scores[i] > scores[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        // Sum all rewards to get the final total
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }
        
        return totalRewards;
    }
    
    // Test the function with different performance scores
    public static void main(String[] args) {
        int[] scores1 = {1, 3, 8, 12, 66};
        System.out.println(minRewards(scores1)); // The output: 15
        
        int[] scores2 = {4, 2, 3, 4, 32, 5};
        System.out.println(minRewards(scores2)); // The output: 13
    }
}
