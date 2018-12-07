/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestSets.java;

/**
 * TestSets.java
 * 
* @version: Last Modified April 4, 2012
 * @author: Henry Leitner
 */
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GUI implements ActionListener{
JFrame project = new JFrame("Set Demo");
String[] commandLabels = {"Intersection", "UNION", "DIFFERENCE","CARDINALITY A",
                          "CARDINALITY B", "SUBSET (A OF B)", "SUBSET (B OF A)"};
JButton[] commands = new JButton[commandLabels.length];

    public GUI() {
        makeGUI();
    }

    private void makeGUI() {
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(600,600));
        content.setLayout(new GridLayout(commandLabels.length+2,1));
        content.add(new myPanel("Set A"));
        content.add(new myPanel("Set B"));
        for (int i = 0; i < commandLabels.length; i++){
            commands[i] = new myButton(commandLabels[i]);
            content.add(commands[i]);
            commands[i].addActionListener(this);
        }

        project.add(content);
        project.pack();
        project.setVisible(true);
        project.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        System.out.println(s);
    }
    private class myPanel extends JPanel{
        private myPanel(String s){
            this.setLayout(new BorderLayout());
            this.add(new JLabel(s),BorderLayout.WEST);
            this.add(new JTextField(),BorderLayout.CENTER);
        }
    }
    private class myButton extends JButton{
        private myButton(String s){
            this.setText(s);
            this.setActionCommand(s);
            
        }
    }
    
}

public class TestSets {

    static void menu() {

        System.out.println();
        System.out.print("Type 1 to CREATE SET A\n");
        System.out.print("Type 2 to CREATE SET B\n");
        System.out.print("Type 3 to CREATE INTERSECTION (A * B)\n");
        System.out.print("Type 4 to CREATE UNION (A + B)\n");
        System.out.print("Type 5 to CREATE DIFFERENCE (A - B)\n");
        System.out.print("Type 6 to PRINT CARDINALITY (A)\n");
        System.out.print("Type 7 to PRINT CARDINALITY (B)\n");
        System.out.print("Type 8 to PRINT SUBSETS\n");
        System.out.print("Type any OTHER # to EXIT PROGRAM \n\n");
        System.out.print("Command: ");
    }

    public static void main(String[] args) {
        Bitset setA = new Bitset(16);
        Bitset setB = new Bitset(8);
        int command;
        GUI gui = new GUI();

//        Scanner keyboard = new Scanner(System.in);
//        do {
//            menu();
//
//            switch (command = keyboard.nextInt()) {
//                case 1:
//                    System.out.println("Type some small integers, each < 16"
//                            + ", and type DONE when all done!");
//                    setA.readSet(keyboard);
//                    System.out.print("     SET A = " + setA);
//                    break;
//
//                case 2:
//                    System.out.println("Type some small integers, each < 8"
//                            + ", and type DONE when all done!");
//                    setB.readSet(keyboard);
//                    System.out.print("     SET B = " + setB);
//                    break;
//
//                case 3:
//                    System.out.print("     Intersection (A * B) = ");
//                    System.out.print(setA.intersect(setB));
//                    break;
//
//                case 4:
//                    System.out.print("     Union (A + B) = ");
//                    System.out.print(setA.union(setB));
//                    break;
//
//                case 5:
//                    System.out.print("     Difference (A - B) = ");
//                    System.out.print(setA.difference(setB));
//                    break;
//                case 6:
//                    System.out.print("     Cardinality (A) = ");
//                    System.out.println(setA.cardinality());
//                    break;
//
//                case 7:
//                    System.out.print("     Cardinality (B) = ");
//                    System.out.println(setB.cardinality());
//                    break;
//                case 8:
//                    System.out.print("     A Subset of B = ");
//                    System.out.println(setA.isSubset(setB));
//                    System.out.print("     B Subset of A = ");
//                    System.out.println(setB.isSubset(setA));
//                    break;
//                default:
//                    System.exit(0);
//
//            }
//        } while (command > 0 && command < 6);
    }

}
