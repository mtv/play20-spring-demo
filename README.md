Using Spring from within a Play 2.0 application
===============================================

This is a simple application demonstrating how to integrate a Play 2.0 application components with <a href="http://www.springsource.org/">Spring framework</a>.

> Note that the same technique can be applied to any other dependency injection framework.

## How does it work?

There is a few places where the Spring _binding_ is done.

### First, add the spring dependency

In the `project/Build.scala` file, add a dependency to `spring-context`:

```scala
import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play-spring"
    val appVersion      = "1.0.0-SNAPSHOT" // Semantic Versioning - http://semver.org/

  val appDependencies = Seq(
    javaCore,

    // Spring dependency
    "org.springframework" % "spring-context" % "3.2.1.RELEASE"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
```

### Using controllers instances

We wiil use _dynamic_ controller dispatching instead of the _statically compiled_ dispatching used by default in Play framework. To do that, just prefix your controller class name with the `@` symbol in the `routes` file:

```
GET    /       @controllers.Application.index()
```

### Managing controllers instances

The controllers instances management will be delegated to the `Global` object of your application. Here is an implementation of the `Global` using Spring framework:

```java
import play.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.StandardEnvironment;

public class Global extends GlobalSettings {

  private AnnotationConfigApplicationContext ctx;

  @Override
  public void onStart(Application app) {
    ctx = new AnnotationConfigApplicationContext();
    ctx.setEnvironment(new StandardEnvironment());
    ctx.register(GlobalContext.class);
    ctx.refresh();
  }

  @Override
  public <T> T getControllerInstance(Class<T> clazz) {
    return ctx.getBean(clazz);
  }

}
```

And here is the associated `GlobalContext` object we are using to configure Spring:

```java
import services.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "controllers" })
public class GlobalContext {
  
  @Bean
  public HelloService helloService() {
    return new HelloService();
  }
}
```

Here we are using 2 different ways of finding Spring Beans. The first (like in the old) version we are using component scanning of packages. The second way, we are actually annotating the method that returns the Spring Bean.

### Creating Spring components

In this example we are using the annotation driven binding for Spring (so we use the annotations provided in `org.springframework.stereotype` to mark our components as _Spring managed_).

First a simple _"Spring style"_ service:

```java
package services;

@org.springframework.stereotype.Service
public class HelloService {

	public String hello() {
		return "Hello world!";
	}

}
```

And now the controller with the service _autowired_:

```java
package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import services.*;

import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Controller
public class Application extends Controller {

	@Autowired
	private HelloService helloService;
  
  	public Result index() {
    	return ok(index.render(helloService.hello()));
  	}
  
}
```

## To go further

You can then integrate any Spring componenents to your application. The auto-reload feature will just work magically. If you plan to use a database or an ORM, you could want to manage it directly via Spring instead of using the default Play plugins.