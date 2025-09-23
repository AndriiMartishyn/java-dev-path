CREATE SCHEMA IF NOT EXISTS product;

CREATE Table IF NOT EXISTS category
(
    id   BIGSERIAL PRIMARY KEY,
    name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS products
(
    id       BIGSERIAL,
    name     VARCHAR(255) NOT NULL,
    price    DECIMAL(10,2) NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);