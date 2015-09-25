# Dune 2 - The Maker

[![Build Status](https://travis-ci.org/Fundynamic/dune2themaker4j.svg)](https://travis-ci.org/Fundynamic/dune2themaker4j) [![Coverage Status](https://coveralls.io/repos/Fundynamic/dune2themaker4j/badge.svg?branch=master&service=github)](https://coveralls.io/github/Fundynamic/dune2themaker4j?branch=master) [![Dependency Status](https://www.versioneye.com/user/projects/55f7caf43ed894001e000657/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55f7caf43ed894001e000657) [![Codacy Badge](https://api.codacy.com/project/badge/dea23d73a0e04bffb72cda91ba0ef73c)](https://www.codacy.com/app/stefanhendriks/dune2themaker4j) [![Issue Stats](http://issuestats.com/github/fundynamic/dune2themaker4j/badge/pr?style=flat)](http://issuestats.com/github/fundynamic/dune2themaker4j) [![Issue Stats](http://issuestats.com/github/fundynamic/dune2themaker4j/badge/issue?style=flat)](http://issuestats.com/github/fundynamic/dune2themaker4j)

## Goal
This project aims to deliver a complex `Real Time Simulation Role Playing Strategy Game`. If you want to get a feel of where this project will be going, check out the wiki pages.

## Getting started
This project is set up using [slick2d-maven](https://github.com/nguillaumin/slick2d-maven). 

You need `git` and `maven` installed. Also you need to have `java` version 1.7.

## Running the game
- `git clone` this project
- run `mvn clean package`
- in the `target/d2tm-<version>-SNAPSHOT-release` run `game.sh` (or `game.bat` depending on your OS)

## Running with test coverage report
This project uses [jacoco](https://github.com/jacoco/jacoco) to generate test coverage reports, which are also reported to [coveralls](https://coveralls.io/github/Fundynamic/dune2themaker4j).
 
If you want to generate a local report, just run:

```
mvn clean test jacoco:report
```

Then to view the test report, just open `target/site/jacoco/index.html`

## Progress
For every feature or improvement a small tech demo is created and posted at youtube. These demos are grouped by milestone:

- [Alpha 1 (v0.0.1) Demos @ YouTube](https://www.youtube.com/playlist?list=PLGJc4IZyoBW2_Ue06RVQewDQBF8nkW_dE)
- Alpha 2 (v0.0.2) tbd

## Development
Import the project using your favorite IDE.

If you want to `run` or `debug` the project (you need to execute `Game`)

## Linking to native binaries
If you run into a `java.lang.UnsatifsiedLinkError`, you need to add the following VM Arguments to your run configuration: `-Djava.library.path=target/natives`

## Contributing / Helping out
Not sure how you can help? There are tons of ways:

### Spread the word
The more people know about this project, the more people can help out and send feedback. I'd love to hear feedback about the game.
That could be technical feedback, or about game mechanics, game ideas, etc.

### Code changes
Want to help out coding? Great! By looking at the [milestones]() you get a general idea about priority, or you can just
fix a [bug from the buglist](https://github.com/Fundynamic/dune2themaker4j/issues). Whatever floats your boat.

Fork this project, create Pull Request and submit it.

### Found a bug?
Make sure it is not listed [here](https://github.com/Fundynamic/dune2themaker4j/issues). If not, feel free to create one.


