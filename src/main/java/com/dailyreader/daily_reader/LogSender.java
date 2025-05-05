package com.dailyreader.daily_reader;

import java.io.OutputStream;
import java.net.Socket;

public class LogSender {

    public static void main(String[] args) {
        String log = "{\"timestamp\": \"" + System.currentTimeMillis() + "\", \"level\": \"INFO\", \"message\": \"Manual log test from Java\"}";

        try (Socket socket = new Socket("localhost", 5000)) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(log.getBytes());
            outputStream.flush();
            System.out.println("Log sent successfully!");
        } catch (Exception e) {
            System.err.println("Error sending log: " + e.getMessage());
        }
    }

}
