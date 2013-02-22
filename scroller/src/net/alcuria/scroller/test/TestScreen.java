/**
 * 
 */
package net.alcuria.scroller.test;

import net.alcuria.scroller.AlcuriaScreen;
import net.alcuria.scroller.renderables.Animated;
import net.alcuria.scroller.renderables.RenderGroup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Juni
 */
public class TestScreen extends AlcuriaScreen {
    private TextureRegion[] mBase;
    private TextureRegion[] mPillar;
    private TextureRegion[] mHead;

    public TestScreen() {
        super();

        setPosition(0, 0);

        loadTexture("sprites/bullets.png");
        Texture t = getTexture("sprites/bullets.png");

        mBase = new TextureRegion[6];
        mHead = new TextureRegion[6];
        mPillar = new TextureRegion[6];

        for (int x = 0; x < 6; x++) {
            mHead[x] = new TextureRegion(t, x * 16, 48, 16, 16);
            mPillar[x] = new TextureRegion(t, x * 16, 64, 16, 16);
            mBase[x] = new TextureRegion(t, x * 16, 80, 16, 16);
        }

        RenderGroup group = new RenderGroup() {
            float mPhase;

            @Override
            public boolean update(final float deltaTime) {
                mPhase += deltaTime;
                setPosition(100f * MathUtils.cos(mPhase) + 150f, 100f * MathUtils.sin(mPhase) + 150f);

                return super.update(deltaTime);
            }
        };

        Animated a = new Animated();
        a.setTextureRegions(mBase);
        a.setPosition(0, 0);
        a.setSize(64, 64);
        group.addChild(a);

        // a = new Animated();
        // a.setTextureRegions(mPillar);
        // a.setPosition(0, 64);
        // a.setSize(64, 64);
        // group.addChild(a);
        //
        // a = new Animated();
        // a.setTextureRegions(mPillar);
        // a.setPosition(0, 128);
        // a.setSize(64, 64);
        // group.addChild(a);
        //
        // a = new Animated();
        // a.setTextureRegions(mPillar);
        // a.setPosition(0, 192);
        // a.setSize(64, 64);
        // group.addChild(a);

        a = new Animated();
        a.setTextureRegions(mHead);
        a.setPosition(0, 64);
        a.setSize(64, 64);
        group.addChild(a);

        addChild(group);
    }
}
