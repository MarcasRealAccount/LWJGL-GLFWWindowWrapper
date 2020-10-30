package glfwWindowWrapper.input.listener;

import glfwWindowWrapper.input.device.InputDevice;

public interface IWindowAxisListener {
	public void OnAxisChange(InputDevice inputDevice, int axis, double newValue, double previousValue);
}
