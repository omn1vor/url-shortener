package com.example.urlshortener.service;

import com.example.urlshortener.exception.*;

public interface ExceptionBuilder {
	UrlNotValidException urlNotValid(String url);

	ShortUrlException codeAlreadyExists(String code);

	CodeNotFoundException codeNotFound(String code);

	IllegalModificationException illegalFieldModification(String fieldName);

	ShortUrlException outputLimitExceeded(int limit);

	WrongPatchFormatException wrongPatchFormat();
}