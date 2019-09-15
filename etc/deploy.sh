#!/bin/bash

cd /home/manuel/boodle
git pull
yarn
cd resources/src
gulp
cd ../..
npx shadow-cljs compile boodle
clj -A:run
