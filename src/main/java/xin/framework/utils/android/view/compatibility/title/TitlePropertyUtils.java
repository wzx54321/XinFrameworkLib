package xin.framework.utils.android.view.compatibility.title;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * Utils to access android system properties.
 * <p/>
 */
@SuppressWarnings({"unchecked", "WeakerAccess"})
public class TitlePropertyUtils {

    public final static String PROPERTY_DNS_PRIMARY = "net.dns1";
    public final static String PROPERTY_DNS_SECONDARY = "net.dns2";

    private final static String CMD_GET_PROP = "getprop";

    private static Class sClassSystemProperties;
    private static Method sMethodGetString;

    static {
        try {
            sClassSystemProperties = Class.forName("android.os.SystemProperties");
            sMethodGetString = sClassSystemProperties.getDeclaredMethod("get", String.class, String.class);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Get property of corresponding key.
     *
     * @param key      property key.
     * @param defValue default value.
     * @return String
     */
    public static String get(String key, String defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        String value = getWithReflect(key, null);
        if (TextUtils.isEmpty(value)) {
            value = getWithCmd(key, null);
        }
        if (TextUtils.isEmpty(value)) {
            value = defValue;
        }
        return value;
    }

    /**
     * Get property of corresponding key quickly (but provide less validity than {@link #get(String, String)}).
     *
     * @param key      property key.
     * @param defValue default value.
     * @return String
     */
    @SuppressWarnings("SameParameterValue")
    public static String getQuickly(String key, String defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        return getWithReflect(key, defValue);
    }

    private static String getWithReflect(String key, String defValue) {
        if (sClassSystemProperties == null || sMethodGetString == null) {
            return defValue;
        }
        String value = defValue;
        try {
            value = (String) sMethodGetString.invoke(sClassSystemProperties, key, defValue);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }

    @SuppressWarnings("SameParameterValue")
    private static String getWithCmd(String key, String defValue) {
        String value = defValue;
        try {
            Process process = Runtime.getRuntime().exec(CMD_GET_PROP + " " + key);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String readValue = builder.toString();
                if (!TextUtils.isEmpty(readValue)) {
                    // if read value is valid, use it.
                    value = readValue;
                }

            } catch (IOException e) {
                //
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            // clean job.
            process.destroy();

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
}
