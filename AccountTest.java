import java.sql.SQLException;

public class AccountTest {
	public static void main(String[] args) throws SQLException  {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Account myaccount= new Account();
		myaccount.CustomerInput();
	}	
}