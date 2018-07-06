package xin.framework.utils.common;

import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import xin.framework.utils.android.Loger.Log;

/**
 * Description : （反射获取泛型参数）
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * 邮箱：ittfxin@126.com
 */


@SuppressWarnings("unchecked")
public class TypeUtil {

    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
            // Log.e(e,"TypeUtil 没有对应的presenter");
        }
        return null;
    }


    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    @Nullable
    public static <T> Class<T> getTypeClass(Object object) {
        Class<T> entityClass = null;
        try {
            entityClass = (Class<T>) ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception err) {
            Log.printStackTrace(err);
        }

        return entityClass;
    }
}
