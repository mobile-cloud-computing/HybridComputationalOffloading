
Installation
============

Project is mavenized for automatic compilation and deployment.


```xml
$ git clone https://github.com/huberflores/ScalingMobileCodeOffloading.git
````

```xml
$ cd /Framework/Manager/CodeOffload/
````

```xml
$ mvn clean install
````

```xml
$ mvn org.apache.maven.plugins:maven-assembly-plugin:2.2-beta-2:assembly
````

Upload the front-end to the instance

```xml
$ scp -i /home/huber/.../HuberFlores/Ireland/pk-huber_flores.pem -r /home/huber/.../ScalingMobileCodeOffloading/Framework/Manager/CodeOffload/target/frontEnd-0.0.1-SNAPSHOT-jar-with-dependencies.jar ubuntu@ec2-xxx-xxx-xxx-xxx.eu-west-1.compute.amazonaws.com:/home/ubuntu/android-x86/
````

Connect to the instance

```xml
$ ssh -i /home/huber/.../HuberFlores/Ireland/pk-huber_flores.pem ubuntu@ec2-xxx-xxx-xxx-xxx.eu-west-1.compute.amazonaws.com
````

```xml
$ ubuntu@ec2$ nohup java -jar frontEnd-0.0.1-SNAPSHOT-jar-with-dependencies.jar &
````

