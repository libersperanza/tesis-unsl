#!/bin/bash

pivotStrategy=$1
pivotsQty=$2
radio=$3

echo "Starting grails..."

. scripts/run_grails.sh

sleep 15s

echo "Grails started..."

echo "init index... $pivotStrategy $pivotsQty"

curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/initIndex?initMode=load&pivotStrategy=${pivotStrategy}&pivotsQty=${pivotsQty}"

echo "init index... done"

#warmup
#while read title
#do
#    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/sequentialSearch?flat=Y&radio=$radio&categ=$title"
#    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?flat=Y&radio=$radio&categ=$title"
#    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?flat=Y&radio=2&neighbors=10&categ=$title"
#done < test_data/warmup_search.txt

#echo > test_results/search.log


while read title
do
    res_seq=$(curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/sequentialSearch?flat=Y&radio=$radio&categ=$title")
    res_rank=$(curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?flat=Y&radio=$radio&categ=$title")
    res_knn=$(curl -sS "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?flat=Y&radio=$radio&neighbors=$res_rank&categ=$title")
    echo "$res_seq - $res_rank - $res_knn"
done < test_data/item_titles.small.txt

mv test_results/search.log test_results/search."${pivotStrategy}"_"$pivotsQty".log

. scripts/stop_grails.sh

