/**
 * 
 */
package net.alcuria.scroller.play;

import net.alcuria.scroller.AlcuriaScreen;
import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.renderables.Renderable;
import net.alcuria.scroller.utils.AlcuriaTextureRegionFactory;
import net.alcuria.scroller.utils.ScrollerInputProcessor.TouchEvent;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author juni.kim
 */
public class PlayScreen extends AlcuriaScreen {
    private TextureRegion[] mParticleTrs;
    private RenderGroup mParticleLayer;
    private Hal mHal;
    private Vector2 mTouchDownPoint;
    private Vector2 mAnchorPoint;

    public PlayScreen() {
        super();

        setPosition(0, 0);

        loadTexture("particle.png");
        loadTexture("thing.png");
        loadTexture("hal/head.png");
        loadTexture("hal/body.png");

        ScrollingBackground bg = new ScrollingBackground();
        bg.setScrollSpeed(20f);
        bg.setTextureRegions(AlcuriaTextureRegionFactory.loadDirectory(this, "bg"));
        bg.initialize();
        addChild(bg);

        mParticleTrs = AlcuriaTextureRegionFactory.createGridTextureRegions(getTexture("particle.png"), 64, 64);
        mParticleLayer = new RenderGroup();
        addChild(mParticleLayer);

        mHal = new Hal(this);
        mHal.createTextureRegions(getTexture("hal/head.png"), getTexture("hal/body.png"));
        mHal.setPosition(180f - 32f, 180f - 32f);
        addChild(mHal);

        mTouchDownPoint = new Vector2();
        mAnchorPoint = new Vector2();
    }

    @Override
    public void dispose() {
        TestSprite.POOL.clear();
        BackgroundPiece.POOL.clear();
        super.dispose();
    }

    @Override
    public boolean onTouchEvent(final TouchEvent event, final float x, final float y) {
        if (mIsPaused) {
            return false;
        }

        if (event == TouchEvent.DOWN) {
            mTouchDownPoint.set(x, y);
            mAnchorPoint.set(mHal.getPosition());
            mHal.setFiring(true);
        } else {
            float diffX = x - mTouchDownPoint.x;
            float diffY = y - mTouchDownPoint.y;

            mHal.setPosition(mAnchorPoint.x + diffX * 1.5f, mAnchorPoint.y + diffY * 1.5f);

            if (event == TouchEvent.UP) {
                mHal.setFiring(false);
                mHal.setWeapon(mHal.getWeapon() + 1);
            }
        }

        return true;
    }

    public void fireShot() {
        float clipX = mHal.getPosition().x + mHal.getSize().x / 2f;
        float clipY = mHal.getPosition().y + mHal.getSize().y;

        int weapon = mHal.getWeapon();
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
                float diff = (mHal.getSize().x - 8f) / 10f;
                float x = mHal.getPosition().x;
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
