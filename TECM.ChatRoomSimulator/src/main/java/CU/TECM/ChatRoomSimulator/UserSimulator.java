package CU.TECM.ChatRoomSimulator;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UserSimulator implements Runnable {
    private UserStatus userStatus;
    private MessageHandler messageHandler;
    private Random random;
    private String[] SAMPLE_MESSAGES = {
            "Hello everyone!", "How's it going?", "Any plans for the weekend?",
            "This is a great chatroom!", "I agree!", "What's new?", "Just chilling.",
            "Learning Java today!", "Nice!", "Anyone seen that new movie?",
            "Good morning!", "Good evening!", "Hope you're all well."
    };
    volatile boolean running;

    public UserSimulator(UserStatus userStatus, MessageHandler messageHandler) { // Constructor should be public
        this.userStatus = userStatus;
        this.messageHandler = messageHandler;
        this.random = new Random();
        this.running = true; // Initialize to true so the loop runs
    }

    public void joinChat() {
        userStatus.goOnline();
        System.out.println(userStatus.getUserName() + " has joined the chat and is " + userStatus.getStatus() + ".");
    }

    public void sendMessage(String message) {
        if (userStatus.getStatus()) { // Check if user is online
            try {
                // Simulate typing delay 
                TimeUnit.MILLISECONDS.sleep(random.nextInt(500) + 100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("UserSimulator " + userStatus.getUserName() + " was interrupted during message sending.");
                return;
            }
            messageHandler.receiveMessage(userStatus.getUserId(), userStatus.getUserName(), message);
            // User remains online after sending
        } else {
            System.out.println(userStatus.getUserName() + " is offline and cannot send message.");
        }
    }

    public void leaveChat() {
        userStatus.goOffline();
        System.out.println(userStatus.getUserName() + " has left the chat.");
    }

    public void stopSimulation() {
        running = false;
    }

    @Override
    public void run() {
        System.out.println("DEBUG: " + userStatus.getUserName() + " simulator thread started.");
        joinChat();

        while (running) {
            try {
                int action = random.nextInt(100); // 0-99

                if (action < 70) { // 70% chance to send a message
                    String message = SAMPLE_MESSAGES[random.nextInt(SAMPLE_MESSAGES.length)];
                    System.out.println("DEBUG: " + userStatus.getUserName() + " attempting to send message.");
                    sendMessage(message);
                } else if (action < 85) { // 15% chance to briefly go offline and come back
                    System.out.println("DEBUG: " + userStatus.getUserName() + " is going offline temporarily.");
                    userStatus.goOffline();
                    TimeUnit.SECONDS.sleep(random.nextInt(3) + 1); // Offline for 1-3 seconds
                    userStatus.goOnline();
                    System.out.println("DEBUG: " + userStatus.getUserName() + " is back online.");
                } else { // 15% chance to just be online and idle
                    System.out.println("DEBUG: " + userStatus.getUserName() + " is idling online.");
                }

                // Pause for a random duration before the next action (1-5 seconds)
                TimeUnit.SECONDS.sleep(random.nextInt(5) + 1);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("UserSimulator " + userStatus.getUserName() + " thread interrupted.");
                running = false;
            }
        }
        System.out.println("DEBUG: " + userStatus.getUserName() + " simulator thread finished.");
    }
}