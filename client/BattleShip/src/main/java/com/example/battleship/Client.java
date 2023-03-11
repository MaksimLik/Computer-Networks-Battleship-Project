package com.example.battleship;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class Client {
    Socket socket;
    InputStream bufferedReader;
     OutputStream bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = socket.getInputStream();
            this.bufferedWriter = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void sendMessageToServer(String message, String command) {
      //  command = command + " ";
        String finalMessage = command  + " " + message;
        new Thread(() -> {
            String messSize = String.valueOf(finalMessage.length());
            messSize = String.format("%03d", Integer.parseInt(messSize));

            byte[] messageBytes = finalMessage.getBytes();
            byte[] sizeBytes = messSize.getBytes();


            ByteBuffer buffer = ByteBuffer.allocate(200);
            buffer.put(sizeBytes);
            buffer.put(messageBytes);
            try {
                bufferedWriter.write(buffer.array());
                System.out.println("Message sent");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String readMessageFromServer() throws IOException{

        byte[] byteArr = new byte[20];
        int len = this.bufferedReader.read(byteArr);
        String buf = new String(byteArr,0, len, StandardCharsets.UTF_8);
        return buf;
    }

//    public String readMessageFromServer() throws IOException{
//
//        // metoda do naprawy
//        byte[] byteArr = new byte[20];
//        int bytesRead = 0;
//        int totalBytesRead = 0;
//        while (true) {
//            bytesRead = bufferedReader.read(byteArr, totalBytesRead, 5);
//            if(bytesRead <= 0 ) break;
//            totalBytesRead += bytesRead;
//        }
//
//        return new String(byteArr,0, totalBytesRead, StandardCharsets.UTF_8);
//    }



    private void closeAll(Socket socket, InputStream bufferedReader, OutputStream bufferedWriter) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
