package net.alcuria.scroller;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(final String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "scroller";
        cfg.useGL20 = false;
        cfg.width = (int) ScrollerGame.DESIGNED_WIDTH;
        cfg.height = (int) ScrollerGame.DESIGNED_HEIGHT;

        new LwjglApplication(ScrollerGame.getInstance(), cfg);
    }
}
