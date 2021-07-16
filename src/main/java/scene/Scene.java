package scene;

import ecs.GameObject;
import ecs.RigidBody;
import ecs.StaticCollider;
import graphics.Camera;
import graphics.Texture;
import graphics.renderer.DebugRenderer;
import graphics.renderer.DefaultRenderer;
import graphics.renderer.LightmapRenderer;
import graphics.renderer.Renderer;
import input.Keyboard;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import physics.collision.Collider;
import postprocess.ForwardToTexture;
import postprocess.PostProcessStep;
import util.Assets;
import util.Engine;
import util.Tuple;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class Scene {

    private static int sceneCounter = 0;

    public DefaultRenderer renderer = new DefaultRenderer();
    public LightmapRenderer lightmapRenderer = new LightmapRenderer();
    public DebugRenderer debugRenderer = new DebugRenderer();

    private List<Renderer<?>> rendererRegistry = new LinkedList<>();

    private final int sceneId = sceneCounter++;

    protected Camera camera;
    private boolean debugMode = true;
    private boolean active = false;
    private final List<GameObject> gameObjects = new LinkedList<>();
    private final List<Collider> staticColliders = new LinkedList<>();
    private final List<Collider> bodyColliders = new LinkedList<>();

    protected ForwardToTexture forwardToScreen;

    public boolean isActive() {
        return active;
    }

    /**
     * Runs only once on startup, useful for initializing gameObjects or for first time setup.
     */
    public void awake() {
        camera = new Camera();
    }

    /**
     * This method will be called each time this scene becomes active by {@link SceneManager}.
     * Will be called right before the first update.
     * Can be used to prepare the scene to be shown after been shown previously to reset to a certain state.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * This method will be called each time this scene becomes inactive by {@link SceneManager},
     * because of switching to another method or termination of the program.
     * Can be used to preserve the current state of the scene or quickly complete/cancel tasks that were midst execution.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * This method is called every frame, and can be used to update objects.
     */
    public void update() {
        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_GRAVE_ACCENT)) {
            debugMode = !debugMode;
        }
    }

    /**
     * Do a collision check for the specific collider with all known rigidBodies and staticColliders.
     * If there is a collision, the given object will receive calls to {@link Collider#handleCollision(Collider,Tuple)}.
     *
     * @param collider the object to check whether is collides with anything
     */
    public void checkCollision(Collider collider) {
        if (collider == null) return; //ensure that the given collider is not null
        checkCollision(collider, bodyColliders);
        checkCollision(collider, staticColliders);
    }

    private void checkCollision(Collider body, List<Collider> colliders) {
        for (Collider other : colliders) {
            if (other == body) continue;
            if (!body.canCollideWith(other)) continue;
            if (!body.getCollisionShape().boundingSphere().intersection(other.getCollisionShape().boundingSphere()))
                continue;
            Optional<Tuple<Vector2f>> collision = body.doesCollideWith(other);
            if (collision.isPresent()) {
                body.handleCollision(other, collision.get());
                body.resetCollision();
            }
        }
    }

    public void postProcess(Texture texture) {
        forwardToScreen.setTexture(texture);
        forwardToScreen.apply();
    }

    /**
     * This method is called at the end of the program
     */
    public void clean() {
        this.renderer.clean();
        this.lightmapRenderer.clean();
        this.debugRenderer.clean();
        rendererRegistry.forEach(Renderer::clean);
    }

    // The following methods shouldn't be overridden. For this, added final keyword

    /**
     * Loops through all gameobjects already in the scene and calls their start methods.
     */
    public final void startGameObjects() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
            this.lightmapRenderer.add(gameObject);
            this.debugRenderer.add(gameObject);
            rendererRegistry.forEach(r -> r.add(gameObject));
            updateGameObject(gameObject, true);
        }
    }

    public final void updateGameObject(GameObject gameObject, boolean insertion) {
        StaticCollider staticCollider = gameObject.getComponent(StaticCollider.class);
        if (staticCollider != null && !staticColliders.contains(staticCollider)) {
            if (insertion)
                staticColliders.add(staticCollider);
            else staticColliders.remove(staticCollider);
        } else {
            RigidBody rigidBody = gameObject.getComponent(RigidBody.class);
            if (rigidBody != null && !bodyColliders.contains(rigidBody)) {
                if (insertion)
                    bodyColliders.add(rigidBody);
                else bodyColliders.remove(rigidBody);
            }
        }
    }

    /**
     * The sceneId is meant to represent the instance of a scene as an integer
     *
     * @see SceneManager
     */
    public final int sceneId() {
        return sceneId;
    }

    /**
     * @return the List of gameObjects contained in the scene.
     */
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * @param gameObject GameObject to be added.
     *                   Add a new gameObject to the scene and immediately call its start method.
     */
    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.start();
    }

    /**
     * Register a renderer to this scene
     *
     * @param renderer the renderer to be registered
     */
    public void registerRenderer(Renderer<?> renderer) {
        rendererRegistry.add(renderer);
    }

    /**
     * @return Returns the scene's instance of Camera
     */
    public Camera camera() {
        return this.camera;
    }

    /**
     * Loops through all the gameObjects in the scene and calls their update methods.
     */
    public void updateGameObjects() {
        for (GameObject go : gameObjects) {
            go.update(Engine.deltaTime());
        }
    }

    public void render() {
        rendererRegistry.forEach(Renderer::render);
        lightmapRenderer.render();
        lightmapRenderer.bindLightmap();
        renderer.render();
        //lightmapRenderer.framebuffer.blitColorBuffersToScreen(); TODO: remove later
    }

    public void debugRender() {
        if (debugMode) this.debugRenderer.render();
    }

    /**
     * Loads the shader.
     */
    public void loadSceneResources() {
        Assets.getShader("src/assets/shaders/default.glsl");
    }

    /**
     * Initialize all renderers
     */
    public void initRenderers() {
        debugRenderer.init();
        lightmapRenderer.init();
        renderer.init();
        forwardToScreen = new ForwardToTexture(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        forwardToScreen.init();
    }

}