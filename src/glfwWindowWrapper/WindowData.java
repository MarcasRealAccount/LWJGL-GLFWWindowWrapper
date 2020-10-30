package glfwWindowWrapper;

public class WindowData {
	public int x = 0, y = 0;
	public int width = 1280, height = 720;
	public int framebufferWidth = 0, framebufferHeight = 0;

	public boolean maximized = false;
	public boolean iconified = false;

	public void copy(WindowData other) {
		other.x = x;
		other.y = y;
		other.width = width;
		other.height = height;
		other.framebufferWidth = framebufferWidth;
		other.framebufferHeight = framebufferHeight;
		other.maximized = maximized;
		other.iconified = iconified;
	}
}
