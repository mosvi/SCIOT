# Color Detection with Serverless Architecture
This project is about to detect the color of given object using Tcs3200 color detection sensor using a serverless Nuclio function to send and receive MQTT data messages in which ESP8266 is being used as a client and a java application as a consumer. For color detection, I’m detecting only 3 colors i.e. Red, Green and Blue as the sensor gives the color data in RGB values. The MQTT data messages is sending through ESP8266 from the sensor to the RabbitMQ queues. In the end, a java application is receiving the MQTT messages which are invoking by a nuclio function.

Color detection is very useful in different domain of industries, especially if we have a robust serveless arachitecture, so it can be easily monitored for identifying and counting the objects by using different color codes. It can be used in the following domain;

1) Textile industries
2) Vegatables and fruits storage companies
3) Courier services organization


<h2>Tools</h2>

<b>Arduino IDE</b> (https://www.arduino.cc/en/software)

<b>IntelliJ IDEA</b> (https://www.jetbrains.com/idea/)

<b>Ubuntu 20.04</b> (https://ubuntu.com/download)

<b>RabbitMQ</b> (https://rabbitmq.com/getstarted.html)

<b>Nuclio</b> (https://nuclio.io/docs/latest/setup/)

<b>NodeMCU ESP8266 Lolin V3</b> (https://components101.com/development-boards/nodemcu-esp8266-pinout-features-and-datasheet)

<b>Tcs3200</b> (https://components101.com/sensors/tcs3200-color-sensor-module)


## Configuration

The following instructions are the configurations for our project using Linux Ubuntu 20.04 LTS machine.

### Docker

Docker is used in the architecture to deploy the function in an application container, each function is a Docker container that is listening on a socket port and can be invoked by an HTTP request, or by other triggers.

Install Docker using the Docker CE installation [guide](https://docs.docker.com/install/linux/docker-ce/ubuntu/#extra-steps-for-aufs).

```sh
$ sudo apt-get update
$ sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
$ sudo apt-key fingerprint 0EBFCD88
$ sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
$ sudo apt-get update
$ sudo apt-get install docker-ce
```

**IMPORTANT FIX** Ubuntu 18.04 changed to use systemd-resolved to generate /etc/resolv.conf. Now by default it uses a local DNS cache 127.0.0.53. That will not work inside a container, so Docker will default to Google's 8.8.8.8 DNS server, which may break for people behind a firewall. Refers to the [Stackoverflow discussion](https://stackoverflow.com/questions/20430371/my-docker-container-has-no-internet).

```sh
sudo ln -sf /run/systemd/resolve/resolv.conf /etc/resolv.conf
```

----------------------------------------------------------------------------------------------------------------------------
### Docker Compose

Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application’s services.
Docker compose is the technology used by Nuclio to easily create, build and deploy Docker application containers (the functions in this case).

Install Docker Compose using the Docker Compose installation [guide](https://docs.docker.com/compose/install/#install-compose).

```sh
$ sudo curl -L "https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
$ sudo chmod +x /usr/local/bin/docker-compose
```

------------------------------------------------------------------------------------------------------------------------------
### Nuclio 

Nuclio (High-Performance Serverless event and data processing platform) is a new "serverless" project, derived from Iguazio's elastic data life-cycle management service for high-performance events and data processing. The simplest way to explore Nuclio is to run its graphical user interface (GUI) of the Nuclio dashboard.

Start [Nuclio](https://github.com/nuclio/nuclio) using a docker container.

```sh
$ docker run -p 8070:8070 -v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/tmp nuclio/dashboard:stable-amd64
```

Browse to http://localhost:8070, create a project, and add a function. When run outside of an orchestration platform (for example, Kubernetes or Swarm), the dashboard will simply deploy to the local Docker daemon.

----------------------------------------------------------------------------------------------------------------------------

### RabbitMQ 

RabbitMQ is lightweight and easy to deploy on premises and in the cloud. It supports multiple messaging protocols. RabbitMQ can be deployed in distributed and federated configurations to meet high-scale, high-availability requirements.

Start [RabbitMQ](https://www.rabbitmq.com) instance with MQTT enabled using docker.

```sh
$ docker run -p 9000:15672  -p 1883:1883 -p 5672:5672  cyrilix/rabbitmq-mqtt 
```

Browse to http://localhost:9000, and login using username: guest and password: guest, to access to the RabbitMQ managment, where is possible to visualize the message queues and the broker status.

----------------------------------------------------------------------------------------------------------------------------
Once the configuration of the project is completed; import [mqttconsume.yaml](https://github.com/mosvi/SCIOT/blob/main/mqttconsume.yaml) file in nulcio to create function and deploy it. After successfully deployed, connect sensor to ESP8266 and run [ColorDetection.ino](https://github.com/mosvi/SCIOT/blob/main/ColorDetection.ino) file in Arduino IDE to send detected color messages to RabbitMQ queues & import [Java Client](https://github.com/mosvi/SCIOT/tree/main/rabbitmq-java-client-mvn) in IntelliJ IDEA, build and run it to receive MQTT messages.
