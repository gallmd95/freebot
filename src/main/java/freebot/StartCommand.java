package freebot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;

public class StartCommand implements Command {
    @Override
    public void run(String[] inputs, JTextArea ta) {
        if(inputs.length > 1){
            ta.append("\nStarted new session: " + inputs[1]  + "\n>");
            final WebDriver driver = new ChromeDriver();
            driver.get("http://www.google.com");
        } else {
            ta.append("\nMissing session name. Try START <NAME>\n>");
        }
    }

    @Override
    public String description() {
        return "START <NAME>: starts a new session with specified name";
    }
}
