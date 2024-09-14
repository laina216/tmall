# 仿天猫商城
迷你天猫商城是一个基于SSM框架的综合性B2C电商平台，需求设计主要参考天猫商城的购物流程：用户从注册开始，到完成登录，浏览商品，加入购物车，进行下单，确认收货，评价等一系列操作。
作为模拟天猫商城系统的核心组成部分之一，采用SSM框架的天猫数据管理后台包含商品管理，订单管理，类别管理，用户管理和交易额统计等模块，实现了对整个商城的一站式管理和维护。
（仅供参考，如有问题还请见谅！如果觉得不错的请点击五角星帮忙推荐一下，谢谢各位帅哥美女！）

后端页面兼容IE10及以上现代浏览器，Chrome,Edge,Firebox等浏览器表现效果最佳。

# 部署方式(master-旧基础版本，feature1.0-较新版本)
1.项目使用IntelliJ IDEA开发，请使用IntelliJ IDEA的版本控制检出功能，输入“https://gitee.com/HaiTao87/TmallDemo.git”拉取项目即可。
2.项目数据库master分支为MySQL 5.7版本，feature分支为MySQL 8.0及以上版本，请在码云附件上下载SQL文件或者在resources下的sql文件夹中下载sql，并导入到数据库中。
3.使用IDEA打开项目后，在maven面板刷新项目，下载依赖包。（项目jdk为1.8）
4.在IDEA中启动springboot项目即可（run方式或debug方式都行）。Linux服务器部署为：maven打成jar包运行，运行命令：java -jar 包名
5.账户名和密码详见附件中的sql文件或者在resources下的sql文件夹中的sql文件（前台页面和后台页面账户密码下方有说明）。
master分支数据库sql如下：
![master分支数据库sql](https://images.gitee.com/uploads/images/2020/1016/150457_5c0c7304_996301.png "屏幕截图.png")

feature分支数据库sql如下：

![feature分支数据库sql](images/1700038011674.jpg)
6.swagger接口文档地址：http://localhost:8082/tmall/swagger-ui.html

7.Druid Monitor监控地址：http://localhost:8082/tmall/druid/sql.html（账号：admin 密码：123456）

注意事项：后台管理界面的订单图表没有数据为正常现象，该图表显示的为近7天的交易额。

---后台界面(部分)--- 访问地址：http://localhost:8082/tmall/admin/login（账户名和密码在admin表里）
![登录界面](images/img.png)
![首页](images/img_1.png)
![产品列表](images/img_2.png)
![添加产品](images/img_3.png)
![产品详情](images/img_4.png)
![产品类别列表](images/img_5.png)
![属性分类列表](images/img_12.png)
![添加属性](images/img_13.png)
![评论管理](images/img_6.png)
![用户列表](images/img_7.png)
![用户详情](images/img_8.png)
![订单列表](images/img_9.png)
![订单详情](images/img_10.png)
![管理员详情](images/img_11.png)

---前台界面(部分)--- 访问地址 http://localhost:8082/tmall/login (账户名和密码在user表里)
![登录界面](shop/img.png)
![首页](shop/img_1.png)
![产品详情](shop/img_2.png)
![购物车结算](shop/img_3.png)
![下单界面](shop/img_4.png)
![支付页面](shop/img_5.png)
![成功支付](shop/img_6.png)
![订单列表](shop/img_7.png)
![确认收货](shop/img_8.png)
![评价](shop/img_9.png)
![产品列表](shop/img_10.png)
![购物车](shop/img_11.png)
