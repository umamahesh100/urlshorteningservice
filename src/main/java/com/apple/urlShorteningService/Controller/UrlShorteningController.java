package com.apple.urlShorteningService.Controller;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apple.urlShorteningService.model.InvalidUrlError;
import com.apple.urlShorteningService.model.Url;
import com.apple.urlShorteningService.model.UrlDto;
import com.apple.urlShorteningService.model.UrlErrorResponseDto;
import com.apple.urlShorteningService.model.UrlResponseDto;
import com.apple.urlShorteningService.service.UrlService;
import com.apple.urlShorteningService.service.UrlUtil;

@RestController
public class UrlShorteningController {
	@Autowired
	private UrlService urlService;

	@PostMapping("/generate")
	public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto, HttpServletRequest request) {

		// Validation checks to determine if the supplied URL is valid
		UrlValidator validator = new UrlValidator(new String[] { "http", "https" });
		String url = urlDto.getUrl();
		if (!validator.isValid(url)) {

			InvalidUrlError error = new InvalidUrlError("url", urlDto.getUrl(), "Invalid URL");

			// returns a custom body with error message and bad request status code
			return ResponseEntity.badRequest().body(error);
		}
		Url urlToRet = urlService.generateShortLink(urlDto);

		if (urlToRet != null) {
			UrlResponseDto urlResponseDto = new UrlResponseDto();
			urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());

			String baseUrl = null;

			try {
				baseUrl = UrlUtil.getBaseUrl(request.getRequestURL().toString());
			} catch (MalformedURLException e) {

				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request url is invalid", e);
			}

			urlResponseDto.setShortLink(baseUrl + urlToRet.getShortLink());
			return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
		}

		UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
		urlErrorResponseDto.setStatus("404");
		urlErrorResponseDto.setError("There was an error processing your request. please try again.");
		return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);

	}

	@PostMapping("/getOriginalURL")
	public ResponseEntity<?> getOriginalURL(@RequestBody UrlDto urlDto, HttpServletRequest request) {

		String url = urlDto.getShorturl();

		String baseUrl = null;

		try {
			baseUrl = UrlUtil.getBaseUrl(request.getRequestURL().toString());
		} catch (MalformedURLException e) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request url is invalid", e);
		}

		Url urlToRet = urlService.getEncodedUrl(url.replace(baseUrl, ""));

		if (urlToRet != null) {
			UrlResponseDto urlResponseDto = new UrlResponseDto();
			urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());

			urlResponseDto.setShortLink(baseUrl + urlToRet.getShortLink());
			return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
		}

		InvalidUrlError error = new InvalidUrlError("url", urlDto.getUrl(), "Invalid URL");

		// returns a custom body with error message and bad request status code
		return ResponseEntity.badRequest().body(error);

	}

	@GetMapping("/{shortLink}")
	public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response)
			throws IOException {

		if (StringUtils.isEmpty(shortLink)) {
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setError("Invalid Url");
			urlErrorResponseDto.setStatus("400");
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
		}
		Url urlToRet = urlService.getEncodedUrl(shortLink);

		if (urlToRet == null) {
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setError("Url does not exist or it might have expired!");
			urlErrorResponseDto.setStatus("400");
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
		}

		response.sendRedirect(urlToRet.getOriginalUrl());
		return null;
	}
}
