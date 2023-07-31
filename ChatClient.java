package ChatPack;
import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
    	String name = args[0];
        try (Socket socket = new Socket(args[1], 55284);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
        	writer.write(name);
            System.out.println("Connected to server. Start typing messages:");
            new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            String message;
            while ((message = inputReader.readLine()) != null) {
                writer.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
