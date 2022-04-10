package controller;

import DAO.Helper;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FemaleCustomer;
import model.FirstLevelDivision;
import model.MaleCustomer;

public class UpdateCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TextField addresstxt;
    @FXML
    private Button cancelbtn;
    @FXML
    private ComboBox<Country> countrycombo;
    @FXML
    private TextField custnametxt;
    @FXML
    private TextField customeridtxt;
    @FXML
    private ComboBox<FirstLevelDivision> firstlevelcombo;
    @FXML
    private TextField phonetxt;
    @FXML
    private TextField postaltxt;
    @FXML
    private Button savebtn;
    @FXML
    private RadioButton femaleRbtn;
    @FXML
    private RadioButton maleRbtn;
    @FXML
    private DatePicker mammogramDatePicker;
    @FXML
    private Label mammogramLbl;

    public UpdateCustomerController() {
    }

    @FXML
    public void ClickCancel(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/mainScreen.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @FXML
    public void clickSave(ActionEvent event) throws SQLException, IOException {
        String customerName = custnametxt.getText();
        String address = addresstxt.getText();
        String postalCode = postaltxt.getText();
        String phoneNum = phonetxt.getText();
        int customerId = Integer.parseInt(customeridtxt.getText());
        int divId = firstlevelcombo.getValue().getDivisionId();

        int rowsAffected = Helper.update(customerName,address,postalCode,phoneNum,divId,customerId);

        if(rowsAffected > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Updated");
            alert.setContentText("The customer was successfully updated");
            alert.showAndWait();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("There was a problem with updating the customer, please try again");
            alert.showAndWait();
        }

    }


    @FXML
    void clickFemaleRbtn(ActionEvent event) {
        this.mammogramLbl.setText("Last Mammogram Date");
    }

    @FXML
    void clickMaleRbtn(ActionEvent event) {
        this.mammogramLbl.setText("Last Prostate Check Date");
    }

    public void sendCustomer(Customer customer1) throws SQLException {
        this.customeridtxt.setText(String.valueOf(customer1.getCustomerId()));
        this.custnametxt.setText(customer1.getCustomerName());
        this.addresstxt.setText(String.valueOf(customer1.getAddress()));
        this.postaltxt.setText(String.valueOf(customer1.getPostalCode()));
        this.phonetxt.setText(String.valueOf(customer1.getPhoneNumber()));
        this.countrycombo.setItems(Helper.getAllCountries());
        FirstLevelDivision division = Helper.getDivisionById(customer1.getDivisionId());
        this.firstlevelcombo.setItems(Helper.getDivision(division.getCountryId()));
        this.firstlevelcombo.setValue(division);
        this.countrycombo.setValue(Helper.getCountryById(division.getCountryId()));
        Date date;

        LocalDate localDate;
        try {
            if (customer1 instanceof FemaleCustomer) {

                femaleRbtn.setSelected(true);
                date = ((FemaleCustomer) customer1).getLastMammogram();


            } else {
                maleRbtn.setSelected(true);
                mammogramLbl.setText("Last Prostate Exam Date");
                date = ((MaleCustomer) customer1).getLastProstateExam();
            }
            localDate=date.toLocalDate();
            mammogramDatePicker.setValue(localDate);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void chooseCountry(ActionEvent event) throws SQLException {
        this.firstlevelcombo.getItems().clear();
        int countryId = ((Country)this.countrycombo.getSelectionModel().getSelectedItem()).getCountryId();
        Helper.getDivision(countryId);
        this.firstlevelcombo.setItems(Helper.getDivision(countryId));
    }

    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.countrycombo.setItems(Helper.getAllCountries());
            this.customeridtxt.setDisable(true);
        } catch (SQLException var4) {
            System.out.println("error");
        }

    }
}

