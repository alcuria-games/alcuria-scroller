/**
 * 
 */
package net.alcuria.scroller.play;

import net.alcuria.scroller.AlcuriaScreen;
import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.RenderGroup;
import net.alcuria.scroller.utils.AlcuriaTextureRegionFactory;
import net.alcuria.scroller.utils.ScrollerInputProcessor.TouchEvent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author juni.kim
 */
public class PlayScreen extends AlcuriaScreen {
    private TextureRegion[][] mBulletTrs;
    private TextureRegion[] mBallTrs;
    private TextureRegion[] mRayTrs;
    private TextureRegion[] mRedRayTrs;
    private TextureRegion[] mArcTrs;

    private TextureRegion[] mBatTrs;

    private RenderGroup mParticleLayer;
    private RenderGroup mMonsterLayer;
    private RenderGroup mBulletLayer;
    private Hal mHal;
    private Vector2 mTouchDownPoint;
    private Vector2 mAnchorPoint;

    private float mTimeFactor;

    public PlayScreen() {
        super();

        setPosition(0, 0);

        loadTexture("particle.png");
        loadTexture("thing.png");
        loadTexture("sprites/bullets.png");
        loadTexture("sprites/bat.png");
        loadTexture("hal/head.png");
        loadTexture("hal/body.png");

        ScrollingBackground bg = new ScrollingBackground();
        bg.setScrollSpeed(20f);
        bg.setTextureRegions(AlcuriaTextureRegionFactory.loadDirectory(this, "bg"));
        bg.initialize();
        addChild(bg);

        mParticleLayer = new RenderGroup();
        addChild(mParticleLayer);

        mMonsterLayer = new RenderGroup();
        addChild(mMonsterLayer);

        Texture t = getTexture("sprites/bullets.png");
        mBulletTrs = new TextureRegion[4][36];
        for (int c = 0; c < 4; c++) {
            int cx = (c % 2 == 0) ? 0 : 96;
            int cy = (c < 2) ? 0 : 96;

            for (int y = 0; y < 6; y++) {
                for (int x = 0; x < 6; x++) {
                    mBulletTrs[c][y * 6 + x] = new TextureRegion(t, x * 16 + cx, y * 16 + cy, 16, 16);
                }
            }
        }
        mBulletLayer = new RenderGroup();
        addChild(mBulletLayer);

        mBallTrs = AlcuriaTextureRegionFactory.getFrames(mBulletTrs[0], 0, 1, 2, 3, 4, 5);
        mRayTrs = AlcuriaTextureRegionFactory.getFrames(mBulletTrs[1], 6, 7, 8, 9, 10, 11);
        mRedRayTrs = AlcuriaTextureRegionFactory.getFrames(mBulletTrs[2], 6, 7, 8, 9, 10, 11);
        mArcTrs = AlcuriaTextureRegionFactory.getFrames(mBulletTrs[2], 12, 13, 14, 15, 16, 17);

        mBatTrs = AlcuriaTextureRegionFactory.createGridTextureRegions(getTexture("sprites/bat.png"), 64, 64);

        mHal = new Hal(this);
        mHal.createTextureRegions(getTexture("hal/head.png"), getTexture("hal/body.png"));
        mHal.setPosition(180f - 32f, 180f - 32f);
        addChild(mHal);

        mTouchDownPoint = new Vector2();
        mAnchorPoint = new Vector2();

        mTimeFactor = 1f;
    }

    @Override
    public void dispose() {
        BackgroundPiece.POOL.clear();
        Bullet.POOL.clear();
        Monster.POOL.clear();
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
            mTimeFactor = 1f;
        } else {
            float diffX = x - mTouchDownPoint.x;
            float diffY = y - mTouchDownPoint.y;

            mHal.setPosition(mAnchorPoint.x + diffX * 1.5f, mAnchorPoint.y + diffY * 1.5f);

            if (event == TouchEvent.UP) {
                mHal.setFiring(false);
                mHal.setWeapon(mHal.getWeapon() + 1);
                mTimeFactor = 0.25f;
            }
        }

