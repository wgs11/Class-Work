/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package noduplicates;
import java.util.*;
import java.io.*;

public class NoDuplicates {
    public static void PrintDuplicates(String file1, String file2){
        double reference = .5;
        try{
            Scanner a = new Scanner(new FileReader(file1));
            PrintWriter b = new PrintWriter(file2);
            while (a.hasNextInt()){
                int s = a.nextInt();
                if (reference != s){
                    b.println(s);
                    reference = s;
                }
                else{}
            }
            a.close();
            b.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Sorry that wasn't a valid file path\n"
                    + " Please input valid files.");
            Scanner input = new Scanner (System.in);
            String new1 = input.nextLine();
            String new2 = input.nextLine();
            PrintDuplicates(new1, new2);
        }
    }
    public static void PrintFiles(String file1, String file2){
        System.out.println("ORIGINAL FILE: "+file1+" contains the values");
        try{
            Scanner r = new Scanner(new FileReader(file1));
            while (r.hasNextLine()){
                String m = r.nextLine();
                System.out.println(m);
            }
        }              
        catch(FileNotFoundException e){}
        System.out.println("OUTPUT FILE: "+file2+" contains the values");
        try{
            Scanner s = new Scanner(new FileReader(file2));
            while (s.hasNextLine()){
                String n = s.nextLine();
                System.out.println(n);
            }
        }
        catch(FileNotFoundException e){}
    }

    public static void main(String[] args) {
        String file1 = args[0];
        String file2 = args[1];
        PrintDuplicates(file1, file2);
        PrintFiles(file1, file2);
    }

}
