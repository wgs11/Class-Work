/*
	Evan Alexander (eda19@pitt.edu) & William Sheppard-Sage (wgs11@pitt.edu)
	FaceSpace
	CS1555 - FaceSpace.java
 */
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class FaceSpace {

	private Connection connection;
/*Main command prompt method: takes a username and PS# passed by main to initiate
        a connection with the database
        The user then inputs the desired command.
        */
	public FaceSpace(String username, String password) {

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
			this.connection = DriverManager.getConnection(url, username, password);

			System.out.println("\nConnected");

			while (true) {
				System.out.print("\n: ");
				try {

					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String input = br.readLine();
					if (input.equals("exit")) {
						break;
					} else if (input.equals("createUser")) {
						createUser(this.connection);
					} else if (input.equals("initiateFriendship")) {
						initiateFriendship(this.connection);
					} else if (input.equals("establishFriendship")) {
						establishFriendship(this.connection);
					} else if (input.equals("displayFriends")) {
						displayFriends(this.connection);
					} else if (input.equals("createGroup")) {
						createGroup(this.connection);
					} else if (input.equals("addToGroup")) {
						addToGroup(this.connection);
					} else if (input.equals("sendMessageToUser")) {
						sendMessageToUser(this.connection);
					} else if (input.equals("sendMessageToGroup")) {
						sendMessageToGroup(this.connection);
					} else if (input.equals("displayMessages")) {
						displayMessages(this.connection);
					} else if (input.equals("displayNewMessages")) {
						displayNewMessages(this.connection);
					} else if (input.equals("searchForUser")) {
						searchForUser(this.connection);
					} else if (input.equals("threeDegrees")) {
						threeDegrees(this.connection);
					} else if (input.equals("topMessagers")) {
						topMessagers(this.connection);
					} else if (input.equals("dropUser")) {
						dropUser(this.connection);
					}
				} catch (IOException IOE) {
					IOE.printStackTrace();
				}
			}
			connection.close();
		} catch (Exception exception) {
			System.out.println("Error: " + exception.toString());
			exception.printStackTrace();
		}

		System.out.println("Exited.");
	}
