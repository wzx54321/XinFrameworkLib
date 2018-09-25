# XinFrameworkLib

 可以以Module形式引用到项目，作为基础框架。作为一个轻量级的单独配置Common基础框架。
 包括：APP配置、图片加载，网络请求（缓存处理）、数据存储、权限管理、文件管理、通用对话框和popup弹窗、Fragment工具封装等。



###### 整体结构：RxJAVA + Retrofit 2.0 + ObjectBox + MVP


###  使用方法：




##### 方法一：使用git命令

###### 使用条件：
```
你的工程使用了git版本控制,如果没有可以在有git环境的前提下，在工程的跟目录调用：  
$ git init

```

引入到项目使用：  
```
$ git subModule add git@github.com:wzx54321/XinFrameworkLib.git  

```
如果没有使用SSH方式请使用命令：  
```
$ git subModule add https://github.com/wzx54321/XinFrameworkLib.git
```
1) 在父项目中更新使用：  
```
$ git submodule foreach git pull
```
2) 在XinFrameworkLib目录下使用：  
```
$ git pull
```
3) 如果出现'XinFrameworkLib' already exists in the index使用  
```
$ git rm -r --cached XinFrameworkLib
```

#####   方法二：直接导入源码到项目

----------------------------------------------------------------------------------------------------  
  
   
## 如果如果在主工程中使用objectBox数据库并创建实体生成表，需要如下操作
 如果不在主工程中创建，在framework所属的目录下创建表，忽略下方 <li>第一步、<li>第二步、<li>第四步。
 只需要在 xin\framework\store\entity下，创建实体（第三步）即可，及后续操作。




#### 第一步  

工程根目录下build.gradle配置：  
```
dependencies {
       
        classpath "io.objectbox:objectbox-gradle-plugin:1.5.0"
       
    }

...
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "http://objectbox.net/beta-repo/" }
    }
}
```
#### 第二步  

主模块的build.gradle中<code>apply plugin: 'com.android.application'</code>下添加：

```
apply plugin: 'io.objectbox'
```

#### 第三步：创建实体
创建objectbox实体参照如下代码
```
@Entity // 1、必须要有Entity注解
public class EntityTemp {
    @Id // 2、必须要有id的注解和属性
    private long id;
    private String name;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
     // 3、必须要有对应属性的get和set方法
    public String getName() {
        return name;
    }

    // 3、必须要有对应属性的get和set方法
    public void setName(String name) {
        this.name = name;
    }
}

```

#### 第四步

在项目入口调用：
```
// MyObjectBox需要编译生成，并导入正确的路径
BoxStore boxStore = MyObjectBox.builder().androidContext(app).build();

BoxStoreExtedUtils.createBoxStore(boxStore);

```

#### 第五步
创建对应的操作Box类，继承 xin.framework.store.box.base.BaseBoxManager，构造方法根据需要调用：
```
   酌情调用和实现两个构造方法：

      /**
        * 使用默认的BoxStore
        * @param entityClazz
        */
       public BaseBoxManager(Class<T> entityClazz) {
           this(entityClazz, DBConfig.getBoxStore());

       }

       /**
        * 使用工程内自创建的BoxStore{@link MyObjectBox#builder()#androidContext(context)#build()}
        * MyObjectBox为自动生成，对应导入自定义的路径
        * @param entityClazz
        */
       public BaseBoxManager(Class<T> entityClazz, BoxStore boxStore) {
           mBox = boxStore.boxFor(entityClazz);
       }

```




### 对于表的操作参考，类似写法：
[参照代码，点击查看]( https://github.com/wzx54321/XinFrameworkLib/blob/master/src/main/java/xin/framework/store/box/CookieBox.java )
  
## 网络框架使用：


[参照文档，点击这里](https://github.com/wzx54321/XinFrameworkLib/blob/master/src/main/java/xin/framework/http/README.MD)


#

该版本在旧版基础上进行重构和精简
**旧版地址：https://github.com/wzx54321/XinFramework**
# 




#### About Me

炤鑫

个人主页：http://www.shindong.xin

网易博客：http://blog.163.com/ittfxin@126

邮    箱： Get_sugar@hotmail.com

 <div class='row'>
<img src="https://github.com/wzx54321/XinFrameworkLib/blob/master/imgs/blog.png" title="" width="25%" height="25%"/>   
  </div>

```

  Copyright  2017 [炤鑫]

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions
  and limitations under the License.

```
