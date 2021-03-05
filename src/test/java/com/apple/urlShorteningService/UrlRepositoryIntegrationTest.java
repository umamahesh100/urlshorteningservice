package com.apple.urlShorteningService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.apple.urlShorteningService.model.Url;
import com.apple.urlShorteningService.repository.UrlRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlRepositoryIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UrlRepository urlRepository;

	@SuppressWarnings("deprecation")
	@Test
	public void testInsertAndFindSuccess() {

		Url urlToPersist = new Url();
		urlToPersist.setCreationDate(LocalDateTime.now());
		urlToPersist.setOriginalUrl("https://www.apple.com/");
		urlToPersist.setShortLink("https://www.apple.com/");

		urlToPersist = urlRepository.save(urlToPersist);

		assertThat(urlToPersist.getId(), notNullValue());

		Url urlEntityFromDb = urlRepository.findByShortLink(urlToPersist.getShortLink());
		assertThat(urlEntityFromDb.getId(), equalTo(urlToPersist.getId()));

		urlEntityFromDb = urlRepository.findByoriginalUrl(urlToPersist.getOriginalUrl());
		assertThat(urlEntityFromDb.getId(), equalTo(urlToPersist.getId()));
	}

}