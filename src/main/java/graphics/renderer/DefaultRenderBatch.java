package graphics.renderer;

import ecs.SpriteRenderer;
import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.Transform;

public class DefaultRenderBatch extends RenderBatch {
	private final SpriteRenderer[] sprites;

	private int numberOfSprites;

	/**
	 * Create a default type render batch
	 *
	 * @param maxBatchSize maximum number of sprites in the batch
	 * @param zIndex zIndex of the batch. Used for sorting.
	 */
	DefaultRenderBatch(int maxBatchSize, int zIndex) {
		super(maxBatchSize, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);
		this.sprites = new SpriteRenderer[maxBatchSize];
		this.primitiveVertices = new float[vertexCount * 4];

		this.numberOfSprites = 0;
	}

	/**
	 * This function figures out how to add vertices with an origin at the top left
	 *
	 * @param index index of the primitive to be loaded
	 * @param offset offset of where the primitive should start being added to the array
	 */
	@Override
	protected void loadVertexProperties(int index, int offset) {
		SpriteRenderer sprite = this.sprites[index];
		Vector4f color = sprite.getColorVector();
		Vector2f[] textureCoordinates = sprite.getTexCoords();

		int textureID;
		if (sprite.getTexture() != null)
			textureID = addTexture(sprite.getTexture());
		else
			textureID = 0;


		// Add vertex with the appropriate properties
		float xAdd = 1.0f;
		float yAdd = 1.0f;
		for (int i = 0; i < 4; i++) {
			switch (i) {
				case 1:
					yAdd = 0.0f;
					break;
				case 2:
					xAdd = 0.0f;
					break;
				case 3:
					yAdd = 1.0f;
					break;
			}

			// Load position
			Transform spr = sprite.gameObject.getReadOnlyTransform();
			data[offset] = spr.getPosition().x + (xAdd * spr.scale.x);
			data[offset + 1] = spr.getPosition().y + (yAdd * spr.scale.y);

			primitiveVertices[primitiveVerticesOffset] = data[offset];
			primitiveVertices[primitiveVerticesOffset + 1] = data[offset + 1];

			// Load color
			data[offset + 2] = color.x; // Red
			data[offset + 3] = color.y; // Green
			data[offset + 4] = color.z; // Blue
			data[offset + 5] = color.w; // Alpha

			primitiveVertices[primitiveVerticesOffset + 2] = data[offset + 2];
			primitiveVertices[primitiveVerticesOffset + 3] = data[offset + 3];
			primitiveVertices[primitiveVerticesOffset + 4] = data[offset + 4];
			primitiveVertices[primitiveVerticesOffset + 5] = data[offset + 5];

			// Load texture coordinates
			data[offset + 6] = textureCoordinates[i].x;
			data[offset + 7] = textureCoordinates[i].y;

			primitiveVertices[primitiveVerticesOffset + 6] = data[offset + 6];
			primitiveVertices[primitiveVerticesOffset + 7] = data[offset + 7];

			// Load texture ID
			data[offset + 8] = textureID;

			primitiveVertices[primitiveVerticesOffset + 8] = data[offset + 8];

			offset += vertexCount;
			primitiveVerticesOffset += vertexCount;
		}

	}

	/**
	 * Checks if any sprite is dirty (has changed any of its properties).
	 * If so, resets its data in the data[] via load().
	 *
	 * Calls the RenderBatch::updateBuffer method to re-upload the data if required
	 */

	//Old way of updating by calling glSubData
	/*
	public void updateBuffer() {
		for (int i = 0; i < numberOfSprites; i ++) {
			SpriteRenderer spr = sprites[i];
			if (spr.isDirty()) {
				load(i);
				spr.setClean();
			}
		}

		super.updateBuffer();
	}
	*/

	//New way of updating an object which was changed by ONLY updating that object without having to rebuffer entire buffer by calling glSubBufferData
	public void updateBuffer(){
		for(int i = 0; i < numberOfSprites; i++){
			if(sprites[i].isDirty()){
				//Create map for the dirty quad starting at its offset and ending in its length
				super.updateBuffer(i);
				sprites[i].setClean();
			}
		}
	}



	/**
	 * Adds a sprite to this batch
	 *
	 * @param sprite sprite to be added
	 * @return if the sprite was successfully added to the batch
	 */
	public boolean addSprite(SpriteRenderer sprite) {
		// If the batch still has room, and is at the same z index as the sprite, then add it to the batch
		if (hasRoomLeft() && zIndex() == sprite.gameObject.zIndex()) {
			Texture tex = sprite.getTexture();
			if (tex == null || (hasTexture(tex) || hasTextureRoom())) {
				// Get the index and add the renderObject
				int index = this.numberOfSprites;
				this.sprites[index] = sprite;
				this.numberOfSprites++;

				// Add properties to local vertices array
				load(index);

				if (this.numberOfSprites >= this.maxBatchSize) {
					this.hasRoom = false;
				}
				return true;
			}
		}
		return false;
	}
}
