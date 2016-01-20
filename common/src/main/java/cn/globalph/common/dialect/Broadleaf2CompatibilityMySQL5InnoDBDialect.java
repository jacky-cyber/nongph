package cn.globalph.common.dialect;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * Intended to allow installations migrating from BLC version 2.0 to not be forced to make a schema
 * change for boolean fields when migrating to BLC version 3.0, and above.
 *
 * @deprecated use org.hibernate.dialect.MySQL5InnoDBDialect instead
 * @author Jeff Fischer
 */
@Deprecated
public class Broadleaf2CompatibilityMySQL5InnoDBDialect extends MySQL5InnoDBDialect {

    public Broadleaf2CompatibilityMySQL5InnoDBDialect() {
        super();
        registerColumnType( java.sql.Types.BOOLEAN, "bit" );
    }

}