/*Create user takes a first name, last name, email, and date of birth
        and inserts a new record in the User table of the database
        */
	public static void createUser(Connection dbcon) throws IOException {
		System.out.println("In create user");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));       //prompting user for input
		System.out.print("Enter user first name: ");
		String fname = br.readLine();
		System.out.print("Enter user last name: ");
		String lname = br.readLine();
		System.out.print("Enter user email address: ");
		String email = br.readLine();
		System.out.print("Enter user DOB (YYYY-MM-DD): ");
		String birth = br.readLine();
		java.sql.Timestamp login = getCurrentTimeStamp();                               //needs current timestamp
		java.sql.Date date = java.sql.Date.valueOf(birth);
		PreparedStatement prepState = null;                                             //creating the prep statement

		String query = "INSERT INTO USERS"
				+ "(fname, lname, email, DOB, lastLogin) VALUES"
				+ "(?,?,?,?,?)";
		try {
			prepState = dbcon.prepareStatement(query);                              //populating the values
			prepState.setString(1, fname);
			prepState.setString(2, lname);
			prepState.setString(3, email);
			prepState.setDate(4, date);
			prepState.setTimestamp(5, login);
			prepState.executeUpdate();
			System.out.println("\nInserted user");	  
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		query = "SELECT userID FROM USERS WHERE fname = ? AND lname = ? AND email = ? ";//query used to report back userID of new user
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, fname);
			prepState.setString(2, lname);
			prepState.setString(3, email);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String userID = rs.getString("userID");
				System.out.print("New user " + fname + " " + lname + " added to FaceSpace with userID " + userID + "\n");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static java.sql.Timestamp getCurrentTimeStamp() {                               //simply returns current timestamp
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
/*
        initiateFriendship takes two userIDs and creates a new, pending
        friendship (flag 0) between the two users
        */
	public static void initiateFriendship(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));       //obtaining userIDs from user
		System.out.print("Enter Friend 1 user ID: ");
		String user1 = br.readLine();
		System.out.print("Enter Friend 2 user ID: ");
		String user2 = br.readLine();
		Calendar calendar = Calendar.getInstance();                                     //needs current date
		java.util.Date currentDate = calendar.getTime();
		java.sql.Date date = new java.sql.Date(currentDate.getTime());
		PreparedStatement prepState = null;
		String query = "INSERT INTO FRIENDSHIPS"                                        //creating prep statement
				+ "(friend1, friend2, status, DOF) VALUES"
				+ "(?,?,?,?)";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user1);
			prepState.setString(2, user2);
			prepState.setString(3, "0");
			prepState.setDate(4, date);
			prepState.executeUpdate();
			System.out.print("Initiated Friendship");
		} catch (SQLException ex) {
			System.out.print(ex.getMessage());
		}
	}
        /* establishFriendship takes two userIDs and, assuming a pending
        friendship exists between them, updates that friendship to flag = 1.
        Returns an error if for some reason a friendship cannot be established.
        */
	public static void establishFriendship(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));           //prompting user for userIDs
		System.out.print("Enter User 1: ");
		String user1 = br.readLine();
		System.out.print("Enter User 2: ");
		String user2 = br.readLine();
		PreparedStatement prepState = null;
		String query = "UPDATE FRIENDSHIPS SET status = ? WHERE friend1 = ? AND friend2 = ? ";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, "1");
			prepState.setString(2, user1);
			prepState.setString(3, user2);
			prepState.executeUpdate();
			System.out.println("Established Friendship");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		prepState = null;
		query = "UPDATE FRIENDSHIPS SET status = ? WHERE friend2 = ? AND friend1 = ? ";     //creating the update statement
		try {
			prepState = dbcon.prepareStatement(query);                                  //populating the values & executing
			prepState.setString(1, "1");
			prepState.setString(2, user1);
			prepState.setString(3, user2);
			prepState.executeUpdate();
			System.out.println("Established Friendship");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
/*displayFriends takes a userID and prints out every userID that is in a friendship
        (pending or established) with it. It checks friendships with the userID
        in either position of a friendship (1st or 2nd position) to catch all cases
        */
	public static void displayFriends(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter user ID: ");                    //prompts user for ID
		String user = br.readLine();
		PreparedStatement prepState = null;
		String query = "SELECT friend2, status FROM FRIENDSHIPS WHERE friend1 = ?";     //creates & executes query with ID in pos 1
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String friend = rs.getString("friend2");
				String status = rs.getString("status");
				System.out.println("Friend: " + friend + " " + status + "\n");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		query = "SELECT friend1, status FROM FRIENDSHIPS WHERE friend2 = ?";            //creates & executes query with ID in pos 2
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String friend = rs.getString("friend1");
				String status = rs.getString("status");
				System.out.println("Friend: " + friend + " " + status + "\n");
			}

			System.out.println("Displayed Friends");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
/*createGroup takes a group name, group description and membership limit and
        inserts a new group record into the groups table        
        */
	public static void createGroup(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));       //prompts user for name, description and member limit
		System.out.print("Enter Group Name: ");
		String groupName = br.readLine();
		System.out.print("Enter Group Description: ");
		String description = br.readLine();
		System.out.print("Enter Membership Limit: ");
		int memLim = Integer.parseInt(br.readLine());
		PreparedStatement prepState = null;
		String query = "INSERT INTO GROUPS"                                             //creating/executing prep statement 
				+ "(groupID, groupName, description, groupMembers, groupLimit) VALUES"
				+ "(?,?,?,?,?)";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, "1");
			prepState.setString(2, groupName);
			prepState.setString(3, description);
			prepState.setString(4, "0");
			prepState.setInt(5, memLim);
			prepState.executeUpdate();
			System.out.println("Created Group");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		query = "SELECT groupID FROM GROUPS WHERE groupName = ? AND description = ?";   //query to print out new group's ID
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, groupName);
			prepState.setString(2, description);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String groupID = rs.getString("groupID");
				System.out.print("New Group added to FaceSpace with groupID " + groupID + "\n");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
