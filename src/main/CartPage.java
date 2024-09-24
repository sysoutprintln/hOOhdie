// CartPage class
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connect.Connect;

import session.SessionManager;
import session.TransactionManager;
import table.UserTable;
import table.CartTable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartPage<Cart> {
	
	private SessionManager sessionManager;
    private Connect databaseConnection;
    private Label emailLabel, phoneLabel, addressLabel, totalPriceLabel;

	private TableView<CartTable> cartTableView = new TableView<>();

	private Label userCartLbl, hdetailLbl, selectItemLbl, idLbl, nameLbl, priceLbl, qtyLbl, totalPriceLbl, 
			conInfoLbl,
			cartTotalLbl;

	private Button removeBtn, checkOutBtn;

	private Pane pane;
	public MenuBar menuCart;
	public Menu menuAccount, menuUser;

	private GridPane gp, detailsPane, prodPane;
	private HBox hbox;
	public VBox vbox;
	
	public CartPage(SessionManager sessionManager, Connect databaseConnection) {
	        this.sessionManager = sessionManager;
	        this.databaseConnection = databaseConnection;
	        initTable();
	    }

	    public void initCart() {
	    	
	    	String currentUserName = sessionManager.getCurrentUserName();
	        userCartLbl = new Label(currentUserName + "'s Cart");
	    	userCartLbl.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px; -fx-font-style: italic;");
	    	
	    	hdetailLbl = new Label("Hoodie's Detail");
	    	hdetailLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 32px; -fx-font-style: italic;");
	    	
	    	selectItemLbl = new Label("Select an item from the list...");
	    	selectItemLbl.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px;");
	    	
			idLbl = new Label("Hoodie ID: ");
			nameLbl = new Label("Name: ");
			priceLbl = new Label("Price: ");
			qtyLbl = new Label("Quantity: ");
			totalPriceLbl = new Label("Cart's Total Price: ");
			
			removeBtn = new Button("Remove from Cart");
			removeBtn.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px;");
			
			idLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
			nameLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
			priceLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
			qtyLbl.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");
			totalPriceLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Times New Roman';");
			
			conInfoLbl = new Label("Contact Information");
			conInfoLbl.setStyle("-fx-font-weight: bold;  -fx-font-size: 32px; -fx-font-style: italic;");
			
			cartTotalLbl = new Label("Cart's Total Price: ");
			cartTotalLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 32px; -fx-font-style: italic;");
			
			checkOutBtn = new Button("Checkout");
			checkOutBtn.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 14px;");
			checkOutBtn.setOnAction(this::checkout);
			
	        // Set Visibility
	        idLbl.setVisible(false);
	        nameLbl.setVisible(false);
	        priceLbl.setVisible(false);
	        qtyLbl.setVisible(false);
	        totalPriceLbl.setVisible(false);
	        removeBtn.setVisible(false);

	        cartTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	            if (newSelection != null) {

	            	selectItemLbl.setVisible(false);
	            	
	                idLbl.setVisible(true);
	                nameLbl.setVisible(true);
	                priceLbl.setVisible(true);
	                qtyLbl.setVisible(true);
	                totalPriceLbl.setVisible(true);
	                removeBtn.setVisible(true);
	                
	                CartTable selectedItem = newSelection;

	                idLbl.setText("Hoodie ID: " + selectedItem.getHoodieID());
	                nameLbl.setText("Name: " + selectedItem.getHoodieName());
	                priceLbl.setText("Price: " + String.format("$%.2f", selectedItem.getTotalPrice()));
	                qtyLbl.setText("Quantity: " + String.format("%d", selectedItem.getQuantity()));
	                totalPriceLbl.setText("Cart's Total Price: " + String.format("$%.2f", selectedItem.getTotalPrice()));
	                
	                cartTotalLbl.setText(String.format("Cart's Total Price: $%.2f", calculateTotalPrice()));

	            }
	        });
	        
	        removeBtn.setOnAction(event -> {
	            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
	            removeFromCart(actionEvent);
	        });
	        
		    hbox = new HBox();
	        hbox.getChildren().addAll(userCartLbl, hdetailLbl);

	        // Menu bar
	        menuCart = new MenuBar();
	        pane = new Pane();
			pane.getChildren().add(menuCart);

	        menuAccount = new Menu("Account");
	        menuUser = new Menu("User");
	        menuCart.getMenus().addAll(menuAccount, menuUser);
	        
			MenuItem logoutItem = new MenuItem("Logout");
			logoutItem.setOnAction(e -> {
			    LoginPage loginPage = new LoginPage();
			    Scene loginScene = loginPage.createLoginScene();
			    
			    Stage currentStage = (Stage) menuCart.getScene().getWindow();
			    currentStage.setScene(loginScene);
			});
			menuAccount.getItems().add(logoutItem);
			
			MenuItem homeItem = new MenuItem("Home");
			homeItem.setOnAction(e -> {
				HomePage homePage = new HomePage();
				Scene homeScene =  homePage.createHomeScene();
				
				Stage currentStage = (Stage) menuCart.getScene().getWindow();
				currentStage.setScene(homeScene);
			});
			menuUser.getItems().add(homeItem);
			
			MenuItem cartItem = new MenuItem("Cart");
			cartItem.setOnAction(e -> {
				SessionManager sessionManager = SessionManager.getInstance();
				Connect databaseConnection = Connect.getInstance();
				CartPage cartPage = new CartPage(sessionManager, databaseConnection);
				Scene cartScene =  cartPage.createCartScene();
				
				Stage currentStage = (Stage) menuCart.getScene().getWindow();
				currentStage.setScene(cartScene);
			});
			menuUser.getItems().add(cartItem);
			
			MenuItem historyItem = new MenuItem("History");
			historyItem.setOnAction(e -> {
				HistoryPage historyPage = new HistoryPage();
				Scene historyScene = historyPage.createHistoryScene();
				
				Stage currentStage = (Stage) menuCart.getScene().getWindow();
				currentStage.setScene(historyScene);
			});
			menuUser.getItems().add(historyItem);
	        
			
	    }
		
	    public void styleCart() {

		    gp = new GridPane();
		    gp.setHgap(5);
		    gp.setVgap(5);
		    gp.setPadding(new Insets(5, 5, 25, 25));

		    detailsPane = new GridPane();
		    detailsPane.setVgap(5);
		    detailsPane.setHgap(5);

		    detailsPane.add(hdetailLbl, 0, 0);
		    GridPane.setMargin(hdetailLbl, new Insets(0, 0, 10, 0));
		    detailsPane.add(selectItemLbl, 0, 1);
		    detailsPane.add(idLbl, 0, 1);
		    detailsPane.add(nameLbl, 0, 2);
		    detailsPane.add(priceLbl, 0, 3);
		    detailsPane.add(qtyLbl, 0, 4);
		    detailsPane.add(totalPriceLbl, 0, 5);
		    detailsPane.add(removeBtn, 0, 7);
		    
		    detailsPane.add(conInfoLbl, 0, 9);
		    detailsPane.add(emailLabel, 0, 10);
		    detailsPane.add(phoneLabel, 0, 11);
		    detailsPane.add(addressLabel, 0, 12);
		    
		    detailsPane.add(cartTotalLbl, 0, 13);
		    detailsPane.add(checkOutBtn, 0, 14);
		    GridPane.setHalignment(checkOutBtn, HPos.RIGHT);
		    
		    prodPane = new GridPane();
		    prodPane.setVgap(5);
		    prodPane.setHgap(25);

		    prodPane.add(userCartLbl, 0, 0);
		    prodPane.add(cartTableView, 0, 1);

		    gp.add(prodPane, 0, 1);
		    gp.add(detailsPane, 1, 1);
		    
		    GridPane.setMargin(prodPane, new Insets(0, 0, 0, 25));
		    GridPane.setMargin(detailsPane, new Insets(25, 20, 20, 40));
		    
		    vbox = new VBox(menuCart, gp);
		    vbox.setAlignment(Pos.TOP_LEFT);
		    vbox.setPadding(new Insets(5));
		    vbox.setSpacing(5);
	    }

	@SuppressWarnings("unchecked")
	public void initTable() {
		
        TableColumn<CartTable, String> hoodieIdColumn = new TableColumn<>("Hoodie ID");
        TableColumn<CartTable, String> hoodieNameColumn = new TableColumn<>("Hoodie Name");
        TableColumn<CartTable, Integer> quantityColumn = new TableColumn<>("Quantity");
        TableColumn<CartTable, Double> totalPriceColumn = new TableColumn<>("Total Price");
        
        hoodieIdColumn.setCellValueFactory(new PropertyValueFactory<>("HoodieID"));
        hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<>("HoodieName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
        
        cartTableView.setPrefSize(340, 600);

        cartTableView.getColumns().addAll(hoodieIdColumn, hoodieNameColumn, quantityColumn, totalPriceColumn);
        this.cartTableView.getColumns().forEach(column -> column.setMinWidth(85));
        
        emailLabel = new Label();
        phoneLabel = new Label();
        addressLabel = new Label();
        totalPriceLabel = new Label();
        
        loadCartItems();
        loadUserContactInfo();
	}
	
	private void loadCartItems() {
	    if (sessionManager == null) {
	        showAlert("Error", "Session manager is not initialized.");
	        return;
	    }
	    
	    String currentUserRole = sessionManager.getCurrentUserRole();
	    if (currentUserRole == null) {
	        showAlert("Error", "Unable to determine user role.");
	        return;
	    }
	    
	    if (!currentUserRole.equals("User")) {
	        showAlert("Access Denied", "You do not have permission to access the cart page.");
	        return;
	    }
	    
        String currentUserId = sessionManager.getCurrentUserId();
        
        System.out.println(currentUserId);
        cartTableView.getItems().addAll(databaseConnection.getCartItems(currentUserId));
        
        calculateTotalPrice();
    }

	private void loadUserContactInfo() {
	    System.out.println("Loading user contact info...");

	    if (databaseConnection == null) {
	        showAlert("Error", "Database connection is not initialized.");
	        return;
	    }

	    if (sessionManager == null || sessionManager.getCurrentUserId() == null) {
	        showAlert("Error", "Session manager is not initialized or no user is logged in.");
	        return;
	    }

	    UserTable currentUser = databaseConnection.getUserInfo(sessionManager.getCurrentUserId());

	    if (currentUser == null) {
	        showAlert("Error", "Current user data is not available.");
	        return;
	    }

	    System.out.println("Email: " + currentUser.getEmail());
	    System.out.println("Phone: " + currentUser.getPhoneNumber());
	    System.out.println("Address: " + currentUser.getAddress());

	    emailLabel.setText("Email: " + currentUser.getEmail());
	    phoneLabel.setText("Phone: " + currentUser.getPhoneNumber());
	    addressLabel.setText("Address: " + currentUser.getAddress());
	}

    private double calculateTotalPrice() {
        double totalPrice = cartTableView.getItems().stream()
            .mapToDouble(item -> item.getTotalPrice())
            .sum();
        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
		return totalPrice;
    }
    
    public void removeFromCart(ActionEvent actionEvent) {
        CartTable selectedItem = cartTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert("Error", "Please select an item to remove from the cart.");
            return;
        }

        if (!databaseConnection.cartItemExists(selectedItem)) {
        	
            showAlert("Error", "The selected item is not present in the cart.");
            return;
        }
        
        boolean isRemoved = databaseConnection.removeFromCart(selectedItem);

        if (isRemoved) {
            cartTableView.getItems().remove(selectedItem);
            cartTableView.refresh();
            calculateTotalPrice();

            showAlert("Success", "Item successfully removed from your cart.");
        } else {
            showAlert("Error", "Failed to remove item from the cart. Please try again later.");
        }
    }

    public void checkout(ActionEvent event) {
        if (cartTableView.getItems().isEmpty()) {
            showAlert("Error", "Your cart is empty. Please add items to your cart before checking out.");
        } else {
            Confirm popup = new Confirm();
            int transactionIndex = new TransactionManager().getTransactionIndex();
            String userID = sessionManager.getCurrentUserId();
            popup.ConfirmationPopup(transactionIndex, userID);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
	
	public Scene createCartScene() {
	    initCart();
	    styleCart();
	    
	    BorderPane borderPane = new BorderPane();
	    borderPane.setCenter(vbox);
	    
	    return new Scene(borderPane, 1000, 789);
	}

}