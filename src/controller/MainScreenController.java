package controller;

import DAO.Helper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

public class MainScreenController implements Initializable {
    Stage stage;
    Parent scene;
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> customertableview;
    @FXML
    private TableColumn<Customer, String> customeraddresscol;
    @FXML
    private TableColumn<Customer, String> customercountrycol;
    @FXML
    private TableColumn<Customer, Integer> customeridcolumn;
    @FXML
    private TableColumn<Customer, String> customernamecolumn;
    @FXML
    private TableColumn<Customer, String> customerphonecol;
    @FXML
    private TableColumn<Customer, String> customerpostalcol;
    @FXML
    private TableColumn<Customer, Integer> firstLevelDivisionCol;
    @FXML
    private TableView<Appointment> scheduletableview;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointment, Integer> contactCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, Date> endCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, Integer> scheduleCustIdCol;
    @FXML
    private TableColumn<Appointment, Date> startCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, Integer> userIdCol;
    @FXML
    private Button exitbtn;
    @FXML
    private Button addupdatecustomerbtn;
    @FXML
    private Button addUpdateApptBtn;
    @FXML
    private Button reportsBtn;

    public MainScreenController() {
    }

    @FXML
    public void clickaddupdatecustbtn(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/customerRecords.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    public void setAllCustomers(ObservableList<Customer> listOfCustomers) {
        this.customeridcolumn.setCellValueFactory(new PropertyValueFactory("customerId"));
        this.customernamecolumn.setCellValueFactory(new PropertyValueFactory("customerName"));
        this.customeraddresscol.setCellValueFactory(new PropertyValueFactory("address"));
        this.customerpostalcol.setCellValueFactory(new PropertyValueFactory("postalCode"));
        this.customerphonecol.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        this.firstLevelDivisionCol.setCellValueFactory(new PropertyValueFactory("divisionId"));
        this.customertableview.setItems(listOfCustomers);
    }

    public void setAllAppointments(ObservableList<Appointment> listOfAppointments) {
        this.apptIdCol.setCellValueFactory(new PropertyValueFactory("appointmentId"));
        this.titleCol.setCellValueFactory(new PropertyValueFactory("title"));
        this.descriptionCol.setCellValueFactory(new PropertyValueFactory("description"));
        this.locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        this.contactCol.setCellValueFactory(new PropertyValueFactory("contactId"));
        this.typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        this.startCol.setCellValueFactory(new PropertyValueFactory("start"));
        this.endCol.setCellValueFactory(new PropertyValueFactory("end"));
        this.scheduleCustIdCol.setCellValueFactory(new PropertyValueFactory("customerId"));
        this.userIdCol.setCellValueFactory(new PropertyValueFactory("userId"));
        this.scheduletableview.setItems(listOfAppointments);
    }

    @FXML
    public void clickAddUpdateAppt(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/scheduleMain.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @FXML
    public void clickReportsBtn(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/reportsPage.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @FXML
    public void onClickExit(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit the application?", new ButtonType[0]);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.setAllCustomers(Helper.getAllCustomers());
            this.setAllAppointments(Helper.getAllAppointments());
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }
}

