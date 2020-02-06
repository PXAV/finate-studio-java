package de.pxav.finate.gui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.pxav.finate.gui.component.ModernButton;
import de.pxav.finate.gui.component.RoundedButtonBorder;
import de.pxav.finate.gui.theme.ThemeRepository;
import de.pxav.finate.gui.theme.WindowElement;

import javax.swing.*;
import java.awt.*;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class AboutWindow {

  private JFrame jFrame;
  private ThemeRepository themeRepository;
  private Injector injector;

  private JLabel mainLabel = new JLabel();
  private JLabel title = new JLabel();
  private JLabel informationText = new JLabel();

  private ModernButton backButton = new ModernButton();
  private ImageIcon imageIcon = new ImageIcon("assets/pxav_logo.png");

  private JLabel imageContainer = new JLabel(imageIcon);

  @Inject
  public AboutWindow(JFrame jFrame, ThemeRepository themeRepository, Injector injector) {
    this.jFrame = jFrame;
    this.themeRepository = themeRepository;
    this.injector = injector;
  }

  public void draw() {
    Color backgroundColor = themeRepository.getColor(WindowElement.BACKGROUND);
    Color primaryButtonColor = themeRepository.getColor(WindowElement.PRIMARY_BUTTON);
    Color secondaryButtonColor = themeRepository.getColor(WindowElement.SECONDARY_BUTTON);

    RoundedButtonBorder roundedButtonBorder = new RoundedButtonBorder(primaryButtonColor, backgroundColor, 3, 52);
    RoundedButtonBorder secondaryButtonBorder = new RoundedButtonBorder(secondaryButtonColor, backgroundColor, 3, 52);

    title.setText("about finate");
    title.setFont(new Font("Arial", Font.BOLD, 50));

    informationText.setText(
            "<html>" +
            "Developed by pxav <br> <br>" +
            "<br> What is finate? <br>" +
            "Finate is a video processing tool that connects to network-based cameras. <br>" +
                    " It can capture images at regular but customizable intervals. <br>" +
                    "Furthermore, it can record videos with a customizable frame rate and duration. <br> <br>" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "<br> Pre-release <br>" +
            "The finate application has been developed as fast as possible <br>" +
                    "and will contain multiple bugs. It is currently in a pre-release state<br>" +
                    " and the source code needs further style improvements.<br><br>" +
            "" +
            "<br> Plans for future releases <br>" +
                    "- Support multiple and custom themes<br>" +
                    "- Support multiple languages<br>" +
                    "- Implement a composing tool for time-lapse videos<br>" +
            "" +
            "" +
            "</html>"
    );
    informationText.setFont(new Font("Arial", Font.PLAIN, 25));

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
    mainLabel.add(backButton);

    this.updateComponents();
    jFrame.add(mainLabel);
    jFrame.add(title);
    jFrame.add(informationText);
    jFrame.add(imageContainer);
    jFrame.repaint();
  }

  private void updateComponents() {
    title.setLocation((int) (jFrame.getWidth() * 0.03), jFrame.getHeight() / 30);
    title.setSize(jFrame.getWidth(), jFrame.getHeight() / 15);

    informationText.setLocation(title.getX() + 10, title.getY());
    informationText.setSize((int) (jFrame.getWidth() * 0.9), jFrame.getHeight() - title.getHeight());

    imageContainer.setSize(100, 100);
    imageContainer.setLocation(10, 10);

    backButton.setFont(new Font("Arial", Font.BOLD, 20));
    backButton.setSize((int) (jFrame.getWidth() * .18), 55);
    backButton.setLocation((int) (jFrame.getWidth() * 0.01), jFrame.getHeight());
  }

  public void stop() {
    jFrame.remove(mainLabel);
    jFrame.remove(title);
    jFrame.remove(informationText);
    jFrame.remove(imageContainer);
    jFrame.repaint();
  }

}
