#!/bin/bash

pivotStrategy=$1 # random_differentPivotes
pivotsQty=$2 # 4
search_file=$3 # test_data/search_titles/MLA5725.txt

while read title
do
    curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?response_format=empty&radio=5&neighbors=5&categ=$title"
    curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?response_format=empty&radio=5&neighbors=6&categ=$title"
    curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?response_format=empty&radio=5&neighbors=7&categ=$title"
    #echo -n "."
done < $search_file