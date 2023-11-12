package lk.ijse.dep11.leon_pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.dep11.leon_pos.db.CustomerData;
import lk.ijse.dep11.leon_pos.db.OrderData;
import lk.ijse.dep11.leon_pos.tm.Customer;
import lk.ijse.dep11.leon_pos.AppInitializer;
import lk.ijse.dep11.leon_pos.db.CustomerData;
import lk.ijse.dep11.leon_pos.db.OrderData;
import lk.ijse.dep11.leon_pos.tm.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class ManageCustomerFormController {
    public AnchorPane root;
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public ImageView imgHome;
    public JFXButton btnNew;
    public TableView<Customer> tblCustomer;
    public Rectangle rectangle;
    public HBox hBoxContainer;

    public void initialize() {
        rectangle.widthProperty().bind(root.widthProperty());


        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(0).setStyle("-fx-alignment: center;");
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        txtCustomerId.setEditable(false);
        btnDelete.setDisable(true);
        btnSave.setDefaultButton(true);
        btnNew.fire();
        try {
            tblCustomer.getItems().addAll(CustomerData.getAllCustomer());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers, try later!").show();
        }
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur) ->{
            if (cur != null){
                btnSave.setText("UPDATE");
                btnDelete.setDisable(false);
                txtCustomerId.setText(cur.getId());
                txtCustomerName.setText(cur.getName());
                txtCustomerAddress.setText(cur.getAddress());
            }else{
                btnSave.setText("SAVE");
                btnDelete.setDisable(true);
            }
        });
        Platform.runLater(txtCustomerName::requestFocus);
    }

    public void btnNewOnAction(ActionEvent actionEvent) throws IOException {
        for (TextField textField : new TextField[]{txtCustomerId, txtCustomerName, txtCustomerAddress})
            textField.clear();
        tblCustomer.getSelectionModel().clearSelection();
        txtCustomerName.requestFocus();
        try {
            String lastCustomerId = CustomerData.getLastCustomerId();
            if (lastCustomerId == null) {
                txtCustomerId.setText("C001");
            } else {
                int newId = Integer.parseInt(lastCustomerId.substring(1)) + 1;
                txtCustomerId.setText(String.format("C%03d", newId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to establish the database connection, try again").show();
            navigateToMain(null);
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (!isDataValid()) return;

        Customer customer = new Customer(txtCustomerId.getText(),
                txtCustomerName.getText().strip(), txtCustomerAddress.getText().strip());
        try {
            if (btnSave.getText().equals("SAVE")){
                CustomerData.saveCustomer(customer);
                tblCustomer.getItems().add(customer);
            }else{
                CustomerData.updateCustomer(customer);
                ObservableList<Customer> customerList = tblCustomer.getItems();
                Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
                customerList.set(customerList.indexOf(selectedCustomer), customer);
                tblCustomer.refresh();
            }
            btnNew.fire();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the customer, try again").show();
        }
    }

    private boolean isDataValid() {
        String name = txtCustomerName.getText().strip();
        String address = txtCustomerAddress.getText().strip();

        if (!name.matches("[A-Za-z ]{2,}")) {
            txtCustomerName.requestFocus();
            txtCustomerName.selectAll();
            return false;
        } else if (address.length() < 3) {
            txtCustomerAddress.requestFocus();
            txtCustomerAddress.selectAll();
            return false;
        }

        return true;
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            if (OrderData.existsOrderByCustomerId(txtCustomerId.getText())){
                new Alert(Alert.AlertType.ERROR,
                        "Unable to delete this customer, already associated with an order").show();
            }else{
                CustomerData.deleteCustomer(txtCustomerId.getText());
                ObservableList<Customer> customerList = tblCustomer.getItems();
                Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
                customerList.remove(selectedCustomer);
                if (customerList.isEmpty()) btnNew.fire();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void navigateToMain(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
}
