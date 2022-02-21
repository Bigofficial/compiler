# ReadMe



本次实验分为三个阶段，采用增量式开发。分别为词法分析器，语法分析器，解释器。

**1.** **词法分析器**

1）词法分析器最主要的的是识别记号，按照ppt我把记号定义为Token类，它拥有类别type这是一个Toke_type的枚举，后者是用来记录这些记号种类，属性lexeme存输入的字符串，value存常数的值, func存函数接口。

​                               

在这里我把函数定义为一个接口，具体的函数实现在对应的类中，这样在后续可以使用多态完成函数的调用。

 

以cos为例

 

2）词法分析器主要分为三个过程，InitScanner初始化词法分析器，GetToken通过词法分析器获得一个记号，CloseScanner关闭词法分析器。

3）在GetToken中，先过滤掉空格，回车，tab。对于dfa，程序中并没有像ppt中那样设计出具体状态和转移，但思路是类似的。假如读取的第一个字符是A-Za-z那么它一定是函数，关键字，PI，E，接下来继读取后续的字符直到它不是数字和字符为止，具体实现和这个dfa相似。

 

 

读取的第一个字符是数字的话它就是常量，先匹配整数部分在匹配小数点，再匹配小数部分。假如不是字母和数字，它就一定是符号，对于符号,()+这些只匹配到一次就行了，而-号还需再读看后面是否是-，是的话还要再读取注释内容，*还要再读判断乘方情况，这些的具体实现和第一个字符是A-Za-z类似这里就不贴代码了。在判断过程中采用最大化匹配字符原则，会判断识别出来的token是否在字符表Token_Table中，若不在返回ERRTOKEN。

4）结果测试：

测试用例：1：**origin is (200+1/1,3\*3\**3); //注释**

**for T from 0 to 120 step 1 draw (cos(t), sin(t));**

**2****：rot is 3...3;**

测试结果：

1：

 

 

2：

 

符合预期

**2.** **语法分析器**

1）主要工作是设计函数绘图语言的文法，使其适合递归下降分析；设计语法树的结构，用于存放表达式的语法树；设计递归下降子程序，分析句子并构造表达式的语法树。 Parser的属性

 

  2）参照ppt将文法消除二义性，左递归最终变换成对应的EBNF形式产生式，使其适合递归下降分析。对于语法树的结点，分为四类叶子结点(变量T)、叶子结点(常数)、两个孩子的内部结点(二元运算)、一个孩子的内部结点(函数以及函数地址)。

 

​     

  3）语法分析器以Parser为入口，依照EBNF构造递归下降程序参考ppt，Program，Statement，OriginStatement，ScaleStatement，RotStatement，按照EBNF文法构造出识别对应结构的句子。Expression，Term，Factor，Component，Atom这些语句识别对应的运算符，构造出适合的TreeNode结点。以RotStatement，

Expression为例

 

 

 

  4）辅助程序PrintSyntaxTree先序遍历输出树在Expression最后执行，FetchToken获取记号，MatchToken匹配终结符，SyntaxError出错处理，MakeTreeNode构造树的结点。

5）结果测试：

测试用例:1：**origin is (200+1/1,3\*3\**3); //注释**

**for T from 0 to 120 step 1 draw (cos(t), sin(t));**

**2****：rot is 3...3;**

 

测试结果：  1：缩进代表层数。

 

 

2：

 

符合预期。

 

**3****．语义分析**

  1） 语义分析器的主要任务是：根据语言结构，处理函数绘图语言程序的语义。根据语法分析构建的语法树，采用深度优先后序遍历即可计算出表达式的值，其次还可以在图形界面中画出每个坐标点。将语义分析和语法分析合并为一个阶段。Semantic的属性

 

  2)语义分析继承语法分析，在其中再重写OriginStatement，ScaleStatement，ForStatement，RotStatement，这样可以直接super.方法调用父类Parser的对应的方法。还能使用父类定义过的那些属性，得到相应变换的值进行绘图。我以RotStatement为例，调用Parser的RotStatementment可以得到旋转的角度(angle_ptr)属性，再赋给Semantic的rot_angle属性，这样后续计算坐标就可以使用。其他的语句类似，这里不赘述。

 

  3）辅助函数，我们利用GerTreeValue就可以获得表达式的值。使用CalCoord(TreeNode hor_ptr,TreeNode ver_ptr,Main.Point point)比例变换，旋转变化，平移变换计算出当前点的值，DrawLoop(double start_val,double end_val,double step_val,TreeNode x_ptr,TreeNode y_ptr)循环绘制点。

  4）绘制图像，使用java的Graphics.drawline方法。

  5）结果测试：

测试用例：**rot is 0;**

**origin is (300,500);**

**scale is (2,1);**

**for T from 0 to 300 step 1 draw (t,0);**

**for T from 0 to 300 step 1 draw (0,-t);**

**for T from 0 to 120 step 1 draw (t,-t);**

**scale is (10,5);**

**for T from 0 to 60 step 1 draw (t,-sqrt(t));**

**scale is (100,100);**

**FOR T FROM 0 TO 2\*PI STEP PI/50 DRAW (cos(T), sin(T));**

测试结果：  

 

  符合预期