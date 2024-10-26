package org.hibernate.community.dialect;

import java.sql.Types;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.sql.ast.SqlAstTranslatorFactory;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;
import org.hibernate.community.dialect.identity.FileMakerIdentityColumnSupport;
import org.hibernate.community.dialect.pagination.FileMakerLimitHandler;

/**
 * An SQL dialect for FileMaker.
 *
 * @author Francesc Sans
 */

public class FileMakerDialect extends Dialect {

    public FileMakerDialect() {
        this(DatabaseVersion.make(0, 0));
    }

    public FileMakerDialect(DatabaseVersion version) {
        super(version);
    }

    public FileMakerDialect(DialectResolutionInfo info) {
        super(info);
    }

    @Override
    public JdbcType resolveSqlTypeDescriptor(
            String columnTypeName,
            int jdbcTypeCode,
            int precision,
            int scale,
            JdbcTypeRegistry jdbcTypeRegistry) {

        switch (jdbcTypeCode) {
            case Types.FLOAT:
            case Types.TINYINT:
            case Types.INTEGER:
            case Types.DECIMAL:
            case Types.BOOLEAN:
                jdbcTypeCode = Types.DOUBLE;
                break;
            case Types.VARCHAR:
            case Types.LONGNVARCHAR:
                jdbcTypeCode = Types.VARCHAR;
                break;
        }
        return super.resolveSqlTypeDescriptor(columnTypeName, jdbcTypeCode, precision, scale, jdbcTypeRegistry);
    }

    @Override
    public LimitHandler getLimitHandler() {
        return new FileMakerLimitHandler();
    }


    @Override
    public FileMakerIdentityColumnSupport getIdentityColumnSupport() {
        return new FileMakerIdentityColumnSupport();
    }
    
    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean supportsColumnCheck() {
        return false;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }

    @Override
    public boolean supportsLockTimeouts() {
        return false;
    }

    @Override
    public boolean canCreateSchema() {
        return false;
    }

    @Override
    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    @Override
    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean supportsTableCheck() {
        return false;
    }

    @Override
    public boolean supportsUnionAll() {
        return false;
    }

    // New method required for Hibernate 6
    @Override
    public SqlAstTranslatorFactory getSqlAstTranslatorFactory() {
        return super.getSqlAstTranslatorFactory();
    }

    // New method required for Hibernate 6
    /*
    @Override
    public void initializeFunctionRegistry(QueryEngine queryEngine) {
        super.initializeFunctionRegistry(queryEngine);
    }
    */



}
