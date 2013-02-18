/**
 * 
 */
package net.alcuria.scroller.renderables;

import net.alcuria.scroller.ScrollerGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author juni.kim
 */
public class Animated extends Renderable {
    protected TextureRegion[] mTextureRegions;
    protected float mElapsedTime;
    protected float mSecPerFrame;
    protected int mCurrentFrame;

    public Animated() {
        super();
        mElapsedTime = 0f;
        mSecPerFrame = ScrollerGame.S_PER_FRAME;
        mCurrentFrame = 0;
    }

    @Override
    public boolean update(final float deltaTime) {
        if (mTextureRegions != null && mTextureRegions.length > 0) {
            mElapsedTime += deltaTime;
            if (mElapsedTime >= mSecPerFrame) {
                mElapsedTime -= mSecPerFrame;
                setFrame(mCurrentFrame + 1);
            }
        }

        return super.update(deltaTime);
    }

    public void setFps(final int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("fps must be positive!");
        }

        mSecPerFrame = 1f / fps;
    }

    public void setFrame(int frame) {
        if (mTextureRegions == null) {
            throw new IllegalStateException("setFrame can't be called until setTextureRegions is called");
        }

        frame %= mTextureRegions.length;
        setTextureRegion(mTextureRegions[frame]);
        mCurrentFrame = frame;
    }

    public void setTextureRegions(final TextureRegion... frames) {
        mTextureRegions = frames;
        setFrame(0);
    }
}
