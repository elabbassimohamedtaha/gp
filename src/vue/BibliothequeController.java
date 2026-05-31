package vue;

import entities.Medicament;
import service.MedicamentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class BibliothequeController implements Initializable {

    @FXML private TextField nomField;
    @FXML private TextField descField;
    @FXML private TextField prixField;
    @FXML private TextField quantiteField;
    @FXML private TableView<Medicament> tableView;
    @FXML private TableColumn<Medicament, Integer> idCol;
    @FXML private TableColumn<Medicament, String> nomCol;
    @FXML private TableColumn<Medicament, String> descCol;
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
        chargerDonnees();
    }

    private void chargerDonnees() {
        ObservableList<Medicament> liste = 
            FXCollections.observableArrayList(service.listerTous());
        tableView.setItems(liste);
    }

    @FXML
    public void ajouterMedicament() {
        Medicament m = new Medicament();
        m.setNom(nomField.getText());
        m.setDescription(descField.getText());
        m.setPrix(Double.parseDouble(prixField.getText()));
        m.setQuantite(Integer.parseInt(quantiteField.getText()));
        service.ajouter(m);
        chargerDonnees();
        viderChamps();
    }

    @FXML
    public void modifierMedicament() {
        Medicament m = tableView.getSelectionModel().getSelectedItem();
        if (m != null) {
            m.setNom(nomField.getText());
            m.setDescription(descField.getText());
            m.setPrix(Double.parseDouble(prixField.getText()));
            m.setQuantite(Integer.parseInt(quantiteField.getText()));
            service.modifier(m);
            chargerDonnees();
            viderChamps();
        }
    }

    @FXML
    public void supprimerMedicament() {
        Medicament m = tableView.getSelectionModel().getSelectedItem();
        if (m != null) {
            service.supprimer(m.getId());
            chargerDonnees();
        }
    }

    @FXML
    public void actualiser() {
        chargerDonnees();
    }

    private void viderChamps() {
        nomField.clear();
        descField.clear();
        prixField.clear();
        quantiteField.clear();
    }
}