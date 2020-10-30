package glfwWindowWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import glfwWindowWrapper.input.WindowInput;
import glfwWindowWrapper.listeners.IFramebufferSizeListener;
import glfwWindowWrapper.listeners.IWindowPosListener;
import glfwWindowWrapper.listeners.IWindowSizeListener;
import glfwWindowWrapper.listeners.IWindowUpdateListener;

public class Window {
	private static List<Window> createdWindows = new ArrayList<>();

	private long windowPtr;

	private String title = "GLFW Window";

	private WindowData windowedData = new WindowData();
	private WindowData windowData = new WindowData();
	private boolean fullscreen = false;
	private Map<Integer, Integer> windowHints = new HashMap<>();

	private WindowInput input = new WindowInput(this);

	// Listeners:
	private List<IWindowPosListener> windowPosListeners = new ArrayList<>();
	private List<IWindowSizeListener> windowSizeListeners = new ArrayList<>();
	private List<IFramebufferSizeListener> framebufferSizeListeners = new ArrayList<>();
	private List<IWindowUpdateListener> windowUpdateListeners = new ArrayList<>();

	public static boolean IsAllWindowsClosed() {
		return Window.createdWindows.size() == 0;
	}

	public static void UpdateUntilAllWindowsAreaClosed() {
		while (!Window.IsAllWindowsClosed()) {
			Window.Update();
		}
	}

	public static void Update() {
		for (int i = 0; i < Window.createdWindows.size(); i++) {
			Window window = Window.createdWindows.get(i);
			if (window.IsCloseRequested()) {
				window.Destroy();
				i--;
			}
			window.UpdateInput();
		}

		GLFW.glfwPollEvents();

		Window.createdWindows.forEach(window -> {
			window.UpdateWindow();
		});
	}

	public void AddWindowPosListener(IWindowPosListener listener) {
		this.windowPosListeners.add(listener);
		listener.Exec(this.windowData.x, this.windowData.y);
	}

	public void AddWindowSizeListener(IWindowSizeListener listener) {
		this.windowSizeListeners.add(listener);
		listener.Exec(this.windowData.width, this.windowData.height);
	}

	public void AddFramebufferSizeListener(IFramebufferSizeListener listener) {
		this.framebufferSizeListeners.add(listener);
		listener.Exec(this.windowData.framebufferWidth, this.windowData.framebufferHeight);
	}

	public void AddWindowUpdateListener(IWindowUpdateListener listener) {
		this.windowUpdateListeners.add(listener);
	}

	public void RemoveWindowPosListener(IWindowPosListener listener) {
		this.windowPosListeners.remove(listener);
	}

	public void RemoveWindowSizeListener(IWindowSizeListener listener) {
		this.windowSizeListeners.remove(listener);
	}

	public void RemoveFramebufferSizeListener(IFramebufferSizeListener listener) {
		this.framebufferSizeListeners.remove(listener);
	}

	public void RemoveWindowUpdateListener(IWindowUpdateListener listener) {
		this.windowUpdateListeners.remove(listener);
	}

	public void ClearWindowHints() {
		this.windowHints.clear();
	}

	public void WindowHint(int hint, int value) {
		this.windowHints.put(hint, value);
	}

	public boolean IsCreated() {
		return this.windowPtr != 0;
	}

	public void RequestClose() {
		if (IsCreated()) {
			GLFW.glfwSetWindowShouldClose(this.windowPtr, true);
		}
	}

	public boolean IsCloseRequested() {
		if (IsCreated())
			return GLFW.glfwWindowShouldClose(this.windowPtr);
		return false;
	}

	public void SetTitle(String title) {
		if (IsCreated())
			GLFW.glfwSetWindowTitle(this.windowPtr, title);
		this.title = title;
	}

	public void SetSize(int width, int height) {
		if (IsCreated()) {
			GLFW.glfwSetWindowSize(this.windowPtr, width, height);
		} else {
			this.windowData.width = width;
			this.windowData.height = height;
		}
	}
	
	public WindowInput GetInput() {
		return this.input;
	}

	public int GetPosX() {
		return this.windowData.x;
	}

	public int GetPosY() {
		return this.windowData.y;
	}

	public int GetSizeWidth() {
		return this.windowData.width;
	}

	public int GetSizeHeight() {
		return this.windowData.height;
	}

	public int GetFramebufferWidth() {
		return this.windowData.framebufferWidth;
	}

	public int GetFramebufferHeight() {
		return this.windowData.framebufferHeight;
	}

	public boolean IsFullscreen() {
		return this.fullscreen;
	}

	public boolean IsMaximized() {
		return this.windowData.maximized;
	}

