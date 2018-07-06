package xin.framework.http.output;


import java.io.Serializable;

/**
 * 作者：xin on 2018/6/7 0007 19:46
 * <p>
 * <p>公共返回数据源
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 */

public class BaseOutPut<T> implements Serializable  {
    private static final long serialVersionUID = 2097291210268505721L;

    private T data;
    private MetaBean  meta;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseOutPut{" +
                "data=" + data +
                ", mMetaBean=" + meta +
                '}';
    }
}
