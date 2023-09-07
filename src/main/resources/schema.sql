DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;

CREATE TABLE IF NOT EXISTS users
(
    id
    integer
    generated
    by
    default as
    identity
    primary
    key,
    name
    varchar
(
    50
) not null,
    email varchar
(
    50
) not null,
    constraint user_email unique
(
    email
)
    );
CREATE TABLE IF NOT EXISTS items
(
    id
    integer
    generated
    by
    default as
    identity
    primary
    key,
    name
    varchar
(
    50
) not null,
    description varchar
(
    200
) not null,
    is_available boolean default true not null,
    owner_id integer references users
(
    id
) on delete cascade
    );
CREATE TABLE IF NOT EXISTS bookings
(
    id
    integer
    generated
    by
    default as
    identity
    primary
    key,
    rental_start
    timestamp
    without
    time
    zone
    not
    null,
    rental_end
    timestamp
    without
    time
    zone
    not
    null,
    booker_id
    integer
    references
    users
(
    id
) not null,
    item_id integer references items
(
    id
) on delete cascade not null,
    status integer
    );
CREATE TABLE IF NOT EXISTS comments (
                                        id
                                            integer
                                            generated
                                                by
                                                default as
                                                identity
                                            primary
                                                key,
    item_id integer references items (id) on delete cascade,
    author_id integer references users (id) on delete cascade,
    text varchar(500) not null,
    creation_date timestamp default now()
);

