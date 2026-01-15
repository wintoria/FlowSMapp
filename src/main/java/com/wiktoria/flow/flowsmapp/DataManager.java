package com.wiktoria.flow.flowsmapp;

import java.io.*;
import java.util.*;

public class DataManager {
    private static DataManager instance;
    private List<User> users = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private User currentUser;
    private final String FILE_NAME = "database.csv";

    private DataManager() {
        loadData();
        if (users.isEmpty()) {
            users.add(new Admin("admin@app.com", "admin", "admin"));
        }
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public List<User> getUsers() { return users; }
    public List<Post> getPosts() { return posts; }
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    public void addUser(User user) {
        users.add(user);
        saveData();
    }

    public void addPost(Post post) {
        posts.add(0, post);
        saveData();
    }

    public void removePost(Post post) {
        posts.remove(post);
        saveData();
    }

    public void saveDataTrigger() {
        saveData();
    }

    public User getUserByUsername(String username) {
        for(User u : users) if(u.getUsername().equals(username)) return u;
        return null;
    }

    public Post getPostById(UUID id) {
        for(Post p : posts) if(p.getId().equals(id)) return p;
        return null;
    }

    public List<Post> getUserPosts(User user) {
        List<Post> result = new ArrayList<>();
        for(Post p : posts) {
            if(p.getAuthor().getUsername().equals(user.getUsername())) {
                result.add(p);
            }
        }
        return result;
    }

    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            // Format: USER;TYPE;email;username;pass;bio;following_list;followers_list
            for (User u : users) {
                String followingStr = String.join(",", u.getFollowing());
                String followersStr = String.join(",", u.getFollowers());

                String line = String.format("USER;%s;%s;%s;%s;%s;%s;%s",
                        u.getType(), u.getEmail(), u.getUsername(), u.getPassword(),
                        u.getProfile().getBio().replace(";", "").replace("\n", " "),
                        followingStr, followersStr);
                writer.println(line);
            }
            // POSTY
            for (Post p : posts) {
                String line = String.format("POST;%s;%s;%d;%s",
                        p.getId(), p.getAuthor().getUsername(), p.getCreatedAt().getTime(),
                        p.getTextBody().replace(";", "").replace("\n", "[nl]"));
                for(String liker : p.likes) line += ";" + liker;
                writer.println(line);
            }
            // KOMENTARZE
            for (Post p : posts) {
                for (Comment c : p.getComments()) {
                    String line = String.format("COMMENT;%s;%s;%s;%d;%s",
                            c.getId(), p.getId(), c.getAuthor().getUsername(), c.getCreatedAt().getTime(),
                            c.getTextBody().replace(";", "").replace("\n", "[nl]"));
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length < 2) continue;

                String type = parts[0];
                if (type.equals("USER")) {
                    String userType = parts[1];
                    User u = userType.equals("ADMIN")
                            ? new Admin(parts[2], parts[3], parts[4])
                            : new RegularUser(parts[2], parts[3], parts[4]);

                    if (parts.length > 5) u.getProfile().updateBio(parts[5]);

                    // Wczytaj following (indeks 6)
                    if (parts.length > 6 && !parts[6].isEmpty()) {
                        String[] arr = parts[6].split(",");
                        u.following.addAll(Arrays.asList(arr));
                    }
                    // Wczytaj followers (indeks 7)
                    if (parts.length > 7 && !parts[7].isEmpty()) {
                        String[] arr = parts[7].split(",");
                        u.followers.addAll(Arrays.asList(arr));
                    }
                    users.add(u);

                } else if (type.equals("POST")) {
                    User author = getUserByUsername(parts[2]);
                    if (author != null) {
                        Post p = new Post(UUID.fromString(parts[1]), author, parts[4].replace("[nl]", "\n"), new Date(Long.parseLong(parts[3])));
                        for (int i = 5; i < parts.length; i++) p.likes.add(parts[i]);
                        posts.add(p);
                    }
                } else if (type.equals("COMMENT")) {
                    Post parent = getPostById(UUID.fromString(parts[2]));
                    User author = getUserByUsername(parts[3]);
                    if (parent != null && author != null) {
                        Comment c = new Comment(UUID.fromString(parts[1]), author, parts[5].replace("[nl]", "\n"), new Date(Long.parseLong(parts[4])), parent.getId());
                        parent.addComment(c);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}