package xin.framework.utils.android;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB_MR1;

/**
 * Created by xin on 2016/4/22 0022.
 * <p>
 * bitmap 缓存
 */

public class BitmapLruCacheUtil {

    private static LruCache<String, Bitmap> sBitmapLruCache;
    private static final int CAPACITY = 10 * 1024 * 1024;

    static {
        if (sBitmapLruCache == null)
            sBitmapLruCache = new LruCache<String, Bitmap>(CAPACITY) {

                @Override
                protected void entryRemoved(boolean evicted,
                                            String key,
                                            Bitmap oldValue,
                                            Bitmap newValue) {
                    super.entryRemoved(evicted,
                            key,
                            oldValue,
                            newValue);
                }

                @Override
                protected int sizeOf(String key,
                                     Bitmap bitmap) {
                    return SDK_INT >= HONEYCOMB_MR1 ? bitmap.getByteCount() : getByteCount(bitmap);
                }

            };
    }

    private static int getByteCount(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 将一张图片存储到缓存中。
     *
     * @param key    缓存的键
     * @param bitmap 缓存的键，这里传入Bitmap对象。
     */
    public static void addBitmapToMemoryCache(String key,
                                              Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null && key != null && bitmap != null) {
            sBitmapLruCache.put(key,
                    bitmap);
        }
    }

    /**
     * 从缓存中获取一张图片，如果不存在就返回null。
     */
    public static Bitmap getBitmapFromMemoryCache(String key) {
        if (TextUtils.isEmpty(key))
            return null;
        return sBitmapLruCache.get(key);
    }

    /**
     * 清缓存
     */
    public static void clearCache() {
        if (sBitmapLruCache != null) {
            if (sBitmapLruCache.size() > 0) {

                sBitmapLruCache.evictAll();

            }

        }
    }

    public static void clearCacheByKey(String Key) {
        if (sBitmapLruCache != null && sBitmapLruCache.get(Key) != null)
            sBitmapLruCache.get(Key).recycle();
        if (sBitmapLruCache != null) {
            sBitmapLruCache.remove(Key);
        }
    }

}
