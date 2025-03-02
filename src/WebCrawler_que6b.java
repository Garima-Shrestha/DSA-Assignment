/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

/**
 * WebCrawler: A simple multi-threaded web crawler.
 * 
 * In this program, we simulate crawling by visiting URLs, fetching their content, 
 * and extracting more URLs from the page. Multiple threads are utilized for 
 * fetching pages concurrently.
 */
public class WebCrawler_que6b {
    // A thread-safe queue for URLs that need to be crawled
    private final Queue<String> urlQueue = new ConcurrentLinkedQueue<>();

    // A thread-safe set to keep track of the visited URLs and avoid revisiting
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();

    // A pool of threads to handle multiple crawling tasks concurrently
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    // Stores the contents of the crawled pages (thread-safe)
    private final ConcurrentHashMap<String, String> crawledData = new ConcurrentHashMap<>();

    // Constructor initializes the queue with the starting URL
    public WebCrawler_que6b(String startUrl) {
        urlQueue.add(startUrl);
    }

    // Begins the crawling process with a limit on the number of pages to crawl
    public void startCrawling(int maxPages) {
        // Continue crawling while there are URLs left in the queue and the maxPages limit isn't reached
        while (!urlQueue.isEmpty() && visitedUrls.size() < maxPages) {
            String url = urlQueue.poll(); // Get the next URL to crawl
            if (url != null && visitedUrls.add(url)) { // Avoid crawling the same URL twice
                executor.submit(() -> crawl(url)); // Delegate crawling task to thread pool
            }
        }
        shutdownExecutor(); // Shut down the executor service after crawling is done
    }

    // Crawls a given URL: fetches content and extracts new links
    private void crawl(String url) {
        try {
            System.out.println("Crawling: " + url);
            String content = fetchContent(url); // Fetch the page content
            crawledData.put(url, content); // Save the content of the page
            extractLinks(content); // Simulate extracting more links from the content
        } catch (Exception e) {
            System.err.println("Failed to crawl " + url + ": " + e.getMessage());
        }
    }

    // Fetches the content of a webpage using an HTTP GET request
    private String fetchContent(String urlString) throws Exception {
        StringBuilder content = new StringBuilder();
        URL url = new URL(urlString); // Convert string to URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Open connection
        connection.setRequestMethod("GET"); // Use GET method for the request

        // Read the response from the server
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) { // Read each line from the response
                content.append(line); // Append the line to the content
            }
        }
        return content.toString(); // Return the complete page content
    }

    // Simulates extracting new URLs from the page's content
    private void extractLinks(String content) {
        // Normally we'd use an HTML parser, but for this example, we simulate new URLs
        if (visitedUrls.size() < 15) { // Limit to adding 15 links for this demo
            urlQueue.add("http://example.com/page" + (visitedUrls.size() + 1)); // Simulate adding new links
        }
    }

    // Gracefully shuts down the executor service after all tasks are finished
    private void shutdownExecutor() {
        executor.shutdown(); // No new tasks will be accepted
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) { // Wait for up to 5 seconds for tasks to finish
                executor.shutdownNow(); // Force shutdown if tasks are still running
            }
        } catch (InterruptedException e) {
            executor.shutdownNow(); // Interrupt and force shutdown if interrupted
        }
    }

    // Prints out the crawled page data (only the first 100 characters of each page's content)
    public void printCrawledData() {
        System.out.println("Crawled Pages:");
        for (String url : crawledData.keySet()) {
            // Print the URL along with the first 100 characters of its content
            System.out.println(url + " -> " + crawledData.get(url).substring(0, Math.min(100, crawledData.get(url).length())) + "...");
        }
    }

    // Main method to initiate the crawling process
    public static void main(String[] args) {
        // Initialize the crawler with a starting URL
        WebCrawler_que6b crawler = new WebCrawler_que6b("http://example.com");

        // Start crawling, with a limit of 10 pages
        crawler.startCrawling(10);

        // Once crawling is done, print the crawled page content
        crawler.printCrawledData();
    }
}
