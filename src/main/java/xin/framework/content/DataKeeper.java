
package xin.framework.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * 作者：xin on 2018/6/7 0007 17:40
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

@SuppressWarnings("WeakerAccess")
public class DataKeeper {
    private final SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private static final String TAG = DataKeeper.class.getSimpleName();

    @SuppressLint("CommitPrefEdits")
    DataKeeper(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * *************** get ******************
     */

    public String get(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public float get(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long get(String key, long defValue) {
        return sp.getLong(key, defValue);
    }


    public SharedPreferences.Editor put(String key, String value) {
       /* if (value == null) {
            editor.remove(key);
        } else {*/
        editor.putString(key, value);
       /* }*/
        return editor;
    }

    public SharedPreferences.Editor put(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor;
    }

    public SharedPreferences.Editor put(String key, float value) {
        editor.putFloat(key, value);
        return editor;
    }

    public SharedPreferences.Editor put(String key, long value) {
        editor.putLong(key, value);
        return editor;
    }

    public SharedPreferences.Editor putInt(String key, int value) {
        editor.putInt(key, value);
        return editor;
    }

}
