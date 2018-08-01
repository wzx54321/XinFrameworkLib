
### 说明：
       轻量化仅支持客户端使用GET和POST请求，根据不同的参数类型区分get和post请求（参数参考Retrofit形式）。
       轻量化仅支持了先读缓存后调网络和无缓存处理，是否使用缓存根据XinNetRequest.Builder的CacheKey去判断。
       如需要其他请求方法或需要更多缓存方式等其他动态配置需求，请扩展添加，或者邮件提出后续不断更新。


---------------------------------------------------------------------------------------------------------------------------
#### 1.网络请求调用：

 通过 XinRequest.Builder 去构建一个XinRequest,请求调用如下:
```
   new Net().request(
                  new XinRequest.Builder().setBaseUrl(url).setSuffixUrl("homepage/info").
                          setCacheKey(CacheKeyConfig.HOME_KEY)./* 缓存的唯一的KEY,如果没有设置视为无缓存处理 */
                          setFieldMap(map)./* 构造参数 */
                          setHeaders(headers)./* 动态构造头数据 */
                          setRetryCount(3)./* 设置重试次数 */
                          setLifecycleTransformer(lifecycleTransformer)./*  是否关联lifecycleTransformer,根据具体业务添加 */
                          setListener(HomePageBean.class, new XinReqCallback<HomePageBean>() {/* 设置回调 */
                              @Override
                              public void onSuccess(HomePageBean rspObj) {/* 成功 */
                                // 更新UI

                              }

                              @Override
                              public void onError(int code, String details) {/* 失败 */
                                //  异常处理
                              }
                          }).build()).OK();
 ```
#### 2.下载文件调用如：
通过 DownloadRequest.Builder 去构建一个DownloadRequest,请求调用如下:
```
           new Net().downloadFile(new DownloadRequest.Builder().
                setLifecycleTransformer(lifecycleTransformer1).
                setUrl("http://dldir1.qq.com/weixin/android/").
                setSuffixUrl("weixin667android1320.apk").
                setFile(FileConfig.getPublicFile(FileConfig.DIR_DOWNLOAD,Math.random()+".apk")).
                setQueryParams(null).
                setCallback(new DownUpCallback() {
                    @Override
                    public void progress(ProgressData progress) {
                        Log.i("download progress       :--->" + progress.getFormatDownloadSize());
                        Log.i("download progress Status:--->" + progress.getFormatStatusString());
                        Log.i("download progress       :--->" + progress.getPercent());
                    }

                    @Override
                    public void onStart() { Log.i(" download       :--->  开始" );  }

                    @Override
                    public void onComplete() {Log.i(" download       :--->  完成" );}

                    @Override
                    public void onError(int code, String details) {  Log.i(" download    onError   :--->   details" );  }
                })
                .build()

        );
```

#### 3.上传文件调用如：
通过 UploadRequest.Builder UploadRequest,请求调用如下:
  ```
     final Map<String, String> headersUpload = new HashMap<>();
            headersUpload.put("key", "");
            headersUpload.put("Accept", "application/json");
            headersUpload.put("Accept-Encoding", "gzip");
            headersUpload.put("connection", "alive");
            new Net().uploadFile(new UploadRequest.Builder().
                            setBaseUrl("url...").
                            setHeaders(headersUpload).
                            addFile("file", new File("/storage/emulated/0/12345.jpg"),
                                    new DownUpCallback() {
                                        @Override
                                        public void progress(ProgressData progress) {   Log.i(progress.getPercent());  }

                                        @Override
                                        public void onStart() {  Log.i("上传 onStart");  }

                                        @Override
                                        public void onComplete() {   Log.i("上传 完成");  }

                                        @Override
                                        public void onError(int code, String details) { Log.i("上传错误：" + details);  }
                                    }).setRepCallback(String.class, new XinReqCallback<String>() {
                        @Override
                        public void onSuccess(String str) {   Log.i(str);  }

                        @Override
                        public void onError(int code, String details) {   Log.i(details);   }
                    }).build()


            );
```

--------------------------------------------------------------------------------------------------------------------------

  TODO  错误处理

  TODO 断点续传（待定）

--------------------------------------------------------------------------------------------------------------------------






