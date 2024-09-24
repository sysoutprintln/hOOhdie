// HomePage class
package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import table.HoodieTable;
import connect.Connect;
import session.SessionManager;

import java.util.HashMap;
import java.sql.ResultSet;

public class HomePage {

	Label titleLbl, detailLbl, idLbl, nameLbl, priceLbl, quantityLbl, totalPriceLbl, selectListLbl;
	Button cartBtn;

	BorderPane bp;
	GridPane gp, detailsPane, prodPane;
	VBox vbox;
	HBox hbox;

	ColumnConstraints column1, column2;
	RowConstraints row1;

	Scene scene;

	MenuBar menuBar;
	Menu menuAccount, menuUser;
	Pane pane;

	ListView<String> listView = new ListView<>();
	Spinner<Integer> spinner = new Spinner<>(0, 100, 1);
	
	 Map<String, HoodieTable> hoodieData;

	public Node hoodieDetails;

	public void initHome() {

		Map<String, HoodieTable> hoodieData = new HashMap<>();
		
		 hoodieData = new HashMap<>();
		

		titleLbl = new Label("hO-Ohdie");
		titleLbl.setStyle("-fx-font-size: 35px; -fx-font-family: 'Times New Roman'; -fx-font-style: italic;");

		detailLbl = new Label("Hoodie's Detail");
		detailLbl.setStyle("-fx-font-size: 35 px; -fx-font-weight: bold; -fx-font-style: italic;");

		selectListLbl = new Label("Select an item from the list...");

		idLbl = new Label("Hoodie ID: ");
		nameLbl = new Label("Name: ");
		priceLbl = new Label("Price: ");
		quantityLbl = new Label("Quantity: ");
		totalPriceLbl = new Label("Total Price: ");

		idLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
		nameLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
		priceLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
		quantityLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
		totalPriceLbl.setStyle("-fx-font-size: 17px; -fx-font-family: 'Times New Roman';");
		selectListLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
		
		listView = new ListView<>();
		
		spinner = new Spinner<>(1, 100, 1);

		cartBtn = new Button("Add to Cart");
		cartBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Times New Roman';");

		idLbl.setVisible(false);
		nameLbl.setVisible(false);
		priceLbl.setVisible(false);
		quantityLbl.setVisible(false);
		spinner.setVisible(false);
		totalPriceLbl.setVisible(false);
		cartBtn.setVisible(false);

		listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {

				selectListLbl.setVisible(false);
				detailLbl.setVisible(true);
				idLbl.setVisible(true);
				nameLbl.setVisible(true);
				priceLbl.setVisible(true);
				quantityLbl.setVisible(true);
				spinner.setVisible(true);
				totalPriceLbl.setVisible(true);
				cartBtn.setVisible(true);

			}
		});

		// MENU BAR
		hbox = new HBox();
		hbox.getChildren().addAll(titleLbl, detailLbl);

		menuBar = new MenuBar();
		pane = new Pane();
		pane.getChildren().add(menuBar);
		
		menuAccount = new Menu("Account");
		menuUser = new Menu("User");
		menuBar.getMenus().addAll(menuAccount, menuUser);
		
		MenuItem logoutItem = new MenuItem("Logout");
		logoutItem.setOnAction(e -> {
		    LoginPage loginPage = new LoginPage();
		    Scene loginScene = loginPage.createLoginScene();
		    
		    Stage currentStage = (Stage) menuBar.getScene().getWindow();
		    currentStage.setScene(loginScene);
		});
		menuAccount.getItems().add(logoutItem);
		
		MenuItem homeItem = new MenuItem("Home");
		homeItem.setOnAction(e -> {
			HomePage homePage = new HomePage();
			Scene homeScene =  homePage.createHomeScene();
			
			Stage currentStage = (Stage) menuBar.getScene().getWindow();
			currentStage.setScene(homeScene);
		});
		menuUser.getItems().add(homeItem);
		
		MenuItem cartItem = new MenuItem("Cart");
		cartItem.setOnAction(e -> {
			SessionManager sessionManager = SessionManager.getInstance();
			Connect databaseConnection = Connect.getInstance();
			CartPage cartPage = new CartPage(sessionManager, databaseConnection);
			Scene cartScene =  cartPage.createCartScene();
			
			Stage currentStage = (Stage) menuBar.getScene().getWindow();
			currentStage.setScene(cartScene);
		});
		menuUser.getItems().add(cartItem);
		
		MenuItem historyItem = new MenuItem("History");
		historyItem.setOnAction(e -> {
			HistoryPage historyPage = new HistoryPage();
			Scene historyScene = historyPage.createHistoryScene();
			
			Stage currentStage = (Stage) menuBar.getScene().getWindow();
			currentStage.setScene(historyScene);
		});
		menuUser.getItems().add(historyItem);

	}

	public void styleHome() {

		populateListView();

		gp = new GridPane();
	    gp.setHgap(5);
	    gp.setVgap(5);
	    gp.setPadding(new Insets(5, 5, 25, 25));

	    detailsPane = new GridPane();
	    detailsPane.setVgap(5);
	    detailsPane.setHgap(25);
	    
	    bp = new BorderPane();
	    bp.setLeft(spinner);
	    bp.setRight(totalPriceLbl);
	    
	    HBox hbox = new HBox();
	    hbox.getChildren().addAll(spinner, totalPriceLbl);
	    hbox.setSpacing(5);

	    detailsPane.add(hbox, 0, 5);
	    
	    detailsPane.add(detailLbl, 0, 0);
	    GridPane.setMargin(detailLbl, new Insets(0, 0, 10, 0));
	    detailsPane.add(selectListLbl, 0, 1);
	    detailsPane.add(idLbl, 0, 1);
	    detailsPane.add(nameLbl, 0, 2);
	    detailsPane.add(priceLbl, 0, 3);
	    detailsPane.add(quantityLbl, 0, 4);
	    detailsPane.add(bp, 0, 6);
	    detailsPane.add(cartBtn, 0, 7);
	    
	    prodPane = new GridPane();
	    prodPane.setVgap(5);
	    prodPane.setHgap(25);
	    
	    prodPane.add(titleLbl, 0, 0);
	    prodPane.add(listView, 0, 1);
	    
	    gp.add(prodPane, 0, 1);
	    gp.add(detailsPane, 1, 1);
	    
	    GridPane.setMargin(prodPane, new Insets(0, 0, 0, 25));
	    GridPane.setMargin(detailsPane, new Insets(90, 25, 25, 30));
	    
	    vbox = new VBox(menuBar, gp);
	    vbox.setAlignment(Pos.TOP_LEFT);
	    vbox.setPadding(new Insets(5));
	    vbox.setSpacing(5);
	}

	public void populateListView() {
		ObservableList<String> items = FXCollections.observableArrayList();

		Map<String, HoodieTable> hoodieData = new HashMap<>();

		Connect connect = Connect.getInstance();
		String sql = "SELECT HoodieID, HoodieName, HoodiePrice FROM hoodie";
		ResultSet resultSet = connect.execQuery(sql);
		try {
			while (resultSet.next()) {
				HoodieTable hoodie = new HoodieTable();
				hoodie.setId(resultSet.getString("HoodieID"));
	            hoodie.setName(resultSet.getString("HoodieName"));
	            hoodie.setPrice(resultSet.getDouble("HoodiePrice"));
				String hoodieItem = hoodie.getId() + "	" + hoodie.getName();
				items.add(hoodieItem);
				hoodieData.put(hoodieItem, hoodie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		listView.setItems(items);

		listView.setOnMouseClicked(event -> {
			String selectedHoodie = listView.getSelectionModel().getSelectedItem();
			if (selectedHoodie != null) {
				HoodieTable selectedHoodieData = hoodieData.get(selectedHoodie);
				if (selectedHoodieData != null) {
					idLbl.setText("Hoodie ID: " + selectedHoodieData.getId());
					nameLbl.setText("Name: " + selectedHoodieData.getName());
					priceLbl.setText("Price: $" + selectedHoodieData.getPrice());

					int initialQuantity = spinner.getValue();
					double price = selectedHoodieData.getPrice();
					double initialTotalPrice = initialQuantity * price;
					totalPriceLbl.setText("Total Price: $" + String.format("%.2f", initialTotalPrice));

					spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
						double totalPrice = newValue * price;
						totalPriceLbl.setText("Total Price: $" + String.format("%.2f", totalPrice));
					});
				}
			}
		});

		cartBtn.setOnAction(event -> {
			String selectedHoodie = listView.getSelectionModel().getSelectedItem();
			if (selectedHoodie != null && spinner.getValue() != null) {
				HoodieTable selectedHoodieData = hoodieData.get(selectedHoodie);
				int quantity = spinner.getValue();

				addToCart(selectedHoodieData.getId(), quantity);
			} else {
				System.out.println("No hoodie selected or invalid quantity.");
			}
		});
		
		  cartBtn.setOnAction(event -> {
		        String selectedHoodie = listView.getSelectionModel().getSelectedItem();
		        if (selectedHoodie != null && spinner.getValue() != null) {
		            HoodieTable selectedHoodieData = hoodieData.get(selectedHoodie);
		            int selectedQuantity = spinner.getValue();

		            addToCart(selectedHoodieData.getId(), selectedQuantity);

		            switchToCartScene();
		        } else {
		            System.out.println("No hoodie selected or invalid quantity.");
		        }
		    });
	}

	private String getCurrentUserId() {
		return SessionManager.getInstance().getCurrentUserId();
	}

	public void addToCart(String hoodieId, int quantity) {
		Connect connect = Connect.getInstance();
		String userId = getCurrentUserId();
		if (userId == null || userId.isEmpty()) {
			System.out.println("User ID is null or empty. User must be logged in to add items to the cart.");
			return;
		}

		String checkSql = "SELECT Quantity FROM cart WHERE UserID = ? AND HoodieID = ?";
		try (PreparedStatement checkStmt = connect.con.prepareStatement(checkSql)) {
			checkStmt.setString(1, userId);
			checkStmt.setString(2, hoodieId);
			try (ResultSet rs = checkStmt.executeQuery()) {
				if (rs.next()) {
					int existingQuantity = rs.getInt("Quantity");
					int newQuantity = existingQuantity + quantity;
					String updateSql = "UPDATE cart SET Quantity = ? WHERE UserID = ? AND HoodieID = ?";
					try (PreparedStatement updateStmt = connect.con.prepareStatement(updateSql)) {
						updateStmt.setInt(1, newQuantity);
						updateStmt.setString(2, userId);
						updateStmt.setString(3, hoodieId);
						updateStmt.executeUpdate();
						showSuccessAlert(hoodieId, newQuantity);
					}
				} else {
					String insertSql = "INSERT INTO cart (UserID, HoodieID, Quantity) VALUES (?, ?, ?)";
					try (PreparedStatement insertStmt = connect.con.prepareStatement(insertSql)) {
						insertStmt.setString(1, userId);
						insertStmt.setString(2, hoodieId);
						insertStmt.setInt(3, quantity);
						insertStmt.executeUpdate();
						showSuccessAlert(hoodieId, quantity);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void switchToCartScene() {
	    CartPage cartPage = new CartPage(SessionManager.getInstance(), Connect.getInstance());
	    Scene cartScene = cartPage.createCartScene();
	    
	    Stage stage = (Stage) cartBtn.getScene().getWindow();
	    
	    stage.setScene(cartScene);
	}

	private void showSuccessAlert(String hoodieId, int quantity) {
		Platform.runLater(() -> {
			Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
			successAlert.setTitle("Success");
			successAlert.setHeaderText("Message");
			successAlert.setContentText("Hoodie " + hoodieId + " - " + quantity + "x added");
			successAlert.showAndWait();
		});
	} 

	public static boolean tableExists(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });
		return resultSet.next();
	}

	public Scene createHomeScene() {
		initHome();
		styleHome();

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(vbox);

		return new Scene(borderPane, 850, 789);
	}
}
