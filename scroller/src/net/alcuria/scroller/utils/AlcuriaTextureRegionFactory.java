/**
 * 
 */
package net.alcuria.scroller.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author juni.kim
 */
public class AlcuriaTextureRegionFactory {
    public static TextureRegion[] createGridTextureRegions(final Texture texture, final int width, final int height) {
        int columns = texture.getWidth() / width;
        int rows = texture.getHeight() / height;

        if (columns <= 0 || rows <= 0) {
            throw new IllegalArgumentException("width and height must be less than texture dimensions");
        }

        TextureRegion[] regions = new TextureRegion[columns * rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                regions[r * columns + c] = new TextureRegion(texture, c * width, r * height, width, height);
            }
        }

        return regions;
    }
}
