#!/bin/bash

search_file=$1 # test_data/search_titles/MLA5725.txt

while read title
do
    curl -sS "http://localhost:8090/TesisFullGroovy/basicImplementation/rankSearch?response_format=empty&radio=23&categ=$title"
    #echo -n "."
done < $search_file

echo "FINISH RANK SEARCH FOR FILE: $search_file"