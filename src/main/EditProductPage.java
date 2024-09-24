// EditProductStage class
package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import connect.Connect;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import session.SessionManager;
import table.CartTable;
import table.HoodieTable;

public class EditProductPage {

	private TableView<HoodieTable> hoodieTableView = new TableView<>();
	
	Scene scene;

	private Label editprdLbl, updatedeleteLbl, selectItemLbl, 
			hidLbl, hnameLbl, priceLbl, 
			inserthLbl, 
			insnameLbl, inspriceLbl;

	private TextField priceTf, nameTf, inspriceTf;

	private Button updateBtn, deleteBtn, insertBtn;

	public BorderPane bp, leftPane, rightPane, pricePane, btnPane, namePane, inspricePane;
	private GridPane leftGrid, rightGrid;
	private HBox hbox, btnBox, priceBox, nameBox, inspriceBox;

	public MenuBar menuEdit;
	private Pane pane;
	private Menu menuAcc, menuAdmin;

	public void initEdit() {

		// EDIT PRODUCT
		editprdLbl = new Label("Edit Product");
		editprdLbl.setStyle("-fx-font-style: italic; -fx-font-size: 40px; -fx-font-family: 'Times New Roman'");

		// UPDATE & DELETE HOODIES
		updatedeleteLbl = new Label("Update & Delete Hoodie(s)");
		updatedeleteLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 30px;");

		selectItemLbl = new Label("Select an item from the list...");
		selectItemLbl.setStyle("-fx-font-size: 15px; -fx-font-family: 'Times New Roman'");

		hidLbl = new Label("Hoodie ID: ");
		hidLbl.setStyle("-fx-font-size: 15px; -fx-font-family: 'Times New Roman';");

		hnameLbl = new Label("Name: ");
		hnameLbl.setStyle("-fx-font-size: 15px; -fx-font-family: 'Times New Roman';");

		priceLbl = new Label("Price:   ");
		priceLbl.setStyle("-fx-font-size: 15px; -fx-font-family: 'Times New Roman';");

		priceTf = new TextField();
		priceTf.setPromptText("Hoodie's price");

		updateBtn = new Button("Update Price");
		updateBtn.setStyle("-fx-font-size: 13px; -fx-font-family: 'Times New Roman'; -fx-font-weight: bold;");
		updateBtn.setMinWidth(105);
		updateBtn.setMinHeight(20);

		deleteBtn = new Button("Delete Hoodie");
		deleteBtn.setStyle("-fx-font-size: 13px; -fx-font-family: 'Times New Roman'; -fx-font-weight: bold;");
		deleteBtn.setMinWidth(105);
		deleteBtn.setMinHeight(20);

		// INSERT HOODIE
		inserthLbl = new Label("Insert Hoodie");
		inserthLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 30px;");

		insnameLbl = new Label("Name:  ");
		nameTf = new TextField();
		nameTf.setPromptText("Input name");

		inspriceLbl = new Label("Price:    ");
		inspriceTf = new TextField();
		inspriceTf.setPromptText("Input price");

		insertBtn = new Button("Insert");
		insertBtn.setStyle("-fx-font-size: 13px; -fx-font-family: 'Times New Roman'; -fx-font-weight: bold;");
		insertBtn.setMinWidth(125);
		insertBtn.setMinHeight(20);
		HBox.setMargin(insertBtn, new Insets(500, 20, 10, 40));

		// SET VISIBILITY
		hidLbl.setVisible(false);
		hnameLbl.setVisible(false);
		priceLbl.setVisible(false);
		priceTf.setVisible(false);
		updateBtn.setVisible(false);
		deleteBtn.setVisible(false);

		hoodieTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
			        // A product is selected
			        selectItemLbl.setVisible(false);
			        hidLbl.setVisible(true);
			        hnameLbl.setVisible(true);
			        priceLbl.setVisible(true);
			        priceTf.setVisible(true);
			        updateBtn.setVisible(true);
			        deleteBtn.setVisible(true);
			        
			        HoodieTable selectedItem = newSelection;
			        
			        hidLbl.setText("Hoodie ID: " + selectedItem.getId());
			        hnameLbl.setText("Name: " + selectedItem.getName());
			        priceTf.setText(String.valueOf(selectedItem.getPrice()));
			    
		}
			
