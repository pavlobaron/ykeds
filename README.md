# ykeds

`ykeds` stands for "Your Kid, Every Day (a little) Smarter".

## Why ykeds?

With this piece of software I'm trying to solve following issues in daily education of my own kids:

- The web is a huge source of information, most of which is totally useless, especially for kids
- Search engines marked as "for kids" still show tons of web pages and leave it to the kid to filter what's relevant and what's not
- Search engines in generall are passive tools. Even adults can be easily overwhelmed with the task of finding relevant information through the lense of a search engine
- Random search exists, but it's really random. There is no way I'm aware of how to control the search randomness based on some topics
- Kids easily end up bored in front of the computer as long as it's not a game (or programming, depending on the kid)
- Kids know very well what they're currently interested in, but it's still necessary to point them at new topics so they interests constantly grow
- I don't always have time to introduce new topics to my kids, so I need a sort of bot taking over for me, at least for routine or initial educational teasers
- I don't want to bore my kids with dry daily education beside the school, but I want that they learn something new every day, since my trust in german school education is limited
- I couldn't find anything comparable, though it's probably available, but since I can build it myself and let my kids test it, it's not a real issue. Actually, it's fun

## How does ykeds work?

So, ykeds is this bot I've mentioned. You configure it once (and update your configuration later as you go) with topics of interest and web sources, and from there it starts working for you, or better for your kid(s).

It uses web sources you've configured to find information and news relevant to the topics you've configured. It creates a very short list of readings for your kid with some teaser text and follow-up links. It keeps this list for the time interval you configure, with the default of once per 6h. Your kid only needs to use their browser to access the list, and follow the link if the topic is interesting. One list per reasonably long, configurable period of time, gone and refreshed after this period. With the maximum of time to spend of about 30-40 minutes per day, that can be spread all over the day.

You will have a simple log telling you what articles have been viewed and what not. Basically, it's for your tuning of topics and follow-up discussions with your kid, not necessarily for control.

It doesn't solve the problem that you need to protect your kids from accessing parts of the internet they shouldn't access. But this task is independent and is totally up to you, from the technical as well as educational perspective. What web sources you configure in ykeds, is also up to you.


## Prerequisites

This software runs on the JVM, so you need a machine, ideally a private one, with an OS where JVM can run on. You need to open one port on this machine for you kid's browser to access it through HTTP. Further, this machine needs to access the web source you've configured. Your kid needs a browser and ideally a different machine. The machine ykeds is running on should continuously run or be booted every day, so the reading list can be updated on the daily basis.

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

All the rest, basically libraries, will be pulled from Clojars on demand.

## Configuration / default configuration

In `etc` you can find the file `ykeds.clj.default` and rename it to `<whatever_you_use_as_lein_run_parameter>.clj`. Configuration is done through Clojure code that gets evaluated at application startup. Configuration keys should explain themselves by name.

The default configuration should be sufficient to get you going in Bavaria / Germany. I use the bavarian school plan for the 4th grade as collection of topics, currently picking random trigrams every day and searching through some kids' search engines for relevant information, randomly picking links to follow.

Stopwords and topics are all German now, which is perfectly ok for me. From what I can see now, it should work for any setup in any region or country, but of course I can be wrong and never tested it, so feedback is welcome.

## Running

To start a web server for the application, run:

    lein run -c etc/ykeds.clj


## Why Clojure?

I originally wanted to write this in Python, and the initial version would have taken maybe an hour or two. Now I wrote it in Clojure, just to learn Clojure for future work. There is no other reason, really.

## Future work / TODOs

Of course, it's work in progress. I didn't yet implement several parts I want to be included:

* currently, there is no own semantics in picking topics. I rely on search engines to decide what to read based on a random trigram. Ideally, own crawlers should do the job gathering information, applying some ML/NLP algorithms to make decisions on what's relevant. Since search engines I'm using are carefuly operated and target pages are being manually selected to really be kid safe, t's fair enough for now
* I need to invest more into collecting and showing news. I'm using a school curriculum to pick relevant topics, which is good as a general source, but probably not good to catch the news. I totally rely on the search engines to show news on the very first hit list page, but since I randomy pick links to follow, I still might exclude news
* The UI is currently no UI - it's good old plain HTML. As agreed with my kids, they're cool about it, but probably not yours if you want to use it. I need to invest some brain into design, but surely will fail not being a designer. Any ideas are welcome
* Everything relies on the quality of topic texts. I need to toy with the idea how to make topic explicitly configurable
* There are several bugs and spots where the quality has to improve, for example teaser. I need to play more with extracting a teaser from the body of the target page. Currently, I'm simply looking for some text of length between 64 and 256 characters. Chances are that it's even a piece of JavaScript code. This part is very buggy yet. Also, there are bugs in collecting the random recommended links, such as duplicates. Search engines I'm using can return same target links, and can have multiple links to the same destination as well. Also, my own implementaiton doesn't yet exclude duplicates
* There is no logging yet I've mentioned earlier, but I don't see it as the highest priority yet

## Credit

Stop words were taken from [http://code.google.com/p/stop-words/](http://code.google.com/p/stop-words/) (under GPL v3).

Special credit to @ifesdjeen for quickly getting me going in Clojure providing a powerful development environment and showing me some tricks.

## License

Copyright © 2013 Pavlo Baron. Source code licenced under GPL v3. GPL cannot be applied to some of the used libraries, so these are licensed through their corresponding licenses. I hope they're compatible in some way, at least for this work, but one can never know.
