
CREATE TABLE cart
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id    BIGINT                                  NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE cart_item
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    cart_id    BIGINT                                  NOT NULL,
    product_id BIGINT                                  NOT NULL,
    quantity   INTEGER                                 NOT NULL,
    CONSTRAINT pk_cart_item PRIMARY KEY (id)
);

CREATE TABLE category
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name       VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    orders_id   BIGINT                                  NOT NULL,
    products_id BIGINT                                  NOT NULL,
    quantity    INTEGER                                 NOT NULL,
    price       DOUBLE PRECISION,
    CONSTRAINT pk_order_item PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    users_id    BIGINT                                  NOT NULL,
    status      VARCHAR(255),
    total_price DOUBLE PRECISION,
    date        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE product
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name          VARCHAR(255)                            NOT NULL,
    description   VARCHAR(255)                            NOT NULL,
    price         DOUBLE PRECISION                        NOT NULL,
    quantity      INTEGER                                 NOT NULL,
    user_id       BIGINT,
    is_active     BOOLEAN                                 NOT NULL,
    categories_id BIGINT,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username   VARCHAR(255)                            NOT NULL,
    first_name VARCHAR(255)                            NOT NULL,
    last_name  VARCHAR(255)                            NOT NULL,
    email      VARCHAR(255)                            NOT NULL,
    password   VARCHAR(255)                            NOT NULL,
    is_active  BOOLEAN                                 NOT NULL,
    role       VARCHAR(255)                            NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CART_ITEM_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CART_ITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_ORDERS FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_PRODUCTS FOREIGN KEY (products_id) REFERENCES product (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORIES FOREIGN KEY (categories_id) REFERENCES category (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);