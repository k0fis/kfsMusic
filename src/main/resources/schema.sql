CREATE TABLE IF NOT EXISTS artist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS  album (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    year INTEGER,
    artist_id INTEGER NOT NULL,
    FOREIGN KEY(artist_id) REFERENCES artist(id)
);

CREATE TABLE IF NOT EXISTS  track (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    path TEXT NOT NULL UNIQUE,
    note TEXT,
    track_no INTEGER,
    genre TEXT,
    duration INTEGER,
    album_id INTEGER NOT NULL,
    FOREIGN KEY(album_id) REFERENCES album(id)
);


-- =========================
-- FTS5 TABLE
-- =========================

CREATE VIRTUAL TABLE IF NOT EXISTS track_fts USING fts5(
  title,
  artist,
  album,
  genre,
  note,
  content='track',
  content_rowid='id'
);

-- =========================
-- CLEANUP (for re-run)
-- =========================

DROP TRIGGER IF EXISTS track_ai;
DROP TRIGGER IF EXISTS track_au;
DROP TRIGGER IF EXISTS track_ad;

-- =========================
-- AFTER DELETE TRIGGER
-- =========================

CREATE TRIGGER track_ad
AFTER DELETE ON track
BEGIN
  DELETE FROM track_fts WHERE rowid = OLD.id;
END;


-- =========================
-- INSERT TRIGGER
-- =========================
CREATE TRIGGER track_ai
AFTER INSERT ON track
BEGIN
  INSERT INTO track_fts(rowid, title, artist, album, genre, note)
  VALUES (
    NEW.id,
    NEW.title,
    (SELECT name FROM artist WHERE id = (SELECT artist_id FROM album WHERE id = NEW.album_id)),
    (SELECT title FROM album WHERE id = NEW.album_id),
    NEW.genre,
    NEW.note
  );
END;

-- =========================
-- AFTER UPDATE TRIGGER
-- =========================

CREATE TRIGGER track_au
AFTER UPDATE ON track
BEGIN
  DELETE FROM track_fts WHERE rowid = OLD.id;

  INSERT INTO track_fts(rowid, title, artist, album, genre, note)
  VALUES (
    NEW.id,
    NEW.title,
    (SELECT name FROM artist WHERE id = (SELECT artist_id FROM album WHERE id = NEW.album_id)),
    (SELECT title FROM album WHERE id = NEW.album_id),
    NEW.genre,
    NEW.note
  );
END;

