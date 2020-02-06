package de.pxav.finate.common;

import com.google.inject.Singleton;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class ImageUtils {

  public BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
    BufferedImage scaledImage = null;
    if (imageToScale != null) {
      scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
      Graphics2D graphics2D = scaledImage.createGraphics();
      graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
      graphics2D.dispose();
    }
    return scaledImage;
  }

}
