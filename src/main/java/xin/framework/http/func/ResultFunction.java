package xin.framework.http.func;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import xin.framework.http.output.BaseOutPut;
import xin.framework.http.output.MetaBean;
import xin.framework.utils.android.Loger.Log;

/**
 *  结果在这里转换
 */
public class ResultFunction<T> implements Function<ResponseBody, BaseOutPut<T>> {
    protected Type type;

    public ResultFunction(Type type) {
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
            Log.e(e,"ResultFunction");
            baseOutPut.getMeta().setMessageX(e.getMessage());
        } finally {
            responseBody.close();
        }
        return baseOutPut;
    }

    private BaseOutPut parseApiResult(String json, BaseOutPut outPut) throws JSONException {
        if (TextUtils.isEmpty(json)) return null;

        if(!isJSON(json)){
            outPut.setData(json);
            MetaBean metaBean = new MetaBean();
            metaBean.setCodeX(0);
            metaBean.setMessageX("返回数据不是json类型");
            outPut.setMeta(metaBean);
            return outPut;
        }

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




    public final static boolean isJSON(String jsonStr) {
        try {
            new Gson().fromJson(jsonStr,Object.class);
            return true;
        } catch(JsonSyntaxException ex) {
            return false;
        }
    }
}
