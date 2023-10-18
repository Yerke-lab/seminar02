package server.client;

import server.server.ServerWindow;

public class Client implements ClientView {
    private String name;
    private ClientView clientView;
    private ServerWindow server;
    private boolean connected;

    public Client(ClientView clientView, ServerWindow serverWindow) {
        this.clientView = clientView;
        this.server = serverWindow;
    }

    public boolean connectToServer(String name){
        this.name = name;
        if (server.connectUser(this)){
            printText("Добро пожаловать! Вы успешно подключились!\n");
            connected = true;
            String log = server.getHistory();
            if (log != null){
                printText(log);
            }
            return true;
        } else {
            printText("Подключение не удалось");
            return false;
        }
    }

   
    public void sendMessage(String message){
        if (connected) {
            if (!message.isEmpty()) {
                server.sendMessage(name + ": " + message);
                serverAnswer(name + ": " + message);
            }
        } else {
            printText("Нет подключения к серверу");
        }
    }
    
    public void serverAnswer(String answer){
        printText(answer);
    }

    public void disconnect(){
        if (connected) {
            connected = false;
            clientView.disconnectFromServer();
            server.disconnectUser(this);
            printText("Вы были отключены от сервера!");
        }
    }

    @Override
    public void disconnectFromServer() {
   
    }

    public String getName() {
        return name;
    }

    private void printText(String text){
        clientView.showMessage(text);
    }

    public void showMessage(String text) {
        if (connected) {
            if (!text.isEmpty()) {
                server.sendMessage(name + ": " + text);
            }
        }
    }

}
