CREATE TABLE product
(
    id       BIGSERIAL PRIMARY KEY ,
    name     VARCHAR(255) NOT NULL,
    price    DECIMAL(10,2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);