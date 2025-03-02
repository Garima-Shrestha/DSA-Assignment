/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */

import java.util.*;

public class TrendingHashtags_que4a {
    
    // This method calculates the top trending hashtags based on tweet data
    public static List<Map.Entry<String, Integer>> findTopTrendingHashtags(List<Tweet> tweets) {
        Map<String, Integer> hashtagCount = new HashMap<>(); // A map to store the frequency of each hashtag
        
        // Iterate through the provided tweets
        for (Tweet tweet : tweets) {
            // Only focus on tweets that are from February 2024
            if (tweet.date.startsWith("2024-02")) {
                // Split the tweet text into words
                String[] words = tweet.text.split("\\s+"); 
                for (String word : words) {
                    // Check if the word starts with a '#' symbol (indicating it's a hashtag)
                    if (word.startsWith("#")) {
                        // If it's a hashtag, increment its count in the map
                        hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        // Sort the hashtags first by frequency (in descending order), then alphabetically
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet()); // Convert map to list of entries
        sortedHashtags.sort((a, b) -> {
            if (!b.getValue().equals(a.getValue())) {
                return b.getValue() - a.getValue(); // Sort by frequency in descending order
            }
            return a.getKey().compareTo(b.getKey()); // If frequencies are the same, sort alphabetically
        });

        // Return the top 3 hashtags or fewer if there aren't enough
        return sortedHashtags.subList(0, Math.min(3, sortedHashtags.size())); // Get top 3 hashtags
    }

    public static void main(String[] args) {
        // Sample tweets with updated input for a different output
        List<Tweet> tweets = Arrays.asList(
            new Tweet(135, "2024-02-01", "Excited for the weekend! #WeekendVibes #FunTimes"),
            new Tweet(136, "2024-02-03", "Learning new things every day. #KnowledgeIsPower #StayCurious"),
            new Tweet(137, "2024-02-05", "Health is wealth! #FitnessGoals #HealthyLiving"),
            new Tweet(138, "2024-02-06", "Traveling to new places. #Wanderlust #TravelDiaries"),
            new Tweet(139, "2024-02-08", "Celebrating small victories. #MotivationMonday #KeepGoing"),
            new Tweet(140, "2024-02-09", "Nature is the best therapy. #NatureLover #Mindfulness"),
            new Tweet(141, "2024-02-10", "Chasing dreams and making them happen! #DreamBig #Inspiration")
        );

        // Get the top trending hashtags from the tweets
        List<Map.Entry<String, Integer>> topHashtags = findTopTrendingHashtags(tweets);

        // Display the results with a simple header
        System.out.println("Hashtag\tCount"); // Display header for hashtags and their counts
        for (Map.Entry<String, Integer> entry : topHashtags) {
            System.out.println(entry.getKey() + "\t" + entry.getValue()); // Print each hashtag and its count
        }
    }
}

// Tweet class to represent each tweet's data
class Tweet {
    int id; // Unique identifier for each tweet
    String date; // The date the tweet was posted
    String text; // The content of the tweet

    // Constructor to initialize the Tweet object
    Tweet(int id, String date, String text) {
        this.id = id; // Assign the tweet's ID
        this.date = date; // Assign the date of the tweet
        this.text = text; // Assign the text content of the tweet
    }
}