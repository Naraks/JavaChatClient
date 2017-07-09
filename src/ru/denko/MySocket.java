package ru.denko;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySocket {

    private final String host;
    private final int port;
    private final String clientName;
    private int closed;
    private Logger logger = Logger.getLogger("MySocketLogger");

    public MySocket(String host, int port, String clientName) {
        this.host = host;
        this.port = port;
        this.clientName = clientName;
    }

    public void runSocket() throws IOException {
        Socket socket = new Socket(InetAddress.getByName(host), port);
        logger.log(Level.INFO, "Set socket: host: " + host + " port: "
                + port + " name: " + clientName);
        //socket.setSoTimeout(5000); //timeout 5 sec
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        out.write(clientName.getBytes());
        logger.log(Level.INFO, "Sending client name to server");

        Thread readServer = new Thread(() -> {
            while (true) {
                try {
                    byte[] readBuf = new byte[1024];
                    closed = in.read(readBuf);
                    if (closed == -1) {
                        break;
                    }
                    System.out.println("Server: " + new String(readBuf));
                } catch (IOException e) {
                    System.out.println("Exception on Thread readServer");
                    e.printStackTrace();
                }
            }
        });
        readServer.start();

        Thread readConsole = new Thread(() -> {
//            byte[] buf = new byte[1024];
            char[] consoleBuf = new char[1024];
            //InputStream console = new BufferedInputStream(System.in);
            while (true) {
                if (closed == -1) {
                    break;
                }
                try {
                    Reader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    bufferedReader.read(consoleBuf);
                    out.write(new String(consoleBuf).getBytes());
                } catch (IOException e) {
                    System.out.println("Exception in Thread readConsole");
                }
            }
        });
        readConsole.start();

        //logger.log(Level.INFO, "client closed");
    }

}
