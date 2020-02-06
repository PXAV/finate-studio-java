package de.pxav.finate.gui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class ModernButton extends JButton {

  private static final float minAlpha = .8f;
  private static final double minScale = .85d;
  private static final double maxScale = 1d;
  private static final long delayInMillis = 14L;

  private float alpha = minAlpha;
  private double scale = minScale;
  private int initialWidth = -1;
  private int initialHeight = -1;

  public ModernButton() {
    setFocusPainted(false);
    addMouseListener(new ML());
  }

  @Override
  public void paintComponent(Graphics graphics) {
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

  private void setAlpha(float alpha) {
    this.alpha = alpha;
    repaint();
  }

  private void setScale(double scale) {
    this.scale = scale;
    repaint();
  }

  public class ML extends MouseAdapter {
    public void mouseExited(MouseEvent me) {
      setSize(initialWidth, initialHeight);
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

    public void mouseEntered(MouseEvent me) {
      new Thread(() -> {
        for (float i = minAlpha; i <= 1f; i += .03f) {
          setAlpha(i);
          try {
            Thread.sleep(delayInMillis);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
      new Thread(() -> {
        for (double i = minScale; i <= maxScale; i += .03d) {
          setScale(i);
          try {
            Thread.sleep(delayInMillis);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
    }

    public void mousePressed(MouseEvent me) {
      new Thread(() -> {
        for (float i = 1f; i >= 0.6f; i -= .1f) {
          setAlpha(i);
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
