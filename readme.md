# 1.引包

在根目录下build.gradle下repositories里面添加

```groovy
maven { url 'https://jitpack.io' }
```

在项目目录下build.gradle的dependencies里面添加

```groovy
implementation 'com.github.HolmesBill:AllLogCollector:1.1'
```





# 2.用法

## 2.1 简单用法

收集所有日志，有用的无用的都有

```kotlin
 LogCollector.init(this)
             .start();
```

## 2.2 添加Tag过滤

```kotin
tagsFilter(tags: List<String>)
```

## 2.3 添加不收集的Tag

```kotin
setWithoutMatchTags(tags: MutableList<String>)
```

## 2.4 添加定时任务

```kotin
setOperations(taskList: MutableList<BaseLogcatHandlerTask>)
```

同时也可以利用下面的方法来设置定时任务的周期

```kotin
setTaskDelay(timeDelay: Long, timeDelayTimeUnit: TimeUnit):
```

