#!/bin/sh
# Installs the following jar files into your local maven
# repository.

mvn install:install-file -Dfile=hiero.jar -DgroupId=com.cokeandcode -DartifactId=hiero -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=ibxm.jar -DgroupId=com.cokeandcode -DartifactId=ibxm -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=jinput.jar -DgroupId=com.cokeandcode -DartifactId=jinput -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=jnlp.jar -DgroupId=com.cokeandcode -DartifactId=jnlp -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=jogg-0.0.7.jar -DgroupId=com.cokeandcode -DartifactId=jogg-0.0.7 -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=jorbis-0.0.15.jar -DgroupId=com.cokeandcode -DartifactId=jorbis-0.0.15 -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=lwjgl.jar -DgroupId=com.cokeandcode -DartifactId=lwjgl -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=natives-linux.jar -DgroupId=com.cokeandcode -DartifactId=natives-linux -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=natives-mac.jar -DgroupId=com.cokeandcode -DartifactId=natives-mac -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=natives-win32.jar -DgroupId=com.cokeandcode -DartifactId=natives-win32 -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=packulike.jar -DgroupId=com.cokeandcode -DartifactId=packulike -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=pedigree.jar -DgroupId=com.cokeandcode -DartifactId=pedigree -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=scalar.jar -DgroupId=com.cokeandcode -DartifactId=scalar -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=slick-util.jar -DgroupId=com.cokeandcode -DartifactId=slick-util -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=slick.jar -DgroupId=com.cokeandcode -DartifactId=slick -Dversion=1.1 -Dpackaging=jar
