/**
 * 
 */
package net.alcuria.scroller.test;

import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.renderables.Renderable;

/**
 * @author juni.kim
 */
public class ScrollingBackground extends RenderGroup {
    private float mOffset = 0f;
    private float mScrollSpeed = 0f;

    @Override
    public boolean update(final float deltaTime) {
        mOffset += mScrollSpeed * deltaTime;
        float height = mRenderables.get(0).getSize().y;
        if (mOffset >= height) {
            mRenderables.add(mRenderables.remove(0));
            mOffset -= height;
        }

        float y = -mOffset;
        for (Renderable child : mRenderables) {
            child.setPosition(child.getPosition().x, y);
            y += child.getSize().y;
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
}
