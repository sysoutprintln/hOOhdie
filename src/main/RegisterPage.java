// RegisterPage class
package main;

import connect.Connect;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class RegisterPage extends Application {
	Label regLbl, emailLbl, usmLbl, passwordLbl, conPasswordLbl, phoneLbl, addressLbl, genderLbl, termsLbl;
	TextField emailTf, usmTf, phoneTf;
	TextArea addressTA;
	PasswordField passwordField, conPasswordField;
	ToggleGroup genderGroup;
	RadioButton maleBtn, femaleBtn;
	CheckBox termsCb;
	Button regBtn;
	GridPane gp;
	VBox vbox;
	Pane pane;
	MenuBar menuBar;
	Menu menuReg;

	public void initReg() {

		regLbl = new Label("Register");
		regLbl.setStyle("-fx-font-size: 50px; " + "-fx-font-weight: bold;" + "-fx-font-style: italic;");

		emailLbl = new Label("Email: ");
		emailLbl.setStyle("-fx-font-weight: bold;");
		emailTf = new TextField();
		emailTf.setPromptText("Input Email");

		usmLbl = new Label("Username: ");
		usmLbl.setStyle("-fx-font-weight: bold;");
		usmTf = new TextField();
		usmTf.setPromptText("Input a unique username");

		passwordLbl = new Label("Password: ");
		passwordLbl.setStyle("-fx-font-weight: bold;");
		passwordField = new PasswordField();
		passwordField.setPromptText("Input password");

		conPasswordLbl = new Label("Confirm Password: ");
		conPasswordLbl.setStyle("-fx-font-weight: bold;");
		conPasswordField = new PasswordField();
		conPasswordField.setPromptText("Confirm password");

		phoneLbl = new Label("Phone Number: ");
		phoneLbl.setStyle("-fx-font-weight: bold;");
		phoneTf = new TextField();
		phoneTf.setPromptText("Example: +6212345678901");

		genderLbl = new Label("Gender: ");
		genderLbl.setStyle("-fx-font-weight: bold;");
		genderGroup = new ToggleGroup();
		maleBtn = new RadioButton("Male");
		maleBtn.setToggleGroup(genderGroup);
		femaleBtn = new RadioButton("Female");
		femaleBtn.setToggleGroup(genderGroup);

		addressLbl = new Label("Address: ");
		addressLbl.setStyle("-fx-font-weight: bold;");
		addressTA = new TextArea();
		addressTA.setPromptText("Input address");

		termsLbl = new Label("I Agree to Terms & Conditions");
		termsCb = new CheckBox();
		termsCb.setStyle("-fx-padding-right: 10px;");
		termsLbl.setLabelFor(termsCb);
		termsLbl.setStyle("-fx-font-size: 12 px; ");

		regBtn = new Button("Register");

		regBtn = new Button("Register");
		regBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				validateUserInput();
			}
		});
		
		menuBar = new MenuBar();
		menuReg = new Menu("Register");
		menuBar.getMenus().add(menuReg);
		MenuItem loginItem = new MenuItem("Login");
		menuReg.getItems().add(loginItem);

		loginItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Create a new LoginPage
				LoginPage loginPage = new LoginPage();

				// Get the current stage
				Stage currentStage = (Stage) menuBar.getScene().getWindow();

				// Switch to the LoginPage
				try {
					currentStage.setScene(loginPage.createLoginScene());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void styleReg() {

		gp = new GridPane();
		gp.setHgap(15);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);

		int row = 0;

		gp.add(emailLbl, 0, row++);
		gp.add(emailTf, 0, row++, 2, 1);
		gp.add(usmLbl, 0, row++);
		gp.add(usmTf, 0, row++, 2, 1);
		gp.add(passwordLbl, 0, row++);
		gp.add(passwordField, 0, row++, 2, 1);
		gp.add(conPasswordLbl, 0, row++);
		gp.add(conPasswordField, 0, row++, 2, 1);
		gp.add(phoneLbl, 0, row++);
		gp.add(phoneTf, 0, row++, 2, 1);
		gp.add(genderLbl, 0, row++);
		gp.add(maleBtn, 0, row);
		gp.add(femaleBtn, 1, row++);

		gp.add(addressLbl, 0, row++);
		gp.add(addressTA, 0, row++, 2, 1);

		HBox termsHBox = new HBox(5);
		termsHBox.getChildren().addAll(termsCb, termsLbl);
		HBox.setHgrow(termsLbl, Priority.ALWAYS);

		gp.add(termsHBox, 0, row++, 2, 1);

		gp.add(regBtn, 0, row++, 2, 1);

		GridPane.setConstraints(regBtn, 1, row, 1, 1);
		GridPane.setHalignment(regBtn, HPos.RIGHT);
		GridPane.setValignment(regBtn, VPos.BOTTOM);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(javafx.scene.layout.Priority.NEVER);
		col1.setPrefWidth(150);

		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(javafx.scene.layout.Priority.ALWAYS);

		gp.getColumnConstraints().addAll(col1, col2);

		vbox = new VBox(regLbl, gp);
		vbox.setAlignment(Pos.CENTER_LEFT);
		vbox.setPadding(new Insets(10, 50, 50,50));
	}
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		initReg();
		styleReg();
		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(vbox);
		Scene scene = new Scene(bp, 760, 789);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void showErrorAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void validateUserInput() {

		Connect connect = Connect.getInstance();

		int userIndex = connect.getUserCount() + 1;
		String userID = String.format("US%03d", userIndex);

		if (userID.length() > 5) {
			userID = userID.substring(0, 5);
		}

		String email = emailTf.getText();
		if (!email.endsWith("@hoohdie.com")) {
			showErrorAlert("Error", "Email must end with @hoohdie.com");
			return;
		}

		String username = usmTf.getText();
		if (username.isEmpty() || !username.matches("^[a-zA-Z0-9_]{3,20}$")) {
			showErrorAlert("Error", "Username must be unique and contain only alphanumeric characters or underscores");
			return;
		}

		String password = passwordField.getText();
		if (password.isEmpty() || !password.matches("^[a-zA-Z0-9]{5,}$")) {
			showErrorAlert("Error", "Password must contain minimal 5 characters");
			return;
		}

		String confirmPassword = conPasswordField.getText();
		if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
			showErrorAlert("Error", "Confirm Password must be the same as Password");
			return;
		}

		String phoneNumber = phoneTf.getText();
		if (phoneNumber.isEmpty() || !phoneNumber.matches("^\\+62[1-9]{11}$")) {
			showErrorAlert("Error", "Phone Number must be 14 characters and start with +62");
			return;
		}

		String address = addressTA.getText();
		if (addressTA.getText().isEmpty()) {
			showErrorAlert("Error", "Address must be filled");
			return;
		}

		String gender = (maleBtn.isSelected()) ? "Male" : "Female";
		if (genderGroup.getSelectedToggle() == null) {
			showErrorAlert("Error", "Gender must be selected");
			return;
		}

		if (!termsCb.isSelected()) {
			showErrorAlert("Error", "Please agree to Terms & Conditions");
			return;
		}

		String insertQuery = "INSERT INTO user (UserID, Email, Username, Password, PhoneNumber, Address, Gender, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		connect.execInsert(insertQuery, userID, email, username, password, phoneNumber, address, gender, "User");

		Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
		successAlert.setTitle("Success");
		successAlert.setHeaderText("Your input is valid!");
		successAlert.showAndWait();

		Stage primaryStage = (Stage) regBtn.getScene().getWindow();
		LoginPage loginPage = new LoginPage();
		primaryStage.setScene(loginPage.createLoginScene());
	}

}