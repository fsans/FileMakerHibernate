# FileMaker Hibernate Dialect

- Added request limit handling
- Fixed implementation to Hibernate estandars
- Adding as dependency
- Update to version 20.1.4 (aka FileMaker/Claris 2023)
- - tested ok
- - added a maven repo local
- 1st attempt migration from Hibernate v5 to Hibernate v6
>Termporary excluded identityColumn support, still on investigation
- Update to version 21.0.1 (aka FileMaker/Claris 2024)
- - tested OK
- - added a maven repo local
- in progress - Migration to hibernate 6
- in progress - Adding test suite junit5
  
### branch: Feature/hibernate6 test suite
migrated to version 6.5.3.Final. Still not production ready because is using an autoenter serialized id in FileMaker side and then using "select max(id) from table" that may be a problem in concurrent connections.
Should be imoplemented using either:
- usding UUID strategy
FileMakerIdentityColumnSupport.java
```java
@Override
public boolean supportsIdentityColumns() {
    return true;  // Change to true since we'll handle ID generation
}

@Override
public String getIdentityColumnString(int type) {
    return "TEXT DEFAULT Get(UUID)";  // Use FileMaker's UUID function
}

@Override
public String getIdentitySelectString(String table, String column, int type) {
    return "SELECT Get(UUID)";  // Get the UUID directly
}
```
Contact.java
```java
@Entity
@Table(name = "contact")
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Change to UUID
    private String id;  // Change to String for UUID
    // ... rest of the class
}
```

- using one id as autoneter get(recordID) and then "SELECT ROWID FROM table"
FileMakerIdentityColumnSupport.java
```java
@Override
public String getIdentityColumnString(int type) {
    return "TEXT DEFAULT Get(RecordID)";  // Use FileMaker's RecordID
}

@Override
public String getIdentitySelectString(String table, String column, int type) {
    return "SELECT ROWID FROM " + table;
}
```
- using a helper identity table, but it seems too complex if can be resolved by the previous options
  FileMakerIdentityColumnSupport.java
```Java
@Override
public String getSequenceNextValString(String sequenceName) {
    return "SELECT next_val FROM hibernate_sequences WHERE sequence_name = '" + sequenceName + "'";
}

@Override
public String getSelectSequenceNextValString(String sequenceName) {
    return "SELECT next_val FROM hibernate_sequences WHERE sequence_name = '" + sequenceName + "'";
}

@Override
public String getIncrementSequenceString(String sequenceName) {
    return "UPDATE hibernate_sequences SET next_val = next_val + 1 WHERE sequence_name = '" + sequenceName + "'";
}
````

```sql
CREATE TABLE hibernate_sequences (
    sequence_name TEXT PRIMARY KEY,
    next_val NUMBER DEFAULT 1
);
```
