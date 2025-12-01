
CREATE OR REPLACE ROLE MEW_GM_DB_Views;
GRANT SELECT ON MEW_GM_DB.* TO MEW_GM_DB_Views;

CREATE DATABASE IF NOT EXISTS MEW_GM_DB;
USE MEW_GM_DB;

DROP TABLE IF EXISTS Genre;

CREATE TABLE Genre (

    Genre_Id TINYINT PRIMARY KEY AUTO_INCREMENT NOT NULL
    , Genre_Name VARCHAR(30) NOT NULL
    , Genre_Description VARCHAR(254)
);

DROP TABLE IF EXISTS Console;

CREATE TABLE Console (

    Console_Id TINYINT PRIMARY KEY AUTO_INCREMENT NOT NULL
    , Console_Name VARCHAR(12) NOT NULL
);

DROP TABLE IF EXISTS Game;

CREATE TABLE Game (

    Game_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL
    , Game_Title VARCHAR(50) NOT NULL
    , Single_Player BOOLEAN DEFAULT 1 NOT NULL
    , Multi_Player BOOLEAN DEFAULT 0 NOT NULL
    , Primary_Genre TINYINT NOT NULL
    , FOREIGN KEY (Primary_Genre) REFERENCES Genre(Genre_Id)
);

DROP TABLE IF EXISTS User;

CREATE TABLE User (

    User_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL
    , First_Name VARCHAR(15) NOT NULL
    , Last_Name VARCHAR(20)
    , Dob DATE NOT NULL
    , Email VARCHAR(50) NOT NULL
    , Gamertag VARCHAR(25) NOT NULL
    , Preferred_Console TINYINT NOT NULL
    , About_User VARCHAR(500)
    , FOREIGN KEY (Preferred_Console) REFERENCES Console(Console_Id)
);

DROP TABLE IF EXISTS UserGames;

CREATE TABLE UserGames (

    User_Id INT NOT NULL
    , Game_Id INT NOT NULL
    , PRIMARY KEY (User_Id, Game_Id)
    , FOREIGN KEY (User_Id) REFERENCES User(User_Id) ON DELETE CASCADE
    , FOREIGN KEY (Game_Id) REFERENCES Game(Game_Id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS Matched_User;

CREATE TABLE Matched_User (

    User_Id INT NOT NULL
    , Liked_User_Id INT NOT NULL
    , Is_Matched BOOLEAN DEFAULT 0
    , PRIMARY KEY (User_Id, Liked_User_Id)
    , FOREIGN KEY (User_Id) REFERENCES User(User_Id) ON DELETE CASCADE
    , FOREIGN KEY (Liked_User_Id) REFERENCES User(User_Id) ON DELETE CASCADE
);

INSERT INTO Console (Console_Name) VALUES ('PlayStation');
INSERT INTO Console (Console_Name) VALUES ('Nintendo');
INSERT INTO Console (Console_Name) VALUES ('Xbox');
INSERT INTO Console (Console_Name) VALUES ('PC');

INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('MOBA', 'Multiplayer Online Battle Arena');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('FPS', 'First Person Shooter');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('RPG', 'Role-Playing Game');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Sports', 'Sports');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Racing', 'Vroom Vroom');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Fighting', 'Boom Pow');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Puzzle', 'Big think');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Adventure', 'It''s dangerous bussiness steeping out your front door');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Survival', 'survive');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Strategy', 'strats');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Battle Royale', 'darn kids and their fortnite');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Sandbox', 'they yearn for the mines...');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Hero Shooter', 'toxic af');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Horror', 'spooky');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Platformer', 'Don''t trip');
INSERT INTO Genre (Genre_Name, Genre_Description) VALUES ('Party', 'WOO!');

INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Fortnite', 0, 1, 11);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Pokemon: Unite', 0, 1, 1);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Call of Duty', 1, 1, 2);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('League of Legends', 0, 1, 1);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Minecraft', 1, 1, 12);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Apex Legends', 0, 1, 11);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Overwatch', 0, 1, 13);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Valorant', 0, 1, 13);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('TLoZ: Tears of the Kingdom', 1, 0, 3);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('FIFA', 1, 1, 4);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Mario Kart World', 1, 1, 5);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Tekken', 1, 1, 6);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Tetris', 1, 0, 7);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Assassin''s Creed', 1, 0, 8);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Palworld', 1, 1, 9);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Civilization', 1, 0, 10);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES ('Resident Evil', 1, 0, 14);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES
('Monster Hunter: Wilds', TRUE, TRUE, 3),
('EA Sports College Football 26', TRUE, TRUE, 4),
('Kingdom Come: Deliverance II', TRUE, FALSE, 3),
('Elden Ring: Nightreign', TRUE, TRUE, 3),
('NBA 2K26', TRUE, TRUE, 4),
('The Elder Scrolls IV: Oblivion Remastered', TRUE, FALSE, 3),
('Mafia: The Old Country', TRUE, FALSE, 8),
('Metal Gear Solid Delta: Snake Eater', TRUE, FALSE, 8),
('WWE 2K25', TRUE, TRUE, 4),
('Forza Horizon 5', TRUE, TRUE, 5);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES
('Battlefield 6', TRUE, TRUE, 2),
('Hogwarts Legacy', TRUE, FALSE, 3),
('Hollow Knight: Silksong', TRUE, FALSE, 3),
('Clair Obscur: Expedition 33', TRUE, FALSE, 3),
('Metal Gear Solid Delta: Snake Eater', TRUE, FALSE, 8),
('Mafia: The Old Country', TRUE, FALSE, 8),
('Gears of War: Reloaded', TRUE, TRUE, 2),
('Grounded 2', TRUE, TRUE, 3),
('Wuchang: Fallen Feathers', TRUE, FALSE, 3),
('Marvel Rivals', TRUE, TRUE, 2);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES
('Astro Bot', TRUE, FALSE, 15),
('Still Wakes the Deep', TRUE, FALSE, 14),
('Helldivers 2', TRUE, TRUE, 2),
('Black Myth: Wukong', TRUE, FALSE, 3),
('Thank Goodness You''re Here!', TRUE, FALSE, 3),
('Lego Horizon Adventures', TRUE, TRUE, 3),
('Warhammer 40,000: Space Marine 2', TRUE, TRUE, 2),
('Final Fantasy VII Rebirth', TRUE, FALSE, 3),
('The Plucky Squire', TRUE, FALSE, 3),
('Super Mario Party Jamboree', TRUE, TRUE, 16);
INSERT INTO Game (Game_Title, Single_Player, Multi_Player, Primary_Genre) VALUES
('Persona 3 Reload', TRUE, FALSE, 3),
('Dragon Quest I & II HD-2D Remake', TRUE, FALSE, 3),
('Plants vs Zombies: Replanted', TRUE, TRUE, 10);

INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Evan', 'Timmons', '1991-01-16', 'timmonsevan@gmail.com', 'dudeicidal', 1);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Sarah', 'Henderson', '1993-05-22', 'sarah.henderson93@gmail.com', 'QueenBee93', 2);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Liam', 'Garcia', '2000-11-04', 'liamg2000@yahoo.com', 'NightFalcon', 4);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Ava', 'Robinson', '1998-03-14', 'ava.robinson98@gmail.com', 'PixieShot', 3);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Noah', 'Williams', '1995-09-10', 'noah_williams95@hotmail.com', 'ZeroByte', 2);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Isabella', 'Moore', '2002-07-30', 'isa.moore02@gmail.com', 'EchoDream', 1);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Mason', 'Davis', '1989-12-02', 'mason.davis89@yahoo.com', 'RetroRogue', 4);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Olivia', 'Clark', '1996-02-19', 'olivia.clark96@gmail.com', 'NovaHeart', 3);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('James', 'Lopez', '1990-08-23', 'jlopez90@gmail.com', 'ShadowPulse', 1);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Emma', 'Rodriguez', '1997-04-11', 'emma.rod97@yahoo.com', 'Sn1perKitten', 2);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Benjamin', 'Scott', '1988-06-09', 'benscott88@gmail.com', 'LagWizard', 4);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Charlotte', 'Perez', '2001-10-25', 'charperez01@gmail.com', 'MoonSurfer', 3);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Elijah', 'Turner', '1994-01-05', 'elijah.turner94@yahoo.com', 'RogueCore', 2);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Amelia', 'Morris', '1999-12-17', 'ameliamorris99@gmail.com', 'BlitzBerry', 1);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Logan', 'Hill', '1992-03-29', 'logan.hill92@gmail.com', 'CrashPilot', 4);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Sophia', 'Adams', '1995-11-08', 'sophia.adams95@hotmail.com', 'NeonNova', 3);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Alexander', 'Baker', '1991-04-21', 'alexbaker91@gmail.com', 'JetStreamX', 2);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Mia', 'Carter', '1998-09-02', 'mia.carter98@gmail.com', 'LunaHawk', 1);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Jacob', 'Mitchell', '1993-05-15', 'jacobmitch93@gmail.com', 'WarpedLogic', 4);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Harper', 'Evans', '2000-02-27', 'harper.evans00@gmail.com', 'CrimsonAce', 3);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('John', 'Doe', '2006-06-06', 'leAntichrist@gmail.com', 'D33vil', 2);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Jane', 'Doe', '1995-02-14', 'JaneDOH@gmail.com', 'DohDoh', 3);
INSERT INTO `User` (First_Name, Last_Name, Dob, Email, Gamertag, Preferred_Console) VALUES ('Electric', 'Wizard', '1692-06-06', 'dopethrone@gmail.com', 'puffpuffp4$$', 1);

