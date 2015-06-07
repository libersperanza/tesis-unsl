pivotStrategy=$1
pivotsQty=$2
radio=$3
neigh=$4


echo "init ... $pivotStrategy $pivotsQty"

curl "http://localhost:8080/TesisFullGroovy/basicImplementation/initIndex?initMode=load&pivotStrategy=${pivotStrategy}&pivotsQty=${pivotsQty}"

echo ".. done"

#warmup
while read title
do
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/sequentialSearch?flat=Y&radio=$radio&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?flat=Y&radio=$radio&neighbors=$neigh&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?flat=Y&radio=2&neighbors=$neigh&categ=$title"
done < warmup_search.txt

echo > search.log


while read title
do
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/sequentialSearch?flat=Y&radio=$radio&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?flat=Y&radio=$radio&neighbors=$neigh&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?flat=Y&radio=2&neighbors=$neigh&categ=$title"
done < item_titles.txt

mv search.log search."${pivotStrategy}"_"$pivotsQty".log