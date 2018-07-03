package xin.framework.mvp.mvp;

/**
 * P层
 * @param <V>
 */
public interface IPresent<V> {
    void attachV(V view);

    void detachV();
}
