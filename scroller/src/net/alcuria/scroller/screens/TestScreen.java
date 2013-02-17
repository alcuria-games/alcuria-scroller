/**
 * 
 */
package net.alcuria.scroller.screens;

import net.alcuria.scroller.renderables.Renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author juni.kim
 */
public class TestScreen extends AlcuriaScreen {
    private TextureRegion trBg;

    private float mPhase;

    public TestScreen() {
        super();

        setPosition(0, 0);

        loadTexture("bg.png");

        trBg = new TextureRegion(getTexture("bg.png"));

        Renderable bg = new Renderable();
        bg.setTextureRegion(trBg);
        bg.setSize(getSize());
        bg.setPosition(0, 0);
        addChild(bg);
    }

    @Override
    public boolean update(final float deltaTime) {

        return super.update(deltaTime);
    }
}
