package xin.framework.store.box;


import xin.framework.store.box.base.BaseBoxManager;
import xin.framework.store.entity.EntityCookie;
import xin.framework.store.entity.EntityCookie_;

/**
 * Description :Cookie存储的Box
 * Created by xin on 2017/9/15 0015.
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class CookieBox extends BaseBoxManager<EntityCookie> {


    public CookieBox() {
        super(EntityCookie.class);
    }

    @Override
    public String getTableName() {
        return EntityCookie_.__DB_NAME;
    }


}
