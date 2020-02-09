package de.pxav.finate.gui.component;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * This class is an own implementation of an {@code AbstractBorder}.
 *
 * It is mostly used in combination with the {@code ModernButton}
 * component, because it contributes to a more modern look of buttons.
 *
 * The effect itself is comparable with the {@code border-radius} property
 * in CSS or similar style languages, which basically means that the corners
 * of the button will become rounded and in the end the button will
 * have two semicircles on the right and the left.
 *
 * @author pxav
 * @see AbstractBorder
 * @see ModernButton
 */
public class RoundedButtonBorder extends AbstractBorder {

  // Around the rounded corners there will
  // be a border which you can control via the
  // {@code borderThickness}.
  private final Color color;

  // the function of each attribute is explained in the
  // constructor documentation
  private final Color backgroundColor;
  private final int borderThickness;
  private final int borderRadius;
  private final int strokePad;
  private final Insets insets;
  private final BasicStroke stroke;
  private final RenderingHints hints;

  /**
   * This is the constructor used to build the border for your button.
   * The values which have to be injected are explained below.
   *
   * @param color             The rounded corners will be surrounded by a border,
   *                          which smooths it up so that the individual pixels
   *                          do not become visible for the user. For most cases
   *                          it's recommended to set this color to the button fill color
   *                          in order to create a seamless transition from the button
   *                          to the actual border.
   * @param backgroundColor   This property is very important to avoid an unwanted effect.
   *                          All buttons have a rectangular shape by default. This rectangle
   *                          still exists and has to be filled with the background color
   *                          of the frame in order to avoid an ugly looking UI.
   * @param borderThickness   The amount of pixels which describes how thick the border
   *                          around the button should be like.
   * @param borderRadius      Figure given in pixels which describes the strength of
   *                          the rounding.
   */
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

  /**
   * This is the method which is called when the border is
   * actually painted.
   *
   * @param component The component on which the border should be applied.
   * @param graphics  The {@code Graphics} object of the border.
   * @param x         The x-location of the border
   * @param y         The y-location of the border
   * @param width     The width of the border
   * @param height    The height of the border
   */
  @Override
  public void paintBorder(Component component,
                          Graphics graphics,
                          int x,
                          int y,
                          int width,
                          int height) {

    // get Graphics2D object from the normal graphics object.
    Graphics2D graphics2D = (Graphics2D) graphics;

    // save the initial clip of the graphic in order to restore it later
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

    // create a new rectangle with the size of the border
    Rectangle rectangle = new Rectangle(0, 0, width, height);

    Area borderRegion = new Area(rectangle);
    borderRegion.subtract(area);
    graphics2D.setClip(borderRegion);
    graphics2D.setColor(backgroundColor);
    graphics2D.fillRect(0, 0, width + 30, height+10);
    graphics2D.setClip(null);

    // restore initial clip
    graphics2D.setClip(clip);

    graphics2D.setColor(color);
    graphics2D.setStroke(stroke);
    graphics2D.draw(area);
  }

}
