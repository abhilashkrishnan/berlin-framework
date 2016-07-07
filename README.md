# Berlin Framework
Dependency Injection, IoC and MVC Framework for the web. 

##Latest News

> Working on improved and faster performance upgrade on Dependency Injection and Inversion of Control (IoC)

#Why another framework?

Berlin Framework aims to be a `superheroic and innovative polygot web framework` with support for `Java, Scala, JavaScript, PHP, Python, Ruby and Groovy`. What it means is that you will be able to write complete web applications in any or all of those programming languages using Berlin Framework. We are also exploring the possibility of supporting `Go Programming Language` in Berlin Framework. 

Berlin Framework will provide you `hassle-free` interoperability between code written in multiple programming languages. For example you will be able to write a web module in PHP or Python or Ruby and a database module in Java or Scala. You will be seamlessly able to call data access operations of the Java or Scala database module from the web module written in PHP or Python or Ruby.

Broadly speaking, there are many ways to achieve inter process communication (IPC). They are,
*  Using java.lang.Runtime.exec() from Java applications
*  Using java.lang.ProcessBuilder from Java applications
*  Using RMI (Remote Method Invocation) between Java applications
*  Using CORBA (Common Object Request Broker Architecture)
*  RPC (Remote Procedure Call)
*  File
*  Pipe  
*  Shared Memory 
*  Sockets
*  Databases
*  Web Services

In Berlin Framework invocation of methods across disparate languages in the same container will be achieved through `API Bridge`. It will work as a `native method call` without the use of any additional configuration or IDL (Interface Definition Language). Since it uses a `native method call` you will rarely experience any performance degradation in the application.

Your development pipeline will be able to `mix-and-match` and be `flexible` enough to choose the programming language which matters the most for a specific task. Berlin Framework will `automagically` inject the dependencies and provide you unmatched integration and interoperability across the code written in multiple programming languages. Sounds exciting? 

We certainly plan to take web application development to the `next level` and be `enterprise ready`! 

##Native Method Call
This section will be elaborated upon the release of version 1.0 of Berlin Framework

##Scalability 
Having web application packaged with polygot modules deployed in the same container doesn't mean you cannot scale. You will be able to scale not only vertically (Scale Up/Down) but also horizontally (Scale In/Out). You will be able to deploy the web application consisting of polygot modules distributed across the container nodes. In such a scenario you should be able to provide `reverse proxy or load balancing` solutions to serve the web applications deployed in the `distributed container nodes`.

##Distributed Polygot Modules, an evolution inside
Apart from the deployment of web application packaged with polygot modules in the same container we are also planning to support `distributed polygot modules` i.e. polygot modules deployed in containers distributed across the network. For e.g. Server1 in your network will host the container for module written in PHP or JavaScript and Server2 will host the container for module written in Java or Scala. PHP or Javascript module will be able to invoke methods on Java or Scala module distributed across the network. In such a scenario Berlin Framework will have to bend the rules and fallback on RPC to invoke the methods across `distributed polygot modules`.

##Features

```
Dependency Injection
Inversion of Control (IoC) Container
Singleton and Prototype beans
Auto-wiring beans by Type and Name
Http POST and GET request support
JSON request and response auto mapping
```
##What next?

```
Bean Scoping
Aspect Oriented Programming (AOP) support
XML request and response auto mapping
JDBC and Object Relational Mapping (ORM) support
Security support
Polygot Programming support
SOAP and REST Web Services support
Micro Services support
Http PUT DELETE HEAD OPTIONS support
WebSocket support
Reactive Programming support
NoSQL support
JMS support
Rule Engine support
BPM support
Reporting support
Scheduler support
Big Data support
```
##How to contribute?
Connect with us through GitHub. We will try to honour your interest and time in the best possible way. 

##Development
The project is divided into sub projects. You can use your preferred IDE for development.

When we introduce Polygot support later, we are planning to release plugins for IntelliJ IDEA and Eclipse which will enable you to develop, build, test and deploy polygot web applications using Berlin Framework.

##Build
We are using Gradle to build the projects. 

##Tutorials
We suggest you to build and run the berlin-webmvc-test project.

More tutorials coming soon!

##License
Berlin Framework is licensed under `GNU Lesser General Public License version 2.1 (LGPLv2.1)`. 
