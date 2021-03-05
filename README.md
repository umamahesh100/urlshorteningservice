The URL Shortener service accepts a valid http or https URL and shortens the URL in the following format http://localhost:9090/{random_short_string} If we select the same shorted URL, it will return the actual full URL and populate the URL in a new browser tab.

The full URL and shorted URLs are persisted, so we will get the same URL’s .

Tech stack used

Spring boot, HTML, JavaScript, AJAX.

**Getting Started **

Clone the application in your local machine

run mvn clean install

run the following class UrlShorteningServiceApplication.java

The Spring boot application will start. Access the browser with the following URL http://localhost:9090/ and you will be prompted with two text boxes: 1) with Generate Short URL and 2) Get Original URL.

Enter the URL that you want to shorten (for example: http://www.apple.com/iphone/) in the Full URL text field, and click on generate short URL . You will receive success alert and then click OK . You will receive the shorted URL in Short URL text field. If you click on the Get Original URL button, you will receive the original URL and the original URL will be opened in new browser tab .

I used H2 DB for this example, so we don't need to explicitly setup the database.

It will only accept the http or https URLs . Another URL will throw an exception.

There are different encryption techniques for shorting the URL. I used Murmer Hashing, as I believe the Hashing based search pattern is faster. As the traffic increases, the Web Crawlers will quickly find the URLs based on the hashing technique used.
