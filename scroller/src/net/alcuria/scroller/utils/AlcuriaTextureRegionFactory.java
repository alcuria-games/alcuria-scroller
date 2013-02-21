/**
 * 
 */
package net.alcuria.scroller.utils;

import java.util.ArrayList;

import net.alcuria.scroller.AlcuriaScreen;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author juni.kim
 */
public class AlcuriaTextureRegionFactory {
    private static ArrayList<TextureRegion> buffer = new ArrayList<TextureRegion>();

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

    public static TextureRegion[] loadDirectory(final AlcuriaScreen requester, final String path) {
        FileHandle directoryHandle = null;
        if (Gdx.app.getType() == ApplicationType.Android) {
            directoryHandle = Gdx.files.internal(path);
        } else if (Gdx.app.getType() == ApplicationType.Desktop) {
            directoryHandle = Gdx.files.internal("./bin/" + path);
        }

        buffer.clear();
        for (FileHandle file : directoryHandle.list()) {
            if (isImage(file.extension())) {
                requester.loadTexture(file.path());
                buffer.add(new TextureRegion(requester.getTexture(file.path())));
            }
        }

        TextureRegion[] array = new TextureRegion[buffer.size()];
        array = buffer.toArray(array);
        buffer.clear();

        return array;
    }

    public static TextureRegion[] getFrames(final TextureRegion[] from, final int... indices) {
        if (indices == null || indices.length == 0) {
            return from;
        }

        TextureRegion[] to = new TextureRegion[indices.length];
        int index = 0;
        for (int i : indices) {
            i %= from.length;
            to[index] = from[i];
            index++;
        }

        return to;
    }

    private static boolean isImage(final String extension) {
        return ("jpg".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension));
    }
}