	public boolean IsIconified() {
		return this.windowData.iconified;
	}

	public long GetPreferredMonitor() {
		if (!IsCreated())
			return GLFW.glfwGetPrimaryMonitor();

		long bestMonitor = GLFW.glfwGetWindowMonitor(this.windowPtr);
		if (bestMonitor != 0)
			return bestMonitor;
		long bestMonitorArea = 0;
		PointerBuffer monitors = GLFW.glfwGetMonitors();

		int[] mx = new int[1], my = new int[1], mw = new int[1], mh = new int[1];

		for (int i = 0; i < monitors.capacity(); i++) {
			long monitor = monitors.get(i);
			if (monitor == 0)
				continue;
			GLFW.glfwGetMonitorWorkarea(monitor, mx, my, mw, mh);

			long area = (Math.max(Math.min(this.windowData.x + this.windowData.width, mx[0] + mw[0]), mx[0]) - Math.max(Math.min(this.windowData.x, mx[0] + mw[0]), mx[0]))
					* (Math.max(Math.min(this.windowData.y + this.windowData.height, my[0] + mh[0]), mh[0]) - Math.max(Math.min(this.windowData.y, mh[0] + mh[0]), mh[0]));

			if (area > bestMonitorArea) {
				bestMonitor = monitor;
				bestMonitorArea = area;
			}
		}

		if (bestMonitor == 0)
			return GLFW.glfwGetPrimaryMonitor();
		return bestMonitor;
	}

	public void Create() {
		if (IsCreated())
			return;

		if (Window.createdWindows.size() == 0) {
			if (!GLFW.glfwInit()) {
				PointerBuffer pDesc = MemoryUtil.memAllocPointer(1);
				int errorCode = GLFW.glfwGetError(pDesc);
				String errorString = "glfwInit error: " + errorCode + "\n" + pDesc.getStringUTF8(0);
				MemoryUtil.memFree(pDesc);
				throw new IllegalStateException(errorString);
			}
		}

		CreateImpl();
		Window.createdWindows.add(this);
	}

	public void Destroy() {
		if (!IsCreated())
			return;

		DestroyImpl();
		this.windowPtr = 0;
		Window.createdWindows.remove(this);

		if (Window.createdWindows.size() == 0) {
			GLFW.glfwTerminate();
		}
	}

	public void Restart() {
		if (!IsCreated())
			return;

		DestroyImpl();
		CreateImpl();
	}

	public void SetFullscreen(boolean fullscreen) {
		if (IsCreated()) {
			SetFullscreenImpl(fullscreen);
		} else {
			this.fullscreen = fullscreen;
		}
	}

	public void CenterWindow() {
		if (!IsCreated())
			return;

		long preferredMonitor = GetPreferredMonitor();

		if (preferredMonitor != 0) {
			int[] ww = new int[1], wh = new int[1];
			int[] mx = new int[1], my = new int[1], mw = new int[1], mh = new int[1];

			GLFW.glfwGetWindowSize(this.windowPtr, ww, wh);
			GLFW.glfwGetMonitorWorkarea(preferredMonitor, mx, my, mw, mh);

			int x = mx[0] + ((mw[0] - ww[0]) / 2);
			int y = my[0] + ((mh[0] - wh[0]) / 2);

			GLFW.glfwSetWindowPos(this.windowPtr, x, y);
		}
	}

	public void Maximize() {
		if (IsCreated()) {
			GLFW.glfwMaximizeWindow(this.windowPtr);
			this.windowData.maximized = GLFW.glfwGetWindowAttrib(this.windowPtr, GLFW.GLFW_MAXIMIZED) > 0;
		} else {
			this.windowData.maximized = true;
		}
	}

	public void Iconify() {
		if (IsCreated()) {
			GLFW.glfwIconifyWindow(this.windowPtr);
			this.windowData.iconified = GLFW.glfwGetWindowAttrib(this.windowPtr, GLFW.GLFW_ICONIFIED) > 0;
		} else {
			this.windowData.iconified = true;
		}
	}

	public void Restore() {
		if (IsCreated()) {
			GLFW.glfwRestoreWindow(this.windowPtr);
			this.windowData.maximized = GLFW.glfwGetWindowAttrib(this.windowPtr, GLFW.GLFW_MAXIMIZED) > 0;
			this.windowData.iconified = GLFW.glfwGetWindowAttrib(this.windowPtr, GLFW.GLFW_ICONIFIED) > 0;
		} else {
			this.windowData.maximized = false;
			this.windowData.iconified = false;
		}
	}

	public void MakeContextCurrent() {
		GLFW.glfwMakeContextCurrent(this.windowPtr);
	}

	public void SwapBuffers() {
		GLFW.glfwSwapBuffers(this.windowPtr);
	}

