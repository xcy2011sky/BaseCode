package com.xcy.lib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author xcy
 * ViewFinder
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })

public @interface FindViewById {

	int value();
}
