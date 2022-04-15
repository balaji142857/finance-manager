FROM postgres:14.2-alpine3.15
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD password
ENV POSTGRES_DB finman-db
ADD db_setup.sql /docker-entrypoint-initdb.d/
