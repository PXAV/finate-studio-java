package de.pxav.finate.gui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a repository for all states the main frame
 * can have during runtime.
 *
 *
 *
 * @author pxav
 */
@Singleton
public class WindowRepository {

  private final Injector injector;

  private final Map<String, WindowTemplate> windows;
  private WindowTemplate currentWindow;

  @Inject
  public WindowRepository(Injector injector) {
    this.injector = injector;
    this.windows = new HashMap<>();
  }

  public void detectWindows(String... packages) {
    System.out.println("detecting windows");
    try (ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .whitelistPackages(packages)
            .scan()) {

      ClassInfoList classInfoList = scanResult.getClassesImplementing(WindowTemplate.class.getName());

      for (ClassInfo classInfo : classInfoList) {
        System.out.println(classInfo.getName());
        WindowTemplate windowTemplate = (WindowTemplate) injector.getInstance(classInfo.loadClass());
        this.windows.put(classInfo.getName(), windowTemplate);
      }
    }
  }

  public void openWindowAndReset(Class<? extends WindowTemplate> windowClass) {
    if (currentWindow != null) {
      this.currentWindow.unload();
    }
    this.currentWindow = windows.get(windowClass.getName()).show();
  }

  public void showWindowAndKeep(Class<? extends WindowTemplate> windowClass) {
    System.out.println("window list "+windows);
    System.out.println("window "+windows.get(windowClass.getName()));
    this.currentWindow = windows.get(windowClass.getName()).show();
  }

  public void unloadWindow(Class<? extends WindowTemplate> windowClass) {
    windows.get(windowClass.getName()).unload();
  }

}
