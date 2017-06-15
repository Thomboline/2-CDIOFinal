package controller;

import java.text.DecimalFormat;
import connector.DALException;
import dao.BrugerDAO;
import dao.ProduktBatchDAO;
import dao.ProduktBatchKompDAO;
import dao.RaavareBatchDAO;
import dao.RaavareDAO;
import dao.ReceptDAO;
import dao.ReceptKompDAO;
import dto.ProduktBatchDTO;
import dto.ProduktBatchKompDTO;
import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketController;
import socket.SocketInMessage;
import socket.SocketOutMessage;
import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;

/**
 * MainController - integrating input from socket
 *  and ui. Implements ISocketObserver and IUIObserver to handle this.
 * @author Christian Budtz
 * @version 0.1 2017-01-24
 *
 */
public class WeightController implements IWeightController, ISocketObserver, IWeightInterfaceObserver {

	/************Controllers*******************/
	
	private ISocketController socketHandler;
	private IWeightInterfaceController weightController;
	
	/************DAO og DTO*******************/
	
	private BrugerDAO bdao = new BrugerDAO();
	private ProduktBatchDAO pdao = new ProduktBatchDAO();
	private ProduktBatchKompDAO pkdao = new ProduktBatchKompDAO();
	private RaavareBatchDAO rbdao = new RaavareBatchDAO();
	private RaavareDAO rdao = new RaavareDAO();
	private ReceptDAO recdao = new ReceptDAO();
	private ReceptKompDAO rkdao = new ReceptKompDAO();
	private ProduktBatchDTO pbdto = new ProduktBatchDTO();
	private ProduktBatchKompDTO pkdto = new ProduktBatchKompDTO();
	
	
	/**********Instance variabler*************/
	
	//Til kontrol af input på vægten - begrænser brugeren til
	//funktionaliteten af det givne trin i afvejningsprocessen
	private KeyState keyState;
	private KeyState prevKeyState = KeyState.K1;
	
	//Vægt variable
	private double currentWeight = 0.000;
	private double containerWeight, registeredWeight, minWeight, maxWeight;
	
	//Vægt input
	private char[] msCMD = new char[32];
	private int counter = 0;
	
	//Array til beskeder vist på displayet i det givne afvejningstrin
	//Gør det muligt at skifte mellem softbtn tekst og viste instrukser
	private String[] cd;
	
	//Boolske variable til condition handling og container til softBtn beskrivelser
	private boolean isWriting, isTara, isRM208, isWeighing, isFinished;
	private boolean softBtn0 = false, softBtn1 = false, softBtn2 = false,
					softBtn3 = false, softBtn4 = false, softBtn5 = false;
	private boolean softBtns[] = {softBtn0, softBtn1, softBtn2, softBtn3, softBtn4, softBtn5};
	private String btnLayout0[] = {"", "", "Taravægt", "Nettovægt", "", "OK"};
	private String btnLayout1[] = {"", "", "", "", "", "OK"};

	//Iterator til afvejningsprocedure og input, simple variable til kritisk
	//input fra brugeren og container dertil.
	private int wtIterator = 2;
	private int[] tempInf = new int[4];
	private int tm, lm, pb, rb;
	private int inf = 0;
	
	//Simple variable til info fra databasen
	int rbRaavare, rkRaavare, pbRecept;
	double rkNomNetto, rbNomNetto, tolerance;
	
	//DecimalFormat til formatering af vægt i primary display - viser ellers 8 decimaler
	DecimalFormat df = new DecimalFormat ("0.000");
	
	public WeightController(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.init(socketHandler, weightInterfaceController);
	}

	//
	@Override
	public void init(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.socketHandler = socketHandler;
		this.weightController=weightInterfaceController;
	}

	@Override
	public void start() {
		if (socketHandler != null && weightController != null) {
			
			/**********************OPSTART****************************/
			
			// Makes this controller interested in messages from the socket
			socketHandler.registerObserver(this);

			// Starts socketHandler & weightController in own threads
			new Thread(socketHandler).start();
			new Thread(weightController).start();
			weightController.registerObserver(this);

		} else {
			System.err.println("No controllers injected!");
		}
	}
	
	// Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		

		//Hvis det er første gang programmet startes
		begin();
		
