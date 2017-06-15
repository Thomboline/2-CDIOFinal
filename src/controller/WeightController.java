package controller;

import java.awt.List;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

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
import dto.ReceptDTO;
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
	private boolean isWriting, isTara, isRM208, isWeighing, isFinished, cancel;
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

			/*************************TEST DATA*************************/
			
			try {
				
				for(int i = 0; i < pkdao.getProduktBatchKompList().size(); i++) {
					System.out.println(pkdao.getProduktBatchKompList(i));
				}
				
				System.out.println("Users: " + bdao.getBrugerList());
				System.out.println("Produktbatches: " + pdao.getProduktBatchList());
				System.out.println("Tilt�nkt pbId = 2: " + pdao.getProduktBatch(2));
				System.out.println("Pb status: " + pdao.getProduktBatch(2).getStatus());
				System.out.println("R�varebatches: " + rbdao.getRaavareBatchList());
				System.out.println("Recepter: " + recdao.getReceptList());

				String rkomp = "{ ";
				for (int i = 0; i < rkdao.getReceptKompList(1).size(); i++) {
					if (!rkdao.getReceptKompList(1).isEmpty()) {
						rkomp += rkdao.getReceptKompList(1).get(i);
						if (i > 0)
							rkomp += ", ";
					}
					rkomp += "}";
				}
				
//				int rbRaavare = rbdao.getRaavareBatch(2).getRaavareId();
//				int rkRaavare = rkdao.getReceptKompList(3).get(0).getRaavareId();
//				double rkNomNetto = rkdao.getReceptKompList(3).get(0).getNomNetto();
//				double rbNomNetto = rbdao.getRaavareBatchList(2).get(0).getMaengde();
//
//				// Hvis den kr�vede r�vare er den samme r�vare som i batchet
//				// og der er nok af den
//				if (rkRaavare == rbRaavare && rkNomNetto < rbNomNetto) {
//					System.out.println("Hul");
//					System.out.println("rbRaavare: " + rbRaavare);
//					System.out.println("rkRaavare: " + rkRaavare);
//					System.out.println("rkNomNetto: " + rkNomNetto);
//					System.out.println("rbNomNetto: " + rbNomNetto);
//					System.out.println("Recept id til pb = 2: " + pdao.getProduktBatch(2).getProduktBatchId());
//
//					if (pkdao.getProduktBatchKompList(2).get(0).getBrugerId() == 0) {
//						System.out.println("Ingen bruger verificeret.");
//					}
//					if (3 == pdao.getProduktBatch(2).getReceptId()) {
//						System.out.println("\nSUCCESS");
//					}
//				}

			} catch (Exception e) {
				e.printStackTrace();
			}

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
						System.out.println("Notify tara");
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
						System.out.println("Registered weight: " + currentWeight);
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
					begin();
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
		System.out.println("Performing case " + wtIterator);
		isRM208 = true;
		initDisplay(message.getMessage(), "", "");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		return true;
	
	//Vælg laborant nummer	
	case 4:
		System.out.println("Performing case " + wtIterator);
		isRM208 = true;
		prevKeyState = KeyState.K1;
		
		//Tjek om sidste case er gyldig
		if(tm < 1 || tm > 4 || tm == '0') {
			inf--;
			return false;
		}
		initDisplay(message.getMessage(), "", "");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		keyState = KeyState.K2;
		return true;
		
	//Svar med Laborantens initialer
	case 5:
		System.out.println("Performing case " + wtIterator);
		System.out.println("lm = " + lm);
		prevKeyState = KeyState.K1;
		isRM208 = true;
		try {
		System.out.println("Displaying initials of user " + bdao.getBruger(lm));
		initDisplay(message.getMessage() + bdao.getBruger(lm).getBrugerNavn() + ", " + bdao.getBruger(lm).getIni(), "Tryk 'OK'", "Velkommen");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		}
		//Sidste case er ikke gyldig
		catch (Exception e) {
				inf--;
				System.out.println("inf skal v�re 1 nu, men den er: " + inf);
				keyState = prevKeyState;
				return false;
		}
		keyState = KeyState.K1;
		return true;
	
	//Indtast produktbatchnummer
	case 6:
		System.out.println("Performing case " + wtIterator);
		prevKeyState = KeyState.K2;
		isRM208 = true;
		initDisplay(message.getMessage(), "", "");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		keyState = KeyState.K2;
		return true;
	
	//Svar med recept
	case 7:
		System.out.println("Performing case " + wtIterator);
		prevKeyState = KeyState.K1;
		isRM208 = true;
		
		//TEST
		try {
			if(recdao.getRecept(1).getReceptId() == pdao.getProduktBatch(2).getReceptId()) {
		System.out.println("\nSUCCESS: Produktbatch: " + pdao.getProduktBatch(pb) + "\n");
			}
		}
		catch(Exception e) {
			System.out.println("Couldn't retrieve produktbatch.");
		}
		
		//REAL
		try {
			pbRecept = pdao.getProduktBatch(pb).getReceptId();
			
			// K�rer igennem produktbatchkomponenterne
			for (int i = 0; i < pkdao.getProduktBatchKompList(pb).size(); i++) {

				// Hvis det givne produktbatchkomponent ikke er behandlet (mangler brugerID)
				if (pkdao.getProduktBatchKompList(pb).get(i).getBrugerId() == 0) {
					
					System.out.println("Ingen bruger p� produktbatchkomp " + i);
					rkRaavare = rkdao.getReceptKompList(pbRecept).get(i).getRaavareId();
					rkNomNetto = rkdao.getReceptKompList(pbRecept).get(i).getNomNetto();
//					rbNomNetto = rbdao.getRaavareBatchList(rb).get(i).getMaengde();
					tolerance = rkdao.getReceptKompList(pbRecept).get(i).getTolerance();
					
					minWeight = rkNomNetto - (rkNomNetto * tolerance / 100);
					maxWeight = rkNomNetto + (rkNomNetto * tolerance / 100);
					System.out.println("Råvare id: " + rkRaavare);
					initDisplay(message.getMessage() + " " + recdao.getRecept(pdao.getProduktBatch(pb).getReceptId()).getReceptNavn(),
						"Du skal afveje ca. " + rkNomNetto + " kg " + rdao.getRaavare(rkRaavare).getRaavareNavn(), "");
					socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
					
					socketWait();
					keyState = KeyState.K2;
					return true;
					}
			}
			initDisplay(message.getMessage(), "Produktbatch færdigproduceret. Vælg et andet.", "Fejl");
			inf--;
			socketWait();
			return false;
			}
		//Sidste case er ikke gyldig
		catch (Exception e) {
			System.out.println("Her fejler det");
			inf--;
			initDisplay(message.getMessage(), "Eksisterer ikke. Prøv et andet.", "Fejl");
			socketWait();
			return false;
		}
		
		// Kontroller at v�gten er ubelastet
		case 8:
			System.out.println("Performing case " + wtIterator);
			prevKeyState = KeyState.K2;
			isRM208 = true;
			initDisplay("Nuværende belastning:", "Tryk 'OK'", df.format(currentWeight) + "kg");
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			return true;
	
		// S�tter produktbatches status til "Under produktion"
		case 9:
			System.out.println("Performing case " + wtIterator);
			isRM208 = true;
			try {
				//Gem reference til den p�g�ldende produktbatch, setStatus og update
				ProduktBatchDTO pbdto = pdao.getProduktBatch(pb);
				pbdto.setStatus(1);
				pdao.updateProduktBatch(pbdto);
				initDisplay(message.getMessage(), "Klar til afvejning", "Klar");
				System.out.println("Produktbatch status: " + pdao.getProduktBatch(pb).getStatus());
			} catch (Exception e) {
				throw new DALException(e.getMessage());
			}
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			keyState = KeyState.K3;
			return true;
	
		// V�gten tareres
		case 10:
			System.out.println("Performing case " + wtIterator);
			System.out.println("Vægten er tareret.");
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
			System.out.println("Performing case " + wtIterator);
			initDisplay("Vægten er tareret. Taravægt: " + containerWeight, "Placer beholder og tryk 'Tara'", df.format(currentWeight) + "kg");
			weightController.showMessageTernaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			keyState = KeyState.K1;
			return true;

		// R�varebatch nummer
		case 12:
			prevKeyState = KeyState.K3;
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
			initDisplay(message.getMessage(), "", "Vælg batch");
			weightController.showMessageTernaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			socketWait();
			keyState = KeyState.K4;
			return true;

	//R�varebatch / begynd afvejning
		case 13:
			prevKeyState = KeyState.K1;
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
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
							System.out.println("R�vare og m�ngde tjek done");
