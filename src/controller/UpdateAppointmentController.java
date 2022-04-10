package controller;

import DAO.Helper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

public class UpdateAppointmentController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private ComboBox<String> TypeCombo;
    @FXML
    private ComboBox<Contact> contactCombo;
    @FXML
    private ComboBox<LocalTime> endTimeCombo;
    @FXML
    private ComboBox<LocalTime> startTimeCombo;
    @FXML
    private ComboBox<Customer> custIdCombo;
    @FXML
    private ComboBox<User> userIdCombo;
    @FXML
    private TextField apptIdTxt;
    @FXML
    private TextField custIdTxt;
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField userIdTxt;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private TextField locationTxt;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private DatePicker datePicker;

    public UpdateAppointmentController() {
    }

    @FXML
    public void clickCancel(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/scheduleMain.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @FXML
    public void clickSave(ActionEvent event) throws SQLException, IOException {
        ObservableList<Appointment> apptByCustomer = FXCollections.observableArrayList();

        String title = titleTxt.getText();
        String description = descriptionTxt.getText();
        String location = locationTxt.getText();
        String type = TypeCombo.getValue();
        LocalDate date = datePicker.getValue();
        LocalTime start = startTimeCombo.getValue();
        LocalTime end = endTimeCombo.getValue();
        int customerId = custIdCombo.getValue().getCustomerId();
        String userName = userIdCombo.getValue().getUserName();
        int contactId = contactCombo.getValue().getContactId();
        int apptId = Integer.parseInt(apptIdTxt.getText());

        apptByCustomer = Helper.filterByCustomerId(customerId);


        LocalDateTime start1 = LocalDateTime.of(date, start);
        LocalDateTime end1 = LocalDateTime.of(date, end);

        if (titleTxt.getText().isEmpty() || descriptionTxt.getText().isEmpty() || locationTxt.getText().isEmpty() || TypeCombo.getValue().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty field");
            alert.setContentText("Fields cannot be empty, please enter a value");
            alert.showAndWait();
        }

        boolean checkOverlap = false;
        for (Appointment appointment : apptByCustomer) {

            if (appointment.getAppointmentId() != apptId && appointment.getCustomerId() == customerId) {
                System.out.println("reached here");
                System.out.println(apptByCustomer);


                //LocalDateTime of appointment in the system
                Timestamp tsStart = appointment.getStart();
                LocalDateTime start2 = tsStart.toLocalDateTime();
                Timestamp tsEnd = appointment.getEnd();
                LocalDateTime end2 = tsEnd.toLocalDateTime();


                if ((start1.isAfter(start2) || start1.isEqual(start2)) && start1.isBefore(end2)) {
                    System.out.println("number 1 issue");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Overlap Error");
                    alert.setContentText("This appointment overlaps another appointment for this customer. Please choose a new time");
                    alert.showAndWait();
                    checkOverlap = true;
                    break;
                }
                if (end1.isAfter(start2) && end1.isBefore(end2)) {
                    System.out.println("number 2 issue");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Overlap Error");
                    alert.setContentText("This appointment overlaps another appointment for this customer. Please choose a new time");
                    alert.showAndWait();
                    checkOverlap = true;
                    break;
                }
                if ((start1.isBefore(start2) || start2.equals(start1)) && (end1.isAfter(end2) || end1.isEqual(end2))) {
                    System.out.println("number 3 issue");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Overlap Error");
                    alert.setContentText("This appointment overlaps another appointment for this customer. Please choose a new time");
                    alert.showAndWait();
                    checkOverlap = true;
                    break;
                }
                if (start == end) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Appointment start and end time cannot be the same. Please select a different time");
                    alert.showAndWait();
                    checkOverlap = true;
                    break;
                }
                if (start.isAfter(end)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Start time cannot come after end time. Please select a different time");
                    alert.showAndWait();
                    checkOverlap = true;
                    break;
                }

            }
        }
        if(!Helper.validateBusinessHours(start1,end1,date)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Outside Business Hours");
            alert.setContentText("Please select an appointment within the business hours of 8 am and 10 pm EST");
            alert.showAndWait();

        }
        else if (checkOverlap == false) {
            int userId = Helper.getUserId(userName);
            int rowsAffected = Helper.updateAppt(title, description, location, type, start1, end1, customerId, userId, contactId, apptId);

            if (rowsAffected > 0) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Appointment updated");
                alert1.setContentText("The appointment was successfully updated");
                alert1.showAndWait();


                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/scheduleMain.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment not added");
                alert.setContentText("An error occurred and this appointment could not be added");
                alert.showAndWait();
            }
        }

    }


    public void sendAppointment(Appointment appt1) throws SQLException {
        LocalTime start = LocalTime.of(1, 0);
        LocalTime end = LocalTime.of(1, 30);

        while (start.isBefore(LocalTime.of(23, 0))) {
            startTimeCombo.getItems().add(start);
            start = start.plusMinutes(30);
        }
        while (end.isBefore(LocalTime.of(23, 30))) {
            endTimeCombo.getItems().add(end);
            end = end.plusMinutes(30);
        }

        apptIdTxt.setText(String.valueOf(appt1.getAppointmentId()));
        titleTxt.setText(appt1.getTitle());
        descriptionTxt.setText(appt1.getDescription());
        locationTxt.setText(appt1.getLocation());
        TypeCombo.setItems(Helper.getAllTypes());
        TypeCombo.getSelectionModel().select(appt1.getType());
        custIdCombo.setItems(Helper.getAllCustomers());
        Customer customer = Helper.getCustomerByID(appt1.getCustomerId());
        custIdCombo.setValue(customer);
        userIdCombo.setItems(Helper.getAllUsers());
        User user = Helper.getUserById(appt1.getUserId());
        userIdCombo.setValue(user);

        //populate the start time combo box with the start time from the selected appt
        LocalDateTime start1 = appt1.getStart().toLocalDateTime();
        LocalTime startTime = start1.toLocalTime();
        startTimeCombo.getSelectionModel().select(startTime);

        //populate the endtime combo box with the end time from the selected appt
        LocalDateTime end1 = appt1.getEnd().toLocalDateTime();
        LocalTime endTime = end1.toLocalTime();
        endTimeCombo.getSelectionModel().select(endTime);

        //populate the datepicker with the date of the selected appt
        LocalDate date = appt1.getStart().toLocalDateTime().toLocalDate();
        datePicker.setValue(date);


        contactCombo.setItems(Helper.getAllContacts());
        Contact contact = Helper.getContactById(appt1.getContactId());
        contactCombo.setValue(contact);

        // startTimeCombo.getSelectionModel().select(appt1.getStart());

    }

    public void initialize(URL url, ResourceBundle rb) {
        this.apptIdTxt.setDisable(true);
    }
}
