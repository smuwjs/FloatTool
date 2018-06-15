# FloatTool

> 只需几行代码就能让View浮动在整个APP的界面上

## 1.初始化View
```java
ImageView iv = new ImageView(getBaseContext());
iv.setImageResource(R.mipmap.ic_launcher);
FloatManager.getInstance().initialize(iv);
```

## 2. 显示
```java
FloatManager.getInstance().show();
```

## 3.隐藏
```java
FloatManager.getInstance().dismiss();
```

![截图](arts/screenshot.png "截图")