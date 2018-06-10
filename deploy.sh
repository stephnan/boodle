#!/bin/bash

git pull
cd resources/src
yarn
gulp
cd ../..
/home/manuel/bin/lein cljsbuild once min
/home/manuel/bin/lein run
