package com.hiscene.camera;

import android.os.Build;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

 /**
   * @author Minamo
   * @e-mail kleinminamo@gmail.com
   * @time   2019/12/10
   * @des    Constant
   */
public class Constant {

    public static List<String> G200_MODELS;
    public static boolean IS_G200;
    public static boolean IS_G100;
    static {
        G200_MODELS = new ArrayList<>();
        G200_MODELS.add("MSM8996 for arm64");
        G200_MODELS.add("G200");

        IS_G200 = G200_MODELS.contains(Build.MODEL);
        IS_G100 = Build.BRAND.equals("HiAR");
    }

     public static String getProperty(String key, String defaultValue) {
         String value = defaultValue;
         try {
             Class<?> c = Class.forName("android.os.SystemProperties");
             Method get = c.getMethod("get", String.class, String.class);
             value = (String) (get.invoke(c, key, defaultValue));
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             return value;
         }
     }

     public static boolean beyondFirmwareVersion1_0_0() {
         //T288-V0.0.0.30-T-userdebug-kernelperf-20190430
         String version = getProperty("ro.build.display.id", "V1.0.0");
         if (version.equals("V1.0.0")) {
             return true;
         }
         try {
             int start = version.indexOf('V');
             int end = version.indexOf('.');
             if (start + 1 <= end) {
                 int firstVersion = Integer.parseInt(version.substring(start + 1, end));
                 return firstVersion >= 1;
             } else {
                 return true;
             }
         } catch (Exception e) {
             return true;
         }
     }
}
