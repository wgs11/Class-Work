import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.*;
public class dbinterface{

    private static Connection connection;
    private static String uname, passwrd;
    private static String query; //currently being used query
    private static Statement statement;
    private static PreparedStatement prepStatement;
    private static ResultSet resultSet;
    private static final String USER_MENU = "1: Add customer\n" + 
        "2: Show customer info, given customer name\n" +
        "3: Find price for flights between two cities\n" +
        "4: Find all routes between two cities\n" +
        "5: Find all routes between two cities of a given airline\n" +
        "6: Find all routes with available seats between two cities on given day\n" +
        "7: For a given airline, find all routes with available seats between two cities on given day\n" +
        "8: Add reservation\n" +
        "9: Show reservation info, given reservation number\n" +
        "0: Buy ticket from existing reservation\n" +
        "q: Quit";

    private static final String ADMIN_MENU = "1: Erase the database\n" +
        "2: Load airline information\n" +
        "3: Load schedule information\n" +
        "4: Load pricing information\n" +
        "5: Load plane information\n" +
        "6: Generate passenger manifest for specific flight on given day\n" +
        "q: Quit";

    public static void main(String[] args){
        System.out.println("hello world");


        connectToDB();
        

        System.out.println("are you an admin? y/n");
        char in = '0';
        try{
            in = (char) System.in.read();
        }
        catch(Exception e2){
            System.out.println("couldn't read that " + e2);
        }

        if(in == 'y'){
            adminInterface();
        }
        else{
            userInterface();
        }

        //close the connection
        closeDB();
    }

    public static void closeDB(){
        try{
            connection.close();
        }
        catch(Exception e){
            System.out.println("could not close the DB");
        }
    }


    public static void connectToDB(){
        uname = "cas275";
        passwrd = "pitt1787";

        try{
            System.out.println("Connecting . . .");
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
            connection = DriverManager.getConnection(url, uname, passwrd);
        }
        catch(Exception e){
            System.out.println("error connecting + " + e);
            e.printStackTrace();
            connection = null;
        }

    }

    // private static final String ADMIN_MENU = "1: Erase the database\n" +
    // "2: Load airline information\n" +
    // "3: Load schedule information\n" +
    // "4: Load pricing information\n" +
    // "5: Load plane information\n" +
    // "6: Generate passenger manifest for specific flight on given day\n" +
    // "q: Quit";

    public static void adminInterface(){

        System.out.println("~Admin menu~");
        System.out.println(ADMIN_MENU);

        char in = 'z';

        try{
            in = (char) System.in.read();
            while(in == '\n'){
                in = (char) System.in.read();
            }
        }
        catch(Exception e){
            System.out.println("could not read");
        }

        while(in!='q'){
            Scanner adminScan = new Scanner(System.in);
			switch(in){
				case '1':
					deleteTables();
					break;
				case '2': //insert airline data
					System.out.println("Please enter full path to airline data file");
					String airFile = adminScan.next();
					importAirlines(airFile);
					break;
				case '3':		
					System.out.println("Please enter full path to scheduling data file");
					String flightFile = adminScan.next();
					importFlights(flightFile);
					break;
					
				case '4':
					System.out.println("Do you want to: \n" +
                        "L: Load pricing information\n" +
                        "C: Change the price of an existing fight");
					String priceChoice = adminScan.next();
					if(priceChoice.equals("L"))
					{
						System.out.println("Please enter full path to pricing information");
						String priceFile = adminScan.next();
						importPrice(priceFile);
						
					}
					else if(priceChoice.equals("C"))
					{
						System.out.println("Please enter departure city, arrival city, high price, and low price separated by spaces");
						String depCity = adminScan.next();
						String arrCity = adminScan.next();
						String high = adminScan.next();
						String low = adminScan.next();
						
						changePrice(depCity, arrCity, high, low);
					}
					else
					{
						System.out.println("Invalid Choice");
					}
					break;
					
				case '5':
					System.out.println("Please enter full path to plane data file");
					String planeFile = adminScan.next();
					importPlanes(planeFile);				
					break;
					
				case '6':
					System.out.println("Please enter flight number and date, separated by spaces");
					String fNumber = adminScan.next();
					String fDate = adminScan.next();
					printManifest(fNumber, fDate);
					break;
					
				case 'q':
					System.out.println("Thank you for your time\nExiting.....");
					break;
					
				case 'h':
					System.out.println(ADMIN_MENU);
					break;
				
				default:
					System.out.println("Invalid Command");
					break;
			}		

            try{
                in = (char) System.in.read();
                while(in == '\n'){
                    in = (char) System.in.read();
                }
            }
            catch(Exception e1){
                System.out.println("could not read");
                in = 'z';
            }

        }

    }
	
