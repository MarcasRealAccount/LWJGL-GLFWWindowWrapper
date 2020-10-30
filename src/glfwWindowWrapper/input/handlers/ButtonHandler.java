package glfwWindowWrapper.input.handlers;

import glfwWindowWrapper.input.WindowInput;
import glfwWindowWrapper.input.device.InputDevice;

public class ButtonHandler {
	public static final byte PRESSED_BIT = 0b001;
	public static final byte RELEASED_BIT = 0b010;
	public static final byte DOWN_BIT = 0b100;

	private byte[] buttons;

	private InputDevice inputDevice;
	private WindowInput input;

	public ButtonHandler(InputDevice inputDevice, int numButtons, WindowInput input) {
		this.buttons = new byte[numButtons];
		for (int i = 0; i < this.buttons.length; i++)
			this.buttons[i] = 0;
		this.inputDevice = inputDevice;
		this.input = input;
	}

	public void Update() {
		for (int i = 0; i < this.buttons.length; i++) {
			if (IsButtonDown(i)) {
				this.input.GetCurrentInputGroup().OnButtonDown(this.inputDevice, i, this.buttons[i]);
			}

			if (IsButtonReleased(i))
				this.buttons[i] = 0;
			else
				this.buttons[i] &= DOWN_BIT;
		}
	}

	public void OnRawButtonPress(int button, int mods) {
		this.input.GetCurrentInputGroup().OnRawButtonPress(this.inputDevice, button, mods);
	}

	public void OnRawButtonRelease(int button, int mods) {
		this.input.GetCurrentInputGroup().OnRawButtonRelease(this.inputDevice, button, mods);
	}

	public void OnRawButtonRepeat(int button, int mods) {
		this.input.GetCurrentInputGroup().OnRawButtonRepeat(this.inputDevice, button, mods);
	}

	public void OnButtonPress(int button) {
		if (button >= 0 && button < this.buttons.length) {
			if (!IsButtonDown(button)) {
				this.buttons[button] |= PRESSED_BIT;
				this.buttons[button] |= DOWN_BIT;

				this.input.GetCurrentInputGroup().OnButtonPress(this.inputDevice, button, this.buttons[button]);
			}
		}
	}

	public void OnButtonRelease(int button) {
		if (button >= 0 && button < this.buttons.length) {
			if (IsButtonDown(button)) {
				this.buttons[button] |= RELEASED_BIT;

				this.input.GetCurrentInputGroup().OnButtonRelease(this.inputDevice, button, this.buttons[button]);
			}
		}
	}

	public boolean IsButtonPressed(int button) {
		if (button >= 0 && button < this.buttons.length)
			return (this.buttons[button] & PRESSED_BIT) == PRESSED_BIT;
		return false;
	}

	public boolean IsButtonReleased(int button) {
		if (button >= 0 && button < this.buttons.length)
			return (this.buttons[button] & RELEASED_BIT) == RELEASED_BIT;
		return false;
	}

	public boolean IsButtonDown(int button) {
		if (button >= 0 && button < this.buttons.length)
			return (this.buttons[button] & DOWN_BIT) == DOWN_BIT;
		return false;
	}

	public byte GetButtonState(int button) {
		if (button >= 0 && button < this.buttons.length)
			return this.buttons[button];
		return 0;
	}
}
