package com.javastart.server;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) {
        Server server = new Server(9998);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
