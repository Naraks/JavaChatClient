package ru.denko;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class Client implements Runnable {

    private final String clientName;
    private Socket socket;

    private Logger logger = Logger.getLogger("MySocketLogger");

    /**
     * Constructor
     * @param host address of server
     * @param port port of server
     * @param clientName name of the attached client
     */
    Client(String host, int port, String clientName) {
        this.clientName = clientName;
        try {
            this.socket = new Socket(InetAddress.getByName(host), port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.log(Level.INFO, "Set socket: host: " + host + " port: " + port + " name: " + clientName);
    }

    public void run()  {
        try {
            writeToServer(clientName); //при создании клиента отправляем серверу имя клиента
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Sending client name to server");

        readServer();
        readConsole();
        logger.log(Level.INFO, "client closed");
    }

    private void readServer() {
        new Thread(() -> {
            logger.log(Level.INFO, "Thread readServer created");
            String msg;
            while (true) {
                try {
                    msg = readFrom(socket.getInputStream());
                    System.out.println("Server: " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        System.out.println("Could not close socket");
                    }
                    break;
                }
            }
        }).start();
    }

    private void readConsole() {
       new Thread(() -> {
            logger.log(Level.INFO, "Thread readConsole created");
            String nameTo;
            String msg;
            while (true) {
                try {
                    System.out.println("Напишите имя адресата:");
                    nameTo = readFromConsole(); //читаем адресата сообщения, имя может быть null, тогда сервер рассылает сообщение всем клиентам
                    writeToServer(nameTo); //пишем серверу имя адресата
                    System.out.println("Напишите сообщение:");
                    msg = readFromConsole(); //читаем сообщение адресату
                    writeToServer(msg); //пишем сообщение
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        System.out.println("Could not close socket");
                    }
                    break;
                }
            }
        }).start();
    }

    private void writeToServer(String msg) throws IOException {
        logger.log(Level.INFO, "Sending message to server: " + msg);
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out.writeUTF(msg);
        out.flush();
    }

    private String readFrom(InputStream inputStream) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));
        return in.readUTF();
    }

    private String readFromConsole() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        if (msg.equals("EXIT")) {
            writeToServer("EXIT");
            socket.close();
        }
        return msg;
    }
}


