
# Event Management System - Full Project (File-backed)

This is a complete Java Event Management System ready to upload to GitHub and submit to GUVI.
It uses a simple **file-backed persistence** (no database required) so it runs immediately after download.

## What's included
- `src/ems/` Java classes:
  - `Event`, `PublicEvent`, `PrivateEvent`
  - `EventOperations`, `EventNotFoundException`
  - `EventManager` (thread-safe, file persistence)
  - `EventManagementApp` (Swing GUI)

- `data/events.dat` - generated at runtime (empty by default)
- `database.sql` - SQL create table (optional, if you want to use MySQL)

## How to compile & run (easy)
From project root:

### Compile
```
javac -d out src/ems/*.java
```

### Run
```
java -cp out ems.EventManagementApp
```

The GUI will open. All events are stored in `data/events.dat`.

## Optional: Using MySQL (not required)
A `database.sql` file is included if you prefer to switch to a database-backed version later.

## What to upload to GitHub
Upload the entire project folder (keep `src/` and `README.md`). That's enough for reviewers.

If you want, I can also:
- Convert to Maven project
- Add JDBC MySQL implementation
- Create a PPT for submission with rubric mapping

