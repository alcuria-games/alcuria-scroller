/**
 * 
 */
package net.alcuria.scroller.play;

import net.alcuria.scroller.renderables.Animated;
import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.utils.AlcuriaTextureRegionFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author juni.kim
 */
public class Hal extends RenderGroup implements Animated.Listener {
    private PlayScreen mScreen;
    private int mCurrentWeapon;
    private float mShootCoolTime;
    private boolean mIsFiring;

    private Animated mHead;
    private Animated mBody;

    private TextureRegion[] mRaiseAnimation;
    private TextureRegion[] mLoopAnimation;
    private TextureRegion[] mLowerAnimation;

    public Hal(final PlayScreen screen) {
        mCurrentWeapon = 0;
        mScreen = screen;

        mBody = new Animated();
        mBody.setFps(16);
        mBody.stop();
        mBody.setListener(this);
        addChild(mBody);

        mHead = new Animated();
        mHead.setFps(16);
        addChild(mHead);

        setSize(64, 64);
    }

    @Override
    public boolean update(final float deltaTime) {
        if (mShootCoolTime > 0) {
            mShootCoolTime = Math.max(mShootCoolTime - deltaTime, 0.0f);
        }

        if (mIsFiring && mShootCoolTime == 0) {
            mScreen.fireShot();
            mShootCoolTime += 0.25f;
        }

        return super.update(deltaTime);
    }

    @Override
    public void setSize(final float w, final float h) {
        mBody.setSize(w, h);
        mHead.setSize(w, h);

        super.setSize(w, h);
    }

    @Override
    public void onStop(final Animated animated) {
        if (mIsFiring) {
            mBody.setTextureRegions(mLoopAnimation);
            mBody.play();
        } else {
            mBody.setTextureRegions(mRaiseAnimation);
            mBody.stopAt(0);
        }
    }

    public void createTextureRegions(final Texture headTexture, final Texture bodyTexture) {
        TextureRegion[] base = AlcuriaTextureRegionFactory.createGridTextureRegions(bodyTexture, 64, 64);
        mRaiseAnimation = AlcuriaTextureRegionFactory.getFrames(base, 0, 1, 2, 3);
        mLoopAnimation = AlcuriaTextureRegionFactory.getFrames(base, 4, 5, 6, 7);
        mLowerAnimation = AlcuriaTextureRegionFactory.getFrames(base, 4, 3, 2, 1);

        mBody.setTextureRegions(mRaiseAnimation);
        mHead.setTextureRegions(AlcuriaTextureRegionFactory.createGridTextureRegions(headTexture, 64, 64));
    }

    public void setWeapon(final int which) {
        mCurrentWeapon = which % 4;
    }

    public int getWeapon() {
        return mCurrentWeapon;
    }

    public void setFiring(final boolean isIt) {
        mIsFiring = isIt;

        if (mIsFiring) {
            mBody.setTextureRegions(mRaiseAnimation);
            mBody.play(0, mRaiseAnimation.length - 1);
        } else {
            mBody.setTextureRegions(mLowerAnimation);
            mBody.play(0, mLowerAnimation.length - 1);
        }
    }
}
