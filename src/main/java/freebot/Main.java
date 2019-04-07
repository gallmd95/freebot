package freebot;

import java.io.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        final Properties props = new Properties();
        try {
            final InputStream inputStream = new FileInputStream("freebot.properties");
            props.load(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        final SessionManager sessionManager = new SessionManager(props);
        final Client client = new Client(new CommandFactory(sessionManager));

    }
}
