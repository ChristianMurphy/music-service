Music Service
=============
a Java server, Java client and Objective C client able to upload and download songs.


###Setup
installer scripts for linux distros using apt package manager are included in /installer folder

for Java, Ant and Tomcat

```
sh installTomcat.sh
```

restart terminal

```
sh installTomcatPart2.sh
```

for GnuStep

```
sh installGnustep.sh
```

###Java Web Service Server
Uses JAX-WS to create a library for java client

```
ant server
```

Then deploy to tomcat. Tomcat is hosting to ```localhost:8080```


###Java Upload Download Server
Uses a socket on port 3030 to server content to clients.

Build using ant

```
ant build.ud.server
ant execute.ud.server
```

###Java Client
Swing interface for interacting with Web Service and upload Download servers

```
ant client -DWSHost=127.0.0.1
ant execute.client -DWSHost=127.0.0.1
```


###Objective C Client
Build using Gnu Step (on linux)
Connects exclusively to the upload download server

```
gnustepstart
make
```
###Credits
This project is based off Assignment 4 for Distributed Stystems (CST 420) with Professor Tim Lindquist.
All code which is base off his samples has his name in the ```@author``` tag.
