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
import model.AlertInterface;
import model.Customer;
import model.FemaleCustomer;
import model.MaleCustomer;

public class CustomerRecordsController implements Initializable {
    Stage stage;
    Parent scene;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    AlertInterface alert1 = (s) -> {
        return "A " + s + " was not chosen, please select a " + s + " and try again";
    };
    @FXML
    private Button addcustomerbtn;
    @FXML
    private Button cancelbtn;
    @FXML
    private Button updatecustomerbtn;
    @FXML
    private Button deleteBtn1;
    @FXML
    private Button updateMaleCustomerbtn1;
    @FXML
    private TableView<FemaleCustomer> customertableview;
    @FXML
    private TableColumn<FemaleCustomer, String> addresscol;
    @FXML
    private TableColumn<FemaleCustomer, Integer> customeridcol;
    @FXML
    private TableColumn<FemaleCustomer, String> namecol;
    @FXML
    private TableColumn<FemaleCustomer, String> phonenumcol;
    @FXML
    private TableColumn<FemaleCustomer, String> postalcol;
    @FXML
    private TableColumn<FemaleCustomer, String> custcountrycol;
    @FXML
    private TableColumn<FemaleCustomer, Integer> firstleveldivcol;
    @FXML
    private TableColumn<FemaleCustomer, java.sql.Date> femaleLastExamCol;
    @FXML
    private TableColumn<MaleCustomer, String> maleAddressCol;
    @FXML
    private TableColumn<MaleCustomer, Integer> maleCustIdCol;
    @FXML
    private TableView<MaleCustomer> maleCustomerView;
    @FXML
    private TableColumn<MaleCustomer, Integer> maleFirstLevelCol;
    @FXML
    private TableColumn<MaleCustomer, Date> maleLastExamCol;
    @FXML
    private TableColumn<MaleCustomer, String> maleNameCol;
    @FXML
    private TableColumn<MaleCustomer, String> malePhoneNum;
    @FXML
    private TableColumn<MaleCustomer, String> malePostalCol;

    public CustomerRecordsController() {
    }

    public void setAllMaleCustomers(ObservableList<MaleCustomer> listOfMaleCustomers) {
        this.maleCustIdCol.setCellValueFactory(new PropertyValueFactory("customerId"));
        this.maleNameCol.setCellValueFactory(new PropertyValueFactory("customerName"));
        this.maleAddressCol.setCellValueFactory(new PropertyValueFactory("address"));
        this.malePostalCol.setCellValueFactory(new PropertyValueFactory("postalCode"));
        this.malePhoneNum.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        this.maleFirstLevelCol.setCellValueFactory(new PropertyValueFactory("divisionId"));
        this.maleLastExamCol.setCellValueFactory(new PropertyValueFactory("lastProstateExam"));
        this.maleCustomerView.setItems(listOfMaleCustomers);
    }



    public void setAllFemaleCustomers(ObservableList<FemaleCustomer> listOfCustomers) {
        this.customeridcol.setCellValueFactory(new PropertyValueFactory("customerId"));
        this.namecol.setCellValueFactory(new PropertyValueFactory("customerName"));
        this.addresscol.setCellValueFactory(new PropertyValueFactory("address"));
        this.postalcol.setCellValueFactory(new PropertyValueFactory("postalCode"));
        this.phonenumcol.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        this.firstleveldivcol.setCellValueFactory(new PropertyValueFactory("divisionId"));
        this.femaleLastExamCol.setCellValueFactory(new PropertyValueFactory<>("lastMammogram"));
        this.customertableview.setItems(listOfCustomers);
    }

    @FXML
    public void clickaddcustomerbtn(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/addCustomer.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @FXML
    public void clickcancelbtn(ActionEvent event) throws IOException {
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        this.scene = (Parent)FXMLLoader.load(this.getClass().getResource("/view/mainScreen.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @FXML
    public void clickupdatecustbtn(ActionEvent event) throws IOException, SQLException {
        if(customertableview.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer not selected");
            alert.setContentText(alert1.getAlert("customer"));
            alert.showAndWait();

        }
        else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/updateCustomer.fxml"));
            loader.load();
            UpdateCustomerController UCC = loader.getController();
            UCC.sendCustomer(customertableview.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    @FXML
    void clickupdateMaleCustbtn(ActionEvent event) throws SQLException, IOException {
        if(maleCustomerView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer not selected");
            alert.setContentText(alert1.getAlert("customer"));
            alert.showAndWait();
        }
        else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/updateCustomer.fxml"));
            loader.load();
            UpdateCustomerController UCC = loader.getController();
            UCC.sendCustomer(maleCustomerView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    @FXML
    public void clickDeleteBtn(ActionEvent event) throws SQLException, IOException {
        //checks to see if a customer is selected
        Customer customerToDelete = customertableview.getSelectionModel().getSelectedItem();
        int customerToDeleteId = customerToDelete.getCustomerId();
        if(!Helper.filterByCustomerId(customerToDeleteId).isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot delete customer");
            alert.setContentText("This customer currently has appointments and cannot be deleted");
            alert.showAndWait();
        }

        else if(customertableview.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer not selected");
            alert.setContentText(alert1.getAlert("customer"));
            alert.showAndWait();
        }

        else{
            //confirms the user wants to delete the customer
            Customer customerToDelete1 = customertableview.getSelectionModel().getSelectedItem();
            int customerToDeleteId1 = customerToDelete.getCustomerId();
            int rowsAffected = Helper.deleteCustomer(customerToDeleteId1);
            if(rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete this customer, do you want to proceed?");
                Optional<ButtonType> result = alert.showAndWait();

                //upon confirmation that the user wants to delete the customer, deletes them
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    allCustomers.remove(customerToDelete1);
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Customer deleted");
                    alert2.setContentText("Customer was successfully deleted");
                    alert2.showAndWait();
                }

                //refreshes the page once customer is deleted
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/customerRecords.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else{
                System.out.println("Delete Failed");
            }
        }

    }
    @FXML
    void clickMaleDeleteBtn(ActionEvent event) throws SQLException, IOException {
        //checks to see if a customer is selected
        Customer customerToDelete = maleCustomerView.getSelectionModel().getSelectedItem();
        int customerToDeleteId = customerToDelete.getCustomerId();
        if(!Helper.filterByCustomerId(customerToDeleteId).isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot delete customer");
            alert.setContentText("This customer currently has appointments and cannot be deleted");
            alert.showAndWait();
        }

        else if(maleCustomerView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer not selected");
            alert.setContentText(alert1.getAlert("customer"));
            alert.showAndWait();
        }

        else{
            //confirms the user wants to delete the customer
            Customer customerToDelete1 = maleCustomerView.getSelectionModel().getSelectedItem();
            int customerToDeleteId1 = customerToDelete.getCustomerId();
            int rowsAffected = Helper.deleteCustomer(customerToDeleteId1);
            if(rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete this customer, do you want to proceed?");
                Optional<ButtonType> result = alert.showAndWait();

                //upon confirmation that the user wants to delete the customer, deletes them
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    allCustomers.remove(customerToDelete1);
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Customer deleted");
                    alert2.setContentText("Customer was successfully deleted");
                    alert2.showAndWait();
                }

                //refreshes the page once customer is deleted
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/customerRecords.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else{
                System.out.println("Delete Failed");
            }
        }
    }



    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.setAllFemaleCustomers(Helper.getAllFemaleCustomers("female"));
            this.setAllMaleCustomers(Helper.getAllMaleCustomers("male"));
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }
}

