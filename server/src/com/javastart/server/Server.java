package com.javastart.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Класс сервера
 * Нужно понять как он в связке работает с клиентом
 * Так же отрефакторить, отделить от метода main
 * <p>
 * 1) Нужно добавить базу данных в памяти (в виде Singleton)
 * 2) Нужно уметь принимать сущности от клиента, обрабатывать их и сохранять в базу
 * 3) Нужно дописать обработку ошибок
 * 4) Добавить логирования важных действий
 * <p>
 * 5) **(со зведочкой) Научить сервер отвечать клиенту об успешных операциях
 */
public class Server {

    private ServerSocket serverSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private int port;
    private static final String stop_server = "Stop server";

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        createConnection(port);
        Socket clientSocket = serverSocket.accept();
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        startListener();
    }

    private void readCommand() {

        try {
            String data = reader.readLine();
            if (data.startsWith("{\"id\"")) {
                Account account1 = readAccount(data);
                System.out.println(account1.toString());
                writer.write("Аккаунт " + account1.getName() + " был успешно отправлен на сервер." + "\n");

                if (reader.ready()) {
                    data = reader.readLine();
                    int a = Integer.parseInt(data);
                    if ((a > 0) && (data != null)) {
                        depAccount(account1, a);
                        writer.write("На аккаунт " + account1.getName() + " были успешно зачислены деньги.");
                        writer.flush();
                    } else if ((a < 0) && (data != null) && ((account1.getBill().getAmount() + a) > 0)) {
                        payAccount(account1, a);
                        writer.write("На аккаунте " + account1.getName() + " была успешно произведена оплата.");
                        writer.flush();

                    } else {
                        System.out.println("Недостаточно средств");
                        writer.write("На аккаунте " + account1.getName() + " операция не выполнена");
                        writer.flush();
                    }
                }
                writer.flush();
            } else {
                if (!stop_server.equals(data)) {
                    System.out.println("Клиент передал сообщение: " + data);
                    writer.write("Cообщение успешно доставлено на сервер");
                    writer.flush();
                } else {
                    serverSocket.close();
                    reader.close();
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't parse task, got message");
        }
    }

    private void startListener() {
        new Thread(() -> {
            while (true) {
                try {
                    if (reader.ready()) {
                        readCommand();
                    }
                    if (serverSocket.isClosed())
                        break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void createConnection(int port) {
        try {
            serverSocket = new ServerSocket(port, 10000);
            System.out.println("Server starts");
        } catch (IOException e) {
            System.out.println("Can't create connection");
            e.printStackTrace();
        }
    }

    private Account readAccount(String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringReader stringReader = new StringReader(data);
        Account account = mapper.readValue(stringReader, Account.class);
        System.out.println("Client sent account " + account.getName());
        DataBase dataBase = DataBase.getINSTANCE();
        if (dataBase.checkAccount(account)) {
            dataBase.addAccount(account);
        }
        return account;
    }

    private static void depAccount(Account account, int a) {
        DataBase dataBase = DataBase.getINSTANCE();
        if (dataBase.checkAccount(account)) {
            dataBase.addAccount(account);
        }
        for (int i = 0; i < dataBase.getDatabaseAccount().size(); i++) {
            if (account.getId() == dataBase.getDatabaseAccount().get(i).getId()) {
                dataBase.getDatabaseAccount().get(i).getBill().setAmount(dataBase.getDatabaseAccount().get(i).getBill().getAmount() + a);
                System.out.println(dataBase.getDatabaseAccount().get(i).toString());
            }
        }
        System.out.println("Client " + account.getName() + " sent deposit");
    }

    private static void payAccount(Account account, int a) {
        DataBase dataBase = DataBase.getINSTANCE();
        if (dataBase.checkAccount(account)) {
            dataBase.addAccount(account);
        }
        for (int i = 0; i < dataBase.getDatabaseAccount().size(); i++) {
            if (account.getId() == dataBase.getDatabaseAccount().get(i).getId()) {
                dataBase.getDatabaseAccount().get(i).getBill().setAmount(dataBase.getDatabaseAccount().get(i).getBill().getAmount() + a);
                System.out.println(dataBase.getDatabaseAccount().get(i).toString());
            }
        }
        System.out.println("Client " + account.getName() + " sent pay");
    }
}
