DELIMITER //
CREATE PROCEDURE createRecept
(
	IN p_receptId	INTEGER,
	IN p_receptNavn	TEXT
)
	BEGIN 
	INSERT INTO recept (receptId, receptNavn) VALUES (p_receptId, p_receptNavn);
	END //



DELIMITER //
CREATE PROCEDURE updateRecept
(
	IN p_receptId	INTEGER,
	IN p_receptNavn	TEXT
)
	BEGIN
	UPDATE recept
	SET receptNavn = p_receptNavn
	WHERE receptId = p_receptId;
	END //
	DELIMITER ;