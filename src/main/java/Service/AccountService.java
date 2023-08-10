package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * The AccountService class handles logic related to user accounts.
 */
public class AccountService{
    private AccountDAO accountDao;

    /**
     * Constructor that initializes the AccountDAO object required by the service.
     */
    public AccountService() {
        this.accountDao = new AccountDAO();
    }

    /**
     * Creates a new account if the conditions are met.
     * @param account The account object to be created.
     * @return The created account if successful, otherwise null.
     */
    public Account createAccount(Account account) {
        if(account.getUsername().isEmpty() || account.getPassword().length() < 4){
            return null;
        }

        if(accountExists(account.getUsername())){
            return null;
        }
        return accountDao.insertAccount(account);
    }

    /**
     * Checks if an account with the given username already exists.
     * @param username The username to check.
     * @return True if the account exists, otherwise false.
     */
    public boolean accountExists(String username){
        return accountDao.accountExists(username);
    }

    /**
     * Authenticates a user account based on provided credentials.
     * @param username The username of the account.
     * @param password The password of the account.
     * @return True if the account is authenticated, otherwise false.
     */
    public boolean authenticateAccount(String username, String password) {
        Account loginAccount = accountDao.getAccountByUsername(username);
        return loginAccount != null && loginAccount.getPassword().equals(password);
    }

    /**
     * Retrieves an account by its username.
     * @param user_name The username of the account to retrieve.
     * @return The retrieved account if found, otherwise null.
     */
    public Account getAccountByUsername(String user_name){
        return accountDao.getAccountByUsername(user_name);
    }

    /**
     * Checks if an account with the given ID exists.
     * @param accountId The ID of the account to check.
     * @return True if the account exists, otherwise false.
     */
    public boolean accountIdExists(int accountId){
        return accountDao.accountIdExists(accountId);
    }
    
}
