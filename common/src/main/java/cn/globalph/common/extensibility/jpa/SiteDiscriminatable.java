package cn.globalph.common.extensibility.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark collections for multi-tenancy when the commercial multi-tenant module is loaded. Please note, multi-tenant
 * collections are NOT eligible for level 2 cache, which results in a query to the database every time the collection is lazy initialized.
 * This can result in production performance degradation depending on how frequently the collection is utilized. It is
 * for this reason that we recommend utilizing a custom service or service extension that explicitly creates a query
 * for the collection entity members based on the parent entity. See the multi-tenant module documentation for more
 * information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SiteDiscriminatable {

    SiteDiscriminatableType type();

}
