package com.filemaker.hibernate.dialect.identity;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.GetGeneratedKeysDelegate;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.id.PostInsertIdentityPersister;

public class FileMakerIdentityColumnSupport implements IdentityColumnSupport {
   
    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public boolean supportsInsertSelectIdentity() {
        return true;
    }

    @Override
    public boolean hasDataTypeInIdentityColumn() {
        return true;
    }

    @Override
    public String appendIdentitySelectToInsert(String insertString) {
        return insertString;
    }

    /**
    * Get the select command to use to retrieve the last generated IDENTITY
    * value for a particular table
    *
    * @param table The table into which the insert was done
    * @param column The PK column.
    * @param type The {@link java.sql.Types} type code.
    *
    * @return The appropriate select command
    *
    * @throws MappingException If IDENTITY generation is not supported.
    */
    @Override
    public String getIdentitySelectString(String table, String column, int type) throws MappingException {
        //throw new MappingException( getClass().getName() + " does not support identity key generation" );
        //  WARNING This is build on the assumption thta all PK columns will be named "id" (autoenter, not null, )
        return "select max(id) from " + table;
    }

    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        throw new MappingException( getClass().getName() + " does not support identity key generation" );
    }

    @Override
    public String getIdentityInsertString() {
        return null;
    }

    @Override
    public GetGeneratedKeysDelegate buildGetGeneratedKeysDelegate(
            PostInsertIdentityPersister persister,
            Dialect dialect) {
        return new GetGeneratedKeysDelegate( persister, dialect );
    }
    
}
