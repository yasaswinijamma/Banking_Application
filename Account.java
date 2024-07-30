import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Account {
	
	public String name;
	public String email;
	public double balance;
	public double depositamount;
	public double Withdrawlamount;
	public String transaction_type;
	public Timestamp Transaction_time;
	public int val;
	public int inp;
	public int status;
	public double tranbal;
	
	Scanner input = new Scanner(System.in);
	String url = "jdbc:mysql://localhost:33306/Database";
	String user = "root";
	String password = "Spring@2022";

	Connection conn = DriverManager.getConnection(url, user, password);
	
	public Account() throws SQLException {
		super();		
	}
//customer Input
	public void CustomerInput() throws SQLException {
		System.out.println("Please enter "
				+ " 1. Opening of bank accounts\r\n"
				+ " 2. Deposit of money\r\n"
				+ " 3. Withdrawal of money\r\n"
				+ " 4. Viewing of account balances\r\n"
				+ " 5. Generation of transaction reports\r\n"
				+ " 6. Closing of bank accounts\r\n"
				+ "-1. Exit: ");
		try {
		inp = input.nextInt();
	        } catch (InputMismatchException e) {
	    System.out.println("Invalid input: Please enter an integer.");
	}
		SelectingMethod();
	}
	
	public void SelectingMethod() throws SQLException {
		
		switch (inp) {
		case 1: 
			OpeningBankAcount();
			printAccountInfo();
			CustomerInput();
			break;
		case 2: 
			deposit();
			CustomerInput();
			break;
		case 3: 
			Withdrawal();
			CustomerInput();
			break;
		case 4: 
			ViewAccountbalance();
			CustomerInput();
			break;
		case 5: 
			TransactionReport();
			CustomerInput();
			break;
		case 6: 
			ClosingAccount();
			System.out.println("Thank you for Banking with UCM");
			break;
		case -1: 
			System.out.println("Thank you for Banking with UCM");
			conn.close();
			break;
		}
	}
	
	public void InsertAccountData() throws SQLException {
		
		String sql = "INSERT INTO Account(name, email, balance,status) VALUES (?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
        pstmt.setString(2, email);
        pstmt.setDouble(3, balance);
        pstmt.setInt(4, status);
        pstmt.executeUpdate();
        pstmt.close();
		
	}
	
	public void InsertTransactionData() throws SQLException{
		
		 String transql = "INSERT INTO Transaction(name, amount, transaction_type, Transaction_time) VALUES (?,?,?,?)";
			PreparedStatement tranpstmt = conn.prepareStatement(transql);
			tranpstmt.setString(1, name);
			tranpstmt.setDouble(2, tranbal);
			tranpstmt.setString(3, transaction_type);
			Date date = new Date();
	        Timestamp timestamp = new Timestamp(date.getTime());
	        tranpstmt.setTimestamp(4, timestamp);			
			tranpstmt.executeUpdate();
			tranpstmt.close();
	}
	
	public void UpdateBalance() throws SQLException {
		
		String Sql = "update Account set balance = ? where name = ?";
		PreparedStatement pstmt = conn.prepareStatement(Sql);
		pstmt.setDouble(1, balance);
		pstmt.setString(2, name);
		pstmt.executeUpdate();
		pstmt.close();
		
	}

	public void UpdateStatus() throws SQLException {
		String updateSql = "update Account set status = ? where name = ?";
		PreparedStatement updatePstmt = conn.prepareStatement(updateSql);
		updatePstmt.setDouble(1, status);
		updatePstmt.setString(2, name);
		updatePstmt.executeUpdate();
		updatePstmt.close();
	}
	public static boolean isValidEmail( String email) {
	    String regex = "^[a-zA-Z0-9_+&.-]+@[a-zA-Z0-9.-]+$";	    
	    return email.matches(regex);
	}
	public int CheckingIfBankAccountExists() throws SQLException {
		
		System.out.println("Please enter Account holder user name: ");
		name = input.next();
		System.out.println("Please enter Account holder email: ");
		email = input.next();
		if(isValidEmail(email)) {
			String countsql = "select count(*) as count from Account where name = ? and email = ? ";
			PreparedStatement countpstmt = conn.prepareStatement(countsql);
			countpstmt.setString(1, name);
			countpstmt.setString(2, email);
			ResultSet countrs = countpstmt.executeQuery();
			int count = 0;
			if(countrs.next()){
				 count = countrs.getInt(1);
				 //System.out.println(count);
				}
			
			String staussql = "select status from Account where name = ?";
			PreparedStatement statuspstmt = conn.prepareStatement(staussql);
			statuspstmt.setString(1, name);
			
			ResultSet statusrs = statuspstmt.executeQuery();
			while(statusrs.next()) {
				status = statusrs.getInt("status");
			}
			
			if (status ==1) {
			
				System.out.printf("You account - %s with %s email is in deactivated state, please press 0 to re-active your account or 1 to exit  ", name, email);
				int reactivatestatus = input.nextInt();
				if (reactivatestatus == 0)
				{
					setStatus(0);
					UpdateStatus();
					System.out.println("Your Account has been reactivated. Please continue with the UCM banking services.");
					
				}
				else if (reactivatestatus == 1) {
					System.out.println("Thank you for Banking with UCM");
				}
			//return count;
			}
			
			else {
				return count;
			}
			
		}
		else {
			System.out.println("Please Enter a valid Email Id!!");
		}
		return 2;
	}
	
	public int CheckingBankAccountstatus() throws SQLException {
	 
		int bankacctsttaus=0;
		
		String staussql = "select status from Account where name = ?";
		PreparedStatement statuspstmt = conn.prepareStatement(staussql);
		statuspstmt.setString(1, name);
		
		ResultSet statusrs = statuspstmt.executeQuery();
		while(statusrs.next()) {
			bankacctsttaus = statusrs.getInt("status");
		}
	
		
		return bankacctsttaus;
	}
	
	public void OpeningBankAcount() throws SQLException {

			System.out.println("Welcome to UCM Bank");
			
			int bankacctexist = CheckingIfBankAccountExists();
			int bankacctsttaus = CheckingBankAccountstatus();
			
			if(bankacctsttaus == 1 && bankacctexist ==1 ) {
				System.out.printf("You are an existing customer with UCM Bank. please reactive or create a new account with UCM ",name);
			}
			else if(bankacctsttaus == 0  && bankacctexist ==0) {
			
			System.out.printf("Please enter Opening Balance for %s Account:", name);
			setTransaction_type("Credit");
			balance = input.nextDouble();
			
			if (balance > 0) 
			{
				setBalance(balance);
			} 
			else 
			{
				balance = 0;
				System.out.println("Balance cannot be less than Zero");
			}
			setStatus(0);
			InsertAccountData();
			tranbal = getBalance();
			InsertTransactionData();
			}
	}
	
	public void printAccountInfo() {
		System.out.print("\n\n");
		System.out.printf("Account Holder Name is %s and Account Balance is %.2f%n ",name, balance);
		System.out.print("\n\n");
	}
	
	public double deposit() throws SQLException 
	{
		int bankacctexist = CheckingIfBankAccountExists();
		int bankacctsttaus = CheckingBankAccountstatus();
		
		if(bankacctsttaus == 1 && bankacctexist ==1 ) {
			System.out.printf("You are an existing customer with UCM Bank. please reactive or create a new account with UCM ",name);
		}
		else if(bankacctsttaus == 0  && bankacctexist ==1) {
			
		String sql = "select balance from Account where name = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			balance = rs.getInt("balance");
			System.out.println("Balance: " + balance);
		}
		
		System.out.printf("Account Holder Name is %s and Account Balance is %.2f%n ",name, balance);
		System.out.printf("Enter Deposit Amount for %s account: ", name);
		setDepositamount(input.nextDouble());
		
		if (depositamount>0 ) {
			setBalance(balance+depositamount);
			System.out.printf("Adding %.2f to %s's Account%n",depositamount,name);
		}
		else 
		{
			setBalance(balance);
			System.out.println("Deposit Amount cannot be entered in negative values");
		}
		setTransaction_type("Credit");
		UpdateBalance();
		tranbal = getDepositamount();
		InsertTransactionData();
		
        //conn.close();
		
		System.out.printf("%s account balance is %.2f%n ",name, balance);
		}
			
		return balance;
		//System.out.print("\n\n");
		
	}
	
	private void Withdrawal() throws SQLException {
		// TODO Auto-generated method stub
		
		int bankacctexist = CheckingIfBankAccountExists();
		int bankacctsttaus =CheckingBankAccountstatus();
		
		if(bankacctsttaus == 1 && bankacctexist ==1 ) {
			System.out.printf("You are an existing customer with UCM Bank. please reactive or create a new account with UCM ",name);
		}
		else if(bankacctsttaus == 0  && bankacctexist ==1) {
		String sql = "select balance from Account where name = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			balance = rs.getInt("balance");
			System.out.println("Balance: " + balance);
		}
			System.out.printf("Account Holder Name is %s and Account Balance is %.2f%n ",name, balance);
			System.out.printf("Enter Withdrawl Amount for %s account: ", name);
			setWithdrawlamount(input.nextDouble());
			if (Withdrawlamount<= balance) {
				setWithdrawlamount(Withdrawlamount);
				setBalance(balance-Withdrawlamount);
				System.out.printf("%s account balance is %.2f%n ",name, balance);
			}
			else {
				System.out.printf("%s account balance is %.2f%n , please select amount less than or equals to %.2f%n ",name, balance, balance);	
			}
			setTransaction_type("Debit");
			UpdateBalance();
			tranbal = getWithdrawlamount();
			InsertTransactionData();
		}
	
	}
	
	private void ViewAccountbalance() throws SQLException {
		// TODO Auto-generated method stub
		
		int bankacctexist = CheckingIfBankAccountExists();
		int bankacctsttaus =CheckingBankAccountstatus();
		
		if(bankacctsttaus == 1 && bankacctexist==1 ) {
			System.out.printf("You are an existing customer with UCM Bank. please reactive or create a new account with UCM ",name);
		}
		else if(bankacctsttaus == 0 && bankacctexist ==1) {
		String sql = "select balance from Account where name = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			balance = rs.getInt("balance");
			System.out.println("Balance: " + balance);
		}
		System.out.printf("Account Holder Name is %s and Account Balance is %.2f%n ",name, balance);
		System.out.print("\n\n");	
		}
			
	}
	
	private void TransactionReport() throws SQLException {
		// TODO Auto-generated method stub
		int bankacctexist = CheckingIfBankAccountExists();
		int bankacctsttaus =CheckingBankAccountstatus();
		
		
		if(bankacctsttaus == 1 && bankacctexist ==1 ) {
			System.out.printf("You are an existing customer with UCM Bank. please reactive or create a new account with UCM ",name);
		}
		else if(bankacctsttaus == 0  && bankacctexist ==1) {
		String sql = "select name, amount, transaction_type,Transaction_time from Transaction where name = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		
		ResultSet rs = pstmt.executeQuery();
		System.out.println("name" + "\t\t" + "amount" + "\t\t" +" transaction_type" + "\t\t" + "Transaction_time" );
		while(rs.next()) {
			name = rs.getString("name");
			balance= rs.getInt("amount");
			transaction_type = rs.getString("transaction_type");
			Transaction_time = rs.getTimestamp("Transaction_time");
			//System.out.println("Balance: " + balance);
			System.out.println(name + "\t\t" + balance + "\t\t" + transaction_type + "\t\t" + Transaction_time );
		
		}
		}
		
		System.out.print("\n\n");		
		
	}
	
	private void ClosingAccount() throws SQLException 
	{
		// TODO Auto-generated method stub
		int bankacctexist = CheckingIfBankAccountExists();
		int bankacctsttaus =CheckingBankAccountstatus();
		
		if(bankacctsttaus == 1 && bankacctexist ==1 ) 
		{
			System.out.printf("You are an existing customer with UCM Bank. please reactive or create a new account with UCM ",name);
		}
		else if(bankacctsttaus == 0) 
		{
		String sql = "select balance from Account where name = ?";
		Connection conn1 = DriverManager.getConnection(url, user, password);
		PreparedStatement pstmt1 = conn1.prepareStatement(sql);
		pstmt1.setString(1, name);
		ResultSet rs = pstmt1.executeQuery();
		while(rs.next()) 
		{
			balance = rs.getInt("balance");
			System.out.println("Balance: " + balance);
		}
		System.out.printf("Account Holder Name is %s and Account Balance is %.2f%n ",name, balance);
		System.out.printf("Please confirm 1 to close or 0 to not close %s Account ",name);
		status = input.nextInt();
		
		if (status == 1 ) 
		{
			setStatus(status);
			if (balance>=50) 
			{	
			setBalance(balance-50);
			System.out.printf("\n%s Account Holder, as closing charges are 50 dollars,"
					+ " closing amount is debited from your account and remaining ammount %.2f will be sent via check to your register address\n",name, balance);
			setTransaction_type("Debit");
			tranbal = balance-50;
			InsertTransactionData();
			UpdateBalance();
			UpdateStatus();
			setBalance(0);
			UpdateBalance();
			
			}
			else if (balance <50)  
			{
				double ExtraAmount = 50 - balance;  
				System.out.printf("\n%s Account Holder, as closing charges are 50 dollars,"
						+ " please add the remaining amount of  %.2f%n to your account and then proceed with closing the account\n",name, ExtraAmount);
				deposit();
				setStatus(1);
				UpdateStatus();
				setBalance(0);
				UpdateBalance();
			}
		}
		else if (status ==0) 
		{
			System.out.printf("Please proceed with the banking services with UCM Bank\n.");
			CustomerInput();
		}
		}
	}	
	
	//getter and setter methods
	public String getname()
	{
		return name;
	}
	public void setname(String name)
	{
	this.name = name;	
	}
	
	public double getBalance()
	{
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getDepositamount() {
		return depositamount;
	}
	public void setDepositamount(double depositamount) {
		this.depositamount = depositamount;
	}
	
	public double getWithdrawlamount() {
		return Withdrawlamount;
	}
	public void setWithdrawlamount(double withdrawlamount) {
		Withdrawlamount = withdrawlamount;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public Timestamp getTransaction_time() {
		return Transaction_time;
	}
	public void setTransaction_time(Timestamp transaction_time) {
		Transaction_time = transaction_time;
	}
}
