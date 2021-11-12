#Dump every class which in running in the JVM.

This tool can be used for dumping classes dynamically loaded
using some kind of ClassLoader. (Malware, Minecraft Clients)

##Usage:

**With a normal Java Application use:**</br>
_java -javaagent:Dumper.jar -jar program.jar_</br>
![Screenshot_1](./screenshots/Screenshot_1.jpg) </br>

**With a Minecraft Client use:**</br>
_add -javaagent:Dumper.jar to Launcher Arguments_</br>
![Screenshot_2](./screenshots/Screenshot_2.jpg) </br>

**After dumping you will have a folder with the classes.**
![Screenshot_3](./screenshots/Screenshot_3.jpg) </br>

**You can convert the folder with classes into a .jar file using the Dumper as normal java application.</br>**
![Screenshot_4](./screenshots/Screenshot_4.jpg) </br>