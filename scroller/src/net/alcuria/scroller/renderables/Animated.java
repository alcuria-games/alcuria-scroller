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
    protected int mTargetFrame;
    protected boolean mIsPlaying;
    protected Listener mListener;

    public Animated() {
        super();
        mElapsedTime = 0f;
        mSecPerFrame = ScrollerGame.S_PER_FRAME;

        play();
    }

    @Override
    public boolean update(final float deltaTime) {
        if (mIsPlaying && mTextureRegions != null && mTextureRegions.length > 0) {
            mElapsedTime += deltaTime;
            while (mElapsedTime >= mSecPerFrame) {
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
            mCurrentFrame = frame;
            return;
        }

        frame %= mTextureRegions.length;
        setTextureRegion(mTextureRegions[frame]);
        mCurrentFrame = frame;

        if (mIsPlaying && mCurrentFrame == mTargetFrame) {
            stop();
        }
    }

    public void setTextureRegions(final TextureRegion... frames) {
        mTextureRegions = frames;
        setFrame(0);
    }

    public void play() {
        mIsPlaying = true;
        mTargetFrame = -1;

        setFrame(0);
    }

    public void play(final int targetFrame) {
        mIsPlaying = true;
        mTargetFrame = targetFrame;
    }

    public void play(final int fromFrame, final int targetFrame) {
        mIsPlaying = true;
        mTargetFrame = targetFrame;

        setFrame(fromFrame);
    }

    public void stop() {
        final boolean was = mIsPlaying;
        mIsPlaying = false;

        if (was && mListener != null) {
            mListener.onStop(this);
        }
    }

    public void stopAt(final int frame) {
        stop();

        setFrame(frame);
    }

    public void setListener(final Listener listener) {
        mListener = listener;
    }

    public static interface Listener {
        public void onStop(Animated animated);
    }
}
