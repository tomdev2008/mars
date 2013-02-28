package com.birdstudio.mars.registry.support;

/**
 * Wrapper异常，用于指示 {@link FailbackRegistry}跳过Failback。
 * <p>
 * NOTE: 期望找到其它更常规的指示方式。
 * 
 * @author ding.lid
 * @see FailbackRegistry
 */
public class SkipFailbackWrapperException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8325459123471049013L;

	public SkipFailbackWrapperException(Throwable cause) {
		super(cause);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		// do nothing
		return null;
	}
}
