package de.pxav.finate.connection;

import com.github.sarxos.webcam.WebcamUtils;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class ImageCapture {

  private CameraConnection cameraConnection;
  private File outputFile;
  private int delayInSeconds;
  private String imageFormat;
  private boolean isCapturing = false;

  private Thread worker;
  private int index = 1;

  public ImageCapture() {}

  public void timedCapture() {
    isCapturing = true;
    this.worker = new Thread(() -> {
      while (isCapturing) {
        this.capture("IMG_" + index);
        index++;

        try {
          Thread.sleep(delayInSeconds * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }, "TIMED_CAPTURE_" + ThreadLocalRandom.current().nextInt(1000));
    this.worker.start();
  }

  public void capture(String fileName) {
    WebcamUtils.capture(cameraConnection.getWebcam(), this.outputFile + "/" + fileName, imageFormat);
  }

  public void stopCapturing() {
    this.isCapturing = false;
  }

  public void setCameraConnection(CameraConnection cameraConnection) {
    this.cameraConnection = cameraConnection;
  }

  public void setOutputFile(File outputFile) {
    this.outputFile = outputFile;
  }

  public void setImageFormat(String imageFormat) {
    this.imageFormat = imageFormat;
  }

  public void setDelayInSeconds(int delayInSeconds) {
    this.delayInSeconds = delayInSeconds;
  }
}
