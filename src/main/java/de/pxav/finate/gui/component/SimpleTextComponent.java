package de.pxav.finate.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class SimpleTextComponent extends JComponent {

  private String text;

  public SimpleTextComponent(String text) {
    this.text = text;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.BLACK);
    g.drawString(text, 0, 0);
  }

  public void setText(String text) {
    this.text = text;
  }

}
