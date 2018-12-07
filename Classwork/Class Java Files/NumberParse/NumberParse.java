package numberparse;

import java.util.*;
import java.util.regex.*;
import java.math.*;
import java.io.*;

/**
 *
 * @author William
 */
public class NumberParse {

    private static final String MONTHS = "(January|February|March|April|May|June|July|August|September|October|November|December|Jan.|Feb.|Mar.|Apr.|Aug.|Sept.|Oct.|Nov.|Jan|Feb|Mar|Apr|Aug|Sept|Oct|Nov|Dec)";
    private static final String monthSuf1 = "(\\d|\\d)(\\d\\d\\d\\d)";
    private static final String monthSuf2 = "((\\d|\\d\\d)";
    private static final String monthSuf3 = "(\\d\\d\\d\\d)";
    private static final String NUMBERS = "([1-9][0-9\\,]*)";
    private static final String DECIMALS = "([1-9][\\d\\,]*|0)\\.(\\d+)";
    private static final String NAMES = "(thousand|million|billion)";
    private static final String ORDINALS = "(\\d+)(st|nd|rd|th)";
    private static final String FRACTIONS = "(([1-9][0-9]*\\s)?([1-9]+\\d*)(\\\\\\/)([1-9]+\\d*))";

    public static void printLines(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String check = line;                         //remove commas, we don't care about them and they make regex messier
                check = check.replaceAll("\\%", "percent");
                check = checkDates(check);                                //done
                check = checkCurrency(check);                             //done
                check = checkFractions(check);
                check = checkTemps(check);
                check = checkOrdinal(check);                                //done
                check = checkLengths(check);
                check = checkLocations(check);
                check = checkBasics(check);                                 //done
                System.out.println(check);
            }
        } catch (Exception e) {
        }
    }

    public static String convertOrdinal(String input) {

        return input;
    }

    public static String parseInt(String input) {

        return input;
    }

    public static String checkLocations(String input) {

        return input;
    }

    public static String checkDates(String input) {
        Pattern monthA = Pattern.compile((MONTHS) + "(\\s)(\\d\\d?)(\\s\\,\\s)(\\d\\d\\d\\d)");           //day and year
        Pattern monthB = Pattern.compile((MONTHS) + "(\\s\\,\\s)(\\d\\d\\d\\d)");                         //only year
        Pattern monthC = Pattern.compile((MONTHS) + "(\\s)(\\d\\d?)");                                //only day
        Pattern inYear = Pattern.compile("(in\\s)(\\d\\d\\d\\d)");
        Matcher monthMatchA = monthA.matcher(input);                                    //if months, check for each case
        StringBuffer a = new StringBuffer(input.length());
        String text;
        while (monthMatchA.find()) {
            text = monthMatchA.group(5);
            int year = Integer.valueOf(text);
            if ((year >= 2000) && (year < 2010)) {

                text = monthMatchA.group(1) + " " + EnglishNumberToWords.convertToOrdinal(monthMatchA.group(3)) + " " + EnglishNumberToWords.convert(year);
            } else {
                String month = monthMatchA.group(1);
                text = (month + " " + EnglishNumberToWords.convertToOrdinal(monthMatchA.group(3)) + " " + EnglishNumberToWords.convert(Integer.valueOf(text.substring(0, 2))) + " " + EnglishNumberToWords.convert(Integer.valueOf(text.substring(2, 4))));   //replace 5 with year parse
            }
            monthMatchA.appendReplacement(a, monthMatchA.quoteReplacement(text));
        }
        monthMatchA.appendTail(a);
        text = (a.toString());
        StringBuffer b = new StringBuffer(text.length());
        Matcher monthMatchB = monthB.matcher(text);
        while (monthMatchB.find()) {
            text = monthMatchB.group(1) + " " + EnglishNumberToWords.convert(Integer.valueOf(monthMatchB.group(3).substring(0, 2))) + " " + EnglishNumberToWords.convert(Integer.valueOf(monthMatchB.group(3).substring(2, 4)));      //replace 3 with year parse
            monthMatchB.appendReplacement(b, monthMatchB.quoteReplacement(text));
        }
        monthMatchB.appendTail(b);
        text = (b.toString());
        StringBuffer c = new StringBuffer(text.length());
        Matcher monthMatchC = monthC.matcher(text);
        while (monthMatchC.find()) {
            text = monthMatchC.group(1) + " " + (EnglishNumberToWords.convertToOrdinal(monthMatchC.group(3)));      //replace 3 with day parse
            monthMatchC.appendReplacement(c, monthMatchC.quoteReplacement(text));
        }
        monthMatchC.appendTail(c);
        text = c.toString();
        StringBuffer d = new StringBuffer(text.length());
        Matcher monthMatchD = inYear.matcher(text);
        while (monthMatchD.find()) {
            text = monthMatchD.group(1) + EnglishNumberToWords.convert(Integer.valueOf(monthMatchD.group(2).substring(0, 2))) + " " + EnglishNumberToWords.convert(Integer.valueOf(monthMatchD.group(2).substring(2, 4)));
            monthMatchD.appendReplacement(d, monthMatchD.quoteReplacement(text));
        }
        monthMatchD.appendTail(d);
        text = d.toString();
        return text;
    }

    public static String checkCurrency(String input) {
        Pattern cash = Pattern.compile("(\\$\\s)([1-9][0-9\\,]*)\\.?(\\d*)(\\s)");
        Pattern cashNamed = Pattern.compile("(\\$\\s)([1-9][0-9\\,]*\\.?\\d*)(\\s)" + (NAMES));
        String text;
        StringBuffer a = new StringBuffer(input.length());
        Matcher namedCashMatch = cashNamed.matcher(input);
        while (namedCashMatch.find()) {
            text = (checkBasics(namedCashMatch.group(2)) + " " + namedCashMatch.group(4) + " dollars");     //replace 2 with parse number value
            namedCashMatch.appendReplacement(a, namedCashMatch.quoteReplacement(text));
        }
        namedCashMatch.appendTail(a);
        text = a.toString();
        Matcher cashMatch = cash.matcher(text);
        StringBuffer b = new StringBuffer(text.length());
        while (cashMatch.find()) {
            text = stripCommas(cashMatch.group(2));
            text = (EnglishNumberToWords.convert(Integer.valueOf(text)) + " dollars");
            if (!(cashMatch.group(4).equals(" "))) {
                String cents = cashMatch.group(3);
                if (cents.length() < 2 && cents.length() > 0) {
                    //    System.out.println(cents);
                    text = text + " and " + EnglishNumberToWords.convert(Integer.valueOf(cents)) + " cents";

                } else {
                    text = text + " and " + EnglishNumberToWords.convert(Integer.valueOf(cents.substring(0, 2))) + " point " + (EnglishNumberToWords.convert(Integer.valueOf(cents.substring(1)))) + " cents";

                }
            }
            cashMatch.appendReplacement(b, cashMatch.quoteReplacement(text));
        }
        cashMatch.appendTail(b);
        text = b.toString();

        return text;
    }

    public static String checkFractions(String input) {
        Pattern fraction = Pattern.compile(FRACTIONS);
        Matcher fractionMatch = fraction.matcher(input);
        String text;
        String pre = "";
        StringBuffer a = new StringBuffer(input.length());
        while (fractionMatch.find()) {
            if (fractionMatch.group(2) != null) {
                int get = Integer.parseInt(fractionMatch.group(2).trim());
                pre = (EnglishNumberToWords.convert(get) + " and ");
                text = pre + (EnglishNumberToWords.convert(Integer.valueOf(fractionMatch.group(3))) + " " + EnglishNumberToWords.convertToOrdinal(fractionMatch.group(5)) + "s");
            } else {
                text = (EnglishNumberToWords.convert(Integer.valueOf(fractionMatch.group(3))) + " " + EnglishNumberToWords.convertToOrdinal(fractionMatch.group(5)) + "s");
            }
            fractionMatch.appendReplacement(a, fractionMatch.quoteReplacement(text));
            //  System.out.println(fractionMatch.group());                      //replace numerator with parsed number
        }
        fractionMatch.appendTail(a);//replace denominator with ordinal denominator
        text = a.toString();
        return text;
    }

    public static String checkOrdinal(String input) {
        Pattern ordinal = Pattern.compile(ORDINALS);
        Matcher ordinalMatch = ordinal.matcher(input);
        String text;
        StringBuffer a = new StringBuffer(input.length());
        while (ordinalMatch.find()) {
            text = EnglishNumberToWords.convertToOrdinal(ordinalMatch.group(1));
            ordinalMatch.appendReplacement(a, ordinalMatch.quoteReplacement(text));
        }
        ordinalMatch.appendTail(a);
        text = a.toString();
        return text;
    }

    public static String checkTemps(String input) {
        //check for degree symbol
        //parse as normal and add appropriate
        //degree after
        return input;
    }

    public static String checkBasics(String input) {
        Pattern basic = Pattern.compile(NUMBERS);

        Pattern decimal = Pattern.compile(DECIMALS);
        Matcher decimalMatch = decimal.matcher(input);
        StringBuffer a = new StringBuffer(input.length());
        String text;
        while (decimalMatch.find()) {
            text = (EnglishNumberToWords.convert(Integer.valueOf(stripCommas(decimalMatch.group(1)))) + " point");
            for (int i = 0; i < decimalMatch.group(2).length(); i++) {
                int get = Character.getNumericValue(decimalMatch.group(2).charAt(i));
                text = text + " " + EnglishNumberToWords.convert(get);
            }
            decimalMatch.appendReplacement(a, decimalMatch.quoteReplacement(text));
            // System.out.println(text);
        }
        decimalMatch.appendTail(a);
        text = a.toString();
        StringBuffer b = new StringBuffer(text.length());
        Matcher basicMatch = basic.matcher(text);
        while (basicMatch.find()) {
            text = (EnglishNumberToWords.convert(Integer.valueOf(stripCommas(basicMatch.group(1)))));
            basicMatch.appendReplacement(b, decimalMatch.quoteReplacement(text));
        }
        basicMatch.appendTail(b);
        text = b.toString();
        return text;
    }

    public static String checkLengths(String input) {
        //look for ' and ''
        //treat as normal # with feet/inches

        return input;
    }

    public static String stripCommas(String input) {
        return input.replaceAll(("\\,"), "");
    }

    public static void main(String[] args) {
        printLines("moreexamples.txt");
    }

}
