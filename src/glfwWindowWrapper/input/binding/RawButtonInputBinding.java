package glfwWindowWrapper.input.binding;

import glfwWindowWrapper.input.InputGroup;
import glfwWindowWrapper.input.device.InputDevice;
import glfwWindowWrapper.input.device.InputType;
import glfwWindowWrapper.input.listener.IRawButtonListener;

public class RawButtonInputBinding extends InputBinding {

	private ButtonInputType inputType;
	private IRawButtonListener listener;

	public RawButtonInputBinding(InputGroup group, String id, InputDevice device, int button, int mods, ButtonInputType inputType, IRawButtonListener listener) {
		super(group, id, device, button, mods);
		this.inputType = inputType;
		this.listener = listener;
	}

	public ButtonInputType GetInputType() {
		return this.inputType;
	}
	
	public IRawButtonListener GetListener() {
		return this.listener;
	}
	
	public void BindListener(IRawButtonListener listener) {
		this.listener = listener;
	}

	@Override
	public InputType GetType() {
		return InputType.RAW_BUTTON;
	}

}
