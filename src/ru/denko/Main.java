package ru.denko;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        MySocket mySocket = new MySocket("localhost", 18989, "Serg");
        mySocket.runSocket();
    }
}
