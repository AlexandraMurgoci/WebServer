import config.Configuration;
import config.ConfigurationParser;
import executor.ExecutorServiceWrapper;
import requestprocessing.RequestParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static Configuration configuration;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        configuration = new ConfigurationParser().parse();
        serverSocket= new ServerSocket(configuration.getPort());
        ExecutorServiceWrapper executor = new ExecutorServiceWrapper(configuration.getMaxThreads());
        while (true) {
            try {
                Socket s=serverSocket.accept(); // cerere pă socket
                System.out.println("Client connected on "+s.getPort()); // --> apel metoda clasa Logger (IZA)
                RequestParser C = new RequestParser(s); // se creaza un thread ( obiect RequestParser (MONA) ) pentru fiecare cerere de resursa
                executor.submit(C); // Start Thread for request; se apeleaza metoda "run()" din clasa RequestParser
            }
            catch (IOException exception) {
                // --> apel la o metoda a clasei Logger (IZA)
                exception.printStackTrace();
            }
        }
    }
}
