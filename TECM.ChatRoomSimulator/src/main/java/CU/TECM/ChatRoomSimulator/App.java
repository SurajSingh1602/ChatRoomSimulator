package CU.TECM.ChatRoomSimulator;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of Users (e.g., 3):");
        int n = sc.nextInt();

        System.out.println("Enter the simulation time (in seconds, e.g., 20):");
        int timeS = sc.nextInt();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.startSimulation(n, timeS);

        sc.close(); // Always close the scanner
        System.out.println("Application exited.");
    }
}