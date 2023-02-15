package com.javastart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

public class SendMessage {
    private StringWriter stringWriter;
    private ObjectMapper objectMapper;

    public void sendMessage(String message, Client client) throws IOException {
        client.getToServer().writeBytes(message + "\n");
        getAnswer(client);
    }

    public void sendAccount(Account account, Client client) throws Exception {
        stringWriter = new StringWriter();
        objectMapper = new ObjectMapper();
        objectMapper.writeValue(stringWriter, account);
        client.getToServer().writeBytes(stringWriter.toString() + "\n");
        getAnswer(client);
    }

    public void sendDep(Account account, int dep, Client client) throws Exception {
        stringWriter = new StringWriter();
        objectMapper = new ObjectMapper();
        objectMapper.writeValue(stringWriter, account);
        String s = String.valueOf(dep);
        client.getToServer().writeBytes(stringWriter.toString() + "\n" + s + "\n");
        getAnswer(client);
    }

    public void sendPay(Account account, int pay, Client client) throws Exception {
        stringWriter = new StringWriter();
        objectMapper = new ObjectMapper();
        pay = pay * (-1);
        String s = String.valueOf(pay);
        objectMapper.writeValue(stringWriter, account);
        client.getToServer().writeBytes(stringWriter.toString() + "\n" + s + "\n");
        getAnswer(client);
    }

    private static void getAnswer(Client client) {
        char[] chars = new char[100];
        try {
            client.getFromServer().read(chars);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = new String(chars);
        s = s.replaceAll("[^A-Za-zА-Яа-я0-9,.]", " ");
        System.out.println(s);
    }
}
