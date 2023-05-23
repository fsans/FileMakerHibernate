package com.filemaker.hibernate.dialect.pagination;

import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.engine.spi.RowSelection;

public class FileMakerLimitHandler extends AbstractLimitHandler {
    /* design notes
    * OFFSET clause should come first.
    * The OFFSET and FETCH FIRST clauses are not supported in subqueries
    * WITH TIES must be used with the ORDER BY clause
    *
    * Offset syntax:
    * OFFSET n {ROWS | ROW} ]
    *
    * Fetch syntax
    * FETCH FIRST [ n [ PERCENT ] ] { ROWS | ROW } {ONLY | WITH TIES } ]
    *
    */
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

    @Override
    public boolean supportsLimitOffset() {
        return supportsLimit();
    }
}
