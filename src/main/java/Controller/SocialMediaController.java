package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

/**
 * This class defines the API endpoints and handles incoming HTTP requests for the Social Media Application.
 * It acts as a bridge between the client, the service layer, and the data access layer.
*/
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    /**
     * Initializes the controller with a reference to Account Service and Message Service.
     */
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * Sets up the API endpoints and handlers using Javalin.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // User Registration Endpoint
        app.post("/register", this::registerHandler);
        // Login Endpoint
        app.post("/login", this::loginHandler);
        // Create New Message Endpoint
        app.post("/messages", this::creatMessageHandler);
        // Get All Messages Endpoint
        app.get("/messages", this::getAllMessageHandler);
        // Get One Message by Its ID Endpoint 
        app.get("/messages/{message_id}", this::getOneMessageHandler);
        // Delete One Message by Its ID Endpoint
        app.delete("/messages/{message_id}", this::deleteOneMessageHandler);
        // Update One Message by Its ID Endpoint
        app.patch("/messages/{message_id}", this::updateOneMessageHandler);
        // Get All Messages From User by Account ID Endpoint
        app.get("/accounts/{account_id}/messages", this::getAllMessageFromUserHandler);
       
        return app;
    }

    /**
     * Handles the user registration request.
     * @param context The HTTP context containing the request and response.
     */
    private void registerHandler(Context context){
        // Extract the JSON data from the request body and convert it to an Account object
        Account newAccount = context.bodyAsClass(Account.class);
        
        // Create new account in the database using the account service layer
        Account createdAccount = accountService.createAccount(newAccount);

        // Check if the account creation was successful
        if(createdAccount == null){
            context.status(400); // Bad Request
        }else{
            context.status(200).json(createdAccount); // Success
        }
    }

    /**
     * Handles the user login request.
     * @param context The HTTP context containing the request and response.
     */
    private void loginHandler(Context context){
        // Extract the JSON data from the request body and convert it to an Account object
        Account loginAccount = context.bodyAsClass(Account.class);

        // Authenticate the account credentials using the account service layer
        if(accountService.authenticateAccount(loginAccount.getUsername(), loginAccount.getPassword())){
            // If authentication is successful, retrive the authenticate account
            Account authenticatedAccount = accountService.getAccountByUsername(loginAccount.getUsername());
            context.status(200).json(authenticatedAccount); // Success
        }else{
            context.status(401); // Bad Request
        }
    }
    
    /**
     * Hndles the creation of a new message.
     * @param context The HTTP context containing the request and response.
     */
    private void creatMessageHandler(Context context){
        // Extract the JSON data from the request body and convert it to a Message object
        Message newMessage = context.bodyAsClass(Message.class);

        //Create the new message in the database using the message service layer
        Message createdMessage = messageService.createMessage(newMessage);

        // Check if the message creation was successful
        if(createdMessage == null){
            context.status(400); // Bad Request
        }else{
            context.status(200).json(createdMessage); // Success
        }
    }

    /**
     * Handles the retrieval of all messages.
     * @param context The HTTP context containing the request and response.
     */
    private void getAllMessageHandler(Context context){
        // Retrieve a list of all messages from the database using the message service layer
        List<Message> allMessages = messageService.getAllMessages();

        // Return the list of messages with a Always 200 OK response
        context.status(200).json(allMessages);
    }
    
    /**
     * Handles the retrieval of a single message based on its message_id.
     * @param context The HTTP context containing the request and response.
     */
    private void getOneMessageHandler(Context context){
        // Extract the message_id from the path parameter and convert it to an integer
        int messageId = context.pathParamAsClass("message_id", Integer.class).get();

        // Retrieve the specific message from the database using the message service layer
        Message message = messageService.getMessageById(messageId);

        // Return the message with a 200 OK response, or an empty response if the message does not exist
        if(message == null){
            context.status(200); // Failure
        }else{
            context.status(200).json(message); // Success
        }
    }

    /**
     * Handles the deletion of a message based on its message_id.
     * @param context The HTTP context containing the request and response.
     */
    private void deleteOneMessageHandler(Context context){
        // Extract the message_id from the path parameter and convert it to an integer
        int messageId = context.pathParamAsClass("message_id", Integer.class).get();

        // Delete the specific message from the database using the message service layer
        Message deletedMessage = messageService.deleteMessage(messageId);

        // Return the deleted message with a 200 OK response, or an empty response if the message does not exist
        if(deletedMessage != null){
            context.status(200).json(deletedMessage); // Success
        }else{
            context.status(200); // Failure
        }
    }

    /**
     * Handles the updating of a message based on its message_id.
     * @param context The HTTP context containing the request and response.
     */
    private void updateOneMessageHandler(Context context){
        // Extract the message_id from the path parameter and convert it to an integer
        int messageId = context.pathParamAsClass("message_id", Integer.class).get();

        // Extract the updated message information from the request body and convert it to a Message object
        Message updatedMessage = context.bodyAsClass(Message.class);
        
        // Update the specific message in the database using the message service layer
        Message result = messageService.updateMessage(messageId, updatedMessage);

        // Return the updated message with a 200 OK response, or a 400 Bad Request response if the update is not successful
        if(result == null){
            context.status(400); // Bad Request
        }else{
            context.status(200).json(result); // Success
        }
    }
    
    /**
     * Handles retrieving all messages posted by a specific user based on the provided account_id.
     * Retrieves a list of messages associated with the provided account_id from the database.
     * @param context The HTTP context containing the request and response.
     */
    private void getAllMessageFromUserHandler(Context context){
        // Extract the account_id from the path parameter and convert it to an integer
        int accoundId = context.pathParamAsClass("account_id", Integer.class).get();

        // Retrieve a list of messages posted by the specific user from the database using the message service layer
        List<Message> messages = messageService.getAllMessagesFromUser(accoundId);

        // Return the list of messages with a 200 OK response, even if the list is empty
        context.status(200).json(messages);
    }

}