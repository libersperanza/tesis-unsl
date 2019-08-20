#!/bin/bash
pivotStrategy=$1 # random_differentPivotes
pivotsQty=$2 # 4
search=$3 # knn|rank

. scripts/run_grails.sh

. scripts/init_index.sh $pivotStrategy $pivotsQty

echo > test_results/search.log

categs=$(awk -F\; '{print$1}' test_data/categs.csv)

for categ in $categs
do
	. scripts/run_"$search".sh $pivotStrategy $pivotsQty $categ >/dev/null 2>&1 &
done

# Espero a que termine el proceso
wait

mv test_results/search.log test_results/search."$search"."$pivotStrategy"_"$pivotsQty".log

. scripts/stop_grails.sh