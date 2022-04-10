package controller;

import DAO.Helper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Appointment;
import resources.Logger;

import static DAO.Helper.checkCredentials;

public class LogInFormController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private Label enterPasswordlbl;
    @FXML
    private Label enterUserIdlbl;
    @FXML
    private Label zonelbl;
    @FXML
    private Label pleaseloginlbl;
    @FXML
    private Label schedulingsoftwarelbl;
    @FXML
    private Button loginbtn;
    @FXML
    private Button exitbtn;
    @FXML
    private PasswordField passwordtxt;
    @FXML
    private TextField usernametxt;

    public LogInFormController() {
    }

    @FXML
    public void clickLogIn(ActionEvent event) throws SQLException, IOException {
        String userName = usernametxt.getText();
        String password = passwordtxt.getText();

        boolean correctLogIn = checkCredentials(userName, password);
        //record log in attempt in log in file
        Logger.recordLogin(userName,correctLogIn);

        if (userName.isEmpty() || password.isEmpty()) {
            Locale userLocale = Locale.getDefault();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/language");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("emptyfield1"));
            alert.setContentText(resourceBundle.getString("emptyField"));
            alert.showAndWait();
        }

        else if (correctLogIn) {
            int userId = Helper.getCurrentUser().getUserId();
            System.out.println("made it");
            System.out.println(Helper.getApptsIn15(userId));
            if (Helper.getApptsIn15(userId).isEmpty()) {
                Locale userLocale = Locale.getDefault();
                ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/language");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resourceBundle.getString("noUpcoming"));
                alert.setContentText(resourceBundle.getString("successNoAppt"));
                alert.showAndWait();

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                ObservableList<Appointment> appointmentsSoon = FXCollections.observableArrayList();
                appointmentsSoon = Helper.getApptsIn15(userId);
                for(Appointment upcomingAppointment : appointmentsSoon ) {
                    int userApptID = upcomingAppointment.getAppointmentId();
                    Timestamp userTimestamp = upcomingAppointment.getStart();

                    Locale userLocale = Locale.getDefault();
                    ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/language");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(resourceBundle.getString("successTitle"));
                    alert.setContentText(resourceBundle.getString("successlogin")  + userApptID + " " + resourceBundle.getString("time") + " " + userTimestamp);
                    alert.showAndWait();

                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }

        else{
            Locale userLocale = Locale.getDefault();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/language");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("emptyfield1"));
            alert.setContentText(resourceBundle.getString("invalidfield"));
            alert.showAndWait();
        }
    }

    @FXML
    public void clickExit(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit the application?", new ButtonType[0]);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    public void initialize(URL location, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("resources/Language", Locale.getDefault());
        this.enterUserIdlbl.setText(rb.getString("enterUserIdlbl"));
        this.enterPasswordlbl.setText(rb.getString("enterPasswordlbl"));
        this.pleaseloginlbl.setText(rb.getString("pleaseloginlbl"));
        this.schedulingsoftwarelbl.setText(rb.getString("schedulingsoftwarelbl"));
        this.loginbtn.setText(rb.getString("loginbtn"));
        this.exitbtn.setText(rb.getString("exitbtn"));

        this.zonelbl.setText(ZoneId.systemDefault().toString());
    }
}

