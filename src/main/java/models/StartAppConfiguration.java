package models;

public class StartAppConfiguration {
    public static StartAppConfiguration Instance = new StartAppConfiguration();

    public String pathIn;
    public String pathOut;
    public boolean localStart;
}
