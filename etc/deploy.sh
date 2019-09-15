#!/bin/bash

cd /home/manuel/boodle
git pull
yarn
cd resources/src
yarn
gulp
cd ../..
npx shadow-cljs release boodle
clj -A:run
