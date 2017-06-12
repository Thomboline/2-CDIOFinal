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

	private ISocketController socketHandler;
	private IWeightInterfaceController weightController;
	private BrugerDAO bdao = new BrugerDAO();
	private ProduktBatchDAO pdao = new ProduktBatchDAO();
	private ProduktBatchKompDAO pkdao = new ProduktBatchKompDAO();
	private RaavareBatchDAO rbdao = new RaavareBatchDAO();
	private RaavareDAO rdao = new RaavareDAO();
	private ReceptDAO recdao = new ReceptDAO();
	private ReceptKompDAO rkdao = new ReceptKompDAO();
	private KeyState keyState = KeyState.K1;
	private KeyState prevKeyState = KeyState.K1;
	private SocketInMessage prevMessage;
	private double currentWeight = 0.000;
	private double containerWeight;
	private char[] msCMD = new char[32];
	private int counter = 0;
	private boolean isWriting, isTara, isRM208;
	private String batchnummer;
	private String userName;
	private int userId;
	private int wtIterator = 3;
	private int[] tempInf = new int[4];
	int tm, lm, pb, rb;
	private int inf = 0;
	DecimalFormat df = new DecimalFormat ("0.000");
//	private String tempMessage = "";
//	private char messageID;
	
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
				System.out.println("Råvarebatches: " + rbdao.getRaavareBatchList());
				System.out.println("Recepter: " + recdao.getReceptList());
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
				System.out.println(rdao.getRaavare(rkdao.getReceptKompList(1).get(0).getRaavareId()).getRaavareNavn());
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
			weightController.showMessageSecondaryDisplay("Ikke gyldigt input. Prøv igen.");
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
	public void notifyKeyPress(KeyPress keyPress) {
		System.out.println(keyPress.getCharacter());

		String[] tara = new String[] { "", "", "Reset Stored-weight", "Show Stored-weight", "", "Cancel" };
		String[] text = new String[] { "Backspace", "", "", "", "", "" };
		String[] zero = new String[] { "", "", "", "", "Change Batch-id", "Logout" };
		String[] empty = new String[] { "", "", "", "", "", "" };

		switch (keyPress.getType()) {
		case SOFTBUTTON:
			if (keyPress.getKeyNumber() == 0) {
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
			if (keyPress.getKeyNumber() == 2) {
				this.containerWeight = 0.000;
			}
			if (keyPress.getKeyNumber() == 3) {
				weightController.showMessageTernaryDisplay(this.containerWeight + "kg");
			}
			if (keyPress.getKeyNumber() == 4) {
				// Batch-id funktion
			}
			
			//177-183 VIRKER IKKE
			if (keyState.equals(KeyState.K2)) {
				if (keyPress.getKeyNumber() == 5) {
					notifyKeyPress(KeyPress.Cancel());
					}
				else if (keyPress.getKeyNumber() == 6) {
					notifyKeyPress(KeyPress.Send());
				}
			}
			else {
				if (keyPress.getKeyNumber() == 5) {
					// Log in & out funktion
				}
			}
		
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
			if (keyState.equals(KeyState.K1)) {

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
			
			if(keyState.equals(KeyState.K2)) {
				
				//Det er ikke muligt at skrive i keystate 2, men flush for en sikkerheds skyld
				flushMsCMD();
				
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

	@Override
	public void notifyWeightChange(double newWeight) {
		if(keyState.equals(KeyState.K3)) {
		this.currentWeight = (double) newWeight;
		weightController.showMessageTernaryDisplay("Tara weight: ");
		weightController.showMessagePrimaryDisplay(df.format(currentWeight) + "kg");
		}
	}

	public String resetWeightChange() {
		this.currentWeight = 0.000;
		String weight = this.currentWeight + "kg";
		return weight;
	}
	
	
	//Aflåst miljø til afvejningsprocedure
public boolean weightState(SocketInMessage message, int wtIterator) throws DALException {
	//Gem beskeden i tilfælde af fejl i send()
	prevMessage = message;
	
	//Brug 'tempInf' til at gemme midlertidige variable
	//[0] til terminal nummer
	//[1] til laborant nummer
	//[2] til råvarebatch nummer
	
	//Switch-casen starter på 3 så cases svarer til de egentlige trin i afvejningsproceduren
	
	switch (wtIterator) {
	
	//Vælg afvejningsterminal
	case 3:
		System.out.println("Performing case " + wtIterator);
		isRM208 = true;
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
				System.out.println("inf skal være 1 nu, men den er: " + inf);
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
		
		try {
		weightController.showMessageTernaryDisplay(message.getMessage() + " " + recdao.getRecept(pdao.getProduktBatch(pb).getReceptId()).getReceptNavn());
		weightController.showMessageSecondaryDisplay("Ingredienser: " + rdao.getRaavare(rkdao.getReceptKompList(1).get(0).getRaavareId()).getRaavareNavn());
//  	weightController.showMessageSecondaryDisplay(rkdao.getReceptKompList(recdao.getRecept(pdao.getProduktBatch(pb).getReceptId()).getReceptId()).toString());
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
			keyState = prevKeyState;
			return false;
	
		}
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		keyState = KeyState.K2;
		return true;
	
	//Kontroller at vægten er ubelastet
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
	
	//Sætter produktbatches status til "Under produktion"
	case 9:
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		weightController.showMessageTernaryDisplay(message.getMessage());
		try {
		pdao.getProduktBatch(pb).setStatus(1);
		weightController.showMessageSecondaryDisplay("Pb " + pb + " status: " + pdao.getProduktBatch(pb).getStatus());
		System.out.println("Produktbatch status: " + pdao.getProduktBatch(pb).getStatus());
		}
		catch(Exception e) {
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
	
	//Vægten tareres
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
	
	//Vægten beder om første tara beholder og om at tarere vægten
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
		keyState = KeyState.K2;
		return true;
	
	//Råvarebatch nummer
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
		return true;
	
	//Råvarebatch - find lige ud af præcis hvordan og hvorledes med 12/13
	case 13:
		try {
			//TEST VARIABEL, HARD CODED 1 TAL
			for(int i = 0; i < pkdao.getProduktBatchKompList(2).size(); i++) {
				System.out.println("Pk: " + pkdao.getProduktBatchKompList(pb).get(i).toString());
				System.out.println("Pk bruger: " + pkdao.getProduktBatchKompList(1).get(i).getBrugerId());
				
				//Skal bruges: 
				//if(rkdao.getProduktBatchKompList(pb).get(i).getBrugerId == 0) {
				//Den første komponent uden bruger ID skal afvejes
			
			}
			
		}
		catch (Exception e) {
			//Sidste case er ikke gyldig
			
			return false;
		}
		System.out.println("Containerweight: " + containerWeight);
		isRM208 = true;
		System.out.println("Performing case " + wtIterator);
		weightController.showMessageTernaryDisplay(message.getMessage() + containerWeight);
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return true;
	
	//Afvejning
	case 14:
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
	
	//Indtast råvarebatchnummer
	case 15:
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
//			weightController.showMessageSecondaryDisplay("Invalid input. Try again.");
//			inf--;
//			wtIterator--;
//			SocketController.test--;
//			keyState = prevKeyState;
//			try {
//			weightState(prevMessage, wtIterator);
//			}
//			catch (Exception ex) {
//				ex.printStackTrace();
//			}
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
//	
//	public void keepMessageDisplay() {
//		if(messageID == 'T') {
//			weightController.showMessageTernaryDisplay(tempMessage);
//		}
//		else if(messageID == 'S') {
//			weightController.showMessageSecondaryDisplay(tempMessage);
//		}
//		else if(messageID == 'P') {
//			weightController.showMessagePrimaryDisplay(tempMessage);
//		}
//	}
	
	public void flushTerDisplay() {
		weightController.showMessageTernaryDisplay("");
	}
	
	public void resetButtonTexts(String[] tara, String[] zero, String[] empty) {
		tara[0] = "";
		zero[0] = "";
		empty[0] = "";
	}
	
	public void userLoginLocal() {
		this.userName = "Anders And";
		this.userId = 12;

		weightController.showMessageTernaryDisplay("Please enter you user id.");
		String input = prepMessageCMD();
		if (input == "12")
			weightController.showMessageTernaryDisplay("Your user ID is " + this.userId + " confirm by send y");
		input = prepMessageCMD();
		if (input == "y")
			weightController.showMessageTernaryDisplay("Your name is " + this.userName + " confirm by pressing y");
		input = prepMessageCMD();
	}
	
	public void changeBatch() {
		while (true) {
			socketHandler.sendMessage(new SocketOutMessage("Enter batch number: 1234"));
			if (batchnummer.equals("1234")) {
				break;
			} else
				socketHandler.sendMessage(new SocketOutMessage("Invalid entry, try again"));
		}
	}
	
}

								//Hører til nederst i notify()
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
