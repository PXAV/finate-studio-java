package de.pxav.finate.gui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.pxav.finate.connection.ImageCapture;
import de.pxav.finate.gui.theme.ThemeRepository;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class ImageCaptureWindow {

  private JFrame jFrame;
  private ThemeRepository themeRepository;
  private ImageCapture imageCapture;
  private Injector injector;

  private JLabel mainLabel = new JLabel();
  private JLabel informationText = new JLabel();

  private ScheduledFuture<?> informationUpdaterTask;

  @Inject
  public ImageCaptureWindow(JFrame jFrame, ThemeRepository themeRepository, ImageCapture imageCapture, Injector injector) {
    this.jFrame = jFrame;
    this.themeRepository = themeRepository;
    this.imageCapture = imageCapture;
    this.injector = injector;
  }

  public void draw() {
    jFrame.setTitle("Finate - Capturing images...");



    jFrame.add(mainLabel);
    jFrame.add(informationText);
    jFrame.repaint();
  }

  private void updateComponents() {

  }

  private void updateInformationText() {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(0);
    informationUpdaterTask = executorService.scheduleAtFixedRate(() -> {

      informationText.setText(
              "<html>" +
              "" +
              "" +
              "" +
              "" +
              "</html>"
      );

    }, 0, 50, TimeUnit.MILLISECONDS);
  }

  public void stop() {
    informationUpdaterTask.cancel(true);
    jFrame.remove(mainLabel);
    jFrame.remove(informationText);
    jFrame.repaint();
  }

}
