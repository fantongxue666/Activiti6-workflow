
## 三, ActivitiDesigner流程设计器安装与使用（Eclipse版本）



`Eclipse`版本的流程设计器下载地址：链接：https://pan.baidu.com/s/1kgPqXlA75JzBSZ0xvkAG1w 提取码：vgl1

下载完成后解压，然后替换`Eclipse`安装目录列表中的相同的两个目录里面的`jar`包，也就是移到`Eclipse`的安装目录的那两个文件夹，然后重新打开`Eclipse`，就已经有流程设计器插件了。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164926_28994826_5680115.png)

## 四，ActivitiDesigner流程设计器安装与使用（idea版本）

安装`actiBPM`插件，并重启`idea`

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164927_dd0cb383_5680115.png)

安装完成后，在资源文件夹`resources`右键新建`actiBPMFiles`，然后就可以画流程图了。



## 五，Activiti支持的数据库及准备工作

`Activiti`工作流需要数据库的25张表支撑。

数据库：mysql

创建表方式：通过运行java程序创建表。

### 1，快速创建一个springboot项目

过程不用多说

### 2，加入maven依赖的坐标（jar包）

```xml
<properties>
        <java.version>1.8</java.version>
        <slf4j.version>1.6.6</slf4j.version>
        <log4j.version>1.2.12</log4j.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-engine</artifactId>
            <version>7.0.0.Beta1</version>
        </dependency>

        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-spring</artifactId>
            <version>7.0.0.Beta1</version>
        </dependency>
		
        <!--activiti整合springboot的坐标-->
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-spring-boot-starter</artifactId>
            <version>7.0.0.Beta2</version>
        </dependency>
        
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-bpmn-model</artifactId>
            <version>7.0.0.Beta1</version>
        </dependency>

        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-bpmn-converter</artifactId>
            <version>7.0.0.Beta1</version>
        </dependency>

        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-json-converter</artifactId>
            <version>7.0.0.Beta1</version>
        </dependency>

        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-bpmn-layout</artifactId>
            <version>7.0.0.Beta1</version>
        </dependency>

        <dependency>
            <groupId>org.activiti.cloud</groupId>
            <artifactId>activiti-cloud-services-api</artifactId>
            <version>7.0.0.Beta1</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <!-- log start -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        <!-- log end -->

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.5</version>
        </dependency>

        <dependency>
            <groupId>commons-dbcp</groupId>
            <artifactId>commons-dbcp</artifactId>
            <version>1.4</version>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>alfresco</id>
            <name>Activiti Releases</name>
            <url>https://artifacts.alfresco.com/nexus/content/repositories/activiti-releases/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
    </repositories>
```

### 3，配置Activiti连接数据库的配置文件

这里使用`dbcp`连接池连接`mysql`数据库，在`resources`资源文件夹下新建文件`activiti.cfg.xml`文件。

前提是`mysql`数据库新建一个库名为`activiti`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/contex http://www.springframework.org/schema/context/spring-context.xsd
						http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
	<!--数据源配置dbcp-->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver" />
        <property name="url" value="jdbc:mysql://localhost:3306/activiti" />
        <property name="username" value="root" />
        <property name="password" value="1234" />
        <property name="maxActive" value="3" />
        <property name="maxIdle" value="1" />
    </bean>
	<!--activiti单独运行的ProcessEngine配置，使用单独启动方式-->
    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="dataSource" ref="dataSource"></property>
        <property name="databaseSchemaUpdate" value="true"/>
    </bean>

    <!--<bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="jdbcDriver" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/itcast0711activiti"/>
        <property name="jdbcUsername" value="root"/>
        <property name="jdbcPassword" value="root"/>
        <property name="databaseSchemaUpdate" value="true"/>
    </bean>-->
</beans>
```

### 4，运行代码来创建mysql所需要的25张表

随便哪个地方写下面的测试类，然后运行，那么数据库中就已经创建了25张表。

```java
 //测试activiti所需要的25张表的生成
    @Test
    public void testGenTable() {
        //1,创建ProcessEngineConfiguration对象
        ProcessEngineConfiguration configuration=
 	ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        //2，创建ProcessEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        //3，打印processEngine对象
        System.out.println(processEngine);
    }
