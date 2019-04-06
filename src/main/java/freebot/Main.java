package freebot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        //final WebDriver driver = new ChromeDriver();
        //driver.get("http://www.google.com");
        final Client client = new Client();


    }

}
