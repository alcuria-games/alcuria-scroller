/**
 * 
 */
package net.alcuria.scroller.renderables;

import net.alcuria.scroller.ScrollerGame;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * @author juni.kim
 */
public class TestSprite extends Renderable implements Poolable {
    public final static Pool<TestSprite> POOL = new Pool<TestSprite>() {
        @Override
        protected TestSprite newObject() {
            return new TestSprite();
        }
    };

    @Override
    public boolean update(final float deltaTime) {
        float x = getPosition().x;
        float y = getPosition().y + getSize().y * deltaTime * 10f;
        setPosition(x, y);

        mRotation += 180f * deltaTime;

        if (y >= ScrollerGame.DESIGNED_HEIGHT + getSize().y) {
            return false;
        }

        return super.update(deltaTime);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        POOL.free(this);
    }

    @Override
    public void reset() {
        mTextureRegion = null;

        setPosition(0, 0);
        setSize(0, 0);
        setScale(1f, 1f);
    }
}
