package barcodetest;

public class BarCode extends BarCodeTest {

    String myBarCode, myZipCode = "";
    public int[] weights = {7, 4, 2, 1, 0};
    public String[] codes = {"||:::", ":::||", "::|:|", "::||:", ":|::|", ":|:|:",
        ":||::", "|:::|", "|::|:", "|:|::"};

    public BarCode(String input) {
//32 & 5 Need to be static final variables of known origin
//Needs to return proper values for both fields
        if (input.length() == 32) {
            this.myBarCode = input;
            if (isValidBarCode()) {
                decode();
            }
        } else if (input.length() == 5) {
            this.myZipCode = input;
            if (isValidZipCode()) {
                encode();
            }
        } else {
            this.myBarCode = this.myZipCode = "";
        }
    }
//getters need to only return values, conversion happens elsewhere

    public String getZipCode() {
        StringBuilder zip = new StringBuilder("");
        for (int i = 1; i < (this.myBarCode.length() - 6); i += 5) {
            zip.append(codeToDigit(this.myBarCode.substring(i, i + 5)));
        }
        return zip.toString();
    }

    private String codeToDigit(String code) {
        int weight;
        int sum = 0;
        for (int i = 0; i < code.length(); i++) {
            weight = weights[i];
            if (code.charAt(i) == ':') {
                sum += (weight * 0);
            } else {
                sum += (weight * 1);
            }
        }
        if (sum == 11) {
            sum = 0;
        }
        return Integer.toString(sum);
    }

    public String getBarCode() {
        StringBuilder bar = new StringBuilder("|");
        for (int i = 0; i < this.myZipCode.length(); i++) {
            bar.append(digitToCode(this.myZipCode.charAt(i)));
        }
        bar.append(codes[getCheckDigit(this.myZipCode)]);
        bar.append("|");
        return bar.toString();
    }

    private String digitToCode(char digit) {
        int index = Integer.parseInt(Character.toString(digit));
        return codes[index];
    }

    private Integer getCheckDigit(String zip) {
        int sum = 0;
        for (int i = 0; i < zip.length(); i++) {
            sum += Integer.parseInt(Character.toString(zip.charAt(i)));
        }
        return (10 - sum % 10);
    }

    private Integer getCheckDigitRubric(String zip) {
        int sum = 0;                //will be sum of digits
        for (int i = 0; i < zip.length(); i++) {
//@@ -1: overkil. zip.charAt(i) - '0'
            sum += Integer.parseInt(Character.toString(zip.charAt(i)));
        }

//@@ -1:  (10 - (sum % 10)) %10;
        return (10 - sum % 10); //digit is # needed to reach next multiple of 10
    }

    public boolean isValidBarCode() {
        //Needs to check for invalid | : patterns as well as number for each
        int barcount = 0;
        int dashcount = 0;
        for (int i = 0; i < this.myBarCode.length(); i++) {
            if (myBarCode.charAt(i) == '|') {
                barcount++;
            } else if (myBarCode.charAt(i) == ':') {
                dashcount++;
            } else {
            }
        }
        if (this.myBarCode.startsWith("|") && this.myBarCode.endsWith("|")
                && barcount == 14 && dashcount == 18
                && ((codeToDigit(this.myBarCode.substring(26, 31)).equals(
                        (Integer.toString(getCheckDigit(getZipCode()))))))) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isValidZipCode() {
        //don't need count
        //don't need special case
        int count = 0;
        for (int i = 0; i < this.myZipCode.length(); i++) {
            if (Character.isDigit(this.myZipCode.charAt(i))
                    || this.myZipCode.charAt(i) == '0') {
                count++;
            } else {
            }
        }
        if (count == 5) {
            return true;
        } else {
            return false;
        }
    }
//should be private static and implementation, not getters
    public String encode() {
        return getBarCode();
    }

    public String decode() {
        return getZipCode();
    }
}
