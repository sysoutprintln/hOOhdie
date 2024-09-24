// HistoryStage class
package main;

import java.util.List;

import connect.Connect;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import session.SessionManager;
import session.TransactionManager;
import table.TransactionHeader;
import table.TransactionDetail;

public class HistoryPage {
	
	private Label transLbl,
		selectTransLbl,
		transdtlLbl, totalPriceLbl;
	
	private TableView<TransactionHeader> transactionheader = new TableView<>();
	private TableView<TransactionDetail> transactiondetail = new TableView<>();
	
	public VBox leftVBox, rightVBox, vbox;
	private HBox hbox;
	public BorderPane bp, bproot;
	public GridPane gp, leftgp, rightgp;
	
	MenuBar menuHs;
	Pane pane;
	Menu menuAccount, menuUser;
	
	public void initHistory() {
		
		transLbl = new Label("[Username's]'s Transaction(s)");
		transLbl.setStyle("-fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 25px;");
		
		selectTransLbl = new Label("(Select a Transaction)");
		selectTransLbl.setStyle("-fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 25px;");
		
		transdtlLbl = new Label("[TransactionID]'s Transaction Detail(s)");
		transdtlLbl.setStyle("-fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 25px;");
		
		totalPriceLbl = new Label("Total Price: ");
		totalPriceLbl.setStyle("-fx-font-weight: bold; -fx-font-family: 'Times New Roman'; -fx-font-size: 20px;");
		totalPriceLbl.setAlignment(Pos.BOTTOM_RIGHT);
		
        // Set Visibility
		transdtlLbl.setVisible(false);
		transactiondetail.setVisible(false);
		totalPriceLbl.setVisible(false);

		transactionheader.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
            	
            	transdtlLbl.setVisible(true);
        		transactiondetail.setVisible(true);
        		totalPriceLbl.setVisible(true);

            }
        });
		
        // Menu bar
        menuHs = new MenuBar();
        pane = new Pane();
		pane.getChildren().add(menuHs);

        menuAccount = new Menu("Account");
        menuUser = new Menu("User");
        menuHs.getMenus().addAll(menuAccount, menuUser);
        
		MenuItem logoutItem = new MenuItem("Logout");
		logoutItem.setOnAction(e -> {
		    LoginPage loginPage = new LoginPage();
		    Scene loginScene = loginPage.createLoginScene();
		    
		    Stage currentStage = (Stage) menuHs.getScene().getWindow();
		    currentStage.setScene(loginScene);
		});
		menuAccount.getItems().add(logoutItem);
		
		MenuItem homeItem = new MenuItem("Home");
		homeItem.setOnAction(e -> {
			HomePage homePage = new HomePage();
			Scene homeScene =  homePage.createHomeScene();
			
			Stage currentStage = (Stage) menuHs.getScene().getWindow();
			currentStage.setScene(homeScene);
		});
		menuUser.getItems().add(homeItem);
		
		MenuItem cartItem = new MenuItem("Cart");
		cartItem.setOnAction(e -> {
			SessionManager sessionManager = SessionManager.getInstance();
			Connect databaseConnection = Connect.getInstance();
			CartPage cartPage = new CartPage(sessionManager, databaseConnection);
			Scene cartScene =  cartPage.createCartScene();
			
			Stage currentStage = (Stage) menuHs.getScene().getWindow();
			currentStage.setScene(cartScene);
		});
		menuUser.getItems().add(cartItem);
		
		MenuItem historyItem = new MenuItem("History");
		historyItem.setOnAction(e -> {
			HistoryPage historyPage = new HistoryPage();
			Scene historyScene = historyPage.createHistoryScene();
			
			Stage currentStage = (Stage) menuHs.getScene().getWindow();
			currentStage.setScene(historyScene);
		});
		menuUser.getItems().add(historyItem);
		
	}
	
	public void styleHistory() {
		
	    leftgp = new GridPane();
	    leftgp.setVgap(15);
	    leftgp.add(transLbl, 0, 0);
	    leftgp.add(transactionheader, 0, 1);

	    rightgp = new GridPane();
	    rightgp.setVgap(15);
	    rightgp.add(transdtlLbl, 0, 0);
	    rightgp.add(selectTransLbl, 0, 0);
	    rightgp.add(transactiondetail, 0, 1);
	    rightgp.add(totalPriceLbl, 0, 2);
	    GridPane.setHalignment(totalPriceLbl, HPos.RIGHT);
	    
	    hbox = new HBox(leftgp, rightgp);
	    hbox.setSpacing(25);

	    bp = new BorderPane();
	    bp.setCenter(hbox);
	    bp.setPadding(new Insets(90, 30, 30, 70));
	    
	    vbox = new VBox(menuHs, hbox);
	    vbox.setAlignment(Pos.TOP_LEFT);
	    vbox.setPadding(new Insets(5));
	    vbox.setSpacing(5);
	}
	
    @SuppressWarnings("unchecked")
	public void initTransactionHeader() {
		
		TableColumn<TransactionHeader, String> transIDColumn = new TableColumn<>("Transaction ID");
	    transIDColumn.setCellValueFactory(new PropertyValueFactory<>("TransactionID"));

	    TableColumn<TransactionHeader, String> userIDColumn = new TableColumn<>("User ID");
	    userIDColumn.setCellValueFactory(new PropertyValueFactory<>("UserID"));
	    
	    transactionheader.setPrefSize(100, 450);
	    
	    this.transactionheader.getColumns().addAll(transIDColumn, userIDColumn);
        this.transactionheader.getColumns().forEach(column -> column.setMinWidth(175));
	}
    
    @SuppressWarnings("unchecked")
	public void initTransactionDetail() {
		
		TableColumn<TransactionDetail, String> transIDColumn = new TableColumn<>("Transaction ID");
		transIDColumn.setCellValueFactory(new PropertyValueFactory<>("TransactionID"));
		
		TableColumn<TransactionDetail, String> hoodieIDColumn = new TableColumn<>("Hoodie ID");
	    hoodieIDColumn.setCellValueFactory(new PropertyValueFactory<>("HoodieID"));
	    
	    TableColumn<TransactionDetail, String> hoodieNameColumn = new TableColumn<>("Hoodie Name");
	    hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<>("HoodieName"));
	    
	    TableColumn<TransactionDetail, Integer> quantityColumn = new TableColumn<>("Quantity");
	    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
	    
	    TableColumn<TransactionDetail, Double> totalPriceColumn = new TableColumn<>("Total Price");
	    totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
	    
	    transactiondetail.setPrefSize(100, 600);
	    
	    this.transactiondetail.getColumns().addAll(transIDColumn, hoodieIDColumn, hoodieNameColumn, quantityColumn, totalPriceColumn);
        this.transactiondetail.getColumns().forEach(column -> column.setMinWidth(90));
	}

	public Scene createHistoryScene() {
		initHistory();
		styleHistory();
		initTransactionDetail();
		initTransactionHeader();
		
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(20);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menuHs);
		borderPane.setCenter(hbox);
		
		return new Scene(borderPane, 970, 750);
	}
	
	public void initTransactionTable() {
	    TableColumn<TransactionHeader, String> transactionIdColumn = new TableColumn<>("Transaction ID");
	    transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));

	    TableColumn<TransactionHeader, String> userIdColumn = new TableColumn<>("User ID");
	    userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

	    transactionheader.getColumns().addAll(transactionIdColumn, userIdColumn);

	    TransactionManager transactionManager = new TransactionManager();
	    String currentUserId = SessionManager.getInstance().getCurrentUserId();
	    List<Transaction> transactions = transactionManager.getTransactionsByUserID(currentUserId);
	    ObservableList<TransactionHeader> transactionHeaders = FXCollections.observableArrayList();

	    for (Transaction transaction : transactions) {
	        TransactionHeader transactionHeader = new TransactionHeader(transaction);
	        transactionHeaders.add(transactionHeader);
	    }

	    transactionheader.setItems(transactionHeaders);

	    transactionheader.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	            displayTransactionDetails(newSelection.getTransactionID());
	        } else {
	            transdtlLbl.setVisible(false);
	            transactiondetail.setVisible(false);
	            totalPriceLbl.setVisible(false);
	        }
	    });
	}
	
	public void displayTransactionDetails(String transactionID) {
	    transactiondetail.getItems().clear();

	    TransactionManager transactionManager = new TransactionManager();
	    List<TransactionDetail> details = transactionManager.getTransactionDetails(transactionID);

	    ObservableList<TransactionDetail> detailItems = FXCollections.observableArrayList(details);
	    transactiondetail.setItems(detailItems);

	    double totalPrice = details.stream().mapToDouble(detail -> detail.getProductPrice() * detail.getQuantity()).sum();
	    totalPriceLbl.setText("Total Price: " + totalPrice);

	    transdtlLbl.setVisible(true);
	    transactiondetail.setVisible(true);
	    totalPriceLbl.setVisible(true);
	}
	
	public void initTransactionDetailTable() {
	    TableColumn<TransactionDetail, String> transactionIdColumn = new TableColumn<>("Transaction ID");
	    transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

	    TableColumn<TransactionDetail, String> hoodieIdColumn = new TableColumn<>("Hoodie ID");
	    hoodieIdColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));

	    TableColumn<TransactionDetail, String> hoodieNameColumn = new TableColumn<>("Hoodie Name");
	    hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

	    TableColumn<TransactionDetail, Integer> quantityColumn = new TableColumn<>("Quantity");
	    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

	    TableColumn<TransactionDetail, Double> totalPriceColumn = new TableColumn<>("Total Price");
	    totalPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(
	        cellData.getValue().getProductPrice() * cellData.getValue().getQuantity()
	    ).asObject());

	    transactiondetail.getColumns().addAll(transactionIdColumn, hoodieIdColumn, hoodieNameColumn, quantityColumn, totalPriceColumn);
	}
}