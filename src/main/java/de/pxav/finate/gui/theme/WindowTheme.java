package de.pxav.finate.gui.theme;

import com.google.common.collect.Maps;

import java.awt.*;
import java.util.Map;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public abstract class WindowTheme {

  protected Map<WindowElement, Color> colors = Maps.newHashMap();

  public abstract String themeName();

  public abstract void defineColors();

  public Color getColor(WindowElement windowElement) {
    return this.colors.get(windowElement);
  }

}
