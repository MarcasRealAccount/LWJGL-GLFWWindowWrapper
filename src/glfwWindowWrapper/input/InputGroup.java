package glfwWindowWrapper.input;

import java.util.LinkedHashMap;
import java.util.Map;

import glfwWindowWrapper.input.binding.AxisInputBinding;
import glfwWindowWrapper.input.binding.ButtonInputBinding;
import glfwWindowWrapper.input.binding.ButtonInputType;
import glfwWindowWrapper.input.binding.InputBinding;
import glfwWindowWrapper.input.binding.RawButtonInputBinding;
import glfwWindowWrapper.input.device.InputDevice;
import glfwWindowWrapper.input.device.InputType;
import glfwWindowWrapper.input.listener.AxisListenerData;
import glfwWindowWrapper.input.listener.ButtonListenerData;
import glfwWindowWrapper.input.listener.IAxisListener;
import glfwWindowWrapper.input.listener.IButtonListener;
import glfwWindowWrapper.input.listener.ICharTypedListener;
import glfwWindowWrapper.input.listener.IRawButtonListener;
import glfwWindowWrapper.input.listener.IWindowAxisListener;
import glfwWindowWrapper.input.listener.IWindowButtonListener;
import glfwWindowWrapper.input.listener.RawButtonListenerData;

public class InputGroup implements IWindowButtonListener, IWindowAxisListener, ICharTypedListener {

	private WindowInput input;
	private String id;

	private Map<String, InputBinding> inputBindings = new LinkedHashMap<>();

	private ICharTypedListener charTypedListener = null;

	public InputGroup(WindowInput input, String id) {
		this.id = id;
	}

	public String GetId() {
		return this.id;
	}

	public void Attach() {
		this.input.SetCurrentInputGroup(this);
	}

	public void Detach() {
		this.input.SetCurrentInputGroup(null);
	}

	public InputBinding GetInputBinding(String id) {
		return inputBindings.get(id);
	}

	public boolean RemoveInputBinding(InputBinding binding) {
		return inputBindings.remove(binding.GetId()) != null;
	}

	public AxisInputBinding CreateAxisInputBinding(String id, int axis) {
		return CreateAxisInputBinding(id, axis, InputDevice.MOUSE, null);
	}

	public AxisInputBinding CreateAxisInputBinding(String id, int axis, InputDevice device) {
		return CreateAxisInputBinding(id, axis, device, null);
	}

	public AxisInputBinding CreateAxisInputBinding(String id, int axis, InputDevice device, IAxisListener listener) {
		// TODO: Read binding from config file!
		AxisInputBinding binding = new AxisInputBinding(this, id, device, axis, listener);
		if (this.inputBindings.putIfAbsent(id, binding) != null)
			return null;
		return binding;
	}

	public ButtonInputBinding CreateButtonInputBinding(String id, int button) {
		return CreateButtonInputBinding(id, button, ButtonInputType.PRESS, InputDevice.KEYBOARD, null);
	}

	public ButtonInputBinding CreateButtonInputBinding(String id, int button, ButtonInputType inputType) {
		return CreateButtonInputBinding(id, button, inputType, InputDevice.KEYBOARD, null);
	}

	public ButtonInputBinding CreateButtonInputBinding(String id, int button, ButtonInputType inputType, InputDevice device) {
		return CreateButtonInputBinding(id, button, inputType, device, null);
	}

	public ButtonInputBinding CreateButtonInputBinding(String id, int button, ButtonInputType inputType, InputDevice device, IButtonListener listener) {
		// TODO: Read binding from config file!
		ButtonInputBinding binding = new ButtonInputBinding(this, id, device, button, inputType, listener);
		if (this.inputBindings.putIfAbsent(id, binding) != null)
			return null;
		return binding;
	}

	public RawButtonInputBinding CreateRawButtonInputBinding(String id, int button) {
		return CreateRawButtonInputBinding(id, button, -1, ButtonInputType.PRESS, InputDevice.KEYBOARD, null);
	}

	public RawButtonInputBinding CreateRawButtonInputBinding(String id, int button, int mods) {
		return CreateRawButtonInputBinding(id, button, mods, ButtonInputType.PRESS, InputDevice.KEYBOARD, null);
	}

	public RawButtonInputBinding CreateRawButtonInputBinding(String id, int button, int mods, ButtonInputType inputType) {
		return CreateRawButtonInputBinding(id, button, mods, inputType, InputDevice.KEYBOARD, null);
	}

	public RawButtonInputBinding CreateRawButtonInputBinding(String id, int button, int mods, ButtonInputType inputType, InputDevice device) {
		return CreateRawButtonInputBinding(id, button, mods, inputType, device, null);
	}

	public RawButtonInputBinding CreateRawButtonInputBinding(String id, int button, int mods, ButtonInputType inputType, InputDevice device, IRawButtonListener listener) {
		// TODO: Read binding from config file!
		RawButtonInputBinding binding = new RawButtonInputBinding(this, id, device, button, mods, inputType, listener);
		if (this.inputBindings.putIfAbsent(id, binding) != null)
			return null;
		return binding;
	}

	public void OnBindingAltered(InputBinding binding) {
		// TODO: Save binding to config file!
	}