/*addToGroup takes a userID and groupID and adds the user to the group by
        inserting a record in the GROUPMEMBERS (userID and groupID)
        */ 
	public static void addToGroup(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter Group ID: ");
		String user = br.readLine();
		System.out.print("Enter User ID: ");
		String group = br.readLine();
		//needs to also update Group record with +1 member
		PreparedStatement prepState = null;
		String query = "INSERT INTO GROUPMEMBERS"
				+ "(groupID, userID) VALUES"
				+ "(?,?)";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user);
			prepState.setString(2, group);
			prepState.executeUpdate();
			System.out.print("Added to Group\n");
		} catch (SQLException ex) {
			System.out.print(ex.getMessage());
		}
	}
/*sendMessageToUser takes a subject line, senderID, message, and receiver
        and inserts a new record into the MESSAGES table that includes
        the timestamp of when the message was "sent"(added).
        */ 
	public static void sendMessageToUser(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter subject: ");                                //prompting user for input
		String subject = br.readLine();
		System.out.print("Enter message: ");
		String body = br.readLine();
		System.out.print("Enter sender: ");
		String sender = br.readLine();
		System.out.print("Enter receiver: ");
		String receiver = br.readLine();
		java.sql.Timestamp sent = getCurrentTimeStamp();
		PreparedStatement prepState = null;
		String query = "INSERT INTO MESSAGES"                               //creating/executing prep statement
				+ "(subject, bodyText, senderID, receiverID, dateSent) VALUES"
				+ "(?,?,?,?,?)";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, subject);
			prepState.setString(2, body);
			prepState.setString(3, sender);
			prepState.setString(4, receiver);
			prepState.setTimestamp(5, sent);
			prepState.executeUpdate();
			System.out.print("Message Sent\n");
		} catch (SQLException ex) {
			System.out.print(ex.getMessage());
		}
	}
/*sendMessagetoGroup functions almost identically to messageUser, but instead
        of a recipientID it takes a groupID and iterates through the members
        of that group, "sending" the message to each of them in turn
        */
	public static void sendMessageToGroup(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter subject: ");                //prompts user for inputs
		String subject = br.readLine();
		System.out.print("Enter message: ");
		String body = br.readLine();
		System.out.print("Enter recipient group: ");
		String groupRecipient = br.readLine();
		System.out.print("Enter sender: ");
		String sender = br.readLine();
		PreparedStatement prepState = null;
		String query = "SELECT userID FROM GROUPMEMBERS WHERE groupID = ?";     //queries database for userIDs in relevant group
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, groupRecipient);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String recipient = rs.getString("userID");              //creates a series of prep statements to insert
				java.sql.Timestamp sent = getCurrentTimeStamp();        //message records for each individual userID
				String query2 = "INSERT INTO MESSAGES"
						+ "(subject, bodyText, senderID, receiverID, dateSent) VALUES"
						+ "(?,?,?,?,?)";
				PreparedStatement prepState2 = null;
				try {
					prepState2 = dbcon.prepareStatement(query2);
					prepState2.setString(1, subject);
					prepState2.setString(2, body);
					prepState2.setString(3, sender);
					prepState2.setString(4, recipient);
					prepState2.setTimestamp(5, sent);
					prepState2.executeUpdate();
					System.out.print("Message Sent!\n");
				} catch (SQLException ex) {
					System.out.print(ex.getMessage());
				}
			}

		} catch (SQLException ex) {
			System.out.print(ex.getMessage());
		}
	}
