package com.example.battleship;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class InitGameController implements Initializable {
    @FXML
    public TextField tfPlayerName;
    @FXML
    private Button btnPlay;
    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Game game = new Game();
        btnPlay.setOnAction(actionEvent -> {
                if (!Objects.equals(tfPlayerName.getText(), "") && !game.isGameInit()) {
                    try {

                        GameHolder holder = GameHolder.getInstance();
                        holder.setGame(game);
                        game.setPlayer(new Player(tfPlayerName.getText(), new Client(new Socket("localhost", 1234))));
                        game.getPlayer().getClient().sendMessageToServer( game.getPlayer().getName(), "newplayer");
                        welcomeText.setText("Oczekiwanie na przeciwnika");
                        game.setGameInit(true);
                        new Thread(() -> {
                            final byte[] byteArr = new byte[20];
                            try {
                                int len =  game.getPlayer().getClient().bufferedReader.read(byteArr);
                                System.out.println("Odczytano wiadomosc o dlugoscie len" + len);
                                String buf = new String(byteArr,0, len, StandardCharsets.UTF_8);
                                System.out.println("tresc: " + buf);
                                if (buf.equals(Constant.RUN)) {
                                    System.out.println("Odpalam gre za 3 s");

                                    // nowe okno
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            Stage stage;
                                            Parent root;
                                            try {
                                                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game-view.fxml")));
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                                            Scene scene = new Scene(root);
                                            stage.setScene(scene);
                                            stage.show();
                                        }});
                                }
                            } catch (IOException e) { e.printStackTrace(); }
                        }).start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        });
    }

}


//     game.initGame(tfPlayerName.getText(), actionEvent);
//             System.out.println("Gra zainicializiowana");
//             welcomeText.setText("Oczekiwanie na przeciwnika");
//
//
//    public void initGame(String name, ActionEvent event) {
//        try {
//            this.gameInit = true;
//            player = new Player(name, new Client(new Socket("localhost", 1234)));
//            player.getClient().sendMessageToServer(player.getName());
//            Thread t1 = new Thread(() -> {
//                final byte[] byteArr = new byte[20];
//                while (true) {
//                    try {
//                        int len = player.getClient().bufferedReader.read(byteArr);
//                        String buf = new String(byteArr,0, len, StandardCharsets.UTF_8);
//                        if (buf.equals(Constant.RUN)) {
//                            System.out.println("Odpalam gre za 3 s");
//                            // nowe okno
//                            Platform.runLater(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Stage stage;
//                                    Parent root = null;
//                                    try {
//                                        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game-view.fxml")));
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
//
//
//
//                                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//                                    Scene scene = new Scene(root);
//                                    stage.setScene(scene);
//                                    stage.show();
//                                }
//                            });
//                        }
//                        if (len < 0) break;
//                    } catch (IOException e) { e.printStackTrace(); }
//                }
//            }
//            );
//            t1.start();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }