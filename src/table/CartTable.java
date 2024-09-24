package table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connect.Connect;

public class CartTable {
  
	private String UserID;
	private String HoodieID;
	private String HoodieName;
	private int Quantity;
	private double TotalPrice;
  
	public CartTable(String UserID, String HoodieID, String HoodieName, int Quantity, double TotalPrice) {
		this.UserID = UserID;
	    this.HoodieID = HoodieID;
	    this.HoodieName = HoodieName;
	    this.Quantity = Quantity;
	    this.TotalPrice = TotalPrice;
	}
	
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getHoodieID() {
		return HoodieID;
	}

	public void setHoodieID(String hoodieID) {
		HoodieID = hoodieID;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public double getTotalPrice() {
		return TotalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		TotalPrice = totalPrice;
	}

	public void setHoodieName(String hoodieName) {
		HoodieName = hoodieName;
	}
  
	public String getHoodieName() {
	    try {
	        Connection con = Connect.getInstance().con;

	        String query = "SELECT HoodieName, HoodiePrice FROM hoodie WHERE HoodieID = ?";

	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, this.HoodieID);

	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            String hoodieName = resultSet.getString("HoodieName");
	            double hoodiePrice = resultSet.getDouble("HoodiePrice");

	            resultSet.close();
	            preparedStatement.close();

	            return hoodieName + " (" + hoodiePrice + ")";
	        } else {
	            return "Hoodie not found";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error retrieving hoodie name";
	    }
	}

  
  public HoodieTable getHoodieDetails() throws SQLException {
	  Connection con = Connect.getInstance().con;

	  String query = "SELECT HoodieName, HoodiePrice FROM hoodie WHERE HoodieID = ?";

	  PreparedStatement preparedStatement = con.prepareStatement(query);
	  preparedStatement.setString(1, this.HoodieID);

	  ResultSet resultSet = preparedStatement.executeQuery();

	  if (resultSet.next()) {
	    HoodieTable hoodie = new HoodieTable();
	    hoodie.setId(resultSet.getString("HoodieID"));
	    hoodie.setName(resultSet.getString("HoodieName"));
	    hoodie.setPrice(resultSet.getDouble("HoodiePrice"));

	    resultSet.close();
	    preparedStatement.close();

	    return hoodie;
	  } else {
	    return null;
	  }
	}
}