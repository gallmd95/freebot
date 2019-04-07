package freebot;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.json.*;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.*;

class SessionManager {
    private Set<Session> sessions;
    private final BrowserMobProxy proxyServer;
    private final Proxy seleniumProxy;
    private long startTime;
    private Session session;
    private boolean isPaused;
    private String consoleText;
    private JSONObject har;
    private final Properties props;
    private WebDriver driver;

    SessionManager(Properties props) {
        System.setProperty("webdriver.chrome.driver", props.getProperty("chromedriver"));
        try {
            sessions = (Set<Session>) new ObjectInputStream(new FileInputStream(props.getProperty("sessions"))).readObject();
        } catch (EOFException e){
            sessions = new HashSet<>();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        proxyServer = getProxyServer();
        seleniumProxy = getSeleniumProxy(proxyServer);
        isPaused = false;
        consoleText = "";
        this.props = props;
        har = null;
    }

    boolean startSession(String name, String consoleText) throws IOException {
        if (!sessions.stream().filter(s -> s.getName().equals(name)).anyMatch(s -> {
            session = s;
            try {
                final File temp = new File(s.getPath().getPath());
                if(temp.length()>0){
                    har = new JSONObject(convertStreamToString(new FileInputStream(temp)));
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return true;
        })){
            session = createSession(name);
            sessions.add(session);
        }
        ChromeOptions capabilities = new ChromeOptions();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        this.driver = new ChromeDriver(capabilities);
        proxyServer.newHar(name);
        driver.get("http://www.google.com");
        startTime = System.currentTimeMillis();
        isPaused = false;
        this.consoleText = consoleText;
        return true;
    }

    private Session createSession(String name) throws IOException {
        final File harFile = new File(props.getProperty("hars")+"/"+name.toLowerCase()+".har");
        if(!harFile.exists()){
            harFile.createNewFile();
        } else {
            System.out.println(convertStreamToString(new FileInputStream(harFile)));
        }
        return new Session(name, harFile.toURI().toURL());
    }

    void pauseSession(){
        har = concatHars(proxyServer.endHar(), har);
        isPaused = true;
        session.setElapsedTime(session.getElapsedTime() + System.currentTimeMillis()-startTime);
    }

    void unPauseSession(){
        isPaused = false;
        proxyServer.newHar(session.getName());
        startTime = System.currentTimeMillis();
    }

    void endSession() {
        session.setElapsedTime(session.getElapsedTime() + System.currentTimeMillis()-startTime);
        if (proxyServer.getHar()!=null) {
            har = concatHars(proxyServer.endHar(), har);
        }
        try {
            OutputStream outputStream = new FileOutputStream(new File(session.getPath().getPath()));
            outputStream.write(har.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveSession();
        driver.close();
        har = null;
        session = null;
        isPaused = false;
        consoleText = "";
    }

    private JSONObject concatHars(Har newHar, JSONObject oldHar){
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            newHar.writeTo(outputStream);
            final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            final String harString = convertStreamToString(inputStream);
            final JSONObject newHarJson = new JSONObject(harString);
            if(oldHar==null){
                return newHarJson;
            }
            newHarJson.getJSONObject("log").getJSONArray("entries").toList().forEach(oldHar.getJSONObject("log").getJSONArray("entries")::put);
            newHarJson.getJSONObject("log").getJSONArray("pages").toList().forEach(oldHar.getJSONObject("log").getJSONArray("pages")::put);
            return oldHar;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean saveSession(){
        try {
            FileOutputStream f = new FileOutputStream(new File(props.getProperty("sessions")));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(sessions);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Set<Session> getSessions() {
        return sessions;
    }

    private Proxy getSeleniumProxy(BrowserMobProxy proxyServer) {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxyServer);
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            seleniumProxy.setHttpProxy(hostIp + ":" + proxyServer.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + proxyServer.getPort());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return seleniumProxy;
    }

    private BrowserMobProxy getProxyServer() {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.start();
        return proxy;
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    boolean isPaused() {
        return isPaused;
    }

    String getConsoleText() {
        return consoleText;
    }

    public Session getSession() {
        return session;
    }

    boolean removeSession(String name){
        final Iterator iterator = sessions.iterator();
        while(iterator.hasNext()){
            final Session temp = (Session) iterator.next();
            if(temp.getName().equals(name)){
                new File(temp.getPath().getPath()).delete();
                iterator.remove();
            }
        }
        return saveSession();
    }
}