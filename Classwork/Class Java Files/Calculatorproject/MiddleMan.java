package calculatorproject;

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.text.*;

public class MiddleMan implements ActionListener {

    GUI gui;
    CalcLogic logic;
    private NumberFormat format = DecimalFormat.getInstance();

    MiddleMan(GUI gui, CalcLogic cl) {
        this.gui = gui;
        this.logic = cl;
        gui.buttonActionListeners(this);
        format.setMaximumFractionDigits(10);
        format.setRoundingMode(RoundingMode.UP);
        format.setGroupingUsed(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (gui.getNumber().equals("ERROR")){
            gui.setNumber("0.0");
        }
        double input = Double.valueOf(gui.getNumber());
        String command = e.getActionCommand();
        if (command.equals("C")) {
            logic.reset();
            input = 0.0;
            gui.setNumber("0.0");
            gui.setSeconNum(true);
            command = "";
        } else if (command.equals("=")) {
            if (!logic.hasOperator()) {
                logic.setAnswer(input);
            }
            else{
                command = logic.getOperator();
            }
        } else if (command.equals("\u221A")){
            if (input < 0){
                gui.setNumber("ERROR");
            }
            else {input = Math.sqrt(input);
            }
    } 
        if (!logic.hasOperator()|| gui.secondNum()) {
            logic.setAnswer(input);
            logic.setOperator(command);
        }
        else if (logic.hasOperator()) {
            command = logic.getOperator();
            if (command.equals("+")) {
                logic.addition(input);
            } else if (command.equals("-")) {
                logic.subtraction(input);
            } else if (command.equals("/")) {
                if (input == 0){
                gui.setNumber("ERROR");
                }
                else{
                logic.division(input);
                }
            } else if (command.equals("*")) {
                logic.multiplication(input);
            }
            logic.setOperator(e.getActionCommand());
        }
        if (gui.getNumber().equals("ERROR")){
            logic.reset();
            input = 0.0;
            
        }
        else{
        gui.setNumber("" + format.format(logic.getAnswer()));
        }
        gui.setSeconNum(true);
    }

}
