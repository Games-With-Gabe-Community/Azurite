package tiles;

import ecs.GameObject;
import ecs.SpriteRenderer;
import ecs.StaticCollider;
import graphics.Color;
import physics.Transform;
import physics.collision.Shapes;
import scene.Scene;
import util.Utils;

public class TilesystemSideScroll {
    Spritesheet sheet;
    GameObject[][] gameObjects = new GameObject[0][0];
    MapHandler m;

    GameObject background;

    int w, h;

    public TilesystemSideScroll(Scene scene, Spritesheet s, int xTiles, int yTiles, int width, int height, GameObject c) {
        sheet = s;
        gameObjects = new GameObject[xTiles][yTiles];
        m = new MapHandler(xTiles, yTiles, 30);
        w = width;
        h = height;

        int i = 0;


        background = new GameObject(scene, "Background", new Transform(0, 0, xTiles * w, yTiles * h), 0);
        background.addComponent(new SpriteRenderer(new Color(41, 30, 49)));
        scene.addGameObjectToScene(background);

        for (int x = 0; x < xTiles; x++) {
            for (int y = 0; y < yTiles; y++) {
                gameObjects[x][y] = new GameObject(scene, "Tile " + i, new Transform(x * width, y * height, width, height), 0);

                if (m.getMap()[x][y] == 1) {
                    //gameObjects[x][y].addComponent(new AABB());
                    gameObjects[x][y].addComponent(new StaticCollider(Shapes.axisAlignedRectangle(0,0, width, height), 2));
                    gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(
                            Utils.randomInt(0, 6) == 0 ? 11 : Utils.randomInt(1, 5)

                    )));

                } else {
                    if (m.getMap()[x][y] != 1 && m.getMap()[x][y - 1] == 1) {
                        if (Utils.randomInt(0, 5) == 1) {
                            gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(19))); // hanging vines
                        } else if (Utils.randomInt(0, 5) == 1) {
                            gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(25)));
                        }
                    }
                }
                i++;
            }
        }
    }

    public int getType(int worldX, int worldY) {
        int x = (int) worldX / w;
        int y = (int) worldY / h;

        return m.getMap()[x][y];
    }

    public int[] getIndex(int worldX, int worldY) {
        int x = (int) worldX / w;
        int y = (int) worldY / h;

        return new int[]{x, y};
    }

    public Transform getTransform(int worldX, int worldY) {
        int x = (int) worldX / w;
        int y = (int) worldY / h;

        return gameObjects[x][y].getReadOnlyTransform();
    }

}