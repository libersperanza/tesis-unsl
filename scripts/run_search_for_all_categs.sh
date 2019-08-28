#!/bin/bash
pivotStrategy=$1 # random_differentPivotes
pivotsQty=$2 # 4
search=$3 # knn|rank

. scripts/run_grails.sh

. scripts/init_index.sh $pivotStrategy $pivotsQty

echo > test_results/search.log

#categs=$(awk -F\; '{print$1}' test_data/categs.csv)
categs="MLA1574 MLA1430 MLA1168 MLA1132 MLA1648 MLA407134 MLA1276 MLA1000 MLA1051 MLA1246 MLA1499 MLA3937 MLA1367 MLA1368 MLA5726 MLA1182 MLA1798 MLA1144 MLA1459 MLA1384 MLA1953 MLA1071 MLA1039 MLA1403 MLA409431 MLA1743 MLA1540 MLA2547"

for categ in $categs
do
	. scripts/run_"$search".sh $pivotStrategy $pivotsQty $categ >/dev/null 2>&1 &
done

# Espero a que termine el proceso
wait

mv test_results/search.log test_results/search."$search"."$pivotStrategy"_"$pivotsQty".log

. scripts/stop_grails.sh