//							pkdto.setProduktBatchId(rb);
//							pkdto.setBrugerId(lm);
//							pkdao.getProduktBatchKompList(pb).get(i).setRaavareBatchId(rb);
//							pkdao.getProduktBatchKompList(pb).get(i).setBrugerId(lm);
							String interval = "Godkendt vægtinterval: [" + minWeight + ", " + maxWeight + "]";
							initDisplay(message.getMessage(), interval, "");
							System.out.println("Containerweight: " + containerWeight);
							socketWait();
							keyState = KeyState.K2;
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
				System.out.println("Fik exception i case 13.");
				inf--;
				return false;
			}

	//Check afvejning
	case 14:
		prevKeyState = KeyState.K4;
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
//		weightController.showMessageTernaryDisplay(message.getMessage());
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		
		if(currentWeight> minWeight && currentWeight < maxWeight) {
			if(currentWeight > minWeight && currentWeight < maxWeight) {
				registeredWeight = currentWeight;
				double brutto = containerWeight + registeredWeight;
				double bruttoCheck = registeredWeight - brutto;
				if(bruttoCheck == (-containerWeight)); {
					System.out.println("Forventet check: " + (-containerWeight));
					System.out.println("Egentligt check: " + bruttoCheck);
				initDisplay("Resultat: Tara " + containerWeight + " kg, " + "Nettovægt: "
					+ registeredWeight + " kg", "Netto- minus bruttovægt: " + df.format(bruttoCheck) + " kg", message.getMessage());;
					registeredWeight = registeredWeight * 1000;
					containerWeight = containerWeight * 1000;
					updatePbKomp();
				}
			}
		socketWait();
		keyState = KeyState.K2;
		isWeighing = false;
		return true;
		}
		else {
			System.out.println("Vægt ikke godkendt");
			registeredWeight = 0;
			keyState = prevKeyState;
			return false;
			
		}
	
	//Check afvejning
	case 15:
		System.out.println("Performing case " + wtIterator);
		isFinished = true;
		isRM208 = true;
		initDisplay(message.getMessage(), "Vil du foretage en ny afvejning?", "");
		btnLayout0[4] = "Annuller";
		weightController.setSoftButtonTexts(btnLayout0);
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		socketWait();
		return true;
		
