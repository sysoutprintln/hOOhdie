// Main class
package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import session.SessionManager;
import connect.Connect; // Replace with the actual package name where Connect is located

public class Main extends Application {

	private Scene scene;
	private BorderPane bp;
	private LoginPage login;
	private RegisterPage reg;
	private HomePage homePage;
	private CartPage cartPage;
	private HistoryPage historyPage;
	
	private EditProductPage editPage;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	    
		SessionManager sessionManager = SessionManager.getInstance();
		Connect databaseConnection = Connect.getInstance();
	        
	    bp = new BorderPane();
	    scene = new Scene(bp, 850, 789);

	    primaryStage.setScene(scene);
	    primaryStage.setTitle("hO-Ohdie");

	    login = new LoginPage();
	    reg = new RegisterPage();
	    homePage = new HomePage();
//	    cartPage = new CartPage(sessionManager, databaseConnection);
	    historyPage = new HistoryPage();

	    showLoginScene();

	    primaryStage.show();
	}

	public void showLoginScene() {
		login.initLogin();
		login.styleLogin();
		bp.setTop(login.menuBar);
		bp.setCenter(login.vbox);
	}

	public void showRegisterScene() {
		reg.initReg();
		reg.styleReg();
		bp.setTop(reg.menuBar);
		bp.setCenter(reg.vbox);
	}
	
	public void showHomeScene(Stage primaryStage) {
		homePage.initHome();
		homePage.styleHome();
		bp.setTop(homePage.menuBar);
		bp.setCenter(homePage.vbox);
	}

	public void showCartScene() {
		cartPage.initCart();
		cartPage.styleCart();
		cartPage.initTable();
		bp.setTop(cartPage.menuCart);
		bp.setCenter(cartPage.vbox);
	}
	
	public void showConfirm() {
		
	}
	
	public void showHistoryScene() {
		historyPage.initHistory();
		historyPage.styleHistory();
		historyPage.initTransactionDetail();
		historyPage.initTransactionHeader();
		bp.setCenter(historyPage.bp);
		bp.setTop(historyPage.menuHs);
		scene = new Scene(bp, 970, 750);
	}
	
	public void showEditProductScene() {
		editPage.initEdit();
		editPage.styleEdit();
		editPage.initHoodieTable();
		bp.setCenter(editPage.bp);
		bp.setTop(editPage.menuEdit);
		scene = new Scene(bp, 970, 750);
	}

}