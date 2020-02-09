package de.pxav.finate.gui;

import com.github.sarxos.webcam.WebcamPanel;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.pxav.finate.common.CustomTimeFormat;
import de.pxav.finate.connection.CameraConnection;
import de.pxav.finate.connection.VideoRecorder;
import de.pxav.finate.gui.component.ModernButton;
import de.pxav.finate.gui.component.RoundedButtonBorder;
import de.pxav.finate.gui.theme.ThemeRepository;
import de.pxav.finate.gui.theme.WindowElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class RecordingVideoWindow implements WindowTemplate {

  private JFrame jFrame;
  private CameraConnection cameraConnection;
  private VideoRecorder videoRecorder;
  private ThemeRepository themeRepository;
  private CustomTimeFormat customTimeFormat;
  private Injector injector;

  private final JLabel mainLabel = new JLabel();
  private final JLabel informationText = new JLabel();
  private ScheduledFuture<?> informationTextTask;

  private WebcamPanel webcamPanel;
  private final ModernButton playPauseButton = new ModernButton();

  @Inject
  public RecordingVideoWindow(JFrame jFrame,
                              CameraConnection cameraConnection,
                              VideoRecorder videoRecorder,
                              ThemeRepository themeRepository,
                              CustomTimeFormat customTimeFormat,
                              Injector injector) {
    this.jFrame = jFrame;
    this.cameraConnection = cameraConnection;
    this.videoRecorder = videoRecorder;
    this.themeRepository = themeRepository;
    this.customTimeFormat = customTimeFormat;
    this.injector = injector;
  }

  @Override
  public WindowTemplate show() {
    Color backgroundColor = themeRepository.getColor(WindowElement.BACKGROUND);
    Color primaryButtonColor = themeRepository.getColor(WindowElement.PRIMARY_BUTTON);
    Color secondaryButtonColor = themeRepository.getColor(WindowElement.SECONDARY_BUTTON);
    jFrame.setLayout(injector.getInstance(LayoutManager.class));

    webcamPanel = new WebcamPanel(cameraConnection.getWebcam());
    webcamPanel.setFPSDisplayed(true);
    webcamPanel.setImageSizeDisplayed(true);

    this.startInformationTextUpdater();
    mainLabel.add(informationText);

    RoundedButtonBorder roundedButtonBorder = new RoundedButtonBorder(primaryButtonColor, backgroundColor, 3, 52);

    playPauseButton.setText("START");
    playPauseButton.setBorder(roundedButtonBorder);
    playPauseButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    playPauseButton.setBackground(new Color(0, 165, 2));
    if (playPauseButton.getActionListeners().length == 0) {
      playPauseButton.addActionListener(event -> {
        if (!videoRecorder.isRecording()) {
          try {
            videoRecorder.record();
            playPauseButton.setText("STOP");
            playPauseButton.setBackground(primaryButtonColor);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {
          videoRecorder.stopRecording();
        }
      });
    }
    mainLabel.add(playPauseButton);


    this.updateComponents();
    webcamPanel.start();
    jFrame.setTitle("Finate - Recording video...");
    jFrame.addComponentListener(new ComponentListener() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateComponents();
      }

      @Override
      public void componentMoved(ComponentEvent e) {

      }

      @Override
      public void componentShown(ComponentEvent e) {

      }

      @Override
      public void componentHidden(ComponentEvent e) {

      }
    });

    try {
      Thread.sleep(20);
      jFrame.setSize(jFrame.getWidth() + 1, jFrame.getHeight() + 1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    jFrame.add(webcamPanel);
    jFrame.add(mainLabel);
    jFrame.repaint();
    System.out.println(Arrays.toString(mainLabel.getComponents()));
    System.out.println(Arrays.toString(jFrame.getRootPane().getComponents()));
    return this;
  }

  private void startInformationTextUpdater() {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(0);
    informationTextTask = scheduledExecutorService.scheduleAtFixedRate(() -> {
      long currentMillis = videoRecorder.getCurrentRecordTimeInSeconds() * 1000;
      long maxMillis = videoRecorder.getMaxRecordTimeInSeconds() * 1000;
      long remainingMillis = maxMillis - currentMillis;
      double finishedInPerCent = ((double) currentMillis / (double) maxMillis) * 100;
      String maxRecordTime;
      String currentRecordTime;
      String timeRemaining;

      NumberFormat numberFormat = NumberFormat.getInstance();
      numberFormat.setMaximumFractionDigits(1);
      String finishedIn = numberFormat.format(finishedInPerCent);

      if (videoRecorder.getMaxRecordTimeInSeconds() == -1) {
        maxRecordTime = "Unlimited";
        timeRemaining = "Unlimited";
      } else {
        maxRecordTime = videoRecorder.getMaxRecordTimeInSeconds() + " seconds (" + customTimeFormat.getTimeFormatForMilliseconds(maxMillis) + ")";
        timeRemaining = (remainingMillis / 1000) + " seconds (" + customTimeFormat.getTimeFormatForMilliseconds(remainingMillis) + ")";
      }
      currentRecordTime = videoRecorder.getCurrentRecordTimeInSeconds() + " seconds (" + customTimeFormat.getTimeFormatForMilliseconds(currentMillis) + ")";
            informationText.setText(
                    "<html>" +
                            "Recorded FPS:      " + videoRecorder.getFramesPerSecond() + "<br>" +
                            "Total record time: " + maxRecordTime + "<br>" +
                            "Time elapsed:      " + currentRecordTime + "<br>" +
                            "Time remaining:    " + timeRemaining + "<br>" +
                            "Finished (%):      " + finishedIn +"<br>" +
                    "</html>"
            );

      if (finishedInPerCent == 100) {
        this.unload();
      }
    }, 0, 10, TimeUnit.MILLISECONDS);
  }

  @Override
  public void updateComponents() {
    webcamPanel.setLocation(mainPaddingLeft(), mainPaddingTop());
    webcamPanel.setSize(jFrame.getWidth() / 3, jFrame.getHeight() / 3);

    informationText.setLocation(webcamPanel.getX() + webcamPanel.getWidth() + paddingLeft(), webcamPanel.getY());
    informationText.setSize(jFrame.getWidth() / 3, jFrame.getHeight() / 3);
    informationText.setFont(new Font("Arial", Font.BOLD, 20));

    playPauseButton.setLocation(mainPaddingLeft(), paddingTop() + webcamPanel.getHeight() + webcamPanel.getY());
    playPauseButton.setSize(jFrame.getWidth() / 8, 55);

    jFrame.repaint();
  }

  @Override
  public void unload() {
    informationTextTask.cancel(true);
    jFrame.remove(mainLabel);
    jFrame.repaint();
  }

  private int paddingLeft() {
    return (int) (jFrame.getWidth() * 0.01);
  }

  private int mainPaddingLeft() {
    return (int) (jFrame.getWidth() * 0.1);
  }

  private int mainPaddingTop() {
    return (int) (jFrame.getHeight() * 0.1);
  }

  private int paddingTop() {
    return (int) (jFrame.getHeight() * 0.01);
  }

}
