package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

/**
 * The MessageDAO class handles database operations related to messages.
 * It provides methods for interacting with the "message" table in the database.
 */
public class MessageDAO {
    private Connection connection;

    /**
     * Constructor that initializes the database connection using the ConnectionUtil class.
     */
    public MessageDAO(){
        this.connection = ConnectionUtil.getConnection();
    }

    /**
     * Inserts a new message into the database.
     * @param message The message to be inserted.
     * @return The inserted message with its generated message ID, or null if insertion fails.
     */
    public Message insertMessage(Message message){
        String insertQuery = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 0){
                return null;
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                int messageId = generatedKeys.getInt(1);
                message.setMessage_id(messageId);
                return message;
            }else{
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Retrieves a list of all messages from the database.
     * @return A list containing all messages retrieved from the database.
     */
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM message";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int message_id = resultSet.getInt("message_id");
                    int posted_by = resultSet.getInt("posted_by");
                    String message_text = resultSet.getString("message_text");
                    long time_posted_epoch = resultSet.getLong("time_posted_epoch");

                    messages.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * Retrieves a specific message from the database based on its message ID.
     * @param messageId The ID of the message to retrieve.
     * @return The message with the specified message ID, or null if not found.
     */
    public Message getMessageById(int messageId){
        String query = "SELECT * FROM message WHERE message_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, messageId);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int message_id = resultSet.getInt("message_id");
                    int posted_by = resultSet.getInt("posted_by");
                    String message_text = resultSet.getString("message_text");
                    long time_posted_epoch = resultSet.getLong("time_posted_epoch");

                    return new Message(message_id, posted_by, message_text, time_posted_epoch);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes a message from the database based on its message ID.
     * @param messageId The ID of the message to delete.
     * @return The deleted message, or null if the message was not found or the deletion failed.
     */
    public Message deleteMessageById(int messageId){
        Message deletedMessage = getMessageById(messageId);
        if(deletedMessage != null){
            String deleteQuery = "DELETE FROM message WHERE message_id = ?";

            try(PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)){
                preparedStatement.setInt(1, messageId);
    
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0){
                    return deletedMessage;
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Updates the message text of an existing message in the database based on its message ID.
     * @param messageId The ID of the message to update.
     * @param updatedMessage The updated message object containing the new message text.
     * @return The updated message, or null if the message was not found or the update failed.
     */
    public Message updateMessage(int messageId, Message updatedMessage){
        if(getMessageById(messageId) != null){
            String updateQuery = "UPDATE message SET message_text = ? WHERE message_id = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)){
                preparedStatement.setString(1, updatedMessage.getMessage_text());
                preparedStatement.setInt(2, messageId);

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0){
                    return getMessageById(messageId);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Retrieves a list of messages posted by a specific user from the database.
     * @param accountId The ID of the account whose messages are to be retrieved.
     * @return A list of messages posted by the specified user, or an empty list if no messages were found.
     */
    public List<Message> getAllMessagesFromUser(int accountId){
        List<Message> messages = new ArrayList<Message>();
        String query = "SELECT * FROM message WHERE posted_by = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, accountId);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int message_id = resultSet.getInt("message_id");
                    int posted_by = resultSet.getInt("posted_by");
                    String message_text = resultSet.getString("message_text");
                    long time_posted_epoch = resultSet.getLong("time_posted_epoch");

                    messages.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }  
}
