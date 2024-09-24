// Connect to the database class
package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import session.SessionManager;
import table.UserTable;
import table.CartTable;
import table.HoodieTable;

public class Connect {

	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE_NAME = "ho-ohdie";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE_NAME);

	public ResultSet rs;
	public ResultSetMetaData rsm;

	public Statement st;
	public Connection con;

	private static Connect connect;
	
	private static final int USER_ID_INDEX = 1;
	private static final int EMAIL_INDEX = 2;
	private static final int USERNAME_INDEX = 3;
	private static final int PASSWORD_INDEX = 4;
	private static final int PHONE_NUMBER_INDEX = 5;
	private static final int ADDRESS_INDEX = 6;
	private static final int GENDER_INDEX = 7;
	private static final int ROLE_INDEX = 8;

	public static Connect getInstance() {
		if (connect == null) {
			connect = new Connect();
		}
		return connect;
	}

	private Connect() {
		try {
			con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getUserCount() {
		int userCount = 0;
		String query = "SELECT COUNT(*) FROM user";
		try {
			ResultSet resultSet = execQuery(query);
			if (resultSet.next()) {
				userCount = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userCount;
	}

	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void execInsert(String query, String userID, String email, String username, String password,
			String phoneNumber, String address, String gender, String role) {
		try {
			try (PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				preparedStatement.setString(USER_ID_INDEX, userID);
				preparedStatement.setString(EMAIL_INDEX, email);
				preparedStatement.setString(USERNAME_INDEX, username);
				preparedStatement.setString(PASSWORD_INDEX, password);
				preparedStatement.setString(PHONE_NUMBER_INDEX, phoneNumber);
				preparedStatement.setString(ADDRESS_INDEX, address);
				preparedStatement.setString(GENDER_INDEX, gender);
				preparedStatement.setString(ROLE_INDEX, role);
				preparedStatement.executeUpdate();

				// You can retrieve the generated keys if needed
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				if (generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					System.out.println("Generated UserID: " + generatedId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void execInsert(String query, String hoodieId, int quantity, double totalPrice) {
		try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
			preparedStatement.setString(1, hoodieId);
			preparedStatement.setInt(2, quantity);
			preparedStatement.setDouble(3, totalPrice);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<CartTable> getCartItems(String userId) {
	    List<CartTable> cartItems = new ArrayList<>();
	    String query = "SELECT c.UserID, c.HoodieID, h.HoodieName, c.Quantity, (h.HoodiePrice * c.Quantity) AS `Total Price` "
                + "FROM cart c JOIN hoodie h ON c.HoodieID = h.HoodieID "
                + "WHERE c.UserID = ?";

	    try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	        preparedStatement.setString(1, userId);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            CartTable item = new CartTable(
	            		resultSet.getString("UserID"),
	            		resultSet.getString("HoodieID"), 
	            		resultSet.getString("HoodieName"), 
	            		resultSet.getInt("Quantity"), 
	            		resultSet.getDouble("Total Price")
	            		);
	            cartItems.add(item);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return cartItems;
	}
	
	    public UserTable getUserInfo(String userId) {
	        UserTable user = null;
	        String query = "SELECT Email, PhoneNumber, Address FROM user WHERE UserID = ?";
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, userId);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                user = new UserTable();
	                user.setEmail(resultSet.getString("Email"));
	                user.setPhoneNumber(resultSet.getString("PhoneNumber"));
	                user.setAddress(resultSet.getString("Address"));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return user;
	    }
	    
	    public boolean cartItemExists(CartTable cartItem) {
	        String query = "SELECT 1 FROM cart WHERE UserID = ? AND HoodieID = ?";
	        
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, cartItem.getUserID());
	            preparedStatement.setString(2, cartItem.getHoodieID());
	            ResultSet resultSet = preparedStatement.executeQuery();
	            System.out.println("cartItemExists query: " + preparedStatement);
	            return resultSet.next();
	        } catch (Exception e) {
	            System.err.println("Error during cartItemExists operation: " + e.getMessage());
	            e.printStackTrace();
	        }
	        return false;
	    }
	    
		 // Updated removeFromCart method with return value and better error handling
 		public boolean removeFromCart(CartTable cartItem) {
 			String query = "DELETE FROM cart WHERE UserID = ? AND HoodieID = ?";
 			try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
 				preparedStatement.setString(1, cartItem.getUserID());
 				preparedStatement.setString(2, cartItem.getHoodieID());
 				int rowsAffected = preparedStatement.executeUpdate();
 				return rowsAffected > 0; // Return true if item removed successfully
 			} catch (SQLException e) {
 				System.err.println("Error during removeFromCart operation: " + e.getMessage());
 				e.printStackTrace();
 				return false; // Return false if removal failed
 			}
 		}
	   
		public List<HoodieTable> getHoodieItems(String userId) {
		    List<HoodieTable> hoodieItems = new ArrayList<>();
		    String query = "SELECT HoodieID, HoodieName, HoodiePrice FROM `hoodie` WHERE UserID = ?";
		    try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
		        preparedStatement.setString(1, userId);
		        ResultSet resultSet = preparedStatement.executeQuery();

		        while (resultSet.next()) {
		        	HoodieTable item = new HoodieTable();
		        	item.setId(resultSet.getString("HoodieID"));
		        	item.setName(resultSet.getString("HoodieName"));
		        	item.setPrice(resultSet.getDouble("HoodiePrice"));
		            hoodieItems.add(item);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return hoodieItems;

		}

		public Connection getConnection() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Replace with your driver class
        String url = "jdbc:mysql://localhost:3306/ho-ohdie"; // Update with your connection details
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        return null;
    }
}

		public int getTransactionIndex() {
		    Connection connection = this.con; // Use the existing connection within your Connect class
		    int transactionIndex = -1;

		    try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'transactionheader' AND COLUMN_NAME = 'transactionID'")) {
		        ResultSet resultSet = preparedStatement.executeQuery();

		        if (resultSet.next()) {
		            transactionIndex = resultSet.getInt(1);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return transactionIndex;
		}
}