        return true;
    }

    @Override
    public boolean update(final float deltaTime) {
        if (MathUtils.random() <= 0.0625f) {
            Monster monster = Monster.newInstance(Monster.BAT, mHal);
            monster.setTextureRegions(mBatTrs);
            monster.setPosition(MathUtils.random(ScrollerGame.DESIGNED_WIDTH), ScrollerGame.DESIGNED_HEIGHT - 1);

            float scale = MathUtils.random(0.4f, 0.75f);
            monster.setScale(scale, scale);
            monster.setFrame(MathUtils.random(mBatTrs.length - 1));
            mMonsterLayer.addChild(monster);
        }

        return super.update(deltaTime * mTimeFactor);
    }

    public void fireShot() {
        float clipX = mHal.getPosition().x + mHal.getSize().x * 0.85f;
        float clipY = mHal.getPosition().y + mHal.getSize().y;

        int weapon = mHal.getWeapon();

        Bullet effect;
        Bullet bullet;
        float scale;
        switch (weapon) {
            case Bullet.BALL:
                bullet = Bullet.newInstance(weapon, false);
                bullet.setTextureRegions(mBallTrs);
                bullet.setPosition(clipX - 8f, clipY - 8f);
                mBulletLayer.addChild(bullet);

                for (int i = 0; i < 5; i++) {
                    scale = MathUtils.random(0.1251f, 0.25f);

                    effect = Bullet.newInstance(weapon, true);
                    effect.setTextureRegions(mBallTrs);
                    effect.setScale(scale, scale);
                    effect.setPosition(clipX - 8f, clipY - 8f);
                    effect.setRotation(MathUtils.random(360));
                    mParticleLayer.addChild(effect);
                }

                break;
            case Bullet.RAY:
                bullet = Bullet.newInstance(weapon, false);
                bullet.setTextureRegions(mRayTrs);
                bullet.setPosition(clipX - 8f, clipY - 8f);
                mBulletLayer.addChild(bullet);

                for (int i = 0; i < 5; i++) {
                    scale = MathUtils.random(0.25f, 0.5f);

                    effect = Bullet.newInstance(weapon, true);
                    effect.setTextureRegions(mRayTrs);
                    effect.setScale(scale, scale);
                    effect.setPosition(clipX + MathUtils.random(-16f, 16f), clipY + MathUtils.random(-16f, 16f));
                    effect.setRotation(MathUtils.random(-45f, 45f));
                    mParticleLayer.addChild(effect);
                }
                break;
            case Bullet.ARC:
                for (int i = -2; i <= 2; i++) {
                    scale = 3 - 0.5f * i * i;
                    bullet = Bullet.newInstance(weapon, false);
                    bullet.setTextureRegions(mArcTrs);
                    bullet.setPosition(clipX - 8f * scale, clipY - 8f * scale);
                    bullet.setScale(scale, scale);
                    bullet.setRotation(i * 15f);
                    mBulletLayer.addChild(bullet);
                }

                for (int i = 0; i < 3; i++) {
                    effect = Bullet.newInstance(weapon, false);
                    effect.setTextureRegions(mRedRayTrs);
                    effect.setScale(0.5f, 1.25f);
                    effect.setRotation(i * 15f + 7.5f);
                    effect.setPosition(clipX - 8f, clipY - 8f);
                    mParticleLayer.addChild(effect);

                    effect = Bullet.newInstance(weapon, false);
                    effect.setTextureRegions(mRedRayTrs);
                    effect.setScale(0.5f, 1.25f);
                    effect.setRotation(i * -15f - 7.5f);
                    effect.setPosition(clipX - 4f, clipY - 10f);
                    mParticleLayer.addChild(effect);
                }
                break;
        }

    }
}
