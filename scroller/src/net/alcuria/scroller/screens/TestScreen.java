/**
 * 
 */
package net.alcuria.scroller.screens;

import net.alcuria.scroller.renderables.Renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author juni.kim
 */
public class TestScreen extends AlcuriaScreen {
    private TextureRegion trBg;

    private float mPhase;

    public TestScreen() {
        super();

        loadTexture("bg.png");

        trBg = new TextureRegion(getTexture("bg"));

        Renderable bg = new Renderable();
        bg.setTextureRegion(trBg);
        bg.setSize(getSize());
        addChild(bg);
    }

    @Override
    public boolean update(final float deltaTime) {
        mPhase += deltaTime;
        mPhase %= MathUtils.PI * 2;
        setPosition(MathUtils.cos(mPhase), MathUtils.sin(mPhase));

        return super.update(deltaTime);
    }
}