		//Bearbejder beskeden og sender den videre til vægtprocedure-metoden weightState()
		processSocketMessage(message);
	}

	//Lytter efter keypress i GUIen
	@Override
	public void notifyKeyPress(KeyPress keyPress) {
		System.out.println(keyPress.getCharacter());
		String[] empty = new String[] { "", "", "", "", "", "" };
		try {
			switch (keyPress.getType()) {
			case SOFTBUTTON:
				if (wtIterator == 2) {
					break;
				} else {
					if (keyPress.getKeyNumber() == 0) {
						softBtns[0] = true;
						if (isWriting) {
							if (msCMD[0] != '\0') {
								counter--;
								msCMD[counter] = '\0';
								String temp = new String(msCMD);
								weightController.showMessageSecondaryDisplay(temp);
							}
						}
					}
					if (keyPress.getKeyNumber() == 1) {
					}

					if (keyPress.getKeyNumber() == 2 && isWeighing) {
						if (softBtns[2]) {
							currentDisplay(2, 1);
							switchBtnLayout(2, "Taravægt");
						} else if (!softBtns[2]) {
							softBtns[2] = true;
							weightController.showMessageSecondaryDisplay("Tara weight: " + containerWeight);
							switchBtnLayout(2, "Tilbage");
						}
					}
					if (keyPress.getKeyNumber() == 3 && isWeighing) {
						if (softBtns[3]) {
							currentDisplay(3, 0);
							switchBtnLayout(3, "Nettovægt");
						} else if (!softBtns[3]) {
							softBtns[3] = true;
							weightController.showMessageTernaryDisplay("Current weight: " + currentWeight + " kg");
							switchBtnLayout(3, "Tilbage");
						}
					}
					if (keyPress.getKeyNumber() == 4) {
						notifyKeyPress(KeyPress.Cancel());
					}
					if (keyPress.getKeyNumber() == 5) {
						notifyKeyPress(KeyPress.Send());
					}
				}
				break;
			case TARA:
				if (isTara) {
					this.containerWeight += this.currentWeight;
					notifyWeightChange(0);
					isTara = false;
					if(isRM208) {
					socketWake();
					}
				}
				break;
			case TEXT:
				if (keyState.equals(KeyState.K1)) {
					btnLayout1[0] = "Backspace";
					weightController.setSoftButtonTexts(btnLayout1);
					isWriting = true;
					msCMD[counter] = keyPress.getCharacter();
					String temp = new String(msCMD);
					weightController.showMessageSecondaryDisplay(temp);
					counter++;
				} else {
				}
				break;
			case ZERO:
			/**
			 
			 Ikke implementeret
			 
			 */
				break;
			case CANCEL:
				if(isFinished) {
					notifyKeyPress(KeyPress.Exit());
				}
				
		/**
		 
		 Implementationen gør hvad den skal, men der opstår tilfældige Java FX exceptions. Nullpointer og 
		 en masse ArrayIndexOutOfBounds - en stacktrace på 4 sider. Det har ikke været muligt at kommer til bunds
		 i fejlen, men det er resultatet af en ulovlig handlign ifbm. flere tråde.
		 Koden forbliver her, dog udkommenteret, så programmet ikke har risiko for at crashe på tilfældig vis.
		 
		 */
//				if (keyState.equals(KeyState.K2) || keyState.equals(KeyState.K1)) {
//					if (prevKeyState.equals(KeyState.K1) && inf != 0) {
//						inf--;
//					}
//					cancel = true;
//					wtIterator--;
//					weightController.setSoftButtonTexts(empty);
//					keyState = prevKeyState;
//					flushDisplays();
//					try {
//						synchronized (socketHandler) {
//							socketHandler.notify();
//							SocketController.iterator = SocketController.iterator - 2;
//							isRM208 = false;
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
				break;
			case EXIT:
				socketHandler.close();
				System.exit(0);
				break;
			case SEND:
				if (keyState.equals(KeyState.K1) && msCMD[0] != 0 || isTara) {
					btnLayout1[0] = "";
					weightController.setSoftButtonTexts(btnLayout1);
					if (!isWeighing) {
						weightController.setSoftButtonTexts(empty);
					}
					if(isTara) {
						notifyKeyPress(KeyPress.Tara());
					} else {
					prepInfo();	// Stores input in weight info container
					sendMessageCMD(); // Prepares message and sends a new String to CMD
					flushMsCMD(); // Flush msCMD char array
					if (isRM208) {
						socketWake();
					}
					socketHandler.sendMessage(new SocketOutMessage("RM A "));
					}
				}
				if (keyState.equals(KeyState.K2) && !isFinished || keyState.equals(KeyState.K4)) {
					flushMsCMD(); //for en sikkerheds skyld
					if (keyState.equals(KeyState.K4)) {
						registeredWeight = currentWeight;
					}
					socketWake();
					socketHandler.sendMessage(new SocketOutMessage("RM A "));
					socketHandler.sendMessage(new SocketOutMessage("'OK'"));
				}
				if (keyState.equals(KeyState.K3)) {
					notifyKeyPress(KeyPress.Tara());
				}
				if(isFinished) {
				/***	Metoden er lavet og implementeret, men forsager fejl. 	***/	
				/***	begin();												 ***/
				}
				break;
			case ONCLOSE:
				socketHandler.close();
			}
		}
		catch(NullPointerException e) {
			System.out.println(("Ingen forbindelse til vægt. \n"
					+ "Forbind venligst programmet via telnet ip: 127.0.0.1 port: 8000"));
		}
	}

	@Override
	public void notifyWeightChange(double newWeight) {
		if(keyState.equals(KeyState.K3) || keyState.equals(KeyState.K4)) {
		currentWeight = (double) newWeight;
		weightController.showMessagePrimaryDisplay(df.format(currentWeight) + "kg");
		}
	}

	/** Afl�st milj� til afvejningsprocedure **/
