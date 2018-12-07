// CS1555 Term Project
// FINAL MILESTONE (YAY!)
//
// group 13

import java.io.*;

public class Part3Driver{

    // Strings of input files
    private static final String loadAirlineFile = "airlines.csv";
    private static final String loadFlightFile  = "flights.csv";
    private static final String loadPlaneFile   = "planes.csv";
    private static final String loadPrice       = "price.csv";

    public static void main(String[] args){
        
        System.out.println("==================================================================================");
        System.out.println("                             starting tests                                       ");
        System.out.println("==================================================================================");

        // Connect to the DB
        dbinterface.connectToDB();

        // Call admin1 (erase the DB)
        // COMMENTED OUT FOR TESTS, THIS IS DANGEROUS IF YOU'RE NOT READY FOR IT
        // System.out.println("Erase the DB");
        // dbinterface.deleteTables(); 

        // Call admin2 (load airline information)
        try{
            System.out.println("\n***import airlines***");
            dbinterface.importAirlines(loadAirlineFile);
        } catch (Exception e1){
            System.out.println("[ERROR] admin2 " + e1);
        }

        // Call admin3 (load schedule)
        try{
            System.out.println("\n***import flights***");
            dbinterface.importFlights(loadFlightFile);
        } catch (Exception e2){
            System.out.println("[ERROR] admin3 " + e2);
        }

        // Call admin4 (load pricing)
        try{
            System.out.println("\n***load Pricing***");
            dbinterface.importPrice(loadPrice);
        } catch (Exception e3){
            System.out.println("[ERROR] admin4 " + e3);
        }

        // Call admin5 (load plane information)
        try{
            System.out.println("\n***load Planes***");
            dbinterface.importPlanes(loadPlaneFile);
        } catch (Exception e4){
            System.out.println("[ERROR] admin5 " + e4);
        }

        // Let's not call admin 6 just yet, as there aren't any passengers! 
        // Call user1 (add customer)
        // Let's do 50-100 loops
        System.out.println("\n***insert users***");
        try{
            for(int i=0; i<50; i++){
                System.out.println("adding user " + i);
                // we can just use a toString of i to make names, we don't really care who our custs are
                // let's make up an evenly distributed salutation
                String salutation;
                if(i%3 == 0){
                    salutation = "Mr";
                } else if(i%3 == 2){
                    salutation = "Mrs";
                }
                else{
                    salutation = "Ms";
                }

                // let's make numerical based name
                String fname = (new Integer(i)).toString();
                String lname = fname;

                // yes, the credit card number is 1
                String cc = "1";

                // wow, everyone's credit cards have expired . . .
                String expdate = "01-MAY-01";   

                // huh, everyone lives at a different house on the same street
                String street = fname + " " + "first street";

                // ewww, cleveland
                String city;
                String state;
                if(i%2 == 0){
                    city = "Pittsburgh";
                    state = "PA";
                } else{
                    city = "Cleveland";
                    state = "OH";
                }
                // that phone number looks fake...
                String pn = "5555551234";

                // looks like everyone has their own email domain.  Cool
                String email = fname + "@" + lname + ".com";

                // ok, enough fun, let's add them back to the DB
                dbinterface.insertUserQuery(salutation,fname,lname,cc,expdate,street,city,state,pn,email);
                System.out.println("user " + i + " added");

            }
        } catch (Exception e5){
            System.out.println("[ERROR] cust1 " + e5);
        }
        // Call user2 (show cust, given name)
        // Let's find 20 of the ones we just added
        System.out.println("\n***find users***");
        try{
            for(int i=0; i<20; i++){
                System.out.println("finding customer " + i);
                if(dbinterface.findUserQuery((new Integer(i)).toString(), (new Integer(i)).toString())){
                    System.out.println("found");
                } else{
                    System.out.println("[ERROR] could not find user " + i);
                }
            }
        } catch (Exception e6){
            System.out.println("[ERROR] cust2 " + e6);
        }

        // Call user3 (find prices between 2 cities)
        // This one is harder to loop, unless our city names are number that increment by 1
        // We should do this one 10+ times
        // and should print expected result too
        System.out.println("\n***find prices***");
        try{
            for(int i=1; i<=5; i++){
                for(int j=1; j<=5; j++){
                    if(i != j){
                        System.out.println("finding from " + i + " to " + j);
                        dbinterface.findPriceQuery((new Integer(i)).toString(), (new Integer(j)).toString());
                    }
                }
            }
        } catch (Exception e7){
            System.out.println("[ERROR] cust3 " + e7);
        }
        
        // Call user4 (find all routes between 2 cities)
        // Very similar to the one above 
        // once again 10+
        // and print the expected result too
        System.out.println("\n***find route***");
        try{
            for(int i=1; i<=5; i++){
                for(int j=25; j>=20; j--){
                    if(i != j){
                        System.out.println("finding from " + i + " to " + j);
                        dbinterface.findRoutesQuery((new Integer(i)).toString(), (new Integer(j)).toString());
                    }
                }
            }
        } catch (Exception e8){
            System.out.println("[ERROR] cust4 " + e8);
        }

        // Call user5 (find all routes between 2 cities for a given airline)
        // Let's call this one twice for each airline
        System.out.println("\n***find routes airline***");
        try{
            for(int i=1; i<=4; i++){
                for(int j=25; j>=23; j--){
                    if( i != j){
                        System.out.println("finding from " + i + " to " + j);
                        System.out.println("for airline 001");
                        dbinterface.airlineRouteQuery((new Integer(i)).toString(), (new Integer(j)).toString(), "001");
                        System.out.println("for airline 003");
                        dbinterface.airlineRouteQuery((new Integer(i)).toString(), (new Integer(j)).toString(), "003");
                    }
                }
            }
        } catch (Exception e9){
            System.out.println("[ERROR] cust5 " + e9);
        }

        // Call user6 (find all routes with seats on a given day)
        // Let's call this 10 time now, with no reservations, and call again later
        System.out.println("\n***find routes w/ seats***");
        try{
            //TODO
            System.out.println("TODO");

        } catch (Exception e10){
            System.out.println("[ERROR] user6 " + e10);
        }

        // Call user7 (given airline, routes with seats on a day)
        // Same as above
        System.out.println("\n***find airline routes w/ seats***");
        try{
            //TODO
            System.out.println("TODO");

        } catch (Exception e11){
            System.out.println("[ERROR] user7 " + e11);
        }

        // Call user8 (add reservation)
        // Like 20+ reservations!
        System.out.println("\n***add reservation***");
        try{
            //TODO
            System.out.println("TODO");
        } catch (Exception e12){
            System.out.println("[ERROR] user8 " + e12);
        }

        // Call user 9 (show reservation infor)
        // to verify above 
        System.out.println("\n***show reservation info***");
        try{
            //TODO
            System.out.println("TODO");
        } catch (Exception e13){
            System.out.println("[ERROR] user9 " + e13);
        }

        // Call user6 (find all routes w/ seats) again
        // line 5 times, on flights that have people on them now
        System.out.println("\n**find all routes wi/ seats, again***");
        try{
            //TODO
            System.out.println("TODO");
        } catch (Exception e14){
            System.out.println("[ERROR] user6 " + e14);
        }

        // Call user7 (find all routes w/ seats, airline) again
        // like 5 times, on flights that have people on them now
        System.out.println("\n***find all airline routes w/ seats, again***");
        try{
            //TODO
            System.out.println("TODO");
        } catch (Exception e15){
            System.out.println("[ERROR] user7 " + e15);
        }

        // Call user0 (buy ticket for reservation)
        // call for /Most/ of the reservations
        System.out.println("\n***buy tickets***");
        try{
            //TODO
            System.out.println("TODO");
        } catch (Exception e16){
            System.out.println("[ERROR] user0 " + e16);
        }

        // Call admin6 (passenger manifest)
        // finally we have data
        System.out.println("\n***mainifest***");
        try{
            //TODO
            System.out.println("TODO");
        } catch (Exception e17){
            System.out.println("[ERROR] admin6 " + e17);
        }

        // -----PUT ANY OTHER TESTS BELOW HERE------




        // Close connection   
        dbinterface.closeDB();
        System.out.println("==================================================================================");
        System.out.println("                        testing completed                                         ");
        System.out.println("==================================================================================");
    }


}
