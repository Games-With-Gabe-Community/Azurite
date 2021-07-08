package graphics;


import event.EventData;
import event.Events;
import input.Keyboard;
import input.Mouse;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import postprocess.PostProcessing;
import scene.Scene;
import scene.SceneManager;
import util.Engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * The {@code Window} handles scenes, and runs the main loop.
 */
public class Window {
    
    /**
     * SceneManager. Need a better explanation? The thingy that does things with scenes.
     */
    private SceneManager sceneManager;
    
    // Window Variables
    /**
     * PLEASE GET RID OF THIS AND USE DELTA TIME VAR INSTEAD! I HATE THIS SYSTEM!
     */
    private long frameCount = 0;
    
    /**
     * The title of the window.
     */
    private String title;
    /**
     * The memory address of the {@code GLFWwindow}
     */
    private static long glfwWindow;
    /**
     * Honestly, even I don't know...
     */
    private final GLFWVidMode videoMode;
    /**
     * The width and height of the window.
     */
    private static int width, height;
    /**
     * Whether to recalculate the camera's projection matrix when the window is resized.
     */
    private final boolean recalculateProjectionOnResize;
    
    /**
     * Window constructor.
     * @param pwidth Width of the window.
     * @param pheight Height of the window.
     * @param ptitle Initial title of the window.
     * @param fullscreen Whether the window should be fullscreen.
     * @param minSceneLighting Minimum light level of the scene. (0.0F - 1.0F)
     * @param recalculateProjectionOnResize Whether to recalculate the camera's projection matrix when the window is resized.
     */
    public Window(int pwidth, int pheight, String ptitle, boolean fullscreen, float minSceneLighting, boolean recalculateProjectionOnResize) {
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = pwidth;
        height = pheight;
        title = ptitle;
        this.recalculateProjectionOnResize = recalculateProjectionOnResize;
        
        //create the sceneManager to be able to set a scene
        sceneManager = new SceneManager();
        
        sceneManager.setMinSceneLight(minSceneLighting);
        
        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        if (!fullscreen)
            initWindow(width, height, title, 0);
        else
            initWindow(width, height, title, glfwGetPrimaryMonitor());
    }
    
    /**
     * Window constructor. The width and height are taken from {@link Window#videoMode}.
     * @param ptitle Initial title of the window.
     * @param minSceneLighting Minimum light level of the scene. (0.0F - 1.0F)
     * @param recalculateProjectionOnResize Whether to recalculate the camera's projection matrix when the window is resized.
     */
    public Window(String ptitle, float minSceneLighting, boolean recalculateProjectionOnResize) {
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = videoMode.width();
        height = videoMode.height();
        title = ptitle;
        this.recalculateProjectionOnResize = recalculateProjectionOnResize;
        
        //create the sceneManager to be able to set a scene
        sceneManager = new SceneManager();
        
        sceneManager.setMinSceneLight(minSceneLighting);
        
        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        initWindow(width, height, title, glfwGetPrimaryMonitor());
    }
    
    /**
     * Overload constructor with {@code fullscreen} set to {@code false}.
     * @see Window#Window(int, int, String, boolean, float, boolean)
     */
    public Window(int pwidth, int pheight, String ptitle, float minSceneLighting, boolean recalculateProjectionOnResize) {
        this(pwidth, pheight, ptitle, false, minSceneLighting, recalculateProjectionOnResize);
    }
    
    /**
     * Overload constructor with {@code minSceneLighting} set to {@code 1.0F}.
     * @see Window#Window(int, int, String, float, boolean)
     */
    public Window(int pwidth, int pheight, String ptitle, boolean recalculateProjectionOnResize) {
        this(pwidth, pheight, ptitle, 1.0f, recalculateProjectionOnResize);
    }
    
    /**
     * Overload constructor with {@code minSceneLighting} set to {@code 1.0F}.
     * @see Window#Window(String, float, boolean)
     */
    public Window(String ptitle, boolean recalculateProjectionOnResize) {
        this(ptitle, 1.0f, recalculateProjectionOnResize);
    }
    
