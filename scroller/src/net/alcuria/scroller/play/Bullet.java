/**
 * 
 */
package net.alcuria.scroller.play;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.Animated;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * @author Juni
 */
public class Bullet extends Animated implements Poolable {
    public static final int BALL = 0;
    public static final int RAY = 1;
    public static final int ARC = 2;
    public static final float VELOCITY = 8f;

    private boolean mIsEffect;
    private int mType = -1;
    private float mBulletTime;

    public static Bullet newInstance(final int weaponType, final boolean isEffect) {
        Bullet bullet = POOL.obtain();
        bullet.setType(weaponType);
        bullet.setIsEffect(isEffect);
        return bullet;
    }

    public final static Pool<Bullet> POOL = new Pool<Bullet>(512) {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    @Override
    public boolean update(final float deltaTime) {
        mBulletTime += deltaTime;

        float x = getPosition().x;
        float y = getPosition().y;

        switch (mType) {
            case BALL:
                if (mIsEffect) {
                    if (mBulletTime >= 0.75f) {
                        return false;
                    }
                }

                break;
            case RAY:
                if (mIsEffect) {
                    setRotation(mRotation + (mRotation > 0 ? 10f : -10f) * deltaTime);
                    float scale = getScale().x * 0.99f;
                    setScale(scale, scale);
                    if (mBulletTime >= 1.25f) {
                        return false;
                    }
                } else {
                    setScale(mScale.x, mScale.y + mBulletTime);
                    y += mBulletTime;
                }
                break;
            case 2:
                setRotation(mRotation + mRotation * deltaTime * 2f);
                if (mBulletTime >= 0.75f && Math.abs(mRotation) >= 90f) {
                    return false;
                }
                break;
            default:
            case -1:
                return false;
        }

        float dx = VELOCITY * MathUtils.cosDeg(mRotation + 90f) * (mIsEffect ? 0.25f : 1f);
        float dy = VELOCITY * MathUtils.sinDeg(mRotation + 90f) * (mIsEffect ? 0.25f : 1f);
        setPosition(x + dx, y + dy);

        if (mPosition.y >= ScrollerGame.DESIGNED_HEIGHT) {
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

        mType = -1;
        mTextureRegion = null;
        mBulletTime = 0;
        mElapsedTime = 0;

        play();
        setPosition(0, 0);
        setSize(0, 0);
        setScale(1f, 1f);
        setRotation(0);
        setFps(16);
    }

    public void setType(final int type) {
        mType = type;
    }

    public void setIsEffect(final boolean isIt) {
        mIsEffect = isIt;
    }
}
