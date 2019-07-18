#!/bin/bash

pivotStrategy=$1
pivotsQty=$2
radio=$3

echo "starting grails..."

. scripts/run_grails.sh

sleep 15s

echo "grails started..."

echo "init index... $pivotStrategy $pivotsQty"

curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/initIndex?initMode=load&pivotStrategy=${pivotStrategy}&pivotsQty=${pivotsQty}"

echo "init index... done"

echo "begin warmup..."
while read title
do
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/sequentialSearch?flat=Y&radio=$radio&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?flat=Y&radio=$radio&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?flat=Y&radio=2&neighbors=10&categ=$title"
done < test_data/warmup_search.txt

echo > test_results/search.log

echo "warmup ended..."

while read title
do
    res_seq=$(curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/sequentialSearch?flat=Y&radio=$radio&categ=$title")
    res_rank=$(curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?flat=Y&radio=$radio&categ=$title")
    res_knn=$(curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?flat=Y&radio=$radio&neighbors=$res_rank&categ=$title")
    if [ $res_seq -ne $res_rank ]
     then
     echo "[ERROR][search:$title][results_seq:$res_seq][results_rank:$res_rank]"
	fi
done < test_data/item_titles.txt

mv test_results/search.log test_results/search."${pivotStrategy}"_"$pivotsQty".log

echo "stopping grails..."

. scripts/stop_grails.sh

echo "grails stopped..."