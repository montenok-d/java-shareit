create TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description varchar(255) NOT NULL,
    requestor_id INTEGER REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

create TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id BIGINT REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    request_id BIGINT REFERENCES requests (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    item_id INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE,
    booker_id INTEGER REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    status varchar(10) NOT NULL
);

create TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    item_id INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE,
    author_id INTEGER REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    created TIMESTAMP
);


