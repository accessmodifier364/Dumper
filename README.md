<h2>Dump every class which is running in the JVM.</h2>

This tool can be used for dumping classes dynamically loaded
using some kind of ClassLoader. (Malware, Minecraft Clients)

### Usage:

**With a normal Java Application:**</br>
_java -javaagent:Dumper.jar -jar program.jar_</br>
![Screenshot_1](./screenshots/Screenshot_1.jpg) </br>

**With a Minecraft Client:**</br>
_add -javaagent:Dumper.jar to Launcher Arguments_</br>
![Screenshot_2](./screenshots/Screenshot_2.jpg) </br>

**After dumping you will have a folder in your desktop with the classes.**</br>
![Screenshot_3](./screenshots/Screenshot_3.jpg) </br>

**You can convert the folder with classes into a .jar file using the Dumper as normal java application.</br>**
![Screenshot_4](./screenshots/Screenshot_4.jpg) </br>

Before using: Some programs can have checks for this kind of dump, and you can be banned if is a Minecraft Client. A perfect example is: [Anti-Dump](https://github.com/zzurio/Anti-Dump), but this can be easily bypassed changing the value in strings using a tool as Recaf or IDA (if it uses native files for Anti-Dump check). 