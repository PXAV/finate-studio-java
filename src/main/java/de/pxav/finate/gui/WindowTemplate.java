package de.pxav.finate.gui;

/**
 * This interface class should be implemented by all
 * window classes handling a UI interface.
 *
 * It provides methods for
 *
 * @author pxav
 */
public interface WindowTemplate {

  /**
   * Loads the components and adds them to the given frame.
   * After all components have been added the frame will be
   * repainted.
   *
   * @return  The instance of the window class so that the
   *          current window can be updated in the repository
   *          class if needed.
   */
  WindowTemplate show();

  /**
   * This method is called every time the window
   * is resized by the user and updates the location and
   * size of the window components.
   *
   * Therefore, it's recommended to set the sizes and locations
   * relative to the window size.
   */
  void updateComponents();

  /**
   * When the user wants to close or switch the window,
   * the current one has to be unloaded first.
   */
  void unload();

}
