package org.hibernate.community.dialect.identity;

import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

public class FileMakerIdentityColumnSupport extends IdentityColumnSupportImpl {
 
  
    @Override
    public boolean supportsIdentityColumns() {
        return false;
    }

    @Override
    public boolean supportsInsertSelectIdentity(){
        return false;
    }


    @Override
    public boolean hasDataTypeInIdentityColumn() {
        return false; // FileMaker doesn't support a native identity column type
    }

    @Override
    public String getIdentityColumnString(int type) {
        return "serial"; // Representing the concept, adjust as per actual requirement
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type) {
        // Customize based on FileMaker approach (e.g., use "getGeneratedKeys" in practice)
        //return "SELECT MAX(" + column + ") FROM " + table;
        return "SELECT MAX(ROWID) FROM " + table;
    }

    @Override
    public String getIdentityInsertString() {
        return null; // Hibernate will use this during inserts (use null in this case)
    }
}

