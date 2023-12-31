package main;
/**
 * @author Darren Woods
 */

import database.DBConnect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**Class used to start/create/open the program.*/
public class Main extends Application {
    /**
     * Opens the starting point of the program, the login page.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        primaryStage.setTitle("Client Scheduler");
        primaryStage.setScene(new Scene(root, 400, 375));
        primaryStage.show();
    }

    /**
     * Launches program.
     * @param args
     */
    public static void main(String[] args) {

        DBConnect.openConnection();
        launch(args);
        DBConnect.closeConnection();
    }
}
