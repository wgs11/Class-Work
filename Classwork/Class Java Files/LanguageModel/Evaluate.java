/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package languagemodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 *
 * @author William
 */
public class Evaluate {

    public static void answerQuestions(String questionFile, nGram grams, String mode, Boolean devFlag) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(new File("output.txt"), true));
        } catch (Exception e) {
        }
        try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) {
                if ((!devFlag && (lineNum < 2600)) || (devFlag && lineNum >= 2600)) {

                    line = line.replaceFirst("\\d+..", "<s>");
                    line = line.replace("[", "");
                    line = line.replace("]", "");
                    if (line.endsWith(".")) {
                        line = line.substring(0, line.length() - 1) + "</s>";
                    }
                    
                    String[] words = line.split("\\s+");
                    
                    out.println(line+"\t"+Perplexity.perWordPerplexity(words, grams, mode));
                }
                lineNum++;
            }
        } catch (Exception e) {
        }
        out.flush();
        out.close();
    }
}
