package ui.src.main.java.tim;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public class CustomFont {

    private final String DEFAULT_FONT_FILE_NAME = "FreeSans.ttf";
    private Font font;

    public CustomFont() {
        try {
            final ClassLoader classLoader = this.getClass().getClassLoader();
            final InputStream ttfFile = classLoader
                .getResourceAsStream(DEFAULT_FONT_FILE_NAME);

            final GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
            font = Font.createFont(Font.TRUETYPE_FONT, ttfFile);
            ge.registerFont(font);
        } catch(Exception e) {
            System.out.println("[ERROR] Could not import font. "
                + "Reverting to default");
            font = new Font("default", Font.PLAIN, 12);
System.out.println(e.getMessage());
        }
    }

    public Font getFont(final int style, final float size) {
        return font.deriveFont(style, size);
    }

    public Font getFont(final float size) {
        return getFont(Font.PLAIN, size);
    }

    public Font getFont() {
        return getFont(Font.PLAIN, 12);
    }
}