#!/bin/bash
pivotStrategy=$1 # random_differentPivotes
pivotsQty=$2 # 4
search=$3 # knn|rank
prefix=$4 # MLA1000 (para correr una categ en particular o un grupo de archivos en base al prefijo)
. scripts/run_grails.sh

. scripts/init_index.sh $pivotStrategy $pivotsQty

echo > test_results/search.log

files=$(ls test_data/search_titles/${prefix}*)


for file in $files
do
	. scripts/run_"$search".sh $pivotStrategy $pivotsQty $file >/dev/null 2>&1 &
	#echo -n "."
done

# Espero a que termine el proceso
wait

mv test_results/search.log test_results/search."$search"."$pivotStrategy"_"$pivotsQty".log

. scripts/stop_grails.sh