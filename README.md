# url-shortener

A URL shortener service, with internationalization, audit and two profiles: 
- dev (in-memory h2)
- prod (MySQL and Kafka for storing clicks data).

Use `docker compose up` to start a full-blown prod version.
Kafka containers are run with no volumes by default, to not pollute the storage.
MySQL is run with a volume assigned, so that data is persistent.