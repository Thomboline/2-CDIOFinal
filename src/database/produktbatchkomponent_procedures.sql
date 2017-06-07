DELIMITER //
CREATE PROCEDURE createProduktBatchKomponent
(
	IN p_pbId	INTEGER,
	IN p_rbId	INTEGER,
	IN p_brugerId	INTEGER,
	IN p_tara	REAL(3,3),
	IN p_netto	REAL(3,3),
	IN p_terminal	ENUM('1','2','3','4')
)
	BEGIN
	INSERT INTO produktbatchkomponent(pdId, rbId, brugerId, tara, netto, terminal) VALUES(p_pbId, p_rbId, p_brugerId, p_tara, p_netto, p_terminal);
	END //



DELIMITER //
CREATE PROCEDURE updateProduktBatchKomponent
(
	IN p_pbId	INTEGER,
	IN p_rbId	INTEGER,
	IN p_brugerId	INTEGER,
	IN p_tara	REAL(3,3),
	IN p_netto	REAL(3,3),
	IN p_terminal	ENUM('1','2','3','4')
)
	BEGIN
	UPDATE produktbatchkomponent
	SET tara = p_tara, netto = p_netto, terminal = p_terminal
	WHERE pbId = p_pbId and rbId = p_rbId and brugerId = p_brugerId;
	END //
	DELIMITER ;