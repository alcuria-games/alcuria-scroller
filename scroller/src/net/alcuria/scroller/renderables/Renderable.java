/**
 * 
 */
package net.alcuria.scroller.renderables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author juni.kim
 */
public class Renderable {
    public boolean mIsPaused;

    protected final Vector2 mPosition;
    protected final Vector2 mSize;
    protected final Vector2 mScale;
    protected TextureRegion mTextureRegion;
    protected Container mParent;
    protected float mRotation;

    public Renderable() {
        mPosition = new Vector2();
        mSize = new Vector2();
        mScale = new Vector2(1.0f, 1.0f);
        mRotation = 0.0f;
    }

    public void setPosition(final float x, final float y) {
        mPosition.set(x, y);
    }

    final public void setPosition(final Vector2 position) {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }

        setPosition(position.x, position.y);
    }

    public Vector2 getPosition() {
        return mPosition;
    }

    public void setSize(final float w, final float h) {
        mSize.set(w, h);

        if (mTextureRegion != null) {
            float scaleX = w / mTextureRegion.getRegionWidth();
            float scaleY = h / mTextureRegion.getRegionHeight();
            mScale.set(scaleX, scaleY);
        }
    }

    final public void setSize(final Vector2 size) {
        if (size == null) {
            throw new IllegalArgumentException("size cannot be null");
        }

        setSize(size.x, size.y);
    }

    public Vector2 getSize() {
        return mSize;
    }

    public void setScale(final float xScale, final float yScale) {
        mScale.set(xScale, yScale);

        if (mTextureRegion != null) {
            float width = mTextureRegion.getRegionWidth() * xScale;
            float height = mTextureRegion.getRegionHeight() * yScale;
            mSize.set(width, height);
        }
    }

    final public void setScale(final Vector2 scale) {
        if (scale == null) {
            throw new IllegalArgumentException("scale cannot be null");
        }

        setScale(scale.x, scale.y);
    }

    public Vector2 getScale() {
        return mScale;
    }

    public boolean update(final float deltaTime) {
        return true;
    }

    public void draw(final SpriteBatch batch) {
        if (mTextureRegion != null) {
            float x = getPosition().x;
            float y = getPosition().y;
            Container parent = getParent();
            if (parent != null && parent instanceof Renderable) {
                x += ((Renderable) parent).getPosition().x;
                y += ((Renderable) parent).getPosition().y;
            }
            batch.draw(getTextureRegion(), x, y, mSize.x / 2f, mSize.y / 2f, mSize.x, mSize.y, 1.0f, 1.0f, mRotation);
        }
    }

    public Container getParent() {
        return mParent;
    }

    public void onAdded(final Container parent) {
        mParent = parent;
    }

    public void onRemoved() {
        mParent = null;
    }

    public void pause() {
        mIsPaused = true;
    }

    public void resume() {
        mIsPaused = false;
    }

    public void setTextureRegion(final TextureRegion region) {
        mTextureRegion = region;

        if (region != null && mSize.x == 0 && mSize.y == 0) {
            setSize(region.getRegionWidth(), region.getRegionHeight());
        }
    }

    final public TextureRegion getTextureRegion() {
        return mTextureRegion;
    }
}
