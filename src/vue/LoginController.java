package vue;

import entities.Utilisateur;
import service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private UtilisateurService service = new UtilisateurService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    public void seConnecter() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Utilisateur u = service.login(username, password);

        if (u != null) {
            try {
                String fxml = u.getRole().equals("ADMIN") ? 
                    "/vue/admin.fxml" : "/vue/employe.fxml";
                Parent root = FXMLLoader.load(getClass().getResource(fxml));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle(u.getRole().equals("ADMIN") ? 
                    "Admin - Gestion Pharmacie" : "Employé - Gestion Pharmacie");
                stage.show();
            } catch (Exception e) {
                messageLabel.setText("Erreur: " + e.getMessage());
            }
        } else {
            messageLabel.setText("Identifiants incorrects !");
        }
    }
}