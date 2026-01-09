# FTS Query Normalization (SQLite FTS5)

Tento dokument popisuje **normalizaci uživatelských dotazů** pro vyhledávání nad **SQLite FTS5**.

Cílem je:

* zabránit chybám `MATCH syntax error`
* zajistit stabilní chování search endpointů
* oddělit „bezpečný uživatelský search“ od „pokročilého FTS dotazu“

---

## Proč je normalizace nutná

SQLite FTS5 je **citlivé na syntaxi**.
Uživatelský vstup jako:

```
radiohead (live) 1997-remaster
```

může:

* způsobit SQL chybu
* vrátit prázdné výsledky
* nebo se chovat nepředvídatelně

Proto **NEPOUŠTÍME** uživatelský vstup přímo do `MATCH`.

---

## Základní princip normalizace

Normalizace:

1. odstraní problematické znaky
2. sjednotí whitespace
3. rozdělí vstup na tokeny
4. spojí je explicitním `AND`

### Příklad

| Vstup              | Normalizovaný dotaz  |
| ------------------ | -------------------- |
| `radiohead live`   | `radiohead AND live` |
| `radiohead (live)` | `radiohead AND live` |
| `"ok computer"`    | `ok AND computer`    |
| `live-remaster`    | `live AND remaster`  |

---

## Implementace normalizace

### `FtsQueryNormalizer`

```java
public final class FtsQueryNormalizer {

    private static final Pattern INVALID_CHARS =
            Pattern.compile("[\"'\\\\*:+\\-()\\[\\]{}<>~]");

    private static final Pattern WHITESPACE =
            Pattern.compile("\\s+");

    private FtsQueryNormalizer() {
    }

    public static String normalize(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String q = input.toLowerCase(Locale.ROOT);
        q = INVALID_CHARS.matcher(q).replaceAll(" ");
        q = WHITESPACE.matcher(q).replaceAll(" ").trim();

        if (q.isEmpty()) {
            return "";
        }

        return String.join(" AND ", q.split(" "));
    }
}
```

---

## Doporučené endpointy

### 1️⃣ Bezpečný search (normalizovaný)

Určený pro:

* běžné uživatele
* UI search box
* REST API

```http
GET /api/tracks/search?q=radiohead live
```

#### Chování

* vždy bezpečný
* nikdy nevyhodí SQL chybu
* nepodporuje FTS operátory (`OR`, `"..."`, `*`)

```java
String ftsQuery = FtsQueryNormalizer.normalize(q);
```

---

### 2️⃣ Pokročilý search (raw FTS)

Určený pro:

* interní použití
* debug
* power-user scénáře

```http
GET /api/tracks/search/raw?q=artist:radiohead OR live*
```

#### Chování

* **bez normalizace**
* očekává validní FTS5 syntaxi
* může vyhodit chybu při špatném vstupu

```java
String ftsQuery = q; // žádná normalizace
```

⚠️ **NIKDY nepoužívat pro běžné uživatele**

---

## Doporučené API rozhraní

```http
GET /api/tracks/search       # normalizovaný
GET /api/tracks/search/raw   # raw FTS
```

---

## Designové rozhodnutí

* normalizace **není workaround**
* je to **bezpečnostní a stabilizační vrstva**
* oddělení endpointů:

    * zjednodušuje UI
    * umožňuje budoucí rozšíření

---

## Budoucí rozšíření (neimplementováno)

* field-specific search (`artist:`, `album:`)
* prefix search (`live*`)
* váhování polí (title > artist > note)
* fuzzy search

---

## Shrnutí

✔ SQLite FTS5 je rychlé, ale citlivé
✔ normalizace je nutná pro user input
✔ raw FTS má smysl jen pro pokročilé použití
✔ oddělené endpointy = čistá architektura

---

**TL;DR**

> Uživatelský vstup → **normalizovat**
> Pokročilý FTS → **explicitní raw endpoint**
