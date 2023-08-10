package Service;

import java.util.List;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

/**
 * The MessageService class handles logic related to messages.
 */
public class MessageService{
    private AccountDAO accountDao;
    private MessageDAO messageDao;

    /**
     * Constructor that initializes the DAO objects required by the service.
     */
    public MessageService() {
        this.accountDao = new AccountDAO();
        this.messageDao = new MessageDAO();
    }

    /**
     * Creates a new message if the conditions are met.
     * @param message The message object to be created.
     * @return The created message if successful, otherwise null.
     */
    public Message createMessage(Message message){
        if(message.getMessage_text().isEmpty() || message.getMessage_text().length() >= 255 || !accountDao.accountIdExists(message.getPosted_by())){
            return null;
        }
        return messageDao.insertMessage(message);
    }

    /**
     * Retrieves all messages from the database.
     * @return A list of all messages.
     */
    public List<Message> getAllMessages(){
        return messageDao.getAllMessages();
    }

    /**
     * Retrieves a message by its ID.
     * @param message_id The ID of the message to retrieve.
     * @return The retrieved message if found, otherwise null.
     */
    public Message getMessageById(int message_id){
        return messageDao.getMessageById(message_id);
    }
    
    /**
     * Deletes a message by its ID.
     * @param message_id The ID of the message to delete.
     * @return The deleted message if found and deleted, otherwise null.
     */
    public Message deleteMessage(int message_id){
        return messageDao.deleteMessageById(message_id);
    }

    /**
     * Updates a message with new content.
     * @param messageId The ID of the message to update.
     * @param updatedMessage The updated message object.
     * @return The updated message if successful, otherwise null.
     */
    public Message updateMessage(int messageId, Message updatedMessage) {
        if(updatedMessage.getMessage_text().isEmpty() || updatedMessage.getMessage_text().length() >= 255){
            return null;
        }
        return messageDao.updateMessage(messageId, updatedMessage);
    }

    /**
     * Retrieves all messages posted by a specific user.
     * @param account_id The ID of the user account.
     * @return A list of messages posted by the user.
     */
    public List<Message> getAllMessagesFromUser(int account_id) {
        return messageDao.getAllMessagesFromUser(account_id);
    }

}
