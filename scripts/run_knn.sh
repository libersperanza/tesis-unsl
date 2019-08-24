#!/bin/bash

pivotStrategy=$1 # random_differentPivotes
pivotsQty=$2 # 4
categ=$3 # MLA3025

while read title
do
    curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?response_format=empty&radio=5&neighbors=5&categ=$title"
    curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?response_format=empty&radio=5&neighbors=6&categ=$title"
    curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?response_format=empty&radio=5&neighbors=7&categ=$title"
    #echo -n "."
done < test_data/search_titles/1pct/"$categ"_search_titles.txt