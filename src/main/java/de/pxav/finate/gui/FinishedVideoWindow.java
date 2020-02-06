package de.pxav.finate.gui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.pxav.finate.connection.VideoRecorder;
import de.pxav.finate.gui.theme.ThemeRepository;

import javax.swing.*;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class FinishedVideoWindow {

  private JFrame jFrame;
  private ThemeRepository themeRepository;
  private VideoRecorder videoRecorder;
  private Injector injector;

  private JLabel mainLabel = new JLabel();
  private JLabel imageLabel;

  @Inject
  public FinishedVideoWindow(JFrame jFrame,
                             ThemeRepository themeRepository,
                             VideoRecorder videoRecorder,
                             Injector injector) {
    this.jFrame = jFrame;
    this.themeRepository = themeRepository;
    this.videoRecorder = videoRecorder;
    this.injector = injector;
  }

  public void draw() {
    jFrame.setTitle("Finate - Video record finished");

    ImageIcon imageIcon = new ImageIcon("assets/confirmation.png");
    imageLabel = new JLabel(imageIcon);

    jFrame.add(mainLabel);
    jFrame.add(imageLabel);
  }

  private void updateComponents() {

  }

  public void stop() {
    jFrame.add(mainLabel);
  }

}
