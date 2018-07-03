package xin.framework.store.entity;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Description :网络缓存实体
 * Created by xin on 2017/9/14 0014.
 */
@Entity
public class EntityHttpCache implements Serializable {
    private static final long serialVersionUID = 7145026421247335129L;

    @Id
    private long id;

    private String host;


    private String data;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
