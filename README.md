##这是一个下拉刷新和下拉加载的Recycleview的控件,实现的想法来自于XListview
使用方法很简单

##看下效果图展示
![这里写图片描述](http://img.blog.csdn.net/20170317141629430?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRWFza1NoYXJr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

###maven引用方式
```
<dependency>
  <groupId>cn.yuan.yu.recycleview</groupId>
  <artifactId>mylibrary</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```
###gradle引用方式
```
compile 'cn.yuan.yu.recycleview:mylibrary:1.0.1'
```
```javascript
    布局文件添加
     <yuan.kuo.yu.view.YRecycleview
        android:id="@+id/ycl"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />



```
### 至于更改上啦刷新和下拉加载的动画,我发现了一个更加简单的方式去实现,就是通过帧动画的形式,直接设置,方便diy设置
###其他的适配器以及正常编写都是和官方的一样,没有任何改变上手比较容易简单
#有问题请反馈
* 邮件(yukuoyuan@hotmail.com)
* QQ: 152046273
更新日志
2017/4/19:修复了因为没有上啦刷新,导致删除数据索引越界的问题,感谢[XiaoDada](https://github.com/XiaoDada)提的bug.
