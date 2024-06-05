package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;

public class ScreenSender {
    public static void main(String[] args) throws IOException, AWTException {
//        String serverAddress = "localhost";
        String serverAddress = "159.203.168.51";
//        int serverPort = 12345;
        int serverPort = 8089;
        Robot robot = new Robot();

        try (Socket socket = new Socket(serverAddress, serverPort)) {

            System.out.println("Connected to server at " + serverAddress + ":" + serverPort);

            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            int i=0;

            while (true) {
                BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(screenCapture, "jpg", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                dataOutputStream.writeInt(imageBytes.length);
                dataOutputStream.write(imageBytes);
                dataOutputStream.flush();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (SocketException e) {
            System.out.println("Server is closed.");
//            e.printStackTrace();
        }
    }
}
