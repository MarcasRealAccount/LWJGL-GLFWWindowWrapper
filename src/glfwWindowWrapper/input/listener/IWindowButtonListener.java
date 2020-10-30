package glfwWindowWrapper.input.listener;

import glfwWindowWrapper.input.device.InputDevice;

public interface IWindowButtonListener {
	public void OnRawButtonPress(InputDevice inputDevice, int button, int mods);

	public void OnRawButtonRelease(InputDevice inputDevice, int button, int mods);

	public void OnRawButtonRepeat(InputDevice inputDevice, int button, int mods);

	public void OnButtonPress(InputDevice inputDevice, int button, byte state);

	public void OnButtonRelease(InputDevice inputDevice, int button, byte state);

	public void OnButtonDown(InputDevice inputDevice, int button, byte state);
}
