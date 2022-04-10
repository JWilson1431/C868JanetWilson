package sample;

import DAO.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/logInForm.fxml"));
        primaryStage.setTitle("Telemedicine Unlimited Scheduling");
        primaryStage.setScene(new Scene(root, 600.0D, 400.0D));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        DBConnection.openConnection();
        ResourceBundle rb = ResourceBundle.getBundle("resources/Language", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            System.out.println(rb.getString("loginbtn"));
        }

        launch(args);
        DBConnection.closeConnection();
    }
}
