#https://cadu.dev/creating-a-docker-image-with-database-preloaded/
#https://belowthemalt.com/2021/12/08/mount-volumes-to-persist-data-in-local-initialize-database-in-docker/
# dump build stage
FROM postgres:11-alpine
COPY db_setup.sql /docker-entrypoint-initdb.d/
RUN ["sed", "-i", "s/exec \"$@\"/echo \"skipping...\"/", "/usr/local/bin/docker-entrypoint.sh"]
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=finman-db
ENV PGDATA=/data
RUN ["/usr/local/bin/docker-entrypoint.sh", "postgres"]

# final build stage
FROM postgres:11-alpine
COPY --from=dumper /data $PGDATA