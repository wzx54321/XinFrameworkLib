
 实现加载缓存，在加载网络


 调用如：
  Net.requestWithCache(CacheKeyConfig.HOME_KEY, HomePageBean.class, lifecycleTransformer
                , new NetworkRequest<>(Net.getApiManager(url, MyApiManager.class).getHomePageData(map)), new ResultListener() {
                    @Override
                    public void onLoadSuccess(BaseOutPut o) {
                        HomePageBean data = (HomePageBean) o.getData();

                        getV().showData(data);
                    }
                });







 说明：

   1.MyApiManager需要您自己实现接口请求的interface 如：

    public interface MyApiManager  {
    @POST("...")
    @FormUrlEncoded
    @Headers({...})
    Flowable<BaseOutPut<HomePageBean>> getHomePageData(@FieldMap Map<String, String> map);
}

    2.HomePageBean为返回的数据。自行定义