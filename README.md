<div align="center">
  <h1>Mars</h1>
  <p>MongoDB ORM/ODM for Java.</p>
</div>



##  致自己 
造自己的轮子，让别人去说 


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

mars 是 [上海锦木信息技术有限公司](https://www.jinmuinfo.com/) 与 [中国东方航空公司](https://www.ceair.com/) 合作研发的一款ODM/ORM框架  
纪念中国火星探测任务“天问一号”的火星车祝融号成功登陆火星的乌托邦平原【2021年5月15日】,故起名为 mars。

### 🍺mars如何改变我们的coding方式

mars的目标是使得非关系型数据库使用一套标准的方式，即可简单的实现业务逻辑，减少开发者的学习使用成本,彻底改变我们写代码的方式。

mars的存在就是为了减少框架的学习成本，避免网络上参差不齐的代码出现导致的bug,同时避免重复造轮子。

使用一套标准 兼容多种Nosql 。

-------------------------------------------------------------------------------

## 🛠️包含组件
一个MongoDB java  ORM/ODM 框架，同时提供以下组件：

| 模块                |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| mars-core          |     核心包                                    |
| mars-springboot    |     基于springboot 自动注入的插件                                                    |

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


### 🧬贡献代码的步骤及PR遵照的原则

详见wiki  [How2Contribute](https://github.com/whaleal/mars/wiki/Mars-00How2Contribute)



## ⭐欢迎关注

如果你觉得本项目还不错，欢迎持续关注，在此表示感谢^_^。


![whaleal](https://github.com/whaleal/whaleal.github.io/blob/main/images/logo1.png)

## 其他相关项目 
[Project](https://docs.whaleal.com/project)

## 社区支持
本项目由 [whaleal社区](https://www.whaleal.com/)提供支持  



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
