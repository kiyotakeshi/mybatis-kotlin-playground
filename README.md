# mybatis-kotlin-playground

- run container

```shell
docker compose up -d

docker compose exec mysql bash

mysql -u 'kotlin-sample' -D 'kotlin-sample' -p1qazxsw2
```

- insert sample data

```sql
-- USE `kotlin-sample`;
CREATE TABLE user
(
    id      int(10)     NOT NULL,
    name    varchar(16) NOT NULL,
    age     int(10)     NOT NULL,
    profile varchar(64) NOT NULL,
    primary key (id)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8;

INSERT INTO `kotlin-sample`.user (id, name, age, profile) VALUES (1, 'mike', 22, 'hello');
INSERT INTO `kotlin-sample`.user (id, name, age, profile) VALUES (2, 'popcorn', 33, 'goodbye');
select * from user;
```
