package com.filemaker.hibernate.dialect;

import java.sql.Types;

import org.hibernate.LockMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.pagination.LimitHandler;

import com.filemaker.hibernate.dialect.identity.FileMakerIdentityColumnSupport;
import com.filemaker.hibernate.dialect.pagination.FileMakerLimitHandler;
 
import static org.hibernate.type.SqlTypes.ROWID;

 @SuppressWarnings("deprecation")
 public class FileMakerDialect extends Dialect {
 
     public FileMakerDialect() {
 
         super();
 
         // database type mapping support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 



     // identity columns support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     @Override
     public IdentityColumnSupport getIdentityColumnSupport() {
         return new FileMakerIdentityColumnSupport();
     }
 
     // limit/offset support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     @Override
     public LimitHandler getLimitHandler() {
         return new FileMakerLimitHandler();
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
 
 