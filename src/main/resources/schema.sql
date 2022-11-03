CREATE TABLE IF NOT EXISTS filmorate_users (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(100) NOT NULL,
    login varchar(100) NOT NULL,
    name varchar(100) NOT NULL,
    birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS user_friend (
    user_id integer REFERENCES filmorate_users (id) ON DELETE CASCADE,
    friend_id integer REFERENCES filmorate_users (id) ON DELETE CASCADE,
    is_friend boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS rating (
    id integer PRIMARY KEY,
    name varchar(10) NOT NULL,
    description varchar(255)
);

CREATE TABLE IF NOT EXISTS filmorate_films (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    description varchar(255),
    release_date date NOT NULL,
    duration integer NOT NULL,
    rating_id integer REFERENCES rating (id)
);

CREATE TABLE IF NOT EXISTS genre (
    id integer PRIMARY KEY,
    name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id integer REFERENCES filmorate_films (id) ON DELETE CASCADE,
    genre_id integer REFERENCES genre (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_like (
    film_id integer REFERENCES filmorate_films (id) ON DELETE CASCADE,
    user_id integer REFERENCES filmorate_users (id) ON DELETE CASCADE
);