			// Set the onAction event handler for the updateBtn
			updateBtn.setOnAction(event -> {
			    // Get the selected hoodie from the TableView
			    HoodieTable selectedHoodie = hoodieTableView.getSelectionModel().getSelectedItem();

			    if (selectedHoodie != null) {
			        // Get the new price from the text field
			        double newPrice = Double.parseDouble(priceTf.getText());

			        // Update the price of the hoodie in the database
			        Connect connect = Connect.getInstance();
			        String sql = "UPDATE hoodie SET HoodiePrice = ? WHERE HoodieID = ?";
			        try (PreparedStatement pstmt = connect.con.prepareStatement(sql)) {
			            pstmt.setDouble(1, newPrice);
			            pstmt.setString(2, selectedHoodie.getId());
			            pstmt.executeUpdate();
			        } catch (SQLException e) {
			            e.printStackTrace();
			        }

			        // Update the price of the hoodie in the TableView
			        selectedHoodie.setPrice(newPrice);
			        hoodieTableView.refresh();
			    }
			});

			// Set the onAction event handler for the deleteBtn
			deleteBtn.setOnAction(event -> {
			    // Get the selected hoodie from the TableView
			    HoodieTable selectedHoodie = hoodieTableView.getSelectionModel().getSelectedItem();

			    if (selectedHoodie != null) {
			        // Delete the hoodie from the database
			        Connect connect = Connect.getInstance();
			        String sql = "DELETE FROM hoodie WHERE HoodieID = ?";
			        try (PreparedStatement pstmt = connect.con.prepareStatement(sql)) {
			            pstmt.setString(1, selectedHoodie.getId());
			            pstmt.executeUpdate();
			        } catch (SQLException e) {
			            e.printStackTrace();
			        }

			        // Remove the hoodie from the TableView
			        hoodieTableView.getItems().remove(selectedHoodie);
			    }
			});
			
			insertBtn.setOnAction(event -> {
			    // Get the name and price of the new hoodie from the text fields
			    String name = nameTf.getText();
			    double price = Double.parseDouble(inspriceTf.getText());

			    // Get the maximum existing ID from the database
			    String sqlMaxId = "SELECT MAX(HoodieID) AS MaxID FROM hoodie";
			    Connect connect = Connect.getInstance();
				ResultSet resultSetMaxId = connect.execQuery(sqlMaxId);
			    String maxId = "";
			    try {
					if (resultSetMaxId.next()) {
					    maxId = resultSetMaxId.getString("MaxID");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			    // Generate a new ID for the hoodie
			    int index = Integer.parseInt(maxId.substring(2)) + 1;
			    String id = String.format("HO%03d", index);

			    // Create a new HoodieTable object
			    HoodieTable newHoodie = new HoodieTable();
			    newHoodie.setId(id);
			    newHoodie.setName(name);
			    newHoodie.setPrice(price);

			    // Insert the new hoodie into the database
			    connect = Connect.getInstance();
			    String sql = "INSERT INTO hoodie (HoodieID, HoodieName, HoodiePrice) VALUES (?, ?, ?)";
			    try (PreparedStatement pstmt = connect.con.prepareStatement(sql)) {
			        pstmt.setString(1, id);
			        pstmt.setString(2, name);
			        pstmt.setDouble(3, price);
			        pstmt.executeUpdate();
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }

			    // Add the new hoodie to the TableView
			    hoodieTableView.getItems().add(newHoodie);

			    // Clear the text fields
			    nameTf.clear();
			    inspriceTf.clear();
			});
		});

		// MENU BAR
		menuEdit = new MenuBar();
		pane = new Pane();
		pane.getChildren().add(menuEdit);

		menuAcc = new Menu("Account");
		menuAdmin = new Menu("Admin");
		menuEdit.getMenus().addAll(menuAcc, menuAdmin);

//		menuAcc .getItems().add(new MenuItem("Logout"));
//		menuAdmin.getItems().addAll(new MenuItem("Edit Product"));

		MenuItem logoutItem = new MenuItem("Logout");
		logoutItem.setOnAction(e -> {
		    // Create the LoginPage instance and get the login scene
		    LoginPage loginPage = new LoginPage();
		    Scene loginScene = loginPage.createLoginScene();

		    // Get the current stage and set the scene to the login page
		    Stage currentStage = (Stage) menuEdit.getScene().getWindow();
		    currentStage.setScene(loginScene);
		});
		menuAcc.getItems().add(logoutItem);
		
		MenuItem editProductItem = new MenuItem("Edit Product");
		editProductItem.setOnAction(e -> {
		    // Create the EditProductPage instance and get the edit product scene
		    EditProductPage editProductPage = new EditProductPage();
		    Scene editProductScene = editProductPage.createEditProductPage();

		    // Get the current stage and set the scene to the edit product page
		    Stage currentStage = (Stage) menuEdit.getScene().getWindow();
		    currentStage.setScene(editProductScene);
		});
		menuAdmin.getItems().add(editProductItem);
		
