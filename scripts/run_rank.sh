#!/bin/bash

pivotStrategy=$1 # random_differentPivotes
pivotsQty=$2 # 4
categ=$3 # MLA3025

while read title
do
    curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?response_format=empty&radio=23&categ=$title"
    #echo -n "."
done < test_data/search_titles/"$categ"_search_titles.txt