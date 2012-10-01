Dune II - The Maker
===================

Dune II The Maker is now written in Java using the Slick game library.


============
REQUIREMENTS
============
Developing dune2themaker is easier than ever. All the required software is
free. You need:
- a JDK (1.5, higher should work, but is not tested) set up correctly
- Maven 2.x installed

Make sure the following environmental variables are known:
- JAVA_HOME
- M2_HOME

Get java JDK from: http://java.sun.com/javase/downloads/index_jdk5.jsp
Get Maven 2 from : http://maven.apache.org/download.html


=================
TEST REQUIREMENTS
=================
You can test if your settings are correct, by opening a command window (CMD) and run:

java --version

should say something similiar as:
java version "1.5.0_16"
Java(TM) 2 Runtime Environment, Standard Edition (build 1.5.0_16-b02)
Java HotSpot(TM) Client VM (build 1.5.0_16-b02, mixed mode)


You should also be able to run:

mvn -version

should say something similiar as:
Apache Maven 2.2.1 (r801777; 2009-08-06 21:16:01+0200)
Java version: 1.5.0_16
Java home: d:\DEV\java\jdk\jre
Default locale: nl_NL, platform encoding: Cp1252
OS name: "windows vista" version: "6.1" arch: "x86" Family: "windows"


TODO: - set up in IntelliJ

===========================================
Setting up D2TM to develop in IntelliJ IDEA
===========================================
1. First, go to the install directory and run install.bat / install.sh
  - this should only be done once
2. Open IntelliJ
3. New Project from external model
4. Choose maven
5. Check - Search projects recursively
6. Check - Automatically add maven projects.
7. Set up your run configuration of the class "main" to
   use native libraries. Add VM flags:
   -Djava.library.path=engine/src/main/resources/natives

=====================================
Setting up D2TM to develop in Eclipse
=====================================
1. First, go to the install directory and run install.bat / install.sh
  - this should only be done once
2. Get back to the project root directory (where POM.XML is located).
3. Run
    mvn clean eclipse:eclipse
   
   There will be warnings that some libraries cannot be found. However, we have
   installed them in your local repository in step 1.
4. Start eclipse
5. Import project
6. Select dune2themaker project
7. Set up your run configuration of the class "main" to
   use native libraries. Add VM flags:
   -Djava.library.path=engine/src/main/resources/natives

=====================================
Linux - Ubuntu
=====================================
