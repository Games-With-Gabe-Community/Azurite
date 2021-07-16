package ecs;

import input.Keyboard;
import org.joml.Vector2f;

//FIXME
public class CharacterController extends Component {

    private float movementSpeed;
    private Vector2f currentDirection;
    private final boolean[] keys = new boolean[3];
    private RigidBody body;

    public CharacterController() {
        movementSpeed = 0.5f;
        this.order = SpriteRenderer.ORDER - 5;
        this.currentDirection = new Vector2f();
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void start() {
        super.start();
        this.body = gameObject.getComponent(RigidBody.class);
    }

    @Override
    public void update(float dt) {

        //FIXME this class is just for debug currently is hard coded as hell

        if (this.body == null) return;

        boolean change;

        change = keys[0] != (keys[0] = Keyboard.getKeyDown(Keyboard.A_KEY) || Keyboard.getKeyHeld(Keyboard.A_KEY));
        change = change || keys[1] != (keys[1] = Keyboard.getKeyDown(Keyboard.D_KEY) || Keyboard.getKeyHeld(Keyboard.D_KEY));
        change = change || keys[2] != (keys[2] = Keyboard.getKeyDown(Keyboard.W_KEY) || Keyboard.getKeyHeld(Keyboard.W_KEY));

        if (!change) return;

        //remove old movement
        if (this.body.velocity().dot(currentDirection) > 0)
            this.body.velocity().add(currentDirection.mul(-1, 0));
        currentDirection = new Vector2f();

        //define new movement

        if (keys[0] != keys[1]) {
            currentDirection.add(keys[0] ? -movementSpeed : movementSpeed, 0);
        }

        if (keys[2])
            currentDirection.add(0, -movementSpeed * 3);

        this.body.velocity().add(currentDirection);

    }

}
