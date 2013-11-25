#!/bin/bash
#move to home folder
cd /home/$USER

##uninstall everything
#remove open jdk 6
sudo apt-get remove openjdk-6-jdk
sudo apt-get remove openjdk-6-jre
sudo apt-get remove openjdk-6-java

#remove open jdk 7
sudo apt-get remove openjdk-7-jdk
sudo apt-get remove openjdk-7-jre
sudo apt-get remove openjdk-7-java

#remove ant and tomcat
sudo apt-get remove ant
sudo apt-get remove tomcat7
sudo apt-get remove tomcat6

#remove emacs
sudo apt-get remove emacs
sudo apt-get remove vim

#remove everything that is left over
sudo apt-get autoremove

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

