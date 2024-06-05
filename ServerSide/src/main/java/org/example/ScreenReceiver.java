package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ScreenReceiver {
    public static void main(String[] args) {
        String serverAddress = "159.203.168.51"; // Replace with the server IP address
        int serverPort = 8089;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        try (Socket socket = new Socket(serverAddress, serverPort);
             InputStream inputStream = socket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {

            System.out.println("Connected to server at " + serverAddress + ":" + serverPort);

            JFrame frame = new JFrame("Screen Receiver");
            JLabel label = new JLabel();
            frame.add(label);
            frame.setSize(screenSize);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            while (true) {
                int length = dataInputStream.readInt();
                if (length > 0) {
                    byte[] imageBytes = new byte[length];
                    dataInputStream.readFully(imageBytes);
                    System.out.println("Received image of size: " + length);

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                    BufferedImage image = ImageIO.read(byteArrayInputStream);
                    byteArrayInputStream.close();

                    if (image != null) {
                        Image scaledImage = image.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        label.setIcon(imageIcon);
                        frame.repaint();
                        System.out.println("Image displayed");
                    } else {
                        System.out.println("Failed to decode image");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}