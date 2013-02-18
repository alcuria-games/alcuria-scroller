/**
 * 
 */
package net.alcuria.scroller.utils;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.screens.AlcuriaScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/**
 * @author juni.kim
 */
public class ScrollerInputProcessor implements InputProcessor {
    private Vector3 mLastTouchPoint;
    private boolean yes;

    public ScrollerInputProcessor() {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        mLastTouchPoint = new Vector3();
    }

    public static enum TouchEvent {
        DOWN, UP, DRAG
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        final AlcuriaScreen currentScreen = ScrollerGame.getInstance().getScreen();
        if (currentScreen != null) {
            currentScreen.getCamera().unproject(mLastTouchPoint.set(screenX, screenY, 0));
            return currentScreen.onTouchEvent(TouchEvent.DOWN, mLastTouchPoint.x, mLastTouchPoint.y);
        }

        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        final AlcuriaScreen currentScreen = ScrollerGame.getInstance().getScreen();
        if (currentScreen != null) {
            currentScreen.getCamera().unproject(mLastTouchPoint.set(screenX, screenY, 0));
            return currentScreen.onTouchEvent(TouchEvent.UP, mLastTouchPoint.x, mLastTouchPoint.y);
        }

        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        final AlcuriaScreen currentScreen = ScrollerGame.getInstance().getScreen();
        if (currentScreen != null) {
            currentScreen.getCamera().unproject(mLastTouchPoint.set(screenX, screenY, 0));
            return currentScreen.onTouchEvent(TouchEvent.DRAG, mLastTouchPoint.x, mLastTouchPoint.y);
        }

        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        switch (keycode) {
            case Keys.MENU:
            case Keys.BACK:
                Gdx.app.exit();
        }

        return true;
    }

    // Ignore
    @Override
    public boolean keyDown(final int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        if (character == ' ') {
            yes = !yes;
            if (yes) {
                ScrollerGame.getInstance().getScreen().pause();
            } else {
                ScrollerGame.getInstance().getScreen().resume();
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(final int amount) {
        return false;
    }
}
