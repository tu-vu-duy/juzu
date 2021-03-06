/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package examples.tutorial.weather6;

import examples.tutorial.weather3.WeatherService;
import juzu.Action;
import juzu.Path;
import juzu.Response;
import juzu.View;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class Weather {

  static Set<String> locations = new HashSet<String>();

  static {
    locations.add("marseille");
    locations.add("paris");
  }

  @Inject
  WeatherService weatherService;

  @Inject
  @Path("index.gtmpl")
  examples.tutorial.weather6.templates.index index;

  @View
  public void index() {
    index("marseille");
  }

  @View
  public void index(String location) {
    index.
      with().
      location(location).
      temperature(weatherService.getTemperature(location)).
      locations(locations).
      render();
  }

  @Action
  public Response add(String location) {
    locations.add(location);
    return Weather_.index(location);
  }
}
