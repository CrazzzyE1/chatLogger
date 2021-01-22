package project;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class ClientHandler implements Runnable, Closeable {
    private final DbController dbController;
    private static int cnt = 0;
    private String userName;
    private String login;
    private final DataInputStream is;
    private final DataOutputStream os;
    private final byte[] buffer;
    private final EchoServer server;
    private CommandController commandController;
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class.getName());

    public ClientHandler(Socket socket, EchoServer server) throws IOException, SQLException, ClassNotFoundException {
        this.dbController = new DbController();
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        cnt++;
        userName = "not_authorized#" + cnt;
        buffer = new byte[256];
        this.server = server;
        commandController = new CommandController(dbController);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int bytesRead = is.read(buffer);
                if (bytesRead == -1) {
                    server.kickMe(this);
                    server.broadCast("Client " + userName + " leave!" + "\n\r");
                    LOGGER.info("SERVER: CLIENT LEAVE");
                    break;
                }
                String messageFromClient = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                if (messageFromClient.replaceAll("[\n\r]", "").isEmpty()) {
                    continue;
                }
                if (messageFromClient.startsWith("/")) {
                    LOGGER.info("CLIENT SEND COMMAND: " + messageFromClient);
                    if (!messageFromClient.startsWith("/private")) {
                        server.broadCast(commandController.giveAnswer(messageFromClient, this, server));
                    } else {
                        commandController.giveAnswer(messageFromClient, this, server);
                    }
                    continue;
                }
                LOGGER.info("Received from " + userName + ": " + messageFromClient);
                server.broadCast(userName + ": " + messageFromClient + "\n\r");
            } catch (IOException | SQLException e) {
                LOGGER.info("Exception while read");
                break;
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void changeName(String userName) {
        this.userName = userName;
    }

    public void sendMessage(String message) throws IOException {
        os.write(message.getBytes(StandardCharsets.UTF_8));
        os.flush();
    }

    @Override
    public void close() throws IOException {
        os.close();
        is.close();
    }
}