```

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164926_7a3d17c7_5680115.png)

### 5，上述25张数据库表的命名规则

`Activiti`表都以`ACT_`开头。

- ACT_RE_*，这个前缀的表包含了流程定义和流程静态资源（图片，规则等等）
- ACT_RU_*，这些运行时的表，包含流程实例，任务，变量，异步任务，等运行中的数据，`Activiti`只在流程实例执行过程中保存这些数据，在流程结束后就会删除这些记录。
- ACT_HI_*，这些表包含历史数据，比如历史流程实例，变量，任务等等。
- ACT_GE_*，通用数据，用于不同场景下。

## 六，Activiti架构图

![img](https://images.gitee.com/uploads/images/2020/0528/164927_580f1368_5680115.jpeg)

从图中可以看出，通过`ProcessEngine`对象可以得到下面的好多`Service`，而下面的`Service`的作用就是直接对数据库中的25张表进行增删改查操作，很简便，不用再自己写增删改查了。

`Activiti`架构图说明

- `ProcessEngineConfiguration`类，主要作用是加载`activiti.cfg.xml`文件。

- `ProcessEngine`类，作用是帮助我们可以快速得到各个`service`接口，并且可以生成`activiti`的工作环境，25张表。

- `Service`接口，作用是可以快速实现数据库25张表的操作。

  - ResposityService

  - RuntimeService

  - TaskService

  - HistoryService

    



### 1，使用idea的ActiveDesigner完成流程定义

新建流程`holiday.bpmn`，在`resources`资源文件夹下新建`BPNMFile`文件，并画一个流程，这里举例请假流程。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164926_50bda92f_5680115.png)

1，点击白色区域，看左边的`id`和`name`，每一个流程图都有唯一的`id`和`name`，可以自定义。

2，点击任务格子，看左边的`Assignee`，如下图，是指定该任务分配给谁来执行的。都分配一下，这里分配结果==》填写请假单：zhangsan；部门经理审批：lisi；总经理审批：wangwu。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164926_c7a541a5_5680115.png)



### 2，idea中制作Activiti流程定义的png文件

画完图后，但是并没有生成`png`图片，这个时候重命名刚才创建的文件，把后缀改成`xml`，然后右键

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-OCz5ae6n-1584088100296)(D:\工作\学习资料\我的笔记（图片）\1583144076116.png)\]](https://images.gitee.com/uploads/images/2020/0528/164927_b9b1b3db_5680115.png)

然后保存到与刚才创建文件的一个文件夹内就可以了

下面是保存按钮

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-w2ckHfX9-1584088100296)(D:\工作\学习资料\我的笔记（图片）\1583144893670.png)\]](https://images.gitee.com/uploads/images/2020/0528/164927_47c71756_5680115.png)

<font color="red">如果流程图是中文乱码的情况，那么在`idea`安装目录的下面两个文件添加一句代码。</font>

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-knqRfCLj-1584088100297)(D:\工作\学习资料\我的笔记（图片）\1583144972938.png)\]](https://images.gitee.com/uploads/images/2020/0528/164927_b9608ea8_5680115.png)

```
-Dfile.encoding=UTF-8
```

然后重新启动`idea`就可以了，重新走一遍上面的过程即可。



### 3，Activiti实现流程定义部署的步骤及实现

写一个测试类进行测试流程部署。

```java
 //流程定义的部署
    @Test
    public void testDeploy(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到repositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3，进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday.bpmn") //添加bpmn资源
                .addClasspathResource("holiday.png")
                .name("请假申请流程")
                .deploy();

        //4，输出部署的一些信息
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }
```

打印出

> 1
> 请假申请流程

看数据库表`act_re_deployment`表新增了一条数据。（该表存放部署信息）

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164927_0acbd942_5680115.png)

`act_re_procdef`表也新增了一条数据。（该表存放流程定义的一些信息）

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164927_1d8d3269_5680115.png)

`act_ge_bytearray`新增了两条，这是存储的文件。（该表存放流程定义的bpmn文件及png文件）

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164927_05d94a45_5680115.png)

<font color="blue">总的来说，就是部署一个新的流程时，数据库发生变化的是上面的三张表。</font>



### 4，Activiti实现流程实例启动的步骤及实现

提及一个概念：一个流程定义可以对应多个流程实例。

写一个测试类，启动流程实例。

```java
 //启动流程实例：前提是已经完成了流程定义的部署工作
    @Test
    public void testBeginFlow(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RuntimeService实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3，创建流程实例，流程定义的key需要知道流程唯一id：holiday
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");
        //4，输出实例相关信息
        System.out.println("实例部署id："+processInstance.getDeploymentId());
        System.out.println("流程实例id："+processInstance.getId());
        System.out.println("活动id："+processInstance.getActivityId());
    }
