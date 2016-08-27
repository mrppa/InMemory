-- Role: InMemoryUser

-- DROP ROLE "InMemoryUser";

CREATE ROLE "InMemoryUser" LOGIN
  ENCRYPTED PASSWORD 'md5678289992a2448ad603b544919a060b3'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;

  
-- Database: "InMemory"

-- DROP DATABASE "InMemory";

CREATE DATABASE "InMemory"
  WITH OWNER = "InMemoryUser"
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'English_United Kingdom.1252'
       LC_CTYPE = 'English_United Kingdom.1252'
       CONNECTION LIMIT = -1;
GRANT ALL ON DATABASE "InMemory" TO "InMemoryUser";
GRANT ALL ON DATABASE "InMemory" TO public;



-- Schema: testschema1

-- DROP SCHEMA testschema1;

CREATE SCHEMA testschema1
  AUTHORIZATION "InMemoryUser";



  
  -- Table: testschema1.testproduct

-- DROP TABLE testschema1.testproduct;

CREATE TABLE testschema1.testproduct
(
  product_id character varying(50) NOT NULL,
  branch character varying(50) NOT NULL,
  product_name character varying(100),
  CONSTRAINT testproduct_pkey PRIMARY KEY (product_id, branch)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE testschema1.testproduct
  OWNER TO "InMemoryUser";

INSERT INTO testproduct VALUES ('P1', 'BRANCH1', 'Branch 1 Product 1');
INSERT INTO testproduct VALUES ('P2', 'BRANCH1', 'Branch 1 Product 2');
INSERT INTO testproduct VALUES ('P1', 'BRANCH2', 'Branch 2 Product 1');

  