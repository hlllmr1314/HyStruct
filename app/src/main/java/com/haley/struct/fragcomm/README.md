1，简介
###### Fragment是从Android3.0引入的，主要目的是为了给大屏幕(如平板电脑)上更加动态和灵活的UI设计提供支持。由于平板电脑的屏幕比手机的屏幕大很多，因此可用于组合和交换的UI组件的空间更大，利用Fragment实现此类设计的时，就无需管理对视图层次结构的复杂更改。

2，优势
1. 可复用性。特别适用于模块化的开发，因为一个Fragment可以被多个Activity嵌套，有个共同的业务模块就可以复用了，是模块化UI的良好组件。
2. Fragment有着具有自己一套完整的生命周期，Activity用来管理Fragment。Fragment的生命周期是寄托到Activity中，Fragment可以被Attach添加和Detach释放。
3. 可控性。Fragment可以像普通对象那样自由的创建和控制，传递参数更加容易和方便，也不用处理系统相关的事情，显示方式、替换、不管是整体还是部分，都可以做到相应的更改。
4. Fragments是view controllers，它们包含可测试的，解耦的业务逻辑块，由于Fragments是构建在views之上的，而views很容易实现动画效果，因此Fragments在屏幕切换时具有更好的控制。

3，设计的初衷
###### 来之Android官方的一段话，Two Fragments should never communicate directly.就是说2个Fragment间不要直接通讯，同时官方也推荐使用最新出得android.arch.lifecycle的框架来实现，但是这个相对而言比较重量级的，所以我们希望可以实现一个轻量级的解耦框架

4，思路
###### 实现Fragment和Activity之间的通讯其实有很多的实现方式，比如接口，EventBus，广播，静态变量，单例，广播等。他们之间的利弊我就不详细诉说，用的比较多的就是接口和EventBus。
###### 从性能上考虑接口其实是个不错的选择，而EventBus的原理是采用的反射所以效率会低一些。从耦合度上考虑，EventBus有它的优势，接口耦合度会高一些。所以出于这2个方便考虑，我们就希望设计一个框架可以兼容这个方式有点。

5，面向对象编程
###### 要实现Fragment和Activity之间的通讯，其实我们可以从面向对象的角度进行考虑，要通讯我们肯定需要一个functionName，有的functionName我们还需要入参数和返回值，参数和返回值之前的组合有一下几种。
1. 无参，无返回值（FunctionNoParamNoResult）
2. 无参，有返回值（FunctionOnlyResult）
3. 有参，无返回值（FunctionOnlyParam）
4. 有参，有返回值（FunctionParamAndResult）
###### 针对这4个情况我们可以设计一下我们的接口Function，首先functionName是每个函数的唯一标示，所以必须要有唯一性。再根据上面的4个情况需要设计它的一个函数体。
###### Fragment在使用之前需要进行绑定
