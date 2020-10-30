package glfwWindowWrapper.input.binding;

import glfwWindowWrapper.input.InputGroup;
import glfwWindowWrapper.input.device.InputDevice;
import glfwWindowWrapper.input.device.InputType;
import glfwWindowWrapper.input.listener.IAxisListener;

public class AxisInputBinding extends InputBinding {

	private IAxisListener listener;

	public AxisInputBinding(InputGroup group, String id, InputDevice device, int axis, IAxisListener listener) {
		super(group, id, device, axis, 0);
		this.listener = listener;
	}

	public IAxisListener GetListener() {
		return this.listener;
	}

	public void BindListener(IAxisListener listener) {
		this.listener = listener;
	}

	@Override
	public InputType GetType() {
		return InputType.AXIS;
	}

}
