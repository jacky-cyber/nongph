package cn.globalph.common.util;

import org.codehaus.jettison.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class StringUtil {

    public static long getChecksum(String test) {
        try {
            byte buffer[] = test.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            CheckedInputStream cis = new CheckedInputStream(bais, new Adler32());
            byte readBuffer[] = new byte[buffer.length];
            cis.read(readBuffer);
            return cis.getChecksum().getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double determineSimilarity(String test1, String test2) {
        String first = new String(test1);
        first = first.replaceAll("[ \\t\\n\\r\\f\\v\\/'-]", "");
        Long originalChecksum = StringUtil.getChecksum(first);
        String second = new String(test2);
        second = second.replaceAll("[ \\t\\n\\r\\f\\v\\/'-]", "");
        Long myChecksum = StringUtil.getChecksum(second);
        StatCalc calc = new StatCalc();
        calc.enter(originalChecksum);
        calc.enter(myChecksum);
        return calc.getStandardDeviation();
    }
    
    /**
     * Protect against HTTP Response Splitting
     * @return
     */
    public static String cleanseUrlString(String input){
        return removeSpecialCharacters(decodeUrl(input));
    }

    public static String decodeUrl(String encodedUrl) {
        try {
            return encodedUrl == null ? null : URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // this should not happen
            e.printStackTrace();
            return encodedUrl;
        }
    }

    public static String removeSpecialCharacters(String input) {
        if (input != null) {
            input = input.replaceAll("[ \\r\\n]", "");
        }
        return input;
    }

    public static String getMapAsJson(Map<String, Object> objectMap) {
        String nullString = "null";
        StringBuffer sb = new StringBuffer("{");
        boolean firstIteration = true;

        for (Entry<String, Object> entry : objectMap.entrySet()) {
            if (!firstIteration) {
                sb.append(',');
            }
            sb.append(JSONObject.quote(entry.getKey()));
            sb.append(':');
            Object value = entry.getValue();
            if (value == null) {
                sb.append(nullString);
            } else if (value instanceof Boolean) {
                sb.append(((Boolean) value).booleanValue());
            } else if (value instanceof String) {
                sb.append(JSONObject.quote(value.toString()));
            } else {
                sb.append(value.toString());
            }
            firstIteration = false;
        }
        sb.append("}");

        return sb.toString();
    }
}
