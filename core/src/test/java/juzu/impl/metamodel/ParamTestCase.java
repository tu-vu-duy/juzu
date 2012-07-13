package juzu.impl.metamodel;

import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import juzu.impl.compiler.CompilationError;
import juzu.impl.common.JSON;
import juzu.impl.common.Tools;
import juzu.test.AbstractTestCase;
import juzu.test.CompilerAssert;
import juzu.test.JavaFile;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static juzu.impl.common.JSON.json;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class ParamTestCase extends AbstractTestCase {

  /** . */
  private static JSON expectedJSON = json()
    .set("applications", json().
      list("values", json().
        list("controllers", json().
          set("handle", "ElementHandle.Class[fqn=model.meta.param.A]").
          list("methods", json().
            set("handle", "ElementHandle.Method[fqn=model.meta.param.A,name=index,parameterTypes[model.meta.param.Bean]]").
            set("id", null).
            set("name", "index").
            set("phase", "RENDER").
            list("parameters", json().
              set("name", "bean").
              set("declaredType", "model.meta.param.Bean").
              set("type", "ElementHandle.Class[fqn=model.meta.param.Bean]").
              set("cardinality", "SINGLE")
            )
          )
        ).
        set("fqn", "model.meta.param.ParamApplication").
        set("handle", "ElementHandle.Package[qn=model.meta.param]").
        list("templates")
      )
    );

  @Test
  public void testBuild() throws Exception {
    CompilerAssert<File, File> helper = incrementalCompiler("model", "meta", "param");
    helper.assertCompile();

    //
    MetaModel mm = Tools.unserialize(MetaModel.class, helper.getSourceOutput().getPath("juzu", "metamodel.ser"));
    assertEquals(expectedJSON, mm.toJSON());
  }

  @Test
  public void testParam() throws Exception {
    CompilerAssert<File, File> helper = incrementalCompiler("model", "meta", "param");
    helper.assertCompile();

    // Remove @Param
    JavaFile file = helper.assertJavaFile("model", "meta", "param", "Bean.java");
    ClassOrInterfaceDeclaration bean = file.assertDeclaration();
    AnnotationExpr annotation = bean.getAnnotations().get(0);
    bean.getAnnotations().clear();
    file.assertSave();
//      helper.assertRemove("model", "meta", "param", "A.java");

    // Recompile
    // we should have a way to test the error kind more precisely
    List<CompilationError> errors = helper.addClassPath(helper.getClassOutput()).failCompile();
    assertEquals(1, errors.size());

    // Add back @Param
    bean.getAnnotations().add(annotation);
    file.assertSave();

    // Recompile
    helper.addClassPath(helper.getClassOutput()).assertCompile();

    // Check
    MetaModel mm = Tools.unserialize(MetaModel.class, helper.getSourceOutput().getPath("juzu", "metamodel.ser"));
    assertEquals(expectedJSON, mm.toJSON());
  }
}