```

打印：

> 实例部署id：null
> 流程实例id：2501
> 活动id：null

看数据表`act_hi_procinst`新增了一条数据。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164927_e264aef8_5680115.png)

启动一个流程实例影响数据库的表：

- act_hi_actinst	已完成的活动
- act_hi_identitylink     参与者信息
- act_hi_procinst       流程实例
- act_hi_taskinst        任务实例
- act_ru_execution      执行表
- act_ru_identitylink       参与者信息
- act_ru_task            任务

### 5，Activiti实现指定用户查询任务列表 [查询待办]

流程启动后，各个任务的负责人就可以查询自己当前需要处理的任务，查询出来的任务都是该用户的待办任务。

写一个测试类，查询指定用户下面的待办任务列表。

```java
 //查询当前用户的任务列表
    @Test
    public void query(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到TaskService对象
        TaskService taskService = processEngine.getTaskService();
        //3，根据流程定义的key，负责人assignee来实现当前用户的任务列表查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("zhangsan")
                .list();
        //4，任务列表的展示
        for(Task task:list){
            System.out.println("流程实例id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        }
    }
```

打印：

> 流程实例id：2501
> 任务id：2505
> 任务负责人：zhangsan
> 任务名称：填写请假单



### 6，Activiti实现当前用户任务处理

写一个测试类，处理当前用户的待办任务。

```java
    //处理任务
    @Test
    public void chuli(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到TaskService对象
        TaskService taskService = processEngine.getTaskService();
        //3，处理任务，结合当前用户任务列表的查询操作，得到任务ID：2505
        taskService.complete("2505");
    }
```

运行之后，该任务就完成了，然后就跳到下一个审批的节点了。

看数据库`act_hi_taskinst`表的变化，填写请假单有了结束时间，然后新增了一条部门经理审批，并且结束时间为NULL，这就说明跳到下一个节点了。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164927_f7f13024_5680115.png)



处理任务操作背后影响的表：

- act_hi_actinst   历史数据
- act_hi_identitylink   参与者信息
- act_hi_taskinst   
- act_ru_execution
- act_ru_identitylink
- act_ru_task  该表从始至终只存一条数据，就是当前执行的节点信息（走到哪一步了），如果流程执行完了则会删除这一条数据。

### 7，Activiti流程部署的zip包方式的补充

将`holiday.bpmn`和`holiday.png`压缩成`holiday.zip`包进行部署，效果和上面的（标题3）一样。

```java
 //流程定义的部署
    @Test
    public void testDeploy(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到repositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //转化出ZipInputStream流对象
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("holiday.zip");
        //将inputStream转化为ZipInputStream流
        ZipInputStream zipInputStream=new ZipInputStream(inputStream);

        //3，进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("请假申请流程")
                .deploy();

        //4，输出部署的一些信息
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }
```

这种方法也可以的。

### 8，Activiti流程定义信息查询

<font color="red">说明：什么是流程定义？流程定义就是启动了的流程图，比方说上面启动了一个请假申请流程，那么该流程定义就是指这个请假申请流程。</font>

写一个测试类，查询流程定义信息。

```java
//流程定义查询
    @Test
    public void defined(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3，得到ProcessDefinitionQuery对象，可以认为它就是一个查询器
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //4，设置条件，并查询出当前的所有流程定义，查询条件：流程定义的key=holiday

        List<ProcessDefinition> holiday = processDefinitionQuery.processDefinitionKey("holiday")
                .orderByProcessDefinitionVersion()  //设置排序方式，根据流程定义的版本号进行排序
                .desc()     //降序排列
                .list();

        //5，输出流程定义信息
        for(ProcessDefinition processDefinition:holiday){
            System.out.println("流程定义ID："+processDefinition.getId());
            System.out.println("流程定义名称："+processDefinition.getName());
            System.out.println("流程定义的Key："+processDefinition.getKey());
            System.out.println("流程定义的版本号："+processDefinition.getVersion());
            System.out.println("流程部署的ID："+processDefinition.getDeploymentId());
        }
    }
```

打印出了流程定义的所有信息。

> 流程定义ID：holiday:1:4
> 流程定义名称：请假流程
> 流程定义的Key：holiday
> 流程定义的版本号：1
> 流程部署的ID：1



### 9，Activiti流程定义信息删除

删除已经部署成功的流程定义。

```java
 	@Test
    public void del(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3，执行删除流程定义，参数代表流程部署的id
        repositoryService.deleteDeployment("1");
    }
```

删除流程定义影响的表和部署流程定义影响的表是一样的，删除是减去，部署是增加，都是同样的三张表。

<font color="red">注意事项：</font>

<font color="red">1，当我们正在执行的这一套流程没有完全审批结束的时候，如果此时要删除流程定义信息就会失败。</font>

<font color="red">2，如果要强制删除，可以使用`repositoryService.deleteDeployment("1",true)`；参数`true`代表级联删除，此时就会先删除没有完成的流程结点，最后就可以删除流程定义信息，`false`值代表不级联删除，不写默认为`false`</font>



## 七，Activiti实现资源文件保存的需求与方案分析

### 1，Activiti流程定义资源

通过流程定义对象获取流程定义资源，获取`bpmn`和`png`。

真实应用场景：用户想查看这个请假申请流程具体有哪些步骤要走？

技术方案：

1. 第一种方式使用`Activiti`的`api`来实现；
2. 其实就是原理层面，可以使用`jdbc`对`blob`类型，`clob`类型数据的读取和保存。
3. `IO`流转换，使用`commons-io.jar`包可以轻松解决`IO`操作。

需求：

1. 数据库里有一个现成的流程定义，现在要把数据库中存放该流程定义的资源文件读取到硬盘的指定位置。
2. 从`act_ge_bytearray`表中读取两个资源文件，并将两个资源文件保存到路径：D:\



### 2，Activiti实现资源文件保存的实现步骤

写一个测试类实现资源文件保存到本地磁盘。

里面输入流和输出流转换需要用到了`commons-io.jar`包，这里引入一个在`idea`中快速搜索并选择版本号并导入依赖的方法。

`Alt`+`Insert`键，选择`Dependency`，搜索依赖的名字，并选择版本号，确定导入即可。

```java
//保存资源文件
    @Test
    public void save() throws IOException {
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3，得到ProcessDefinitionQuery对象，可以认为它就是一个查询器
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //4，设置查询条件
        processDefinitionQuery.processDefinitionKey("holiday");//参数是流程定义的key
        //5，执行查询操作，查询出想要的流程定义
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        //6，通过流程定义信息，得到部署ID
        String deploymentId = processDefinition.getDeploymentId();

        //7，通过repositoryService的方法，实现读取图片信息及bpmn文件信息（输入流）
        //getResourceAsStream()方法的参数说明：第一个参数是部署id，第二个参数代表资源名称
        //processDefinition.getDiagramResourceName() 获取png图片资源的名称
        //processDefinition.getResourceName() 获取bpmn文件的名称
        InputStream pngIs = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());
        InputStream bpmnIs = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());

        //8，构建出OutputStream流
        OutputStream pngOs=new FileOutputStream("D:\\工作\\"+processDefinition.getDiagramResourceName());
        OutputStream bpmnOs=new FileOutputStream("D:\\工作\\"+processDefinition.getResourceName());

        //9，输入流、输出流的转换（commons-io.jar中的方法）
        IOUtils.copy(pngIs,pngOs);
        IOUtils.copy(bpmnIs,bpmnOs);

        //10，关闭流
        pngOs.close();
        bpmnOs.close();
        pngIs.close();
        bpmnIs.close();
    }
