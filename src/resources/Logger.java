package resources;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Logger {
    private static final String filename = "login_activity.txt";

    public Logger() {
    }

    public static void recordLogin(String userName, boolean loginSuccessful) throws IOException {
        FileWriter fwriter = new FileWriter("login_activity.txt", true);
        new PrintWriter(fwriter);
        fwriter.append("Time: " + ZonedDateTime.now(ZoneOffset.UTC) + " UserName: " + userName + " sucessfully loggedin: " + loginSuccessful + "\n");
        fwriter.close();
    }
}