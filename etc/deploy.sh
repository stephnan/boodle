#!/bin/bash

cd /home/manuel/boodle
git pull
yarn
cd resources/src
yarn
gulp
cd ../..
shadow-cljs release boodle
clj -A:run
