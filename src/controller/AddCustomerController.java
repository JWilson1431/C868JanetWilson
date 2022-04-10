package controller;

import DAO.Helper;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
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
import model.FemaleCustomer;
import model.FirstLevelDivision;
import model.MaleCustomer;

public class AddCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private Button savebtn;
    @FXML
    private Button cancelbtn;
    @FXML
    private TextField customeridtxt;
    @FXML
    private TextField customernametxt;
    @FXML
    private TextField phonetxt;
    @FXML
    private TextField postaltxt;
    @FXML
    private TextField addresstxt;
    @FXML
    private ComboBox<Country> countrycombo;
    @FXML
    private ComboBox<FirstLevelDivision> firstLevelCombo;
    @FXML
    private RadioButton femaleRbtn;
    @FXML
    private RadioButton maleRbtn;
    @FXML
    private Label mammogramlbl;
    @FXML
    private DatePicker mammogramDatePicker;

    public AddCustomerController() {
    }

    @FXML
    public void clickSavebtn(ActionEvent event) throws SQLException, IOException {
        String customerName = this.customernametxt.getText();
        String address = this.addresstxt.getText();
        String postalCode = this.postaltxt.getText();
        String phoneNum = this.phonetxt.getText();

        LocalDate localDate =mammogramDatePicker.getValue();

        FirstLevelDivision division = (FirstLevelDivision)this.firstLevelCombo.getValue();
        Country country = (Country)this.countrycombo.getValue();

        try {
            Alert alert;
            if (customernametxt.getText().isEmpty() || addresstxt.getText().isEmpty() || postaltxt.getText().isEmpty() ||
                    phonetxt.getText().isEmpty() || firstLevelCombo.getValue().toString().isEmpty() || mammogramDatePicker.getValue().toString().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Empty field error");
                alert.setContentText("Fields cannot be empty, please fill out all fields and try again");
                alert.showAndWait();
            }

               else if (this.femaleRbtn.isSelected()) {
                    int divisionId = ((FirstLevelDivision)this.firstLevelCombo.getValue()).getDivisionId();
                    String gender = "female";
                    Date date=Date.valueOf(localDate);
                    FemaleCustomer femaleCustomer = new FemaleCustomer(customerName, address, postalCode, phoneNum, divisionId, date, gender);
                    int rowsAffected1 = Helper.addCustomer(femaleCustomer);
                    if (rowsAffected1 > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Customer Added");
                        alert.setContentText("A customer was successfully added");
                        alert.showAndWait();
                        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/mainScreen.fxml"));
                        this.stage.setScene(new Scene(this.scene));
                        this.stage.show();
                    }
                } else if (this.maleRbtn.isSelected()) {

                    int divisionId = ((FirstLevelDivision)this.firstLevelCombo.getValue()).getDivisionId();
                    String gender="male";
                    Date date=Date.valueOf(localDate);
                    MaleCustomer maleCustomer = new MaleCustomer(customerName, address, postalCode, phoneNum, divisionId, date, gender);
                    int rowsAffected1 = Helper.addMaleCustomer(maleCustomer);
                    if (rowsAffected1 > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Customer Added");
                        alert.setContentText("A customer was successfully added");
                        alert.showAndWait();
                        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/mainScreen.fxml"));
                        this.stage.setScene(new Scene(this.scene));
                        this.stage.show();
                    }
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("There was a problem with adding the customer, please try again");
                    alert.showAndWait();
                }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void clickcancelbtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void chooseCountry(ActionEvent event) throws SQLException {
        firstLevelCombo.getItems().clear();
        int countryId= countrycombo.getSelectionModel().getSelectedItem().getCountryId();
        Helper.getDivision(countryId);
        firstLevelCombo.setItems(Helper.getDivision(countryId));
    }

    @FXML
    public void clickFemaleRbtn(ActionEvent event) {
        this.mammogramlbl.setText("Last Mammogram");
    }

    @FXML
    public void clickMaleRbtn(ActionEvent event) {
        this.mammogramlbl.setText("Last Prostate Check");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            countrycombo.setItems(Helper.getAllCountries());
            customeridtxt.setDisable(true);

        }
        catch(SQLException e){
            System.out.println("error");
        }
    }
}
