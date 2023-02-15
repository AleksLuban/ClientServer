package com.javastart;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Это клиент для отправки сообщений на приложение сервера
 * Здесь есть несколько проблем, которые нужно исправить
 * <p>
 * Нужно разобраться как он работает (запустить сервер в дебаге и посмотреть сообщения)
 * <p>
 * 1) Разнести отдельно класс клиента и класс с методом main
 * 2) Обеспечетить ввод сообщений как с консоли, так и при старте приложения (String[] args)
 * 3) Не завершать клиента после отправки сообщения
 * 4) Передавать сущности, осуществлять перевод денег (Account, Bill, Payment, Adjustment)
 * 5) Сделать нормальную обработку ошибок
 * 6) **(Со зведочокй) Настроить обратную связь от сервера, что перевод успешно выполнил и тд. (подтверждение операций)
 */
public class Client {

    private DataOutputStream toServer;
    private InputStreamReader fromServer;
    private static final String stop_server = "Stop server";

    public DataOutputStream getToServer() {
        return toServer;
    }

    public InputStreamReader getFromServer() {
        return fromServer;
    }

    public Client(int port) {
        try {
            Socket socket = new Socket("localhost", port);
            toServer = new DataOutputStream(socket.getOutputStream());
            fromServer = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка создания соединения с сервером, проверьте номер порта, название адреса");
        }
    }

    private void sendNotification(Long external_id, String message, String extra_params) throws Exception {
        toServer.writeBytes(external_id + "\n" + message + "\n" + extra_params + "\n");
    }
    public static void clientStart(Client client) {
        SendMessage sendMessage = new SendMessage();
        Bill bill1 = new Bill(5000);
        Bill bill2 = new Bill(6000);
        Account alexLuban = new Account(1L, "Alex", bill1);
        Account artem = new Account(3L, "Artem", bill2);
        Scanner scanner = new Scanner(System.in);
        try {
            sendMessage.sendDep(alexLuban, 500, client);
            sendMessage.sendPay(artem, 700, client);
            sendMessage.sendMessage("Checking", client);
            System.out.println("Введите сообщение с консоли, если хотите завершить общение с клиентом наберите 'Stop server' ");
            new Thread(() -> {
                while (true) {
                    try {
                        String s = scanner.nextLine();
                        if (!stop_server.equals(s)) {
                            sendMessage.sendMessage(s, client);
                        } else {
                            sendMessage.sendMessage(s, client);
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Сообщение на сервер не было доставлено, убедитесь, что соединение установлено");
        }
    }

}
