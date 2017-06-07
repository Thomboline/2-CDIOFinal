package controller;

import socket.ISocketController;
import weight.IWeightInterfaceController;

public interface IWeightController {
	void init(ISocketController socketHandler, IWeightInterfaceController uiController);
	void start();

}
