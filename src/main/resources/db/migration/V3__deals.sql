CREATE TABLE deals (
    id              bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title           varchar(200)   NOT NULL,
    amount          numeric(15, 2) NOT NULL CHECK (amount >= 0),
    -- Required when a deal is created, but cleared when its company is deleted.
    company_id      bigint REFERENCES companies (id) ON DELETE SET NULL,
    main_contact_id bigint REFERENCES contacts (id) ON DELETE SET NULL
);

CREATE INDEX deals_company_id_idx ON deals (company_id);
CREATE INDEX deals_main_contact_id_idx ON deals (main_contact_id);
