package com.task;

import java.awt.Dimension;
import com.controller.BluetoothController;

public class Tasker {
	
	public static final String URL = null;
	
	public static void main(String[] args) {
		BluetoothController.connect(URL);
		new Joystick(new Dimension(570, 576));
	}
}
