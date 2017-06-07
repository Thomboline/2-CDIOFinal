package Controller;

import Socket.SocketController;
import Weight.IWeightInterfaceController;
import Weight.gui.WeightInterfaceControllerGUI;
import Socket.ISocketController;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {
	private static boolean gui= true;

	public static void main(String[] args) {
		ISocketController socketHandler = new SocketController();
		IWeightInterfaceController weightController = new WeightInterfaceControllerGUI();
		IMainController mainCtrl = new WeightController(socketHandler, weightController);
		mainCtrl.start();
	}
}
