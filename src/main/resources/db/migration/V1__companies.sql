CREATE TABLE companies (
    id      bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    varchar(200) NOT NULL,
    website varchar(500)
);
