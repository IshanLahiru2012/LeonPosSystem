package lk.ijse.dep11.leon_pos.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import lk.ijse.dep11.leon_pos.AppInitializer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    public AnchorPane root;
    public ImageView imgCustomer;
    public ImageView imgStock;
    public ImageView imgSearch;
    public Label lblMenu;
    public Label lblDescription;
    public ImageView imgOrder;
    public VBox vBoxContainer;


    public void initialize(URL url, ResourceBundle resourceBundle) {

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

    }
    public void navigate(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getSource() instanceof ImageView){
            ImageView instance = (ImageView) mouseEvent.getSource();

            Parent subRoot = null;

            switch (instance.getId()){
                case "imgCustomer" :
                    subRoot = FXMLLoader.load(getClass().getResource("/view/ManageCustomerForm.fxml"));
                    break;
                case "imgStock":
                    subRoot = FXMLLoader.load(getClass().getResource("/view/ManageItemForm.fxml"));
                    break;
                case "imgOrder":
                    subRoot = FXMLLoader.load(getClass().getResource("/view/PlaceOrderForm.fxml"));
                    break;
                case "imgSearch":
                    subRoot = FXMLLoader.load(getClass().getResource("/view/SearchOrderForm.fxml"));
                    break;
            }

            if(subRoot != null){
                Scene subScene = new Scene(subRoot);
                Platform.runLater(()->{
                    Stage primaryStage = (Stage) root.getScene().getWindow();
                    primaryStage.setResizable(true);
                    primaryStage.setScene(subScene);
                    primaryStage.sizeToScene();
                    primaryStage.centerOnScreen();
                    primaryStage.setOnCloseRequest(Event::consume);

                    TranslateTransition trans = new TranslateTransition(Duration.millis(200), subScene.getRoot());
                    trans.setFromY(subScene.getWidth());
                    trans.setToY(0);
                    trans.play();
                });
            }

        }
    }

    public void iconOnMouseEntered(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof ImageView){
            ImageView instance = (ImageView) mouseEvent.getSource();
            switch(instance.getId()){
                case "imgCustomer" :
                    lblMenu.setText("Manage Customers");
                    lblDescription.setText("Click to add, edit, delete, search or view customers");
                    break;
                case "imgStock":
                    lblMenu.setText("Manage Items");
                    lblDescription.setText("Click to add, edit, delete, search or view items");
                    break;
                case "imgOrder":
                    lblMenu.setText("Place Orders");
                    lblDescription.setText("Click to place a new order");
                    break;
                case "imgSearch":
                    lblMenu.setText("Search Orders");
                    lblDescription.setText("Click if you want to search orders");
                    break;
            }
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), instance);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.play();

            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.DARKGOLDENROD);
            dropShadow.setRadius(30);
            dropShadow.setWidth(30);
            dropShadow.setHeight(30);

            instance.setEffect(dropShadow);
        }
    }

    public void iconOnMouseExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView instance = (ImageView) mouseEvent.getSource();
            instance.setEffect(null);
            lblMenu.setText("Welcome to Smart POS System");
            lblDescription.setText("Please select one of main operations to proceed");

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200),instance);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        }

    }
    public static void navigateToMain(Node rootNode) throws IOException {
        Parent root = FXMLLoader.load(AppInitializer.class.getResource("/view/MainForm.fxml"));
        Scene scene = new Scene(root);
        Stage subStage = (Stage) rootNode.getScene().getWindow();
        Stage primaryStage = new Stage();

        Platform.runLater(()->{
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();
            primaryStage.setOnCloseRequest(null);
            primaryStage.show();
        });
        subStage.close();


    }


}
