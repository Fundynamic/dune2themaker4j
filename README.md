# Dune 2 - The Maker [![Build Status](https://travis-ci.org/stefanhendriks/dune2themaker4j.svg?branch=feature%2Ftravis-ci-integration)](https://travis-ci.org/stefanhendriks/dune2themaker4j)

This project aims to deliver a complex `Real Time Simulation Role Playing Strategy Game`. If you want to get a feel of where this project will be going, check out the wiki pages.

## Getting started
This project is set up using [slick2d-maven](https://github.com/nguillaumin/slick2d-maven). This means you need to
have `java` and `git` and `maven` installed.

## Running the game
- `git clone` this project
- run `mvn clean package`
- in the `target/d2tm-<version>-SNAPSHOT-release` run `game.sh` (or `game.bat` depending on your OS)

## Development
Import the project using your favorite IDE.

If you want to `run` or `debug` the project (you need to execute `Game`), then you probably run into a `java.lang.UnsatifsiedLinkError`.

To fix that you need to add the following VM Arguments to your run configuration: `-Djava.library.path=target/natives`

## Contributing
Fork and create a pull request. Please mention issue nr if applicable.
