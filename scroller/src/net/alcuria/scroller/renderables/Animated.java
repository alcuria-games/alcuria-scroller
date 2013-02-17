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
    private TextureRegion[] mTextureRegions;
    private float mElapsedTime;
    private float mSecPerFrame;

    public Animated() {
        super();
        mElapsedTime = 0f;
        mSecPerFrame = ScrollerGame.S_PER_FRAME;
    }

    @Override
    public boolean update(final float deltaTime) {
        if (mTextureRegions != null && mTextureRegions.length > 0) {
            mElapsedTime += deltaTime;

            int index = (int) (mTextureRegions.length * mElapsedTime / mSecPerFrame);
            index %= mTextureRegions.length;
            setTextureRegion(mTextureRegions[index]);
        }

        return super.update(deltaTime);
    }

    public void setFps(final int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("fps must be positive!");
        }

        mSecPerFrame = 1f / fps;
    }

    public void setTextureRegions(final TextureRegion... frames) {
        mTextureRegions = frames;
    }
}
