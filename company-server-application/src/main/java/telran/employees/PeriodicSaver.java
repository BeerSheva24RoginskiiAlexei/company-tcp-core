package telran.employees;

import java.time.LocalTime;

import telran.io.Persistable;

public class PeriodicSaver extends Thread {
    private static final int DEFAULT_SECONDS_DELAY = 30;
    private static final String DEFAULT_FILE_NAME = "employees.data";
    private final Company company;
    private static int secondsDelay = DEFAULT_SECONDS_DELAY;
    private static String fileName = DEFAULT_FILE_NAME;

    public PeriodicSaver(Company company) {
        this.company = company;
        setDaemon(true); 
    }

    public static void setDelay(int secondsDelay) {
        if (secondsDelay > 0) {
            PeriodicSaver.secondsDelay = secondsDelay;
        }
    }

    public static void setFileName(String fileName) {
        if (fileName != null && !fileName.isBlank()) {
            PeriodicSaver.fileName = fileName;
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (company instanceof Persistable persistable) {
                persistable.saveToFile(fileName);
                System.out.printf("Data saved to %s at %s%n", fileName, LocalTime.now());
            }
            try {
                sleep(secondsDelay * 1000L);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
        System.out.println("PeriodicSaver thread stopped.");
    }
}
