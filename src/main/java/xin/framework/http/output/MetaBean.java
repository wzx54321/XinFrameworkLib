package xin.framework.http.output;

import com.google.gson.annotations.SerializedName;

/**
 * <li>Description:判断网络请求数据返回是否成功的信息
 * <li>Created by xin on 2018/6/12.
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class MetaBean {
    /**
     * code : 0
     * message : success
     */

    @SerializedName("code")
    private int codeX;
    @SerializedName("message")
    private String messageX;

    public int getCodeX() {
        return codeX;
    }

    public void setCodeX(int codeX) {
        this.codeX = codeX;
    }

    public String getMessageX() {
        return messageX;
    }

    public void setMessageX(String messageX) {
        this.messageX = messageX;
    }
}
