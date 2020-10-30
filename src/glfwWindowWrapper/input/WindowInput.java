package glfwWindowWrapper.input;

import java.util.LinkedHashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import glfwWindowWrapper.Window;
import glfwWindowWrapper.input.device.Keyboard;
import glfwWindowWrapper.input.device.Mouse;
import glfwWindowWrapper.input.handlers.AxisHandler;

public class WindowInput {

	private Window window;
	private long windowPtr = 0;

	private Keyboard keyboard = new Keyboard(this);
	private Mouse mouse = new Mouse(this);

	private Map<String, InputGroup> inputGroups = new LinkedHashMap<>();
	private InputGroup currentInputGroup = null;

	public WindowInput(Window window) {
		this.window = window;
	}

	public Window GetWindow() {
		return this.window;
	}

	public void Update() {
		keyboard.Update();
		mouse.Update();
	}

	public InputGroup GetInputGroup(String id) {
		return this.inputGroups.get(id);
	}

	public InputGroup CreateInputGroup(String id) {
		InputGroup ig = new InputGroup(this, id);
		this.inputGroups.putIfAbsent(id, ig);
		if (this.currentInputGroup == null)
			SetCurrentInputGroup(ig);
		return ig;
	}

	public InputGroup GetOrCreateInputGroup(String id) {
		InputGroup ig = GetInputGroup(id);
		if (ig != null)
			return ig;
		return CreateInputGroup(id);
	}

	public boolean RemoveInputGroup(InputGroup inputGroup) {
		return this.inputGroups.remove(inputGroup.GetId()) != null;
	}

	public void SetCurrentInputGroupId(String id) {
		SetCurrentInputGroup(inputGroups.get(id));
	}

	public void SetCurrentInputGroup(InputGroup inputGroup) {
		this.currentInputGroup = inputGroup;
	}

	public InputGroup GetCurrentInputGroup() {
		return this.currentInputGroup;
	}

	public void ResetCallbacks(long windowPtr) {
		this.windowPtr = windowPtr;

		if (this.windowPtr != 0) {
			GLFW.glfwSetKeyCallback(this.windowPtr, this::WindowKeyCallback);
			GLFW.glfwSetCharModsCallback(this.windowPtr, this::WindowCharCallback);

			GLFW.glfwSetMouseButtonCallback(this.windowPtr, this::WindowMouseButtonCallback);
			GLFW.glfwSetScrollCallback(this.windowPtr, this::WindowScrollCallback);

			GLFW.glfwSetCursorPosCallback(this.windowPtr, this::WindowMouseMoveCallback);
			GLFW.glfwSetCursorEnterCallback(this.windowPtr, this::WindowMouseEnterCallback);
		}
	}

	public void SetCursor(long cursor) {
		if (this.windowPtr != 0)
			GLFW.glfwSetCursor(this.windowPtr, cursor);
	}

	public void LockKeyMods() {
		if (this.windowPtr != 0)
			GLFW.glfwSetInputMode(this.windowPtr, GLFW.GLFW_LOCK_KEY_MODS, GLFW.GLFW_TRUE);
	}

	public void UnlockKeyMods() {
		if (this.windowPtr != 0)
			GLFW.glfwSetInputMode(this.windowPtr, GLFW.GLFW_LOCK_KEY_MODS, GLFW.GLFW_FALSE);
	}

	public void EnableRawMouseMotion() {
		if (this.windowPtr != 0)
			if (GLFW.glfwRawMouseMotionSupported())
				GLFW.glfwSetInputMode(this.windowPtr, GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
	}

	public void DisableRawMouseMotion() {
		if (this.windowPtr != 0)
			if (GLFW.glfwRawMouseMotionSupported())
				GLFW.glfwSetInputMode(this.windowPtr, GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_FALSE);
	}

	public void HideCursor() {
		if (this.windowPtr != 0)
			GLFW.glfwSetInputMode(this.windowPtr, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
	}

	public void LockCursor() {
		if (this.windowPtr != 0)
			GLFW.glfwSetInputMode(this.windowPtr, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}

	public void NormalCursor() {
		if (this.windowPtr != 0)
			GLFW.glfwSetInputMode(this.windowPtr, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

	private void WindowKeyCallback(long windowPtr, int key, int scancode, int action, int mods) {
		switch (action) {
		case GLFW.GLFW_PRESS:
			this.keyboard.GetButtonHandler().OnButtonPress(key);
			this.keyboard.GetButtonHandler().OnRawButtonPress(key, mods);
			break;
		case GLFW.GLFW_RELEASE:
			this.keyboard.GetButtonHandler().OnButtonRelease(key);
			this.keyboard.GetButtonHandler().OnRawButtonRelease(key, mods);
			break;
		case GLFW.GLFW_REPEAT:
			this.keyboard.GetButtonHandler().OnRawButtonRepeat(key, mods);
			break;
		}
	}

	private void WindowCharCallback(long windowPtr, int codepoint, int mods) {
		this.currentInputGroup.OnCharTyped(codepoint, mods);
	}

	private void WindowMouseButtonCallback(long windowPtr, int button, int action, int mods) {
		switch (action) {
		case GLFW.GLFW_PRESS:
			this.mouse.GetButtonHandler().OnButtonPress(button);
			this.mouse.GetButtonHandler().OnRawButtonPress(button, mods);
			break;
		case GLFW.GLFW_RELEASE:
			this.mouse.GetButtonHandler().OnButtonRelease(button);
			this.mouse.GetButtonHandler().OnRawButtonRelease(button, mods);
			break;
		}
	}

	private void WindowScrollCallback(long windowPtr, double xOffset, double yOffset) {
		AxisHandler handler = this.mouse.GetAxisHandler();
		handler.SetAxisValue(Mouse.MOUSE_WHEEL_X_AXIS, xOffset);
		handler.SetAxisValue(Mouse.MOUSE_WHEEL_Y_AXIS, yOffset);
	}

	private void WindowMouseMoveCallback(long windowPtr, double xPos, double yPos) {
		AxisHandler handler = this.mouse.GetAxisHandler();
		handler.SetAxisValue(Mouse.MOUSE_X_AXIS, xPos);
		handler.SetAxisValue(Mouse.MOUSE_Y_AXIS, yPos);
	}

	private void WindowMouseEnterCallback(long windowPtr, boolean entered) {
		double[] xPos = new double[1], yPos = new double[1];
		GLFW.glfwGetCursorPos(this.windowPtr, xPos, yPos);
		AxisHandler handler = this.mouse.GetAxisHandler();
		handler.SetAxisPreviousValue(Mouse.MOUSE_X_AXIS, xPos[0]);
		handler.SetAxisPreviousValue(Mouse.MOUSE_Y_AXIS, yPos[0]);
		handler.SetAxisValue(Mouse.MOUSE_X_AXIS, xPos[0]);
		handler.SetAxisValue(Mouse.MOUSE_Y_AXIS, yPos[0]);
	}
}
