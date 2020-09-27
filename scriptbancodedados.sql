script banco de dados


CREATE DATABASE movierentaldb;

CREATE USER teste@localhost IDENTIFIED BY 'test';
GRANT ALL ON movierentaldb.* TO teste@localhost;

CREATE TABLE users (
	user_id INT AUTO_INCREMENT NOT NULL,
	name VARCHAR(100) NOT NULL,
	username VARCHAR(45) NOT NULL,
	password VARCHAR(100) NOT NULL,
	role VARCHAR(45) NOT NULL,
	enable TINYINT NULL,
	PRIMARY KEY (user_id)
);

INSERT INTO users (name, username, password, role, enable) VALUES ('admin', 'teste', '$2a$10$w5yRWIih1k6Q9m9svPNUOujnGDdy1sjzTojEg2Hc6hYR2SBbELO7a', 'ROLE_USER', '1');


CREATE TABLE movies (
	id INT AUTO_INCREMENT NOT NULL,
	movie_title VARCHAR (100) NOT NULL,
	movie_director VARCHAR (100) NOT NULL,
	movie_amount INT NOT NULL,
	movie_amount_available INT NOT NULL,
	PRIMARY KEY(id)
);


INSERT INTO movies (movie_title, movie_director, movie_amount, movie_amount_available)
	VALUES  ('Pulp Fiction', 'Quentin Tarantino', 1, 1),
			('Django Unchained','Quentin Tarantino', 3, 3),
			('Inglourious Basterds','Quentin Tarantino', 2, 2),
			('Saving Private Ryan', 'Steven Spielberg', 2, 2),
			('Schindlers List', 'Steven Spielberg',3 ,3),
			('Bridge of Spies','Steven Spielberg', 3, 3),
			('Avatar', 'James Cameron',4 ,4),
			('Terminator 2: Judgment Day','James Cameron',1 ,1),
			('True Lies', 'James Cameron',2 ,2),
			('Solaris', 'James Cameron',1 ,1);
			
			
CREATE TABLE user_movies (
	id INT AUTO_INCREMENT NOT NULL,
	user_id INT NOT NULL,
	username VARCHAR(45) NOT NULL,
	movie_title VARCHAR(100) NOT NULL,
	PRIMARY KEY(id)
);			
			