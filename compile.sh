#!/bin/bash

cd ~/shopit
javac -cp $CATALINA_HOME/lib/javax.ws.rs-api-2.0.1.jar:$CATALINA_HOME/lib/gson-2.3.1.jar:$CATALINA_HOME/lib/java-jwt-3.3.0.jar:$CATALINA_HOME/lib/jackson-core-2.13.2.jar:$CATALINA_HOME/lib/apache-commons.jar:$CATALINA_HOME/lib/jersey-media-multipart-2.21.1.jar:. business/Service.java
rm WEB-INF/classes/business/*
cp business/*.class WEB-INF/classes/business/.
jar cvf Service.war WEB-INF META-INF
sh $CATALINA_HOME/bin/catalina.sh stop
rm $CATALINA_HOME/webapps/Service.war
rm -rf $CATALINA_HOME/webapps/Service
mv Service.war $CATALINA_HOME/webapps
if [ $1 = debug ];
then
    sh $CATALINA_HOME/bin/catalina.sh run
else
    sh $CATALINA_HOME/bin/catalina.sh start
fi