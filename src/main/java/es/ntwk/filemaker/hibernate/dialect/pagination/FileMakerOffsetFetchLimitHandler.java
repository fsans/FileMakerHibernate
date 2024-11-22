package es.ntwk.filemaker.hibernate.dialect.pagination;

import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.OffsetFetchLimitHandler;
import org.hibernate.query.spi.Limit;

/**
 * A {@link LimitHandler} for FileMaker, which
 * fully supports the ANSI SQL standard syntax
 * {@code FETCH FIRST m ROWS ONLY} and
 * {@code OFFSET n ROWS FETCH NEXT m ROWS ONLY}.
 *
 * @author Gavin King
 */
public class FileMakerOffsetFetchLimitHandler extends OffsetFetchLimitHandler {

	// [ORDER BY ...]
	//		[OFFSET n {ROW|ROWS}]
	// 		[FETCH {FIRST|NEXT} m {ROW|ROWS} ONLY]
	// 			[FOR {UPDATE|READ ONLY|FETCH ONLY}] or [WITH TIES]


	private static final Logger logger = LoggerFactory.getLogger(FileMakerOffsetFetchLimitHandler.class);

	public FileMakerOffsetFetchLimitHandler(boolean variableLimit) {
		super(variableLimit);
	}

	private static final Pattern FOR_UPDATE_WITH_LOCK_PATTERN =
			Pattern.compile("\\s+for\\s+(update|read|fetch)\\b|\\s+with\\s+ties\\b|\\s*;?\\s*$");

	/**
	 * The offset/fetch clauses must come before the
	 * {@code FOR UPDATE}ish and {@code WITH} clauses.
	 */
	@Override
	protected Pattern getForUpdatePattern() {
		return FOR_UPDATE_WITH_LOCK_PATTERN;
	}

	@Override
	public String processSql(String sql, Limit limit) {
		try {
			logger.debug("processSql method called");
			logger.debug("Processing clauses in LimitHandler");
			logger.debug("Original SQL: {}", sql);
			logger.debug("Before processing - Sql: {}, firstrow: {}, maxrows: {}", sql, limit.getFirstRow(), limit.getMaxRows());
	
			String processedSql = super.processSql(sql, limit);
	
			// Log the processed SQL
			logger.debug("Processed SQL: {}", processedSql);
	
			return processedSql;
		} catch (Exception e) {
			logger.error("Error in processSql: {}", e.getMessage(), e);
			throw e; // Rethrow if necessary
		}
	}

	
}


