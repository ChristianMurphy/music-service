Music Service
=============

a Java server and Objective C client able to upload and download songs.

###Java Web Service Server
Uses JAX-WS to create a library for java client

```
ant server
```

Then deploy to tomcat


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
ant client
ant execute.client
```


###Objective C Client
Build using Gnu Step (on linux)
Connects exclsively to the upload download server

```
gnustepstart
make
```
