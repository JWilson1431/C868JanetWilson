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
import model.DisplayMessage;
import model.User;

public class AddAppointmentController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private ComboBox<Contact> contactCombo;
    @FXML
    private ComboBox<String> typeCombo;
    @FXML
    private ComboBox<LocalTime> endTimeCombo;
    @FXML
    private ComboBox<LocalTime> startTimeCombo;
    @FXML
    private ComboBox<Customer> customerIdCombo;
    @FXML
    private ComboBox<User> userIdCombo;
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private TextField locationTxt;
    @FXML
    private TextField apptIdTxt;
    @FXML
    private DatePicker startDatePicker;

    public AddAppointmentController() {
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
        DisplayMessage message = () -> {
            return "This appointment overlaps another appointment for this customer. Please choose a new time";
        };
            ObservableList<Appointment> apptByCustomer = FXCollections.observableArrayList();

            String title = titleTxt.getText();
            String description = descriptionTxt.getText();
            String location = locationTxt.getText();



        //checks for empty fields
        if(titleTxt.getText().isEmpty() || descriptionTxt.getText().isEmpty() || locationTxt.getText().isEmpty()  || typeCombo.getValue().isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty field");
            alert.setContentText("Fields cannot be empty, please enter a value");
            alert.showAndWait();

        }


        else {
            String type = typeCombo.getValue();
            LocalDate date = startDatePicker.getValue();
            LocalTime start = startTimeCombo.getValue();
            LocalTime end = endTimeCombo.getValue();
            int contactId = contactCombo.getValue().getContactId();
            int customerId1 = customerIdCombo.getValue().getCustomerId();
            int userId = userIdCombo.getValue().getUserId();

            //LocalDateTime of appointment being created
            LocalDateTime start1 = LocalDateTime.of(date, start);
            LocalDateTime end1 = LocalDateTime.of(date, end);
            apptByCustomer = Helper.filterByCustomerId(customerId1);
            boolean checkOverlap = false;
            for (Appointment appointment : apptByCustomer) {

                if (appointment.getCustomerId() == customerId1) {
                    System.out.println("reached here");
                    System.out.println(apptByCustomer);


                    //LocalDateTime of appointment in the system
                    Timestamp tsStart = appointment.getStart();
                    LocalDateTime start2 = tsStart.toLocalDateTime();
                    Timestamp tsEnd = appointment.getEnd();
                    LocalDateTime end2 = tsEnd.toLocalDateTime();

                    //checks for overlapping appointments
                    if ((start1.isAfter(start2) || start1.isEqual(start2)) && start1.isBefore(end2)) {
                        System.out.println("number 1 issue");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Overlap Error");
                        alert.setContentText(message.displayMessage());
                        alert.showAndWait();
                        checkOverlap = true;
                        break;
                    }
                    if (end1.isAfter(start2) && end1.isBefore(end2)) {
                        System.out.println("number 2 issue");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Overlap Error");
                        alert.setContentText(message.displayMessage());
                        alert.showAndWait();
                        checkOverlap = true;
                        break;
                    }
                    if ((start1.isBefore(start2) || start2.equals(start1)) && (end1.isAfter(end2) || end1.isEqual(end2))) {
                        System.out.println("number 3 issue");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Overlap Error");
                        alert.setContentText(message.displayMessage());
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
            //validates business hours and displays an alert if the appointment is not within business hours
            if (!Helper.validateBusinessHours(start1, end1, date)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Outside Business Hours");
                alert.setContentText("Please select an appointment within the business hours of 8 am and 10 pm EST");
                alert.showAndWait();

            }


            //if no overlap and within business hours, the appointment is added
            else if (checkOverlap == false && Helper.validateBusinessHours(start1, end1, date)) {
                System.out.println("start1 " + start1 + "  " + "end1 " + end1);
                int rowsAffected = Helper.addAppointment(title, description, location, type, start1, end1, customerId1, userId, contactId);

                if (rowsAffected > 0) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Appointment added");
                    alert1.setContentText("The appointment was successfully added");
                    alert1.showAndWait();

                    //takes the user back to the main scheduling page once the appointment is added
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/scheduleMain.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
                //alerts the user if the appointment was not added
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment not added");
                    alert.setContentText("An error occurred and this appointment could not be added");
                    alert.showAndWait();
                }
            }
        }

        }



        /**This is the initialize method. This method populates the combo boxes for the screen when the page is loaded.
         * @param rb
         * @param url */
        @Override
        public void initialize(URL url, ResourceBundle rb){
            try{
                apptIdTxt.setDisable(true);
                contactCombo.setItems(Helper.getAllContacts());
                typeCombo.setItems(Helper.getAllTypes());
                customerIdCombo.setItems(Helper.getAllCustomers());
                userIdCombo.setItems(Helper.getAllUsers());

                //populate times in combo boxes for start and end times
                LocalTime start = LocalTime.of(1,0);
                LocalTime end = LocalTime.of(1,30);


                while(start.isBefore(LocalTime.of(23,0))){
                    startTimeCombo.getItems().add(start);
                    start = start.plusMinutes(30);
                }
                while(end.isBefore(LocalTime.of(23,30))){
                    endTimeCombo.getItems().add(end);
                    end = end.plusMinutes(30);
                }
            } catch (SQLException e) {
                System.out.println("error");
            }
        }
    }

