# TwitScape
A tool to collect and index tweets to allow for smoother searching/filtering processes 

# Phase 1 - Geotagged Tweet Collector
Collected 2GB of geotagged tweets using Twitter Streaming API. If tweet contains external links, title of the page is collected and added as an additional field of the tweet (that is, it is included in the JSON of the tweet, so it becomes searchable).

# Phase 2 - Indexing data and Web-based search interface
Java program parses the JSON objects of collected tweet file from Phase 1 and inserts them into Apache Lucene. Handles fields like username, location, and so on. The interface contains a textbox, and a search button. When you click search, a list of results appears (e.g., first 10) returned by Lucene for this query and their scores. The list is ordered in decreasing order of score. 

