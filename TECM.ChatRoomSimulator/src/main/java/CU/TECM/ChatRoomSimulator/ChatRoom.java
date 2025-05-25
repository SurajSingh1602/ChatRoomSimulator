package CU.TECM.ChatRoomSimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRoom {
    MessageHandler messageHandler;
    Map<String, UserStatus> users;
    List<UserSimulator> userSimulators;

    ExecutorService executorService;
    Random random;
    AtomicInteger userCounter;

    public ChatRoom() {
        this.messageHandler = new MessageHandler();
        this.users = new ConcurrentHashMap<>();
        this.userSimulators = new ArrayList<>();
        this.random = new Random();
        this.userCounter = new AtomicInteger(0);
    }

    public UserStatus addUser(String username) {
        String userId = "user_" + userCounter.incrementAndGet();
        // Note: UserStatus constructor now sets status to false (offline)
        UserStatus newUser = new UserStatus(userId, username);
        users.put(userId, newUser);

        UserSimulator simulator = new UserSimulator(newUser, messageHandler);
        userSimulators.add(simulator);

        System.out.println("New user added: " + username + " (ID: " + userId + ")");
        return newUser;
    }

    public void startSimulation(int numberOfUsers, int simulationDurationSeconds) {
        System.out.println("\n--- Starting Chatroom Simulation ---");
        System.out.println("Preparing " + numberOfUsers + " users for the chatroom...");

        for (int i = 0; i < numberOfUsers; i++) {
            addUser("ChatUser" + (i + 1));
        }

        executorService = Executors.newFixedThreadPool(numberOfUsers);

        System.out.println("Users are now joining and simulating actions. Running for " + simulationDurationSeconds + " seconds...");
        for (UserSimulator simulator : userSimulators) {
            executorService.submit(simulator);
        }

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (long) simulationDurationSeconds * 1000;
        int statusDisplayInterval = 2000;
        long lastStatusDisplayTime = startTime;

        while (System.currentTimeMillis() < endTime) {
            try {
                messageHandler.processAndDistributeMessages();

                if (System.currentTimeMillis() - lastStatusDisplayTime >= statusDisplayInterval) {
                    displayChatStatus();
                    lastStatusDisplayTime = System.currentTimeMillis();
                }

                TimeUnit.MILLISECONDS.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("ChatRoom simulation interrupted during sleep. Stopping.");
                break;
            }
        }

        stopSimulation();
        System.out.println("\n--- Chatroom Simulation Ended ---");
    }

    public void stopSimulation() {
        System.out.println("\n--- Shutting down user simulators ---");

        for (UserSimulator simulator : userSimulators) {
            simulator.stopSimulation();
        }

        if (executorService != null) {
            executorService.shutdown();
            try {
                System.out.println("Waiting for user simulators to complete or timeout (10 seconds)...");
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Some simulators did not terminate. Forcing shutdown.");
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                        System.err.println("ExecutorService did not terminate completely.");
                    }
                }
            } catch (InterruptedException ie) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
                System.err.println("ChatRoom shutdown interrupted.");
            }
        }
        System.out.println("All simulators have been stopped.");
    }

    public void displayChatStatus() {
        System.out.println("\n--- Current Chat Status ---");
        // Correctly filter for online users only when counting AND listing
        long onlineCount = users.values().stream().filter(u -> u.getStatus()).count();
        System.out.println("Online Users (" + onlineCount + " online):");

        users.values().stream()
                .filter(u -> u.getStatus()) // Only include users who are online
                .forEach(u -> {
                    // With just boolean status, 'Online' is clear enough. No 'Typing' status with boolean.
                    System.out.println("  - " + u.getUserName() + " (Online)");
                });

        System.out.println("\nRecent Messages (Last 5):");
        List<ChatMessage> recentMessages = messageHandler.getRecentMessages(5);
        if (recentMessages.isEmpty()) {
            System.out.println("  No messages in history yet.");
        } else {
            recentMessages.forEach(msg -> System.out.println("  " + msg.toString()));
        }
        System.out.println("Total messages sent: " + messageHandler.getMessageCount());
        System.out.println("---------------------------\n");
    }
}