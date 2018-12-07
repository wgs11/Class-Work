/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GUI implements ActionListener{
    private Font use = new Font("Helvetica", Font.BOLD, 30);
    private JTextArea display;
    private String[] numberLabels = 
               {"7", "8", "9",
                "4", "5", "6",
                "1", "2", "3",
                "0", "."};
    private String[] actionLabels = {"C","\u221A","/","*","-","+","="};
    private MyButton[] numbers = new MyButton[numberLabels.length];
    private MyButton[] actions = new MyButton[actionLabels.length];
    private boolean secondNum = true, decimalPresent = true;
    public GUI() {
        makeGUI();
        
    }
    public void makeGUI(){
        JFrame calculator = new JFrame("Calculator");
        calculator.setLayout(new BorderLayout());
        calculator.add(createPanel());
        calculator.pack();
        calculator.setLocationRelativeTo(null);
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    private JPanel createPanel(){
        display = new JTextArea("0.0");
        display.setEditable(false);
        display.setFont(use);
        display.setBackground(Color.YELLOW);
        JPanel hold = new JPanel(new BorderLayout());
        hold.setBackground(Color.YELLOW);
        hold.add(display,BorderLayout.EAST);
        JPanel gui = new JPanel(new BorderLayout());
        JPanel middle = new JPanel(new BorderLayout());
        JPanel one = new JPanel(new GridLayout(1,4));
        for (int i = 0; i < actionLabels.length;i++){
            actions[i] = new MyButton(actionLabels[i]);
        }
        for (int i = 0; i < 4; i++){
            one.add(actions[i]);
        }
        middle.add(one,BorderLayout.NORTH);
        JPanel numberPanel = new JPanel(new GridLayout(4,3));
        for (int i = 0; i < numberLabels.length;i++){
            numbers[i] = new MyButton(numberLabels[i]);
            numbers[i].addActionListener(this);
            numbers[i].setActionCommand(numberLabels[i]);
            numberPanel.add(numbers[i]);
        }
        JPanel right = new JPanel(new GridLayout(4,1));
        for (int i = 4; i < 7; i++){
            right.add(actions[i]);
        }
        middle.add(numberPanel);
        middle.add(right,BorderLayout.EAST);
        gui.add(middle);
        gui.add(hold,BorderLayout.NORTH);


        return gui;
    }
    public String getNumber(){
            return display.getText();
        }
    public void setNumber(String input){
        display.setText(input);
    }
    public boolean secondNum(){
        return secondNum;
    }
    public void setSeconNum(boolean b){
        secondNum = b;
    }
    class MyButton extends JButton {
       public MyButton(String s) {
            this.setPreferredSize(new Dimension(80, 50));
            this.setText(s);
        }
    }
    public void actionPerformed(ActionEvent a){
        String command = a.getActionCommand();
        if(secondNum){
            setNumber("");
            secondNum = false;
        }
        if (command.equals(".")){
            if(decimalPresent){
                decimalPresent = false;
                setNumber(getNumber()+command);
        }
        }
        else{
            setNumber(getNumber()+command);
        }
    }
        public void buttonActionListeners(ActionListener a) {
            for (int i = 0; i < actionLabels.length; i++){
                actions[i].setActionCommand(actionLabels[i]);
                actions[i].addActionListener(a);
            }
        }

    
}
