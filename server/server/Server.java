package server.server;

import server.client.Client;

public interface Server {
    boolean start();
    boolean stop();
    boolean isRunning();
    boolean connectUser(Client client);
    void disconnectUser(Client client);    
}