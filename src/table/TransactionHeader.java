package table;

import main.Transaction;

public class TransactionHeader {

	private String TransactionID, UserID;
	
	public TransactionHeader(String TransactionID, String UserID) {
		this.TransactionID = TransactionID;
		this.UserID = UserID;
	}

	public String getTransactionID() {
		return TransactionID;
	}

	public void setTransactionID(String transactionID) {
		TransactionID = transactionID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}
	public TransactionHeader(Transaction transaction) {
        this.TransactionID = transaction.getTransactionID();
        this.UserID = transaction.getUserID();
    }
	
}
