package de.pxav.finate.gui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class is an own implementation of the {@code JButton} by AWT.
 * It gives the button a more modern look and provides some
 * interactive animations to create a more responsive feeling for the
 * user.
 *
 * @author pxav
 * @see JButton
 * @see JComponent
 */
public class ModernButton extends JButton {

  private static final float minAlpha = .8f;

  private static final double minScale = .85d;
  private static final double maxScale = 1d;

  // delay between each animation state in milliseconds.
  private static final long delayInMillis = 14L;

  private float alpha = minAlpha;
  private double scale = minScale;
  private int initialWidth = -1;
  private int initialHeight = -1;

  public ModernButton() {
    setFocusPainted(false);
    addMouseListener(new AnimationMouseListener());
  }

  /**
   * This method is executed every time the {@code #repaint()}
   * method for this component is called.
   *
   * @param graphics  The {@code Graphics} object used to modify
   *                  the button paint data (injected by AWT automatically)
   */
  @Override
  public void paintComponent(Graphics graphics) {
    // set initial size of the components if there has been no one
    // before
    if (initialHeight == -1 && initialWidth == -1) {
      initialHeight = getHeight();
      initialWidth = getWidth();
    }

    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, getWidth(), getHeight());

    graphics.setColor(getBackground());
    Graphics2D graphics2D = (Graphics2D) graphics;
    graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, alpha));

    graphics2D.scale(scale, scale);
    setBorder(getBorder());
    super.paintComponent(graphics2D);
  }

  /**
   * Changes the alpha of the component's font to the given
   * value and immediately.
   *
   * @param alpha The scale factor you want to scale it to.
   *              1d = 100% scale (original size)
   *              .5d = 50% scale (half the initial size)
   *              0d = 0% scale (invisible)
   *              and so on...
   */
  private void setAlpha(float alpha) {
    this.alpha = alpha;
    repaint();
  }

  /**
   * Changes the scale of the component to the given
   * value and immediately repaints it so that the changes
   * will be visible for the user as well.
   *
   * @param scale The scale factor you want to scale it to.
   *              1d = 100% scale (original size)
   *              .5d = 50% scale (half the initial size)
   *              0d = 0% scale (invisible)
   *              and so on...
   */
  private void setScale(double scale) {
    this.scale = scale;
    repaint();
  }

  /**
   * This class is responsible for all interactive animations
   * of the button. These animations should create a more
   * responsive feeling when using the app. It includes for example
   * rescaling and changing the opacity when hovering over the
   * component.
   */
  public class AnimationMouseListener extends MouseAdapter {

    /**
     * This method is called when the user has been hovering
     * over the button before but now moves his mouse cursor
     * away from it.
     *
     * In this case the event is used to reset the scale
     * and alpha animations which were fired when the user
     * has moved his cursor on the button before and smoothly
     * bring the button back to its initial state.
     *
     * @param mouseEvent The event instance providing
     *                   the affected component, etc.
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {
      // reset the size back to the initial height and width
      setSize(initialWidth, initialHeight);

      // establish new threads for the animation to avoid
      // blocking the main thread.
      new Thread(() -> {
        for (float i = 1f; i >= .5f; i -= .03f) {
          setAlpha(i);
          try {
            Thread.sleep(delayInMillis);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();

      new Thread(() -> {
        for (double i = maxScale; i >= minScale; i -= .03d) {
          setScale(i);
          try {
            Thread.sleep(delayInMillis);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
    }

    /**
     * This event is the exact opposite of {@code #mouseExited}.
     * It is triggered when the user moves his mouse cursor onto the
     * corresponding component.
     *
     * In this case the event is used to stress the
     * button by increasing the alpha values of the
     * button font and smoothly increasing the scale.
     *
     * @param mouseEvent The event instance providing
     *                   the affected component, etc.
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

      // establish new thread for alpha animation
      // they are used so that the delay between the
      // animation steps does not block the main thread.
      new Thread(() -> {
        for (float i = minAlpha; i <= 1f; i += .03f) {
          // update alpha and repaint the component
          setAlpha(i);
          try {
            // wait until next animation step can begin
            Thread.sleep(delayInMillis);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();

      // establish new thread for scale animation
      new Thread(() -> {
        for (double i = minScale; i <= maxScale; i += .03d) {
          // update scale and repaint the component
          setScale(i);
          try {
            Thread.sleep(delayInMillis);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
    }

    /**
     * This event method is triggered when the user
     * presses a button with his mouse. This method is
     * executed even if the mouse button is not released.
     *
     * In this case the event is used to reset the alpha
     * value back to their initial state when the button
     * is pressed.
     *
     * @param mouseEvent The event instance providing
     *                   the affected component, etc.
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
      // establish a new thread handling the animation
      new Thread(() -> {
        // start at the full alpha and decrease it by 10%
        // until it's down to 60%.
        for (float alpha = 1f; alpha >= 0.6f; alpha -= .1f) {
          setAlpha(alpha);
          try {
            Thread.sleep(1);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }

}
