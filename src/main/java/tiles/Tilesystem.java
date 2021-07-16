package tiles;

import ecs.GameObject;
import ecs.SpriteRenderer;
import ecs.StaticCollider;
import physics.Transform;
import physics.collision.Shapes;
import scene.Scene;
import util.Utils;

public class Tilesystem {

    GameObject[][] gameObjects;
    int w, h;

    public int map[] = {
            257,55,2,52,1,55,3,52,2,55,1,52,3,55,2,52,1,55,3,52,2,55,1,52,3,55,2,52,1,55,259,
            273,71,18,54,5,71,37,17,18,71,17,21,53,71,18,19,20,71,19,53,18,71,37,18,19,71,54,18,18,71,275,
            289,87,34,35,33,87,35,33,34,87,33,34,35,87,34,35,85,87,35,33,34,87,33,34,35,87,97,98,99,100,291,
            273,97,98,99,100,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,387,51,113,114,115,116,275,
            289,113,117,117,116,51,51,51,51,51,51,51,51,51,51,51,387,97,98,99,100,51,51,51,51,51,129,130,131,372,275,
            273,113,114,115,116,51,51,51,51,51,51,51,51,51,51,51,51,113,114,115,116,51,51,51,51,51,51,51,51,372,291,
            289,129,130,131,51,51,51,51,51,51,51,51,51,51,51,51,51,129,130,131,132,51,51,51,51,51,51,51,51,372,275,
            273,387,51,51,51,51,51,51,51,97,99,98,99,100,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,372,275,
            273,51,51,51,51,51,51,51,51,113,117,103,117,116,51,51,51,51,51,51,97,98,99,100,51,51,51,51,51,51,291,
            273,51,51,51,51,51,51,51,51,129,130,119,103,99,100,51,51,51,51,51,113,114,115,116,51,51,51,51,51,51,275,
            273,370,51,51,51,51,51,51,51,51,387,113,119,117,116,51,51,51,51,51,129,130,131,132,51,51,51,51,51,51,291,
            273,370,51,51,51,51,51,51,51,51,51,113,117,117,116,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,275,
            273,51,51,51,51,51,51,51,51,51,51,129,130,131,132,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,275,
            273,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,51,291,
            167,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,359,0
    };

    // hilt of blade = 87
    // should be 48

    public Tilesystem(Scene scene, Spritesheet a, Spritesheet b, int xTiles, int yTiles, int width, int height) {


        gameObjects = new GameObject[xTiles][yTiles];
        w = width;
        h = height;

        int i = 0;

        for (int y = 0; y < yTiles; y ++) {
            for (int x = 0; x < xTiles; x ++) {

                gameObjects[x][y] = new GameObject(scene, "Tile " + i, new Transform(x * width, y * height, width, height), 0);

                if (getAt(x, y, 31) <= 255 && getAt(x, y, 31) >= 0) {
                    gameObjects[x][y].addComponent(new SpriteRenderer(a.getSprite(
                        getAt(x, y, 31)
                    )));
                    if (getAt(x, y, 31) == 1) {
                        System.out.println("called");
                        gameObjects[x][y].addComponent(new StaticCollider(Shapes.axisAlignedRectangle(0,0, width, height), 2));
                    }
                } else if (getAt(x, y, 31) >= 256) {
                    gameObjects[x][y].addComponent(new SpriteRenderer(b.getSprite(
                            (int) Utils.map(getAt(x, y, 31), 256, 256*2-1, 0, 255)
                    )));
                }
                i ++;
            }
        }
    }

    public int getAt (int x, int y, int dimensionWidth) {
        return map[(dimensionWidth*y) + x] - 1;
    }

    public int[] getIndex (int worldX, int worldY) {
        int x = (int)worldX/w;
        int y = (int)worldY/h;

        return new int[] {x, y};
    }

    public Transform getTransform (int worldX, int worldY) {
        int x = (int)worldX/w;
        int y = (int)worldY/h;

        return gameObjects[x][y].getReadOnlyTransform();
    }

}