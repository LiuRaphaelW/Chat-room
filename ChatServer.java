package ChatPack;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	private BufferedReader reader;
    private List<ClientList> clients = new ArrayList<>();
    
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start(55284);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server is ready");
            String name;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                name = reader.readLine();
                System.out.println(name+" connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientList ClientList = new ClientList(clientSocket, this, name);
                clients.add(ClientList);
                ClientList.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, String temp) {
        for (ClientList client : clients) {
        	if(!temp.equals(client.name)) {
        		client.sendMessage(message);	
        	}
        }
    }

}

class ClientList extends Thread {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ChatServer server;
    public String name;

    public ClientList(Socket socket, ChatServer server, String name) throws IOException {
        clientSocket = socket;
        this.server = server;
        this.name = name;
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(name+": " + message);
                server.broadcastMessage(name+" : " + message, name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}


