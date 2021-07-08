package util;

import graphics.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import scene.SceneManager;

import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * The Engine class initializes GLFW and the game loop. <br\>
 * Singleton.
 */
public final class Engine {
    /**
     * The static {@code Engine} instance.
     */
    private static final Engine instance = new Engine();
    
    /**
     * The exact time the engine started in milliseconds.
     */
    private final long startMillis;
    
    /**
     * No clue
     */
    private Window window;
    
    /**
     * Whether the {@code Engine} is running.
     */
    private boolean running;
    
    /**
     * The time between last frame and current
     */
    private float deltaTime;
    
    //private to prevent creating new instances
    /**
     * Private constructor so that creation of new instances is prohibited.
     */
    private Engine() {
        running = true;
        deltaTime = 0;
        startMillis = System.currentTimeMillis();
    }
    
    /**
     * Get the global unique instance of the Engine object.
     */
    public static Engine getInstance() {
        return instance;
    }
    
    /**
     * @return The time difference between last frame and current. - {@link Engine#deltaTime}
     */
    public static float deltaTime() {
        return getInstance().getDeltaTime();
    }
    
    /**
     * Updates {@link Engine#deltaTime}
     * @param deltaTime The new value.
     */
    public static void updateDeltaTime(float deltaTime) {
        getInstance().deltaTime = deltaTime;
    }
    
    /**
     * @return Whether the {@code Engine} is running. - {@link Engine#running}
     */
    public static boolean isRunning() {
        return getInstance().running;
    }
    
    /**
     * @return The {@link Window} of the current instance.
     */
    public static Window window() {
        return getInstance().getWindow();
    }
    
    /**
     * @return The {@link Window}.
     */
    public Window getWindow() {
        return window;
    }
    
    /**
     * @return The time difference between last frame and current. - {@link Engine#deltaTime}
     */
    public float getDeltaTime() {
        return deltaTime;
    }
    
    /**
     * Makes the window visible and runs the main window loop.
     */
    public static void showWindow() {
        window().showWindow();
    }
    
    /**
     * @return The {@link SceneManager} of the window.
     */
    public static SceneManager scenes() {
        return window().getSceneManager();
    }
    
    //internal method called before any init
    /**
     * Run before {@link Engine#init}.
     * <li>Set the GLFW error callback.</li>
     * <li>Initialize GLFW.</li>
     */
    private static void preInit() {
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!glfwInit())
            throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");
    }
    
    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowWidth      Width of the window to be created
     * @param windowHeight     Height of the window to be created
     * @param windowTitle      Title of the window to be created
     * @param minSceneLighting float from 0-1 indicating the minimum scene light level
     */
    public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting) {
        preInit();
        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, false);
    }
    
    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowWidth  Width of the window to be created
     * @param windowHeight Height of the window to be created
     * @param windowTitle  Title of the window to be created
     */
    public static void init(int windowWidth, int windowHeight, String windowTitle) {
        preInit();
        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, false);
    }
    
    /**
     * Start the engine, and initialize GLFW. This will create a fullscreen window.
     *
     * @param windowTitle      Title of the window to be created
     * @param minSceneLighting float from 0-1 indicating the minimum scene light level
     */
    public static void init(String windowTitle, float minSceneLighting) {
        preInit();
        getInstance().window = new Window(windowTitle, minSceneLighting, false);
    }
    
    public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting, boolean recalculateProjectionOnResize) {
        preInit();
        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, recalculateProjectionOnResize);
    }
    
    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowTitle Title of the window to be created
     */
    public static void init(String windowTitle) {
        preInit();
        getInstance().window = new Window(windowTitle);
    }
    
    /**
     * @return Returns the number of milliseconds since the engine started. (since the first call)
     */
    public static double millisRunning() {
        return System.currentTimeMillis() - getInstance().startMillis;
    }
    
}
