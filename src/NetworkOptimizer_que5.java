/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
/*
 * Network Design Tool
 * ---------------------
 * This application, built with Java Swing, enables users to visually design and optimize network topologies. 
 * Key functionalities include:
 * - Adding nodes (servers marked in red, clients in blue).
 * - Displaying a basic adjacency matrix for graph-based representation.
 * - Implementing Dijkstra's algorithm to calculate the shortest path.
 *
 * A simple, intuitive interface without the use of external graph libraries.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Represents a node (either server or client) within the network
class Node {
    int id, x, y;      // Unique identifier and position (x, y) of the node
    boolean isServer;  // True if this node is a server, false if it's a client

    Node(int id, int x, int y, boolean isServer) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isServer = isServer;
    }
}

// Represents a connection (edge) between two nodes in the network
class Edge {
    int from, to, cost, bandwidth;

    Edge(int from, int to, int cost, int bandwidth) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

// The panel where the network visualization and interaction take place
public class NetworkOptimizer_que5 extends JPanel {
    private final java.util.List<Node> nodes = new ArrayList<>();  // Holds all nodes in the network
    private final java.util.List<Edge> edges = new ArrayList<>();  // Holds all edges (connections) between nodes
    private int nodeCount = 0;  // A counter to track the number of nodes
    private int selectedNode1 = -1, selectedNode2 = -1;  // Stores the selected nodes for shortest path calculation
    private java.util.List<Integer> shortestPath = new ArrayList<>();  // The calculated shortest path

    public NetworkOptimizer_que5() {
        // Mouse listener to handle node addition and selection for path calculation
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Right-click: Choose nodes for pathfinding
                    selectNode(e.getX(), e.getY());
                } else {
                    // Left-click: Create a new node at the clicked position
                    addNode(e.getX(), e.getY());
                }
            }
        });
    }

    // Method to create and add a new node at the clicked coordinates
    private void addNode(int x, int y) {
        // Alternate between server (red) and client (blue)
        nodes.add(new Node(nodeCount++, x, y, nodeCount % 2 == 0));  
        repaint();  // Redraw the panel to reflect the new node
    }

    // Method to add a connection (edge) between two nodes
    private void addEdge(int from, int to, int cost, int bandwidth) {
        edges.add(new Edge(from, to, cost, bandwidth));
        repaint();  // Redraw the panel to include the new edge
    }

    // Method to select nodes for shortest path calculation
    private void selectNode(int x, int y) {
        for (Node node : nodes) {
            if (Math.abs(node.x - x) < 15 && Math.abs(node.y - y) < 15) {
                if (selectedNode1 == -1) {
                    selectedNode1 = node.id;  // First node is selected
                } else {
                    selectedNode2 = node.id;  // Second node is selected, compute the shortest path
                    computeShortestPath();
                    selectedNode1 = -1;  // Reset selections after computing the path
                    selectedNode2 = -1;
                }
                break;
            }
        }
        repaint();  // Refresh the UI after a node is selected
    }

    // Method to calculate the shortest path using Dijkstra's algorithm
    private void computeShortestPath() {
        if (selectedNode1 == -1 || selectedNode2 == -1) return;

        int n = nodes.size();
        int[][] adjMatrix = new int[n][n];
        for (int[] row : adjMatrix) Arrays.fill(row, Integer.MAX_VALUE);  // Initialize matrix with large values

        // Fill the adjacency matrix with the cost values of the edges
        for (Edge edge : edges) {
            adjMatrix[edge.from][edge.to] = edge.cost;
            adjMatrix[edge.to][edge.from] = edge.cost;
        }

        // Calculate the shortest path between the selected nodes
        shortestPath = dijkstra(adjMatrix, selectedNode1, selectedNode2);
        repaint();  // Redraw to show the calculated shortest path
    }

    // Dijkstra’s algorithm implementation to find the shortest path
    private java.util.List<Integer> dijkstra(int[][] graph, int start, int end) {
        int n = graph.length;
        int[] dist = new int[n];  // Stores the shortest distance to each node
        int[] prev = new int[n];  // Keeps track of the previous node in the shortest path
        boolean[] visited = new boolean[n];  // Tracks whether a node has been processed

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[start] = 0;

        // Min-priority queue to select the next closest node
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(i -> dist[i]));
        pq.add(start);

        while (!pq.isEmpty()) {
            int u = pq.poll();  // Extract the node with the smallest distance
            if (visited[u]) continue;
            visited[u] = true;

            // Relax edges to update shortest distances
            for (int v = 0; v < n; v++) {
                if (graph[u][v] != Integer.MAX_VALUE && !visited[v]) {
                    int newDist = dist[u] + graph[u][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                        pq.add(v);
                    }
                }
            }
        }

        // Reconstruct the shortest path by tracing back from the end node
        java.util.List<Integer> path = new ArrayList<>();
        for (int at = end; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);  // Reverse the path to get the correct order
        return path;
    }

    // Method to render the network layout, including nodes, edges, and shortest path
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw edges
        for (Edge edge : edges) {
            Node n1 = nodes.get(edge.from);
            Node n2 = nodes.get(edge.to);
            boolean isShortestPath = false;

            // Highlight the edges that are part of the shortest path
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                if ((shortestPath.get(i) == edge.from && shortestPath.get(i + 1) == edge.to) ||
                    (shortestPath.get(i) == edge.to && shortestPath.get(i + 1) == edge.from)) {
                    isShortestPath = true;
                    break;
                }
            }

            g.setColor(isShortestPath ? Color.GREEN : Color.BLACK);  // Green for shortest path
            g.drawLine(n1.x, n1.y, n2.x, n2.y);
            g.setColor(Color.BLACK);
            g.drawString("NPR" + edge.cost + ", BW:" + edge.bandwidth, (n1.x + n2.x) / 2, (n1.y + n2.y) / 2);
        }

        // Draw nodes (servers and clients)
        for (Node node : nodes) {
            g.setColor(node.isServer ? Color.RED : Color.BLUE);  // Red for server, blue for client
            g.fillOval(node.x - 10, node.y - 10, 20, 20);  // Draw the node as a circle
            g.setColor(Color.BLACK);
            g.drawString("N" + node.id, node.x - 5, node.y - 15);  // Label the node with its ID
        }
    }

    // Main method to initialize and display the GUI
    public static void main(String[] args) {
        JFrame frame = new JFrame("Network Optimizer");
        NetworkOptimizer_que5 panel = new NetworkOptimizer_que5();

        JButton addEdgeButton = new JButton("Add Edge");
        JTextField fromField = new JTextField(2);
        JTextField toField = new JTextField(2);
        JTextField costField = new JTextField(3);
        JTextField bwField = new JTextField(3);

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("From:"));
        controlPanel.add(fromField);
        controlPanel.add(new JLabel("To:"));
        controlPanel.add(toField);
        controlPanel.add(new JLabel("Cost:"));
        controlPanel.add(costField);
        controlPanel.add(new JLabel("BW:"));
        controlPanel.add(bwField);
        controlPanel.add(addEdgeButton);

        addEdgeButton.addActionListener(e -> {
            try {
                int from = Integer.parseInt(fromField.getText());
                int to = Integer.parseInt(toField.getText());
                int cost = Integer.parseInt(costField.getText());
                int bw = Integer.parseInt(bwField.getText());
                panel.addEdge(from, to, cost, bw);  // Add an edge between the two nodes
            } catch (NumberFormatException ignored) {}
        });

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);  // Make the window visible
    }
}
