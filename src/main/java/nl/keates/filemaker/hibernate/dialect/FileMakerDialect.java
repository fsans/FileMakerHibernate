/*
 * Copyright Keates Creative Technology and/or its affiliates
 * and other contributors as indicated by the @author tags and
 * the COPYRIGHT.txt file distributed with this work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * FIleMaker JDBC types:
 *
 * NUMERIC, DECIMAL, INT, DATE, TIME, TIMESTAMP, VARCHAR, CHARACTER VARYING, BLOB, VARBINARY, LONGVARBINARY, or BINARY VARYING
 *
 * Literals:
 *
 * USER, USERNAME, CURRENT_USER, CURRENT_DATE, CURDATE, CURRENT_TIME, CURTIME, CURRENT_TIMESTAMP, CURTIMESTAMP, and NULL.
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
        return LIMIT_HANDLER;
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
    // TODO: check this
    @Override
    public String getForUpdateNowaitString() {
        return "";
    }
    // TODO: check this
    @Override
    public String getForUpdateNowaitString(String aliases) {
        return "";
    }

    // TODO: check this
    @Override
    public String getForUpdateString() {
        return " for update";
    }

    // TODO: check this
    @Override
    public String getForUpdateString(LockMode lockMode) {
        return "";
    }

    // TODO: fix this
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

