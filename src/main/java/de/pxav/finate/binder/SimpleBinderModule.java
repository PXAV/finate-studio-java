package de.pxav.finate.binder;

import com.google.inject.AbstractModule;
import de.pxav.finate.connection.CameraConnection;
import de.pxav.finate.connection.ImageCapture;
import de.pxav.finate.connection.VideoRecorder;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the basic module for the injector,
 * which binds essential instances like the main frame.
 *
 * The main frame is injected into every window class and modified
 * by the individual window classes (implementing {@code WindowTemplate}).
 *
 * @author pxav
 */
public final class SimpleBinderModule extends AbstractModule {

  // the main frame which is used throughout the entire project.
  private final JFrame mainFrame;

  public SimpleBinderModule(JFrame mainFrame) {
    this.mainFrame = mainFrame;
  }

  /**
   * Configures the abstract Guice injector module.
   *
   * This allows you to
   */
  @Override
  protected void configure() {
    final CameraConnection cameraConnection = new CameraConnection();
    final LayoutManager layoutManager = mainFrame.getLayout();

    bind(JFrame.class).toInstance(this.mainFrame);
    bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
    bind(CameraConnection.class).toInstance(cameraConnection);
    bind(VideoRecorder.class).toInstance(new VideoRecorder());
    bind(ImageCapture.class).toInstance(new ImageCapture());
    bind(LayoutManager.class).toInstance(layoutManager);
  }
}
