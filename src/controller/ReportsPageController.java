package controller;

import DAO.Helper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

public class ReportsPageController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TableView<Appointment> AppointmentTableview;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointment, Integer> custIdcol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, LocalTime> endCol;
    @FXML
    private TableColumn<Appointment, LocalTime> startCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;


    @FXML
    private TableView<Customer> customerTableview;
    @FXML
    private TableColumn<?, ?> addressCol;
    @FXML
    private TableColumn<?, ?> custIdCustCol;
    @FXML
    private TableColumn<?, ?> genderCol;
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> phoneCol;
    @FXML
    private TableColumn<?, ?> postalCodeCol;

    @FXML
    private Button backToMainBtn;
    @FXML
    private Button searchContactBtn;
    @FXML
    private Button searchTypeBtn;
    @FXML
    private Button searchUserBtn;
    @FXML
    private Label typeLbl;
    @FXML
    private Label monthReportLbl;

    @FXML
    private TextField searchCustomerTxt;
    @FXML
    private TextField searchContactTxt;
    @FXML
    private Button monthSearchBtn;
    @FXML
    private TextField searchUserTxt;

    @FXML
    private TextArea reportTextArea;

    public ReportsPageController() {
    }

    @FXML
    public void clickBackToMain(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/mainScreen.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }


    @FXML
    void clickSearchAppointment(ActionEvent event) throws SQLException {
        ObservableList<Appointment> apptById=FXCollections.observableArrayList();
        int apptId= Integer.parseInt(searchContactTxt.getText());

        apptById=Helper.getApptById(apptId);
        setAllAppts(apptById);
        AppointmentTableview.setItems(apptById);

        if(apptById.isEmpty()){
            Alert alert=new Alert(AlertType.ERROR);
            alert.setTitle("This is not a valid appointment ID");
            alert.setContentText("Please choose a valid appointment ID and try again");
            alert.showAndWait();
        }


    }


    @FXML
    void clickSearchCustomerId(ActionEvent event) throws SQLException {
        ObservableList<Customer> customers=FXCollections.observableArrayList();
        int customerId=Integer.parseInt(searchCustomerTxt.getText());

        //customers.add(Helper.getCustomerByID(customerId));
        Customer customer=Helper.getCustomerByID(customerId);

        customers.add(customer);
        setAllCustomers(customers);
        customerTableview.setItems(customers);


    if (customer==null) {

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("No matching customer");
        alert.setContentText("This is not a correct customer ID. Please try a valid customer ID");
        alert.showAndWait();
    }

    }
    public void setAllAppts(ObservableList<Appointment> allAppts) {
        this.apptIdCol.setCellValueFactory(new PropertyValueFactory("appointmentId"));
        this.titleCol.setCellValueFactory(new PropertyValueFactory("title"));
        this.descriptionCol.setCellValueFactory(new PropertyValueFactory("description"));
        this.typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        this.startCol.setCellValueFactory(new PropertyValueFactory("start"));
        this.endCol.setCellValueFactory(new PropertyValueFactory("end"));
        this.custIdcol.setCellValueFactory(new PropertyValueFactory("customerId"));
        this.AppointmentTableview.setItems(allAppts);
    }

    public void setAllCustomers(ObservableList<Customer> allCustomers) {
        this.custIdCustCol.setCellValueFactory(new PropertyValueFactory("customerId"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory("customerName"));
        this.addressCol.setCellValueFactory(new PropertyValueFactory("address"));
        this.postalCodeCol.setCellValueFactory(new PropertyValueFactory("postalCode"));
        this.phoneCol.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        this.genderCol.setCellValueFactory(new PropertyValueFactory("gender"));
        this.customerTableview.setItems(allCustomers);
    }

    @FXML
    public void clickMonthSearch(ActionEvent event) throws SQLException {
        this.reportTextArea.setText(Helper.createMonthTypeReport());
    }

    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.setAllAppts(Helper.getAllAppointments());
            this.setAllCustomers(Helper.getAllCustomers());
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }
}

