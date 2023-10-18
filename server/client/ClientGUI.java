package server.client;

import server.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ClientView{
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    JTextArea log;
    JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    JPasswordField password;
    JButton btnLogin, btnSend;
    JPanel headerPanel;

    private Client client;

    public ClientGUI(ServerWindow server){
        this.client = new Client(this, server);

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setLocation(server.getX() - 500, server.getY());

        createPanel();

        setVisible(true);
    }

    private void connectToServer() {
        if (client.connectToServer(tfLogin.getText())){
            hideHeaderPanel(false);
        }
    }

     @Override
    public void showMessage(String text) {
        appendLog(text);
    }

    public void disconnectFromServer() {
        hideHeaderPanel(true);
        client.disconnect();
    }

    private void hideHeaderPanel(boolean visible){
        headerPanel.setVisible(visible);
    }

    public void sendMessage(){
        client.sendMessage(tfMessage.getText());
        tfMessage.setText("");
    }

     private void appendLog(String text){
        log.append(text + "\n");
    }

    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createHeaderPanel(){
        headerPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField("Ivan Ivanovich");
        password = new JPasswordField("123456");
        btnLogin = new JButton("login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);

        return headerPanel;
    }

    private Component createLog(){
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    sendMessage();
                }
            }
        });
        btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectFromServer();
        }
    }
       
    @Override
    public void serverAnswer(String text) {
        showMessage(text); // Выводим сообщение на клиентском интерфейсе
    }

}
