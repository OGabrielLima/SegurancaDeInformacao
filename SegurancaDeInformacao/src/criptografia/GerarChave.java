package criptografia;

public class GerarChave {
	static String getRandomString(int i) { 
        String theAlphaNumericS;
        StringBuilder builder;
        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                         + "0123456789"; 
        builder = new StringBuilder(i); 
        for (int m = 0; m < i; m++) { 
            int myindex = (int)(theAlphaNumericS.length() * Math.random()); 
            builder.append(theAlphaNumericS.charAt(myindex)); 
        }
        return builder.toString(); 
    } 
}
