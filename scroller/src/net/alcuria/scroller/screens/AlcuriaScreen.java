/**
 * 
 */
package net.alcuria.scroller.screens;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.RenderGroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author juni.kim
 */
public class AlcuriaScreen extends RenderGroup implements Screen {
    private final ObjectMap<String, Texture> mTextures = new ObjectMap<String, Texture>();

    private boolean mIsActive;
    private float mSigma;

    public AlcuriaScreen() {
        mIsActive = false;
        mSigma = 0.0f;
    }

    @Override
    public void setPosition(final float x, final float y) {
        super.setPosition(-x, -y);
    }

    @Override
    public void render(final float delta) {
        if (!mIsActive) {
            return;
        }

        mSigma += Gdx.graphics.getDeltaTime();
        while (mSigma >= ScrollerGame.FPS) {
            update(ScrollerGame.FPS);
            mSigma -= ScrollerGame.FPS;
        }

        synchronized (ScrollerGame.BATCH) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            ScrollerGame.BATCH.begin();
            draw(ScrollerGame.BATCH);
            ScrollerGame.BATCH.end();
        }
    }

    @Override
    public void resize(final int width, final int height) {
        setSize(width, height);
    }

    @Override
    public void show() {
        mIsActive = true;
    }

    @Override
    public void hide() {
        mIsActive = false;
    }

    @Override
    public void dispose() {
        for (Texture texture : mTextures.values()) {
            texture.dispose();
        }

        mTextures.clear();
    }

    protected void loadTexture(final String filePath) {
        int index = filePath.lastIndexOf("/");
        int lastIndex = filePath.lastIndexOf(".");
        String key = filePath;
        if (index >= 0 && lastIndex >= 0) {
            key = filePath.substring(index + 1, lastIndex);
        } else {
            Gdx.app.error("AlcuriaScreen", "INVALID FILEPATH! TEXTURE NOT LOADED");
            return;
        }

        mTextures.put(key, new Texture(filePath));
    }

    protected Texture getTexture(final String key) {
        return mTextures.get(key);
    }
}
