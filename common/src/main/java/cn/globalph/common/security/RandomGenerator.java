package cn.globalph.common.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class RandomGenerator {

    private final static char[] CHARSET = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
    	                                                 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 
                                                                          '0', '1', '2', '3', '4','5', '6', '7', '8', '9' };

    private RandomGenerator() {
        /**
         * Intentionally blank to force static usage
         */
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static String generateRandomId(String prng, int len) throws NoSuchAlgorithmException {
        return generateRandomId(SecureRandom.getInstance(prng), len);
    }

    public static String generateRandomId(SecureRandom sr, int len) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < len + 1; i++) {
            int index = sr.nextInt(CHARSET.length);
            char c = CHARSET[index];
            sb.append(c);

            if ((i % 4) == 0 && i != 0 && i < len) {
                sb.append('-');
                }
           }

        return sb.toString();
    }

}
