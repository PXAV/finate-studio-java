package de.pxav.finate.gui.animation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class OpacityAnimation extends TransformAnimation {

  private static Map<Component, ScheduledFuture> animations = Maps.newHashMap();

  private final Component component;
  private int increaseBy = 0;
  private short setTo = -1;
  private final int timeInMillis;

  private static Map<Component, Long> times = Maps.newHashMap();

  public OpacityAnimation(Component component, int increaseBy, int timeInMillis) {
    this.component = component;
    this.increaseBy = increaseBy;
    this.timeInMillis = timeInMillis;
  }

  public OpacityAnimation(Component component, short setTo, int timeInMillis) {
    this.component = component;
    this.setTo = setTo;
    this.timeInMillis = timeInMillis;
  }

  @Override
  public void perform() {
    if (animations.containsKey(component)) {
      ScheduledFuture future = animations.get(component);
      future.cancel(true);
      animations.remove(component);
    }

    int[] currentAlpha = new int[] {component.getBackground().getAlpha()};
    System.out.println("init alpha: " + currentAlpha[0]);
    int[] maxAlpha = new int[] {currentAlpha[0] + increaseBy};
    System.out.println("max alpha: " + maxAlpha[0]);

    if (setTo != -1 && setTo <= 255) {
      maxAlpha[0] = setTo;
    }

    if (maxAlpha[0] > 255) {
      maxAlpha[0] = 255;
    }
    System.out.println("max alpha corrected: " + maxAlpha[0]);

    int[] difference = new int[] {maxAlpha[0] - currentAlpha[0]};
    System.out.println("difference: " + difference[0]);
    if (difference[0] == 0) {
      return;
    }

    double timeDelay = (timeInMillis / Math.abs(difference[0])) * 1000;
    double t2 = timeInMillis / Math.abs(difference[0]);
    System.out.println("delay in micro seconds: " + timeDelay);
    System.out.println("delay in ms: " + t2);
    int[] perTick = new int[] {difference[0] / (int) (timeDelay/1000)};
    System.out.println("per tick raw: " + perTick[0]);
    if (perTick[0] == 0) {
      perTick[0] = 1;
    }
    System.out.println("per tick corr: " + perTick[0]);

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(0);

    Runnable runnable = () -> {
      try {
        if (difference[0] <= 1) {
          animations.get(component).cancel(true);
          System.out.println("shutdown");
          animations.remove(this.component);
          System.out.println("operation took " + (System.currentTimeMillis() - times.get(component)) + "ms");
        }
        System.out.println("init loop alpha: " + currentAlpha[0]);
        System.out.println("alpha + pertick = " + (currentAlpha[0] + perTick[0]));
        Color color = changeAlpha(currentAlpha[0] + perTick[0]);

        difference[0] = maxAlpha[0] - color.getAlpha();
        System.out.println("new diff " + difference[0]);
        component.setBackground(color);
        currentAlpha[0]+=perTick[0];
        component.repaint();
      } catch (Exception e) {
        e.printStackTrace();
      }

    };

    System.out.println("started");
    times.put(component, System.currentTimeMillis());
    ScheduledFuture future = scheduledExecutorService.scheduleAtFixedRate(runnable, 0, (int) timeDelay, TimeUnit.MICROSECONDS);
    animations.put(this.component, future);
  }

  private Color changeAlpha(int alpha) {
    if (alpha > 255) {
      alpha = 255;
    }

    return new Color(
            component.getBackground().getRed(),
            component.getBackground().getGreen(),
            component.getBackground().getBlue(),
            alpha
    );
  }

}
