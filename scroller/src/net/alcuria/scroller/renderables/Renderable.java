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
    protected final Vector2 mPosition;
    protected final Vector2 mSize;

    protected TextureRegion mTextureRegion;

    protected Container mParent;

    public Renderable() {
        mPosition = new Vector2();
        mSize = new Vector2();
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

    public boolean update(final float deltaTime) {
        return true;
    }

    public void draw(final SpriteBatch batch) {
        if (getTextureRegion() != null) {
            float x = getPosition().x;
            float y = getPosition().y;
            Container parent = getParent();
            if (parent != null && parent instanceof Renderable) {
                x += ((Renderable) parent).getPosition().x;
                y += ((Renderable) parent).getPosition().y;
            }
            batch.draw(getTextureRegion(), x, y, getSize().x, getSize().y);
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
    }

    public void resume() {
    }

    public void setTextureRegion(final TextureRegion region) {
        mTextureRegion = region;
    }

    final public TextureRegion getTextureRegion() {
        return mTextureRegion;
    }
}
