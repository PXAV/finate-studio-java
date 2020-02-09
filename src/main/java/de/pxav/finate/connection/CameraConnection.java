package de.pxav.finate.connection;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import java.awt.*;
import java.io.IOException;

/**
 * A class description goes here.
 *
 * @author pxav
 */
public class CameraConnection {

  private String url;
  private Dimension resolution = new Dimension();

  private boolean isConnected = false;
  private Webcam webcam;

  public CameraConnection() {}

  public CameraConnection(String url) {
    this.url = url;
  }

  public void connect() throws IOException {
    if (this.isConnected) {
      return;
    }
    IpCamDeviceRegistry.unregisterAll();
    IpCamDeviceRegistry.register(new IpCamDevice("DEFAULT", url, IpCamMode.PUSH));
    this.webcam = Webcam.getDefault();
    this.webcam.open(true);
    this.isConnected = true;
  }

  public void disconnect() {
    if (!this.isConnected) {
      return;
    }
    IpCamDeviceRegistry.unregisterAll();
    this.isConnected = false;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setHeight(int height) {
    this.resolution.height = height;
  }

  public void setWidth(int width) {
    this.resolution.width = width;
  }

  public void setResolution(Dimension resolution) {
    this.resolution = resolution;
  }

  public Webcam getWebcam() {
    return webcam;
  }

}
