package com.apple.urlShorteningService.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apple.urlShorteningService.model.Url;
import com.apple.urlShorteningService.model.UrlDto;
import com.apple.urlShorteningService.repository.UrlRepository;
import com.google.common.hash.Hashing;

@Component
public class UrlServiceImpl implements UrlService {

	private static final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);
	@Autowired
	private UrlRepository urlRepository;

	@Override
	public Url generateShortLink(UrlDto urlDto) {

		if (StringUtils.isNotEmpty(urlDto.getUrl())) {

			Url urlToRet = urlRepository.findByoriginalUrl(urlDto.getUrl());

			if (urlToRet != null && StringUtils.isNotEmpty(urlToRet.getShortLink())) {
				return urlToRet;
			} else {
				String encodedUrl = encodeUrl(urlDto.getUrl());
				Url urlToPersist = new Url();
				urlToPersist.setCreationDate(LocalDateTime.now());
				urlToPersist.setOriginalUrl(urlDto.getUrl());
				urlToPersist.setShortLink(encodedUrl);

				urlToRet = persistShortLink(urlToPersist);

				if (urlToRet != null)
					return urlToRet;

				return null;

			}

		}
		return null;
	}

	private String encodeUrl(String url) {
		String encodedUrl = "";
		LocalDateTime time = LocalDateTime.now();
		encodedUrl = Hashing.murmur3_32().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
		return encodedUrl;
	}

	@Override
	public Url persistShortLink(Url url) {
		Url urlToRet = urlRepository.save(url);
		return urlToRet;
	}

	@Override
	public Url getEncodedUrl(String url) {
		Url urlToRet = urlRepository.findByShortLink(url);
		return urlToRet;
	}

	@Override
	public void deleteShortLink(Url url) {
		// TODO Auto-generated method stub
		
	}

}
