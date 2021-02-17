package C482PA.Controllers;

import C482PA.Model.InHouse;
import C482PA.Model.Inventory;
import C482PA.Model.Outsourced;
import C482PA.Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static C482PA.Controllers.MainScreen.partToModifyIndex;

public class ModifyPart implements Initializable {

    @FXML
    private Label lblModifyPartScreen;

    @FXML
    private RadioButton rbModIn;

    @FXML
    private ToggleGroup tgModSource;

    @FXML
    private RadioButton rbModOut;

    @FXML
    private Label lblModPartDuo;

    @FXML
    private TextField txtModPartID;

    @FXML
    private TextField txtModPartName;

    @FXML
    private TextField txtModPartInv;

    @FXML
    private TextField txtModPartPrice;

    @FXML
    private TextField txtModPartMax;

    @FXML
    private TextField txtModPartDuo;

    @FXML
    private TextField txtModPartMin;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private boolean isInHouse;
    private int partId;
    int partIndex = MainScreen.partToModifyIndex();
    private String exceptionMessage = "";

    /**
     * RUNTIME ERROR Had to Throw IOException to prevent from an error in opening the FXML file
     * Cancels and returns to the main screen
     * @param event When the cancel button is pressed exit to main screen
     * @throws IOException In case there is an error loading the file
     */
    @FXML
    void handleModPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm cancel");
        alert.setHeaderText("Confirm cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage modPart = (Stage) ((Node)event.getSource()).getScene().getWindow();
            modPart.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();


        }
        else {
            alert.close();
        }
    }

    /**
     * FUTURE ENHANCEMENT A more seamless integration to creating the objects different types
     * Saves the information entered as an updated part
     * @param event
     */
    @FXML
    void handleModPartSave(ActionEvent event) throws IOException {
        String name = txtModPartName.getText();
        String inv = txtModPartInv.getText();
        String price = txtModPartPrice.getText();
        String min = txtModPartMin.getText();
        String max = txtModPartMax.getText();
        String duo = txtModPartDuo.getText();


        try {
            exceptionMessage = Part.isPartValid(name, Double.parseDouble(price), Integer.parseInt(inv),
                                                Integer.parseInt(min), Integer.parseInt(max), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Error Modify Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                if (rbModIn.isSelected()) {
                    System.out.println("Part name: " + name);
                    InHouse inPart = new InHouse(partId, name, Double.parseDouble(price), Integer.parseInt(inv), Integer.parseInt(min),
                                                Integer.parseInt(max), Integer.parseInt(duo));
                    Inventory.updatePart(partToModifyIndex(), inPart);

                } else {
                    System.out.println("Part name: " + name);
                    Outsourced outPart = new Outsourced(partId, name, Double.parseDouble(price), Integer.parseInt(inv), Integer.parseInt(min),
                                                        Integer.parseInt(max), duo);
                    Inventory.updatePart(partToModifyIndex(), outPart);
                }
                Stage modPart = (Stage) ((Node)event.getSource()).getScene().getWindow();
                modPart.close();
                Parent modSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modSave);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("There was an issue with the information in the fields");
            alert.showAndWait();
        }
    }




    /**
     * Changes the label depending on radio button selected
     * @param event
     */
    @FXML
    void modInHouseRadio(ActionEvent event) {
        isInHouse = true;
        lblModPartDuo.setText("Machine ID");
        txtModPartDuo.setPromptText("Machine ID");
        rbModOut.setSelected(false);
    }

    /**
     * Changes the label depending on radio button selected
     * @param event
     */
    @FXML
    void modOutsourcedRadio(ActionEvent event) {
        isInHouse = false;
        lblModPartDuo.setText("Company name");
        txtModPartDuo.setPromptText("Company name");
        rbModIn.setSelected(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part part = Inventory.getAllParts().get(partIndex);
        partId = Inventory.getAllParts().get(partIndex).getId();
        txtModPartID.setText("Auto-Gen: " + partId);
        txtModPartName.setText(part.getName());
        txtModPartPrice.setText(Double.toString(part.getPrice()));
        txtModPartInv.setText(Integer.toString(part.getStock()));
        txtModPartMax.setText(Integer.toString(part.getMax()));
        txtModPartMin.setText(Integer.toString(part.getMin()));
        if (part instanceof InHouse) {
            lblModPartDuo.setText("Machine ID");
            txtModPartDuo.setText(Integer.toString(((InHouse) part).getMachineId()));
            rbModIn.setSelected(true);
        }
        else if (part instanceof Outsourced){
            lblModPartDuo.setText("Company Name");
            txtModPartDuo.setText(((Outsourced) part).getCompanyName());
            rbModOut.setSelected(true);
        }



    }
}