	    populateTableView();

	}
	
	public void styleEdit() {

			// LEFT BORDERPANE
			leftPane = new BorderPane();
			leftGrid = new GridPane();
			leftGrid.add(editprdLbl, 0, 0);
			leftGrid.add(hoodieTableView, 0, 1);
			leftPane.setCenter(leftGrid);

			// RIGHT BORDERPANE
			// FIRST SECTION
			rightPane = new BorderPane();
			rightGrid = new GridPane();
			rightGrid.setVgap(10);
			rightGrid.add(updatedeleteLbl, 0, 0);
			rightGrid.add(selectItemLbl, 0, 1);
			rightGrid.add(hidLbl, 0, 1);
			rightGrid.add(hnameLbl, 0, 2);

			pricePane = new BorderPane();
			pricePane.setLeft(priceLbl);
			pricePane.setCenter(priceTf);
			rightGrid.add(pricePane, 0, 3);

			btnBox = new HBox();
			btnBox.setSpacing(10);
			btnBox.getChildren().addAll(updateBtn, deleteBtn);
			rightGrid.add(btnBox, 0, 4);
			
			// SECOND SECTION
			rightGrid.add(inserthLbl, 0, 5);
			inserthLbl.setPadding(new Insets(15, 0, 0, 0));

			namePane = new BorderPane();
			namePane.setLeft(insnameLbl);
			namePane.setCenter(nameTf);
			rightGrid.add(namePane, 0, 6);

			inspricePane = new BorderPane();
			inspricePane.setLeft(inspriceLbl);
			inspricePane.setCenter(inspriceTf);
			rightGrid.add(inspricePane, 0, 7);
			rightGrid.add(insertBtn, 0, 8);
			rightPane.setCenter(rightGrid);
			
			// BUTTON
			priceBox = new HBox();
			priceTf.setPrefWidth(200);
			priceBox.getChildren().add(priceTf);
			pricePane.setCenter(priceBox);

			nameBox = new HBox();
			nameTf.setPrefWidth(125);
			nameBox.getChildren().add(nameTf);
			namePane.setCenter(nameBox);

			inspriceBox = new HBox();
			inspriceTf.setPrefWidth(125);
			inspriceBox.getChildren().add(inspriceTf);
			inspricePane.setCenter(inspriceBox);
			
			// RIGHT GRID PANE PADDING
			rightGrid.setPadding(new Insets(50, 0, 0, 0));

			// INSERT ALL PANE(S) INTO A BORDERPANE
			hbox = new HBox(leftPane, rightPane);
			hbox.setSpacing(45);

			bp = new BorderPane();
			bp.setCenter(hbox);
			bp.setPadding(new Insets(50, 90, 30, 90));
	}

	@SuppressWarnings("unchecked")
	public void initHoodieTable() {
	    TableColumn<HoodieTable, String> hoodieIDColumn = new TableColumn<>("Hoodie ID");
	    hoodieIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<HoodieTable, String> hoodieNameColumn = new TableColumn<>("Hoodie Name");
	    hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

	    TableColumn<HoodieTable, Double> hoodiePriceColumn = new TableColumn<>("Hoodie Price");
	    hoodiePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

	    hoodieTableView.setPrefSize(360, 550);

	    this.hoodieTableView.getColumns().addAll(hoodieIDColumn, hoodieNameColumn, hoodiePriceColumn);
	    this.hoodieTableView.getColumns().forEach(column -> column.setMinWidth(120));
	}

	public void populateTableView() {
	    // RETRIEVE DATA FROM THE DATABASE
	    Connect connect = Connect.getInstance();
	    String sql = "SELECT HoodieID, HoodieName, HoodiePrice FROM hoodie";
	    ResultSet resultSet = connect.execQuery(sql);
	    try {
	        while (resultSet.next()) {
	            HoodieTable hoodie = new HoodieTable();
	            hoodie.setId(resultSet.getString("HoodieID"));
	            hoodie.setName(resultSet.getString("HoodieName"));
	            hoodie.setPrice(resultSet.getDouble("HoodiePrice"));

	            // ADD THE HOODIE INTO THE TABLEVIEW
	            hoodieTableView.getItems().add(hoodie);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	public Scene createEditProductPage() {
		initEdit();
		styleEdit();
		
		initHoodieTable();
		
		BorderPane borderPane = new BorderPane();
	    borderPane.setTop(menuEdit);
		borderPane.setCenter(bp);
		
		return new Scene(borderPane, 970, 750);
	}

}