```

查看本地`D:/工作`路径下，有了`holiday.png`和`holiday.bpmn`文件，已经下载到了本地。

### 3，Activiti历史信息的查询

即使流程定义已经删除了，但是流程执行的历史信息依然保存在`activiti`的`act_hi_*`相关的表中。所以我们还是可以查询流程执行的历史信息，可以通过`HistoryService`来查看相关的历史记录。

```java
/查询历史数据
    @Test
    public void select(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到HistoryService对象
        HistoryService historyService = processEngine.getHistoryService();
        //3，得到查询器
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        //4，执行查询
        instanceQuery.processInstanceId("2501"); //设置流程实例的id
        List<HistoricActivityInstance> list = instanceQuery.orderByHistoricActivityInstanceStartTime().asc().list();//排序
        //5，遍历查询结果
        for(HistoricActivityInstance instance:list){
            System.out.println(instance.getActivityId());
            System.out.println(instance.getActivityName());
            System.out.println(instance.getProcessDefinitionId());
            System.out.println(instance.getProcessInstanceId());
            System.out.println("===========================");
        }
    }
```

打印

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-OpgI4LLC-1584088100299)(D:\工作\学习资料\我的笔记（图片）\1583565935597.png)\]](https://images.gitee.com/uploads/images/2020/0528/164927_effcf79f_5680115.png)



## 八，Activiti与业务系统整合开发的原理分析

Activiti：数据库中的25张表只是负责`Activiti`的工作正常流程，和业务数据没有关系。

业务系统：存放业务数据。

<font color="red">比如请假流程，提交的表单信息是存到业务数据库中，但是要把`Activiti`和业务系统关联起来，意思就是，提交一个请假审批，`Activiti`怎么知道这个审批流程是谁提交的？因此关联的方法是把业务主键（请假表的`id`字段）保存到`Activiti`里面。</font>

存放到哪里？

`act_ru_execution`表中的`bussiness_key`字段就是业务主键（业务标识）。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164927_bccddb55_5680115.png)

### 1，Activiti实现与业务系统businessKey整合操作

在上面启动流程实例时，参数是一个，也就是流程id。

```java
 		//3，创建流程实例，流程定义的key需要知道流程唯一id：holiday
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");
```

但是要把业务表（请假表）的业务标识（请假表的`id`）和`Activiti`整合起来，需要传入第二个参数`businessKey`，第二个参数就是请假表的`id`。

```java
@Test
    public void bang(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RuntimeService实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3，创建流程实例，流程定义的key需要知道流程唯一id：holiday
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday","1001");
        System.out.println(processInstance.getBusinessKey());
    }
