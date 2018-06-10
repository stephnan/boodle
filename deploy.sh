#!/bin/bash

git pull
cd resources/src
yarn
gulp
cd ../..
lein cljsbuild once min
lein run
