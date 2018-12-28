package xin.framework.imageloader.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.Call;
import xin.framework.imageloader.progress.ProgressManager;
import xin.framework.utils.android.Loger.Log;


/**
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <!--
 * Allows Glide to monitor connectivity status and restart failed requests if users go from a
 * a disconnected to a connected network state.
 * -->
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <p>
 * 备注：<p>
 * 1.ConnectivityMonitor:网络监听<p>
 * 2.在后台线程加载图片也是直接使用 submit(int, int)：
 * <p>
 * FutureTarget<Bitmap> futureTarget =
 * Glide.with(context)
 * .asBitmap()
 * .load(url)
 * .submit(width, height);
 * Bitmap bitmap = futureTarget.get();
 * <p>
 * // Do something with the Bitmap and then when you're done with it:
 * Glide.with(context).clear(futureTarget);
 * <p>
 * 3.placeholder是在主线程从Android Resources加载的。我们通常希望占位符比较小且容易被系统资源缓存机制缓存起来。
 * Transformation仅被应用于被请求的资源，而不会对任何占位符使用。例如你正在加载圆形图片，你可能希望在你的应用中包含圆形的占位符。但是你也可以考虑自定义一个View来剪裁(clip)你的占位符，达到你的transformation的效果。
 * <p>
 * 4.RequestOption 可以被组合使用。如果 RequestOptions 对象之间存在相互冲突的设置，那么只有最后一个被应用的 RequestOptions 会生效。
 * <p>
 * 5.TransitionOptions 用于显示淡入的效果。不会默认应用交叉淡入或任何其他的过渡效果。每个请求必须手动应用过渡。
 * <p>
 * 6.RequestBuilder 可以指定：
 * <p>
 * 你想加载的资源类型(Bitmap, Drawable, 或其他)<p>
 * 你要加载的资源地址(url/model)<p>
 * 你想最终加载到的View<p>
 * 任何你想应用的（一个或多个）RequestOption 对象<p>
 * 任何你想应用的（一个或多个）TransitionOption 对象<p>
 * 任何你想加载的缩略图 thumbnail()<p>
 * <p>
 * <p>
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * 邮箱：ittfxin@126.com
 */
// @Excludes({com.example.unwanted.GlideModule, com.example.conflicing.GlideModule}) 排除其他依赖库中不需要的AppGlideModule
@SuppressWarnings("WeakerAccess")
@GlideModule
public class GlideConfig extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull final Context context, @NonNull final GlideBuilder builder) {


        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * 500));
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        // builder.setDiskCacheExecutor(newDiskCacheExecutor(myUncaughtThrowableStrategy));
        //  builder.setResizeExecutor(newSourceExecutor(myUncaughtThrowableStrategy));
        builder.setLogLevel(Log.DEBUG);

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        Call.Factory clint = ProgressManager.getOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(clint));


    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
