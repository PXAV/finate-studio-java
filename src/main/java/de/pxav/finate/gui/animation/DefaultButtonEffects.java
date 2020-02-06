package de.pxav.finate.gui.animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class DefaultButtonEffects implements MouseListener {

  private JButton jButton;

  public DefaultButtonEffects(JButton jButton) {
    this.jButton = jButton;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    new Thread(new Runnable()
    {
      public void run()
      {
        for (int i = 127; i <= 255; i += 7)
        {
          changeAlpha(e.getComponent(), i);
          try
          {
            Thread.sleep(10);
          }
          catch (Exception e)
          {
          }
        }
      }
    }).start();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    new Thread(new Runnable()
    {
      public void run()
      {
        for (int i = 255; i >= 127; i -= 7)
        {
          changeAlpha(e.getComponent(), i);
          try
          {
            Thread.sleep(10);
          }
          catch (Exception e)
          {
          }
        }
      }
    }).start();
  }

  private Color changeAlpha(Component component, int alpha) {
    if (alpha > 255) {
      alpha = 255;
    }
    Color color = new Color(
            component.getBackground().getRed(),
            component.getBackground().getGreen(),
            component.getBackground().getBlue(),
            alpha
    );
    component.setBackground(color);
    component.repaint();
    return color;
  }

}