    /**
     * Overload constructor with {@code recalculateProjectionOnResize} set to {@code false}.
     * @see Window#Window(String, boolean)
     */
    public Window(String ptitle) {
        this(ptitle, false);
    }
    
    /**
     * Initialize the window.
     * <li>Create {@code GLFWwindow}.</li>
     * <li>Set up callbacks.</li>
     * <li>Make the window context current, so that OpenGL can recognize it.</li>
     * <li>Set the window position.</li>
     * <li>I don't understand the next line.</li>
     * @param width Width of the {@code GLFWwindow} to be created.
     * @param height Height of the {@code GLFWwindow} to be created.
     * @param title Title of the {@code GLFWwindow} to be created.
     * @param monitor Another thing I don't understand.
     */
    private void initWindow(int width, int height, String title, long monitor) {
        // Create window
        glfwWindow = glfwCreateWindow(width, height, title, monitor, 0);
        
        if (glfwWindow == 0)
            throw new IllegalStateException("[FATAL] Failed to create window.");
        
        // Set up callback
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
            
            if (recalculateProjectionOnResize && currentScene().camera() != null)
                currentScene().camera().adjustProjection();
            Events.windowResizeEvent.onEvent(new EventData.WindowResizeEventData(newWidth, newHeight));
        });
        
        Mouse.setupCallbacks();
        Keyboard.setupCallbacks();
        
        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        
        // Enable V-Sync
//        glfwSwapInterval(1);
        
        // Center the window
        glfwSetWindowPos(glfwWindow, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        GL.createCapabilities();
        
    }
    
    /**
     * Oh god not this...
     */
    void getFPS() {
        //TODO this wont properly display the FPS it will just count up the frames, there is no reset after a second yet
        frameCount++;
        glfwSetWindowTitle(glfwWindow, title + " @ " + Math.round((frameCount / (Engine.millisRunning() / 1000))) + " FPS");
    }
    
    /**
     * @return The memory address of the {@code GLFWwindow}.
     */
    public static long glfwWindow() {
        return glfwWindow;
    }
    
    /**
     * @return The window's title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @param title New title.
     */
    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(glfwWindow, title);
    }
    
    //Avoiding use of "game" loop as this could be used for non game stuff too, potentially.
    /**
     * Make the window visible.<br\>
     * Run the main scene loop.<br\>
     * Destroy the window.
     */
    public void showWindow() {
        /*
         * scenes.Main game loop
         */
        glfwShowWindow(glfwWindow);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        double frameBeginTime = glfwGetTime();
        double frameEndTime;
        
        sceneManager.enable();
        
        while (!glfwWindowShouldClose(glfwWindow)) {
            
            frameEndTime = glfwGetTime();
            Engine.updateDeltaTime((float) (frameEndTime - frameBeginTime));
            frameBeginTime = frameEndTime;
            
            Mouse.update();
            Keyboard.update();
            // poll GLFW for input events
            glfwPollEvents();
            
            sceneManager.update();
            sceneManager.updateGameObjects();
            sceneManager.render();
            PostProcessing.prepare();
            sceneManager.postProcess(currentScene().renderer.fetchColorAttachment(0));
            PostProcessing.finish();
            sceneManager.debugRender();
            
            glfwSwapBuffers(glfwWindow);
            getFPS();
        }
        
        currentScene().clean();
        // Delete all framebuffers
        Framebuffer.clean();
        
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    /**
     * @return The current scene.
     */
    public Scene currentScene() {
        return sceneManager.currentScene();
    }
    
    /**
     * @return The scene manager. - {@link Window#sceneManager}
     */
    public SceneManager getSceneManager() {
        return sceneManager;
    }
    
    /**
     * @param newHeight The new height of the window. Intended for usage in the WindowSizeCallback.
     */
    private static void setHeight(int newHeight) {
        height = newHeight;
    }
    
    /**
     * @param newWidth The new width of the window. Intended for usage in the WindowSizeCallback.
     */
    private static void setWidth(int newWidth) {
        width = newWidth;
    }
    
    /**
     * @return The width of the current window.
     */
    public static int getWidth() {
        return width;
    }
    
    /**
     * @return The height of the current window.
     */
    public static int getHeight() {
        return height;
    }
}