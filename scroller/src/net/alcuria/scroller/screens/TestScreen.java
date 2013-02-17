/**
 * 
 */
package net.alcuria.scroller.screens;

import net.alcuria.scroller.ScrollerGame;
import net.alcuria.scroller.renderables.Animated;
import net.alcuria.scroller.renderables.Renderable;
import net.alcuria.scroller.utils.AlcuriaTextureRegionFactory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author juni.kim
 */
public class TestScreen extends AlcuriaScreen {
    private TextureRegion trBg;

    public TestScreen() {
        super();

        setPosition(0, 0);

        loadTexture("bg.png");
        loadTexture("particle.png");

        trBg = new TextureRegion(getTexture("bg.png"));

        Renderable sprite = new Renderable();
        sprite.setTextureRegion(trBg);
        sprite.setSize(getSize());
        sprite.setPosition(0, 0);
        addChild(sprite);

        for (int i = 0; i < 50; i++) {
            float size = MathUtils.random(4f, 16f);
            Animated clip = new TestSprite();
            clip.setTextureRegions(AlcuriaTextureRegionFactory.createGridTextureRegions(getTexture("particle.png"), 64, 64));
            clip.setPosition(MathUtils.random(ScrollerGame.DESIGNED_WIDTH), MathUtils.random(ScrollerGame.DESIGNED_HEIGHT));
            clip.setSize(size, size);
            clip.setFps(1);
            clip.setFrame(MathUtils.random(3));
            addChild(clip);
        }
    }

    private static class TestSprite extends Animated {
        @Override
        public boolean update(final float deltaTime) {
            float x = getPosition().x - getSize().x * deltaTime;
            float y = getPosition().y - getSize().y * deltaTime;

            if (x <= -getSize().x) {
                x = ScrollerGame.DESIGNED_WIDTH + getSize().x;
            }

            if (y <= -getSize().y) {
                y = ScrollerGame.DESIGNED_HEIGHT + getSize().y;
            }

            setPosition(x, y);

            return super.update(deltaTime);
        }
    }
}
