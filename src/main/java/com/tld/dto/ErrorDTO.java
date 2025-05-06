package com.tld.dto;

public class ErrorDTO {

		private String code;
		private String message;
	    private String details;

	    public ErrorDTO(String code, String message, String details) {
	    	this.code = code;
	        this.message = message;
	        this.details = details;
	    }
	    

	    public String getCode() {
	        return code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public String getDetails() {
	        return details;
	    }
	
}
