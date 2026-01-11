
CREATE TABLE IF NOT EXISTS artist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);

--- kfsSplit ---

CREATE TABLE IF NOT EXISTS  album (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    year INTEGER,
    artist_id INTEGER NOT NULL,
    FOREIGN KEY(artist_id) REFERENCES artist(id)
);

--- kfsSplit ---

CREATE TABLE IF NOT EXISTS  track (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    path TEXT NOT NULL UNIQUE,
    note TEXT,
    track_no INTEGER,
    genre TEXT,
    comment TEXT,
    duration INTEGER,
    album_id INTEGER NOT NULL,
    FOREIGN KEY(album_id) REFERENCES album(id)
);

--- kfsSplit ---

CREATE TABLE IF NOT EXISTS tag (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    track_id INTEGER NOT NULL,
    name TEXT,
    value TEXT,
    FOREIGN KEY(track_id) REFERENCES track(id)
);

--- kfsSplit ---

CREATE VIRTUAL TABLE IF NOT EXISTS track_fts USING fts5(
    title, artist, album, genre, note, comment, tags,
    tokenize = 'unicode61 remove_diacritics 2'
);

--- kfsSplit ---

DROP TRIGGER IF EXISTS track_ai;

--- kfsSplit ---

DROP TRIGGER IF EXISTS track_au;

--- kfsSplit ---

DROP TRIGGER IF EXISTS track_ad;

--- kfsSplit ---

CREATE TRIGGER track_ad AFTER DELETE ON track
BEGIN
  DELETE FROM track_fts WHERE rowid = OLD.id;
END;

--- kfsSplit ---

CREATE TRIGGER track_ai AFTER INSERT ON track
BEGIN
  INSERT INTO track_fts(rowid, title, artist, album, genre, note, comment, tags)
  VALUES (
    NEW.id,
    NEW.title,
    (SELECT name FROM artist WHERE id = (SELECT artist_id FROM album WHERE id = NEW.album_id)),
    (SELECT title FROM album WHERE id = NEW.album_id),
    NEW.genre,
    NEW.note,
    NEW.comment,
    (SELECT trim(GROUP_CONCAT(tg.value || ' ')) FROM tag tg WHERE tg.track_id = NEW.id)
  );
END;

--- kfsSplit ---

CREATE TRIGGER track_au AFTER UPDATE ON track
BEGIN
  DELETE FROM track_fts WHERE rowid = OLD.id;

  INSERT INTO track_fts(rowid, title, artist, album, genre, note,comment, tags)
  VALUES (
    NEW.id,
    NEW.title,
    (SELECT name FROM artist WHERE id = (SELECT artist_id FROM album WHERE id = NEW.album_id)),
    (SELECT title FROM album WHERE id = NEW.album_id),
    NEW.genre,
    NEW.note,
    NEW.comment,
    (SELECT trim(GROUP_CONCAT(tg.value || ' ')) FROM tag tg WHERE tg.track_id = NEW.id)
  );
END;

