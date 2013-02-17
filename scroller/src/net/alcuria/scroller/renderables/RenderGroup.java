/**
 * 
 */
package net.alcuria.scroller.renderables;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author juni.kim
 */
public class RenderGroup extends Renderable implements Container {
    protected List<Renderable> mRenderables = new LinkedList<Renderable>();

    @Override
    public boolean update(final float deltaTime) {
        Iterator<Renderable> itr = mRenderables.iterator();
        while (itr.hasNext()) {
            Renderable child = itr.next();
            if (!child.mIsPaused && !child.update(deltaTime)) {
                itr.remove();
                child.onRemoved();
            }
        }

        return super.update(deltaTime);
    }

    @Override
    public void draw(final SpriteBatch batch) {
        super.draw(batch);
        for (Renderable child : mRenderables) {
            child.draw(batch);
        }
    }

    @Override
    public void pause() {
        for (Renderable child : mRenderables) {
            child.pause();
        }

        super.pause();
    }

    @Override
    public void resume() {
        for (Renderable child : mRenderables) {
            child.resume();
        }

        super.resume();
    }

    @Override
    public boolean addChild(final Renderable child) {
        if (mRenderables.add(child)) {
            child.onAdded(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeChild(final Renderable child) {
        if (mRenderables.remove(child)) {
            child.onRemoved();
            return true;
        }
        return false;
    }

    @Override
    public Renderable getChild(final Renderable child) {
        for (Renderable myChild : mRenderables) {
            if (myChild.equals(child)) {
                return myChild;
            }
        }
        return null;
    }
}
