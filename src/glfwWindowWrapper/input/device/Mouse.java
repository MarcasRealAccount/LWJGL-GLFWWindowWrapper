package glfwWindowWrapper.input.device;

import glfwWindowWrapper.input.WindowInput;
import glfwWindowWrapper.input.handlers.AxisHandler;
import glfwWindowWrapper.input.handlers.AxisHandler.EAxisResetType;
import glfwWindowWrapper.input.handlers.ButtonHandler;

public class Mouse {

	private ButtonHandler buttons;
	private AxisHandler axises;

	public Mouse(WindowInput input) {
		buttons = new ButtonHandler(InputDevice.MOUSE, LAST_BUTTON + 1, input);
		axises = new AxisHandler(InputDevice.MOUSE, LAST_AXIS + 1, input);

		axises.SetAxisResetType(MOUSE_WHEEL_X_AXIS, EAxisResetType.ZERO);
		axises.SetAxisResetType(MOUSE_WHEEL_Y_AXIS, EAxisResetType.ZERO);
	}

	public void Update() {
		buttons.Update();
		axises.Update();
	}

	public ButtonHandler GetButtonHandler() {
		return this.buttons;
	}

	public AxisHandler GetAxisHandler() {
		return this.axises;
	}

	public boolean IsButtonPressed(int button) {
		return this.buttons.IsButtonPressed(button);
	}

	public boolean IsButtonReleased(int button) {
		return this.buttons.IsButtonReleased(button);
	}

	public boolean IsButtonDown(int button) {
		return this.buttons.IsButtonDown(button);
	}

	public byte GetButtonState(int button) {
		return this.buttons.GetButtonState(button);
	}

	public double GetAxisValue(int axis) {
		return this.axises.GetAxisValue(axis);
	}

	public double GetAxisPreviousValue(int axis) {
		return this.axises.GetAxisPreviousValue(axis);
	}

	public double GetAxisDeltaValue(int axis) {
		return this.axises.GetAxisDeltaValue(axis);
	}

	public static final int MOUSE_BUTTON_1 = 0;
	public static final int MOUSE_BUTTON_2 = 1;
	public static final int MOUSE_BUTTON_3 = 2;
	public static final int MOUSE_BUTTON_4 = 3;
	public static final int MOUSE_BUTTON_5 = 4;
	public static final int MOUSE_BUTTON_6 = 5;
	public static final int MOUSE_BUTTON_7 = 6;
	public static final int MOUSE_BUTTON_8 = 7;
	public static final int LAST_BUTTON = MOUSE_BUTTON_8;
	public static final int MOUSE_BUTTON_LEFT = MOUSE_BUTTON_1;
	public static final int MOUSE_BUTTON_RIGHT = MOUSE_BUTTON_2;
	public static final int MOUSE_BUTTON_MIDDLE = MOUSE_BUTTON_3;

	public static final int MOUSE_X_AXIS = 0;
	public static final int MOUSE_Y_AXIS = 1;
	public static final int MOUSE_WHEEL_X_AXIS = 2;
	public static final int MOUSE_WHEEL_Y_AXIS = 3;
	public static final int LAST_AXIS = MOUSE_WHEEL_Y_AXIS;

}
