package project;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private boolean running;
    private ConcurrentLinkedDeque<ClientHandler> clients = new ConcurrentLinkedDeque<>();
    private static final Logger LOGGER = LogManager.getLogger(EchoServer.class.getName());

    public ConcurrentLinkedDeque<ClientHandler> getClients() {
        return clients;
    }

    public EchoServer() {
        running = true;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try(ServerSocket server = new ServerSocket(8189)) {
            LOGGER.info("Server started!");
            while (running) {
                LOGGER.info("Server is waiting connection");
                Socket socket = server.accept();
                LOGGER.info("Client accepted!");

                ClientHandler handler = new ClientHandler(socket, this);
                executorService.submit(handler);
            }
        } catch (Exception e) {
            LOGGER.info("Server crashed");
        }
    }

    public void addUser(ClientHandler handler){
        clients.add(handler);
    }

    public void broadCast(String msg) throws IOException {
        if (msg.equals("")) return;
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }

    public void kickMe(ClientHandler client) {
        clients.remove(client);
    }

    public static void main(String[] args){
        new EchoServer();
    }

}
