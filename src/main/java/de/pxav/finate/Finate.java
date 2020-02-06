package de.pxav.finate;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.pxav.finate.binder.SimpleBinderModule;
import de.pxav.finate.gui.MainWindow;
import de.pxav.finate.gui.theme.ThemeRepository;
import de.pxav.finate.gui.theme.WindowTheme;

import javax.swing.*;
import java.awt.Dimension;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class Finate {

  private static JFrame mainFrame;

  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    mainFrame = new JFrame("Finate");
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mainFrame.setSize(1280, 720);
    mainFrame.setMinimumSize(new Dimension(1024, 576));
    mainFrame.setLocationByPlatform(true);

    Webcam.setDriver(new IpCamDriver());

    SimpleBinderModule simpleBinderModule = new SimpleBinderModule(mainFrame);

    Injector injector = Guice.createInjector(
            simpleBinderModule
    );

    injector.injectMembers(new Finate());

    injector.getInstance(ThemeRepository.class).detectThemes("de.pxav.finate");
    WindowTheme windowTheme = injector.getInstance(ThemeRepository.class).getByName("Light theme");
    injector.getInstance(ThemeRepository.class).changeCurrentTheme(windowTheme);

    SwingUtilities.invokeLater(() -> {
      injector.getInstance(MainWindow.class).draw();
    });

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("APPLICATION IS SHUTTING DOWN!");
    }, "SHUTDOWN_HOOK"));

  }
//
//  public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException, InterruptedException {
//    File file = new File("output.mp4");
//
//    IpCamDeviceRegistry.register(new IpCamDevice("DEFAULT", "http://192.168.132.112:8080/video", IpCamMode.PUSH));
//
//    IMediaWriter writer = ToolFactory.makeWriter(file.getName());
//    Dimension size = WebcamResolution.HD720.getSize();
//
//    writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, 640, 480);
//
//    Webcam webcam = Webcam.getDefault();
//    webcam.setViewSize(new Dimension(640, 480));
//    webcam.open(true);
//
//    long start = System.currentTimeMillis();
//
//    for (int i = 0; i < 50; i++) {
//
//      System.out.println("Capture frame " + i);
//
//      BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
//      IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
//
//      IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
//      frame.setKeyFrame(i == 0);
//      frame.setQuality(0);
//
//      writer.encodeVideo(0, frame);
//
//      // 10 FPS
//      Thread.sleep(25);
//    }
//
//    writer.close();
//
//    System.out.println("Video recorded in file: " + file.getAbsolutePath());


}

  /*

  long start = System.currentTimeMillis();
    BufferedImage image;
    try {

      URL url = new URL("http://192.168.132.112:8080/photo.jpg");
      image = ImageIO.read(url);
      System.out.println("read  " + (System.currentTimeMillis() - start));

      ImageIO.write(image, "png",new File("out.png"));
      System.out.println("write " + (System.currentTimeMillis() - start));

    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Done");
    System.out.println("Took " + (System.currentTimeMillis() - start));
   */
