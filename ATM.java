import java.util.Scanner;

// Define the ATMInterface
interface ATMInterface {
    void withdraw(int amount);
    void deposit(int amount);
    void checkBalance();
    void exit();
}

// Abstract class implementing ATMInterface
abstract class AbstractATM implements ATMInterface {
    protected int balance = 10000; // Initial balance
    protected final Object lock = new Object(); // Lock for thread safety

    @Override
    public void checkBalance() {
        synchronized (lock) { // Ensuring thread safety
            System.out.println("Your current balance is: " + balance);
        }
    }
}

// ATM class extending AbstractATM and implementing ATMInterface
public class ATM extends AbstractATM {
    private static final int PIN = 2910;  // Correct PIN for ATM access
    private boolean isAuthenticated = false; // Flag to track if user is authenticated

    public static void main(String[] santu) {
        ATM atm = new ATM();
        Scanner scanner = new Scanner(System.in);

        // Ask for PIN authentication first
        if (atm.authenticate(scanner)) {
            while (true) {
                System.out.println("-------");
                System.out.println("Welcome to ATM");
                System.out.println("-------");
                System.out.println("1. Withdraw");
                System.out.println("2. Deposit");
                System.out.println("3. Check Balance");
                System.out.println("4. Exit");
                System.out.print("Choose the operation you want to perform: ");

                try {
                    int choice = scanner.nextInt();
                    
                    switch (choice) {
                        case 1:
                            System.out.print("Enter amount to withdraw: ");
                            int withdrawAmount = scanner.nextInt();
                            new Thread(() -> atm.withdraw(withdrawAmount)).start();
                            break;

                        case 2:
                            System.out.print("Enter amount to deposit: ");
                            int depositAmount = scanner.nextInt();
                            new Thread(() -> atm.deposit(depositAmount)).start();
                            break;

                        case 3:
                            atm.checkBalance();
                            break;

                        case 4:
                            atm.exit();
                            break;

                        default:
                            System.out.println("Invalid option. Please try again.");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.next(); // Clear invalid input
                }
            }
        } else {
            System.out.println("Incorrect PIN. Access denied.");
        }
    }

    // Method to authenticate the user with a PIN
    public boolean authenticate(Scanner scanner) {
        System.out.print("Enter your PIN: ");
        int enteredPin = scanner.nextInt();
        if (enteredPin == PIN) {
            isAuthenticated = true;
            System.out.println("Authentication successful!");
            return true;
        } else {
            isAuthenticated = false;
            return false;
        }
    }

    // Withdraw method with thread safety
    @Override
    public void withdraw(int amount) {
        synchronized (lock) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                System.out.println("Please collect your money. Remaining balance: " + balance);
            } else {
                System.out.println(amount <= 0 ? "Invalid amount!" : "Insufficient balance!");
            }
        }
    }

    // Deposit method with thread safety
    @Override
    public void deposit(int amount) {
        synchronized (lock) {
            if (amount > 0) {
                balance += amount;
                System.out.println("Amount successfully deposited. New balance: " + balance);
            } else {
                System.out.println("Invalid amount!");
            }
        }
    }

    // Exit method
    @Override
    public void exit() {
        System.out.println("Exiting...");
        System.exit(0);
    }
}