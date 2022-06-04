package myssm.util;

public class StringUtil {
    //判斷字串是否為null或""
    public static boolean isEmpty(String str){
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
