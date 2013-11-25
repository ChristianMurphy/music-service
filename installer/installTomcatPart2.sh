#!/bin/bash
#create aliases to start and stop tomcat
echo "alias starttomcat='sudo env JAVA_HOME=$JAVA_HOME $CATALINA_HOME/bin/startup.sh'"  >> /home/$USER/.bashrc
echo "alias stoptomcat='sudo env JAVA_HOME=$JAVA_HOME $CATALINA_HOME/bin/shutdown.sh'"  >> /home/$USER/.bashrc

#make the directories you are going to need
sudo mkdir $JAVA_HOME/lib/endorsed
sudo mkdir $CATALINA_HOME/endorsed
sudo mkdir /usr/share/tomcat6/logs

#endorse JAX WS into Java Home
sudo cp -p /home/$USER/jaxws-ri/lib/jaxws-api.jar $JAVA_HOME/lib/endorsed/
sudo cp -p /home/$USER/jaxws-ri/lib/jaxb-api.jar $JAVA_HOME/lib/endorsed/
sudo cp -p /home/$USER/jaxws-ri/lib/javax.annotation-api.jar $JAVA_HOME/lib/endorsed/
sudo cp -p /home/$USER/jaxws-ri/lib/jsr181-api.jar $JAVA_HOME/lib/endorsed/

#endorse JAX WS into Tomcat 
sudo cp -p /home/$USER/jaxws-ri/lib/jaxws-api.jar $CATALINA_HOME/endorsed/
sudo cp -p /home/$USER/jaxws-ri/lib/jaxb-api.jar $CATALINA_HOME/endorsed/
sudo cp -p /home/$USER/jaxws-ri/lib/javax.annotation-api.jar $CATALINA_HOME/endorsed/
sudo cp -p /home/$USER/jaxws-ri/lib/jsr181-api.jar $CATALINA_HOME/endorsed/
sudo cp -p /home/$USER/jaxws-ri/lib/gmbal-api-only.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/ha-api.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/jaxb-core.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/jaxb-impl.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/jaxws-api.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/jaxws-rt.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/management-api.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/policy.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/stax-ex.jar $CATALINA_HOME/lib/
sudo cp -p /home/$USER/jaxws-ri/lib/streambuffer.jar $CATALINA_HOME/lib/

#Simlink this stuff
sudo ln -s /var/lib/tomcat6/webapps /wr/share/tomcat6/webapps
sudo ln -s /etc/tomcat6 /usr/share/tomcat6/conf

#start stuff
./usr/share/tomcat6/bin/startup.sh
