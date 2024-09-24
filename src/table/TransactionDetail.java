package table;

public class TransactionDetail {
	
	private String transactionId, hoodieId;
	private int quantity;
	private String productName;
	private double productPrice;
	private String productID;
	
	public String getProductName() {
		return productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public String getProductID() {
		return productID;
	}

	public TransactionDetail(String transactionId, String hoodieId, int quantity) {
		this.transactionId = transactionId;
		this.hoodieId = hoodieId;
		this.quantity = quantity;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getHoodieId() {
		return hoodieId;
	}
	public void setHoodieId(String hoodieId) {
		this.hoodieId = hoodieId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setProductID(String string) {
		this.productID = productID;
		
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
		
	}

	public void setProductName(String string) {
		this.productName = productName;
		
	}

}
