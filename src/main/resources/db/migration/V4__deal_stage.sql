-- Every deal has a stage. Deals that exist before this migration become open (spec 006 R1, AS-07).
ALTER TABLE deals
    ADD COLUMN stage varchar(10) NOT NULL DEFAULT 'open' CHECK (stage IN ('open', 'won', 'lost'));
