package barcodetest;

public class BarCodeTest {

    public static void main(String[] args) {
        BarCode[] Bar = new BarCode[10];

        Bar[0] = new BarCode("02138");
        System.out.println(Bar[0].getBarCode());
        Bar[1] = new BarCode("||:|:::|:|:||::::::||:|::|:::|||");
        if (Bar[1].isValidBarCode()) {
            System.out.println(Bar[1].getZipCode());
        } else {
            System.out.println("Sorry, that isn't a valid barcode.");
        }
        Bar[2] = new BarCode("44236");
        System.out.println(Bar[2].getBarCode());
        Bar[3] = new BarCode(Bar[2].getBarCode());
        System.out.println(Bar[3].getZipCode());
        Bar[4] = new BarCode("||:::||::|:::|:|:|::|||:::||:::|");
        if (Bar[4].isValidBarCode()) {
            System.out.println(Bar[4].getZipCode());
        } else {
            System.out.println("Sorry, that isn't a valid barcode.");
        }
        Bar[5] = new BarCode("||:|:::|:|:||::::::||:|::|:::||");
        if (Bar[5].isValidBarCode()) {
            System.out.println(Bar[5].getZipCode());
        } else {
            System.out.println("Sorry, that isn't a valid barcode.");
        }
        Bar[6] = new BarCode("||:|:::|:|:||::::::||:|::|:::||:");
        if (Bar[6].isValidBarCode()) {
            System.out.println(Bar[6].getZipCode());
        } else {
            System.out.println("Sorry, that isn't a valid barcode.");
        }
        Bar[7] = new BarCode("abc12");
        if (Bar[7].isValidZipCode()) {
            System.out.println(Bar[7].getBarCode());
        } else {
            System.out.println("Sorry, that isn't a valid zipcode.");
        }
        Bar[8] = new BarCode("12");
        if (Bar[8].isValidZipCode()) {
            System.out.println(Bar[8].getBarCode());
        } else {
            System.out.println("Sorry, that isn't a valid zipcode.");
        }
        Bar[9] = new BarCode("123456");
        if (Bar[9].isValidZipCode()) {
            System.out.println(Bar[9].getBarCode());
        } else {
            System.out.println("Sorry, that isn't a valid zipcode.");
        }
    }
}