```

打印：1001

观察`act_ru_execution`表，已经和业务`id`绑定了。
![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164928_8a9a2283_5680115.png)

### 2，Activiti全部流程的挂起与激活

需求：公司制度发生了变化，原本没有批完的流程怎么办？

<font color="red">某些情况可能由于流程变更需要当前运行的流程暂停而不是直接删除，流程暂停后将不会继续执行。操作流程定义为挂起状态，该流程定义下边所有的流程实例全部暂停，并不允许启动新的流程实例。</font>

```java
	 //流程定义全部挂起
    @Test
    public void guaqi(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3，查询流程定义的对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
		 //可以根据流程定义的id查询指定流程定义，不过id要到Activiti数据库表中去看，			act_re_procdef表
         //  .processDefinitionId()   
             .processDefinitionKey("holiday") //也可以根据流程定义的key查询
             .singleResult();
        //4，得到当前流程定义的实例是否都为暂停状态
        boolean suspended = processDefinition.isSuspended();
        //5，如果都为暂停状态，则全部激活
       if(suspended){
           repositoryService.activateProcessDefinitionById(processDefinition.getId(),true,null);
           System.out.println("流程定义："+processDefinition.getId()+"被激活");
       }else{
           repositoryService.suspendProcessDefinitionById(processDefinition.getId(),true,null);
           System.out.println("流程定义："+processDefinition.getId()+"被挂起");
       }
    }
```

运行测试类，打印

> 流程定义：holiday:1:4被挂起

再次运行测试类，打印

> 流程定义：holiday:1:4被激活



### 3，Activiti单个流程实例的挂起与激活

> processInstanceId("2501")，是通过流程实例id查询到的流程实例对象

```java
//单个流程实例的挂起
    @Test
    public void single(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3，查询流程实例对象
        ProcessInstance holiday = runtimeService.createProcessInstanceQuery().processInstanceId("2501").singleResult();
        //4，查看当前流程定义的实例是否为暂停状态
        boolean suspended = holiday.isSuspended();
        //判断
        if(suspended){
            runtimeService.activateProcessInstanceById(holiday.getId());
            System.out.println("流程实例："+holiday.getId()+"被激活");
        }else{
            runtimeService.suspendProcessInstanceById(holiday.getId());
            System.out.println("流程实例："+holiday.getId()+"被挂起");
        }
    }
