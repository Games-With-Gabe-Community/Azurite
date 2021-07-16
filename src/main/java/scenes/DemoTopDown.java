package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import org.joml.Vector2f;
import physics.Transform;
import physics.collision.Shapes;
import postprocess.BloomEffect;
import postprocess.PostProcessStep;
import scene.Scene;
import tiles.Spritesheet;
import tiles.Tilesystem;
import util.Assets;
import util.Engine;
import util.Utils;

import static graphics.Graphics.setDefaultBackground;

public class DemoTopDown extends Scene {
    public static void main(String[] args) {
        Engine.init(1080, 720, "Azurite Engine Demo 1", 0.01f);
        Engine.scenes().switchScene(new DemoTopDown(), true);
        Engine.showWindow();
    }

    Spritesheet a;
    Spritesheet b;
    Tilesystem t;
    GameObject player;
    GameObject booper;
    GameObject greenLight;
    GameObject trRes;

    BloomEffect bloom;

    public void awake() {
        camera = new Camera();
        setDefaultBackground(0);

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
        b = new Spritesheet(Assets.getTexture("src/assets/images/walls.png"), 16, 16, 256, 0);
        t = new Tilesystem(this, a, b, 31, 15, 200, 200);

        trRes = new GameObject(this, "", new Transform(new Vector2f(0, 0), new Vector2f(100)), -20);

        booper = new GameObject(this, "Booper", new Transform(800, 800, 100, 100), 2);
        booper.addComponent(new Animation(1, a.getSprite(132), a.getSprite(150)));
        booper.addComponent(new PointLight(new Color(255, 153, 102), 30));

        //TODO edit for new collision system

        player = new GameObject(this, "Player", new Transform(600, 600, 100, 100), 2);
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));
        player.addComponent(new RigidBody(Shapes.axisAlignedRectangle(0,0, 100, 100), 1));
        player.addComponent(new SpriteRenderer(a.getSprite(132)));
        player.addComponent(new CharacterController());

        greenLight = new GameObject(this, "Green light", new Transform(3315, 300, 1, 1), 3);
        greenLight.addComponent(new PointLight(new Color(102, 255, 102), 30));

        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        bloom.init();
    }

    public void update() {
        super.update();
        player.getComponent(PointLight.class).intensity = Utils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 100, 140);
        booper.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);
        greenLight.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);

        camera.smoothFollow(player.getRawTransform());
    }

    @Override
    public void postProcess(Texture texture) {
        bloom.apply(texture);
    }
}
