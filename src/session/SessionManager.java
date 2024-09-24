// SessionManager class
package session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connect.Connect;
import table.UserTable;

public class SessionManager {
    private static SessionManager instance;
    private String currentUserId;
    private Connect databaseConnection;

    public SessionManager() {
        databaseConnection = Connect.getInstance();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;

    }
    
    public String getCurrentUserName() {
        String userName = null;
        if (currentUserId != null && !currentUserId.isEmpty()) {
            String query = "SELECT UserName FROM user WHERE UserID = ?";
            try (PreparedStatement preparedStatement = databaseConnection.con.prepareStatement(query)) {
                preparedStatement.setString(1, currentUserId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    userName = resultSet.getString("UserName");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userName;
    }


    public String getCurrentUserRole() {
        String role = null;
        if (currentUserId != null && !currentUserId.isEmpty()) {
            String query = "SELECT Role FROM user WHERE UserID = ?";
            try (PreparedStatement preparedStatement = databaseConnection.con.prepareStatement(query)) {
                preparedStatement.setString(1, currentUserId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    role = resultSet.getString("Role");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
        return role;
    }
    
    public boolean isAdmin() {
        boolean isAdmin = false;
        if (currentUserId != null && !currentUserId.isEmpty()) {
            String query = "SELECT Role FROM user WHERE UserID = ? AND Role = 'Admin'";
            try (PreparedStatement preparedStatement = databaseConnection.con.prepareStatement(query)) {
                preparedStatement.setString(1, currentUserId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    isAdmin = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isAdmin;
    }
}