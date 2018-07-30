
 说明：
       轻量化仅支持客户端使用GET和POST请求，根据不同的参数类型区分get和post请求（参数参考Retrofit形式）。
       轻量化仅支持了先读缓存后调网络和无缓存处理，是否使用缓存根据XinNetRequest.Builder的CacheKey去判断。
       如需要其他请求方法或需要更多缓存方式等其他动态配置需求，请扩展添加，或者邮件提出后续不断更新。


----------------------------------------------------------------------------------------------------------------------------------------
 1.网络请求调用：

 通过 XinRequest.Builder 去构建一个XinNetRequest。再由NetWork请求调用如下

   new Net().request(
                  new XinRequest.Builder().setBaseUrl(url).setSuffixUrl("homepage/info").
                          setCacheKey(CacheKeyConfig.HOME_KEY)./* 缓存的唯一的KEY,如果没有设置视为无缓存处理 */
                          setFieldMap(map)./* 构造参数 */
                          setHeaders(headers)./* 动态构造头数据 */
                          setLifecycleTransformer(lifecycleTransformer)./*  是否关联lifecycleTransformer,根据具体业务添加 */
                          setListener(HomePageBean.class, new XinReqCallback<HomePageBean>() {/* 设置回调 */
                              @Override
                              public void onSuccess(HomePageBean rspObj) {/* 成功 */
                                // 更新
                                  getV().showData(rspObj);
                              }

                              @Override
                              public void onError(int code, String details) {/* 失败 */
                                // TODO something
                              }
                          }).build()).OK();
  2.下载文件调用：


  3.上传文件调用：



----------------------------------------------------------------------------------------------------------------------------------------
  TODO 错误处理
  TODO 重试支持
  TODO 上传、下载
  TODO 泛型处理
----------------------------------------------------------------------------------------------------------------------------------------






