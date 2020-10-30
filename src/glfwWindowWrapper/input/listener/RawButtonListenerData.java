package glfwWindowWrapper.input.listener;

import glfwWindowWrapper.input.binding.ButtonInputType;
import glfwWindowWrapper.input.device.InputDevice;

public class RawButtonListenerData {
	InputDevice device;
	ButtonInputType inputType;
	int button;
	int mods;
}
