package juzu.plugin.portlet.impl;

import juzu.Scope;
import juzu.impl.inject.BeanDescriptor;
import juzu.impl.metadata.Descriptor;
import juzu.impl.bridge.spi.portlet.PortletPreferencesProvider;
import juzu.impl.common.Tools;

import java.util.Collections;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class PortletDescriptor extends Descriptor {

  /** . */
  public static PortletDescriptor INSTANCE = new PortletDescriptor();

  private PortletDescriptor() {
  }

  @Override
  public Iterable<BeanDescriptor> getBeans() {
    try {
      Class portletPreferencesClass = Thread.currentThread().getContextClassLoader().loadClass("javax.portlet.PortletPreferences");
      return Tools.list(new BeanDescriptor(portletPreferencesClass, Scope.REQUEST, null, PortletPreferencesProvider.class));
    }
    catch (ClassNotFoundException e) {
      // Not available
      return Collections.emptyList();
    }
  }
}
