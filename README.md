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

## Progress
For every feature or improvement a small tech demo is created and posted at youtube. These demos are grouped by milestone:

- [Alpha 1 Demos @ YouTube](https://www.youtube.com/playlist?list=PLGJc4IZyoBW2_Ue06RVQewDQBF8nkW_dE)

## Development
Import the project using your favorite IDE.

If you want to `run` or `debug` the project (you need to execute `Game`), then you probably run into a `java.lang.UnsatifsiedLinkError`.

To fix that you need to add the following VM Arguments to your run configuration: `-Djava.library.path=target/natives`

## Contributing
Fork and create a pull request. Please mention issue nr if applicable.
