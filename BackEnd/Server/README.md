
Installation
============

Project is mavenized for automatic compilation and deployment.


```xml
$ git clone https://github.com/mobile-cloud-computing/HybridComputationalOffloading.git
````

```xml
$ cd /BackEnd/Server/
````

```xml
$ mvn clean install
````


Upload the front-end to the instance

```xml
$ scp -i /home/huber/.../HuberFlores/Ireland/pk-huber_flores.pem -r /home/huber/.../HybridComputationalOffloading/BackEnd/Server/target/frontEnd-0.0.1-SNAPSHOT-jar-with-dependencies.jar ubuntu@ec2-xxx-xxx-xxx-xxx.eu-west-1.compute.amazonaws.com:/home/ubuntu/android-x86/
````

Connect to the instance

```xml
$ ssh -i /home/huber/.../HuberFlores/Ireland/pk-huber_flores.pem ubuntu@ec2-xxx-xxx-xxx-xxx.eu-west-1.compute.amazonaws.com
````

```xml
$ ubuntu@ec2$ nohup java -jar frontEnd-0.0.1-SNAPSHOT-jar-with-dependencies.jar &
````

