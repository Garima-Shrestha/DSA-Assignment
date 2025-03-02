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
 * This program computes the minimum cost required to connect all devices
 * in a network. The devices can either install a communication module
 * individually or establish connections with each other.
 *
 * Approach:
 * - Each device has a cost to install its own communication module.
 * - There are also direct connections between devices, each with its own cost.
 * - To minimize the total cost, we use Kruskal's algorithm to form a Minimum Spanning Tree (MST),
 *   taking into account both module installation costs and direct connections.
 * - A virtual node (node 0) is introduced to simulate module installation for each device.
 * - The program sorts the edges by cost and uses a Disjoint Set Union (DSU) data structure to manage the connections.
 * - The process continues until all devices are connected, ensuring the minimum total cost.
 *
 * Time Complexity: O(E log E), where E is the number of edges (both connections and modules),
 * due to the sorting and union-find operations in Kruskal's algorithm.
 */
public class NetworkConnetion_que3a {

    // Union-Find Data Structure to manage connected components
    static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            // Initially, every device is its own parent, and rank is set to 1
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        // Find with path compression, optimizes search for root
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression step
            }
            return parent[x];
        }

        // Union by rank, connects two devices if they aren't already connected
        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            // If both devices have the same root, they are already connected
            if (rootX == rootY) return false;

            // Union by rank: attach the tree with the smaller rank under the root of the tree with higher rank
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++; // Increase rank if both roots are the same
            }

            return true;
        }
    }

    /**
     * This function calculates the minimum cost to connect all devices
     * in the network using Kruskal's algorithm.
     *
     * @param n Number of devices in the network
     * @param modules Cost of installing communication modules on each device
     * @param connections List of direct connections between devices with their costs
     * @return The minimum total cost to connect all devices
     */
    public static int minNetworkCost(int n, int[] modules, int[][] connections) {
        List<int[]> edges = new ArrayList<>();

        // Add module installation costs as edges to a virtual node (node 0)
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{0, i + 1, modules[i]}); // Connect virtual node (0) to device i
        }

        // Add the direct connections between devices
        for (int[] conn : connections) {
            edges.add(new int[]{conn[0], conn[1], conn[2]});
        }

        // Sort all edges by cost in ascending order
        edges.sort(Comparator.comparingInt(a -> a[2]));

        // Apply Kruskal's Algorithm using DSU to form MST
        DSU dsu = new DSU(n + 1); // Extra node for virtual node 0
        int minCost = 0; // Variable to store the minimum cost
        int edgesUsed = 0; // Counter to track the number of edges used

        // Iterate over the sorted edges and apply union-find
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], cost = edge[2];
            // If the devices are not already connected, connect them and add the cost
            if (dsu.union(u, v)) {
                minCost += cost;
                edgesUsed++;
                if (edgesUsed == n) break; // Stop when all devices are connected
            }
        }

        return minCost;
    }

    public static void main(String[] args) {
        // Test with different input
        int n = 4; // Number of devices
        int[] modules = {5, 1, 3, 4}; // Module costs for each device
        int[][] connections = {
            {1, 2, 2}, // Device 1 and Device 2 can connect with cost 2
            {1, 3, 1}, // Device 1 and Device 3 can connect with cost 1
            {2, 4, 2}, // Device 2 and Device 4 can connect with cost 2
            {3, 4, 3}  // Device 3 and Device 4 can connect with cost 3
        };

        // Calculate and print the minimum network cost
        System.out.println(minNetworkCost(n, modules, connections)); // The Output: 6
    }
}
