package de.pxav.finate.common;

import com.google.inject.Singleton;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class CustomTimeFormat {

  public String getTimeFormatForMilliseconds(long milliseconds) {
    StringBuilder output = new StringBuilder();
    int seconds = 0;
    int minutes = 0;
    int hours = 0;

    while (milliseconds >= 1000) {
      milliseconds -= 1000;
      seconds++;
    }

    while (seconds >= 60) {
      seconds -= 60;
      minutes++;
    }

    while (minutes >= 60) {
      minutes -= 60;
      hours++;
    }

    if (hours < 10)
      output.append(0).append(hours);
    else output.append(hours);
    output.append(":");

    if (minutes < 10)
      output.append(0).append(minutes);
    else output.append(minutes);
    output.append(":");

    if (seconds < 10)
      output.append(0).append(seconds);
    else output.append(seconds);

    return output.toString();
  }

}
