package xin.framework.http.func;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import xin.framework.http.output.BaseOutPut;
import xin.framework.http.output.MetaBean;

/**
 * @Description: ResponseBody转ApiResult<T>
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-12-30 17:55
 */
public class ResultFunc<T> implements Function<ResponseBody, BaseOutPut<T>> {
    protected Type type;

    public ResultFunc(Type type) {
        this.type = type;
    }

    @Override
    public BaseOutPut<T> apply(ResponseBody responseBody) throws Exception {
        BaseOutPut<T> baseOutPut = new BaseOutPut<>();
        MetaBean metaBean = new MetaBean();
        baseOutPut.setMeta(metaBean);
        metaBean.setCodeX(-1);
        try {
            String json = responseBody.string();
            BaseOutPut result = parseApiResult(json, baseOutPut);
            if (result != null) {

                if (result.getData() != null) {
                    if (type.equals(String.class)) {
                        baseOutPut.setData((T) result.getData());
                    } else {
                        T data = new Gson().fromJson(result.getData().toString(), type);
                        baseOutPut.setData(data);
                    }
                    baseOutPut.setMeta(result.getMeta());
                } else {
                    baseOutPut.getMeta().setMessageX("ApiResult's data is null");
                }
            } else {
                baseOutPut.getMeta().setMessageX("json is null");
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            baseOutPut.getMeta().setMessageX(e.getMessage());
        } finally {
            responseBody.close();
        }
        return baseOutPut;
    }

    private BaseOutPut parseApiResult(String json, BaseOutPut outPut) throws JSONException {
        if (TextUtils.isEmpty(json)) return null;
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("data")) {
            outPut.setData(jsonObject.getString("data"));
        }

        if (jsonObject.has("meta")) {
            JSONObject meta = jsonObject.getJSONObject("meta");
            MetaBean metaBean = new MetaBean();
            if (meta.has("code")) {
                metaBean.setCodeX(meta.getInt("code"));
            }
            if (meta.has("message")) {
                metaBean.setMessageX(meta.getString("message"));
            }
            outPut.setMeta(metaBean);
        }
        return outPut;
    }
}
