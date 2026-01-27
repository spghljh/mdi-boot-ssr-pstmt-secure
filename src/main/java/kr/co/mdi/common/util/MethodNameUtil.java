package kr.co.mdi.common.util;

public class MethodNameUtil {

    private MethodNameUtil() {
        
    }

    public static String getCurrentMethodName() {
        return new Object(){}.getClass().getEnclosingMethod().getName();
    }
}

