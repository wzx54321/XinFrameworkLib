

package xin.framework.utils.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.FileDescriptor;
import java.io.IOException;


/**
 * 描述：用于对图片压缩处理，实现了
 *
 * @author Wangzhaoxin
 * @since JDK1.8
 */
public class ImageResizer {
    private static final String TAG = "ImageResizer";
    protected int mImageWidth;
    protected int mImageHeight;
    private Context mContext;

    /**
     * 通过图片宽高初始化
     *
     * @param context
     * @param imageWidth
     * @param imageHeight
     */
    public ImageResizer(Context context, int imageWidth, int imageHeight) {
        mContext = context;
        setImageSize(imageWidth, imageHeight);
    }

    /**
     * 通过一个尺寸初始化
     *
     * @param context
     * @param imageSize
     */
    public ImageResizer(Context context, int imageSize) {
        mContext = context;
        setImageSize(imageSize);
    }

    /**
     * 设置图片的宽高
     *
     * @param width
     * @param height
     */
    public void setImageSize(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
    }

    /**
     * 设置图片宽高可能会相等
     *
     * @param size
     */
    public void setImageSize(int size) {
        setImageSize(size, size);
    }

    /**
     * 父类子线程调用 从资源文件中获取bitmap
     *
     * @param resId
     * @return
     */
    public Bitmap processBitmap(int resId) {

        return decodeSampledBitmapFromResource(mContext.getResources(), resId, mImageWidth, mImageHeight);
    }

    protected Bitmap processBitmap(Object data) {
        return processBitmap(Integer.parseInt(String.valueOf(data)));
    }

    /**
     * 通过资源id和宽高处理bitmap
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    protected Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);

    }

    /**
     * 通过文件来处理图片压缩
     *
     * @param filename  图片路径
     * @param reqWidth
     * @param reqHeight
     */
    public Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filename, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);

    }

    /**
     * 通过流读取想要宽高的bitmap
     */
    protected Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

    }

    public static Bitmap scaledBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {

        return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);


    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional LogMaic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * 用于压缩时旋转图片
     *
     * @param srcFilePath
     * @param bitmap
     * @return
     * @throws IOException
     * @throws OutOfMemoryError
     */
    public static Bitmap rotateBitMap(String srcFilePath, Bitmap bitmap) throws IOException, OutOfMemoryError {
        ExifInterface exif;
        exif = new ExifInterface(srcFilePath);
        float degree = 0F;
        switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90F;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180F;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270F;
                break;
            default:
                break;
        }
        Matrix matrix = new Matrix();
        if (bitmap == null)
            return null;
        matrix.setRotate(degree, bitmap.getWidth(), bitmap.getHeight());
        Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != b2) {
            bitmap.recycle();
            bitmap = b2;
        }
        return bitmap;
    }
}
