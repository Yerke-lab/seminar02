package server.client;

public interface ClientView {
    void showMessage(String text);
    void disconnectFromServer();
    void serverAnswer(String text); 
}