	@Override
	public void OnCharTyped(int codepoint, int mods) {
		if (charTypedListener != null) {
			charTypedListener.OnCharTyped(codepoint, mods);
		}
	}

	@Override
	public void OnAxisChange(InputDevice inputDevice, int axis, double newValue, double previousValue) {
		if (charTypedListener == null) {
			inputBindings.forEach((id, binding) -> {
				if (binding.GetType() == InputType.AXIS) {
					AxisInputBinding axisInputBinding = (AxisInputBinding) binding;
					if (axisInputBinding.GetDevice() == inputDevice && axisInputBinding.GetIndex() == axis) {
						axisInputBinding.GetListener().OnAxisChange(new AxisListenerData());
					}
				}
			});
		}
	}

	@Override
	public void OnRawButtonPress(InputDevice inputDevice, int button, int mods) {
		if (charTypedListener == null) {
			inputBindings.forEach((id, binding) -> {
				if (binding.GetType() == InputType.RAW_BUTTON) {
					RawButtonInputBinding rawButtonInputBinding = (RawButtonInputBinding) binding;
					if (rawButtonInputBinding.GetInputType() == ButtonInputType.PRESS && rawButtonInputBinding.GetDevice() == inputDevice && rawButtonInputBinding.GetIndex() == button
							&& (rawButtonInputBinding.GetMods() == -1 | (rawButtonInputBinding.GetMods() & mods) == rawButtonInputBinding.GetMods())) {
						rawButtonInputBinding.GetListener().OnRawButtonChange(new RawButtonListenerData());
					}
				}
			});
		}
	}

	@Override
	public void OnRawButtonRelease(InputDevice inputDevice, int button, int mods) {
		if (charTypedListener == null) {
			inputBindings.forEach((id, binding) -> {
				if (binding.GetType() == InputType.RAW_BUTTON) {
					RawButtonInputBinding rawButtonInputBinding = (RawButtonInputBinding) binding;
					if (rawButtonInputBinding.GetInputType() == ButtonInputType.RELEASE && rawButtonInputBinding.GetDevice() == inputDevice && rawButtonInputBinding.GetIndex() == button
							&& (rawButtonInputBinding.GetMods() == -1 | (rawButtonInputBinding.GetMods() & mods) == rawButtonInputBinding.GetMods())) {
						rawButtonInputBinding.GetListener().OnRawButtonChange(new RawButtonListenerData());
					}
				}
			});
		}
	}

	@Override
	public void OnRawButtonRepeat(InputDevice inputDevice, int button, int mods) {
		if (charTypedListener == null) {
			inputBindings.forEach((id, binding) -> {
				if (binding.GetType() == InputType.RAW_BUTTON) {
					RawButtonInputBinding rawButtonInputBinding = (RawButtonInputBinding) binding;
					if (rawButtonInputBinding.GetInputType() == ButtonInputType.REPEAT && rawButtonInputBinding.GetDevice() == inputDevice && rawButtonInputBinding.GetIndex() == button
							&& (rawButtonInputBinding.GetMods() == -1 | (rawButtonInputBinding.GetMods() & mods) == rawButtonInputBinding.GetMods())) {
						rawButtonInputBinding.GetListener().OnRawButtonChange(new RawButtonListenerData());
					}
				}
			});
		}
	}

	@Override
	public void OnButtonPress(InputDevice inputDevice, int button, byte state) {
		if (charTypedListener == null) {
			inputBindings.forEach((id, binding) -> {
				if (binding.GetType() == InputType.BUTTON) {
					ButtonInputBinding buttonInputBinding = (ButtonInputBinding) binding;
					if (buttonInputBinding.GetInputType() == ButtonInputType.PRESS && buttonInputBinding.GetDevice() == inputDevice && buttonInputBinding.GetIndex() == button) {
						buttonInputBinding.GetListener().OnButtonChange(new ButtonListenerData());
					}
				}
			});
		}
	}

	@Override
	public void OnButtonRelease(InputDevice inputDevice, int button, byte state) {
		if (charTypedListener == null) {
			inputBindings.forEach((id, binding) -> {
				if (binding.GetType() == InputType.BUTTON) {
					ButtonInputBinding buttonInputBinding = (ButtonInputBinding) binding;
					if (buttonInputBinding.GetInputType() == ButtonInputType.RELEASE && buttonInputBinding.GetDevice() == inputDevice && buttonInputBinding.GetIndex() == button) {
						buttonInputBinding.GetListener().OnButtonChange(new ButtonListenerData());
					}
				}
			});
		}
	}

	@Override
	public void OnButtonDown(InputDevice inputDevice, int button, byte state) {
		if (charTypedListener == null) {
			inputBindings.forEach((id, binding) -> {
				if (binding.GetType() == InputType.BUTTON) {
					ButtonInputBinding buttonInputBinding = (ButtonInputBinding) binding;
					if (buttonInputBinding.GetInputType() == ButtonInputType.DOWN && buttonInputBinding.GetDevice() == inputDevice && buttonInputBinding.GetIndex() == button) {
						buttonInputBinding.GetListener().OnButtonChange(new ButtonListenerData());
					}
				}
			});
		}
	}
}
