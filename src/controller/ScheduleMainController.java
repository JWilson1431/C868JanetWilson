package controller;

import DAO.Helper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AlertInterface;
import model.Appointment;
import model.Customer;

public class ScheduleMainController implements Initializable {
    Stage stage;
    Parent scene;
    AlertInterface alert1 = (s) -> {
        return "A " + s + " was not chosen, please select a " + s + " and try again";
    };
    @FXML
    private Button addApptBtn;
    @FXML
    private Button backToMainBtn;
    @FXML
    private Button deleteApptBtn;
    @FXML
    private Button updateApptBtn;
    @FXML
    private TableView<Appointment> scheduleTableView;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointment, String> contactCol;
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> endDateTimeCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> startDateTimeCol;
    @FXML
    private TableColumn<Appointment, String> titleIdCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, Integer> userIdCol;
    @FXML
    private RadioButton viewAllApptRbtn;
    @FXML
    private RadioButton viewMonthRbtn;
    @FXML
    private RadioButton viewWeekRbtn;
    @FXML
    private DatePicker chooseDatePicker;
    private static ObservableList<Customer> allAppts = FXCollections.observableArrayList();

    public ScheduleMainController() {
    }

    @FXML
    public void clickAddAppt(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/addAppointment.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @FXML
    public void clickDeleteBtn(ActionEvent event) throws SQLException, IOException {
        if(scheduleTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment not selected");
            alert.setContentText(alert1.getAlert("appointment"));
            alert.showAndWait();
        }
        else{
            //confirms the user wants to delete the appointment
            Appointment apptToDelete = scheduleTableView.getSelectionModel().getSelectedItem();
            int customerToDeleteId = apptToDelete.getAppointmentId();
            int rowsAffected = Helper.deleteAppt(customerToDeleteId);
            if(rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete this appointment, do you want to proceed?");
                Optional<ButtonType> result = alert.showAndWait();

                //upon confirmation that the user wants to delete the customer, deletes them
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    int apptId = apptToDelete.getAppointmentId();
                    String type = apptToDelete.getType();
                    allAppts.remove(apptToDelete);
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Appointment deleted");
                    alert2.setContentText("Appointment with ID: " + apptId + " and type: " + type + " was deleted.");
                    alert2.showAndWait();
                }

                //refreshes the page once customer is deleted
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/scheduleMain.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else{
                System.out.println("Delete Failed");
            }
        }

    }

    @FXML
    public void clickUpdateAppt(ActionEvent event) throws IOException, SQLException {
        if(scheduleTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment not selected");
            alert.setContentText(alert1.getAlert("appointment"));
            alert.showAndWait();
        }
        else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/updateAppointment.fxml"));
            loader.load();
            UpdateAppointmentController UCC = loader.getController();
            UCC.sendAppointment(scheduleTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    public void clickViewAllApptsRbtn(ActionEvent event) throws SQLException {
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        allAppts = Helper.getAllAppointments();
        this.scheduleTableView.setItems(allAppts);
    }

    @FXML
    public void clickViewMonthRbtn(ActionEvent event) throws SQLException {
        ObservableList<Appointment> monthAppts = FXCollections.observableArrayList();
        LocalDate startDate = (LocalDate)this.chooseDatePicker.getValue();
        LocalDate endDate = startDate.plusMonths(1L);
        monthAppts = Helper.getApptsFilteredMonthWeek(startDate, endDate);
        this.scheduleTableView.setItems(monthAppts);
    }

    public void clickViewWeekRbtn(ActionEvent event) throws SQLException {
        ObservableList<Appointment> weekAppts = FXCollections.observableArrayList();
        LocalDate startDate = (LocalDate)this.chooseDatePicker.getValue();
        LocalDate endDate = startDate.plusWeeks(1L);
        weekAppts = Helper.getApptsFilteredMonthWeek(startDate, endDate);
        this.scheduleTableView.setItems(weekAppts);
    }

    @FXML
    public void clickBackToMain(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/mainScreen.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    public void setAllAppointments(ObservableList<Appointment> listOfAppointments) {
        this.apptIdCol.setCellValueFactory(new PropertyValueFactory("appointmentId"));
        this.titleIdCol.setCellValueFactory(new PropertyValueFactory("title"));
        this.descriptionCol.setCellValueFactory(new PropertyValueFactory("description"));
        this.locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        this.contactCol.setCellValueFactory(new PropertyValueFactory("contactId"));
        this.typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        this.startDateTimeCol.setCellValueFactory(new PropertyValueFactory("start"));
        this.endDateTimeCol.setCellValueFactory(new PropertyValueFactory("end"));
        this.customerIdCol.setCellValueFactory(new PropertyValueFactory("customerId"));
        this.userIdCol.setCellValueFactory(new PropertyValueFactory("userId"));
        this.scheduleTableView.setItems(listOfAppointments);
    }

    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.setAllAppointments(Helper.getAllAppointments());
            LocalDate now = LocalDate.now();
            this.chooseDatePicker.setValue(now);
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }
}