public boolean weightState(SocketInMessage message, int wtIterator) throws DALException {
	
	//Brug 'tempInf' til at gemme midlertidige variable
	//[0] til terminal nummer
	//[1] til laborant nummer
	//[2] til produktbatch nummer
	//[3] til raavarebatch nummer
	
	//Switch-casen starter paa 3 saa cases svarer til de egentlige trin i afvejningsproceduren
	
	/*******************ALLE SYSTEM.OUT.PRINTLN ER TEST DATA******************/
	
	switch (wtIterator) {
	
	//Vaelg afvejningsterminal
	case 3:
		isRM208 = true;
		initDisplay(message.getMessage(), "", "Indtast");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		return true;
	
	//Vælg laborant nummer	
	case 4:
		isRM208 = true;
		prevKeyState = KeyState.K1;
		
		//Tjek om sidste case er gyldig
		if(tm < 1 || tm > 4 || tm == '0') {
			inf--;
			return false;
		}
		initDisplay(message.getMessage(), "", "Indtast");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		keyState = KeyState.K2;
		return true;
		
	//Svar med Laborantens initialer
	case 5:
		prevKeyState = KeyState.K1;
		isRM208 = true;
		try {
		initDisplay(message.getMessage() + bdao.getBruger(lm).getBrugerNavn() + ", " + bdao.getBruger(lm).getIni(), "Tryk 'OK'", "Velkommen");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		}
		//Sidste case er ikke gyldig
		catch (Exception e) {
				inf--;
				keyState = prevKeyState;
				return false;
		}
		keyState = KeyState.K1;
		return true;
	
	//Indtast produktbatchnummer
	case 6:
		prevKeyState = KeyState.K2;
		isRM208 = true;
		initDisplay(message.getMessage(), "", "Indtast");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		keyState = KeyState.K2;
		return true;
	
	//Svar med recept
	case 7:
		prevKeyState = KeyState.K1;
		isRM208 = true;
		try {
			pbRecept = pdao.getProduktBatch(pb).getReceptId();
			if(pdao.getProduktBatch(pb).getStatus() != 2) {
				System.out.println("Dette trin bør blive ignoreret.");
			// K�rer igennem produktbatchkomponenterne
			for (int i = 0; i < pkdao.getProduktBatchKompList(pb).size(); i++) {

				// Hvis det givne produktbatchkomponent ikke er behandlet (mangler brugerID)
				if (pkdao.getProduktBatchKompList(pb).get(i).getBrugerId() == 0) {
					rkRaavare = rkdao.getReceptKompList(pbRecept).get(i).getRaavareId();
					rkNomNetto = rkdao.getReceptKompList(pbRecept).get(i).getNomNetto();
					tolerance = rkdao.getReceptKompList(pbRecept).get(i).getTolerance();
					minWeight = rkNomNetto - (rkNomNetto * tolerance / 100);
					maxWeight = rkNomNetto + (rkNomNetto * tolerance / 100);
					System.out.println("Råvare id: " + rkRaavare);
					initDisplay(message.getMessage() + " " + recdao.getRecept(pdao.getProduktBatch(pb).getReceptId()).getReceptNavn(),
						"Du skal afveje ca. " + rkNomNetto + " kg " + rdao.getRaavare(rkRaavare).getRaavareNavn(), "Tryk 'OK'");
					socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
					socketWait();
					keyState = KeyState.K2;
					return true;
					}
			}
			}
			initDisplay("", "Produktbatch færdigproduceret. Vælg et andet.", "Fejl");
			inf--;
			socketWait();
			return false;
			}
		//Sidste case er ikke gyldig
		catch (Exception e) {
			inf--;
			initDisplay("Ugyldigt input", "Produktbatchet eksisterer ikke. Prøv et andet.", "Fejl");
			socketWait();
			return false;
		}
		
		// Kontroller at v�gten er ubelastet
		case 8:
			prevKeyState = KeyState.K2;
			isRM208 = true;
			initDisplay("Nuværende belastning:", "Tryk 'OK'", df.format(currentWeight) + "kg");
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			return true;
	
		// S�tter produktbatches status til "Under produktion"
		case 9:
			isRM208 = true;
			try {
				//Gem reference til den p�g�ldende produktbatch, setStatus og update
				pbdto = pdao.getProduktBatch(pb);
				pbdto.setStatus(1);
				pdao.updateProduktBatch(pbdto);
				initDisplay(message.getMessage(), "Klar til afvejning. Tryk 'OK'", "Klar");
			} catch (Exception e) {
				throw new DALException(e.getMessage());
			}
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			keyState = KeyState.K3;
			return true;
	
		// V�gten tareres
		case 10:
			prevKeyState = KeyState.K2;
			
			//Afvejning påbegyndt
			isTara = true;
			isWeighing = true;
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketHandler.sendMessage(new SocketOutMessage("RM A " + containerWeight + " kg\r\n"));
			keyState = KeyState.K3;
			return true;
	
		// V�gten beder om f�rste tara beholder og om at tarere v�gten
		case 11:
			prevKeyState = KeyState.K3;
			isTara = true;
			isRM208 = true;
			initDisplay("Vægten er tareret. Taravægt: " + containerWeight + "kg", "Placer beholder og tryk 'Tara'", df.format(currentWeight) + "kg");
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			keyState = KeyState.K1;
			return true;

		// R�varebatch nummer
		case 12:
			isRM208 = true;
			initDisplay(message.getMessage(), "", "Indtast");
			weightController.showMessageTernaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			keyState = KeyState.K4;
			return true;

	//R�varebatch / begynd afvejning
		case 13:
			prevKeyState = KeyState.K1;
			isRM208 = true;
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			
			try {
				rbRaavare = rbdao.getRaavareBatch(rb).getRaavareId();
				pbRecept = pdao.getProduktBatch(pb).getReceptId();
				
				// K�rer igennem produktbatchkomponenterne
				for (int i = 0; i < pkdao.getProduktBatchKompList(pb).size(); i++) {

					// Hvis det givne produktbatchkomponent ikke er behandlet (mangler brugerID)
					if (pkdao.getProduktBatchKompList(pb).get(i).getBrugerId() == 0) {
						System.out.println("Ingen bruger p� produktbatchkomp " + i);
						rkRaavare = rkdao.getReceptKompList(pbRecept).get(i).getRaavareId();
						rkNomNetto = rkdao.getReceptKompList(pbRecept).get(i).getNomNetto();
						rbNomNetto = rbdao.getRaavareBatch(rb).getMaengde();
						tolerance = rkdao.getReceptKompList(pbRecept).get(i).getTolerance();
						
						minWeight = rkNomNetto - (rkNomNetto * tolerance / 100);
						maxWeight = rkNomNetto + (rkNomNetto * tolerance / 100);

						// Hvis komponentens råvare = råvaren i batchet
						// og der er nok af den til recepten
						if (rkRaavare == rbRaavare && rkNomNetto < rbNomNetto) {
							String interval = "Godkendt vægtinterval: [" + minWeight + ", " + maxWeight + "]";
							initDisplay(message.getMessage(), interval, currentWeight + "kg");
							socketWait();
							keyState = KeyState.K2;
							isWeighing = false;
							return true;
						}
						else {
							inf--;
							keyState = prevKeyState;
							return false;
						}
					}
				}
			// Sidste case er ikke gyldig
			} catch (Exception e) {
				e.printStackTrace();
				inf--;
				return false;
			}

	//Check afvejning
	case 14:
		prevKeyState = KeyState.K4;
		isRM208 = true;
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		
		if(currentWeight> minWeight && currentWeight < maxWeight) {
			if(currentWeight > minWeight && currentWeight < maxWeight) {
				registeredWeight = currentWeight;
				double brutto = containerWeight + registeredWeight;
				double bruttoCheck = registeredWeight - brutto;
				if(bruttoCheck == (-containerWeight)); {
					initDisplay("Resultat: Tara " + containerWeight + " kg, " + "Nettovægt: "
					+ registeredWeight + " kg", "Netto- minus bruttovægt: " + df.format(bruttoCheck) + " kg", message.getMessage());;
					registeredWeight = registeredWeight * 1000;
					containerWeight = containerWeight * 1000;
					updatePbKomp();
					socketWait();
					keyState = KeyState.K2;
					checkPbStatus();
					btnLayout1[4] = "Annuller";
					return true;
				}
			}
		}
		else {
			registeredWeight = 0;
			keyState = prevKeyState;
			isWeighing = true;
			return false;
		}
	
	//Foretag ny afvejning?
	case 15:
		isFinished = true;
		isRM208 = true;
		initDisplay(message.getMessage(), "Vil du foretage en ny afvejning?", "");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		return true;
					
	//Afvej ny produktbatchkomponent?
	case 16:
		isRM208 = true;
		keyState = KeyState.K2;
		initDisplay(message.getMessage(), "Vil du foretage en ny afvejning?", "");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		return true;
	default:
		System.out.println("Default case. Returning false.");
		return false;
	}
}	
	//Beder socketThread om at vente til den får besked
	public void socketWait() {
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	//Vækker socketThread
	public void socketWake() {
		try {
			synchronized (socketHandler) {
				socketHandler.notify();
				isRM208 = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Gør programmet klart ved første notify() fra socket
	public void begin() {
		if (wtIterator == 2) {
			keyState = KeyState.K1;
			wtIterator++;
			pkdto = new ProduktBatchKompDTO();
		}
		else if(isFinished) {
			isFinished = false;
			inf = 2;
			wtIterator = 6;
			SocketController.iterator = 5;
			keyState = KeyState.K1;
			int[] reset1 = {rbRaavare, rkRaavare, pbRecept, pb, rb};
			double[] reset2 = {rkNomNetto, rbNomNetto, tolerance, containerWeight, currentWeight, registeredWeight};
			for(int i = 0; i < reset1.length; i++ ) {reset1[i] = 0;}
			for(int i = 0; i < reset2.length; i++ ) {reset2[i] = 0;}
			btnLayout0[4] = "";
			weightController.setSoftButtonTexts(btnLayout1);
			socketWake();
		}
	}
	
	public void checkPbStatus() {
		try {
			boolean pbdone = true;
			for (int i = 0; i < pkdao.getProduktBatchKompList(pb).size(); i++) {

			// Hvis det givne produktbatchkomponent ikke er behandlet (mangler brugerID)
				if (pkdao.getProduktBatchKompList(pb).get(i).getBrugerId() == 0) {
					pbdone = false;
					break;	
				}
		}
			if(pbdone) {
			pbdto.setStatus(2);
			pdao.updateProduktBatch(pbdto);
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void updatePbKomp() {
		pkdto.setBrugerId(lm);
		pkdto.setProduktBatchId(pb);
		pkdto.setTerminal(tm);
		pkdto.setTara(containerWeight);
		pkdto.setNetto(registeredWeight);
		pkdto.setRaavareBatchId(rb);
		System.out.println(pkdto.toString());
		try {
			System.out.println("Opdaterer produktbatchkomponent.");
		pkdao.updateProduktBatchKomp(pkdto);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	public void processSocketMessage(SocketInMessage message) {
	try {	
		/****Virker med KeyPress.Cancel();, der ikke blev implementeret korrekt****/
			//Hvis der er trykket annuller sidste gang
//			if(cancel) {
//				if(wtIterator != 3) {
//				wtIterator--;
//				cancel = false;
//				}
//			}
		
			//Er besked fra socket af typen T skal der ikke være blokerende kald
			//Går lige igennem trinnet i weightState
			switch (message.getType()) {
			case T:
				containerWeight = currentWeight;
				isTara = true;
			default:
				break;
			}
			
			//Vægtprocedure metode
			if (weightState(message, wtIterator)) {
				if (!isWeighing) {
					weightController.setSoftButtonTexts(btnLayout1);
				} else if (isWeighing) {
					weightController.setSoftButtonTexts(btnLayout0);
				}
				wtIterator++;
			} else {
				keyState = prevKeyState;
				wtIterator--;
				SocketController.iterator -= 2;
				weightController.showMessageSecondaryDisplay("Ikke gyldigt input. Pr�v igen.");
			}
		} catch (DALException ex) {
			ex.printStackTrace();
		}
	}
	
	/****Forbereder input og gemmer det, hvis forudsætningerne er der*****/
	public void prepInfo() {
		String temp = "";
		for(int i = 0; i < msCMD.length; i++) {
			if(msCMD[i] ==  0) {
				break;
			}
			else {
				temp += msCMD[i];
			}
		}
		try {
		if(!temp.isEmpty()) {
			tempInf[inf] = Integer.parseInt(temp);
			if(inf == 0) {
				tm = tempInf[inf];
			}
			else if(inf == 1) {
				lm = tempInf[inf];
			}
			else if(inf == 2) {
				pb = tempInf[inf];
			}
			else if(inf == 3) {
				rb = tempInf[inf];
			}
			inf++;
		}
		else {
			if(keyState.equals(KeyState.K1)) {
				inf++;
			}
			else {
			System.out.println("OK");
			}
		}
		}
		catch (Exception e) {
			if(!temp.isEmpty()) {
				inf++;
			}
		}
	}
	
	//Klargør beskeden til at blive sendt til cmd
	public String prepMessageCMD() {
		 
		msCMD[counter + 1] = '\r';
		msCMD[counter + 2] = '\n';
		String message = new String(msCMD);
		message.split("\n", 0);
		return message;
	}
	
	//Sender beskeden til cmd
	public void sendMessageCMD() {
		socketHandler.sendMessage(new SocketOutMessage(prepMessageCMD()));
	}
	
	//Sikrer, at der ikke er noget tilbage i beskeden
	public void flushMsCMD() {
		isWriting = false;
		weightController.showMessageSecondaryDisplay(null);
		counter = 0;

		for (int i = 0; i < msCMD.length; i++) {
			msCMD[i] = '\0';
		}
	}
	
	//Vis pågældende beskeder på displayet
	public void initDisplay(String s1, String s2, String s3) {
			this.cd = new String[] {s1, s2, s3};
			weightController.showMessageTernaryDisplay(cd[0]);
			weightController.showMessageSecondaryDisplay(cd[1]);
			weightController.showMessagePrimaryDisplay(cd[2]);
	}
	
	//Giver mulighed for at skifte mellem softbutton tekst og tekst i GUIens displays
	public void currentDisplay(int softBtn, int display) {
		
			switch(display) {
			case 0:
				for(int i = 0; i < 6; i++) {
					if (softBtn == i) {
						if(softBtns[i]) {
							weightController.showMessageTernaryDisplay(cd[display]);
							softBtns[i] = false;
						}
					}
				}
				break;
			case 1:
				
				for(int i = 0; i < 6; i++) {
					if (softBtn == i) {
						if(softBtns[i]) {
							weightController.showMessageSecondaryDisplay(cd[display]);
							softBtns[i] = false;
						}
					}
				}	
				break;
			case 2:
				for(int i = 0; i < 6; i++) {
					if (softBtn == i) {
						if(softBtns[i]) {
							weightController.showMessagePrimaryDisplay(cd[display]);
							softBtns[i] = false;
						}
					}
				}
				break;
			default:
				break;
			}
		}
	
	//Skifter softbtn layout hvis der bliver trykket på dem
	public void switchBtnLayout(int softBtn, String text) {
		
		if(softBtns[softBtn]) {
			btnLayout0[softBtn] = "Tilbage";
			weightController.setSoftButtonTexts(btnLayout0);
		}
		else if (!softBtns[softBtn]) {
			btnLayout0[softBtn] = text;
			weightController.setSoftButtonTexts(btnLayout0);
		}
	}
	
	//Ryder GUIens displays
	public void flushDisplays() {
		weightController.showMessageTernaryDisplay("");
		weightController.showMessagePrimaryDisplay("");
		weightController.showMessageSecondaryDisplay("");
	}
	
}
