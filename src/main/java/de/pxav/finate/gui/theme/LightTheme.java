package de.pxav.finate.gui.theme;

import java.awt.*;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class LightTheme extends WindowTheme {

  @Override
  public String themeName() {
    return "Light theme";
  }

  public void defineColors() {
    colors.put(WindowElement.BACKGROUND, new Color(255, 255, 255));

    colors.put(WindowElement.PRIMARY_BUTTON, new Color(21, 188, 222));
    colors.put(WindowElement.SECONDARY_BUTTON, new Color(177, 177, 177));

    colors.put(WindowElement.TEXT, new Color(245, 245, 245));
    colors.put(WindowElement.SECONDARY_TEXT, new Color(17, 17, 17));
  }

}
