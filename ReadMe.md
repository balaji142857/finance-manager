#Container creations

##backend

##db

##web:
```
docker build -t balaji142857/finman-web:1.0 ui
docker run -d -p 80:4200 balaji142857/finman-web
```