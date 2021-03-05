# Getting Started


**Description** : 

URL Shortner service accepts a valid http or https URL and shortens the URL in the following format 
http://localhost:9090/{random_short_string}
If we select the same shorted url , it will return the Actual full url and populate the url website in a new tab .

The Full url and Shorted urls are persisted , So we will get the same url's .

**Tech stack used** 

Spring boot ,
HTML ,
javascript  ,
ajax .

**Getting Started **

Clone the application in your local machine 

run mvn clean install

run the following class  UrlShorteningServiceApplication.java

The Spring boot application will start .
hit the browser with the following url    http://localhost:9090/
You will be prompted with two text boxed with Generate Short url and Get Original Url .

Enter the url that you want to shorten (for ex : http://www.apple.com/iphone/) in the Full URL text field , click on generate short url . You will receive success alert and click OK . You will receive the shorted url in Short url text field .
If you click on Get Original url button , you will receive the original URL and the original url will be opened in new tab .

I used H2 DB for this example , so we don't need to explicitly setup the db .

It will only accept the http , https urls . Otherwise it will throw exception .

There are different encryption techniques for shorting the url . I used Murmer Hashing , as I believe the Hashing based search pattern is faster .
As the traffic goes on increasing the Web Crawlers will be quickly finding the urls if they are based on hashing .
