package glfwWindowWrapper.input.binding;

import glfwWindowWrapper.input.InputGroup;
import glfwWindowWrapper.input.device.InputDevice;
import glfwWindowWrapper.input.device.InputType;

public abstract class InputBinding {

	private InputGroup group;
	private String id;

	private InputDevice device;
	private int index;
	private int mods;

	public InputBinding(InputGroup group, String id, InputDevice device, int index, int mods) {
		this.group = group;
		this.id = id;
		this.device = device;
		this.index = index;
		this.mods = mods;
	}

	public InputGroup GetGroup() {
		return this.group;
	}

	public String GetId() {
		return this.id;
	}

	public InputDevice GetDevice() {
		return this.device;
	}

	public int GetIndex() {
		return this.index;
	}
	
	public int GetMods() {
		return this.mods;
	}

	public void SetDevice(InputDevice device) {
		this.device = device;
		group.OnBindingAltered(this);
	}

	public void SetIndex(int index) {
		this.index = index;
		group.OnBindingAltered(this);
	}

	public abstract InputType GetType();

}
