# URLShortener
A web application for a URL shortening service like bit.ly

The web application is a MVC app which displays the shortened url for a large input url.

On clicking the shortened url that is displayed , the user is redirected to the original large url

This application involves a database and is implemented using a Oracle Database.

Steps for creating the database :

1. Install and set up Oracle Database.
2. Create a new schema URLShortDB
3. Create a new table named "URLDATA"
4. Create columns "url_id", "original_url", "shortened_url", "created_date", "last_accessed_date"
5. Create a sequence for the url_id primary key that starts from 1000000000 till max alloweed value.


# Caching of URLs

This application uses a Thread Safe LRU(Least Recently Used) Cache to cache URL data for performance reasons.

The cache has a configurable time to live (TTL) attribute which signifies the amount of time a particular URL data
is allowed to be present in the cache. It is removed after the TTL expires.

The Caching is thread safe and is implemented using a ConcurrentHashMap.

   
    

