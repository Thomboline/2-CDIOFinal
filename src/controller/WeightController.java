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
	private boolean isWriting, isTara, isRM208;
	private boolean softBtn0 = false, softBtn1 = false, softBtn2 = false,
					softBtn3 = false, softBtn4 = false, softBtn5 = false;
	private boolean softBtns[] = {softBtn0, softBtn1, softBtn2, softBtn3, softBtn4, softBtn5};
	private String btnLayout[] = {"", "Hey!", "Taravægt", "Nettovægt", "", ""};

	//Iterator til afvejningsprocedure og input, simple variable til kritisk
	//input fra brugeren og container dertil.
	private int wtIterator = 2;
	private int[] tempInf = new int[4];
	private int tm, lm, pb, rb;
	private int inf = 0;
	
	//Formatering af vægt i primary display - viser ellers 8 decimaler
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
		if (socketHandler!=null && weightController!=null){
			
			//Makes this controller interested in messages from the socket
			socketHandler.registerObserver(this);
			
			//Starts socketHandler & weightController in own threads
			new Thread(socketHandler).start();
			new Thread(weightController).start();
			weightController.registerObserver(this);
			
			//Test info
			try {
				
				System.out.println("Users: " + bdao.getBrugerList());
				System.out.println("Produktbatches: " + pdao.getProduktBatchList());
				System.out.println("Tilt�nkt pbId = 2: " + pdao.getProduktBatch(2));
				System.out.println("Pb status: " + pdao.getProduktBatch(2).getStatus());
				System.out.println("R�varebatches: " + rbdao.getRaavareBatchList());
				System.out.println("Recepter: " + recdao.getReceptList());
				
//				System.out.println("R�varebatch kompatibel: " + rdao.getRaavare(pkdao.getProduktBatchKomp(3, 2).getRaavareBatchId()).getRaavareNavn());
				String rkomp = "{ ";
				for(int i = 0; i < rkdao.getReceptKompList(1).size(); i++) {
					if(!rkdao.getReceptKompList(1).isEmpty()) {
						rkomp += rkdao.getReceptKompList(1).get(i);
						if(i > 0)
							rkomp += ", ";
					}
					rkomp += "}";
				}
				System.out.println("Receptkomponenter, Salmon: " + rkomp);
				System.out.println(rdao.getRaavare(rkdao.getReceptKompList(3).get(0).getRaavareId()).getRaavareNavn());
				
				int rbRaavare = rbdao.getRaavareBatch(2).getRaavareId();
				int rkRaavare = rkdao.getReceptKompList(3).get(0).getRaavareId();
				double rkNomNetto = rkdao.getReceptKompList(3).get(0).getNomNetto();
				double rbNomNetto = rbdao.getRaavareBatchList(2).get(0).getMaengde();
				
				//Hvis den kr�vede r�vare er den samme r�vare som i batchet 
				//og der er nok af den
				if (rkRaavare == rbRaavare && rkNomNetto < rbNomNetto) {
					System.out.println("Hul");
					System.out.println("rbRaavare: " + rbRaavare);
					System.out.println("rkRaavare: " + rkRaavare);
					System.out.println("rkNomNetto: " + rkNomNetto);
					System.out.println("rbNomNetto: " + rbNomNetto);
					System.out.println("Recept id til pb = 2: " + pdao.getProduktBatch(2).getProduktBatchId());
					
					if(pkdao.getProduktBatchKompList(2).get(0).getBrugerId() == 0) {
						System.out.println("Ingen bruger verificeret.");
					}
					if(3 == pdao.getProduktBatch(2).getReceptId()) {
						System.out.println("\nSUCCESS");
							}
				}
				
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
		
		if(wtIterator == 2) {
			keyState = KeyState.K1;
			wtIterator++;
		}
		if(this.keyState.equals(KeyState.K2)) {
			String[] accept = {"", "", "", "", "Cancel", "OK"};
			weightController.setSoftButtonTexts(accept);
		}
		
		try {
		if(weightState(message, this.wtIterator)) {
			this.wtIterator++;
		}
		else {
//			SocketController.proceed = false;
			keyState = prevKeyState;
			wtIterator--;
			SocketController.test -= 2;
			weightController.showMessageSecondaryDisplay("Ikke gyldigt input. Pr�v igen.");
			System.out.println("tm = " + tm + ", lm = " + lm + ", pb = " + pb + ", rb = " + rb);
			System.out.println("inf: " + inf);
		}
		
		}
		catch (DALException ex) {
			ex.printStackTrace();
		}
		}
	
	//Listening for UI input
	@Override
	public void notifyKeyPress(KeyPress keyPress){
		System.out.println(keyPress.getCharacter());

		String[] tara = new String[] { "", "", "Reset Stored-weight", "Show Stored-weight", "", "Cancel" };
		String[] text = new String[] { "Backspace", "", "", "", "", "" };
		String[] zero = new String[] { "", "", "", "", "Change Batch-id", "Logout" };
		String[] empty = new String[] { "", "", "", "", "", "" };
		
		try {
		switch (keyPress.getType()) {
		case SOFTBUTTON:
			
			
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
				//virker
//				if(!softBtn0) {
//					weightController.showMessagePrimaryDisplay("Hej!");
//					softBtn0 = true;
//				}
//				else if (softBtn0) {
//					weightController.showMessagePrimaryDisplay("Hej igen");
//					softBtn0 = false;
//				}
				
				//virker ikke

				
				if(softBtns[1]) {
					currentDisplay(1, 2);
//					btnLayout[1] = "Tilbage";
//					weightController.setSoftButtonTexts(btnLayout);
					switchBtnLayout(1, "Hey!");
				}
				else if(!softBtns[1])
				{
				weightController.showMessagePrimaryDisplay("Hey!");	
				softBtns[1] = true;
//				btnLayout[1] = cd[1];
//				weightController.setSoftButtonTexts(btnLayout);
				switchBtnLayout(1, "Tilbage");
				
				
				}
			}
			
			if (keyPress.getKeyNumber() == 2) {
				if(softBtns[2]) {
					currentDisplay(2, 1);
					switchBtnLayout(2, "Taravægt");
				}
				else if(!softBtns[2])
				{
				softBtns[2] = true;
				weightController.showMessageSecondaryDisplay("Tara weight: " + containerWeight);
				switchBtnLayout(2, "Tilbage");
				}
	
//				this.containerWeight = 0.000;
			}
			if (keyPress.getKeyNumber() == 3) {
				if(softBtns[3]) {
					currentDisplay(3, 0);
					switchBtnLayout(3, "Nettovægt");
				}
				else if(!softBtns[3])
				{
				softBtns[3] = true;
				weightController.showMessageTernaryDisplay("Current weight: " + currentWeight + " kg");
				switchBtnLayout(3, "Tilbage");
				}
			}
			if (keyPress.getKeyNumber() == 4) {
				// Batch-id funktion
				softBtns[4] = true;
			}
			if (keyPress.getKeyNumber() == 5) {
				// Batch-id funktion
				softBtns[5] = true;
			}

			
//			//177-183 VIRKER IKKE
//			if (keyState.equals(KeyState.K2)) {
//				if (keyPress.getKeyNumber() == 5) {
//					notifyKeyPress(KeyPress.Cancel());
//					}
//				else if (keyPress.getKeyNumber() == 6) {
//					notifyKeyPress(KeyPress.Send());
//				}
//			}
//			else {s
//				if (keyPress.getKeyNumber() == 5) {
//					// Log in & out funktion
//				}
//			}
			break;
		case TARA:
//			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3)) {
//				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
//			}
			if(keyState.equals(KeyState.K3)) {
//			weightController.setSoftButtonTexts(tara);
			this.containerWeight += this.currentWeight;
			notifyWeightChange(0);
			
			if (isRM208) {
				try {
					synchronized (socketHandler) {
						socketHandler.notify();
						isRM208 = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}
			
			break;
		case TEXT:
			if(keyState.equals(KeyState.K1)) {

			if (isTara) {
				tara[0] = "Backspace";
				weightController.setSoftButtonTexts(tara);
			} else {
				weightController.setSoftButtonTexts(text);
			}

			isWriting = true;
			msCMD[counter] = keyPress.getCharacter();
			String temp = new String(msCMD);
			weightController.showMessageSecondaryDisplay(temp);
			counter++;
			}
			else {}
			break; 
		case ZERO:
//			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3)) {
//				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
//			}
//			if (keyState.equals(KeyState.K1) || keyState.equals(KeyState.K4)) {
			if(keyState.equals(KeyState.K3)) {
				isTara = false;
				weightController.setSoftButtonTexts(zero);
				containerWeight = 0.000;
				notifyWeightChange(0);
				flushMsCMD();
			}
			break;
		case CANCEL:
			isTara = false;
			if(this.keyState.equals(KeyState.K2)) {
				wtIterator--;
				SocketController.test--;
			} 
			else {
			weightController.setSoftButtonTexts(empty);
			weightController.showMessageSecondaryDisplay(null);
			flushMsCMD();
			resetButtonTexts(tara, zero, empty);
			}
			break;
		case EXIT:
			weightController.unRegisterObserver(this);
			socketHandler.unRegisterObserver(this);
			System.exit(0);
			break;
		case SEND:
//			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3)) {
//				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
//			}
//			if (keyState.equals(KeyState.K1) || keyState.equals(KeyState.K4)) {
			if (keyState.equals(KeyState.K1) && msCMD[0] != 0) {

				if (isTara) {
					weightController.setSoftButtonTexts(tara);
				} else {
					weightController.setSoftButtonTexts(empty);
				}
				
				//Stores input in weight info container
				prepInfo();
				//Prepares message and sends a new String to CMD
				sendMessageCMD();
				//Flush msCMD char array
				flushMsCMD();
				//Resets all soft button descriptions
				resetButtonTexts(tara, zero, empty);
				
				if (isRM208) {
					try {
						synchronized (socketHandler) {
							socketHandler.notify();
							isRM208 = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				socketHandler.sendMessage(new SocketOutMessage("RM A "));
			}
			
			if(keyState.equals(KeyState.K2) || keyState.equals(KeyState.K4)) {
				
				//Det er ikke muligt at skrive i keystate 2, men flush for en sikkerheds skyld
				flushMsCMD();
				
				if(keyState.equals(KeyState.K4)) {
					System.out.println("Registered weight: " + currentWeight);
				}
					try {
						synchronized (socketHandler) {
							socketHandler.notify();
							isRM208 = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				socketHandler.sendMessage(new SocketOutMessage("RM A "));
				socketHandler.sendMessage(new SocketOutMessage("OK"));
			}
		
			break;
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

//	public String resetWeightChange() {
//		this.currentWeight = 0.000;
//		String weight = this.currentWeight + "kg";
//		return weight;
//	}
	
	
	//Afl�st milj� til afvejningsprocedure
public boolean weightState(SocketInMessage message, int wtIterator) throws DALException {
	//Gem beskeden i tilf�lde af fejl i send()
//	prevMessage = message;
	
	//Brug 'tempInf' til at gemme midlertidige variable
	//[0] til terminal nummer
	//[1] til laborant nummer
	//[2] til r�varebatch nummer
	
	//Switch-casen starter p� 3 s� cases svarer til de egentlige trin i afvejningsproceduren
	
	switch (wtIterator) {
	
	//V�lg afvejningsterminal
	case 3:
		System.out.println("Performing case " + wtIterator);
		isRM208 = true;
		initDisplay(message.getMessage(), "Test", "Test");
//		weightController.showMessagePrimaryDisplay("Test");
//		weightController.showMessageSecondaryDisplay("Test");
//		weightController.showMessageTernaryDisplay(message.getMessage());
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		flushDislays();
		return true;
	
	//Laborant nummer	
	case 4:
		isRM208 = true;
		
		//Tjek om sidste case er gyldig
		if(tm < 1 || tm > 4 || tm == '0') {
			inf--;
			return false;
		}
		System.out.println("Performing case " + wtIterator);
		weightController.showMessageTernaryDisplay(message.getMessage());
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		keyState = KeyState.K2;
		return true;
		
	//Svar med Laborantens initialer
	case 5:
		prevKeyState = KeyState.K1;
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		System.out.println("lm = " + lm);
		try {
		System.out.println("Displaying initials of user " + bdao.getBruger(lm));
		weightController.showMessageTernaryDisplay(message.getMessage() + bdao.getBruger(lm).getIni());
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			try {
				synchronized (socketHandler) {
				socketHandler.wait();
				}
			} catch (InterruptedException ex) {
			ex.printStackTrace();
			}
		}
		catch (Exception e) {
			//Sidste case er ikke gyldig
				inf--;
				System.out.println("inf skal v�re 1 nu, men den er: " + inf);
				keyState = prevKeyState;
				return false;
		}
		keyState = KeyState.K1;
		return true;
	
	//Indtast produktbatchnummer
	case 6:
		prevKeyState = KeyState.K2;
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		weightController.showMessageTernaryDisplay(message.getMessage());
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		keyState = KeyState.K2;
		return true;
	
	//Svar med recept
	case 7:
		prevKeyState = KeyState.K1;
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		
		//TEST
		try {
			if(recdao.getRecept(3).getReceptId() == pdao.getProduktBatch(2).getReceptId()) {
		System.out.println("\nSUCCESS: Produktbatch: " + pdao.getProduktBatch(pb) + "\n");
			}
		}
		catch(Exception e) {
			System.out.println("Couldn't retrieve produktbatch.");
		}
		
		//REAL
		try {
			weightController.showMessageTernaryDisplay(message.getMessage() + " " + recdao.getRecept(pdao.getProduktBatch(pb).getReceptId()).getReceptNavn());
//			weightController.showMessageSecondaryDisplay("Ingredienser: " + rdao.getRaavare(rkdao.getReceptKompList(pdao.getProduktBatch(pb).getReceptId()).get(0).getRaavareId()).getRaavareNavn());
//  		weightController.showMessageSecondaryDisplay(rkdao.getReceptKompList(recdao.getRecept(pdao.getProduktBatch(pb).getReceptId()).getReceptId()).toString());
			try {
				synchronized (socketHandler) {
					socketHandler.wait();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		catch (Exception e) {
			//Sidste case er ikke gyldig
			System.out.println("Her fejler det");
			inf--;
			keyState = prevKeyState;
			return false;
	
		}
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		keyState = KeyState.K2;
		return true;
	
		// Kontroller at v�gten er ubelastet
		case 8:
			prevKeyState = KeyState.K2;
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
			weightController.showMessageTernaryDisplay(message.getMessage());
			weightController.showMessagePrimaryDisplay(df.format(currentWeight) + "kg");
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			try {
				synchronized (socketHandler) {
					socketHandler.wait();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			return true;
	
		// S�tter produktbatches status til "Under produktion"
		case 9:
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
			weightController.showMessageTernaryDisplay(message.getMessage());
			try {
				//Gem reference til den p�g�ldende produktbatch, setStatus og update
				ProduktBatchDTO pbdto = pdao.getProduktBatch(pb);
				pbdto.setStatus(1);
				pdao.updateProduktBatch(pbdto);
				weightController
						.showMessageSecondaryDisplay("Pb " + pb + " status: " + pdao.getProduktBatch(pb).getStatus());
				System.out.println("Produktbatch status: " + pdao.getProduktBatch(pb).getStatus());
			} catch (Exception e) {
				throw new DALException(e.getMessage());
			}
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			try {
				synchronized (socketHandler) {
					socketHandler.wait();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			keyState = KeyState.K3;
			return true;
	
		// V�gten tareres
		case 10:
			prevKeyState = KeyState.K2;
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
			weightController.showMessageTernaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			try {
				synchronized (socketHandler) {
					socketHandler.wait();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			return true;
	
		// V�gten beder om f�rste tara beholder og om at tarere v�gten
		case 11:
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
			weightController.showMessageTernaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			try {
				synchronized (socketHandler) {
					socketHandler.wait();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			keyState = KeyState.K1;
			return true;

		// R�varebatch nummer
		case 12:
			prevKeyState = KeyState.K3;
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
			weightController.showMessageTernaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			try {
				synchronized (socketHandler) {
					socketHandler.wait();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			keyState = KeyState.K2;
			return true;

	//R�varebatch / begynd afvejning
		case 13:
			prevKeyState = KeyState.K1;
			isRM208 = true;
			System.out.println("Performing case " + wtIterator);
			socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
			
			try {
				int rbRaavare = rbdao.getRaavareBatch(rb).getRaavareId();
				int pbRecept = pdao.getProduktBatch(pb).getReceptId();
				
				// K�rer igennem produktbatchkomponentere
				for (int i = 0; i < pkdao.getProduktBatchKompList(pb).size(); i++) {

					// Hvis det givne produktbatch ikke er behandlet (mangler brugerID)
					if (pkdao.getProduktBatchKompList(pb).get(i).getBrugerId() == 0) {
						System.out.println("Ingen bruger p� produktbatchkomp " + i);
						int rkRaavare = rkdao.getReceptKompList(pbRecept).get(i).getRaavareId();
						double rkNomNetto = rkdao.getReceptKompList(pbRecept).get(i).getNomNetto();
						double rbNomNetto = rbdao.getRaavareBatchList(rb).get(i).getMaengde();
						double tolerance = rkdao.getReceptKompList(pbRecept).get(i).getTolerance();
						
						minWeight = rkNomNetto - (rkNomNetto * tolerance / 100);
						maxWeight = rkNomNetto + (rkNomNetto * tolerance / 100);

						// Hvis den kr�vede r�vare er den samme r�vare som i batchet
						// og der er nok af den til recepten
						if (rkRaavare == rbRaavare && rkNomNetto < rbNomNetto) {
							System.out.println("R�vare og m�ngde tjek done");
							pkdao.getProduktBatchKompList(pb).get(i).setRaavareBatchId(rb);
							pkdao.getProduktBatchKompList(pb).get(i).setBrugerId(lm);
							
							weightController.showMessageTernaryDisplay(message.getMessage());
							weightController.showMessageSecondaryDisplay("Accepted interval: [" + minWeight + ", " + maxWeight + "]");
							System.out.println("Containerweight: " + containerWeight);

							try {
								synchronized (socketHandler) {
									socketHandler.wait();
								}
							} catch (InterruptedException ex) {
								ex.printStackTrace();
							}
							keyState = KeyState.K4;
							return true;
						}
					}
				}

			} catch (Exception e) {
				// Sidste case er ikke gyldig
				e.printStackTrace();
				System.out.println("Fik exception i case 13.");
				inf--;
				return false;
			}

	//Afvejning
	case 14:
		prevKeyState = KeyState.K2;
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		weightController.showMessageTernaryDisplay(message.getMessage());
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		keyState = KeyState.K2;
		return true;
	
	//Check afvejning
	case 15:
		prevKeyState = KeyState.K4;
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		
			if(currentWeight > minWeight && currentWeight < maxWeight) {
				registeredWeight = currentWeight;
				weightController.showMessageTernaryDisplay(message.getMessage());
				weightController.showMessageSecondaryDisplay("Tara: " + containerWeight + " nomNetto: " + registeredWeight);
			}
			// TODO Registrer alt i databasen, S�FREMT ALLE KRITERIER ER OPFYLDT.
			// Der skal tjekkes bruttov�gt og laves rollback hvis v�gten ikke er i intervallet. Implementer else return false
			// Der skal registreres slutdato
			// Der mangler ogs� at blive registreret startdato
			// Alle attributer skal gemmes i databasen p� de rigtige tidspunkter
			// Der skal tjekkes for om det producerede produktbatchkomponent er det sidste i produktbatchet
			// Status skal �ndres, hvis det er
			// Laboranten skal sp�rges om den vil afveje endnu et komponent - rollback til trin 6, inf-2
			
			try {
					synchronized (socketHandler) {
						socketHandler.wait();
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				return true;
				
	//Varen afvejes
	case 16:
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		weightController.showMessageTernaryDisplay(message.getMessage());
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return true;
	default:
		System.out.println("Default case. Returning false.");
		return false;
	}
}
	

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
	
	
	public String prepMessageCMD() {
		 
		msCMD[counter + 1] = '\r';
		msCMD[counter + 2] = '\n';

		String message = new String(msCMD);
		message.split("\n", 0);

		return message;
	}
	
	public void sendMessageCMD() {
		socketHandler.sendMessage(new SocketOutMessage(prepMessageCMD()));
	}
	 
	public void flushMsCMD() {
		isWriting = false;
		weightController.showMessageSecondaryDisplay(null);
		counter = 0;

		for (int i = 0; i < msCMD.length; i++) {
			msCMD[i] = '\0';
		}
	}
	
	public void initDisplay(String s1, String s2, String s3) {
			this.cd = new String[] {s1, s2, s3};
			weightController.showMessageTernaryDisplay(cd[0]);
			weightController.showMessageSecondaryDisplay(cd[1]);
			weightController.showMessagePrimaryDisplay(cd[2]);
			weightController.setSoftButtonTexts(btnLayout);
			
	}
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
		
	public void switchBtnLayout(int softBtn, String text) {
		
		if(softBtns[softBtn]) {
			btnLayout[softBtn] = "Tilbage";
			weightController.setSoftButtonTexts(btnLayout);
		}
		else if (!softBtns[softBtn]) {
			btnLayout[softBtn] = text;
			weightController.setSoftButtonTexts(btnLayout);
		}
	}
//		if(softBtn || init) {
//			weightController.showMessageTernaryDisplay(cd[0]);
//			weightController.showMessageSecondaryDisplay(cd[1]);
//			weightController.showMessagePrimaryDisplay(cd[2]);
//			softBtn = false;
//		}
	
	
	public void flushDislays() {
		weightController.showMessageTernaryDisplay("");
		weightController.showMessagePrimaryDisplay("");
		weightController.showMessageSecondaryDisplay("");
	}
	
	public void resetButtonTexts(String[] tara, String[] zero, String[] empty) {
		tara[0] = "";
		zero[0] = "";
		empty[0] = "";
	}
	

	
//	public void userLoginLocal() {
//		this.userName = "Anders And";
//		this.userId = 12;
//
//		weightController.showMessageTernaryDisplay("Please enter you user id.");
//		String input = prepMessageCMD();
//		if (input == "12")
//			weightController.showMessageTernaryDisplay("Your user ID is " + this.userId + " confirm by send y");
//		input = prepMessageCMD();
//		if (input == "y")
//			weightController.showMessageTernaryDisplay("Your name is " + this.userName + " confirm by pressing y");
//		input = prepMessageCMD();
//	}
//	
//	public void changeBatch() {
//		while (true) {
//			socketHandler.sendMessage(new SocketOutMessage("Enter batch number: 1234"));
//			if (batchnummer.equals("1234")) {
//				break;
//			} else
//				socketHandler.sendMessage(new SocketOutMessage("Invalid entry, try again"));
//		}
//	}
	
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
