package session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import table.TransactionDetail;
import connect.Connect;
import main.CartPage;
import main.Transaction;


public class TransactionManager {
    private Connect connect;

    public TransactionManager() {
        this.connect = Connect.getInstance();
    }

    public List<Transaction> getTransactionsByUserID(String userID) {
        List<Transaction> userTransactions = new ArrayList<>();
        String query = "SELECT * FROM transactionheader WHERE userID = ?";
        try (PreparedStatement preparedStatement = connect.con.prepareStatement(query)) {
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionID(resultSet.getString("transactionID"));
                transaction.setUserID(resultSet.getString("userID"));
                userTransactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userTransactions;
    }
    
    public List<TransactionDetail> getTransactionDetails(String transactionID) {
        List<TransactionDetail> transactionDetails = new ArrayList<>();
        Connection connection = connect.con;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT td.transactionID, td.productID, td.quantity, p.product_name, p.product_price FROM transactiondetail td JOIN product p ON td.productID = p.productID WHERE td.transactionID = ?");
            preparedStatement.setString(1, transactionID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TransactionDetail transactionDetail = new TransactionDetail(transactionID, transactionID, 0);
                transactionDetail.setTransactionId(resultSet.getString("transactionID"));
                transactionDetail.setProductID(resultSet.getString("productID"));
                transactionDetail.setQuantity(resultSet.getInt("quantity"));
                transactionDetail.setProductName(resultSet.getString("product_name"));
                double productPrice = resultSet.getDouble("product_price");
                transactionDetail.setProductPrice(productPrice);
                transactionDetails.add(transactionDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }

        return transactionDetails;
    }

    public double getProductPriceById(String productID) {
        double productPrice = 0.0;
        Connection connection = connect.con;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT product_price FROM product WHERE productID = ?");
            preparedStatement.setString(1, productID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                productPrice = resultSet.getDouble("product_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }

        return productPrice;
    }

    public int getTransactionIndex() {
        int transactionIndex = -1;
        Connection connection = connect.con;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(CAST(SUBSTRING(TransactionID, 3) AS SIGNED)) FROM transactionheader");

            if (resultSet.next()) {
                int maxTransactionIndex = resultSet.getInt(1);
                transactionIndex = maxTransactionIndex + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }

        return transactionIndex;
    }

    public synchronized void createNewTransaction(String userID) {
        Connection connection = connect.con;
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);

            int transactionIndex = connect.getTransactionIndex();
            String newTransactionID = "TR" + String.format("%03d", transactionIndex);

            String insertQuery = "INSERT INTO transactionheader (TransactionID, userID, transactionDate) VALUES (?, ?, CURRENT_DATE)";

            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, newTransactionID);
            preparedStatement.setString(2, userID);

            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch(SQLException excep) {
                    excep.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) { /* ignored */ }
            }
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) { /* ignored */ }
        }
    }
}