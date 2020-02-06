package de.pxav.finate.gui.component;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class RoundedButtonBorder extends AbstractBorder {

  private final Color color;
  private final Color backgroundColor;
  private final int borderThickness;
  private final int borderRadius;
  private final int strokePad;
  private final Insets insets;
  private final BasicStroke stroke;
  private final RenderingHints hints;

  public RoundedButtonBorder(Color color,
                             Color backgroundColor,
                             int borderThickness,
                             int borderRadius) {
    this.borderThickness = borderThickness;
    this.borderRadius = borderRadius;
    this.color = color;
    this.backgroundColor = backgroundColor;

    stroke = new BasicStroke(borderThickness);
    strokePad = borderThickness / 2;

    hints = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
    );

    int pad = borderRadius + strokePad;
    int bottomPad = pad + strokePad;
    insets = new Insets(pad, pad, bottomPad, pad);
  }

  @Override
  public Insets getBorderInsets(Component c) {
    return insets;
  }

  @Override
  public Insets getBorderInsets(Component c, Insets insets) {
    return getBorderInsets(c);
  }

  @Override
  public void paintBorder(Component c,
                          Graphics graphics,
                          int x, int y,
                          int width,
                          int height) {

    Graphics2D graphics2D = (Graphics2D) graphics;
    Shape clip = graphics2D.getClip();

    int bottomLineY = height - borderThickness;

    RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
            strokePad,
            strokePad,
            width - borderThickness,
            bottomLineY,
            borderRadius,
            borderRadius);

    Area area = new Area(bubble);
    graphics2D.setRenderingHints(hints);

    Rectangle rect = new Rectangle(0, 0, width, height);
    Area borderRegion = new Area(rect);
    borderRegion.subtract(area);
    graphics2D.setClip(borderRegion);
    graphics2D.setColor(backgroundColor);
    graphics2D.fillRect(0, 0, width + 30, height+10);
    graphics2D.setClip(null);

    graphics2D.setClip(clip);
    graphics2D.setColor(color);
    graphics2D.setStroke(stroke);
    graphics2D.draw(area);
  }
}
