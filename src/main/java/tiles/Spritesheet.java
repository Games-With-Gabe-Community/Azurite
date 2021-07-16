package tiles;

import graphics.Sprite;
import graphics.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {

    private Texture texture;
    private List<Sprite> sprites;

    /**
     * Takes a texture, sprite width, height, number of sprites and the pixel spacing between sprites (if applicable), and adds each sprite in the sheet to a List.
     * @param texture
     * @param spriteWidth
     * @param spriteHeight
     * @param numSprites
     * @param spacing
     */
    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i = 0; i < numSprites; i ++) {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                new Vector2f(rightX, bottomY),
                new Vector2f(rightX, topY),
                new Vector2f(leftX, topY),
                new Vector2f(leftX, bottomY)
            };
            Sprite sprite = new Sprite(this.texture, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    /**
     * Returns the integer size of the List of Sprites
     * @return int size
     */
    public int getSize () {
        return sprites.size();
    }

    /**
     * Return a single sprite by passing it's index in the sprite list
     * @param index of the sprite
     * @return Sprite
     */
    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    /**
     * @return entire list of sprites contained in Spritesheet
     */
    public List<Sprite> getSprites () {
        return sprites;
    }
}