	public void SwapInterval(int interval) {
		GLFW.glfwSwapInterval(interval);
	}

	private void UpdateInput() {
		input.Update();
	}

	private void UpdateWindow() {
		this.windowUpdateListeners.forEach(listener -> {
			listener.Exec();
		});
	}

	private void RefreshWindow(long pWindow) {
		UpdateInput();
		UpdateWindow();
	}

	private void CreateImpl() {
		if (IsCreated())
			return;

		GLFW.glfwDefaultWindowHints();

		this.windowHints.forEach((hint, value) -> {
			GLFW.glfwWindowHint(hint, value);
		});

		if (this.fullscreen) {
			long preferredMonitor = GetPreferredMonitor();
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(preferredMonitor);

			int[] mx = new int[1], my = new int[1], mw = new int[1], mh = new int[1];
			GLFW.glfwGetMonitorWorkarea(preferredMonitor, mx, my, mw, mh);

			int x = mx[0] + ((mw[0] - this.windowData.width) / 2);
			int y = my[0] + ((mh[0] - this.windowData.height) / 2);

			this.windowData.copy(windowedData);
			this.windowedData.x = x;
			this.windowedData.y = y;

			this.windowPtr = GLFW.glfwCreateWindow(vidmode.width(), vidmode.height(), this.title, preferredMonitor, 0);
		} else {
			this.windowPtr = GLFW.glfwCreateWindow(this.windowData.width, this.windowData.height, this.title, 0, 0);
			CenterWindow();

			if (this.windowData.maximized)
				Maximize();
			else if (this.windowData.iconified)
				Iconify();
		}
		if (!IsCreated()) {
			PointerBuffer pDesc = MemoryUtil.memAllocPointer(1);
			int errorCode = GLFW.glfwGetError(pDesc);
			String errorString = "glfwCreateWindow error: " + errorCode + "\n" + pDesc.getStringUTF8(0);
			MemoryUtil.memFree(pDesc);
			throw new IllegalStateException(errorString);
		}

		int[] wx = new int[1], wy = new int[1], ww = new int[1], wh = new int[1], fw = new int[1], fh = new int[1];

		GLFW.glfwGetWindowPos(this.windowPtr, wx, wy);
		GLFW.glfwGetWindowSize(this.windowPtr, ww, wh);
		GLFW.glfwGetFramebufferSize(this.windowPtr, fw, fh);
		this.windowData.x = wx[0];
		this.windowData.y = wy[0];
		this.windowData.width = ww[0];
		this.windowData.height = wh[0];
		this.windowData.framebufferWidth = fw[0];
		this.windowData.framebufferHeight = fh[0];

		GLFW.glfwSetWindowPosCallback(this.windowPtr, (window, x, y) -> {
			this.windowData.x = x;
			this.windowData.y = y;

			this.windowPosListeners.forEach(listener -> {
				listener.Exec(x, y);
			});
		});

		GLFW.glfwSetWindowSizeCallback(this.windowPtr, (window, width, height) -> {
			this.windowData.width = width;
			this.windowData.height = height;

			this.windowSizeListeners.forEach(listener -> {
				listener.Exec(width, height);
			});
		});

		GLFW.glfwSetFramebufferSizeCallback(this.windowPtr, (window, width, height) -> {
			this.windowData.framebufferWidth = width;
			this.windowData.framebufferHeight = height;

			this.framebufferSizeListeners.forEach(listener -> {
				listener.Exec(width, height);
			});
		});

		GLFW.glfwSetWindowRefreshCallback(this.windowPtr, this::RefreshWindow);

		this.input.ResetCallbacks(this.windowPtr);
	}

	private void DestroyImpl() {
		GLFW.glfwDestroyWindow(this.windowPtr);
	}

	private void SetFullscreenImpl(boolean fullscreen) {
		if (this.fullscreen != fullscreen) {
			if (fullscreen) {
				long preferredMonitor = GetPreferredMonitor();
				GLFWVidMode vidmode = GLFW.glfwGetVideoMode(preferredMonitor);

				this.windowData.copy(this.windowedData);

				GLFW.glfwSetWindowMonitor(this.windowPtr, preferredMonitor, 0, 0, vidmode.width(), vidmode.hashCode(), vidmode.refreshRate());
			} else {
				GLFW.glfwSetWindowMonitor(this.windowPtr, 0, this.windowedData.x, this.windowedData.y, this.windowedData.width, this.windowedData.height, 0);

				if (this.windowedData.maximized)
					Maximize();
				else if (this.windowedData.iconified)
					Iconify();
			}
			this.fullscreen = fullscreen;
		}
	}

}
