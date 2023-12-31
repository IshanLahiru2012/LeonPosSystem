package lk.ijse.dep11.leon_pos.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.dep11.leon_pos.db.OrderData;
import lk.ijse.dep11.leon_pos.tm.Order;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class SearchOrderFormController {
    public TextField txtSearch;
    public TableView<Order> tblOrders;
    public AnchorPane root;
    public Rectangle rectangle;

    public void initialize() throws IOException {
        String[] colNames = {"orderId", "orderDate", "customerId", "customerName", "orderTotal"};
        for (int i = 0; i < colNames.length; i++) {
            tblOrders.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(colNames[i]));
        }
        try {
            tblOrders.getItems().addAll(OrderData.findOrders(""));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load orders, try later").show();
            e.printStackTrace();
            navigateToHome(null);
        }
        txtSearch.textProperty().addListener((ov, prev, cur)->{
            tblOrders.getItems().clear();
            try {
                tblOrders.getItems().addAll(OrderData.findOrders(cur));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        rectangle.widthProperty().bind(root.widthProperty());
    }

    public void tblOrders_OnMouseClicked(MouseEvent mouseEvent) {
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
}
