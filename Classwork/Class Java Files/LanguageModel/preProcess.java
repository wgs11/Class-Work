/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package languagemodel;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.regex.*;

/**
 *
 * @author William
 */
public class preProcess {

    public static void process(String file) {       // initialized before loop
        
        PrintWriter out = null;
        StringBuilder paragraph = null;
        String line;
        try {
            out = new PrintWriter("valtozok.txt");
        } catch (FileNotFoundException e) { 
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
           paragraph = new StringBuilder();
           while ((line = br.readLine()) != null){
               if (line.matches(".*\\w.*")) {
                   paragraph.append(line);
               }
               else{
                   printSentences(paragraph.toString(), out);
                   paragraph = new StringBuilder();
               }
            //   out.println(line);
        } 
        }
        catch (Exception e){}

        out.flush();
        out.close();

    }

    public static void printSentences(String paragraph, PrintWriter out) throws FileNotFoundException {
        
        String str = paragraph;
        Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher reMatcher = re.matcher(str);

        while (reMatcher.find()) {
            String output = reMatcher.group();
            output = output.replaceAll("\\,", " ,");
            output = output.replaceAll("\"", "");
            if (output.endsWith(".")) {
                output = output.substring(0, output.length() - 1);
                output = "<s> " + output + " </s>";
                out.println(output);

            }
            
        }

    }
}
