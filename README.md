# bazahr-poc

POC dla aplikacji BaZaHR, pełniącej rolę QueryModelu w projektowanym przez nasz zespół systemie.

## Setup bazy danych

```
docker run --name postgres -e POSTGRES_PASSWORD=<secret> -e POSTGRES_DB=bazahrdb -d -p 5432:5432 postgres
```