package net.alcuria.scroller;

import net.alcuria.scroller.test.TestScreen;
import net.alcuria.scroller.utils.ScrollerInputProcessor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ScrollerGame implements ApplicationListener {
    public static final float DESIGNED_WIDTH = 360f;
    public static final float DESIGNED_HEIGHT = 640f;
    public static final float ASPECT_RATIO = DESIGNED_WIDTH / DESIGNED_HEIGHT;
    public static final int FPS = 60;
    public static final float S_PER_FRAME = 1f / FPS;
    public static final Vector2 DIMENSIONS = new Vector2();
    public static final Vector2 BORDER = new Vector2();
    public static final ScrollerGame INSTANCE = new ScrollerGame();

    private AlcuriaScreen mScreen;

    private ScrollerGame() {
    }

    public static ScrollerGame getInstance() {
        return INSTANCE;
    }

    @Override
    public void create() {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        setScreen(new TestScreen());
        Gdx.input.setInputProcessor(new ScrollerInputProcessor());
    }

    @Override
    public void dispose() {
        if (mScreen != null) {
            mScreen.dispose();
            mScreen = null;
        }
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

        if (mScreen != null) {
            mScreen.resize(width, height);
        }
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

    public AlcuriaScreen getScreen() {
        return mScreen;
    }
}
