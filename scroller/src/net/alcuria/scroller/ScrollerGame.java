package net.alcuria.scroller;

import net.alcuria.scroller.screens.AlcuriaScreen;
import net.alcuria.scroller.screens.TestScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ScrollerGame implements ApplicationListener {
    public static final float DESIGNED_WIDTH = 360f;
    public static final float DESIGNED_HEIGHT = 640f;
    public static final float ASPECT_RATIO = DESIGNED_WIDTH / DESIGNED_HEIGHT;
    public static final float FPS = 1 / 60f;
    public static final Vector2 DIMENSIONS = new Vector2();
    public static final Vector2 BORDER = new Vector2();
    public static final ScrollerGame INSTANCE = new ScrollerGame();
    public static final SpriteBatch BATCH = new SpriteBatch();

    private static final OrthographicCamera CAMERA = new OrthographicCamera();

    private AlcuriaScreen mScreen;

    private ScrollerGame() {

    }

    public static ScrollerGame getInstance() {
        return INSTANCE;
    }

    @Override
    public void create() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setScreen(new TestScreen());
    }

    @Override
    public void dispose() {
        if (mScreen != null) {
            mScreen.dispose();
            mScreen = null;
        }

        BATCH.dispose();
    }

    @Override
    public void render() {
        if (mScreen != null) {
            mScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(final int width, final int height) {
        DIMENSIONS.set(width, height);
        float aspectRatio = DIMENSIONS.x / DIMENSIONS.y;
        if (aspectRatio > ASPECT_RATIO) {
            float scale = DESIGNED_HEIGHT / DIMENSIONS.y;
            float offset = (DIMENSIONS.x * scale - DESIGNED_WIDTH) / 2f;
            BORDER.set(offset, 0);
        } else if (aspectRatio < ASPECT_RATIO) {
            float scale = DESIGNED_WIDTH / DIMENSIONS.x;
            float offset = (DIMENSIONS.y * scale - DESIGNED_HEIGHT) / 2f;
            BORDER.set(0, offset);
        }

        CAMERA.setToOrtho(false, DESIGNED_WIDTH + BORDER.x * 2f, DESIGNED_HEIGHT + BORDER.y * 2f);
        CAMERA.translate(-BORDER.x, -BORDER.y);
        CAMERA.update();
        BATCH.setProjectionMatrix(CAMERA.combined);
    }

    @Override
    public void pause() {
        if (mScreen != null) {
            mScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (mScreen != null) {
            mScreen.resume();
        }
    }

    public void setScreen(final AlcuriaScreen screen) {
        if (mScreen != null) {
            mScreen.pause();
            mScreen.hide();
        }

        mScreen = screen;
        mScreen.show();
    }
}
