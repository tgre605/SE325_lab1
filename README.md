SOFTENG 325 Lab 01 - Sockets & RMI
==========

Before you start
----------
The purpose of this lab is to reinforce week 1's lecture material and to give you some practical experience with developing simple distributed applications. In addition, it requires you to familiarise yourself with Maven, which we'll be using extensively throughout the first part of the course.

Begin by forking this repository, then cloning your fork onto your local machine. Once you've done this, you'll notice the file `folder-maker.jar`, in the same directory as this `README.md` file. This is a simple program that ensures all Maven project folder structures are correct - a necessity in this case because `git` does not preserve empty folder structures in its repositories.

Run the `folder-maker.jar` file by opening a terminal window and typing the following command:

```
java -jar folder-maker.jar se325
```

This will loop through all folders beginning with `se325` in the current directory, and ensure their directory structures are complete. This step should be done **BEFORE** importing the projects into your IDE.

Exercise One - Build the supplied Concert application
----------
A simple client/server application has been developed using Java's support for sockets and the TCP protocol. The server maintains a collection of concerts, and offers an interface with CRUD (Create / Retrieve / Update / Delete) functionality to clients. The application implements a simple request/reply protocol, allowing clients to send requests to the server, which processes the requests and generates replies.

The project is located in the `se325-lab-01-sockets` folder, and is a *multi-module* Maven project. It comprises 3 modules:

1. `se325-lab-01-sockets-common`. This contains classes that are shared by the client and server modules. Specifically it contains request and response message classes, class `Concert` to represent concerts, and a `Config` class that defines network properties.

2. `se325-lab-01-sockets-client`. The client module contains a single JUnit class that implements several test cases to test the server's functionality.

3. `se325-lab-01-sockets-server`. This module contains class `Server` that implements the server program.

Multi-module projects are structured using a *parent* project (`se325-lab-01-sockets`) that contains modules. The parent project has a POM file that declares the modules. Each module also has its own POM file that inherits declarations, e.g. plugin declarations, from the parent POM.

#### (a) Run `folder-maker.jar`
If you haven't already done so, follow the steps in the *Before you start* section above to run this simple program which will ensure all necessary directories are in place.

#### (b) Import the project into your IDE
Maven's POM file contains all information necessary to structure a project and identify its dependencies. Many modern IDEs such as IntelliJ and Eclipse allow developers to easily import Maven projects. This allows individual developers to use their IDE of choice. Furthermore, in combination with a properly specified `.gitignore` file (which you will find in this repository), developers need not commit IDE-specific configuration files to their repositories - the `pom.xml` files are sufficient.

Instructions for importing Maven projects into both Eclipse and IntelliJ are given here. Feel free to use whichever IDE you prefer for this course.

###### IntelliJ:
Select `File -> Open` from the menu, or simply `Open` from the welcome screen. From there, browse to the main project folder (the one with the parent `pom.xml` file - in this case, `se325-lab-01-sockets`), and locate and select the `pom.xml` file within that folder to open, as shown in the below screenshot.

![](./spec/ex01-screenshot-open-intellij.png)

IntelliJ may prompt you, asking whether to open the file `as a project` or `as a file`. Choose `as a project`. If you have already opened the project in IntelliJ in the past, you may also be prompted asking whether to open the existing project or create a new project. In this case, choose `open existing project`.

IntelliJ will take care of loading the provided Maven project, and any of its child projects, as IntelliJ projects / modules as appropriate. Standard IntelliJ files such as `.idea` and `*.iml` will be created - these need not be committed to your repository and thus are included in the `.gitignore`.

###### Eclipse:
Within Eclipse, choose `File -> Import -> Existing Maven Projects` as shown in the below screenshot.

![](./spec/ex01-screenshot-import-eclipse-01.png)

As the *root directory*, select the folder containing the parent `pom.xml` file (in this case, the `se325-lab-01-sockets` folder). You should see the POM, along with its three child POMs, as shown in the screenshot below. Click `Finish` and all these projects will be imported into your Eclipse workspace.

![](./spec/ex01-screenshot-import-eclipse-02.png)

**Note:** Importing a project does not make a copy of the project in your Eclipse workspace directory - the actual source code, `pom.xml` files and other artifacts will remain in the directory in which you cloned your repository. This is good, and expected. It is typical that a workspace directory contains only Eclipse metadata.

#### (c) Build the project
Using Maven, build the complete project. The easiest way to do this is to run a Maven goal, e.g. `package`, on the parent project. The `package` goal will build the `common`, `client` and `server` modules and place the resulting JAR files in your local Maven repository. You can do this in your IDE as follows:

###### IntelliJ:
On the (by default) righthand side of the main IDE window, you'll find a `Maven` tab. Expand the parent project in this tab (i.e. `lab-01-sockets-parent`), and run the `package` goal you see there by double-clicking, or by selecting it and clicking the "play" button, or by right-clicking and selecting `run Maven build`.

