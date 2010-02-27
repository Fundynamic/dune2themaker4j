Dune II - The Maker
===================

Dune II The Maker is now written in Java using the Slick game library.


Setting up D2TM to develop in Eclipse:
--------------------------------------
1. First, go to the install directory and run install.bat
  - this should only be done once
2. Get back to the project root directory (where POM.XML is located).
3. Run
    mvn clean eclipse:eclipse
   
   There will be warnings that some libraries cannot be found. However, we have
   installed them in your local repository in step 1.
4. Start eclipse
5. Import project
6. Select dune2themaker project