	public static void deleteTables(){
		try{
			String[] tables = {"Reservation_details",  "Reservation", "Customer", "Price", "Flight", "Plane", "Airline"};
			statement = connection.createStatement();
			String deleteQuery = "DELETE FROM ";
			for(int i = 0; i < tables.length; i++)
			{
				statement.executeQuery(deleteQuery + tables[i]);
			}
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void importAirlines(String airFile){
		query = "INSERT INTO Airline VALUES (?,?,?,?)";
		String line;
		String[] lineSplit;
		try{
			BufferedReader read = new BufferedReader(new FileReader(airFile));
			prepStatement = connection.prepareStatement(query);
			while((line = read.readLine()) != null)
			{
				lineSplit = line.split(",");
				prepStatement.setString(1, lineSplit[0]);
				prepStatement.setString(2, lineSplit[1]);
				prepStatement.setString(3, lineSplit[2]);
				prepStatement.setInt(4, Integer.parseInt(lineSplit[3]));

				prepStatement.executeUpdate();
			}
		}catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	public static void importFlights(String flightFile){
		query = "INSERT INTO Flight VALUES (?,?,?,?,?,?,?,?)";
		String line;
		String[] lineSplit;
		try{
			BufferedReader read = new BufferedReader(new FileReader(flightFile));
			prepStatement = connection.prepareStatement(query);
			while((line = read.readLine()) != null)
			{
				lineSplit = line.split(",");
				prepStatement.setString(1, lineSplit[0]);
				prepStatement.setString(2, lineSplit[1]);
				prepStatement.setString(3, lineSplit[2]);
				prepStatement.setString(4, lineSplit[3]);
				prepStatement.setString(5, lineSplit[4]);
				prepStatement.setString(6, lineSplit[5]);
				prepStatement.setString(7, lineSplit[6]);
				prepStatement.setString(8, lineSplit[7]);

				prepStatement.executeUpdate();
			}
		}catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void importPrice(String priceFile){
		query = "INSERT INTO Price VALUES (?,?,?,?,?)";
		String line;
		String[] lineSplit;
		try{
			BufferedReader read = new BufferedReader(new FileReader(priceFile));
			prepStatement = connection.prepareStatement(query);
			while((line = read.readLine()) != null)
			{
				lineSplit = line.split(",");
				prepStatement.setString(1, lineSplit[0]);
				prepStatement.setString(2, lineSplit[1]);
				prepStatement.setString(3, lineSplit[2]);
				prepStatement.setInt(4, Integer.parseInt(lineSplit[3]));
				prepStatement.setInt(5, Integer.parseInt(lineSplit[4]));

				prepStatement.executeUpdate();
			}
		}catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void changePrice(String depCity, String arrCity, String high, String low){
		query = "UPDATE Price SET high_price = ?, low_price = ? WHERE departure_city = ? AND arrival_city = ?";
		try{
			prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, Integer.parseInt(high));
			prepStatement.setInt(2, Integer.parseInt(low));
			prepStatement.setString(3, depCity);
			prepStatement.setString(4, arrCity);

			prepStatement.executeUpdate();
		}catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void importPlanes(String planeFile){
		query = "INSERT INTO Plane VALUES (?,?,?,?,?,?)";
		String line;
		String[] lineSplit;

		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM/DD/YYYY");

		try{
			BufferedReader read = new BufferedReader(new FileReader(planeFile));
			prepStatement = connection.prepareStatement(query);
			while((line = read.readLine()) != null)
			{
				lineSplit = line.split(",");

				prepStatement.setString(1, lineSplit[0]);
				prepStatement.setString(2, lineSplit[1]);
				prepStatement.setInt(3, Integer.parseInt(lineSplit[2]));
				java.sql.Date date = new java.sql.Date (df.parse(lineSplit[3]).getTime());
				prepStatement.setDate(4, date);
				prepStatement.setInt(5, Integer.parseInt(lineSplit[4]));
				prepStatement.setString(6, lineSplit[5]);

				prepStatement.executeUpdate();
			}
		}catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void printManifest(String fNumber, String fDate){
		query = "SELECT salutation, first_name, last_name FROM Customer WHERE cid IN( SELECT cid FROM Reservation WHERE reservation_number IN ( SELECT reservation_number FROM Reservation_details WHERE flight_number = ? AND flight_date = to_date(?, 'MM/DD/YYYY')))";
		// query = "SELECT salutation, first_name, last_name FROM Customer WHERE cid IN( SELECT cid FROM Reservation WHERE reservation_number IN ( SELECT reservation_number FROM Reservation_details WHERE flight_number = ? AND flight_date = ?))";
		try{
			prepStatement = connection.prepareStatement(query);
			prepStatement.setString(1, fNumber);
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM/DD/YYYY");
			java.sql.Date datef = new java.sql.Date (df.parse(fDate).getTime());
			prepStatement.setString(2, fDate);
			// prepStatement.setDate(2, datef);

			resultSet = prepStatement.executeQuery();
			while (resultSet.next())
			{
				System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
			}

		}catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
    public static void insertUserQuery(String salutation, String fname, String lname, String cc, String expdate, String street, String city, String state, String pn, String email) {

        try {
            String insCust = "INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?,?,?,?, NULL)";
            PreparedStatement putCust = connection.prepareStatement(insCust);
            Random rand = new Random();
            int n = rand.nextInt(99999999) + 10000000;
            String cid = Integer.toString(n);
            putCust.setString(1, cid);
            putCust.setString(2, salutation);
            putCust.setString(3, fname);
            putCust.setString(4, lname);
            putCust.setString(5, cc);
            putCust.setString(6, expdate);
            putCust.setString(7, street);
            putCust.setString(8, city);
            putCust.setString(9, state);
            putCust.setString(10, pn);
            putCust.setString(11, email);
            putCust.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static boolean findUserQuery(String fname, String lname) {
        try {
            String findCust = "SELECT * FROM Customer WHERE first_name = ? AND last_name = ?";
            PreparedStatement checkcust = connection.prepareStatement(findCust);
            checkcust.setString(1, fname);
            checkcust.setString(2, lname);
            ResultSet rs = checkcust.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString(1) + " "
                        + rs.getString(2) + " "
                        + rs.getString(3) + " "
                        + rs.getString(4) + " "
                        + rs.getString(5) + " "
                        + rs.getDate(6) + " "
                        + rs.getString(7) + " "
                        + rs.getString(8) + " "
                        + rs.getString(9) + " "
                        + rs.getString(10) + " "
                        + rs.getString(11));
                return true;
            } else {
                System.out.println("not found");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    public static void findUser(Scanner scan) {
        try {
            System.out.println("Find User Information");
            System.out.print("Please enter first name: ");
            String fname = scan.nextLine();
            System.out.print("Please enter last name: ");
            String lname = scan.nextLine();
            findUserQuery(fname,lname);
        } catch (Exception e) {
            //Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
            e.printStackTrace();
        }
    }
    
    
       public static void createUser(Scanner scan) {
        try { 
                    System.out.println("Create New User");
                    System.out.print("Please enter a salutation: ");
                    String salutation = scan.nextLine();
                    System.out.print("Please enter first name: ");
                    String fname = scan.nextLine();
                    System.out.print("Please enter last name: ");
                    String lname = scan.nextLine();
                    System.out.print("Please enter street name: ");
                    String street = scan.nextLine();
                    System.out.print("Please enter city name: ");
                    String city = scan.nextLine();
                    System.out.print("Please enter state abbreviation: ");
                    String state = scan.nextLine();
                    System.out.print("Please enter phone number: ");
                    String pn = scan.nextLine();
                    System.out.print("Please enter email address: ");
                    String email = scan.nextLine();
                    System.out.print("Please enter credit card number: ");
                    String cc = scan.nextLine();
                    System.out.print("Please enter card expiration date: ");
                    String expdate = scan.nextLine();
                    if (findUserQuery(fname, lname)){
                        System.out.println("Sorry, that user already exists in the system.");
                    }
                    else{
                        insertUserQuery(salutation,fname,lname,cc,expdate,street,city,state,pn,email);
                    }
//                    String findCust = "SELECT * FROM Customer WHERE first_name = ? AND last_name = ?";
//                    PreparedStatement checkcust = connection.prepareStatement(findCust);
//                    checkcust.setString(1,fname);
//                    checkcust.setString(2,lname);
//                    ResultSet rs = checkcust.executeQuery();
//                    if (rs.next()){ // if we got a result, then someone is already in the db
//                        
//                        
//                    }
//                    else{
//                        insertUserQuery(salutation,fname,lname,cc,expdate,street,city,state,pn,email);
//
//                    }
                } catch (Exception e){//(SQLException ex) {
                    // Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
					e.printStackTrace();
                }
    }   
    public static void findPriceQuery(String origin, String dest) {
        try {
            String findprice = "SELECT high_price, low_price FROM Price WHERE departure_city = ? AND arrival_city = ?";
            PreparedStatement getPrice = connection.prepareStatement(findprice);
            getPrice.setString(1, origin);
            getPrice.setString(2, dest);
            ResultSet rs = getPrice.executeQuery();
            String high_price_to = "0";
            String high_price_from = "0";
            String low_price_to = "0";
            String low_price_from = "0";
            boolean to = false;
            boolean from = false;
            String output = "";
            while (rs.next()) {
                high_price_to = rs.getString("high_price");
                low_price_to = rs.getString("low_price");
                output = String.format("The high cost from %s to %s is %s", origin, dest, high_price_to);
                System.out.println(output);
                output = String.format("The low cost from %s to %s is %s", origin, dest, low_price_to);
                System.out.println(output);
                to = true;
            }
            getPrice = connection.prepareStatement(findprice);
            getPrice.setString(1, dest);
            getPrice.setString(2, origin);
            rs = getPrice.executeQuery();
            while (rs.next()) {
                high_price_from = rs.getString("high_price");
                low_price_from = rs.getString("low_price");
                output = String.format("The high cost from %s to %s is %s", dest, origin, high_price_from);
                System.out.println(output);
                output = String.format("The low cost from %s to %s is %s", dest, origin, low_price_from);
                System.out.println(output);
                from = true;
            }
            if (to && from) {
                int high_round = Integer.valueOf(high_price_to) + Integer.valueOf(high_price_from);
                int low_round = Integer.valueOf(low_price_to) + Integer.valueOf(low_price_from);
                output = String.format("The high price for a round trip from %s to %s is %d", origin, dest, high_round);
                System.out.println(output);
                output = String.format("The low price for a round trip from %s to %s is %d", origin, dest, low_round);
                System.out.println(output);
            }
            getPrice = connection.prepareStatement(findprice);
            getPrice.setString(1, dest);
            getPrice.setString(2, origin);
            rs = getPrice.executeQuery();
            while (rs.next()) {
                high_price_from = rs.getString("high_price");
                low_price_from = rs.getString("low_price");
                output = String.format("The high cost from %s to %s is %s", dest, origin, high_price_from);
                System.out.println(output);
                output = String.format("The low cost from %s to %s is %s", dest, origin, low_price_from);
                System.out.println(output);
                from = true;
            }
            if (to && from) {
                int high_round = Integer.valueOf(high_price_to) + Integer.valueOf(high_price_from);
                int low_round = Integer.valueOf(low_price_to) + Integer.valueOf(low_price_from);
                output = String.format("The high price for a round trip from %s to %s is %d", origin, dest, high_round);
                System.out.println(output);
                output = String.format("The low price for a round trip from %s to %s is %d", origin, dest, low_round);
                System.out.println(output);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public static void findPriceInfo(Scanner scan) {
            System.out.println("Find Price Information");
            System.out.print("Please enter origin city: ");
            String origin = scan.next();
            System.out.print("Please enter destination city: ");
            String dest = scan.next();
            findPriceQuery(origin,dest);
            
    }
    public static void findRoutesQuery(String origin, String dest) {
        try {
            String directQuery = "SELECT flight_number, departure_city, arrival_city,departure_time,arrival_time FROM Flight WHERE departure_city = ? AND arrival_city = ?";
            PreparedStatement findDirect = connection.prepareStatement(directQuery);
            findDirect.setString(1, origin);
            findDirect.setString(2, dest);
            ResultSet rs = findDirect.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
            while (rs.next()) {
				for(int i = 1; i <= cols; i++){
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
            }
            String indirectQuery = "select * from flight f1 JOIN flight f2 on f1.arrival_city = f2.departure_city AND f1.airline_id = f2.airline_id WHERE TO_NUMBER(f1.arrival_time)+100 <= TO_NUMBER(f2.departure_time) AND f1.departure_city = ? AND f2.arrival_city = ?";
            PreparedStatement findIndirect = connection.prepareStatement(indirectQuery);
            findIndirect.setString(1, origin);
            findIndirect.setString(2, dest);
            rs = findIndirect.executeQuery();
			rsmd = rs.getMetaData();
			cols = rsmd.getColumnCount();
            while (rs.next()) {
				for(int i = 1; i <= cols; i++){
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void findRoutes(Scanner scan) {
        //select * from flight f1 JOIN flight f2 on f1.arrival_city = f2.departure_city AND f1.airline_id = f2.airline_id;
        System.out.println("Find Routes");
        System.out.print("Please enter origin city: ");
        String origin = scan.next();
        System.out.print("Please enter destination city: ");
        String dest = scan.next();
        findRoutesQuery(origin, dest);
    }

    public static void airlineRouteQuery(String origin, String dest, String airline) {
        try {
            String directQuery = "SELECT flight_number, departure_city, arrival_city,departure_time,arrival_time FROM Flight WHERE departure_city = ? AND arrival_city = ? AND airline_id = ?";
            PreparedStatement findDirect = connection.prepareStatement(directQuery);
            findDirect.setString(1, origin);
            findDirect.setString(2, dest);
            findDirect.setString(3, airline);
            ResultSet rs = findDirect.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
            while (rs.next()) {
				for(int i = 1; i <= cols; i++){
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
            }
            String indirectQuery = "select * from flight f1 JOIN flight f2 on f1.arrival_city = f2.departure_city AND f1.airline_id = f2.airline_id WHERE TO_NUMBER(f1.arrival_time)+100 <= TO_NUMBER(f2.departure_time) AND f1.departure_city = ? AND f2.arrival_city = ? AND f1.airline_id = ?";
            PreparedStatement findIndirect = connection.prepareStatement(indirectQuery);
            findIndirect.setString(1, origin);
            findIndirect.setString(2, dest);
            findIndirect.setString(3, airline);
            rs = findIndirect.executeQuery();
			rsmd = rs.getMetaData();
			cols = rsmd.getColumnCount();
            while (rs.next()) {
				for(int i = 1; i <= cols; i++){
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
            }

        } catch (Exception e) {
            // Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
            e.printStackTrace();
        }
    }

    public static void findAirlineRoutes(Scanner scan) {
        System.out.println("Find Routes");
        System.out.print("Please enter origin city: ");
        String origin = scan.next();
        System.out.print("Please enter destination city: ");
        String dest = scan.next();
        System.out.print("Please enter airline: ");
        String airline = scan.next();
        airlineRouteQuery(origin, dest, airline);
    }

    public static void availableSeatQuery(String origin, String dest, String ds) {
        try {
            Calendar c = Calendar.getInstance();
            // DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/DD/YYYY");
            java.sql.Date date = new java.sql.Date(formatter.parse(ds).getTime());
            c.setTime(date);
            //gets the count of reservations that fit the flight number
            String directQuery = "SELECT f.flight_number, f.departure_city, f.arrival_city, f.departure_time, f.arrival_time, f.weekly_schedule FROM Flight f JOIN Plane p ON f.plane_type = p.plane_type WHERE f.departure_city = ? AND f.arrival_city = ? AND p.plane_capacity > (SELECT COUNT(*) FROM ( SELECT f.flight_number FROM Flight f JOIN Reservation_details d ON f.flight_number = d.flight_number WHERE d.flight_date = ?))";
            PreparedStatement findDirect = connection.prepareStatement(directQuery);
            findDirect.setString(1, origin);
            findDirect.setString(2, dest);
            findDirect.setDate(3, date);
            ResultSet rs = findDirect.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            while (rs.next()) {
                String schedule = rs.getString("weekly_schedule");
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                if (!(schedule.charAt(dayOfWeek - 1) == '-')) {
                    for (int i = 1; i <= cols; i++) {
                        System.out.print(rs.getString(i) + " ");
                    }
                    System.out.println();
                }

            }
            String indirectQuery = "SELECT * FROM (Flight f1 JOIN Plane p1 ON f1.plane_type = p1.plane_type) JOIN (Flight f2 JOIN Plane p2 ON f2.plane_type = p2.plane_type) ON f1.arrival_city = f2.departure_city WHERE TO_NUMBER(f1.arrival_time)+100 <= TO_NUMBER(f2.departure_time) AND f1.arrival_city = ? AND f2.departure_city = ? AND p1.plane_capacity > (SELECT COUNT(*) FROM ( SELECT f1.flight_number FROM Flight f1 JOIN Reservation_details d ON f1.flight_number = d.flight_number WHERE d.flight_date = ?))	AND p2.plane_capacity > (SELECT COUNT(*) FROM (	SELECT f2.flight_number FROM Flight f2 JOIN Reservation_details d ON f2.flight_number = d.flight_number	WHERE d.flight_date = ?))";
            PreparedStatement findIndirect = connection.prepareStatement(indirectQuery);
            findIndirect.setString(1, origin);
            findIndirect.setString(2, dest);
            findIndirect.setDate(3, date);
            findIndirect.setDate(4, date);
            rs = findIndirect.executeQuery();
            rsmd = rs.getMetaData();
            cols = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            // Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
            e.printStackTrace();
        }
    }

    public static void availableSeatRoute(Scanner scan) {
        System.out.println("Find Routes");
        System.out.print("Please enter origin city: ");
        String origin = scan.next();
        System.out.print("Please enter destination city: ");
        String dest = scan.next();
        // System.out.print("Please enter airline: ");
//        String airline = scan.next();
        System.out.print("Please enter a date:");
        String ds = scan.next();
        availableSeatQuery(origin, dest, ds);
    }
    public static void availableSeatAirlineQuery(String origin, String dest, String airline, String ds){
	try{
        Calendar c = Calendar.getInstance();
		// DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/DD/YYYY");
		java.sql.Date date = new java.sql.Date (formatter.parse(ds).getTime());
		c.setTime(date);
		//gets the count of reservations that fit the flight number
		String directQuery = "SELECT f.flight_number, f.departure_city, f.arrival_city, f.departure_time, f.arrival_time, f.weekly_schedule FROM Flight f JOIN Plane p ON f.plane_type = p.plane_type AND f.airline_id = ? WHERE f.departure_city = ? AND f.arrival_city = ? AND p.plane_capacity > (SELECT COUNT(*) FROM ( SELECT f.flight_number FROM Flight f JOIN Reservation_details d ON f.flight_number = d.flight_number WHERE d.flight_date = ?))";
		PreparedStatement findDirect = connection.prepareStatement(directQuery);
		findDirect.setString(1, airline);
		findDirect.setString(2, origin);
		findDirect.setString(3, dest);
		findDirect.setDate(4, date);
		ResultSet rs = findDirect.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		while (rs.next()) {
			String schedule = rs.getString("weekly_schedule");
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			if (!(schedule.charAt(dayOfWeek - 1) == '-')) {
				for(int i = 1; i <= cols; i++){
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
			}

		}
		String indirectQuery = "SELECT * FROM (Flight f1 JOIN Plane p1 ON f1.plane_type = p1.plane_type) JOIN (Flight f2 JOIN Plane p2 ON f2.plane_type = p2.plane_type) ON f1.arrival_city = f2.departure_city AND f1.airline_id = ? AND f2.airline_id = f1.airline_id WHERE TO_NUMBER(f1.arrival_time)+100 <= TO_NUMBER(f2.departure_time) AND f1.arrival_city = ? AND f2.departure_city = ? AND p1.plane_capacity > (SELECT COUNT(*) FROM ( SELECT f1.flight_number FROM Flight f1 JOIN Reservation_details d ON f1.flight_number = d.flight_number WHERE d.flight_date = ?))	AND p2.plane_capacity > (SELECT COUNT(*) FROM (	SELECT f2.flight_number FROM Flight f2 JOIN Reservation_details d ON f2.flight_number = d.flight_number	WHERE d.flight_date = ?))";
		PreparedStatement findIndirect = connection.prepareStatement(indirectQuery);
		findIndirect.setString(1, airline);
		findIndirect.setString(2, origin);
		findIndirect.setString(3, dest);
		findIndirect.setDate(4, date);
		findIndirect.setDate(5, date);
		rs = findIndirect.executeQuery();
		rsmd = rs.getMetaData();
		cols = rsmd.getColumnCount();
		while (rs.next()) {
			for(int i = 1; i <= cols; i++){
				System.out.print(rs.getString(i) + " ");
			}
			System.out.println();
		}

	} catch (Exception e) {
		// Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
		e.printStackTrace();
	}
    }
    public static void availableSeatRouteAirline(Scanner scan){
        System.out.println("Find Routes");
        System.out.print("Please enter origin city: ");
        String origin = scan.next();
        System.out.print("Please enter destination city: ");
        String dest = scan.next();
        System.out.print("Please enter airline: ");
        String airline = scan.next();
        System.out.print("Please enter a date:");
        String ds = scan.next();
        availableSeatAirlineQuery(origin, dest, airline, ds);
    }

    public static void reservationQuery(String resnum) {
        try {

            String resquery = "SELECT * FROM Reservation_details WHERE reservation_number = ?";
            PreparedStatement findlegs = connection.prepareStatement(resquery);
            findlegs.setString(1, resnum);
            ResultSet rs = findlegs.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            if (!rs.next()) {
                System.out.println("Sorry, that wasn't a valid reservation number.");
            } else {
                while (rs.next()) {
                    for (int i = 1; i <= cols; i++) {
                        System.out.print(rs.getString(i) + " ");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            // Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
            e.printStackTrace();
        }
    }

    public static void findReservation(Scanner scan) {
        System.out.println("Find Reservation Info:");
        System.out.print("Please enter reservation number: ");
        String resnum = scan.next();
        reservationQuery(resnum);
    }

    public static void buyTicketQuery(String resnum) {
        try {

            String resquery = "UPDATE Reservation SET tickted = 1 WHERE reservation_number = ?";
            PreparedStatement updateRes = connection.prepareStatement(resquery);
            updateRes.setString(1, resnum);
            updateRes.executeUpdate();
        } catch (Exception e) {
            // Logger.getLogger(dbinterface.class.getName()).log(Level.SEVERE, null, ex);
            e.printStackTrace();
        }
    }

    public static void buyTicket(Scanner scan) {
        System.out.println("Ticket Reservation:");
        System.out.print("Please enter reservation number: ");
        String resnum = scan.next();
        buyTicketQuery(resnum);
    }
    public static void reservationQuery(String flightnum, String date, Integer legnum) {
        try {
            String resquery = "SELECT COUNT(reservation_number) FROM Reservation_details WHERE flight_number = ?";
            PreparedStatement checkFlight = connection.prepareStatement(resquery);
            checkFlight.setString(1, flightnum);
            ResultSet rs = checkFlight.executeQuery();
            int seatsTaken = rs.getInt("total");
            String seatlimit = "SELECT plane_capacity FROM Plane NATURAL JOIN Flight WHERE Flight.flight_number = ?";
            PreparedStatement seatlimitQuery = connection.prepareStatement(seatlimit);
            seatlimitQuery.setString(1, flightnum);
            ResultSet sl = seatlimitQuery.executeQuery();
            int limit = sl.getInt("plane_capacity");
            if (seatsTaken < limit) {
                String resnumQuery = "SELECT reservation_number FROM Reservation ORDER BY DESC LIMI 1";
                PreparedStatement getResNum = connection.prepareStatement(resnumQuery);
                ResultSet rn = getResNum.executeQuery();
                int high_rn = Integer.valueOf(rn.getString("reservation_number"));
                String new_rn = Integer.toString(high_rn + 1);
                String addLegQuery = "INSERT INTO Reservation_details VALUES(?,?,?,?)";
                PreparedStatement addLeg = connection.prepareStatement(addLegQuery);
                addLeg.setString(1, new_rn);
                addLeg.setString(2, flightnum);
                addLeg.setString(3, date);
                addLeg.setInt(4, legnum);
            }
            else{
                System.out.println("Sorry, there are no seats available for flight "+flightnum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addReservation(Scanner scan){
        System.out.println("Make Reservation");
        String flightnum = "";
        String flightdate = "";
        int numLegs = 0;
        while(flightnum != "0" && numLegs <=4){
            System.out.print("Enter Flight Number: ");
            flightnum = scan.nextLine();
            System.out.print("Enter Flight Date: ");
            flightdate = scan.nextLine();
            reservationQuery(flightnum, flightdate, numLegs);
            numLegs++;
        }
        
    }
    public static void userInterface() {//throws ParseException{
        Scanner scan;
        scan = new Scanner(System.in);
        System.out.println("User menu");
        System.out.println(USER_MENU);

        char in = 'z';
        try{
            in = (char) System.in.read();
            while(in == '\n'){
                in = (char) System.in.read();
            }   
        }
        catch(Exception e1){
            System.out.println("read error" + e1);
        }
        while(in != 'q'){
            if(scan.hasNextLine()){
                scan.nextLine();
            }
            if(in == '1'){
                createUser(scan);
            }
            else if(in == '2'){
                findUser(scan);
            }
            else if(in == '3'){
                findPriceInfo(scan);
            }
            else if(in == '4'){
                findRoutes(scan);   
            }
            else if(in == '5'){
                findAirlineRoutes(scan);       
            }
            else if(in == '6'){
                availableSeatRoute(scan);
            }
            else if(in == '7'){
                availableSeatRouteAirline(scan);
            }
            else if(in == '8'){
                addReservation(scan);
            }
            else if(in == '9'){
                findReservation(scan);
            }
            else if(in == '0'){
                buyTicket(scan);
            }
            else if(in != 'q'){
                System.out.println("invalid");
            }
            //read input
            System.out.println(USER_MENU);
            try{
                in = (char) System.in.read();
                while(in == '\n'){
                    in = (char) System.in.read();
                }
            }
            catch(Exception e3){
                System.out.println("cannot read");
                in = 'z';
            }
            
        }
        System.out.println("quitting");

    }
}

