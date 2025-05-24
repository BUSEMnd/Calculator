package com.buse.proje;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private static final String USERS_FILE_NAME = "users.txt";
    private static final Map<String, String> users = new HashMap<>();
    public static String currentUser = null;
//static back to oad users when class is first loaded
    static {
        loadUsers();
    }
//Save a new number to users map and append to file
    public static void saveUser(String username, String password) {
        if (users.containsKey(username)) {
            System.err.println("User already exists " + username);
            return;
        }
        users.put(username, password);
        // append new user credentials to users file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_NAME, true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error while registering user: " + e.getMessage());
        }
    }
    //Authenticate user by checking if username and password watch
    public static boolean authenticate(String username, String password) {
        boolean isAuthenticated = users.containsKey(username) && users.get(username).equals(password);
        if (isAuthenticated) {
            currentUser = username;
        }
        return isAuthenticated;
    }
    //load all users from users.txt into the users map
    static void loadUsers() {
        File file = new File(USERS_FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
//save a calculation history entry for the current user
    public static void saveHistory(String entry) {
        if (currentUser == null) {
            System.err.println("User not logged in, history is not saved.");
            return;
        }
        String fileName = currentUser + "_history.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error while saving history: " + e.getMessage());
        }
    }
// load calculation history lines for the current user
    public static List<String> loadHistory() {
        List<String> userHistory = new ArrayList<>();
        if (currentUser == null) {
            System.err.println("User not logged in,history cannot  be loaded.");
            return userHistory;
        }
        String fileName = currentUser + "_history.txt";
        File file = new File(fileName);
        if (!file.exists()) return userHistory;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                userHistory.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading history: " + e.getMessage());
        }
        return userHistory;
    }
}