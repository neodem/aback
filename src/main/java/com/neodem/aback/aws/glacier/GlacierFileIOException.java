package com.neodem.aback.aws.glacier;

public class GlacierFileIOException extends Exception {

	private static final long serialVersionUID = -8811282979658362573L;

	public GlacierFileIOException() {
	}

	public GlacierFileIOException(String message) {
		super(message);
	}

	public GlacierFileIOException(Throwable cause) {
		super(cause);
	}

	public GlacierFileIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public GlacierFileIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
