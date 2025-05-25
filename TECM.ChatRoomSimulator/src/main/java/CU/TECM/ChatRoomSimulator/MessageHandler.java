package CU.TECM.ChatRoomSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageHandler {
    private final List<ChatMessage> messageHistory;
    private final ConcurrentLinkedQueue<ChatMessage> incomingMessages;

    public MessageHandler() {
        this.messageHistory = Collections.synchronizedList(new ArrayList<>());
        this.incomingMessages = new ConcurrentLinkedQueue<>();
    }

    public void receiveMessage(String senderId, String senderUsername, String content) { // senderUsername is correct here
        ChatMessage message = new ChatMessage(senderId, senderUsername, content); // Will use ChatMessage's constructor
        incomingMessages.offer(message);
        System.out.println(senderUsername + " is drafting: \"" + content + "\"");
    }

    public void processAndDistributeMessages() {
        while (!incomingMessages.isEmpty()) {
            ChatMessage message = incomingMessages.poll();
            if (message != null) {
                messageHistory.add(message);
                System.out.println("CHAT BROADCAST: " + message.toString());
            }
        }
    }

    public List<ChatMessage> getRecentMessages(int count) {
        synchronized (messageHistory) {
            int historySize = messageHistory.size();
            if (count >= historySize) {
                return new ArrayList<>(messageHistory);
            } else {
                return new ArrayList<>(messageHistory.subList(historySize - count, historySize));
            }
        }
    }

    public void clearHistory() {
        synchronized (messageHistory) {
            messageHistory.clear();
            System.out.println("Message history cleared.");
        }
    }

    public int getMessageCount() {
        return messageHistory.size();
    }
}