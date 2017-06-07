package Controller;

import Socket.ISocketController;
import Weight.IWeightInterfaceController;

public interface IWeightController {
	void init(ISocketController socketHandler, IWeightInterfaceController uiController);
	void start();

}
