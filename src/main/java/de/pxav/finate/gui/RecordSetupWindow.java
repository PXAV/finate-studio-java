package de.pxav.finate.gui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.internal.cglib.proxy.$MethodProxy;
import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import de.pxav.finate.connection.CameraConnection;
import de.pxav.finate.connection.ImageCapture;
import de.pxav.finate.connection.VideoRecorder;
import de.pxav.finate.gui.component.ModernButton;
import de.pxav.finate.gui.component.RoundedButtonBorder;
import de.pxav.finate.gui.theme.ThemeRepository;
import de.pxav.finate.gui.theme.WindowElement;
import de.pxav.finate.user.RecordType;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class RecordSetupWindow {

  private JFrame jFrame;
  private ThemeRepository themeRepository;
  private CameraConnection cameraConnection;
  private VideoRecorder videoRecorder;
  private ImageCapture imageCapture;
  private Injector injector;

  private final JLabel mainLabel = new JLabel();
  private final JPanel recordTypeLabel = new JPanel();

  private final JLabel title = new JLabel();
  private final JTextField urlInput = new JTextField();
  private final ModernButton testConnection = new ModernButton();
  private final JTextField filePath = new JTextField();
  private final ModernButton searchFile = new ModernButton();
  private final JButton imageButton = new JButton();
  private final JButton timeLapseButton = new JButton();
  private final JButton videoButton = new JButton();
  private final ModernButton recordButton = new ModernButton();
  private final ModernButton backButton = new ModernButton();

  private final JSlider imageRateSlider = new JSlider();
  private final JTextField imageRateDisplayEditor = new JTextField();
  private final JComboBox<String> imageFormat = new JComboBox<>();
  private final JCheckBox limitImageAmount = new JCheckBox();

  private final JSlider videoFrameRateSlider = new JSlider();
  private final JTextField videoFrameRateEditor = new JTextField();
  private final JTextField videoFileName = new JTextField();
  private final JComboBox<String> videoFileExtension = new JComboBox<>();
  private final JCheckBox autoRecordStop = new JCheckBox();
  private final JTextField videoRecordDurationInSeconds = new JTextField();

  private RecordType recordType = RecordType.IMAGES;

  @Inject
  public RecordSetupWindow(JFrame jFrame,
                           ThemeRepository themeRepository,
                           CameraConnection cameraConnection,
                           VideoRecorder videoRecorder,
                           ImageCapture imageCapture,
                           Injector injector) {
    this.jFrame = jFrame;
    this.themeRepository = themeRepository;
    this.cameraConnection = cameraConnection;
    this.videoRecorder = videoRecorder;
    this.imageCapture = imageCapture;
    this.injector = injector;
  }

  public void draw() {
    Color backgroundColor = themeRepository.getColor(WindowElement.BACKGROUND);
    Color primaryButtonColor = themeRepository.getColor(WindowElement.PRIMARY_BUTTON);
    Color secondaryButtonColor = themeRepository.getColor(WindowElement.SECONDARY_BUTTON);
    jFrame.getContentPane().setBackground(this.themeRepository.getColor(WindowElement.BACKGROUND));
    jFrame.setTitle("Finate - Record Setup");
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

    mainLabel.setSize(jFrame.getWidth(), jFrame.getHeight());
    mainLabel.setBackground(backgroundColor);

    AbstractBorder buttonBorder = new RoundedButtonBorder(primaryButtonColor, backgroundColor, 5,52);
    AbstractBorder secondaryButtonBorder = new RoundedButtonBorder(secondaryButtonColor, backgroundColor, 5,52);

    title.setText("Record → Setup");

    urlInput.setText("http://192.168.132.112:8080/video");
    urlInput.setBorder(BorderFactory.createTitledBorder("URL"));

    testConnection.setText("TEST");
    testConnection.setForeground(themeRepository.getColor(WindowElement.TEXT));
    testConnection.setBorder(buttonBorder);
    testConnection.setBackground(primaryButtonColor);
    testConnection.setRolloverEnabled(false);

    filePath.setText("/home/pxav/FinateRecords");
    filePath.setBorder(BorderFactory.createTitledBorder("DIRECTORY"));

    searchFile.setText("BROWSE");
    searchFile.setForeground(themeRepository.getColor(WindowElement.TEXT));
    searchFile.setBorder(buttonBorder);
    searchFile.setBackground(primaryButtonColor);
    searchFile.setRolloverEnabled(false);
    if (searchFile.getActionListeners().length == 0) {
      searchFile.addActionListener(event -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select output directory");

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          filePath.setText(fileChooser.getCurrentDirectory().toString());
        }
      });
    }

    this.initializeRecordOptions();

    imageButton.setText("IMAGES");
    imageButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    imageButton.setBorderPainted(false);
    imageButton.setRolloverEnabled(false);
    this.buildImageSettings();
    if (imageButton.getActionListeners().length == 0) {
      imageButton.addActionListener(event -> {
        recordType = RecordType.IMAGES;
        this.updateButtonColors();
        this.removeNormalVideoSettings();
        this.removeTimeLapseSettings();
        this.buildImageSettings();
      });
    }

    timeLapseButton.setText("TIME LAPSE");
    timeLapseButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    timeLapseButton.setBorderPainted(false);
    timeLapseButton.setRolloverEnabled(false);
    if (timeLapseButton.getActionListeners().length == 0) {
      timeLapseButton.addActionListener(event -> {
        recordType = RecordType.TIME_LAPSE_VIDEO;
        this.updateButtonColors();
        this.removeImageSettings();
        this.removeNormalVideoSettings();
        this.buildTimeLapseSettings();
      });
    }

    videoButton.setText("VIDEO");
    videoButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    videoButton.setBorderPainted(false);
    videoButton.setRolloverEnabled(false);
    if (videoButton.getActionListeners().length == 0) {
      videoButton.addActionListener(event -> {
        recordType = RecordType.NORMAL_VIDEO;
        this.updateButtonColors();
        this.removeImageSettings();
        this.removeTimeLapseSettings();
        this.buildNormalVideoSettings();
      });
    }

    recordTypeLabel.setBorder(BorderFactory.createTitledBorder("RECORD TYPE"));
    recordTypeLabel.setBackground(backgroundColor);

    recordButton.setText("● RECORD");
    recordButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    recordButton.setBorder(new RoundedButtonBorder(new Color(215, 0, 54), backgroundColor, 5,52));
    recordButton.setBackground(new Color(215, 0, 54));
    recordButton.setRolloverEnabled(false);
    if (recordButton.getActionListeners().length == 0) {
      recordButton.addActionListener(event -> {
        System.out.println("rec button triggered");
        startRecording();
      });
    }

    backButton.setText("BACK");
    backButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    backButton.setBorder(secondaryButtonBorder);
    backButton.setBackground(secondaryButtonColor);
    backButton.setRolloverEnabled(false);
    if (backButton.getActionListeners().length == 0) {
      backButton.addActionListener(event -> {
        this.stop();
        injector.getInstance(MainWindow.class).draw();
      });
    }

    this.updateComponents();
    this.updateButtonColors();
    mainLabel.add(backButton);
    mainLabel.add(urlInput);
    mainLabel.add(testConnection);
    mainLabel.add(filePath);
    mainLabel.add(searchFile);
    mainLabel.add(title);
    mainLabel.add(recordButton);
    mainLabel.add(imageButton);
    mainLabel.add(timeLapseButton);
    mainLabel.add(videoButton);

    jFrame.add(mainLabel);
    jFrame.repaint();
  }

  private void updateComponents() {
    title.setLocation((int) (jFrame.getWidth() * 0.01), (int) (jFrame.getWidth() * 0.01));
    title.setSize((int) (jFrame.getWidth() * 0.5), 60);
    title.setFont(new Font("Arial", Font.BOLD, 50));
    title.repaint();

    urlInput.setLocation((int) (jFrame.getWidth() * .02), (int) (jFrame.getHeight() * .12));
    urlInput.setSize((int) (jFrame.getWidth() * .3), 55);
    urlInput.setFont(new Font("Arial", Font.BOLD, 20));

    testConnection.setFont(new Font("Arial", Font.BOLD, 20));
    testConnection.setLocation((int) urlInput.getLocation().getX() + urlInput.getWidth() + (int) (jFrame.getWidth() * 0.001), (int) urlInput.getLocation().getY() + (int) (jFrame.getHeight() * 0.009));
    testConnection.setSize((int) (jFrame.getWidth() * .17), 50);

    filePath.setLocation((int) (jFrame.getWidth() * .02), urlInput.getY() + urlInput.getHeight() + (int) (jFrame.getHeight() * 0.01));
    filePath.setSize((int) (jFrame.getWidth() * .3), 55);
    filePath.setFont(new Font("Arial", Font.BOLD, 20));

    searchFile.setFont(new Font("Arial", Font.BOLD, 20));
    searchFile.setLocation((int) filePath.getLocation().getX() + filePath.getWidth() + (int) (jFrame.getWidth() * 0.001), (int) filePath.getLocation().getY() + (int) (jFrame.getHeight() * 0.009));
    searchFile.setSize((int) (jFrame.getWidth() * .17), 50);

    imageButton.setLocation(filePath.getX(), filePath.getY() + filePath.getHeight() + (int) (jFrame.getHeight() * 0.01));
    imageButton.setSize((int) (jFrame.getWidth() * .1), 48);

    imageRateSlider.setLocation(imageButton.getX(), imageButton.getY() + imageButton.getHeight() + (int) (jFrame.getHeight() * 0.01));
    imageRateSlider.setSize((int) (jFrame.getWidth() * 0.3), 50);

    imageRateDisplayEditor.setLocation(imageRateSlider.getX() + imageRateSlider.getWidth() + (int) (jFrame.getWidth() * 0.01), imageRateSlider.getY());
    imageRateDisplayEditor.setSize((int) (imageRateSlider.getWidth() * 0.31), imageRateSlider.getHeight());
    imageRateDisplayEditor.setFont(new Font("Arial", Font.BOLD, 20));

    imageFormat.setLocation(imageRateSlider.getX(), imageRateSlider.getY() + imageRateSlider.getHeight() + (int) (jFrame.getHeight() * 0.011));
    imageFormat.setSize(imageRateSlider.getWidth(), imageRateSlider.getHeight());

    videoFrameRateSlider.setLocation(imageButton.getX(), imageButton.getY() + imageButton.getHeight() + (int) (jFrame.getHeight() * 0.01));
    videoFrameRateSlider.setSize((int) (jFrame.getWidth() * 0.3), 50);

    videoFrameRateEditor.setLocation(imageRateSlider.getX() + imageRateSlider.getWidth() + (int) (jFrame.getWidth() * 0.01), imageRateSlider.getY());
    videoFrameRateEditor.setSize((int) (imageRateSlider.getWidth() * 0.31), imageRateSlider.getHeight());
    videoFrameRateEditor.setFont(new Font("Arial", Font.BOLD, 20));

    videoFileName.setLocation((int) (jFrame.getWidth() * .02), videoFrameRateSlider.getY() + videoFrameRateSlider.getHeight() + (int) (jFrame.getHeight() * 0.01));
    videoFileName.setSize((int) (jFrame.getWidth() * .3), 55);
    videoFileName.setFont(new Font("Arial", Font.BOLD, 20));

    videoFileExtension.setFont(new Font("Arial", Font.BOLD, 20));
    videoFileExtension.setLocation((int) videoFileName.getLocation().getX() + videoFileName.getWidth() + (int) (jFrame.getWidth() * 0.001), (int) videoFileName.getLocation().getY());
    videoFileExtension.setSize((int) (jFrame.getWidth() * .17), videoFileName.getHeight());

    autoRecordStop.setLocation(videoFileName.getX(), videoFileName.getY() + videoFileName.getHeight());
    autoRecordStop.setSize(videoFileName.getWidth() / 2, 55);
    autoRecordStop.setFont(new Font("Arial", Font.BOLD, 20));

    videoRecordDurationInSeconds.setLocation(autoRecordStop.getX() + autoRecordStop.getWidth() + (int) (jFrame.getWidth() * 0.01), autoRecordStop.getY());
    videoRecordDurationInSeconds.setSize(autoRecordStop.getWidth(), autoRecordStop.getHeight());

    timeLapseButton.setLocation(imageButton.getX() + imageButton.getWidth(), filePath.getY() + filePath.getHeight() + (int) (jFrame.getHeight() * 0.01));
    timeLapseButton.setSize((int) (jFrame.getWidth() * .1), 48);

    videoButton.setLocation(timeLapseButton.getX() + timeLapseButton.getWidth(), filePath.getY() + filePath.getHeight() + (int) (jFrame.getHeight() * 0.01));
    videoButton.setSize((int) (jFrame.getWidth() * .1), 48);

    recordButton.setFont(new Font("Arial", Font.BOLD, 20));
    recordButton.setSize((int) (jFrame.getWidth() * .2), 55);
    recordButton.setLocation(jFrame.getWidth() - recordButton.getWidth(), jFrame.getHeight() - 100);

    backButton.setFont(new Font("Arial", Font.BOLD, 20));
    backButton.setSize((int) (jFrame.getWidth() * .18), 55);
    backButton.setLocation((int) (jFrame.getWidth() * 0.01), jFrame.getHeight() - 100);

    //jFrame.repaint();
  }

  private void updateButtonColors() {
    Color primaryButtonColor = themeRepository.getColor(WindowElement.PRIMARY_BUTTON);
    Color secondaryButtonColor = themeRepository.getColor(WindowElement.SECONDARY_BUTTON);

    if (recordType == RecordType.IMAGES) {
      imageButton.setBackground(primaryButtonColor);
    } else {
      imageButton.setBackground(secondaryButtonColor);
    }

    if (recordType == RecordType.TIME_LAPSE_VIDEO) {
      timeLapseButton.setBackground(primaryButtonColor);
    } else {
      timeLapseButton.setBackground(secondaryButtonColor);
    }

    if (recordType == RecordType.NORMAL_VIDEO) {
      videoButton.setBackground(primaryButtonColor);
    } else {
      videoButton.setBackground(secondaryButtonColor);
    }
  }

  private void buildImageSettings() {
    Color backgroundColor = themeRepository.getColor(WindowElement.BACKGROUND);
    Color primaryButtonColor = themeRepository.getColor(WindowElement.PRIMARY_BUTTON);

    imageRateSlider.setMaximum(600);
    imageRateSlider.setMinimum(1);
    imageRateSlider.setValue(5);
    imageRateSlider.setBackground(backgroundColor);
    imageRateSlider.setForeground(primaryButtonColor);
    imageRateSlider.setBorder(BorderFactory.createTitledBorder("IMAGE DELAY IN SECONDS"));
    if (imageRateSlider.getChangeListeners().length == 0) {
      imageRateSlider.addChangeListener(event -> {
        imageRateDisplayEditor.setText(String.valueOf(imageRateSlider.getValue()));
      });
    }

    imageRateDisplayEditor.setText("5");
    if (imageRateDisplayEditor.getActionListeners().length == 0) {
      imageRateDisplayEditor.addActionListener(event -> {
        try {
          int value = Integer.parseInt(imageRateDisplayEditor.getText());
            if (value <= 600 && value >= 5) {
              imageRateSlider.setValue(value);
            } else {
              imageRateDisplayEditor.setText("5");
              imageRateSlider.setValue(5);
            }
        } catch (NumberFormatException e) {
          imageRateDisplayEditor.setText("5");
          imageRateSlider.setValue(5);
        }
      });
    }

    if (imageFormat.getItemAt(0) == null) {
      imageFormat.addItem("PNG");
      imageFormat.addItem("JPG");
      imageFormat.addItem("GIF");
    }
    imageFormat.setSelectedItem("PNG");
    imageFormat.setBackground(backgroundColor);
    imageFormat.setBorder(BorderFactory.createTitledBorder("IMAGE FORMAT"));

    mainLabel.add(imageRateDisplayEditor);
    mainLabel.add(imageRateSlider);
    mainLabel.add(imageFormat);
    jFrame.repaint();
  }

  public void buildTimeLapseSettings() {
    jFrame.repaint();
  }

  public void buildNormalVideoSettings() {
    Color backgroundColor = themeRepository.getColor(WindowElement.BACKGROUND);

    videoFrameRateSlider.setMaximum(60);
    videoFrameRateSlider.setMinimum(1);
    videoFrameRateSlider.setValue(30);
    videoFrameRateSlider.setBackground(backgroundColor);
    videoFrameRateSlider.setBorder(BorderFactory.createTitledBorder("AVERAGE FPS"));
    if (videoFrameRateSlider.getChangeListeners().length == 0) {
      videoFrameRateSlider.addChangeListener(event -> {
        videoFrameRateEditor.setText(String.valueOf(videoFrameRateSlider.getValue()));
      });
    }

    videoFrameRateEditor.setText("30");
    if (videoFrameRateEditor.getActionListeners().length == 0) {
      videoFrameRateEditor.addActionListener(event -> {
        try {
          int value = Integer.parseInt(videoFrameRateEditor.getText());
          if (value <= 60 && value >= 1) {
            videoFrameRateSlider.setValue(value);
          } else {
            videoFrameRateEditor.setText("30");
            videoFrameRateSlider.setValue(30);
          }
        } catch (NumberFormatException e) {
          videoFrameRateEditor.setText("30");
          videoFrameRateSlider.setValue(30);
        }
      });
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY_MM_HH_mm");
    videoFileName.setText("RECORD_" + simpleDateFormat.format(new Date()));
    videoFileName.setBorder(BorderFactory.createTitledBorder("FILE NAME (WITHOUT EXTENSION)"));

    if (videoFileExtension.getItemAt(0) == null) {
      videoFileExtension.addItem("MP4");
      videoFileExtension.addItem("AVI");
      videoFileExtension.addItem("TS");
    }
    videoFileExtension.setSelectedItem("MP4");
    videoFileExtension.setBorder(BorderFactory.createTitledBorder("FILE EXTENSION"));
    videoFileExtension.setBackground(backgroundColor);

    autoRecordStop.setText("STOP RECORDING AUTOMATICALLY");
    autoRecordStop.setBackground(backgroundColor);
    autoRecordStop.setBorderPaintedFlat(true);
    if (autoRecordStop.getActionListeners().length == 0) {
      autoRecordStop.addActionListener(event -> {
        if (autoRecordStop.isSelected()) {
          mainLabel.add(videoRecordDurationInSeconds);
          jFrame.repaint();
        } else {
          mainLabel.remove(videoRecordDurationInSeconds);
          jFrame.repaint();
        }
      });
    }

    videoRecordDurationInSeconds.setBorder(BorderFactory.createTitledBorder("VIDEO DURATION (IN SECONDS)"));
    videoRecordDurationInSeconds.setBackground(backgroundColor);
    videoRecordDurationInSeconds.setText("3600");
    if (videoRecordDurationInSeconds.getActionListeners().length == 0) {
      videoRecordDurationInSeconds.addActionListener(event -> {
        try {
          int seconds = Integer.parseInt(videoRecordDurationInSeconds.getText());
          if (seconds <= 0) {
            videoRecordDurationInSeconds.setText("1");
          }
        } catch (NumberFormatException e) {
          videoRecordDurationInSeconds.setText("3600");
        }
      });
    }

    mainLabel.add(videoFrameRateEditor);
    mainLabel.add(videoFrameRateSlider);
    mainLabel.add(videoFileName);
    mainLabel.add(autoRecordStop);
    mainLabel.add(videoFileExtension);
    jFrame.repaint();
  }

  private void removeImageSettings() {
    mainLabel.remove(imageRateDisplayEditor);
    mainLabel.remove(imageRateSlider);
    mainLabel.remove(imageFormat);
    jFrame.repaint();
  }

  private void removeTimeLapseSettings() {
    jFrame.repaint();
  }

  private void removeNormalVideoSettings() {
    mainLabel.remove(videoFrameRateSlider);
    mainLabel.remove(videoFrameRateEditor);
    mainLabel.remove(videoFileExtension);
    mainLabel.remove(videoFileName);
    mainLabel.remove(autoRecordStop);
    mainLabel.remove(videoRecordDurationInSeconds);
    jFrame.repaint();
  }

  private void initializeRecordOptions() {
    this.buildImageSettings();
    this.removeImageSettings();
    this.buildTimeLapseSettings();
    this.removeTimeLapseSettings();
    this.buildNormalVideoSettings();
    this.removeNormalVideoSettings();
  }

  private void startRecording() {
    System.out.println("rec method called");

    cameraConnection.setUrl(this.urlInput.getText());
    cameraConnection.setWidth(640);
    cameraConnection.setHeight(480);
    try {
      this.cameraConnection.connect();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("connected");

    if (recordType == RecordType.NORMAL_VIDEO) {
      System.out.println("video record selected");
      videoRecorder.setFramesPerSecond(videoFrameRateSlider.getValue());
      videoRecorder.setOutputFile(new File(this.filePath.getText() + "/" + videoFileName.getText() + "." + this.videoFileExtension.getSelectedItem()));
      if (autoRecordStop.isSelected()) {
        videoRecorder.setMaxRecordTimeInSeconds(Integer.parseInt(videoRecordDurationInSeconds.getText()));
      }

      videoRecorder.setCameraConnection(this.cameraConnection);
      System.out.println("applied data");
      System.out.println(videoFrameRateSlider.getValue());
      System.out.println(new File(this.filePath.getText() + "/" + videoFileName.getText() + "." + this.videoFileExtension.getSelectedItem()));
      System.out.println(Integer.parseInt(videoRecordDurationInSeconds.getText()));
      stop();
      injector.getInstance(RecordingVideoWindow.class).draw();
    } else if (recordType == RecordType.IMAGES) {
      imageCapture.setCameraConnection(cameraConnection);
      imageCapture.setDelayInSeconds(imageRateSlider.getValue());
      imageCapture.setImageFormat(imageFormat.getSelectedItem().toString());
      imageCapture.setOutputFile(new File(this.filePath.getText()));
      imageCapture.timedCapture();
    }
  }

  public void stop() {
    jFrame.remove(mainLabel);
    jFrame.remove(recordTypeLabel);
  }

}
