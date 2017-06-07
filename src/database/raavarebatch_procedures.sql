wDELIMITER //
CREATE PROCEDURE createRaavareBatch
(
	IN p_rbId	INTEGER,
	IN p_raavareId	INTEGER,
	IN p_maengde	REAL(3,3)
)
	BEGIN
	INSERT INTO raavareBatch (rbId, raavareId, maengde) VALUES (p_rbId, p_raavareId, p_maengde);
	END //

DELIMITER //
CREATE PROCEDURE updateRaavareBatch
(
	IN p_rbId	INTEGER,
	IN p_raavareId	INTEGER,
	IN p_maengde	REAL(3,3)
)
	BEGIN
	UPDATE raavareBatch
	SET raavareId = p_raavareId, maengde = p_maengde
	WHERE rbId = p_rbId;
	END // 
	DELIMITER ;