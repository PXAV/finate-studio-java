package de.pxav.finate.gui.theme;

import com.google.common.collect.Sets;
import com.google.inject.Singleton;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.awt.*;
import java.util.Set;

/**
 * A class description goes here.
 *
 * @author pxav
 */
@Singleton
public class ThemeRepository {

  private Set<WindowTheme> themes = Sets.newHashSet();
  private WindowTheme currentTheme;

  public ThemeRepository() {

  }

  public void detectThemes(String... packages) throws IllegalAccessException, InstantiationException {
    try (ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .whitelistPackages(packages)
            .scan()) {

      ClassInfoList classInfoList = scanResult.getAllClasses();

      for (ClassInfo classInfo : classInfoList) {
        if (classInfo.getSuperclass() != null
                && classInfo.getSuperclass().getName().equalsIgnoreCase(WindowTheme.class.getName())) {
          WindowTheme windowTheme = (WindowTheme) classInfo.loadClass().newInstance();

          if (!this.nameAvailable(windowTheme.themeName())) {
            System.out.println("Theme with name " + windowTheme.themeName() + " does already exist. Please choose another name!");
            continue;
          }

          windowTheme.defineColors();
          themes.add(windowTheme);
        }
      }

    }
  }

  public void changeCurrentTheme(WindowTheme newTheme) {
    this.currentTheme = newTheme;
  }

  public Color getColor(WindowElement windowElement) {
    return currentTheme.getColor(windowElement);
  }

  public WindowTheme getByName(String name) {
    for (WindowTheme current : themes) {
      if (current.themeName().equalsIgnoreCase(name)) {
        return current;
      }
    }
    return null;
  }

  public Set<WindowTheme> getThemes() {
    return themes;
  }

  private boolean nameAvailable(String themeName) {
    for (WindowTheme theme : themes) {
      if (theme.themeName().equalsIgnoreCase(themeName)) {
        return false;
      }
    }
    return true;
  }

}
