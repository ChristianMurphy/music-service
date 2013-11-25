#!/bin/bash
#move to home folder
cd /home/$USER

##install java, ant and tomcat
sudo apt-get install openjdk-7-jdk
sudo apt-get install ant
sudo apt-get install tomcat6
sudo apt-get install vim

##download jaxws
wget http://repo.maven.apache.org/maven2/com/sun/xml/ws/jaxws-ri/2.2.8/jaxws-ri-2.2.8.zip
unzip jaxws-ri-2.2.8.zip

##set path variables
echo "export JAVA_HOME='/usr/lib/jvm/java-7-openjdk-amd64'"  >> /home/$USER/.bashrc
echo "export CATALINA_HOME='/usr/share/tomcat6'"  >> /home/$USER/.bashrc
echo "export JAXWS_HOME='~/jaxws-ri'"  >> /home/$USER/.bashrc
echo "export EDITOR='vim'" >> /home/$USER/.bashrc

##exit out of terminal
exit

