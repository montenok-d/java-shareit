create TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_EMAIL UNIQUE (email)
);

create TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    description VARCHAR(255) NOT NULL,
    requestor_id BIGINT REFERENCES users (id) ON delete CASCADE ON update CASCADE
);

create TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id BIGINT REFERENCES users (id) ON delete CASCADE ON update CASCADE,
    request_id BIGINT REFERENCES requests (id) ON delete CASCADE ON update CASCADE
);

create TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    item_id BIGINT REFERENCES items (id) ON delete CASCADE ON update CASCADE,
    booker_id BIGINT REFERENCES users (id) ON delete CASCADE ON update CASCADE,
    status varchar(10) NOT NULL
);

create TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    item_id BIGINT REFERENCES items (id) ON delete CASCADE ON update CASCADE,
    author_id BIGINT REFERENCES users (id) ON delete CASCADE ON update CASCADE,
    created TIMESTAMP
);

