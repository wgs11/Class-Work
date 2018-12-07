package LowestGrade;

import java.lang.StringBuilder;
import java.util.*;

public class LowestGrade {

    public static int[] a = removeLowest(23, 90, 47, 55, 88);
    public static int[] b = removeLowest(85);
    public static int[] c = removeLowest();
    public static int[] d = removeLowest(59, 92, 93, 47, 88, 47);
    public static int [] shorter;

    public static String arrayPrint(int[] a) {

        StringBuilder in = new StringBuilder("[");
        if (a.length == 0) {
            return "[]";
        } else if (a.length == 1) {
            in.append(a[0] + "]");
            return in.toString();
        } else {
            for (int i = 0; i < a.length; i++) {
                in.append(a[i] + ", ");
            }
        }
        in.delete((in.length() - 2), in.length());
        in.append("]");
        return in.toString();
    }

    public static int[] removeLowest(int... a) {
        int lowest = 101;
        int [] temp = new int [a.length];
        for (int i = 0; i < a.length; i++){
            temp [i] = a[i];
            if (temp [i] < lowest){
                lowest = temp [i];
            }
        }
        if (temp.length <= 1){
            shorter = temp;
        }
        else {
            shorter = new int [temp.length-1];
            int where = 0;
            boolean trip = false;
            for (int i = 0; i < temp.length; i++){
                if ((temp[i] == lowest)&& !trip){
                    trip = true;
                }
                else if ((temp[i] == lowest) && trip){
                    shorter[where] = temp[i];
                    where++;
                }
                else{
                shorter[where] = temp[i];
                where++;
                }
            }
        }
        return shorter;
    }

        
    

    public static void main(String[] args) {
        System.out.println("a = "+arrayPrint(a));
        System.out.println("b = "+arrayPrint(b));
        System.out.println("c = "+arrayPrint(c));
        System.out.println("d = "+arrayPrint(d));
    }
}