//		prevKeyState = KeyState.K4;
//		isRM208 = true;
//		System.out.println("Performing case " + wtIterator);
//		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
//		
//			if(currentWeight > minWeight && currentWeight < maxWeight) {
//				registeredWeight = currentWeight;
//				double brutto = containerWeight + registeredWeight;
//				double bruttoCheck = registeredWeight - brutto;
//				if(bruttoCheck == (-containerWeight)); {
//					System.out.println("Forventet check: " + -containerWeight);
//					System.out.println("Egentligt check: " + bruttoCheck);
//				initDisplay("Resultat: Tara " + containerWeight + " kg, " + "Nettovægt: " + registeredWeight + " kg", "Netto- minus bruttovægt: " + df.format(bruttoCheck) + " kg", message.getMessage());
////				weightController.showMessageTernaryDisplay(message.getMessage());
////				weightController.showMessageSecondaryDisplay("Tara: " + containerWeight + " nomNetto: " + registeredWeight);
//				isFinished = true;
//				btnLayout0[4] = "Annuller";
//				weightController.setSoftButtonTexts(btnLayout0);
//				socketWait();
//				return true;
//				}
//			}
//			else {
//				keyState = prevKeyState;
//				containerWeight = 0;
//				registeredWeight = 0;
//				currentWeight = 0;
//				return false;
//			}
			// TODO Registrer alt i databasen, S�FREMT ALLE KRITERIER ER OPFYLDT.
			// Der skal tjekkes bruttov�gt og laves rollback hvis v�gten ikke er i intervallet. Implementer else return false
			// Der skal registreres slutdato
			// Der mangler ogs� at blive registreret startdato
			// Alle attributer skal gemmes i databasen p� de rigtige tidspunkter
			// Der skal tjekkes for om det producerede produktbatchkomponent er det sidste i produktbatchet
			// Status skal �ndres, hvis det er
			// Laboranten skal sp�rges om den vil afveje endnu et komponent - rollback til trin 6, inf-2
					
	//Afvej ny produktbatchkomponent?
	case 16:
		System.out.println("Performing case " + wtIterator);
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
			SocketController.iterator = 6;
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
			//Hvis der er trykket annuller sidste gang
			if(cancel) {
				if(wtIterator != 3) {
				wtIterator--;
				cancel = false;
				}
			}
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
			System.out.println("msCMD string split: " + temp);
			tempInf[inf] = Integer.parseInt(temp);
			System.out.println("TempInf: " + tempInf[inf]);
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
			System.out.println("tm = " + tm + ", lm = " + lm + ", pb = " + pb + ", rb = " + rb);
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
			System.out.println("tm = " + tm + ", lm = " + lm + ", pb = " + pb + ", rb = " + rb);
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

								//H�rer til nederst i notify()
