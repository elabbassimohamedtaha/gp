package vue;

import entities.Medicament;
import service.MedicamentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeController implements Initializable {

    @FXML private TextField rechercheField;
    @FXML private Label alerteLabel;
    @FXML private TableView<Medicament> tableView;
    @FXML private TableColumn<Medicament, Integer> idCol;
    @FXML private TableColumn<Medicament, String> nomCol, descCol, dateCol;
    @FXML private TableColumn<Medicament, Double> prixCol;
    @FXML private TableColumn<Medicament, Integer> quantiteCol;

    private MedicamentService service = new MedicamentService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        chargerMedicaments();
        verifierStockFaible();
    }

    private void chargerMedicaments() {
        ObservableList<Medicament> liste =
            FXCollections.observableArrayList(service.listerTous());
        tableView.setItems(liste);
    }

    private void verifierStockFaible() {
        long count = service.listerTous()
            .stream().filter(m -> m.getQuantite() < 10).count();
        if (count > 0) {
            alerteLabel.setText("ALERTE: " + count +
                " médicament(s) avec stock faible (< 10) !");
        } else {
            alerteLabel.setText("");
        }
    }

    @FXML
    public void rechercher() {
        String nom = rechercheField.getText();
        ObservableList<Medicament> liste =
            FXCollections.observableArrayList(service.rechercherParNom(nom));
        tableView.setItems(liste);
    }

    @FXML
    public void actualiser() {
        chargerMedicaments();
        verifierStockFaible();
    }

    @FXML
    public void voirStockFaible() {
        ObservableList<Medicament> liste =
            FXCollections.observableArrayList(service.stockFaible(10));
        tableView.setItems(liste);
        alerteLabel.setText("Affichage stock faible (< 10)");
    }

    @FXML
    public void deconnexion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/login.fxml"));
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion Pharmacie");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}