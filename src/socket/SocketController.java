package socket;

import java.util.List;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import socket.SocketInMessage.SocketMessageType;

import java.util.Set;



public class SocketController implements ISocketController 
{
	private Set<ISocketObserver> observers = new HashSet<ISocketObserver>();
	private Map<String, String> connectedClients = new HashMap<String, String>(); //Answer to = TODO Maybe add some way to keep track of multiple connections?
	private List<DataOutputStream> dout = new ArrayList<DataOutputStream>(); 
	Socket activeSocket;
	private int Port = 8000;
	
	public static int iterator = 3;
	
	
	public void OutputCMD(String message)
	{
		try 
		{
			OutputStreamWriter osw = new OutputStreamWriter(dout.get(0));
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(message);
			bw.flush();
			
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		} 
	}
  
  public void viewClient()
	{
		for(Entry<String, String> entry : connectedClients.entrySet()) 
		{
		    String ClientView = ("Client ip address: " + entry.getKey()); // + " Numbers of clients: " + entry.getValue() + "\n");
		    OutputCMD(ClientView);
		} 
	}

  	public void setPortNumber(int newPort) {
  		this.Port = newPort;
  	}
  	
  	
	@Override
	public void registerObserver(ISocketObserver observer) 
	{
		observers.add(observer);
	}

	@Override
	public void unRegisterObserver(ISocketObserver observer) 
	{
		observers.remove(observer);
	}

	@Override
	public void sendMessage(SocketOutMessage message) 
	{
		if (!dout.isEmpty())
		{
			try 
			{	
				for(int i = 0; i < dout.size(); i++) 
				{
				OutputStreamWriter osw = new OutputStreamWriter(dout.get(i));
				BufferedWriter bw = new BufferedWriter(osw);
				bw.write(message.getMessage());
				bw.flush();
				}
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			} 

		} else 
		{
			try 
			{
				String MessageClosed = "Connection is closed";
				for(int i = 0; i < dout.size(); i++) {
					OutputStreamWriter osw = new OutputStreamWriter(dout.get(i));
					BufferedWriter bw = new BufferedWriter(osw);
					bw.write(MessageClosed);
					bw.flush();
					}
				
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			} 
			
		}
	}
	
	@Override
	public void run() 
	{
		try (ServerSocket listeningSocket = new ServerSocket(Port))
		{ 
			while (true)
			{
				waitForConnections(listeningSocket); 	
			}		
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		} 
	}
	

	private void waitForConnections(ServerSocket listeningSocket) {
		try {
			activeSocket = listeningSocket.accept();
			DataOutputStream temp = new DataOutputStream(activeSocket.getOutputStream());
			dout.add(temp);
			String Addr = activeSocket.getInetAddress().toString();
			new SocketThread(activeSocket, this).start();
			int activeCount = SocketThread.activeCount() - 8;
			String clientCount = Integer.toString(activeCount);
			connectedClients.put(Addr, clientCount);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyObservers(SocketInMessage message) 
	{
		for (ISocketObserver socketObserver : observers) 
		{
			socketObserver.notify(message);
		}
	}
	
	public void close() {
		System.out.println("closing socket");
		try {
		activeSocket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}

class SocketThread extends Thread 
{	
	
	Socket activeSocket;
	SocketController SC;
	  
	  public SocketThread(Socket activeSocket, SocketController SC ) 
	  {
	    this.activeSocket = activeSocket;
	    this.SC = SC;
	  }
	  
	//Styrer kommunikationen mellem socket og vægt i et lukket miljø
	  public void run() 
	  {	
		//  String inLine;
		  
		  try 
		  {
	   	    SC.viewClient();
	   	    while (true)
	    	{
	   	    	switch(SocketController.iterator) {
	   	    		
	   	    	case 3:
	   		   	    System.out.println("Test nr: " + SocketController.iterator);
	   		   	SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Vælg afvejningsterminal (1-4)"));
	   	    		break;
	   	    		
	   	    	case 4:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Indtast laborantnummer: "));
	   	    		break;
	   	    		
	   	    	case 5:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Logget ind som: "));
	   	    		break;
	   	    		
	   	    	case 6:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Indtast produktbatch: "));
	   	    		break;
	   	    		
	   	    	case 7:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Der skal produceres "));
	   	    		break;
	   	    		
	   	    	case 8:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Nuvaerende belastning: "));
	   	    		break;
	   	    		
	   	    	case 9:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Produktion igangsat. Vaegten tareres."));
	   	    		break;
	   	    		
	   	    	case 10:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.T, "Tarer vaegten."));
	   	    		break;
	   	    	
	   	    	//Afvejningsprocedure trin 11, 12, 13 & 14 h�nger sammen i en case.
	   	    	case 11:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Læg beholder på vægten og tryk 'Tara'"));
	   	    		break;
	   	    	
	   	    	case 12:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Indtast ønsket raavarebatchnummer: "));
	   	    		break;
	   	    	
	   	    	case 13:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Afvej råvare og tryk OK"));
	   	    		break;
	   	    	
	   	    	case 14:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Godkendt"));
	   	    		break;
	   	  
	   	    	case 15:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "V�gten er registreret."));
	   	    		break;
	   	    	
	   	    	case 16:
	   	    		System.out.println("Test nr: " + SocketController.iterator);
	   	    		SC.notifyObservers(new SocketInMessage(SocketMessageType.RM208, "Afvejningen er registreret i databasen."));
	   	    		break;
	   	    	
	   	    	case 17:
	   	    		//buffer
	   	    		break;
	   	    	}
	   	    	SocketController.iterator++;
	   	    	}
		  }
		  catch (Exception e) {
		      System.out.println(e);
		   }
	  }
}