```

打印

> 流程实例：2501被挂起



### 4,Activiti中个人任务分配的固定方式和UEL方式

上面所述，每个节点设置的`Assignee`对应的值为`zhangsan`，代表某个人，那就是固定方式分配，但是被定死了，只有张三才能完成该节点，但是肯定是不行的，请假是所有员工都可以申请的。

<font color="red">引出`UEL`方式</font>

`UEL`方式，也称为统一表达式。

写法：`${变量名}`

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164929_fa838d58_5680115.png)

什么时候设置表达式？

在启动流程实例的时候设置流程变量，如下

```java
 //启动流程实例，动态设置assignee
    @Test
    public void dongtai(){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3，设置assignee的取值，用户可以在界面上设置流程的执行人
        Map<String,Object> map=new HashMap<>();
        map.put("assignee0","zhangsan");
        map.put("assignee1","lisi");
        map.put("assignee2","wangwu");
        //4，启动流程实例，同时还要设置流程定义的assignee的值
        ProcessInstance holiday = runtimeService.startProcessInstanceById("holiday", map);
        System.out.println(holiday.getName());
    }
```

前提：在界面上要分别设置`assignee`的值为`assignee0`，`assignee1`，`assignee2`。

结合项目使用的话，获取到提交请假单的名字或者唯一标识，然后动态赋值，然后存到`activiti`数据库中名字就是该设置的名字。

### 5，监听器方式实现activiti流程assignee设置

在界面中添加监听器。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164928_fb9833f5_5680115.png)

定义任务监听类，该类必须实现`org.activiti.delegate.TaskListener`接口。

```java
public class MyTaskListener implements TaskListener(){
    @Override
    public void notify(Delegate delegate){
        //这里指定任务负责人
        delegate.setAssignee("张三");
    }
}
```

## 九，Activiti的流程变量概述及作用

### 1，白话引入Activiti流程变量

比如说现在有一个贷款流程，发起流程的用户如果贷款数目小于100000，那么业务经理审批完之后就可以到会计节点，然后放贷款，但是如果贷款数目大于100000了，那么就要到总经理节点审批，总经理审批完之后才能到会计节点。

<font color="red">那么问题来了，Activiti怎么知道大于100000就要到总经理审批节点呢？那么就需要用到流程变量了。</font>

### 2，Activiti流程变量的作用域

> global变量：一个任务或一个流程实例。可以理解为全局的，所有节点都可以使用该变量。
>
> local变量：仅仅针对一个任务和一个执行实例范围，范围没有流程实例大。可以理解为局部的，只有一个节点可以使用。

`global`变量名不可以重复，如果名字重复，则会被覆盖。

### 3，Activiti流程变量的使用方式

可以在连线上设置UEL表达式，决定流程走向。

比如：${price>100000}和${price<100000}，price就是变量名称。

点击流程图的连线，设置流程变量即可。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164928_66b4af71_5680115.png)

### 4，Activiti流程变量案例

需求：员工创建请假单，由部门经理审核，部门经理审核通过之后请假3天以下由人事经理直接审核，3天以上先由总经理审核，总经理审核通过之后再由人事经理存档。

定义BPMN文件：

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164928_e84b8306_5680115.png)



> 设置请假单申请assignee：zhangsan，部门审批assignee：lisi，总经理审批assignee：wangwu，人事经理存档assignee：zhaoliu。

画好图之后，点击连线（人事经理前面的连线设置为`${holiday.num<3}`，总经理前面的连线设置为`${holiday.num>3}`），这是`UEL`方式赋值，可以对象点出来的属性这种，对象就是下面的实体类。

然后重复上面的动作：改`bpmn`文件后缀为`xml`文件，然后生成`png`文件，然后再把`xml`恢复成`bpmn`。

<font color="blue">其实不生成`png`也没啥问题，工作流一点儿影响都没有。</font>

```java
//请假实体类
@Data
public class Holiday implements Serializable{
    private Integer id;
    private String holidayName;//申请人名字
    private Date beginDate;//开始时间
    private Date endDate;//结束时间
    private Float num;//请假天数
    private String reason;//请假事由
    private String type;//请假类型
}
```

<font color="red">注意：如果将实体类存储到流程变量中，必须实现序列化接口`seriallizable`，否则在存储这个实体类的时候就会报异常，为了防止由于新增字段无法反序列化，需要生成`serialVersionUID`。</font>

#### （2）部署流程定义

```java
//流程变量的测试
public class HolidayTest {
    //新的请假流程定义的部署
    @Test
    public void start(){
        //得到ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //得到RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        //部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("holiday4.bpmn")
                .addClasspathResource("holiday4.png")
                .name("请假流程-测试流程变量")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
```

运行，这就部署好了该流程定义。

#### （3）启动流程实例，同时设置流程变量

```java
 //启动流程实例，同时设置流程变量
    @Test
    public void begin(){
        //得到ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //得到RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        //流程定义的key：myProcess_1
        String key="myProcess_1";
        Map<String,Object> map=new HashMap<>();
        //设置请假天数
        Holiday holiday=new Holiday();
        holiday.setNum(1F);
        map.put("holiday",holiday);
        //启动流程实例，并设置流程变量的值
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, map);
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getProcessDefinitionId());
    }
