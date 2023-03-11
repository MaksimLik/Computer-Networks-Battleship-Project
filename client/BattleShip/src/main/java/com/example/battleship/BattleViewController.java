package com.example.battleship;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class BattleViewController implements Initializable {

    @FXML
    public GridPane myBoardPane;
    @FXML
    public GridPane enemyBoardPane;

    private final Button[][] myBoardButtons = new Button[10][10];

    private final Button[][] enemyBoardButtons = new Button[10][10];
    public Label turnLabel;
    private Game game;

    private Player player;

    private Board board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameHolder holder = GameHolder.getInstance();
        game = holder.getGame();
        player = game.getPlayer();
        board = game.getBoard();

        setMyBoard();
        setEnemyBoard();
        setTurnLabel();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                enemyBoardButtons[i][j].setOnAction(actionEvent -> {
                    String id = ((Control)actionEvent.getSource()).getId();
                    String mess = id.substring(0, id.length()-2);
                    int[] f = board.getIndexOfField(mess);
                    int x = f[0];
                    int y = f[1];
                        if (player.getTurn() && board.enemyBoardGame[x][y].getState() == Constant.WATER) {
                            player.setTurn(false);
                            setTurnLabel();

                            System.out.println("click" + mess);
                            // wysylanie do serwera info o strzale
                            player.getClient().sendMessageToServer(mess, "shot");
                            System.out.println(mess);
                          //  board.enemyBoardGame[i][j].setState(Constant.);
                            board.displayBoard();
                        }
                });
            }
        }



        new Thread(() -> {
            System.out.println("oczekiwanie na strzaly");

           while (true){
               try {

                   String mess = player.getClient().readMessageFromServer();


                   if (mess.contains("disconnect")) {
                       System.out.println("rozloczono");
                       Platform.runLater(() -> {
                           Alert alert = new Alert(Alert.AlertType.INFORMATION);
                           alert.setTitle("Informacja");
                           alert.setHeaderText("Twoj przeciwnik wyszedl z gry");
                           alert.showAndWait().ifPresent(response -> Platform.exit());
                       });
                   } else if (mess.contains("win")) {
                       Platform.runLater(() -> {
                           Alert alert = new Alert(Alert.AlertType.INFORMATION);
                           alert.setTitle("Wygrana");
                           alert.setHeaderText("Gratuluje wygrales");
                           alert.showAndWait().ifPresent(response -> Platform.exit());
                       });

                   } else if (mess.contains("lose")) {
                       Platform.runLater(() -> {
                           Alert alert = new Alert(Alert.AlertType.INFORMATION);
                           alert.setTitle("Przegrana");
                           alert.setHeaderText("Przegrales !! pozdro pocwicz");
                           alert.showAndWait().ifPresent(response -> Platform.exit());
                       });
                   }

                   if (mess.contains("win") || mess.contains("lose") || mess.contains("disconnect")) break;


                   System.out.println("Mess - " + mess);
                   String[] commands = mess.split("_");
                   String command0 = Utils.clearString(commands[0]);
                   String filed = Utils.clearString(commands[1]);

                   Field f = board.getField(filed, Constant.ENEMY_BOARD);

                   System.out.println("X:- " + f.getX());
                   System.out.println("Y: - "+ f.getY());
                   System.out.println("Stan przed : " + f.getState());


                   switch (command0) {
                       case "hit" -> {
                           System.out.println("trafiono");
                           System.out.println(filed);
                           f.setState(Constant.HIT_ATTACKED_FIELD);
                           System.out.println("Stan po : " + f.getState());
                           updateUI();

                       }
                       case "miss" -> {
                           System.out.println("pudlo");
                           System.out.println(filed);
                           f.setState(Constant.MISSED_ATTACKED_FIELD);
                           updateUI();
                       }
                       case "yourmove" -> {
                           player.setTurn(true);
                           System.out.println(filed);
                           Platform.runLater(() ->  {
                               setTurnLabel();
                               int[] fi = board.getIndexOfField(filed);
                               int x = fi[0];
                               int y = fi[1];

                               if (board.boardGame[x][y].getState() == Constant.SHIP_PART) {
                                   board.boardGame[x][y].setState(Constant.DAMAGED_PART_SHIP);
                               }

                               myBoardButtons[x][y]
                                       .setStyle(
                                               "-fx-pref-width: 25px;"+
                                                       "-fx-pref-height: 25px;" +
                                                       "-fx-background-color: red;"
                                       );
                               Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                                   @Override
                                   public void handle(ActionEvent actionEvent) {
                                       updateUI();
                                   }
                               }));
                               timeline.play();
                           });
                       }

                        case "win" -> {
                            System.out.println("Wygrales");
                        }

                   }




               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }

        }).start();

    }

    private void setMyBoard() {
        final int size = 11 ;
        for (int i = 1; i < size; i++) {
            int letter = 'A';
            char label_text = (char)(letter + i-1);
            Label l_r = new Label("  " + label_text);
            Label l_c = new Label(String.valueOf(i));
            myBoardPane.add(l_r, i, 0);
            myBoardPane.add(l_c, 0, i);
        }
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                Button button = new Button();
                button.setId(String.valueOf((char) ('A' + i-1) + "" + j ));
                button.setStyle(
                        "-fx-pref-width: 25px;"+
                                "-fx-pref-height: 25px;" +
                                "-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 55% , #545498, #00d4ff);"

                );
                myBoardPane.add(button, i, j);
                myBoardButtons[j-1][i-1] = button;
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (this.game.getBoard().boardGame[i][j].getState() == Constant.SHIP_PART) {
                    myBoardButtons[i][j].setStyle(
                            "-fx-pref-width: 25px;"+
                                    "-fx-pref-height: 25px;" +
                                    "-fx-background-color: black;"
                    );
                }

            }
        }

    }
    private void setEnemyBoard() {
        final int size = 11 ;
        for (int i = 1; i < size; i++) {
            int letter = 'A';
            char label_text = (char)(letter + i-1);
            Label l_r = new Label("  " + label_text);
            Label l_c = new Label(String.valueOf(i));
            enemyBoardPane.add(l_r, i, 0);
            enemyBoardPane.add(l_c, 0, i);
        }
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                Button button = new Button();
                button.setId(String.valueOf((char) ('A' + i-1) + "" + j + "_e"));
                button.setStyle(
                        "-fx-pref-width: 25px;"+
                                "-fx-pref-height: 25px;" +
                                "-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 55% , #545498, #00d4ff);"

                );
                enemyBoardPane.add(button, i, j);
                enemyBoardButtons[j-1][i-1] = button;
            }
        }
    }

    private void setTurnLabel() {
        if (player.getTurn()) {
            turnLabel.setText("Twoj ruch");
        }
        else {
            turnLabel.setText("Ruch przeciwnika");
        }

    }

    private void updateUI() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board.boardGame[i][j].getState() == Constant.SHIP_PART) {
                    myBoardButtons[i][j].setStyle(
                            "-fx-pref-width: 25px;"+
                                    "-fx-pref-height: 25px;" +
                                    "-fx-background-color: black;"
                    );
                }

                if (board.boardGame[i][j].getState() == Constant.WATER) {
                    myBoardButtons[i][j].setStyle(
                                "-fx-pref-width: 25px;"+
                                    "-fx-pref-height: 25px;" +
                                    "-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 55% , #545498, #00d4ff);"

                    );
                }

                if (board.boardGame[i][j].getState() == Constant.DAMAGED_PART_SHIP)
                {
                    myBoardButtons[i][j].setStyle(
                    "-fx-pref-width: 25px;"+
                    "-fx-pref-height: 25px;" +
                    "-fx-background-color: brown;"
                    );
                }


                if (board.enemyBoardGame[i][j].getState() == Constant.MISSED_ATTACKED_FIELD) {
                    enemyBoardButtons[i][j].setStyle(
                    "-fx-pref-width: 25px;"+
                    "-fx-pref-height: 25px;" +
                    "-fx-background-color: white;"
                    );

                }

                if (board.enemyBoardGame[i][j].getState() == Constant.HIT_ATTACKED_FIELD) {
                    enemyBoardButtons[i][j].setStyle(
                    "-fx-pref-width: 25px;"+
                    "-fx-pref-height: 25px;" +
                    "-fx-background-color: red;"
                    );
                }

            }
        }
    }

}

