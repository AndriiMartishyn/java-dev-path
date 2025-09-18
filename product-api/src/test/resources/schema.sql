DROP TABLE IF EXISTS product;

CREATE TABLE IF NOT EXISTS product
(
    id       BIGINT AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    price    DECIMAL(10,2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);