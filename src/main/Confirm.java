// Confirm class
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import connect.Connect;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import session.TransactionManager;
import main.HistoryPage;

public class Confirm {
	public final Stage stage = new Stage();
	
	private final BorderPane bproot = new BorderPane();
	private final BorderPane bp = new BorderPane();
	
	private final GridPane gptop = new GridPane();
	private final GridPane gpcenter = new GridPane();
	
	private final Label titleLbl = new Label("Payment Confirmation");
	public final Button payment = new Button("Make Payment");
    public final Button cancel = new Button("Cancel");
    
    private final VBox confirmVBox = new VBox();
    private final Label line1 = new Label("Are you sure, you want to complete the");
    private final Label line2 = new Label("payment?");
    
    public void ConfirmationPopup(int transactionIndex, String userID) {
        bproot.setTop(gptop);
        bproot.setCenter(gpcenter);
        HBox confirmHBox = new HBox(10, payment, cancel );
        confirmHBox.setAlignment(Pos.CENTER);
        confirmHBox.setPadding(new Insets(30, 0, 0, 0));
        
        titleLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffffff");

        gptop.setAlignment(Pos.CENTER);
        gptop.setStyle("-fx-background-color: #2C333A; -fx-border-width: 2.5px 2.5px 2.5px 2.5px; -fx-border-color: black; -fx-border-radius: 8 8 8 8;");
        gptop.add(titleLbl, 0, 0);
        
        line1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 23px; -fx-alignment: center;");
        line2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 23px; -fx-alignment: center;");
        confirmVBox.getChildren().addAll(line1, line2);
        confirmVBox.setAlignment(Pos.CENTER);
        bp.setCenter(confirmVBox);
        
        payment.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 15px;");
        payment.setMinWidth(160);
        payment.setMinHeight(30);
        
        cancel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 15px;");
        cancel.setMinWidth(110);
        cancel.setMinHeight(30);

        gpcenter.setAlignment(Pos.CENTER);
        gpcenter.setStyle("-fx-background-color: #89abc1; -fx-border-width: 0 2px 2px 2px; -fx-border-color: #444545;");

        gpcenter.add(bp, 0, 0);
        gpcenter.add(confirmHBox, 0, 1);
        
        stage.setScene(new Scene(bproot, 550, 400));
        stage.show();
        
        payment.setOnAction(e -> {
            handlePayment(transactionIndex, userID);
        });
        
        cancel.setOnAction(e -> {
            stage.close();
        });
        
    }
    
    public void handlePayment(int transactionIndex, String userID) {
        System.out.println("Handling payment...");

        TransactionManager transactionManager = new TransactionManager();
        int newTransactionIndex = transactionManager.getTransactionIndex();
        String transactionID = "TR" + String.format("%03d", newTransactionIndex + 1);

        Connect connect = Connect.getInstance();

        try (Connection conn = connect.getConnection()) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                String sql = "INSERT INTO TransactionHeader (TransactionID, UserID) VALUES ('" + transactionID + "', '" + userID + "')";
                stmt.executeUpdate(sql);

                String insertTransactionHeader = "INSERT INTO transactionheader (TransactionID, UserID) VALUES ('" + transactionID + "', '" + userID + "')";
                stmt.executeUpdate(insertTransactionHeader);

                String insertTransactionDetail = "INSERT INTO transactiondetail (TransactionID, HoodieID, Quantity) VALUES ('" + transactionID + "', 'HoodieID123', 2)";
                stmt.executeUpdate(insertTransactionDetail);

//                sql = "DELETE FROM Cart WHERE UserID = '" + userID + "'";
                stmt.executeUpdate(insertTransactionHeader);
                stmt.executeUpdate(insertTransactionDetail);

                showAlert("Success", "Payment has been made successfully!");

                switchToHistoryPage();
                conn.commit();
            } catch (SQLException e) {
                showAlert("Error", "An error occurred while making the payment.");
                e.printStackTrace();
                conn.rollback();
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while connecting to the database.");
            e.printStackTrace();
        }
    }



    
    private void switchToHistoryPage() {
    	HistoryPage historyPage = new HistoryPage();
    	Scene scene;
    	historyPage.initHistory();
		historyPage.styleHistory();
		historyPage.initTransactionDetail();
		historyPage.initTransactionHeader();
		bp.setCenter(historyPage.bp);
		bp.setTop(historyPage.menuHs);
		scene = new Scene(bp, 970, 750);
		
	}

	private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
}