```

运行，就启动了一个流程实例。

#### （4）测试流程分支

依次调用此方法完成任务，zhangsan，lisi，wangwu/zhaoliu---------判断流程变量的请假天数

```java
    @Test
    public void redify(){
        //得到ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //得到taskService
        TaskService taskService = defaultProcessEngine.getTaskService();
        //查询当前用户是否存在任务
        String key="myProcess_1";
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee("zhangsan").singleResult();
        //如果有任务，结束
        if(task!=null){
            taskService.complete(task.getId());
            System.out.println("任务执行完毕");
        }
    }
```

张三执行完之后，看`act_hi_actinst`表，张三填写申请已经结束，来到了李四部门经理审批。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164928_22cf5fc5_5680115.png)

然后再执行李四（）把zhangsan改成lisi，运行上面的测试类。

运行完之后，李四的任务也执行完毕了，也就是部门审批完了，看`act_hi_actinst`表的变化。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164929_1a319429_5680115.png)

发现来到了人事经理存档的节点，是因为上面设置的请假天数是1天，所以直接来到了人事，如果请假天数大于3天，那么会先到总经理审批节点，然后才到人事节点。

同理，把lisi改成zhaoliu，也就是执行人事经理存档，运行，看`act_hi_actinst`表的变化。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164929_c6a7a0fe_5680115.png)

流程执行完毕。

### 5，Activiti完成任务时设置流程变量的值

上面的流程变量的赋值是创建流程实例就有值了，而完成任务设置值是完成了该节点之后然后流程变量才有的值。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164929_4ea1e979_5680115.png)

### 6，通过流程实例ID设置流程变量的值

流程实例创建好之后可以得到该实例id，然后在启动流程实例的时候通过流程实例id设置流程变量的值。

```java
    //启动流程实例，同时设置流程变量
    @Test
    public void begin(){
        //得到ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //得到RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        //流程定义的key：myProcess_1
        String key="myProcess_1";
        Map<String,Object> map=new HashMap<>();
        //设置请假天数
        Holiday holiday=new Holiday();
        holiday.setNum(1F);
        map.put("holiday",holiday);
        //启动流程实例，并设置流程变量的值
        //第一个参数：流程实例的id
        //第二个参数：流程变量名
        //第三个参数：流程变量所对应的值
        runtimeService.setVariable("2501","holiday",holiday);
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, map);
//        System.out.println(processInstance.getName());
//        System.out.println(processInstance.getProcessDefinitionId());
    }
```

### 7，通过当前任务ID来设置流程变量的值

首先需要得到当前任务的id。

```java
String taskId="1404";
TaskService taskService=processEngine.getTaskService();
//使用taskService来设置
taskService.setVariable(taskId,"holiday",holiday);
```

## 十，Activiti组任务

需求：前面讲述的，每个节点只能有一个任务负责人，现在要一个节点可以有多个任务负责人。

设置任务候选人：

流程图任务节点的配置中配置`candiddate-users`（候选人），多个候选人之间用逗号隔开。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164929_6930d862_5680115.png)

### 1，Activiti组任务办理流程

- 第一步：查询组任务，指定候选人，查询该候选人当前的待办任务。候选人不能办理业务。

- 第二步：拾取任务，该组任务的所有候选人都可以拾取，将候选人的组任务变成个人任务，原来候选人就变成了该任务的负责人。

  - 如果拾取后不想办理该业务怎么办？

  - 需要将已经拾取的个人任务归还到组里边，将个人任务变成了组任务。

    

- 第三步：查询个人任务，查询方式同个人任务部分，根据assignee查询用户负责的个人任务。

- 第四步：办理个人任务。

### 2，Activiti查询候选人的组任务

```java
 //查询组任务
    @Test
    public void find(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String key="myProcess_1";//流程定义的key
        String candidate_users="zhangsan";//指定某一个候选人
        //查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidate_users)//设置候选用户
                .list();
        //输出
        for(Task task:list){
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
        }
    }
