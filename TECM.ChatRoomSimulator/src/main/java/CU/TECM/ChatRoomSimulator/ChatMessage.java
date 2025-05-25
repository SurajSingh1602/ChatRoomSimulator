package CU.TECM.ChatRoomSimulator;

import java.time.LocalDateTime; // Import for timestamp
import java.time.format.DateTimeFormatter; // Import for formatting timestamp

public class ChatMessage {
    final String senderId;
    final String userName;
    final String content;
    final LocalDateTime timestamp; // Added timestamp

    // Make constructor public
    public ChatMessage(String senderId, String userName, String content) {
        this.content = content;
        this.senderId = senderId;
        this.userName = userName;
        this.timestamp = LocalDateTime.now(); // Initialize with current time
    }

    public String getSenderId() { return senderId; }
    public String getUserName() { return userName; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; } // Getter for timestamp

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] %s: %s", timestamp.format(formatter), userName, content);
    }
}