package com.apriori.shared.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a model that can be created using a POST verb in the system.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CreatableModel {
    /**
     * The name type of the CreatableModel.
     *
     * @return The type name of the model when invoking a create call.
     */
    String value();
}
