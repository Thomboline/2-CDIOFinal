DELIMITER //
CREATE PROCEDURE createProduktBatch
(
	IN p_pbId	INTEGER,
	IN p_receptId	INTEGER
	
)
	BEGIN
	INSERT INTO produktbatch(pbId, receptId, status) VALUES (p_pbId, p_receptId, 0);
	END //


DELIMITER //
CREATE PROCEDURE updateProduktBatch
(
	IN p_pbId	INTEGER,
	IN p_receptId	INTEGER
	IN p_status	ENUM('0','1','2')
)
	BEGIN
	UPDATE produktBatch
	SET status = p_status
	WHERE pbId = p_pbId and receptId = p_receptId;
	END //
	DELIMITER ;