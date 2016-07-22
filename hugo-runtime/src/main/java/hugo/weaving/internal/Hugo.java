package hugo.weaving.internal;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import android.view.View;
import hugo.weaving.anotations.DebugRenderLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

@Aspect
public class Hugo {
  private static volatile boolean enabled = true;

  @Pointcut("within(@hugo.weaving.anotations.DebugGenericLog *)")
  public void trackGeneric() {}

  @Pointcut("within(@hugo.weaving.anotations.DebugRenderLog *)")
  public void trackRender() {}

  @Pointcut("execution(!synthetic * *(..)) && (trackGeneric() || trackRender())")
  public void methodInsideAnnotatedType() {}

  @Pointcut("execution(!synthetic *.new(..)) && (trackGeneric() || trackRender())")
  public void constructorInsideAnnotatedType() {}

  @Pointcut("execution(@hugo.weaving.anotations.DebugGenericLog * *(..)) || methodInsideAnnotatedType()")
  public void method() {}

  @Pointcut("execution(@hugo.weaving.anotations.DebugGenericLog *.new(..)) || constructorInsideAnnotatedType()")
  public void constructor() {}

  public static void setEnabled(boolean enabled) {
    Hugo.enabled = enabled;
  }

  @Around("method() || constructor()")
  public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
    enterMethod(joinPoint);

    long startNanos = System.nanoTime();
    Object result = joinPoint.proceed();
    long stopNanos = System.nanoTime();
    long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

    exitMethod(joinPoint, result, lengthMillis);

    return result;
  }

  private static void enterMethod(JoinPoint joinPoint) {
    if (!enabled) return;

    CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

    Class<?> cls = codeSignature.getDeclaringType();
    String methodName = codeSignature.getName();

    if (cls.getAnnotation(DebugRenderLog.class) == null) {
      String[] parameterNames = codeSignature.getParameterNames();
      Object[] parameterValues = joinPoint.getArgs();

      StringBuilder builder = new StringBuilder("\u21E2 ");
      builder.append(methodName).append('(');
      for (int i = 0; i < parameterValues.length; i++) {
        if (i > 0) {
          builder.append(", ");
        }
        builder.append(parameterNames[i]).append('=');
        builder.append(Strings.toString(parameterValues[i]));
      }
      builder.append(')');

      if (Looper.myLooper() != Looper.getMainLooper()) {
        builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
      }
      
      log(cls, builder.toString());

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        final String section = builder.toString().substring(2);
        Trace.beginSection(section);
      }
    }
  }

  private static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {
    if (!enabled) return;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      Trace.endSection();
    }

    Signature signature = joinPoint.getSignature();

    Class<?> cls = signature.getDeclaringType();
    String methodName = signature.getName();
    
    if (cls.getAnnotation(DebugRenderLog.class) != null) {
      if (!View.class.isAssignableFrom(cls))
        throw new UnsupportedAnnotationException("Using " + DebugRenderLog.class.getSimpleName() +
                " annotation inside " + asTag(cls) + " which doesnt subclassifies View, so it cant" +
                "be measured its render time");

      //If its a constructor let it track to know how much times it takes to create
      boolean shouldTrack = signature instanceof ConstructorSignature;

      if (signature instanceof MethodSignature) {
        for (String string : ViewTrackableMethods.methodsName)
          if (methodName.contentEquals(string)) shouldTrack = true;
      }

      if (!shouldTrack)
        return;
    }

    boolean hasReturnType = signature instanceof MethodSignature
            && ((MethodSignature) signature).getReturnType() != void.class;

    StringBuilder builder = new StringBuilder("\u21E0 ")
            .append(methodName)
            .append(" [")
            .append(lengthMillis)
            .append("ms]");

    if (hasReturnType) {
      builder.append(" = ");
      builder.append(Strings.toString(result));
    }
    
    Log.v(asTag(cls), builder.toString());
  }

  private static void log(Class<?> tag, String body) {
    Log.v(asTag(tag), body);
  }

  private static String asTag(Class<?> cls) {
    if (cls.isAnonymousClass()) {
      return asTag(cls.getEnclosingClass());
    }
    return cls.getSimpleName();
  }
}
