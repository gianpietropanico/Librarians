DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_database WHERE datname = 'library'
    ) THEN
        CREATE DATABASE library;
    END IF;
END $$;

\connect library;

-- Verifica l'utente e permessi
ALTER USER postgres WITH PASSWORD '12345';



-- Creazione del tipo genere
CREATE TYPE genere AS ENUM ('classico', 'saggio', 'fantascienza', 'romantico', 'commedia', 'horror', 'thriller', 'giallo', 'fumetto', 'manga', 'didattico', 'romanzo', 'romanzo storico', 'romazo epistolare', 'epico', 'letteratura' ,'fantasy', 'storico', 'avventura', 'drammatico', 'mistery');

-- Creazione dello stato prestito
CREATE TYPE stato AS ENUM('attivo','consegnato');


-- Creazione tabella utenti
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Aggiungi un RAISE NOTICE per capire se lo script arriva a questo punto

-- Creazione tabella libri
CREATE TABLE IF NOT EXISTS books (
    isbn VARCHAR(20) PRIMARY KEY,
    titolo VARCHAR(50) NOT NULL,
    genere genere NOT NULL,  -- Usa il tipo ENUM appena creato
    imageUrl VARCHAR(100) NOT NULL,
    autore VARCHAR(50) NOT NULL,
    quantita INTEGER NOT NULL,
    copieprestate INTEGER NOT NULL
);

-- Creazione Carrello

CREATE TABLE IF NOT EXISTS bag (
    username VARCHAR(255) NOT NULL,
    isbn VARCHAR(20)
);

-- Creazione Pretito

CREATE TABLE IF NOT EXISTS loan (
    id_prestito SERIAL PRIMARY KEY,
    username VARCHAR (255) NOT NULL,
    isbn VARCHAR (255) NOT NULL,
    data_inizio DATE NOT NULL,
    data_fine DATE NOT NULL,
    stato stato NOT NULL
);

CREATE TABLE IF NOT EXISTS k (
    max_prestiti INTEGER
);


INSERT INTO k (max_prestiti) VALUES (10);

INSERT INTO users (username, password) VALUES ('admin', 'admin');

-- Inserimento di dati iniziali nella tabella Books

INSERT INTO books (isbn, titolo, genere, imageUrl, autore, quantita, copieprestate) VALUES 
    ('9788817154857', '1984', 'classico', 'https://covers.openlibrary.org/b/id/12054715-M.jpg', 'George Orwell', 5, 0), 
    ('9780547928227', 'The Hobbit', 'fantasy', 'https://covers.openlibrary.org/b/id/14625529-M.jpg', 'J.R.R. Tolkien', 4, 0), 
    ('9780140447934', 'War and Peace', 'romanzo storico', 'https://covers.openlibrary.org/b/id/14598041-M.jpg', 'Leo Tolstoy', 3, 0), 
    ('9780307277671', 'The Da Vinci Code', 'fantasy', 'https://covers.openlibrary.org/b/id/14553572-M.jpg', 'Dan Brown', 6, 0), 
    ('9788868360269', 'It', 'horror', 'https://covers.openlibrary.org/b/id/14656946-M.jpg', 'Stephen King', 4, 0), 
    ('9780143105428', 'Pride and Prejudice', 'classico', 'https://covers.openlibrary.org/b/id/14619629-M.jpg', 'Jane Austen', 5, 0), 
    ('9780307949486', 'The Girl with the Dragon Tattoo', 'mistery', 'https://covers.openlibrary.org/b/id/6779579-M.jpg', 'Stieg Larsson', 5, 0), 
    ('9798392253876', 'The Great Gatsby', 'romanzo', 'https://covers.openlibrary.org/b/id/8248481-M.jpg', 'F. Scott Fitzgerald', 6, 0), 
    ('9788853019356', 'Treasure Island', 'avventura', 'https://covers.openlibrary.org/b/id/12819540-M.jpg', 'Robert Louis Stevenson', 7, 0), 
    ('9788806228644', 'Frankenstein', 'horror', 'https://covers.openlibrary.org/b/id/12991957-M.jpg', 'Mary Shelley', 4, 0),
    ('9780143035008', 'Anna Karenina', 'romantico', 'https://covers.openlibrary.org/b/id/3062541-M.jpg', 'Leo Tolstoy', 5, 0),
    ('9780544003415', 'The Lord of the Rings', 'fantasy', 'https://covers.openlibrary.org/b/id/14624677-M.jpg', 'J.R.R. Tolkien', 7, 0),
    ('9780156001311', 'The Name of the Rose', 'romanzo storico', 'https://covers.openlibrary.org/b/id/12178294-M.jpg', 'Umberto Eco', 9, 0),
    ('9780316769488', 'The Catcher in the Rye', 'romanzo', 'https://covers.openlibrary.org/b/id/14318906-M.jpg', 'J.D. Salinger', 11, 0),
    ('9780141439556', 'Wuthering Heights', 'romanzo', 'https://covers.openlibrary.org/b/id/14543388-M.jpg', 'Emily Brontë', 8, 0),
    ('9781451673319', 'Fahrenheit 451', 'fantascienza', 'https://covers.openlibrary.org/b/id/14402436-M.jpg', 'Ray Bradbury', 1, 0),
    ('9781503280786', 'Moby Dick', 'avventura', 'https://covers.openlibrary.org/b/id/10720283-M.jpg', 'Herman Melville', 4, 0),
    ('9780060935467', 'To Kill a Mockingbird', 'romanzo', 'https://covers.openlibrary.org/b/id/14351032-M.jpg', 'Harper Lee', 10, 0),
    ('9780060850524', 'Brave New World', 'fantascienza', 'https://covers.openlibrary.org/b/id/12645094-M.jpg', 'Aldous Huxley', 12, 0),
    ('9780486415871', 'Crime and Punishment', 'romanzo', 'https://covers.openlibrary.org/b/id/12622046-M.jpg', 'Fyodor Dostoevsky', 3, 0),
    ('9780140268867', 'The Odyssey', 'epico', 'https://covers.openlibrary.org/b/id/13169600-M.jpg', 'Homer', 7, 0),
    ('9780060934347', 'Don Quixote', 'romanzo', 'https://covers.openlibrary.org/b/id/13538218-M.jpg', 'Miguel de Cervantes', 9, 0),
    ('9780374528379', 'The Brothers Karamazov', 'romanzo', 'https://covers.openlibrary.org/b/id/10736021-M.jpg', 'Fyodor Dostoevsky', 6, 0),
    ('9780141439846', 'Dracula', 'horror', 'https://covers.openlibrary.org/b/id/12333446-M.jpg', 'Bram Stoker', 11, 0),
    ('9780785164036', 'Deadpool Kills The Marvel Universe', 'fumetto', 'https://covers.openlibrary.org/b/id/7690266-M.jpg', 'Dalibor Talajic', 6, 0),
    ('9788891217301', 'Deadpool Kills The Classic', 'fumetto', 'https://covers.openlibrary.org/b/id/7696420-M.jpg', 'Cullen Bunn', 3, 0),
    ('9780785184935', 'Deadpool Kills Deadpool', 'fumetto', 'https://covers.openlibrary.org/b/id/7627228-M.jpg', 'Cullen Bunn', 8, 0);

    
    





