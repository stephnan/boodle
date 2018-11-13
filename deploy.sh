#!/bin/bash

git pull
cd resources/src
yarn
gulp
cd ../..
shadow-cljs compile boodle
clj -A:run
