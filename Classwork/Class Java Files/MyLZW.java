/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int codewords;// = 4096;       // number of codewords = 2^W
    private static int width;// = 12;         // codeword width

    public static void compress(String mode) {			
	System.err.println(mode);
	width = 9;
		codewords = 2 << (width - 1);
		int bytes = 0;
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), width);      // Print s's encoding.
            int t = s.length();
			bytes += t;
            if (t < input.length() && code < codewords){    // Add s to symbol table.
				System.err.println(input.substring(0, t+1));
                st.put(input.substring(0, t + 1), code++);
				if ((code == codewords - 1) && width < 16){
				width ++;
				codewords = 2 << width - 1;
				System.err.println(width);
				}
				}
            input = input.substring(t);            // Scan past s in input.
        }
		BinaryStdOut.write(R, width);
		System.err.println(bytes);
		BinaryStdOut.close();
		
		
    
}	


    public static void expand() {
		width = 9;						//sets width to starting value of 9
		codewords = 2 << width - 1;		//
        String[] st = new String[2 << 15];
        int i; // next available codeword value
		
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(width);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];
        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(width);
			System.err.println(codeword);
            if (codeword == R) break;
            String s = st[codeword];
			System.err.println(s);
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < codewords){
			st[i++] = val + s.charAt(0);
			if (i == codewords){
				width++;
				codewords = 2 << width - 1;
			 }
			}
            val = s;
			System.err.println(val);
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
		if (args[0].equals("-")){
			if (args[1].equals("n") || args[1].equals("r") || args[1].equals("m")){
			compress(args[1]);
			}
			else throw new IllegalArgumentException("Invalid compression mode");
			}
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
	
    }

}
