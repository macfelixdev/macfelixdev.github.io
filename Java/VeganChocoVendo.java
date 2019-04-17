
/***********************************************************************************************************************
 * Application Name: VeganChocoVendo
 * Developer:        Edgar Francis A. Felix
 * Date:             4/11/2019
 * Description:      -This application is the system behind the Vegan Chocolate Vending Machine.
 *                   -Accepts coins of the following denominations: 
 *                       10c, 20c, 50c, $1, $2.
 *                   -Dispenses the following menu items:
 *                       Organic Raw ($2.00)
 *                       Caramel ($2.50)
 *                       Hazelnut ($3.10)
 *                   -Dispenses unused change at the end of the transaction.
 *                   -Has an "cancel" command that exits the transaction.
 *                   -Also has a "terminate" command that closes the application.
 * Assumptions:      1. The input from the user can either be one of the following types:
 *                        -User inserts coin.
 *                        -User enters chosen menu item.
 *                      The prompt input (System.in) accepts both input types to provide ease of 
 *                      use to the user. This mimics the actual interface the user experiences with 
 *                      a vending machine where they can make a menu item selection or insert coins.
 *                   2. There is no input for paper money.
 *                   3. The logic for the storage, monitoring and retrieval of both coins and menu item 
 *                      are assumed to be managed already and is not shown here since these may 
 *                      require maintenance/admin access to configure. It is always assumed that 
 *                      coins and menu items do not run out or overflow.
 *                   4. The tone of the language used by the user interface is adapted for children 
 *                      which are the target customers.
 *                   5. Remaining unused coins are always returned to the user after every 
 *                      transaction even if its total amount is still large enough to buy another 
 *                      item. This is to ensure that the user doesn't forget about the change 
 *                      especially children. The user can insert as many coins as they can since this
 *                      will all be returned after the transaction.
 *                   6. Invalid and 5c coins are not accepted but a value of 0 is still added to the 
 *                      payment amount. This is for code brevity and to avoid unnecessary lines of code and
 *                      processing.
 *                   7. In the actual vending machine, invalid inputs could be any of the following:
 *                          - coins of different currency is inserted.
 *                          - simultaneous press of the menu item selection button (if possible).
 *                          - pressing extra buttons for menu items but are currently empty.
 *                      These actions may just be ignored by the actual vending machine but this is included 
 *                      to show that these scenarios are caught.
 *                   8. Menu Item and coins could be created as objects to store price and name. This would also 
 *                      allow for dynamic addition/subtraction of available menu item and coins. However since 
 *                      the app is only for user interface and not for admin, this was not needed.
 **********************************************************************************************************************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;

public class VeganChocoVendo {

	private boolean shouldLoopTrans = true;
	private boolean shouldLoopGetInput = true;
	private MathContext roundOf2 = new MathContext(2);
	private BigDecimal totalCoins = new BigDecimal(0, this.roundOf2);
	private BigDecimal priceOrganicRaw = new BigDecimal(2, this.roundOf2);
	private BigDecimal priceCaramel = new BigDecimal(2.5, this.roundOf2);
	private BigDecimal priceHazelnut = new BigDecimal(3.1, this.roundOf2);

	public static void main(String[] args) {
		VeganChocoVendo vendoObj = new VeganChocoVendo();
		// Loop for each transaction.
		while (vendoObj.shouldLoopTrans) {
			vendoObj.shouldLoopGetInput = true;
			vendoObj.heading();
			// Loop for each user input.
			while (vendoObj.shouldLoopGetInput) {
				vendoObj.getInput();
			}
		}
	}

	// *************************************************************************************************
	// Presentation Logic
	// *************************************************************************************************

	// show available menu, accepted coins and exits command.
	public void heading() {
		System.out.println("");
		System.out.println("  +-------------------------------------------------------------------------------------+");
		System.out.println("  |                         The Vegan Chocolate Vending Machine                         |");
		System.out.println("  +-------------------------------------------------------------------------------------+");
		System.out.println("  |                                                                                     |");
		System.out.println("  |        Hey there! The following snacks are available for you to choose from:        |");
		System.out.println("  |            Organic Raw ($2.00)                                                      |");
		System.out.println("  |            Caramel ($2.50)                                                          |");
		System.out.println("  |            Hazelnut ($3.10)                                                         |");
		System.out.println("  |                                                                                     |");
		System.out.print("  |        ");
		displayValidCoins();
		System.out.println("                             |");
		System.out.println("  |                                                                                     |");
		System.out.println("  |        Type 'cancel' if you need to leave and I'll return your unused coins.        |");
		System.out.println("  |                                                                                     |");
		System.out.println("  +-------------------------------------------------------------------------------------+");
	}

	// Get input from user. Inputs can either be inserted coins or menu choice. (see assumption 1 )
	public void getInput() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			// Prompt for available menu item that can be purchased. The list changes depending on coins inserted.
			if (this.totalCoins.compareTo(this.priceOrganicRaw) >= 0) {
				System.out.println("          The following snacks can now be purchased:");
				System.out.println("          A - Organic Raw ($2.00)");
				if (this.totalCoins.compareTo(this.priceCaramel) >= 0) {
					System.out.println("          B - Caramel ($2.50)");
				}
				if (this.totalCoins.compareTo(this.priceHazelnut) >= 0) {
					System.out.println("          C - Hazelnut ($3.10)");
				}
				System.out.print("          Please enter the letter of your choice.");
				if (this.totalCoins.compareTo(this.priceHazelnut) < 0) {
					System.out.print(" You may also insert more coins.");
				}
				System.out.println("");
			}
			System.out.println("");

			// Always show an updated value of the total coins in the prompt so that its always visible to the user.
			System.out.print("  >>>> [Your coins so far is $" + this.totalCoins + "] " + "Enter input here:");
			String userInput = br.readLine();
			System.out.println("");

			// Evaluate user input.
			evaluateUserInput(userInput);
		} catch (IOException e) {
			System.out.println("IO Exception.");
		}

	}

	// Dispense selected menu item. (See assumption 3)
	// The Prototype Design Pattern is a possible solution to this. However it was not used for code brevity and to
	// avoid unnecessary processing since we are simply displaying the menu item right after selection and no other
	// action is performed on it.
	public void dispenseItem(String userInput) {
		System.out.println("          Here is your " + userInput + ". Enjoy!");
		exitTransaction();
	}

	// Dispense unused coins. (See assumption 3)
	public void dispenseCoin(String dispensedCoin) {
		System.out.println("          Unused coins returned. (" + dispensedCoin + ")");
	}

	// Display valid coins accepted by machine. Used in heading and when invalid coins are inserted.
	public void displayValidCoins() {
		System.out.print("Please insert 10c, 20c, 50c, $1 or $2 coin only.");
	}

	// Inform user that their inserted coins are not enough for the selected menu item.
	public void displayNotEnoughCoins() {
		System.out.println("          Snack not yet available. Please insert more coins.");
	}

	// Exit message
	public void displayExitTransaction() {
		System.out.println("          Bye. Until next time.");
	}

	// This is shown when the user enters an unrecognized input on the prompt. (see assumption 3 and 7)
	public void displayInvalidInput(String insertedCoin) {
		System.out.println("          Sorry, I don't know what to do with that. (Invalid selection or coin)");
		dispenseCoin(insertedCoin);
		System.out.print("          ");
		displayValidCoins();
		System.out.println("");
	}

	// Inform user that 5c coins are not valid.
	public void displayInvalidCoin(String insertedCoin) {
		System.out.println("          Sorry, I can't take this coin. (5c)");
		dispenseCoin(insertedCoin);
		System.out.print("          ");
		displayValidCoins();
		System.out.println("");
	}

	// Shutdown the application.
	public void displayTerminateApp() {
		System.out.println("          Terminating VeganChocoVendo application...");
	}

	// *************************************************************************************************
	// Business Logic
	// *************************************************************************************************

	/*
	 * Evaluate the user input then call appropriate action. (see assumption 1) 
	 *     -Menu item selection. Get payment for price and dispense item or inform user of insufficient coins. 
	 *     -'exit' which ends current transaction, returns coins and initiates new transaction. 
	 *     -'terminate' which ends current transaction, return coins and shuts down the application. 
	 *     -Inserted coin. This is the default and calls another business logic to evaluate coins.
	 * The Chain of Responsibility design pattern can be used instead of the switch if we want to dynamically add 
	 * handlers (similar to case). However since the apps is for user interface and not admin interface and we don't 
	 * need to dynamically add handlers, Switch was used for simplicity.
	 */
	public void evaluateUserInput(String userInput) {
		String userInputUpper = userInput.toUpperCase();
		switch (userInputUpper) {
		case "A":
			if (this.totalCoins.compareTo(this.priceOrganicRaw) >= 0) {
				this.totalCoins = this.totalCoins.subtract(this.priceOrganicRaw, this.roundOf2);
				dispenseItem("Organic Raw");
			} else {
				displayNotEnoughCoins();
			}
			break;
		case "B":
			if (this.totalCoins.compareTo(this.priceCaramel) >= 0) {
				this.totalCoins = this.totalCoins.subtract(this.priceCaramel, this.roundOf2);
				dispenseItem("Caramel");
			} else {
				displayNotEnoughCoins();
			}
			break;
		case "C":
			if (this.totalCoins.compareTo(this.priceHazelnut) >= 0) {
				this.totalCoins = this.totalCoins.subtract(this.priceHazelnut, this.roundOf2);
				dispenseItem("Hazelnut");
			} else {
				displayNotEnoughCoins();
			}
			break;
		case "CANCEL":
			exitTransaction();
			break;
		case "TERMINATE":
			displayTerminateApp();
			exitTransaction();
			this.shouldLoopTrans = false;
			break;
		default:
			BigDecimal validCoin = evaluateInsertedCoin(userInput);
			addCoinToTotal(validCoin);
		}
	}

	/* Evaluate inserted coin and convert them to corresponding amount. 
	 * The Chain of Responsibility design pattern can also be used here but for simplicity it is not used since no need 
	 * for dynamic addition/removal of handler.
	 */
	public BigDecimal evaluateInsertedCoin(String insertedCoin) {
		BigDecimal validCoin = new BigDecimal(0, this.roundOf2);
		String insertedCoinUpper = insertedCoin.toUpperCase();
		switch (insertedCoinUpper) {
		case "10C":
			validCoin = new BigDecimal(0.1, this.roundOf2);
			break;
		case "20C":
			validCoin = new BigDecimal(0.2, this.roundOf2);
			break;
		case "50C":
			validCoin = new BigDecimal(0.5, this.roundOf2);
			break;
		case "$1":
			validCoin = new BigDecimal(1, this.roundOf2);
			break;
		case "$2":
			validCoin = new BigDecimal(2, this.roundOf2);
			break;
		case "5C":
			displayInvalidCoin(insertedCoin);
			break;
		default:
			// This catches user inputs that do not fit any criteria.
			displayInvalidInput(insertedCoin);
		}
		return validCoin;
	}

	// Adds coin to payment amount. (see assumption 6)
	public void addCoinToTotal(BigDecimal validCoin) {
		this.totalCoins = this.totalCoins.add(validCoin, this.roundOf2);
	}

	// Exit transaction
	public void exitTransaction() {
		// Always dispense change at the end of each transaction (see assumption 5)
		if (this.totalCoins.compareTo(BigDecimal.ZERO) > 0) {
			dispenseCoin("$" + String.valueOf(this.totalCoins));
		}
		this.totalCoins = new BigDecimal(0, this.roundOf2);
		displayExitTransaction();
		this.shouldLoopGetInput = false;
	}

}
