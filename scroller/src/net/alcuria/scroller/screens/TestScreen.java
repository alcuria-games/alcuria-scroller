/**
 * 
 */
package net.alcuria.scroller.screens;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.Animated;
import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.renderables.Renderable;
import net.alcuria.scroller.renderables.ScrollingBackground;
import net.alcuria.scroller.utils.AlcuriaTextureRegionFactory;
import net.alcuria.scroller.utils.ScrollerInputProcessor.TouchEvent;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * @author juni.kim
 */
public class TestScreen extends AlcuriaScreen {
    private TextureRegion[] mParticleTrs;
    private RenderGroup mParticleLayer;
    private Renderable mThing;
    private Vector2 mTouchDownPoint;
    private Vector2 mAnchorPoint;

    public TestScreen() {
        super();

        setPosition(0, 0);

        loadTexture("bg/0.png");
        loadTexture("bg/1.png");
        loadTexture("bg/2.png");
        loadTexture("bg/3.png");
        loadTexture("bg/4.png");
        loadTexture("particle.png");
        loadTexture("thing.png");

        ScrollingBackground bg = new ScrollingBackground();
        for (int i = 0; i < 3; i++) {
            Renderable sprite = new Renderable();
            sprite.setTextureRegion(new TextureRegion(getTexture(String.format("bg/%d.png", i))));
            bg.addChild(sprite);
        }
        bg.setScrollSpeed(20f);
        bg.setSize(ScrollerGame.DESIGNED_WIDTH, ScrollerGame.DESIGNED_HEIGHT);
        addChild(bg);

        mParticleTrs = AlcuriaTextureRegionFactory.createGridTextureRegions(getTexture("particle.png"), 64, 64);
        mParticleLayer = new RenderGroup();
        addChild(mParticleLayer);

        mThing = new Renderable();
        mThing.setTextureRegion(new TextureRegion(getTexture("thing.png")));
        mThing.setSize(64, 64);
        mThing.setPosition(180f - 32f, 180f - 32f);
        addChild(mThing);

        mTouchDownPoint = new Vector2();
        mAnchorPoint = new Vector2();
    }

    @Override
    public void dispose() {
        TestSprite.POOL.clear();
        super.dispose();
    }

    @Override
    public boolean onTouchEvent(final TouchEvent event, final float x, final float y) {
        if (mIsPaused) {
            return false;
        }

        if (event == TouchEvent.DOWN) {
            mTouchDownPoint.set(x, y);
            mAnchorPoint.set(mThing.getPosition());
        } else {
            float diffX = x - mTouchDownPoint.x;
            float diffY = y - mTouchDownPoint.y;

            mThing.setPosition(mAnchorPoint.x + diffX * 2f, mAnchorPoint.y + diffY * 2f);

            float size = MathUtils.random(4f, 16f);
            float clipX = mThing.getPosition().x + mThing.getSize().x / 2f;
            float clipY = mThing.getPosition().y + mThing.getSize().y;
            Animated clip = TestSprite.POOL.obtain();
            clip.setTextureRegions(mParticleTrs);
            clip.setPosition(clipX, clipY);
            clip.setSize(size, size);
            clip.setFps(1);
            clip.setFrame(MathUtils.random(3));
            mParticleLayer.addChild(clip);
        }

        return true;
    }

    private static class TestSprite extends Animated implements Poolable {
        private final static Pool<TestSprite> POOL = new Pool<TestSprite>() {
            @Override
            protected TestSprite newObject() {
                return new TestSprite();
            }
        };

        @Override
        public boolean update(final float deltaTime) {
            float x = getPosition().x;
            float y = getPosition().y + getSize().y * deltaTime * 10f;
            setPosition(x, y);

            if (y >= ScrollerGame.DESIGNED_HEIGHT + getSize().y) {
                return false;
            }

            return super.update(deltaTime);
        }

        @Override
        public void onRemoved() {
            super.onRemoved();
            POOL.free(this);
        }

        @Override
        public void reset() {
            mTextureRegion = null;
            mTextureRegions = null;
            mElapsedTime = 0;
            mSecPerFrame = ScrollerGame.S_PER_FRAME;
            mCurrentFrame = 0;

            setPosition(0, 0);
            setSize(0, 0);
        }
    }
}
