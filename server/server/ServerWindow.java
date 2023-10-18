package server.server;

import server.client.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame implements Server {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final String LOG_PATH = "C:/Users/HOME/Documents/ИТ РАЗРАБОТКА ОБУЧЕНИЕ/Java Development Kit/jdk_server_lesson2-master/src/server/log.txt";

    List<Client> clientList;

    JButton btnStart, btnStop;
    JTextArea log;
    boolean work;

    public ServerWindow() {
        clientList = new ArrayList<>();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setLocationRelativeTo(null);

        createPanel();
        setVisible(true);
    }

    public boolean connectUser(Client client) {
        if (!work) {
            return false;
        }
        clientList.add(client);
        String connectMessage = "Клиент " + client.getName() + " успешно подключился.";
        answerAll(connectMessage);
        return true;
    }

    public String getHistory() {
        return readLog();
    }

    @Override
    public void disconnectUser(Client client) {
        clientList.remove(client);
        if (client != null) {
            client.disconnectFromServer();
        }
    }

    public void sendMessage(String text) {
        if (!work) {
            return;
        }
        String timestamp = getTimestamp();
        String messageWithTimestamp = timestamp + " " + text;

        appendLog(messageWithTimestamp);
        answerAll(messageWithTimestamp);
        saveInLog(messageWithTimestamp);
    }

    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private void answerAll(String text) {
        for (Client client : clientList) {
            client.serverAnswer(text);
        }
    }

    private void saveInLog(String text) {
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(text);
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLog() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH);) {
            int c;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void appendLog(String text) {
        log.append(text + "\n");
    }

    private void createPanel() {
        log = new JTextArea();
        add(log);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (work) {
                    appendLog("Сервер уже был запущен");
                } else {
                    work = true;
                    appendLog("Сервер запущен!");
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!work) {
                    appendLog("Сервер уже был остановлен");
                } else {
                    work = false;
                    for (Client client : clientList) {
                        disconnectUser(client);
                    }
                    appendLog("Сервер остановлен!");
                }
            }
        });

        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }

    @Override
    public boolean start() {
        if (work) {
            appendLog("Сервер уже был запущен");
            return false;
        } else {
            work = true;
            appendLog("Сервер запущен!");
            return true;
        }
    }

    @Override
    public boolean stop() {
        if (!work) {
            appendLog("Сервер уже был остановлен");
            return false;
        } else {
            work = false;
            for (Client client : clientList) {
                disconnectUser(client);
            }
            appendLog("Сервер остановлен!");
            return true;
        }
    }

    @Override
    public boolean isRunning() {
        return work;
    }



}
