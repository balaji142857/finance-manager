#Container creations

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