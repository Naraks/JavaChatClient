package ru.denko;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
//        EventQueue.invokeLater(() -> {
//            ChatFrame chatFrame = new ChatFrame();
//            chatFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            chatFrame.setVisible(true);
//        });

        MySocket mySocket = new MySocket("localhost", 18989, "Serg");
        mySocket.runSocket();
    }
}
