/*
 * Copyright (C) 2011 eXo Platform SAS.
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

package juzu.impl.template.metamodel;

import juzu.Path;
import juzu.impl.compiler.ElementHandle;
import juzu.impl.metamodel.MetaModel;
import juzu.impl.metamodel.MetaModelObject;
import juzu.impl.common.JSON;

import javax.lang.model.element.VariableElement;

/**
 * A reference to a template.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class TemplateRefMetaModel extends MetaModelObject {

  /** . */
  final ElementHandle.Field handle;

  /** . */
  juzu.impl.common.Path path;

  TemplateRefMetaModel(
    ElementHandle.Field handle,
    juzu.impl.common.Path path) {
    this.handle = handle;
    this.path = path;
  }

  public ElementHandle.Field getHandle() {
    return handle;
  }

  public juzu.impl.common.Path getPath() {
    return path;
  }

  public JSON toJSON() {
    JSON json = new JSON();
    json.set("handle", handle);
    json.set("template", getChild(TemplateMetaModel.KEY));
    return json;
  }

  @Override
  public boolean exist(MetaModel model) {
    VariableElement fieldElt = model.env.get(handle);
    boolean exist = true;
    if (fieldElt == null) {
      MetaModel.log.log("Removing handle " + handle + " that does not exist anymore");
      exist = false;
    }
    else if (fieldElt.getAnnotation(Path.class) == null) {
      MetaModel.log.log("Removing handle " + handle + " that is not annoated anymore");
      exist = false;
    }
    return exist;
  }
}
