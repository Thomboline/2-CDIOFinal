package controller;

import java.awt.List;
import java.text.DecimalFormat;
import java.util.ArrayList;

import socket.ISocketController;
import socket.ISocketObserver;
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
	private KeyState keyState = KeyState.K1;
	private double currentWeight = 0.000;
	private double containerWeight;
	private char[] msCMD = new char[32];
	private int counter = 0;
	private boolean isWriting, isTara, isRM208;
	private String batchnummer;
	private String userName;
	private int userId;
	private int wtIterator = 3;
	//private ArrayList<Integer> tempInf = new ArrayList<Integer>();
	private int[] tempInf = new int[3];
	private int inf = 0;
	DecimalFormat df = new DecimalFormat ("0.000");
	
	public WeightController(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.init(socketHandler, weightInterfaceController);
	}

	//
	@Override
	public void init(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.socketHandler = socketHandler;
		this.weightController = weightInterfaceController;
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
			
			

		} else {
			System.err.println("No controllers injected!");
		}
	}
	
	
	// Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		
		try {
		if(weightState(message, this.wtIterator)) {
			this.wtIterator++;
		}
		else {
			notify(message);
		}
		
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	private void handleKMessage(SocketInMessage message) {
		switch (message.getMessage()) {
		case "1":
			this.keyState = KeyState.K1;
			break;
		case "2":
			this.keyState = KeyState.K2;
			break;
		case "3":
			this.keyState = KeyState.K3;
			break;
		case "4":
			this.keyState = KeyState.K4;
			break;
		default:
			socketHandler.sendMessage(new SocketOutMessage("ES"));
			break;
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
			if (keyPress.getKeyNumber() == 5) {
				// Log in & out funktion
			}
			break;
		case TARA:
			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3)) {
				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
			}

			if (keyState.equals(KeyState.K1) || keyState.equals(KeyState.K4)) {

				isTara = true;
				if (isWriting) {
					tara[0] = "Backspace";
				}
				weightController.setSoftButtonTexts(tara);

				this.containerWeight += this.currentWeight;
				notifyWeightChange(0);
			}

			break;
		case TEXT:
			// Implement in next project?
			// Keep "INDTAST NR" in TernaryDisplay while writing message back
			if (isRM208) {

			}

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

			break;
		case ZERO:
			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3)) {
				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
			}
			if (keyState.equals(KeyState.K1) || keyState.equals(KeyState.K4)) {

				isTara = false;
				weightController.setSoftButtonTexts(zero);
				containerWeight = 0.000;
				notifyWeightChange(0);
				flushMsCMD();
			}
			break;
		case CANCEL:
			isTara = false;
			weightController.setSoftButtonTexts(empty);
			weightController.showMessageSecondaryDisplay(null);
			flushMsCMD();
			resetButtonTexts(tara, zero, empty);
			break;
		case EXIT:
			weightController.unRegisterObserver(this);
			socketHandler.unRegisterObserver(this);
			System.exit(0);
			break;
		case SEND:
			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3)) {
				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
			}
			if (keyState.equals(KeyState.K1) || keyState.equals(KeyState.K4)) {

				if (isTara) {
					weightController.setSoftButtonTexts(tara);
				} else {
					weightController.setSoftButtonTexts(empty);
				}

				if (isRM208) {
					try {
						synchronized (socketHandler) {
							socketHandler.notify();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					isRM208 = false;
				}

				socketHandler.sendMessage(new SocketOutMessage("RM A "));
				
				//Stores input in weight info container
				prepInfo();

				//Prepares message and sends a new String to CMD
				sendMessageCMD();
				
				//Flush msCMD char array
				flushMsCMD();
				
				//Resets all soft button descriptions
				resetButtonTexts(tara, zero, empty);
			}
			break;
		}
	}

	@Override
	public void notifyWeightChange(double newWeight) {
		this.currentWeight = (double) newWeight;
		weightController.showMessagePrimaryDisplay(df.format(currentWeight) + "kg");
	}

	public String resetWeightChange() {
		this.currentWeight = 0.000;
		String weight = this.currentWeight + "kg";
		return weight;
	}
	

	//Aflåst miljø til afvejningsprocedure
public boolean weightState(SocketInMessage message, int wtIterator) throws Exception {
	
	//Brug 'tempInf' til at gemme midlertidige variable
	//[0] til terminal nummer
	//[1] til laborant nummer
	//[2] til råvarebatch nummer
	
	//Switch-casen starter på 3 så cases svarer til de egentlige trin i afvejningsproceduren
	
	switch (wtIterator) {
	
	//Vælg afvejningsterminal
	case 3:
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
	
	//Laborant nummer	
	case 4:
		isRM208 = true;
		System.out.println("Performing case " + "tis");
		weightController.showMessageTernaryDisplay("test1");
		socketHandler.sendMessage(new SocketOutMessage("RM B\r\n"));
		try {
			synchronized (socketHandler) {
				socketHandler.wait();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return true;
		
	//Svar med Laborantens initialer
	case 5:
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
	
	//Indtast produktbatchnummer
	case 6:
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
	
	//Svar med recept
	case 7:
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
	
	//Kontroller at vægten er ubelastet
	case 8:
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
	
	//Sætter produktbatches status til "Under produktion"
	case 9:
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
	
	//Vægten tareres
	case 10:
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
	
	//Vægten beder om første tara beholder
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
		return true;
	
	//Placer tara
	case 12:
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
	
	//Tara vægt registreres
	case 13:
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
	
	//Vægten tareres
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
		wtIterator--;
		return false;
	}
}

	public void prepInfo() {
		String temp = "";
		for(int i = 0; i < msCMD.length; i++) {
			if(msCMD[i] == 0) {
				break;
			}
			else {
				temp += msCMD[i];
			}
		}
		if(!temp.isEmpty()) {
			System.out.println("msCMD string split: " + temp);
			tempInf[inf] = Integer.parseInt(temp);
			System.out.println("TempInf: " + tempInf[inf]);
		}
			else {
				System.out.println("OK");
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
