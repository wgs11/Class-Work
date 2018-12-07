/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examanalysis;

import java.util.*;
import java.io.*;

public class ExamAnalysis {

    public static int printFile(String file) {
        int student = 0;
        try {
            Scanner printer = new Scanner(new FileReader(file));
            while (printer.hasNextLine()) {
                student++;
                String s = printer.nextLine();
                if (!s.isEmpty()) {
                    System.out.println("Student #" + student + "'s responses: " + s);

                } else {
                }
            }
        } catch (FileNotFoundException ex) {
        }
        System.out.println("We have reached \"end of file!\"\n\n"
                + "Thank you for the data on " + student + " students.  "
                + "Here's the analysis:");
        return student;
    }

    public static void analyzeStudents(String answers, String file) {
        System.out.println("Student #     Correct     Incorrect     Blank\n"
                + "~~~~~~~~~     ~~~~~~~     ~~~~~~~~~     ~~~~~");

        int student = 0;
        int correct;
        int incorrect;
        int blank;
        try {
            Scanner checker = new Scanner(new FileReader(file));
            while (checker.hasNextLine()) {
                student++;
                correct = incorrect = blank = 0;
                String s = checker.nextLine();
                if (!s.isEmpty()) {
                    for (int i = 0; i < answers.length(); i++) {
                        if (Character.toUpperCase(s.charAt(i))
                                == Character.toUpperCase(answers.charAt(i))) {
                            correct++;
                        } else if (s.charAt(i) == ' ') {
                            blank++;
                        } else {
                            incorrect++;
                        }
                    }

                    System.out.printf("%5s%14s%13s%12s\n",
                            student, correct, incorrect, blank);

                }
            }
        } catch (FileNotFoundException ex) {
        }

    }

    public static void analyzeQuestions(String key, String file, int students) {
        final String[] HEADERS = {
            "A*     B      C      D      E     Blank",
            "A      B*     C      D      E     Blank",
            "A      B      C*     D      E     Blank",
            "A      B      C      D*     E     Blank",
            "A      B      C      D      E*    Blank",
            "A      B      C      D      E     Blank*"};
        System.out.println("QUESTION ANALYSIS   "
                + "(* marks the correct response)\n"
                + "~~~~~~~~~~~~~~~~~");
        for (int i = 0; i < key.length(); i++) {
            char answer = Character.toUpperCase(key.charAt(i));
            String header = "";
            switch (answer) {
                case 'A':
                    header = HEADERS[0];
                    break;
                case 'B':
                    header = HEADERS[1];
                    break;
                case 'C':
                    header = HEADERS[2];
                    break;
                case 'D':
                    header = HEADERS[3];
                    break;
                case 'E':
                    header = HEADERS[4];
                    break;
                case ' ':
                    header = HEADERS[5];
                    break;
            }
            System.out.println("Question #" + (i + 1) + ":\n");
            System.out.printf("%42s\n\n", header);
            getCounts(key, i, file, students);
        }
    }

    public static void getCounts(String answers, int pos, String file, int students) {

        float[] values = new float[6];
        float q = answers.length();;
        try {
            Scanner input = new Scanner(new FileReader(file));
            while (input.hasNextLine()) {
                String s = input.nextLine();
                if (!s.isEmpty()) {
                    if (Character.toUpperCase(s.charAt(pos)) == 'A') {
                        values[0]++;
                    } else if (Character.toUpperCase(s.charAt(pos)) == 'B') {
                        values[1]++;
                    } else if (Character.toUpperCase(s.charAt(pos)) == 'C') {
                        values[2]++;
                    } else if (Character.toUpperCase(s.charAt(pos)) == 'D') {
                        values[3]++;
                    } else if (Character.toUpperCase(s.charAt(pos)) == 'E') {
                        values[4]++;
                    } else if (s.charAt(pos) == ' ') {
                        values[5]++;
                    }
                }
            }
            System.out.print("  ");
            for (int i = 0; i < values.length; i++) {
                System.out.printf("%2.0f     ", values[i]);
            }
            System.out.print("\n\n ");

            for (int i = 0; i < values.length; i++) {
                System.out.printf("%4.1f%%  ", values[i] * 100 / students);
            }
            System.out.print("\n\n\n");
        } catch (FileNotFoundException ex) {

        }

    }

    public static String getFile(String answers) {
        Scanner file = new Scanner(System.in);
        String check = file.nextLine();
        try {
            Scanner verify = new Scanner(new FileReader(check));
            while (verify.hasNextLine()) {
                String s = verify.nextLine();
                if (!s.isEmpty()) {
                    if (s.length() == answers.length()) {
                        return check;
                    } else {
                        System.out.println("I'm sorry, that file doesn't"
                                + " contain valid answers sets.\n "
                                + "Please enter a file with valid answer sets.");
                        return getFile(answers);
                    }
                }
            }
            return check;
        } catch (FileNotFoundException ex) {
            System.out.println("I'm sorry, that file doesn't exist.\n"
                    + "Please enter a valid file name.");
            return getFile(answers);

        }
    }

    public static String getAnswers() {
        Scanner s = new Scanner(System.in);
        String key = s.nextLine();
        for (int i = 0; i < key.length(); i++) {
            if ((key.charAt(i) >= 'A' && key.charAt(i) <= 'F')
                    || (key.charAt(i) >= 'a' && key.charAt(i) <= 'f')
                    || key.charAt(i) == ' ') {

            } else {
                System.out.println("I'm sorry, that isn't a valid format "
                        + "for an answer key.\nPlease type the correct answers"
                        + "to the exam questions one right after the other");
                key = getAnswers();
            }
        }
        return key;
    }

    public static void main(String[] args) {
        System.out.println("Please type the correct answers to the exam"
                + " questions,\n  one right after the other:");
        String answers = getAnswers();
        System.out.println("What is the name of the file containing each student's\n"
                + " responses to the 10 questions?");
        String file = getFile(answers);
        int number = printFile(file);
        System.out.println(answers);
        analyzeStudents(answers, file);
        analyzeQuestions(answers, file, number);
    }

}
