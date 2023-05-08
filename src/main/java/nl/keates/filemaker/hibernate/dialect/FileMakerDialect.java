/*
 *
 *
 * FIleMaker JDBC data types:
 * 
 * NUMERIC, DECIMAL, INT, DATE, TIME, TIMESTAMP, VARCHAR, CHARACTER VARYING, BLOB, VARBINARY, LONGVARBINARY, or BINARY VARYING
 * 
 * text         java.sql.Types.VARCHAR
 * number       java.sql.Types.DOUBLE
 * date         java.sql.Types.DATE
 * time         java.sql.Types.TIME
 * timestamp    java.sql.Types.TIMESTAMP
 * container    java.sql.Types.BLOB
 * calculation  (to the calculation result type)
 * 
 * 
 * Literals:
 * 
 * USER, USERNAME, CURRENT_USER, CURRENT_DATE, CURDATE, CURRENT_TIME, CURTIME, CURRENT_TIMESTAMP, CURTIMESTAMP, and NULL.
 * 
 * NOT SUPPORTED
 * 
 * SAVEPOINT statements
 * retrieval of auto-generated keys
 * passing parameters to a callable statement object by name 1 holdable cursors
 * retrieving and updating the object referenced by a Ref object
 * updating of columns containing CLOB, ARRAY, and REF data types 1 Boolean data type
 * DATALINK data type
 * transform groups and type mapping
 * relationships between the JDBC SPI and the Connector architecture
 * 
 *
 */

package nl.keates.filemaker.hibernate.dialect;

import org.hibernate.LockMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.engine.spi.RowSelection;

import java.sql.Types;

@SuppressWarnings("deprecation")
public class FileMakerDialect extends Dialect {



    private static final LimitHandler LIMIT_HANDLER = new AbstractLimitHandler() {

        @Override
        public String processSql(String sql, RowSelection selection) {
            String soff = String.format(" offset %d rows /*?*/", selection.getFirstRow());
            String slim = String.format(" fetch first %d rows only /*?*/", selection.getMaxRows());
            StringBuilder sb = (new StringBuilder(sql.length() + soff.length() + slim.length())).append(sql);

            if (LimitHelper.hasFirstRow(selection)) {
                sb.append(soff);
            }

            if (LimitHelper.hasMaxRows(selection)) {
                sb.append(slim);
            }

            return sb.toString();
        }

        @Override
        public boolean supportsLimit() {
            return true;
        }
    };
    

    public FileMakerDialect() {

        super();

        // database type mapping support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        registerColumnType(Types.VARCHAR, "VARCHAR");
        registerColumnType(Types.LONGNVARCHAR, "VARCHAR");

        registerColumnType(Types.DOUBLE, "numeric");
        registerColumnType(Types.DECIMAL, "decimal");
        registerColumnType(Types.INTEGER, "int");
        registerColumnType(Types.BOOLEAN, "int");

        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");

        registerColumnType( Types.VARBINARY, "varbinary" );
        registerColumnType( Types.LONGVARBINARY, "longvarbynary" );
        registerColumnType(Types.BLOB, "blob");

        //registerColumnType(Types.CLOB, "clob");


    }

    // limit/offset support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public LimitHandler getLimitHandler() {
        // set out custom handler defined above
        return LIMIT_HANDLER;
    }

    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just leads to error
        // messages about missing tables when we don't have a schema in the database
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



    // Current Timestamp support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public String getCurrentTimestampSQLFunctionName() {
        return "current_timestamp";
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

    @Override
    public boolean supportsUnique() {
        return false;
    }

    @Override
    public String toBooleanValueString(boolean arg0) {
        if (arg0) {
            return "{b'true'}";
        }
        return "{b'false'}";
    }

    // Locking support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public String getForUpdateNowaitString() {
        return "";
    }

    @Override
    public String getForUpdateNowaitString(String aliases) {
        return "";
    }

    @Override
    public String getForUpdateString() {
        return " for update";
    }

    @Override
    public String getForUpdateString(LockMode lockMode) {
        return "";
    }

    @Override
    public String getForUpdateString(String aliases) {
        return " for update of " + aliases;
    }

    //Sequences support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean supportsSequences() {
        return false;
    }

    @Override
    public boolean supportsPooledSequences() {
        return false;
    }

}

