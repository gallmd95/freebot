package freebot;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        final Client client = new Client(new CommandFactory());
    }

}
