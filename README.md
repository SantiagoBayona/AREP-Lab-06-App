# TALLER DE PATRONES ARQUITECTURALES

Aplicación web que recibe, almacena y muestra mensajes ingresados por el usuario, desplegada en una instancia ec2 de AWS usando docker.

## Arquitectura

### AppLB - RoundRobin
La aplicación está compuesta por un cliente web y un servicio REST. El cliente web tiene un campo y un botón y cada vez que el usuario envía un mensaje, este se lo envía al servicio REST y actualiza la pantalla con la información que este le regresa en formato JSON. El servicio REST recibe la cadena e implementa un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio LogService.

### LogService
LogService es un servicio REST que recibe una cadena, la almacena en la base de datos y responde en un objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en que fueron almacenadas.

### MongoDB
El servicio MongoDB es una instancia de MongoDB corriendo en un container de docker en una máquina virtual de EC2 en la que se guardan los mensajes enviados por el usuario

![image](https://github.com/SantiagoBayona/AREP-Lab-06-App/assets/64861204/634d844e-5e7f-4571-a955-17ea72ac033c)

## Prerrequisitos
- Java
- Maven
- Docker

## Instalación

1. Clonar los repositorios que componen la aplicación
   
```
git clone https://github.com/SantiagoBayona/AREP-Lab-06-App.git
```

```
git clone https://github.com/SantiagoBayona/AREP-Lab-06-Service.git
```

2. En la raíz de ambos proyectos ejecutar el comando

```
mvn clean install
```

3. Construir las imágenes de docker

En la raíz de cada proyecto se construye la imagen de docker correspondiente con el siguiente comando

- Para LogService
```
docker build --tag logger-service . -f DockerfileService
```

- Para App
```
docker build --tag logger-app . -f DockerfileApp
```

## Ejecución

### Localmente
Para ejecutar el proyecto debemos correr las tres instancias del servicio, el webapp y la instancia de MongoDB, para esto usamos los siguientes comandos

```
docker network create my_network
```
```
docker run -d -p 36000:4567 --name logger-app --network my_network logger-app
```
```
docker run -d -p 36001:4568 --name logger-service1 --network my_network logger-service
```
```
docker run -d -p 36002:4568 --name logger-service2 --network my_network logger-service
```
```
docker run -d -p 36003:4568 --name logger-service3 --network my_network logger-service
```
```
docker run -d -p 27017:27017 -v mongodb:/data/db -v mongodb_config:/data/configdb --name db --network my_network mongo:3.6.1 mongod
```

Hecho esto, al acceder al siguiente enlace podemos ver y usar el servicio en ejecución

```
http://localhost:36000/index.html
```

![img](https://github.com/SantiagoBayona/AREP-Lab-06-App/assets/64861204/7f97adc7-8dd2-4532-b498-c2d075795aca)

### Usando la instancia ec2 de AWS

Después de haber configurado los grupos de seguridad de la instancia para permitir el tráfico por los puertos que usaremos, ingresamos a la instancia mediante ssh y corremos los comandos del punto anterior para correr las instancias de los recursos que usaremos. Una vez hecho esto podemos acceder al servicio mediante la url específica de la instancia, indicando el puerto y el recurso solicitado.

```
http://ec2-54-152-7-103.compute-1.amazonaws.com:36000/index.html
```

![img1](https://github.com/SantiagoBayona/AREP-Lab-06-App/assets/64861204/7f95d57e-9d06-4001-8dee-5d6e324105e8)

## Vídeo de la aplicación desplegada



## Construido con 
- Java
- Maven
- Docker
- JS
- HTML & CSS
