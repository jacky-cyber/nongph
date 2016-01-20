package cn.globalph.common.security.util;


public class PasswordUtils {

    public static final Character[] characters = {
        'a','b','c','d','e','f','g','h','j','k','m','n','p','q','r','s','t','u','v','w','x','y',
        '2','3','4','6','7','8','9'
    };
    
    public static String generateTemporaryPassword(int requiredLength) {
        int length = characters.length;
        StringBuffer sb = new StringBuffer(requiredLength);
        for (int j=0;j<requiredLength;j++) {
            sb.append(characters[(int) Math.round(Math.floor(Math.random() * length))]);
        }
        
        return sb.toString();
    }
}