/*displayMessages takes a userID and prints all messages from the database
        corresponding to it.
        */
	public static void displayMessages(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter user ID: ");            //prompting user for input
		String user = br.readLine();
		PreparedStatement prepState = null;             //preparing and executing statement
		String query = "SELECT subject, bodyText, dateSent FROM MESSAGES WHERE receiverID = ?";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String subject = rs.getString("subject");       //printing each message
				String message = rs.getString("bodyText");
				String dateSent = rs.getString("dateSent");
				System.out.print("Subject: " + subject + "\n" + "Message: " + message + "\n" + "Date sent: " + dateSent + "\n");

			}

			System.out.println("\nDisplayed Messages");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
/*displayNewMessages functions almost identically to displayMessages, taking a userID as input
        but instead of displaying all messages it only diplays messages that were "sent"
        after the user's last log in time (stored in the USERS table)
        */
	public static void displayNewMessages(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter user ID: ");
		String user = br.readLine();
		PreparedStatement prepState = null;         //preparing/executing the statement
		String query = "SELECT subject, bodyText, dateSent FROM MESSAGES WHERE dateSent > (SELECT lastLogin FROM USERS WHERE userID = ? AND receiverID = ?)";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user);
			prepState.setString(2, user);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String subject = rs.getString("subject");       //printing each message
				String message = rs.getString("bodyText");
				String dateSent = rs.getString("dateSent");
				System.out.print("Subject: " + subject + "\n" + "Message: " + message + "\n" + "Date sent: " + dateSent + "\n\n");
			}

			System.out.println("\nDisplayed Messages");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
/*searchForUser relies on SQLs LIKE selector to search the USER table.
        It examines the first name, last name and email fields of each record
        and compares each with each distinct string input by the user
        */
	public static void searchForUser(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter a search: ");       //prompts user for search
		String search = br.readLine();
		PreparedStatement prepState = null;         //preps and executes search query
		String query = "SELECT * FROM USERS WHERE UPPER(fname) LIKE ? OR UPPER(lname) LIKE ? OR UPPER(email) LIKE ?";
		String [] pieces = search.split(" ");
		try {
			prepState = dbcon.prepareStatement(query);
			for (int i = 0; i < pieces.length; i++) {
				System.out.println("Every match: " + pieces[i] + ":");
				prepState = dbcon.prepareStatement(query);
				prepState.setString(1, "%" + pieces[i].toUpperCase() + "%");
				prepState.setString(2, "%" + pieces[i].toUpperCase() + "%");
				prepState.setString(3, "%" + pieces[i].toUpperCase() + "%");
				ResultSet rs = prepState.executeQuery();
				while (rs.next()) {                     //prints userID & name of matches
					System.out.println("[" + rs.getInt(1) + "] " + rs.getString(2) + " " + rs.getString(3));
				}
				System.out.print("\n");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
/*threeDegrees takes two userIDs and attempts to find a path of 3 or fewer hops
        between them using friendships as connections. Like the friendship
        method, this examines friendships with each friend in either position
        (1st or 2nd) to catch all cases properly.
*/
	public static void threeDegrees(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter User 1: ");             //prompt user for userIDs
		String user1 = br.readLine();
		System.out.print("Enter User 2: ");
		String user2 = br.readLine();
		PreparedStatement prepState = null;             //preparing/executing query for a path in 1 configuration
		String query = "SELECT SYS_CONNECT_BY_PATH(friend2, '->') Path FROM FRIENDSHIPS "
				+ "WHERE status=1 AND friend2=? AND LEVEL <=3 START WITH friend1=? "
				+ "CONNECT BY NOCYCLE PRIOR friend2=friend1 AND LEVEL <=3";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user2);
			prepState.setString(2, user1);
			ResultSet rs = prepState.executeQuery();
			rs.next();
			System.out.println(user1 + rs.getString("Path"));       //displaying path
			rs.close();
			prepState.close();
			System.out.println("Displayed Path");
		} catch (SQLException ex) {
			if (ex.getErrorCode() == 17011) {
				System.out.println("Path found.");
			} else {
				ex.printStackTrace();
			}
		}                                               //preparing/executing query for 2nd configuration
		query = "SELECT SYS_CONNECT_BY_PATH(friend1, '->') Path FROM FRIENDSHIPS "
				+ "WHERE status=1 AND friend1=? AND LEVEL <=3 START WITH friend2=? "
				+ "CONNECT BY NOCYCLE PRIOR friend1=friend2 AND LEVEL <=3";
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user2);
			prepState.setString(2, user1);
			ResultSet rs = prepState.executeQuery();
			rs.next();
			System.out.println(user1 + rs.getString("Path"));       //displaying path
			rs.close();
			prepState.close();
			System.out.println("Displayed Path");
		} catch (SQLException ex) {
			if (ex.getErrorCode() == 17011) {
				System.out.println("Path found.");
			} else {
				ex.printStackTrace();
			}
		}
	}
