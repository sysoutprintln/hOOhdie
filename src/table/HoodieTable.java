package table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HoodieTable {
	private String id;
	private String name;
	private double price;
	private Map<String, HoodieTable> hoodieData = new HashMap<>();

	public HoodieTable() {
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public Map<String, HoodieTable> getHoodieData() {
		return hoodieData;
	}


	public void setHoodieData(Map<String, HoodieTable> hoodieData) {
		this.hoodieData = hoodieData;
	}
	
	public void populateFromResultSet(ResultSet resultSet) {
	    try {
	        this.id = resultSet.getString("HoodieID");
	        this.name = resultSet.getString("HoodieName");
	        this.price = resultSet.getDouble("HoodiePrice");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error populating HoodieTable from ResultSet");
	    }
	}


}
