package controller;

import weight.gui.WeightInterfaceControllerGUI;
import socket.ISocketController;
import socket.SocketController;
import weight.IWeightInterfaceController;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main 
{
	private static boolean gui= true;

	public static void main(String[] args) 
	{
		ISocketController socketHandler = new SocketController();
		IWeightInterfaceController WeightController = new WeightInterfaceControllerGUI();
		IWeightController mainCtrl = new WeightController(socketHandler, WeightController);
		mainCtrl.start();
	}
}
