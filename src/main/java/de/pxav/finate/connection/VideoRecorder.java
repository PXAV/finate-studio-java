package de.pxav.finate.connection;

import com.github.sarxos.webcam.Webcam;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class VideoRecorder {

  private CameraConnection cameraConnection;
  private File outputFile;
  private int framesPerSecond;
  private int currentRecordTimeInSeconds = 0;
  private int maxRecordTimeInSeconds = -1;

  private Thread thread;
  private IMediaWriter writer;
  private boolean isRecording = false;

  public VideoRecorder() {}

  public VideoRecorder(CameraConnection cameraConnection) {
    this.cameraConnection = cameraConnection;
  }

  public void record() throws InterruptedException {
    System.out.println("in record method");
    isRecording = true;
    this.thread = new Thread(() -> {
      System.out.println("in thread");
      Webcam webcam = cameraConnection.getWebcam();
      writer = ToolFactory.makeWriter(outputFile.getName());

      writer.addVideoStream(0,
              0,
              ICodec.ID.CODEC_ID_H264,
              webcam.getViewSize().width,
              webcam.getViewSize().height);

      long start = System.currentTimeMillis();

      // one second:
      // 30 + 1 31
      while (isRecording) {
        for (int i = 0; i < framesPerSecond + 1; i++) {
          System.out.println("Capture frame " + i);


          BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
          IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

          IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
          frame.setKeyFrame(i == 0);
          frame.setQuality(0);

          writer.encodeVideo(0, frame);

          try {
            Thread.sleep(delayFromFPS());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          if (i == framesPerSecond) {
            currentRecordTimeInSeconds++;
            if (maxRecordTimeInSeconds == currentRecordTimeInSeconds) {
              stopRecording();
            }
          }
        }
      }
      System.out.println("writer closed");
      writer.close();

      System.out.println("Video recorded in file: " + outputFile.getAbsolutePath());
    }, "RECORD_" + ThreadLocalRandom.current().nextInt(1000));
    thread.start();
    System.out.println("thread ...");
  }

  public void stopRecording() {
    System.out.println("stopped recording");
    this.isRecording = false;
  }

  private long delayFromFPS() {
    return 1000 / framesPerSecond;
  }

  public void setMaxRecordTimeInSeconds(int maxRecordTimeInSeconds) {
    this.maxRecordTimeInSeconds = maxRecordTimeInSeconds;
  }

  public void setOutputFile(File outputFile) {
    this.outputFile = outputFile;
  }

  public void setFramesPerSecond(int framesPerSecond) {
    this.framesPerSecond = framesPerSecond;
  }

  public void setCameraConnection(CameraConnection cameraConnection) {
    this.cameraConnection = cameraConnection;
  }

  public boolean isRecording() {
    return isRecording;
  }

  public int getFramesPerSecond() {
    return framesPerSecond;
  }

  public int getMaxRecordTimeInSeconds() {
    return maxRecordTimeInSeconds;
  }

  public int getCurrentRecordTimeInSeconds() {
    return currentRecordTimeInSeconds;
  }
}
