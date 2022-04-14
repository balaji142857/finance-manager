#Container creations
##building the images
###backend
``` 
docker build --build-arg JAR_FILE=build/libs/finance-manager-1.0-SNAPSHOT.jar -t balaji142857/finman:1.0 backend/
```
###db

###web:
```
docker build -t balaji142857/finman-web:1.0 ui/
```


##Running the app
```
docker run -d -p 8080:8080 balaji142857/finman:1.0
docker run -d -p 80:4200 balaji142857/finman-web
```