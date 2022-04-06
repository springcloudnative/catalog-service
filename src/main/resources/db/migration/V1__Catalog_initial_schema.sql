CREATE TABLE IF NOT EXISTS books (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    author varchar(255) NOT NULL,
    isbn varchar(255) UNIQUE NOT NULL,
    price float8 NOT NULL,
    publisher varchar(255),
    title varchar(255) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_date timestamp NOT NULL,
    version integer NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;