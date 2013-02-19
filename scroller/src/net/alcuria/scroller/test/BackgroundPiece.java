/**
 * 
 */
package net.alcuria.scroller.test;

import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.renderables.Renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * @author Juni
 */
public class BackgroundPiece extends RenderGroup implements Poolable {
    public final static Pool<BackgroundPiece> POOL = new Pool<BackgroundPiece>() {
        @Override
        protected BackgroundPiece newObject() {
            return new BackgroundPiece();
        }
    };

    private Renderable mBackground;

    public BackgroundPiece() {
        mBackground = new Renderable();
        addChild(mBackground);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        POOL.free(this);
    }

    @Override
    public Vector2 getSize() {
        return mBackground.getSize();
    }

    public void setBackground(final TextureRegion region) {
        mBackground.setTextureRegion(region);
        setSize(mBackground.getSize());
    }

    @Override
    public void reset() {
        clear();

        mBackground.setTextureRegion(null);
        mBackground.setSize(0, 0);
        mBackground.setScale(1, 1);
        addChild(mBackground);
    }
}
