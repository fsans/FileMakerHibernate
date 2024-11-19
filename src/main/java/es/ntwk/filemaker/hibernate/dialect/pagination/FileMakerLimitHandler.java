package es.ntwk.filemaker.hibernate.dialect.pagination;

import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.query.spi.Limit;

public class FileMakerLimitHandler extends AbstractLimitHandler {

	/*
	 * design notes
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
	 * 
	 * 
	 * this is quite diferent from OffsetFetchLimitHandler
	 * 
	 * [ORDER BY ...]
	 * [OFFSET n {ROW|ROWS}]
	 * [FETCH {FIRST|NEXT} m {ROW|ROWS} ONLY] -> must be FETCH FIRST [ n [ PERCENT ]
	 * ] { ROWS | ROW } {ONLY | WITH TIES } ]
	 * [FOR {UPDATE|READ ONLY|FETCH ONLY}] -> must be FOR UPDATE [OF
	 * column_expressions]
	 * [WITH {RR|RS|CS|UR}] -> must be replaced by WITH TIES
	 * 
	 */

	public static final FileMakerLimitHandler INSTANCE = new FileMakerLimitHandler(true);

	private final boolean variableLimit;

	public FileMakerLimitHandler(boolean variableLimit) {
		this.variableLimit = variableLimit;
	}

	@Override
	public String processSql(String sql, Limit limit) {

		System.out.println("#########################  processSql");

		boolean hasFirstRow = hasFirstRow(limit);
		boolean hasMaxRows = hasMaxRows(limit);

		if (!hasFirstRow && !hasMaxRows) {
			return sql;
		}

		System.out.println("#########################  Has limits");

		StringBuilder offsetFetch = new StringBuilder();

		begin(sql, offsetFetch, hasFirstRow, hasMaxRows);

		if (hasFirstRow) {
			offsetFetch.append(" offset ");
			if (supportsVariableLimit()) {
				offsetFetch.append("?");
			} else {
				offsetFetch.append(limit.getFirstRow());
			}
			if (renderOffsetRowsKeyword()) {
				offsetFetch.append(" rows");
			}

		}
		if (hasMaxRows) {
			if (hasFirstRow) {
				offsetFetch.append(" fetch next ");
			} else {
				offsetFetch.append(" fetch first ");
			}
			if (supportsVariableLimit()) {
				offsetFetch.append("?");
			} else {
				offsetFetch.append(getMaxOrLimit(limit));
			}
			offsetFetch.append(" rows only");
		}

		System.out.println("######################### " +  insert(offsetFetch.toString(), sql)  );

		return insert(offsetFetch.toString(), sql);
	}

	void begin(String sql, StringBuilder offsetFetch, boolean hasFirstRow, boolean hasMaxRows) {
	}

	String insert(String offsetFetch, String sql) {
		return insertBeforeForUpdate(offsetFetch, sql);
	}

	@Override
	public final boolean supportsLimit() {
		return true;
	}

	@Override
	public boolean supportsOffset() {
		return true;
	}

	@Override
	public final boolean supportsVariableLimit() {
		return variableLimit;
	}

	protected boolean renderOffsetRowsKeyword() {
		return true;
	}

}
