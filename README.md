# Alarm
Android移动开发—闹钟
Build Tools Version=25.0.2
Compile Sdk Version=API 19 Android 4.4.4
注意：Alarm Manager主要是用来在特定时刻运行你的代码，即便是你的应用在那个特定时刻没有跑的情况。
　　对于常规的计时操作(ticks, timeouts, etc)，使用Handler处理更加方便和有效率。
另：从API 19开始，alarm的机制都是非准确传递，操作系统将会转换闹钟，来最小化唤醒和电池使用。
设置闹钟的时候注意：
　　1.如果声明的triggerAtMillis是一个过去的时间，闹钟将会立即被触发。
　　2.如果已经有一个相同intent的闹钟被设置过了，那么前一个闹钟将会取消，被新设置的闹钟所代替。
