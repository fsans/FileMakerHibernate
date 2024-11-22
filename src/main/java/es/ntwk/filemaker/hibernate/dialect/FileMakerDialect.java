package es.ntwk.filemaker.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.OffsetFetchLimitHandler;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.sql.ast.SqlAstTranslatorFactory;
import org.hibernate.sql.ast.spi.StandardSqlAstTranslatorFactory;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import es.ntwk.filemaker.hibernate.dialect.identity.FileMakerIdentityColumnSupport;


/* An SQL dialect for FileMaker.
 
@author Francesc Sans

WARNING: FileMaker JDBC driver does not support:

- SAVEPOINT statements
- retrieval of auto-generated keys (TO BE REVIEWED)
- passing parameters to a callable statement object by name 
- holdable cursors
- retrieving and updating the object referenced by a Ref object
- updating of columns containing CLOB, ARRAY, and REF data types 
- Boolean data type
- DATALINK data type
- transform groups and type mapping
- relationships between the JDBC SPI and the Connector architecture


Data Types: (filemaler - JDBC)
text 		java.sql.Types.VARCHAR
number 		java.sql.Types.DOUBLE
date 		java.sql.Types.DATE
time 		java.sql.Types.TIME
timestamp 	java.sql.Types.TIMESTAMP
container 	java.sql.Types.BLOB

supports standar ANSI pagination with: OFFSET n ROW|ROWS / FETCH FIRST n ROW|ROWS ONLY (and FOR UPDATE and SORT BY ... WITH TIES)


*/

public class FileMakerDialect extends Dialect {

    private static final Logger logger = LoggerFactory.getLogger(FileMakerDialect.class);

    private static final DatabaseVersion DEFAULT_VERSION = DatabaseVersion.make(21, 0);

    public FileMakerDialect() {
        this(DEFAULT_VERSION);
    }

    public FileMakerDialect(DatabaseVersion version) {
        super(version);
    }

    public FileMakerDialect(DialectResolutionInfo info) {
        // super(info);
        this(info.makeCopyOrDefault(DEFAULT_VERSION));
        registerKeywords(info);
    }


    @Override
    protected String columnType(int sqlTypeCode) {
        switch (sqlTypeCode) {
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.INTEGER:
                return "numeric"; // Maps to FileMaker's numeric type
    
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CHAR:
                return "varchar"; // Maps to FileMaker's varchar type
    
            case Types.BLOB:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return "blob"; // Maps to FileMaker's blob type
    
            case Types.DATE:
                return "date"; // Maps to FileMaker's date type
    
            case Types.TIME:
                return "time"; // Maps to FileMaker's time type
    
            case Types.TIMESTAMP:
                return "timestamp"; // Maps to FileMaker's timestamp type
    
            default:
                return super.columnType( sqlTypeCode ); // Return a default type or handle unsupported types
        }
    }


   @Override
    public LimitHandler getLimitHandler() {
        logger.debug("getLimitHandler called, returning OffsetFetchLimitHandler");
        return new OffsetFetchLimitHandler(false);
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
    // custom FileMaker AST translator
    /* @Override
	public SqlAstTranslatorFactory getSqlAstTranslatorFactory() {
		return new StandardSqlAstTranslatorFactory() {
			@Override
			protected <T extends JdbcOperation> SqlAstTranslator<T> buildTranslator(
					SessionFactoryImplementor sessionFactory, Statement statement) {
                        logger.debug("statement for FileMakerSqlAstTranslator: {}", statement);
				return new FileMakerSqlAstTranslator<>( sessionFactory, statement );
			}
		};
	} */
    
    @Override
    public SqlAstTranslatorFactory getSqlAstTranslatorFactory() {
        //return super.getSqlAstTranslatorFactory();
        return new StandardSqlAstTranslatorFactory();
    }

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
