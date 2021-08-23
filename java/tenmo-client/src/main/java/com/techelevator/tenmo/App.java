package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.view.ConsoleService;


import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "\tExit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "\tRegister";
	private static final String LOGIN_MENU_OPTION_LOGIN = "\tLogin";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "\tView your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "\tSend TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "\tView your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "\tRequest TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "\tView your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "\tLogin as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String TRANSFER_AMOUNT = "Enter the amount you would like to transfer";

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoService tenmoService;



	Utilities utilities = new Utilities();


    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new TenmoService(API_BASE_URL));

    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, TenmoService tenmoService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.tenmoService = tenmoService;


	}

	public void run() {

		System.out.println("***************************************************************");
		System.out.println("*                 $$$$$ Welcome to TEnmo $$$$$                *");
		System.out.println("*     If you ever run out of dollars, just ask for TEnmo!     *");
		System.out.println("*                             -----                           *");
		System.out.println("* Now accepting Dogecoin, EVE ISK, WoW Gold, Gil, PotatoCoin, *");
		System.out.println("*               and other forms of shady currency.            *");
		System.out.println("***************************************************************");

		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
    	Balance balance = tenmoService.getBalance(currentUser.getToken());

		System.out.println("------------------------------------------");
		System.out.println("\t Your current balance is: $" + balance.getBalance());
		System.out.println("------------------------------------------");
	}

	private void viewTransferHistory() {
    	try {
			boolean valid = false;
			while (!valid) {
				int selectUser = Integer.parseInt(console.getUserInput("\t(1) - View all transfers for your account (Long Format)" +
						"\n\t(2) - View all transfers for your account (Short Format)" +
						"\n\t(3) - View the details of one transfer (Requires a Transfer ID)" + "\n\nPlease choose an option >>> "));
				if (selectUser == 1) {
					Transfer[] transfers = tenmoService.listAllTransfers(currentUser.getToken(), currentUser.getUser().getId());
					for (Transfer i : transfers) {
						System.out.println("------------------------------------------");
						System.out.println(" Transfer Id: " + i.getTransferId() + "\t\t  Amount: $" + i.getAmount());
						System.out.println("\t\tSender Acct.:\t    " + i.getAccountFrom());
						System.out.println("\t\tRecipient Acct.:    " + i.getAccountTo());
						System.out.println("------------------------------------------");
					}
					valid = true;
				} else if (selectUser == 2) {
					Transfer[] transfers = tenmoService.listAllTransfers(currentUser.getToken(),
							currentUser.getUser().getId());
					for (Transfer i : transfers) {
						System.out.println(" Id: " + i.getTransferId() + " * To: "
								+ i.getAccountTo() + " * From: " + i.getAccountFrom() +
								" * Amount: $" + i.getAmount());
					}
					valid = true;
				} else if (selectUser == 3) {

					int transferId = Integer.parseInt(console.getUserInput("Enter the transfer ID: "));
					Transfer transfer = tenmoService.singleTransfer(currentUser.getToken(), transferId);
					System.out.println(transfer.getAmount() + " From:" + transfer.getAccountFrom());
				} else {
					System.out.println("Invalid Selection!");
				}
			}
		}
    	catch(NumberFormatException | NullPointerException e){
			System.out.println("Invalid input");

		}
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		Balance balance = tenmoService.getBalance(currentUser.getToken());;
		User[] users = tenmoService.getUsers(currentUser.getToken());
		System.out.println("------------------------------------------");
		System.out.println("\t\t  List of TEnmo Users:");
		System.out.println("------------------------------------------");
		for (int i = 0; i < users.length; i++) {
			if(i == users.length - 1) {
				System.out.println("\t" + users[i].getId() + " -- " + users[i].getUsername());
				System.out.println("------------------------------------------");
			}
			else{
				System.out.println("\t" + users[i].getId() + " -- " + users[i].getUsername());
			}
		}

		boolean validAmt = false;
		while(!validAmt) {
			try {
				int selectUser = Integer.parseInt(console.getUserInput("Enter recipient ID (0 to cancel)"));
				if (selectUser == currentUser.getUser().getId()) {
					System.out.println("*** You cannot transfer to your own account ***");
				}
				else if (selectUser == 0) {
					validAmt = true;
				}
				else{
					boolean matchFound = false;
					for (int i = 0; i < users.length; i++) {
						if (users[i].getId() == selectUser) {
							matchFound = true;
							BigDecimal transferAmount = console.getUserInputBigDecimal(TRANSFER_AMOUNT);
							if (transferAmount.compareTo(new BigDecimal(0)) == 1 && balance.getBalance().compareTo(transferAmount)  >= 0) {
								Transfer transfer = new Transfer();
								transfer.setTypeId(2);
								transfer.setStatus(2);
								transfer.setAccountFrom(tenmoService.getAccId(currentUser.getToken(), currentUser.getUser().getId()));//accountservice to getaccId
								transfer.setAccountTo(tenmoService.getAccId(currentUser.getToken(), selectUser));
								transfer.setAmount(transferAmount);

								tenmoService.transfer(currentUser.getToken(), transfer);
								validAmt = true;
							}
							else {
								System.out.println("*** Invalid transfer amount ***");

							}
						}
//						else {//if (i == users.length - 1 && users[i].getId() != selectUser) {
//							System.out.println("\n*** Invalid User Selected!! ***\n");
//						}
					}
					if(!matchFound){
						System.out.println("\n*** Invalid User Selected!! ***\n");
					}

				}
			}
			catch (NumberFormatException e){
				System.out.println("\n*** Enter the ID of a user ***\n");
			}
		}
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);

			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}


}
