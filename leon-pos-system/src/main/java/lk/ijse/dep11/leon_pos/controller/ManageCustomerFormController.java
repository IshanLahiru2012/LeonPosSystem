package lk.ijse.dep11.leon_pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.leon_pos.db.CustomerData;
import lk.ijse.dep11.leon_pos.tm.Customer;

import java.io.IOException;
import java.sql.SQLException;

public class ManageCustomerFormController {
    public JFXButton btnNew;
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView<Customer> tblCustomer;
    public ImageView imgHome;
    public AnchorPane root;

    public void initialize(){
        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        txtCustomerId.setEditable(false);
        btnDelete.setDisable(true);
        btnSave.setDefaultButton(true);
        btnNew.fire();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, t1) -> {
            if(t1 != null){
                btnSave.setText("UPDATE");
                btnDelete.setDisable(false);
                txtCustomerId.setText(t1.getId());
                txtCustomerName.setText(t1.getName());
                txtCustomerAddress.setText(t1.getAddress());
            }else{
                btnSave.setText("SAVE");
                btnDelete.setDisable(true);
            }
        });
//        Platform.runLater(txtCustomerName::requestFocus);
    }
    public void btnNewOnAction(ActionEvent actionEvent) throws IOException {
        for(TextField textField : new TextField[]{txtCustomerId,txtCustomerName,txtCustomerAddress})textField.clear();
        tblCustomer.getSelectionModel().clearSelection();
        txtCustomerName.requestFocus();
        try {
            String lastCustomerId = CustomerData.getLastCustomerId();
            if(lastCustomerId==null){
                txtCustomerId.setText("C001");
            }else {
                int lastId = Integer.parseInt(lastCustomerId.substring(1));
                txtCustomerId.setText(String.format("C%03d",lastId+1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to connect to database, try again").show();
            navigateToMain(null);
        }

    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if(!isDataValid())return;
        Customer customer = new Customer(txtCustomerId.getText(),txtCustomerName.getText(),
                txtCustomerAddress.getText());
        try {
            if(btnSave.getText().equals("SAVE")){
                CustomerData.insertCustomer(customer);
                tblCustomer.getItems().add(customer);
            }else {
                CustomerData.updateCustomer(customer);
                ObservableList<Customer> customerObservableList = tblCustomer.getItems();
                Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
                customerObservableList.set(customerObservableList.indexOf(selectedCustomer),customer);
                tblCustomer.refresh();
            }
            btnNew.fire();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to save customer.. try again!").show();
        }
    }
    private boolean isDataValid(){
        String name = txtCustomerName.getText().strip();
        String address = txtCustomerAddress.getText().strip();

        if (!name.matches("[A-Za-z ]{2,}")) {
            txtCustomerName.selectAll();
            txtCustomerName.requestFocus();
            return false;
        } else if (address.length() < 3) {
            txtCustomerAddress.selectAll();
            txtCustomerAddress.requestFocus();
            return false;
        }
        return true;
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {

        
    }

    public void navigateToMain(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
}
