package xin.framework.mvp;


public class PresentImpl<V extends IView> implements IPresent<V> {
    private V v;

    @Override
    public void attachV(V view) {
        v = view;
    }

    @Override
    public void detachV() {
        if (v != null) {
            v = null;
        }

    }

    public V getV() {
        if (v == null) {
            throw new IllegalStateException("v can not be null");
        }
        return v;
    }
}
