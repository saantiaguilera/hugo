package hugo.weaving.anotations;

import android.os.Bundle;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for debugging rendering of a View
 * If you want to know how much time an activity takes to create, just use the
 * {@link DebugGenericLog } in the {@link android.app.Activity#onCreate(Bundle)} method for eg
 * Created by saguilera on 7/22/16.
 */
@Target({TYPE}) @Retention(RUNTIME)
public @interface DebugRenderLog {
}
