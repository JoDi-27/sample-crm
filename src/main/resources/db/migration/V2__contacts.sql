CREATE TABLE contacts (
    id         bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name varchar(100) NOT NULL,
    last_name  varchar(100) NOT NULL,
    email      varchar(254) NOT NULL,
    phone      varchar(50),
    company_id bigint REFERENCES companies (id) ON DELETE SET NULL
);

CREATE INDEX contacts_company_id_idx ON contacts (company_id);