/* topMessagers takes a number of months and a nubmer of users and 
        searches the database for that number of users who have received the most
        messages and those who have sent the most messages (it does so as separate
        queries to distinguish between the two) over a certain period of time.
        */
	public static void topMessagers(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter Number of Months: ");       //prompt user for input
		int numMonths = Integer.parseInt(br.readLine());
		System.out.print("Enter Number of Top Messagers: ");
		int messagers = Integer.parseInt(br.readLine());
		System.out.print("Top Senders: ");
		Statement state = null;
		String query = "SELECT senderID, COUNT(*) "         //preparing/executing query
				+ "FROM Messages "
				+ "WHERE Messages.dateSent > "
				+ "(SELECT add_months(current_date,-" + numMonths + ") FROM DUAL) "
				+ "GROUP BY senderID "
				+ "ORDER BY COUNT(*) DESC ";
		try {
		  state = dbcon.createStatement();
		  ResultSet rs = state.executeQuery(query);
			System.out.print("\n");
						int i = 0;
						while (i < messagers && rs.next()){     //display top senders				
				System.out.println(rs.getInt(1));
								i++;
			}
		} catch (SQLException e) {
			System.out.println("Error getting top messages");           //perform same operation but for recipients
			e.printStackTrace();
		}
				System.out.print("Top Receivers: ");
						query = "SELECT receiverID, COUNT(*) "
				+ "FROM Messages "
				+ "WHERE Messages.dateSent > "
				+ "(SELECT add_months(current_date,-" + numMonths + ") FROM DUAL) "
				+ "GROUP BY receiverID "
				+ "ORDER BY COUNT(*) DESC ";
		try {
		  state = dbcon.createStatement();
		  ResultSet rs = state.executeQuery(query);
			System.out.print("\n");
						int i = 0;
						while (i < messagers && rs.next()){				
				System.out.println(rs.getInt(1));
								i++;
			}
		} catch (SQLException e) {
			System.out.println("Error getting top messages");
			e.printStackTrace();
		}
	}
/*dropUser takes a userID and drops that user's record from the USER table, cascasding
        to remove that user from other tables as necessary
        */
	public static void dropUser(Connection dbcon) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter user ID: ");        //prompt user for input
		String user = br.readLine();
		PreparedStatement prepState = null;
		String query = "DELETE FROM USERS WHERE userID=?";      //prepare/execute statement
		try {
			prepState = dbcon.prepareStatement(query);
			prepState.setString(1, user);
			prepState.executeUpdate();
			prepState.close();
			dbcon.commit();
			System.out.println("Dropped User");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		Console console = System.console();
		console.printf("Enter username: ");
		String username = console.readLine();

		char passwordArray[] = console.readPassword("Enter password: ");
		String password = new String(passwordArray);
		FaceSpace proj = new FaceSpace(username, password);
	}
}
