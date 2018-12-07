/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorproject;

public class CalcLogic {

    private double result;
    private String op;

    CalcLogic() {
        result = 0.0;
        op = "";
    }

    public void setOperator(String o) {
        op = o;
    }

    public String getOperator() {
        return op;
    }

    public boolean hasOperator() {
        return !op.equals("");
    }

    public void setAnswer(double value) {
        result = value;
    }

    public double getAnswer() {
        return result;
    }

    public void addition(double input) {

        result += input;
    }

    public void subtraction(double input) {

        result -= input;
    }

    public void multiplication(double input) {

        result *= input;
    }

    public void division(double input) {

        result /= input;
    }

    public void reset() {
        result = 0.0;
        op = "";
    }
}
