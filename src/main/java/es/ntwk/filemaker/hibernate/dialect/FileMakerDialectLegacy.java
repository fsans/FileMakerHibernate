package es.ntwk.filemaker.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.sql.ast.SqlAstTranslatorFactory;
import org.hibernate.sql.ast.spi.StandardSqlAstTranslatorFactory;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import es.ntwk.filemaker.hibernate.dialect.identity.FileMakerIdentityColumnSupport;
import es.ntwk.filemaker.hibernate.dialect.pagination.FileMakerOffsetFetchLimitHandler;

/**
 * An SQL dialect for FileMaker.
 *
 * @author Francesc Sans
 */

public class FileMakerDialectLegacy extends Dialect {

    private static final Logger logger = LoggerFactory.getLogger(FileMakerDialectLegacy.class);

    private static final DatabaseVersion DEFAULT_VERSION = DatabaseVersion.make(21, 0);

    private final LimitHandler limitHandler = new FileMakerOffsetFetchLimitHandler(true);

    public FileMakerDialectLegacy() {
        this(DEFAULT_VERSION);
    }

    public FileMakerDialectLegacy(DatabaseVersion version) {
        super(version);
        logger.info("FileMakerDialectLegacy being initialized fase 2!");
    }

    public FileMakerDialectLegacy(DialectResolutionInfo info) {
        // super(info);
        this(info.makeCopyOrDefault(DEFAULT_VERSION));
        registerKeywords(info);
        logger.info("FileMakerDialectLegacy being initialized fase 3!");
    }

    @Override
    public JdbcType resolveSqlTypeDescriptor(
            String columnTypeName,
            int jdbcTypeCode,
            int precision,
            int scale,

            /*
             * driver supported data types:
             * "numeric", "decimal", "int", "varchar", "character varying", "blob",
             * "varbinary", "longvarbinary", "binary varying", "date", "time", "timestamp"
             * 2, 3, 4, 12, 12, -2, -2, -2, -2, 91, 92, 93
             */

            JdbcTypeRegistry jdbcTypeRegistry) {

        switch (jdbcTypeCode) {

            case Types.NUMERIC: // 2 
            case Types.DECIMAL: // 3
            case Types.INTEGER: // 4
            case Types.TINYINT: // -6
            case Types.FLOAT: // 6
                jdbcTypeCode = Types.NUMERIC; // Map to FileMaker's native "Number"
                break;

            case Types.VARCHAR: // 12 
            case Types.LONGVARCHAR: // -1
            case Types.CLOB: // 2005
                jdbcTypeCode = Types.VARCHAR; // Map to character varying
                break;

            case Types.BLOB: // 2004
            case Types.VARBINARY: // -1
            case Types.LONGVARBINARY: // -4
                jdbcTypeCode = Types.BINARY;
                break;

            case Types.DATE: // 91
                jdbcTypeCode = Types.DATE;
                break;

            case Types.TIME: // 92
                jdbcTypeCode = Types.TIME;
                break;

            case Types.TIMESTAMP: // 93
                jdbcTypeCode = Types.TIMESTAMP;
                break;

            default:
                jdbcTypeCode = Types.VARCHAR;
        }
        return super.resolveSqlTypeDescriptor(
                columnTypeName,
                jdbcTypeCode,
                precision,
                scale,
                jdbcTypeRegistry);
    }

    @Override
    public LimitHandler getLimitHandler() {
        return limitHandler;
    }

    @Override
    public FileMakerIdentityColumnSupport getIdentityColumnSupport() {
        return FileMakerIdentityColumnSupport.INSTANCE;
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
        //return super.getSqlAstTranslatorFactory();
        return new StandardSqlAstTranslatorFactory();
    }

    // New method required for Hibernate 6
    /*
     * @Override
     * public void initializeFunctionRegistry(QueryEngine queryEngine) {
     * super.initializeFunctionRegistry(queryEngine);
     * }
     */

    @Override
    public String[] getCreateSchemaCommand(String schemaName) {
        throw new UnsupportedOperationException("No create schema syntax supported by " + getClass().getName());
    }

    @Override
    public String[] getDropSchemaCommand(String schemaName) {
        throw new UnsupportedOperationException("No drop schema syntax supported by " + getClass().getName());
    }

}
