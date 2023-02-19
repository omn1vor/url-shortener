package com.example.urlshortener.service.impl;

import com.example.urlshortener.exception.*;
import com.example.urlshortener.service.ExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ExceptionBuilderImpl implements ExceptionBuilder {
	@Autowired
	private MessageSource messageSource;

	@Override
	public UrlNotValidException urlNotValid(String url) {
		throw new UrlNotValidException(messageSource.getMessage("url.not-valid",
				new Object[]{url},
				LocaleContextHolder.getLocale()));
	}

	@Override
	public ShortUrlException codeAlreadyExists(String code) {
		throw new ShortUrlException(messageSource.getMessage("code.already-exists",
				new Object[]{code},
				LocaleContextHolder.getLocale()));
	}

	@Override
	public CodeNotFoundException codeNotFound(String code) {
		throw new CodeNotFoundException(messageSource.getMessage("code.not-found",
				new Object[]{code},
				LocaleContextHolder.getLocale()));
	}

	@Override
	public IllegalModificationException illegalFieldModification(String fieldName) {
		throw new IllegalModificationException(messageSource.getMessage("modification.not-allowed",
				new Object[]{fieldName},
				LocaleContextHolder.getLocale()));
	}

	@Override
	public ShortUrlException outputLimitExceeded(int limit) {
		throw new ShortUrlException(messageSource.getMessage("output.limit-exceeded",
				new Object[]{limit},
				LocaleContextHolder.getLocale()));
	}

	@Override
	public WrongPatchFormatException wrongPatchFormat() {
		throw new WrongPatchFormatException(messageSource.getMessage("modification.wrong-format",
				null,
				LocaleContextHolder.getLocale()));
	}
}
