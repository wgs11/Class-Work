

package reverseargs;
import java.util.*;

public class ReverseArgs {

    public static void main(String[] args) {
        for (int i = args.length-1; i > -1; i--){
            System.out.println(args[i]);
        }
    }

}
