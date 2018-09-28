package xin.framework.utils.android.view;

import android.graphics.Color;

/**
 * Description :
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class ColorUtils {


    public static int interpolateColor(int colorFrom, int colorTo, int posFromAlpha, int posToAlpha) {
        float delta = posToAlpha - posFromAlpha;
        int red = (int) ((Color.red(colorFrom) - Color.red(colorTo)) * delta / posToAlpha + Color.red(colorTo));
        int green = (int) ((Color.green(colorFrom) - Color.green(colorTo)) * delta / posToAlpha + Color.green(colorTo));
        int blue = (int) ((Color.blue(colorFrom) - Color.blue(colorTo)) * delta / posToAlpha) + Color.blue(colorTo);
        return Color.argb(255, red, green, blue);
    }
}
