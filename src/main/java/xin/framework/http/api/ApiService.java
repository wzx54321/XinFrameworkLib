package xin.framework.http.api;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 网络请求
 * <p>
 * 作者：xin on 2018/7/27 17:59
 * <p>
 * 邮箱：ittfxin@126.com
 */
public interface ApiService {

    @GET
    Observable<ResponseBody> get(@Url String url,@HeaderMap Map<String, String> headers);


    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap  Map<String, String> queryMap,@HeaderMap Map<String, String> headers);

    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> postForm(@Url() String url, @FieldMap Map<String, Object> fieldMap,@HeaderMap Map<String, String> headers);

    @POST
    Observable<ResponseBody> post(@Url String url, @Body RequestBody body,@HeaderMap Map<String, String> headers);


    @Streaming
    @GET()
    Observable<ResponseBody> download(@Url String url,  @QueryMap  Map<String, String> queryMap);

    @Multipart
    @POST()
    Observable<ResponseBody> upload(@Url String url, @Part List<MultipartBody.Part> parts);
}
