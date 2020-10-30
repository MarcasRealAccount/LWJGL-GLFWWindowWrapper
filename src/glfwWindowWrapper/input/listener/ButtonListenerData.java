package glfwWindowWrapper.input.listener;

import glfwWindowWrapper.input.binding.ButtonInputType;
import glfwWindowWrapper.input.device.InputDevice;

public class ButtonListenerData {
	InputDevice device;
	ButtonInputType inputType;
	int button;
	byte state;
}
