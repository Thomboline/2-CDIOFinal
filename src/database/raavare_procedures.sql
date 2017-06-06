DELIMITER //
CREATE PROCEDURE createRaavare
(
	IN p_raavareId		INTEGER,
	IN p_raavareNavn	INTEGER,
	IN p_leverandoer	INTEGER
)
	BEGIN
	INSERT INTO raavare(raavareId, raavareNavn, levendaoer) VALUES (p_raavareId, p_raavareNavn, leverandoer);
	END //


DELIMITER //
CREATE PROCEDURE updateRaavare
(
	IN p_raavareId		INTEGER,
	IN p_raavareNavn	INTEGER,
	IN p_leverandoer	INTEGER
)
	BEGIN
	UPDATE raavare
	SET raavareNavn = p_raavareNavn, leverandoer = p_leverandoer
	WHERE raavareId = p_raavareId:
	END //
	DELIMITER ;