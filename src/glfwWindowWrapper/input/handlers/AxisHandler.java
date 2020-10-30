package glfwWindowWrapper.input.handlers;

import glfwWindowWrapper.input.WindowInput;
import glfwWindowWrapper.input.device.InputDevice;

public class AxisHandler {

	private double[] values;
	private double[] pValues;
	private EAxisResetType[] resetTypes;

	private InputDevice inputDevice;
	private WindowInput input;

	public AxisHandler(InputDevice inputDevice, int numAxises, WindowInput input) {
		this.values = new double[numAxises];
		this.pValues = new double[numAxises];
		this.resetTypes = new EAxisResetType[numAxises];
		for (int i = 0; i < this.values.length; i++) {
			this.resetTypes[i] = EAxisResetType.DEFAULT;
		}
		this.inputDevice = inputDevice;
		this.input = input;
	}

	public void Update() {
		for (int i = 0; i < this.values.length; i++) {
			switch (this.resetTypes[i]) {
			case DEFAULT:
				this.pValues[i] = this.values[i];
				break;
			case ZERO:
				this.pValues[i] = this.values[i] = 0.0D;
				break;
			}
		}
	}

	public void SetAxisValue(int axis, double value) {
		if (axis >= 0 && axis < this.values.length) {
			this.values[axis] = value;
			if (this.values[axis] - this.pValues[axis] != 0.0D)
				this.input.GetCurrentInputGroup().OnAxisChange(this.inputDevice, axis, this.values[axis], this.pValues[axis]);
		}
	}

	public void SetAxisPreviousValue(int axis, double previousValue) {
		if (axis >= 0 && axis < this.pValues.length)
			this.pValues[axis] = previousValue;
	}

	public void SetAxisResetType(int axis, EAxisResetType resetType) {
		if (axis >= 0 && axis < this.resetTypes.length)
			this.resetTypes[axis] = resetType;
	}

	public double GetAxisValue(int axis) {
		if (axis >= 0 && axis < this.values.length)
			return this.values[axis];
		return 0.0D;
	}

	public double GetAxisPreviousValue(int axis) {
		if (axis >= 0 && axis < this.pValues.length)
			return this.pValues[axis];
		return 0.0D;
	}

	public double GetAxisDeltaValue(int axis) {
		if (axis >= 0 && axis < this.values.length)
			return this.values[axis] - this.pValues[axis];
		return 0.0D;
	}

	public EAxisResetType GetAxisResetType(int axis) {
		if (axis >= 0 && axis < this.resetTypes.length)
			return this.resetTypes[axis];
		return EAxisResetType.DEFAULT;
	}

	public enum EAxisResetType {
		DEFAULT, ZERO
	}

}
