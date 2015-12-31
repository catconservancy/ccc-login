package org.rmcc.ccc.annotations;

import org.rmcc.ccc.logging.CccLogger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {
    CccLogger.Level value() default CccLogger.Level.DEBUG;
    boolean verboseMode() default false;
    String[] excluded() default "password";

}