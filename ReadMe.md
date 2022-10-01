# Docker based installation

- Install [Docker](https://docs.docker.com/get-docker/) for your operating system
- Clone/Download the project from [github repo](https://github.com/balaji142857/finance-manager)
- From the root project directory execute the below commands to start/stop the app
    - start: ```docker-compose up --build```
    - stop: ```docker-compose stop```
- The start internally does the below for you
    - brings up the user interface component on [http://localhost:4200](http://localhost:4200)
    - starts the postgres database on port 5432
    - starts the backend component on port 80


##Development environment setup [WIP]

##building the images

###db
```
docker build -t balaji142857/finman-db:1.0 db/
```

###backend
``` 
docker build --build-arg JAR_FILE=build/libs/finance-manager-1.0-SNAPSHOT.jar -t balaji142857/finman:1.0 backend/
```


###web:
```
docker build -t balaji142857/finman-web:1.0 ui/
```


##Running the app
```
docker run -d --name=postgres-docker-db -p 5432:5432 balaji142857/finman-db:1.0
docker run -d -p 8080:8080 balaji142857/finman:1.0
docker run -d -p 4200:80 balaji142857/finman-web:1.0

docker exec -it postgres-docker-db psql -U postgres
```
