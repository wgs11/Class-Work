
package age;
import java.awt.*;

import javax.swing.*;

public class Age {

    public static void main(String[] args){
       getPanel();
       
    }
    public static void getPanel(){
       JOptionPane query = new JOptionPane();
       String s = query.showInputDialog("What's your age, cowboy?");
       try{
           int a = Integer.parseInt(s);
           if (a < 40){
           JOptionPane.showMessageDialog(new JPanel(), "You're still young.");
           }
           else{
           JOptionPane.showMessageDialog(new JPanel(), "Wow, you're getting"
                   + " old enough to keel over.");    
           }
       }
       catch(NumberFormatException n){
           //use while loop to continue checking
       JOptionPane.showMessageDialog(new JPanel(),
                                    "That's not an age, try again.");
       getPanel();
       }
    }

}
