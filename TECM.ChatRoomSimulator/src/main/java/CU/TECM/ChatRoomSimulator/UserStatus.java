package CU.TECM.ChatRoomSimulator;

public class UserStatus {
    private final String userId; // Make final as recommended
    private final String userName; // Make final
    private boolean status; // No default initialization, handled by constructor

    // Make constructor public
    public UserStatus(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.status = false; // User starts offline by default
    }

    // ... (rest of the getters and goOnline/goOffline methods remain the same)
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public boolean getStatus() { return status; } // Using getStatus() for boolean is fine here

    public void goOnline() {
        this.status = true;
        // System.out.println(userName + " has joined the chat."); // This is now done in UserSimulator.joinChat()
    }

    public void goOffline() {
        this.status = false;
        // System.out.println(userName + " has left the chat."); // This is now done in UserSimulator.leaveChat()
    }

    @Override
    public String toString() {
        return "UserStatus [userId=" + userId + ", userName=" + userName + ", status=" + status + "]";
    }
}