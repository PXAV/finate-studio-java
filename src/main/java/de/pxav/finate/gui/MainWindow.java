package de.pxav.finate.gui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.pxav.finate.gui.animation.DefaultButtonEffects;
import de.pxav.finate.gui.component.ModernButton;
import de.pxav.finate.gui.component.RoundedButtonBorder;
import de.pxav.finate.gui.theme.ThemeRepository;
import de.pxav.finate.gui.theme.WindowElement;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class MainWindow {

  private JFrame jFrame;
  private ThemeRepository themeRepository;
  private Injector injector;

  private JLabel mainLabel = new JLabel();
  private JLabel title = new JLabel();
  private JLabel subTitle = new JLabel();

  private ModernButton recordButton = new ModernButton();
  private ModernButton settingsButton = new ModernButton();
  private ModernButton aboutButton = new ModernButton();
  private ModernButton quitButton = new ModernButton();

  @Inject
  public MainWindow(JFrame jFrame, ThemeRepository themeRepository, Injector injector) {
    this.jFrame = jFrame;
    this.themeRepository = themeRepository;
    this.injector = injector;
  }

  public void draw() {
    Color backgroundColor = themeRepository.getColor(WindowElement.BACKGROUND);
    Color primaryButtonColor = themeRepository.getColor(WindowElement.BACKGROUND);
    jFrame.getContentPane().setBackground(this.themeRepository.getColor(WindowElement.BACKGROUND));

    mainLabel.setSize(jFrame.getWidth(), jFrame.getHeight());
    mainLabel.setBackground(backgroundColor);

    title.setText("finate.");
    title.setFont(new Font("Arial", Font.BOLD, 70));

    subTitle.setText("STREAM IMAGE DATA FROM YOUR IP CAMERA. SIMPLE.");
    subTitle.setFont(new Font("Arial", Font.PLAIN, 22));

    AbstractBorder buttonBorder = new RoundedButtonBorder(primaryButtonColor, backgroundColor, 3,52);

    recordButton.setText("RECORD");
    recordButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    recordButton.setFont(new Font("Arial", Font.BOLD, 21));
    recordButton.setBorder(buttonBorder);
    recordButton.setBackground(themeRepository.getColor(WindowElement.PRIMARY_BUTTON));
    recordButton.addMouseListener(new DefaultButtonEffects(recordButton));
    recordButton.setRolloverEnabled(false);
    if (recordButton.getActionListeners().length == 0) {
      recordButton.addActionListener(event -> {
        this.stop();
        injector.getInstance(RecordSetupWindow.class).draw();
      });
    }
    mainLabel.add(recordButton);

    settingsButton.setText("SETTINGS");
    settingsButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    settingsButton.setFont(new Font("Arial", Font.BOLD, 21));
    settingsButton.setBorder(buttonBorder);
    settingsButton.setBackground(themeRepository.getColor(WindowElement.PRIMARY_BUTTON));
    settingsButton.addMouseListener(new DefaultButtonEffects(recordButton));
    settingsButton.setRolloverEnabled(false);
    mainLabel.add(settingsButton);

    aboutButton.setText("ABOUT");
    aboutButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    aboutButton.setFont(new Font("Arial", Font.BOLD, 21));
    aboutButton.setBorder(buttonBorder);
    aboutButton.setBackground(themeRepository.getColor(WindowElement.PRIMARY_BUTTON));
    aboutButton.addMouseListener(new DefaultButtonEffects(recordButton));
    aboutButton.setRolloverEnabled(false);
    if (aboutButton.getActionListeners().length == 0) {
      aboutButton.addActionListener(event -> {
        stop();
        injector.getInstance(AboutWindow.class).draw();
      });
    }
    mainLabel.add(aboutButton);

    quitButton.setText("QUIT");
    quitButton.setForeground(themeRepository.getColor(WindowElement.TEXT));
    quitButton.setFont(new Font("Arial", Font.BOLD, 21));
    quitButton.setBorder(buttonBorder);
    quitButton.setBackground(themeRepository.getColor(WindowElement.PRIMARY_BUTTON));
    quitButton.addMouseListener(new DefaultButtonEffects(recordButton));
    quitButton.setRolloverEnabled(false);
    if (quitButton.getActionListeners().length == 0) {
      quitButton.addActionListener(event -> {
        System.exit(0);
      });
    }
    mainLabel.add(quitButton);

    this.updateComponents();
    jFrame.add(mainLabel);
    jFrame.add(title);
    jFrame.add(subTitle);
    jFrame.setLayout(null);
    jFrame.repaint();
    jFrame.setVisible(true);
  }

  public void updateComponents() {
    title.setLocation((int) (jFrame.getWidth() * 0.1), jFrame.getHeight() / 30);
    title.setSize(jFrame.getWidth(), jFrame.getHeight() / 8);

    subTitle.setLocation((int) (jFrame.getWidth() * 0.1), title.getY());
    subTitle.setSize(jFrame.getWidth(), jFrame.getHeight() / 4);

    recordButton.setLocation(title.getX(), subTitle.getHeight() + subTitle.getY() + (int) (jFrame.getHeight() * 0.07));
    recordButton.setSize(jFrame.getWidth() / 5, 55);

    settingsButton.setLocation(title.getX(), recordButton.getHeight() + recordButton.getY() + (int) (jFrame.getHeight() * 0.01));
    settingsButton.setSize(jFrame.getWidth() / 5, 55);

    aboutButton.setLocation(title.getX(), settingsButton.getHeight() + settingsButton.getY() + (int) (jFrame.getHeight() * 0.01));
    aboutButton.setSize(jFrame.getWidth() / 5, 55);

    quitButton.setLocation(title.getX(), aboutButton.getHeight() + aboutButton.getY() + (int) (jFrame.getHeight() * 0.01));
    quitButton.setSize(jFrame.getWidth() / 5, 55);
  }

  public void stop() {
    jFrame.remove(mainLabel);
    jFrame.remove(title);
    jFrame.remove(subTitle);
    jFrame.repaint();
  }

}
