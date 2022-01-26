package ru.avalon.javapp.devj130;

import java.io.*;
import java.net.Socket;

public class FileSender {
    private static final String HOST = "localhost";

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Please specify the file name as the first parameter");
            System.exit(1);
        }

        System.out.println("File sender started...");
        new FileSender().run(HOST, args[0]);
        System.out.println("File sender finished.");
    }

    private void run(String ip, String fileName) {

        try(Socket sock = new Socket(ip, FileReceiver.SERVER_PORT);
            OutputStream out = sock.getOutputStream()) {
            File file = new File(fileName);
            byte[] buf = new byte[FileReceiver.BUFFER_SIZE];
            byte[] bufFileName = file.getName().getBytes();
            out.write(bufFileName);
            out.flush();
            Thread.sleep(100);

            try (FileInputStream fis = new FileInputStream(file)) {
                System.out.println("Start sending file...");
                int n;
                while (true) {
                    n = fis.read(buf);
                    if (n < 0) {
                        break;
                    }
                    out.write(buf,0,n);
                }
            }
            System.out.println("End sending file...");
        } catch (Exception e) {
            System.err.println("Error #1: " + e.getMessage());
        }
    }
}
