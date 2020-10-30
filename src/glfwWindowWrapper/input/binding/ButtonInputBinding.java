package glfwWindowWrapper.input.binding;

import glfwWindowWrapper.input.InputGroup;
import glfwWindowWrapper.input.device.InputDevice;
import glfwWindowWrapper.input.device.InputType;
import glfwWindowWrapper.input.listener.IButtonListener;

public class ButtonInputBinding extends InputBinding {

	private ButtonInputType inputType;
	private IButtonListener listener;

	public ButtonInputBinding(InputGroup group, String id, InputDevice device, int button, ButtonInputType inputType, IButtonListener listener) {
		super(group, id, device, button, 0);
		this.inputType = inputType;
		this.listener = listener;
	}

	public ButtonInputType GetInputType() {
		return this.inputType;
	}

	public IButtonListener GetListener() {
		return this.listener;
	}

	public void BindListener(IButtonListener listener) {
		this.listener = listener;
	}

	@Override
	public InputType GetType() {
		return InputType.BUTTON;
	}

}
