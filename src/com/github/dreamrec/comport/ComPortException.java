package com.github.dreamrec.comport;

public class ComPortException extends RuntimeException{

	private static final long serialVersionUID = 21464169459913595L;

	public ComPortException() {
	}

	public ComPortException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComPortException(String message) {
		super(message);
	}

	public ComPortException(Throwable cause) {
		super(cause);
	}
	

}
