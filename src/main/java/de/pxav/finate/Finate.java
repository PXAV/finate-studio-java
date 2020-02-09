package de.pxav.finate;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.pxav.finate.binder.SimpleBinderModule;
import de.pxav.finate.gui.MainWindow;
import de.pxav.finate.gui.WindowRepository;
import de.pxav.finate.gui.theme.ThemeRepository;
import de.pxav.finate.gui.theme.WindowTheme;

import javax.swing.*;
import java.awt.Dimension;

/**
 * This is the main class of the Finate project. The main method is
 * executed on every application startup. It also initializes a shutdown
 * hook, which is executed on ever application shutdown.
 *
 *
 *
 * @author pxav
 */
@Singleton
public class Finate {

  // main frame of the project
  private static JFrame mainFrame;

  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    mainFrame = new JFrame("Finate");
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mainFrame.setSize(1280, 720);
    mainFrame.setMinimumSize(new Dimension(1024, 576));
    mainFrame.setLocationByPlatform(true);

    // Sets the driver for the camera.
    Webcam.setDriver(new IpCamDriver());

    SimpleBinderModule simpleBinderModule = new SimpleBinderModule(mainFrame);

    Injector injector = Guice.createInjector(
            simpleBinderModule
    );

    injector.injectMembers(new Finate());

    injector.getInstance(ThemeRepository.class).detectThemes("de.pxav.finate");
    WindowTheme windowTheme = injector.getInstance(ThemeRepository.class).getByName("Light theme");
    injector.getInstance(ThemeRepository.class).changeCurrentTheme(windowTheme);

    injector.getInstance(WindowRepository.class).detectWindows(Finate.class.getPackage().getName());

    SwingUtilities.invokeLater(() -> {
      injector.getInstance(WindowRepository.class).showWindowAndKeep(MainWindow.class);
    });

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("APPLICATION IS SHUTTING DOWN!");
    }, "SHUTDOWN_HOOK"));

  }
}