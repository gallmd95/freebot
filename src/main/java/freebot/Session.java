package freebot;

import java.io.Serializable;
import java.net.URL;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Session implements Serializable {
    private final String name;
    private final URL harPath;
    private long elapsedTime;

    public Session(String name, URL harPath) {
        this.name = name;
        this.harPath = harPath;
        elapsedTime = 0;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public URL getPath() {
        return harPath;
    }

    public String getName() {
        return name;
    }
}
