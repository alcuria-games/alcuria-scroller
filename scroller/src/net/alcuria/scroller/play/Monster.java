/**
 * 
 */
package net.alcuria.scroller.play;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.Animated;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * @author Juni
 */
public class Monster extends Animated implements Poolable {
    public static final int BAT = 0;

    private Hal mHal;
    private Vector2 mVelocity;

    private int mType = -1;

    public static Monster newInstance(final int monsterType, final Hal hal) {
        Monster monster = POOL.obtain();
        monster.setType(monsterType);
        monster.mHal = hal;
        return monster;
    }

    public final static Pool<Monster> POOL = new Pool<Monster>(16) {
        @Override
        protected Monster newObject() {
            return new Monster();
        }
    };

    private Monster() {
        super();

        mVelocity = new Vector2();
        setFps(16);
    }

    @Override
    public boolean update(final float deltaTime) {
        float px = mPosition.x / ScrollerGame.DESIGNED_WIDTH;

        switch (mType) {
            case BAT:
                if (MathUtils.random() > px) {
                    mVelocity.x += 0.125f;
                } else {
                    mVelocity.x -= 0.125f;
                }

                mVelocity.y -= 0.125f;
                break;
        }

        mPosition.add(mVelocity);

        if (mPosition.x < -mSize.x || mPosition.x > ScrollerGame.DESIGNED_WIDTH || mPosition.y < -mSize.y) {
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
        stop();

        mHal = null;
        mType = -1;
        mTextureRegion = null;
        mElapsedTime = 0;

        play();
        setPosition(0, 0);
        setSize(0, 0);
        setScale(1f, 1f);
        setRotation(0);

        mVelocity.set(0, 0);
    }

    public void setType(final int monsterType) {
        mType = monsterType;
    }
}
