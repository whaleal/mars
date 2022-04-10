<div align="center">
  <h1>Mars</h1>
  <p>MongoDB ORM/ODM for Java.</p>
</div>



##  致自己 
造自己的轮子，让别人去说 ；



<p align="center">
	<a href="https://whaleal.com/"><img src="https://docs.whaleal.com/images/logo1.png" width="45%"></a>
</p>
<p align="center">
	<a href="https://whaleal.com/"><img src="https://docs.whaleal.com/images/logo1.png" width="45%"></a>
</p>
<p align="center">
	<strong>Mars - Object Relational Mapping Framework for NoSql (ORM)</strong>
</p>
<p align="center">
	<strong>Mars - Object Document Mapping Framework for NoSql (ODM)</strong>
</p>
<p align="center">
	👉 <a href="https://whaleal.com">https://whaleal.com/</a> 👈
</p>

<p align="center">
	<a target="_blank" href="https://search.maven.org/artifact/com.whaleal.mars/mars-all">
		<img src="https://img.shields.io/maven-central/v/com.whaleal.mars/mars-core.svg?label=Maven%20Central" />
	</a>
	<a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href='https://github.com/whaleal/mars'>
		<img src="https://img.shields.io/github/stars/whaleal/mars.svg?style=social" alt="github star"/>
	</a>
</p>



-------------------------------------------------------------------------------

[**🌎English Documentation**](README-EN.md)

-------------------------------------------------------------------------------

## 📚简介
mars是一款 关系映射框架和我们经常接触的JPA ,反射技术息息相关，本项目主要针对非关系型数据库，NoSql 大类。其相关性能已经超越 spring-data ，可以完全取代。

mars与spring具有兼容性。本身亦可单独使用。  

mars 目标是兼容大多数的非关系型数据库，使用一套的标准查询方式 ，即可查询多种数据库。减少使用上的学习成本。让开发者有更多的精力创造业务上的价值。

### 🎁mars名称的由来

mars ，是上海锦木信息技术有限公司与 中国东方航空公司 共同研发的一款ODM/ORM框架，纪念中国火星探测任务“天问一号”的火星车祝融号成功登陆火星的乌托邦平原【2021年5月15日】，故起名为 mars。

### 🍺mars如何改变我们的coding方式

mars的目标是使得非关系型数据库使用一套标准的方式，即可简单的实现业务逻辑，减少开发者的学习使用成本，彻底改变我们写代码的方式。

mars的存在就是为了减少框架的学习成本，避免网络上参差不齐的代码出现导致的bug,同时避免重复造轮子。

使用一套标准 兼容多种Nosql 。

-------------------------------------------------------------------------------

## 🛠️包含组件
一个MongoDB java  ORM/ODM 框架，同时提供以下组件：

| 模块                |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| mars-core        |     核心                                         |
| mars-spring      |     基于spring的框架                                                    |
| mars-bson         |    mongodb-bson 的拓展 ，可以完全取代                                                   |

可以根据需求对每个模块单独引入，也可以通过引入`mars-core`方式引入所有模块。

-------------------------------------------------------------------------------

## 📝文档 

* [See the usage docs](https://github.com/whaleal/mars/wiki)
* [Download Latest](https://github.com/whaleal/mars/releases)


-------------------------------------------------------------------------------

## 📦安装

### 🍊Maven
在项目的pom.xml的dependencies中加入以下内容:

```xml
<dependency>
    <groupId>com.whaleal.mars</groupId>
    <artifactId>mars-core</artifactId>
    <version>x.x.x</version>
</dependency>
```

### 🍐Gradle
```
implementation 'com.whaleal.mars:mars-core:x.x.x'
```

### 📥下载jar

点击以下链接，下载`mars-core-X.X.X.jar`即可：

- [Maven中央库](https://repo1.maven.org/maven2/cn/mars/mars-core/)

> 🔔️注意
> 所有版本编译起始均为JDK8+

### 🚽编译安装

访问mars的github主页：[https://github.com/whaleal/mars](https://github.com/whaleal/mars) 下载整个项目源码（v1-main或v1-dev分支都可）然后进入mars项目目录执行：

```sh
mvn install 
```

然后就可以使用Maven引入了。

-------------------------------------------------------------------------------

## 🏗️添砖加瓦

### 🎋分支说明

mars的源码分为两个分支，功能如下：

| 分支       | 作用                                                          |
|-----------|---------------------------------------------------------------|
| main  | 主分支，release版本使用的分支，与中央库提交的jar一致，不接收任何pr或修改 |
| v1-main | 大版本主分支，不同大版本具有不同vxx-main, 用于合并v1-dev,观察特性,不接收任何pr或修改 |
| v1-dev    | 开发分支，默认为下个版本的SNAPSHOT版本，接受修改或pr                 |

### 🐞提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本呢、mars版本和相关依赖库版本。

- [github issue](https://github.com/whaleal/mars/issues)


### 🧬贡献代码的步骤
1. 在github issues 上找到需要修复的问题，或提出要贡献的特性内容  
2. 在github或者Github上fork项目到自己的repo  
3. 把fork过去的项目也就是你的项目clone到你的本地  
4. 修改代码（记得一定要修改v1-dev分支）并进行相关测试  
5. commit后push到自己的库（v1-dev分支）  
6. 登录github或Github在你首页可以看到一个 pull request 按钮，点击它，选择自己的dev 分支及本项目的dev 分支，填写一些说明信息，然后提交即可。  
7. 等待维护者合并  

### 📐PR遵照的原则

mars欢迎任何人为mars添砖加瓦，贡献代码，不过维护者是一个强迫症患者，为了照顾病人，需要提交的pr（pull request）符合一些规范，规范如下：

1. 注释完备，尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，必要时请添加单元测试，如果愿意，也可以加上你的大名。
2. mars的缩进按照IDEA,IDEA真香，默认（tab）缩进，所以请遵守（不要和我争执空格与tab的问题，这是一个病人的习惯）。
3. 新加的方法不要使用第三方库的方法，mars遵循无依赖原则（除非在extra模块中加方法工具）。
4. 请pull request到`v1-dev`分支。mars在1.x版本后使用了新的分支：`v1-main`是主分支，表示已经发布中央库的版本，这个分支不允许pr，也不允许修改。

-------------------------------------------------------------------------------


## ⭐欢迎关注

如果你觉得本项目还不错，欢迎持续关注，在此表示感谢^_^。


![whaleal](https://github.com/whaleal/whaleal.github.io/blob/main/images/logo1.png)

## 其他相关项目 
[Project](https://docs.whaleal.com/project)

## What is Mars

 Mars is a unified driver platform product  developed by Shanghai Jinmu Information Technology Co., Ltd., which is based on Mongodb's driver layer framework and developed with Java language. It makes further innovation on the basis of the original driver function, making the operation more convenient and fast.
 
It uses similar query method as spring, and has higher performance than spring. In the preliminary comparative test, it is improved more than 25% than spring.




## LICENSE

Mars is free and the source is available. All versions released after  2020, including patch fixes for prior versions, are published under the [Server Side Public License (SSPL) v1](LICENSE.md). See individual files for details.

## supporter
<img src="https://www.jinmuinfo.com/community/MongoDB/docs/images/logo/jinmu.png" width="310px" alt="Jinmu Logo">

If you have any problems or suggestions, welcome to contact us!  
 
Hotline：021-58870038 / 021-66696778  
Email：support@jinmuinfo.com  
Office address: building 10, 1228 Jiangchang Rd, Jiangan District, Shanghai, P.R.C  
