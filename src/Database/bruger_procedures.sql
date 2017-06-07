DELIMITER //
 CREATE PROCEDURE createBruger
   ( 
     IN p_brugerId	INTEGER, 
     IN p_brugerNavn	TEXT, 
     IN p_initialer	TEXT, 
     IN p_password	TEXT, 
     IN brugerStatus	ENUM('0','1'),
	IN p_cpr	TEXT,
	IN p_rolle	ENUM('Laborant','Farmaceut', 'Admin', 'Vaerkfoerer')
   ) 
   BEGIN 
	INSERT INTO bruger (brugerId, brugerNavn, initialer, password, brugerStatus) VALUES (p_brugerId, p_brugerNavn, p_initialer, p_password, 1);

	INSERT INTO brugerinfo(brugerId, cpr, rolle) VALUES (p_brugerId, p_cpr, p_rolle);
   END // 




CREATE PROCEDURE updateBruger
   ( 
	IN p_brugerId	INTEGER, 
     IN p_brugerNavn	TEXT, 
     IN p_initialer	TEXT, 
     IN p_password	TEXT, 
     IN brugerStatus	ENUM('0','1'),
	IN p_cpr	TEXT,
	IN p_rolle	ENUM('Laborant','Farmaceut', 'Admin', 'Vaerkfoerer')
   ) 
   BEGIN 
     UPDATE brugere
       SET brugerNavn = p_brugerNavn, initialer = p_initialer, password = p_password 
     WHERE brugerId = p_brugerId;

	UPDATE brugerinfo
	SET cpr = p_cpr, rolle = p_rolle
	WHERE brugerId = p_brugerId;
   END // 
DELIMITER ;