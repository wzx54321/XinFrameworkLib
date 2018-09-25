package xin.framework.store.box.utils;

import io.objectbox.BoxStore;

/**
 * 作者：xin on 2018/9/21 11:32
 * <p>
 * 邮箱：ittfxin@126.com
 * <P>
 * 外部调用用于创建BoxStore
 */
public class BoxStoreFactory {

    private static BoxStore boxStore;


    public static BoxStore getBoxStore() {
        if(boxStore==null){
            throw new NullPointerException("call initBoxStore  ");
        }
        return boxStore;
    }

    public static void createBoxStore(BoxStore boxStore) {
        BoxStoreFactory.boxStore = boxStore;
    }
}