```

其中

> System.out.println(task.getAssignee());

打印为null，是因为查到的任务没有具体的负责人，需要拾取任务。说明张三只是候选人，还不是任务负责人。

### 3，Activiti拾取组任务

候选人拾取组任务后该任务变成自己的个人任务。

```java
 //查询组任务
    @Test
    public void find(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String key="myProcess_1";//流程定义的key
        String candidate_users="zhangsan";//指定某一个候选人
        //查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidate_users)
                .singleResult();
        if(task!=null){
            //第一个参数：任务id，第二个参数：候选人
            taskService.claim(task.getId(),candidate_users);
            System.out.println("任务拾取完毕");
        }
    }
```

候选人张三拾取组任务以后，该任务就已经有任务负责人了（assignee），就是老张了。

### 4，Activiti组任务-用户任务查询&完成任务

上面张三已经拾取了组任务，变成了个人任务，现在张三查询自己的个人任务并完成任务。

```java
@Test
    public void find(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String key="myProcess_1";//流程定义的key
        String candidate_users="zhangsan";
        //查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
               .taskAssignee(candidate_users)//这里就可以指定具体的任务负责人了
                .singleResult();
        if(task!=null){
            taskService.complete(task.getId());
            System.out.println("任务执行完毕");
        }
    }
```

### 5，Activiti组任务-当前用户归还组任务

如果个人不想办理该组任务，可以归还组任务，归还后该用户不再是该任务的负责人。

```java
 //查询组任务
    @Test
    public void find(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String key="myProcess_1";//流程定义的key
        String candidate_users="zhangsan";
        //查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
               .taskAssignee(candidate_users)
                .singleResult();
        if(task!=null){
            //如果设置为null，归还组任务，该任务没有负责人
            taskService.setAssignee(task.getId(),null);
        }
    }
```

<font color="red">说明：建议归还任务前校验该用户是否是该任务的负责人。</font>

<font color="red">也可以通过`setAssignee`方法将任务委托给其他用户负责，注意被委托的用户可以不是候选人。</font>

### 6，Activiti组任务-任务交接的实现

任务交接：任务负责人将任务交给其他候选人办理该业务。

```java
 //查询组任务
    @Test
    public void find(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String key="myProcess_1";//流程定义的key
        String candidate_users="zhangsan";
        //查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
               .taskAssignee(candidate_users)
                .singleResult();
        if(task!=null){
            taskService.setAssignee(task.getId(),"lisi");
            System.out.println("交接任务成功");
        }
    }
```

## 十一，Activiti网关

### 1，排他网关

排他网关用来在流程中实现决策，当流程执行到这个网关，所有分支都会判断条件是否为true，如果为true则执行该分支。

<font color="red">注意：排他网关智慧选择一个为`true`的分支执行，即使有两个分支条件都为`true`，排他网关也会只选择一条分支去执行。</font>

这是关键，因为如果两个分支条件都满足，那同时走两个分支是不现实的，要不然流程就全乱套了。

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164929_debb1bef_5680115.png)

使用方法：在流程图界面拖一个排他网关放在中间就行了。

### 2，并行网关

作用：并行网关把一个流程分成多条分支，并发执行，执行完之后再把这些分支聚合到一起成一个流程，然后继续往下走。

<font color="blue">如果有一个分支没有执行完，那么流程不会继续走，并行网关会一直等所有分支都执行完毕，并聚合在一起之后才接着走流程。</font>

<font color="blue">并行网关并不会解析条件，即使顺序流中定义了条件，也会被忽略。</font>

![在这里插入图片描述](https://images.gitee.com/uploads/images/2020/0528/164929_7398a88e_5680115.png)

### 3，包含网关

这里忽略。