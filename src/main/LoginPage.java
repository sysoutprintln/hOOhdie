// LoginPage class
package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connect.Connect;
import session.SessionManager;

public class LoginPage {

	Label loginLbl, usernameLbl, passwordLbl;
	TextField usernameTf;
	PasswordField passwordField;
	Button loginBtn;
	VBox vbox;
	GridPane gp;
	BorderPane bp;
	MenuBar menuBar;
	Menu menuLogin;
	Scene scene;

	Connect connect;
	ResultSet rs;

	Alert alert;

	private String currentUserId;

	public LoginPage() {
		connect = Connect.getInstance();
	}

	public void initLogin() {

		loginLbl = new Label("Login");
		loginLbl.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; " + "-fx-font-style: italic;");
		usernameLbl = new Label("Username: ");
		usernameTf = new TextField();
		usernameTf.setPromptText("Input Username");
		passwordLbl = new Label("Password: ");
		passwordField = new PasswordField();
		passwordField.setPromptText("Input Password");
		loginBtn = new Button("Login");

		loginBtn.setOnAction(event -> {
		    String username = usernameTf.getText();
		    String password = passwordField.getText();

		    if (validate(username, password)) {
		        SessionManager.getInstance().setCurrentUserId(currentUserId);

		        if (SessionManager.getInstance().isAdmin()) {
		            switchToEditProductPage();
		        } else {
		            switchToHomePage();
		        }
		    } else {
		        alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText("Error");
		        alert.setContentText("Wrong Credential");
		        alert.showAndWait();
		    }
		});


		menuBar = new MenuBar();
		Pane pane = new Pane();
		pane.getChildren().add(menuBar);
		menuLogin = new Menu("Login");
		menuBar.getMenus().add(menuLogin);
		MenuItem registerItem = new MenuItem("Register");
		menuLogin.getItems().add(registerItem);

		registerItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Create a new RegisterPage
				RegisterPage registerPage = new RegisterPage();

				// Get the current stage
				Stage currentStage = (Stage) menuBar.getScene().getWindow();

				// Switch to the RegisterPage
				try {
					registerPage.start(currentStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void switchToEditProductPage() {
		EditProductPage editPage = new EditProductPage();
		Scene editScene = editPage.createEditProductPage();
		
		Stage currentStage = (Stage) loginBtn.getScene().getWindow();
		currentStage.setScene(editScene);
		currentStage.show();
	}

	private void switchToHomePage() {
		HomePage homePage = new HomePage();
		Scene homeScene = homePage.createHomeScene();

		Stage currentStage = (Stage) loginBtn.getScene().getWindow();
		currentStage.setScene(homeScene);
		currentStage.show();
	}

	public Scene createLoginScene() {
		initLogin();
		styleLogin();

		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(vbox);

		return new Scene(bp, 760, 789);
	}

	private boolean validate(String username, String password) {
	    String query = "SELECT UserID, Role FROM user WHERE Username = ? AND Password = ?";
	    try (PreparedStatement pstmt = connect.con.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        pstmt.setString(2, password);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                currentUserId = rs.getString("UserID");
	                SessionManager.getInstance().setCurrentUserId(currentUserId);
	                return true;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	private void onLoginSuccess(String userId) {
	    if (SessionManager.getInstance().isAdmin()) {
	        EditProductPage editProductPage = new EditProductPage();
	        Scene editProductScene = editProductPage.createEditProductPage();
	        Stage currentStage = (Stage) loginBtn.getScene().getWindow();
	        currentStage.setScene(editProductScene);
	    } else {
	        HomePage homePage = new HomePage();
	        Scene homeScene = homePage.createHomeScene();
	        Stage currentStage = (Stage) loginBtn.getScene().getWindow();
	        currentStage.setScene(homeScene);
	    }
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public Scene getScene() {
		return scene;
	}

	public void styleLogin() {

		gp = new GridPane();
		gp.setHgap(15);
		gp.setVgap(15);
		gp.setAlignment(Pos.CENTER);
		gp.add(usernameLbl, 0, 0);
		gp.add(usernameTf, 1, 0);
		gp.add(passwordLbl, 0, 1);
		gp.add(passwordField, 1, 1);
		gp.add(loginBtn, 0, 2, 2, 1);
		loginBtn.setPrefWidth(250);

		vbox = new VBox(loginLbl, gp);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(8);
	}
}
