package glfwWindowWrapper.input.listener;

import glfwWindowWrapper.input.device.InputDevice;

public class AxisListenerData {
	public InputDevice device;
	public int axis;
	public double value;
	public double previousValue;
}
