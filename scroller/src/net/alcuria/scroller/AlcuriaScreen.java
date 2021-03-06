/**
 * 
 */
package net.alcuria.scroller;

import java.util.HashMap;
import java.util.Map;

import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.utils.ScrollerInputProcessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Juni
 */
public class AlcuriaScreen extends RenderGroup implements Screen {
    protected Camera mCamera;

    private Map<String, Texture> mTextures;
    private SpriteBatch mSpriteBatch;
    private float mSigma;
    private boolean mIsActive;
    private boolean mIsDisposed;

    private TextureRegion mGradient;

    public AlcuriaScreen() {
        initialize();

        loadTexture("gradient.png");
        mGradient = new TextureRegion(getTexture("gradient.png"));
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

        mSigma += delta;

        while (mSigma >= ScrollerGame.S_PER_FRAME) {
            update(delta);
            mSigma -= ScrollerGame.S_PER_FRAME;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mSpriteBatch.begin();
        draw(mSpriteBatch);

        mSpriteBatch.draw(mGradient, -100, 0, 110f, mCamera.viewportHeight);
        mSpriteBatch.draw(mGradient, ScrollerGame.DESIGNED_WIDTH + 100, 0, -110f, mCamera.viewportHeight);
        mSpriteBatch.end();
    }

    @Override
    public void resize(final int width, final int height) {
        float aspectRatio = ScrollerGame.DIMENSIONS.x / ScrollerGame.DIMENSIONS.y;
        if (aspectRatio > ScrollerGame.ASPECT_RATIO) {
            float scale = ScrollerGame.DESIGNED_HEIGHT / ScrollerGame.DIMENSIONS.y;
            float offset = (ScrollerGame.DIMENSIONS.x * scale - ScrollerGame.DESIGNED_WIDTH) / 2f;
            ScrollerGame.BORDER.set(offset, 0);
        } else if (aspectRatio < ScrollerGame.ASPECT_RATIO) {
            float scale = ScrollerGame.DESIGNED_WIDTH / ScrollerGame.DIMENSIONS.x;
            float offset = (ScrollerGame.DIMENSIONS.y * scale - ScrollerGame.DESIGNED_HEIGHT) / 2f;
            ScrollerGame.BORDER.set(0, offset);
        }

        float camWidth = ScrollerGame.DESIGNED_WIDTH + ScrollerGame.BORDER.x * 2f;
        float camHeight = ScrollerGame.DESIGNED_HEIGHT + ScrollerGame.BORDER.y * 2f;
        ((OrthographicCamera) mCamera).setToOrtho(false, camWidth, camHeight);
        ((OrthographicCamera) mCamera).translate(-ScrollerGame.BORDER.x, -ScrollerGame.BORDER.y);
        ((OrthographicCamera) mCamera).update();

        mSpriteBatch.setProjectionMatrix(mCamera.combined);
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
        onRemoved();

        mSpriteBatch.dispose();

        for (Texture texture : mTextures.values()) {
            texture.dispose();
        }
        mTextures.clear();

        mCamera = null;
        mTextures = null;
        mSpriteBatch = null;

        mIsDisposed = true;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public boolean onTouchEvent(final ScrollerInputProcessor.TouchEvent event, final float x, final float y) {

        return true;
    }

    protected void initialize() {
        if (mIsDisposed) {
            throw new IllegalStateException("Screen is disposed!");
        }

        mSize.set(ScrollerGame.DESIGNED_WIDTH, ScrollerGame.DESIGNED_HEIGHT);
        mCamera = new OrthographicCamera();

        mTextures = new HashMap<String, Texture>();
        mSpriteBatch = new SpriteBatch();

        mSigma = 0;
        mIsActive = false;
        mIsDisposed = false;
    }

    public void loadTexture(final String name) {
        if (mTextures.get(name) == null) {
            mTextures.put(name, new Texture(Gdx.files.internal(name)));
        }
    }

    public Texture getTexture(final String name) {
        return mTextures.get(name);
    }
}
