CREATE PROCEDURE createReceptKomponent
(
	IN p_receptId	INTEGER,
	IN p_raavareId	INTEGER,
	IN p_nomNetto	REAL(3,3),
	IN p_tolerance	REAL(3,3)
)
	BEGIN
	INSERT INTO receptkomponent (receptId, raavareId, nomNetto, tolerance) VALUES (p_receptId, p_raavareId, p_nomNetto, p_tolerance);
	END //





CREATE PROCEDURE updateReceptKomponent
(
	IN p_receptId	INTEGER,
	IN p_raavareId	INTEGER,
	IN p_nomNetto	REAL(3,3),
	IN p_tolerance	REAL(3,3)
)
	BEGIN
	UPDATE receptkomponent
	SET nomNetto = p_nomNetto, tolerance = p_tolerance
	WHERE receptId = p_receptId and raavareId = p_raavareId;
	END //
	DELIMITER ;