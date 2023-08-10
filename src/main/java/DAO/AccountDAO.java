package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import Model.Account;
import Util.ConnectionUtil;

/**
 * The AccountDAO class handles database operations related to user accounts.
 * It provides methods for interacting with the "account" table in the database.
 */
public class AccountDAO {
    private Connection connection;

    /**
     * Constructor that initializes the database connection using the ConnectionUtil class.
     */
    public AccountDAO(){
        this.connection = ConnectionUtil.getConnection();
    }

    /**
     * Inserts a new user account into the "account" table.
     * The account's username and password are provided in the Account object.
     * @param account The Account object containing the username and password.
     * @return The Account object with the assigned account_id if insertion is successful, otherwise null.
     */
    public Account insertAccount(Account account){
        String insertQuery = "INSERT INTO account (username, password) VALUES (?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        int account_id = generatedKeys.getInt(1);
                        account.setAccount_id(account_id);
                        return account;
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if an account with the given username exists in the "account" table.
     * @param user_name The username to check for existence.
     * @return True if an account with the given username exists, otherwise false.
     */
    public boolean accountExists(String user_name){
        String query = "SELECT COUNT(*) FROM account WHERE username = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, user_name);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return resultSet.getInt(1) > 0;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves an account by its username from the "account" table.
     * @param username The username of the account to retrieve.
     * @return The retrieved Account object, or null if not found.
     */
    public Account getAccountByUsername(String username){
        String query = "SELECT * FROM account WHERE username = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int account_id = resultSet.getInt("account_id");
                    String user_name = resultSet.getString("username");
                    String pass_word = resultSet.getString("password");

                    return new Account(account_id, user_name, pass_word);

                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }    

    /**
     * Checks if an account with the specified accountId exists in the "account" table.
     * @param accountId The account ID to check for existence.
     * @return true if the account with the given accountId exists, false otherwise.
     */
    public boolean accountIdExists(int accoundId){
        String query = "SELECT COUNT(*) FROM account WHERE account_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, accoundId);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Authenticates an account with the specified username and password in the "account" table.
     * @param username The username of the account to authenticate.
     * @param password The password of the account to authenticate.
     * @return true if the account with the given username and password exists, false otherwise.
     */
    public boolean authenticateAccount(String username, String password){
        String query = "SELECT COUNT(*) FROM account WHERE username = ? AND password = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}