INSERT INTO UserGames VALUES (1, 1);
INSERT INTO UserGames VALUES (2, 1);
INSERT INTO UserGames VALUES (3, 1);
INSERT INTO UserGames VALUES (4, 1);
INSERT INTO UserGames VALUES (5, 1);
INSERT INTO UserGames VALUES (6, 1);
INSERT INTO UserGames VALUES (7, 1);
INSERT INTO UserGames VALUES (8, 1);
INSERT INTO UserGames VALUES (9, 1);
INSERT INTO UserGames VALUES (10, 1);
INSERT INTO UserGames VALUES (11, 1);
INSERT INTO UserGames VALUES (12, 1);
INSERT INTO UserGames VALUES (13, 1);
INSERT INTO UserGames VALUES (14, 1);
INSERT INTO UserGames VALUES (15, 1);
INSERT INTO UserGames VALUES (16, 1);
INSERT INTO UserGames VALUES (17, 1);
INSERT INTO UserGames VALUES (18, 1);
INSERT INTO UserGames VALUES (19, 1);
INSERT INTO UserGames VALUES (20, 1);
INSERT INTO UserGames VALUES (2, 25);
INSERT INTO UserGames VALUES (1, 47);
INSERT INTO UserGames VALUES (4, 49);
INSERT INTO UserGames VALUES (14, 48);
INSERT INTO UserGames VALUES (15, 15);
INSERT INTO UserGames VALUES (12, 40);
INSERT INTO UserGames VALUES (10, 13);
INSERT INTO UserGames VALUES (12, 5);
INSERT INTO UserGames VALUES (18, 22);
INSERT INTO UserGames VALUES (20, 11);
INSERT INTO UserGames VALUES (6, 12);
INSERT INTO UserGames VALUES (14, 32);
INSERT INTO UserGames VALUES (9, 16);
INSERT INTO UserGames VALUES (2, 28);
INSERT INTO UserGames VALUES (15, 8);
INSERT INTO UserGames VALUES (8, 41);
INSERT INTO UserGames VALUES (7, 42);
INSERT INTO UserGames VALUES (13, 2);
INSERT INTO UserGames VALUES (18, 25);
INSERT INTO UserGames VALUES (1, 19);
INSERT INTO UserGames VALUES (8, 2);
INSERT INTO UserGames VALUES (10, 49);
INSERT INTO UserGames VALUES (2, 18);
INSERT INTO UserGames VALUES (17, 49);
INSERT INTO UserGames VALUES (9, 32);
INSERT INTO UserGames VALUES (15, 44);
INSERT INTO UserGames VALUES (14, 14);
INSERT INTO UserGames VALUES (9, 23);
INSERT INTO UserGames VALUES (13, 17);
INSERT INTO UserGames VALUES (14, 24);
INSERT INTO UserGames VALUES (10, 47);
INSERT INTO UserGames VALUES (20, 21);
INSERT INTO UserGames VALUES (10, 8);
INSERT INTO UserGames VALUES (17, 38);
INSERT INTO UserGames VALUES (12, 32);
INSERT INTO UserGames VALUES (2, 17);
INSERT INTO UserGames VALUES (20, 5);
INSERT INTO UserGames VALUES (7, 47);
INSERT INTO UserGames VALUES (5, 35);
INSERT INTO UserGames VALUES (6, 14);
INSERT INTO UserGames VALUES (20, 39);
INSERT INTO UserGames VALUES (4, 10);
INSERT INTO UserGames VALUES (10, 34);
INSERT INTO UserGames VALUES (21, 41);
INSERT INTO UserGames VALUES (19, 48);
INSERT INTO UserGames VALUES (1, 37);
INSERT INTO UserGames VALUES (15, 27);
INSERT INTO UserGames VALUES (17, 42);
INSERT INTO UserGames VALUES (9, 13);
INSERT INTO UserGames VALUES (1, 18);
INSERT INTO UserGames VALUES (7, 46);
INSERT INTO UserGames VALUES (15, 10);
INSERT INTO UserGames VALUES (16, 18);
INSERT INTO UserGames VALUES (12, 10);
INSERT INTO UserGames VALUES (12, 8);
INSERT INTO UserGames VALUES (4, 48);
INSERT INTO UserGames VALUES (9, 43);
INSERT INTO UserGames VALUES (8, 21);
INSERT INTO UserGames VALUES (5, 23);
INSERT INTO UserGames VALUES (15, 40);
INSERT INTO UserGames VALUES (20, 3);
INSERT INTO UserGames VALUES (8, 10);
INSERT INTO UserGames VALUES (17, 50);
INSERT INTO UserGames VALUES (5, 2);
INSERT INTO UserGames VALUES (6, 50);
INSERT INTO UserGames VALUES (14, 37);
INSERT INTO UserGames VALUES (14, 39);
INSERT INTO UserGames VALUES (9, 20);
INSERT INTO UserGames VALUES (20, 19);
INSERT INTO UserGames VALUES (10, 38);
INSERT INTO UserGames VALUES (17, 44);
INSERT INTO UserGames VALUES (9, 44);
INSERT INTO UserGames VALUES (7, 36);
INSERT INTO UserGames VALUES (20, 36);
INSERT INTO UserGames VALUES (10, 18);
INSERT INTO UserGames VALUES (13, 32);
INSERT INTO UserGames VALUES (11, 33);
INSERT INTO UserGames VALUES (2, 32);
INSERT INTO UserGames VALUES (2, 36);
INSERT INTO UserGames VALUES (20, 13);
INSERT INTO UserGames VALUES (1, 34);
INSERT INTO UserGames VALUES (6, 19);
INSERT INTO UserGames VALUES (1, 28);
INSERT INTO UserGames VALUES (17, 18);
INSERT INTO UserGames VALUES (16, 13);
INSERT INTO UserGames VALUES (18, 38);
INSERT INTO UserGames VALUES (14, 42);
INSERT INTO UserGames VALUES (3, 2);
INSERT INTO UserGames VALUES (13, 33);
INSERT INTO UserGames VALUES (7, 5);
INSERT INTO UserGames VALUES (13, 6);
INSERT INTO UserGames VALUES (19, 14);
INSERT INTO UserGames VALUES (2, 16);
INSERT INTO UserGames VALUES (9, 14);
INSERT INTO UserGames VALUES (21, 1);
INSERT INTO UserGames VALUES (21, 2);
INSERT INTO UserGames VALUES (21, 18);
INSERT INTO UserGames VALUES (21, 50);
INSERT INTO UserGames VALUES (21, 6);
INSERT INTO UserGames VALUES (21, 43);
INSERT INTO UserGames VALUES (21, 12);
INSERT INTO UserGames VALUES (21, 31);
INSERT INTO UserGames VALUES (22, 1);
INSERT INTO UserGames VALUES (22, 2);
INSERT INTO UserGames VALUES (23, 1);
INSERT INTO UserGames VALUES (23, 2);
INSERT INTO UserGames VALUES (23, 23);
INSERT INTO UserGames VALUES (23, 42);

