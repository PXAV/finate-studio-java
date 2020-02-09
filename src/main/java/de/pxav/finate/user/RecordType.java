package de.pxav.finate.user;

/**
 * These enum states describe which type of record
 * the user wants to take from his camera.
 *
 * @author pxav
 */
public enum RecordType {

  /**
   * The user wants to take photos in a given interval
   * for a defined period of time.
   */
  IMAGES,

  /**
   * The user wants to have a time lapse of his record.
   * This means photos
   */
  TIME_LAPSE_VIDEO,

  /**
   * The user wants to record a simple video without
   * any editing and in real time.
   */
  NORMAL_VIDEO;

}
