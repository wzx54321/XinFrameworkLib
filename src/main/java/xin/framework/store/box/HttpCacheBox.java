package xin.framework.store.box;

import xin.framework.store.box.base.BaseBoxManager;
import xin.framework.store.entity.EntityHttpCache;
import xin.framework.store.entity.EntityHttpCache_;

/**
 * 作者：xin on 2018/6/7 0007 20:35
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class HttpCacheBox extends BaseBoxManager<EntityHttpCache> {
    public HttpCacheBox() {
        super(EntityHttpCache.class);
    }

    @Override
    public String getTableName() {
        return EntityHttpCache_.__DB_NAME;
    }

}
