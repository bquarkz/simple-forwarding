CREATE SCHEMA dbo;

CREATE TABLE dbo.ENTRY
(
    entry_id SERIAL PRIMARY KEY,
    fake_address VARCHAR( 512 ) NOT NULL,
    real_address VARCHAR( 512 ) NOT NULL
);

CREATE TABLE dbo.STATISTICS
(
    statistics_id SERIAL PRIMARY KEY,
    statistics_entry_id INTEGER REFERENCES dbo.ENTRY( entry_id ),
    path VARCHAR( 512 ) NOT NULL,
    hits INTEGER NOT NULL default 0
);