INSERT INTO Matched_User VALUES (3, 4, FALSE);
INSERT INTO Matched_User VALUES (2, 5, FALSE);
INSERT INTO Matched_User VALUES (16, 1, FALSE);
INSERT INTO Matched_User VALUES (1, 12, FALSE);
INSERT INTO Matched_User VALUES (16, 19, FALSE);
INSERT INTO Matched_User VALUES (20, 7, FALSE);
INSERT INTO Matched_User VALUES (9, 4, FALSE);
INSERT INTO Matched_User VALUES (19, 17, FALSE);
INSERT INTO Matched_User VALUES (6, 7, FALSE);
INSERT INTO Matched_User VALUES (12, 2, FALSE);
INSERT INTO Matched_User VALUES (20, 18, FALSE);
INSERT INTO Matched_User VALUES (4, 7, FALSE);
INSERT INTO Matched_User VALUES (22, 12, FALSE);
INSERT INTO Matched_User VALUES (7, 14, FALSE);
INSERT INTO Matched_User VALUES (3, 14, FALSE);
INSERT INTO Matched_User VALUES (8, 4, FALSE);
INSERT INTO Matched_User VALUES (9, 21, FALSE);
INSERT INTO Matched_User VALUES (8, 19, FALSE);
INSERT INTO Matched_User VALUES (10, 22, FALSE);
INSERT INTO Matched_User VALUES (1, 22, FALSE);
INSERT INTO Matched_User VALUES (1, 2, FALSE);

-- CREATE TABLE Matched_User_backup AS SELECT * FROM Matched_User;

-- ALTER TABLE Matched_User
-- DROP FOREIGN KEY Matched_User_ibfk_1,
-- DROP FOREIGN KEY Matched_User_ibfk_2;

-- ALTER TABLE Matched_User
-- ADD CONSTRAINT fk_matched_user_user
-- FOREIGN KEY (User_Id) REFERENCES `User`(User_Id) ON DELETE CASCADE,
-- ADD CONSTRAINT fk_matched_user_liked_user
-- FOREIGN KEY (Liked_User_Id) REFERENCES `User`(User_Id) ON DELETE CASCADE;

-- CREATE TABLE UserGames_backup AS SELECT * FROM UserGames;

-- ALTER TABLE UserGames
-- DROP FOREIGN KEY UserGames_ibfk_1,
-- DROP FOREIGN KEY UserGames_ibfk_2;

-- ALTER TABLE UserGames
-- ADD CONSTRAINT fk_usergames_user
-- FOREIGN KEY (User_Id) REFERENCES `User`(User_Id) ON DELETE CASCADE,
-- ADD CONSTRAINT fk_usergames_games
-- FOREIGN KEY (Game_Id) REFERENCES Game(Game_Id) ON DELETE CASCADE;

-- DROP TABLE IF EXISTS Matched_User_backup;
-- DROP TABLE IF EXISTS UserGames_backup;