###### Eclipse:
Within the Eclipse workspace, right-click the parent `pom.xml` file and select `Run as -> Maven build...`. In the window that pops up, type `package` in the `Goals` text field. Then click `Run`.

**Note:** This simple project introduced in Exercise One doesn't actually require you to build it using Maven goals - you'll be able to run both the client and server directly from within your IDE. However, building using Maven will become more important when we want to start running *integration tests* - more on this in lab two!

#### (d) Run the application
Assuming you've successfully run the Maven `package` goal as above, you'll be able to see a `target` folder within each of the `client`, `common`, and `server` modules. These folders contain the compliler output, and other generated artifacts such as JAR files.

If you like, you can run the server outside of your IDE using the JAR that was generated during the Maven build process. Using any terminal, navigate to the `se325-lab-01-sockets-server/target` directory, and run the following command:

```
java -jar lab-01-sockets-server-1.0-jar-with-dependencies.jar
```

The server will run, and will print the local IP address and port on which the server is listening. By default, this is port 8080 - you may change this by editing the `Config` class in the `common` module if this port doesn't work on your machine. If you do make a change, be sure to re-run the Maven `package` goal.

You may also run the `Server` as a standard Java application from either Eclips or IntelliJ in this case.

Once the server is running, you may then run the client from within your IDE, as a JUnit test. This is done by locating the `Client` class in the `client` module under `src/test/java`, right-clicking it and selecting `Run 'Client'` (IntelliJ) or `Run as -> JUnit test` (Eclipse). All tests should pass.

#### (e) Study the application
Study the application source code to understand how the application works. In the space provided below, critique the application and address the following questions. You may also wish to write your answers in your journal to use as a study aid / restricted-book test material.

- What would be involved in developing a similar application whose server maintained a collection of some other type of information, e.g. movies instead of concerts?

```
It would be very similar since functionality is basically the exact same with different names or fields.
```

- How efficient is the server? *Hint:* How does the server cope with multiple clients?

```
This server can do multiple clients but once there are many, it may begin to get negatively impacted. 
```

- What enhancements do you think should be made to the server?

```
Multiple threads that process the data could be used so that it doesn't have to be one at a time. Many requests could be processed at once.
```


Exercise Two - Build the supplied Whiteboard application
----------
The shared whiteboard application, discussed in lectures, is given in the `se325-lab-01-whiteboard` directory in this repository. The project follows the same structure as the sockets project you've built in exercise one. Import the project into your IDE and run both the server and the client as you did in the previous exercise.

The shared whiteboard application is useful to study when reviewing the lecture material and when working through exercise three below.


Exercise Three - Redevelop the Concert application using Java RMI
----------
In this exercise, you'll redevelop the Concert application from exercise one, using Java RMI as opposed to raw TCP sockets.

#### (a) Develop the Java RMI application
A skeleton project is given in the `se325-lab-01-rmi-concert` directory in this repository. Import the project into your IDE. The project structure mirrors that of the structure of the projects for exercises one and two.

The `common` module defines Concert service's remote interface. Study the Javadoc comments for `ConcertService` and flesh out the remainder of the project as necessary to implement a JUnit client and a server.

#### (b) Reflect on the Java RMI application
Consider the impact of using Java RMI (instead of working directly with sockets) on your development. Record your thoughts here, and in your journal.

```
```

You might have noticed that in the shared whiteboard application, `ShapeServant` and `ShapeFactoryServant` instances are threadsafe - since all methods are `synchronized`. Recall that where all methods of a class are synchronized, only a single thread can be executing any method on the object at any one time. Answer the following questions:

- Why have `Shape` and `ShapeFactory` been made threadsafe?

```
This is so that none of the data can be accessed by multiple requests at the same time, allowing it to maintain integrity, ie. not get corrupted.
```

- Does your `Concert` service need to be threadsafe?

```
Yes since our list of concerts might be accessed by multiple clients and we dont want to run methods that may encounter errors if another user does something to the data. 
```

- Should a RMI server use multiple threads when handling remote invocations?

```
No as you may run the same remote invocation at the same time, which may lead to an error.
```


Assessment and submission
----------
Run Maven's `clean` goal each of the parent projects to clear all generated code. Zip up the projects for the three tasks, and this `README.md` file, into a single Zip folder (but do **not** include the `.git` folder!), and submit the Zip to Canvas on or before the Lab 01 due date listed on the Canvas Assessments page.

Participating in this lab is worth 1% of your SOFTENG 325 mark, and will provide you with fantastic preparation for the part one test, assignment, and exam.

Resources
----------
The following resource on Maven might be of interest to you:

- http://books.sonatype.com/mvnex-book/reference/index.html
