package ecs;

import graphics.Sprite;

public class Animation extends Component {
    /**
     * Speed of the animation in seconds
     */
    private final float speed;
    /**
     * Should the animation loop
     */
    private boolean loop;
    /**
     * Key frames of the animation
     */
    private final Sprite[] sprites;
    /**
     * Cached Sprite Renderer
     */
    private SpriteRenderer spriteRendererComponent;
    /**
     * Is this animation currently playing
     */
    private boolean running;
    /**
     * An internal time accumulator
     */
    private float timeAcc;
    /**
     * Pointer to the current sprite beind shown
     */
    private int pointer;

    /**
     * Creates animation that starts instantly and loops
     *
     * @param speed   speed of the animation in seconds
     * @param sprites the keyframes of this animation
     */
    public Animation(float speed, Sprite... sprites) {
        this(speed, true, sprites);
    }

    /**
     * Creates animation that loops
     *
     * @param speed   speed of the animation in seconds.
     * @param run     should the animation be started immediately.
     * @param sprites the keyframes of this animation.
     */
    public Animation(float speed, boolean run, Sprite... sprites) {
        this(speed, run, true, sprites);
    }

    /**
     * Creates animation with given parameters
     *
     * @param speed   speed of the animation in seconds.
     * @param run     should the animation be started immediately.
     * @param loop    should the animation loop.
     * @param sprites the keyframes of this animation.
     */
    public Animation(float speed, boolean run, boolean loop, Sprite... sprites) {
        this.speed = speed;
        this.loop = loop;
        this.sprites = sprites;
        this.running = run;
        this.order = SpriteRenderer.ORDER - 1; //it is good, if the animation gets updated, if the spriteRenderer does
    }

    /**
     * Initializes the animation
     */
    @Override
    public void start() {
        spriteRendererComponent = gameObject.getComponent(SpriteRenderer.class);
        if (spriteRendererComponent == null) {
            spriteRendererComponent = new SpriteRenderer(sprites[0]);
            gameObject.addComponent(spriteRendererComponent);
        }
    }

    /**
     * Updates the sprite of the parent GameObject
     *
     * @param dt Engine.deltaTime
     */
    @Override
    public void update(float dt) {
        if (running) {
            timeAcc += dt;

            if (timeAcc >= speed) {
                timeAcc = 0;
                Sprite frame = sprites[pointer++];
                spriteRendererComponent.setSprite(frame);

                if (pointer >= sprites.length) {
                    if (loop) {
                        pointer = 0;
                    } else {
                        running = false;
                    }
                }
            }
        }
    }
}
