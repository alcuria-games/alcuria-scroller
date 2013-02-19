/**
 * 
 */
package net.alcuria.scroller.screens;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.renderables.Renderable;
import net.alcuria.scroller.renderables.ScrollingBackground;
import net.alcuria.scroller.renderables.TestSprite;
import net.alcuria.scroller.renderables.Thing;
import net.alcuria.scroller.utils.AlcuriaTextureRegionFactory;
import net.alcuria.scroller.utils.ScrollerInputProcessor.TouchEvent;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author juni.kim
 */
public class TestScreen extends AlcuriaScreen {
    private TextureRegion[] mParticleTrs;
    private RenderGroup mParticleLayer;
    private Thing mThing;
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
        bg.setPosition(0, 0);
        bg.setSize(ScrollerGame.DESIGNED_WIDTH, ScrollerGame.DESIGNED_HEIGHT);
        addChild(bg);

        mParticleTrs = AlcuriaTextureRegionFactory.createGridTextureRegions(getTexture("particle.png"), 64, 64);
        mParticleLayer = new RenderGroup();
        addChild(mParticleLayer);

        mThing = new Thing(this);
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
            mThing.setFiring(true);
        } else {
            float diffX = x - mTouchDownPoint.x;
            float diffY = y - mTouchDownPoint.y;

            mThing.setPosition(mAnchorPoint.x + diffX * 1.5f, mAnchorPoint.y + diffY * 1.5f);

            if (event == TouchEvent.UP) {
                mThing.setFiring(false);
                mThing.setWeapon(mThing.getWeapon() + 1);
            }
        }

        return true;
    }

    public void fireShot() {
        float clipX = mThing.getPosition().x + mThing.getSize().x / 2f;
        float clipY = mThing.getPosition().y + mThing.getSize().y;

        int weapon = mThing.getWeapon();
        Renderable sprite;
        switch (weapon) {
            case 0:
                sprite = TestSprite.POOL.obtain();
                sprite.setTextureRegion(mParticleTrs[weapon]);
                sprite.setPosition(clipX, clipY);
                sprite.setSize(32f, 32f);
                mParticleLayer.addChild(sprite);
                break;
            case 1:
                sprite = TestSprite.POOL.obtain();
                sprite.setTextureRegion(mParticleTrs[weapon]);
                sprite.setPosition(clipX - 32f, clipY);
                sprite.setSize(16f, 16f);
                mParticleLayer.addChild(sprite);

                sprite = TestSprite.POOL.obtain();
                sprite.setTextureRegion(mParticleTrs[weapon]);
                sprite.setPosition(clipX + 32f, clipY);
                sprite.setSize(16f, 16f);
                mParticleLayer.addChild(sprite);
                break;
            case 2:
                for (int i = -2; i <= 2; i++) {
                    sprite = TestSprite.POOL.obtain();
                    sprite.setTextureRegion(mParticleTrs[weapon]);
                    sprite.setPosition(clipX + i * 16f, clipY);
                    sprite.setSize(10f, 10f + i * i * 5f);
                    mParticleLayer.addChild(sprite);
                }
                break;
            case 3:
                float diff = (mThing.getSize().x - 8f) / 10f;
                float x = mThing.getPosition().x;
                for (int i = 0; i < 10; i++) {
                    sprite = TestSprite.POOL.obtain();
                    sprite.setTextureRegion(mParticleTrs[weapon]);
                    sprite.setPosition(x + diff * i, clipY);
                    sprite.setSize(8f, 8f);
                    mParticleLayer.addChild(sprite);
                }
                break;
        }
    }
}
