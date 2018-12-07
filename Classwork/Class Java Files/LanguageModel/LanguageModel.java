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
import java.util.*;

/**
 *
 * @author William
 */
public class LanguageModel {

    public static void main(String[] args) {
        String mode = args[0];
        String train = args[1];
        String dev = args[2];
        String test = null;
        nGram training = null;
        PrintWriter out = null;
        try {                   //clear the output file
            out = new PrintWriter(new FileOutputStream(new File("output.txt")));
        } catch (Exception e) {
        }
        out.flush();
        out.close();
        try (BufferedReader br = new BufferedReader(new FileReader(train))) {
            String line = br.readLine();                    //check for preprocessing
            if (!(line.substring(0, 3).equals("<s>"))) {
                preProcess.process(train);
                training = Training.getVocab("valtozok.txt");
            } else {
                training = Training.getVocab(train);
            }
        } catch (Exception e) {
        }
        if (args.length > 3) {
            test = args[3];
            Evaluate.answerQuestions(dev, training, mode, false);
            Evaluate.answerQuestions(test, training, mode, true);
        } else {
            Evaluate.answerQuestions(dev, training, mode, false);
            Evaluate.answerQuestions(dev, training, mode, true);
        }

    }

}
