package com.javastart;

import java.io.IOException;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        Client client = new Client(9998);
        Client.clientStart(client);
    }
}