//if (isRM208) {
//
//} else {
//	switch (message.getType()) {
//	case B:
//		double newWeight = Double.parseDouble(message.getMessage());
//		socketHandler.sendMessage(new SocketOutMessage("DB"));
//		notifyWeightChange(newWeight);
//		break;
//	case D:
//		weightController.showMessagePrimaryDisplay(message.getMessage());
//		socketHandler.sendMessage(new SocketOutMessage("D A"));
//		break;
//	case Q:
//		weightController.unRegisterObserver(this);
//		socketHandler.unRegisterObserver(this);
//		System.exit(0);
//		break;
//	case RM204:
//		// Not specified
//		break;
//	case RM208:
//		isRM208 = true;
//		weightController.showMessageTernaryDisplay(message.getMessage());
//		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
//		try {
//			synchronized (socketHandler) {
//				socketHandler.wait();
//			}
//		} catch (InterruptedException ex) {
//			ex.printStackTrace();
//		}
//		break;
//	case S:
//		socketHandler.sendMessage(new SocketOutMessage("S S " + this.currentWeight + "\r\n"));
//		break;
//	case T:
//		this.containerWeight += currentWeight;
//		notifyWeightChange(0);
//		weightController.showMessageSecondaryDisplay("Weight of tara: " + containerWeight + "kg");
//		socketHandler.sendMessage(new SocketOutMessage("T S " + this.containerWeight + "kg \r\n"));
//		break;
//	case DW:
//		resetWeightChange();
//		weightController.showMessageSecondaryDisplay(null);
//		weightController.showMessageTernaryDisplay(null);
//		weightController.showMessagePrimaryDisplay(df.format(this.currentWeight) + "kg");
//		socketHandler.sendMessage(new SocketOutMessage("DW A\r\n"));
//		break;
//	case K:
//		handleKMessage(message);
//		socketHandler.sendMessage(new SocketOutMessage("K A\r\n"));
//		break;
//	case P111:
//		String upToNCharacters = message.getMessage().substring(0, Math.min(message.getMessage().length(), 30));
//		weightController.showMessageSecondaryDisplay(upToNCharacters);
//		socketHandler.sendMessage(new SocketOutMessage("P111 A \r\n"));
//		break;
//	case DE:
//	default:
//	socketHandler.sendMessage(new SocketOutMessage("ES \r\n"
//			+ "Wrong input, please use following commands:\n S\r\n"
//			+ "T\r\n"
//			+ "D\r\n"
//			+ "DW\r\n"
//			+ "P111\r\n"
//			+ "RM20 8\r\n"
//			+ "K\r\n"
//			+ "B\r\n"
//			+ "Q\r\n"));
//}
//}
