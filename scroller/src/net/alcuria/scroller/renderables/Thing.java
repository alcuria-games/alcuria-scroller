/**
 * 
 */
package net.alcuria.scroller.renderables;

import net.alcuria.scroller.screens.TestScreen;

/**
 * @author juni.kim
 */
public class Thing extends Renderable {
    private TestScreen mScreen;
    private int mCurrentWeapon;
    private float mShootCoolTime;
    private boolean mIsFiring;

    public Thing(final TestScreen screen) {
        mCurrentWeapon = 0;
        mScreen = screen;
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

    public void setWeapon(final int which) {
        mCurrentWeapon = which % 4;
    }

    public int getWeapon() {
        return mCurrentWeapon;
    }

    public void setFiring(final boolean isIt) {
        mIsFiring = isIt;
    }
}
