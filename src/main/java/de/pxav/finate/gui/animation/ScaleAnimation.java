package de.pxav.finate.gui.animation;

import java.awt.*;
import java.util.UUID;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class ScaleAnimation extends TransformAnimation {

  private Component component;
  private double increaseBy;
  private int timeInMillis;

  public ScaleAnimation(Component component, double increaseBy, int timeInMillis) {
    this.component = component;
    this.increaseBy = increaseBy;
    this.timeInMillis = timeInMillis;
  }

  public void perform() {
    Thread thread = new Thread(() -> {
      double maxWidth = (1 + increaseBy) * component.getWidth(); // a
      double maxHeight = (1 + increaseBy) * component.getHeight(); // b

      double rawWidthUnit = maxWidth / timeInMillis; // a unit
      int heightUnit = 0;

      int multiplied = 0;
      while (rawWidthUnit < 1) {
        rawWidthUnit *= 2;
        multiplied++;
      }

      while (multiplied != 0) {

        multiplied--;
      }


    }, "animation_" + UUID.randomUUID());
    thread.start();
  }

}
