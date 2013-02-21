/**
 * 
 */
package net.alcuria.scroller.play;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.renderables.Renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author juni.kim
 */
public class ScrollingBackground extends RenderGroup {
    private TextureRegion[] mTextureRegions;
    private float mOffset = 0f;
    private float mScrollSpeed = 0f;
    private float mChildrenHeight;
    private boolean mIsInitialized;

    public ScrollingBackground() {
        super();

        setSize(ScrollerGame.DESIGNED_WIDTH, ScrollerGame.DESIGNED_HEIGHT);
    }

    @Override
    public boolean update(final float deltaTime) {
        if (mIsInitialized && mRenderables.size() > 0) {
            mOffset += mScrollSpeed * deltaTime;
            float height = mRenderables.get(0).getSize().y;
            if (mOffset >= height) {
                mOffset -= height;

                mRenderables.remove(0);
                mChildrenHeight -= height;
                addPieces();
            }

            float y = -mOffset;
            for (Renderable child : mRenderables) {
                child.setPosition(child.getPosition().x, y);
                y += child.getSize().y;
            }
        }

        return super.update(deltaTime);
    }

    @Override
    public void setSize(final float w, final float h) {
        super.setSize(w, h);

        for (Renderable child : mRenderables) {
            float scale = w / child.getSize().x;
            child.setScale(scale * child.getScale().x, scale * child.getScale().y);
        }
    }

    public void setScrollSpeed(final float scrollSpeed) {
        mScrollSpeed = scrollSpeed;
    }

    public void setTextureRegions(final TextureRegion... textureRegions) {
        mTextureRegions = textureRegions;
    }

    public void initialize() {
        if (mTextureRegions == null) {
            throw new IllegalStateException("initialize cannot be called before setTextureRegions");
        }

        clear();
        mChildrenHeight = 0f;
        addPieces();

        mIsInitialized = true;
    }

    private int getNext() {
        return MathUtils.random(mTextureRegions.length - 1);
    }

    private void addPieces() {
        float smallest = Float.MAX_VALUE;
        while (mChildrenHeight < mSize.y + smallest) {
            BackgroundPiece child = BackgroundPiece.POOL.obtain();
            child.setBackground(mTextureRegions[getNext()]);

            float scale = mSize.x / child.getSize().x;
            child.setScale(scale, scale);

            float height = child.getSize().y;
            if (height < smallest) {
                smallest = height;
            }

            mChildrenHeight += height;
            addChild(child);
        }
    }
}
