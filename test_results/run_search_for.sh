pivotStrategy=$1
pivotsQty=$2
radio=$3
neigh=$4


echo "init ... $pivotStrategy $pivotsQty"

curl "http://localhost:8080/TesisFullGroovy/basicImplementation/initIndex?initMode=load&pivotStrategy=${pivotStrategy}&pivotsQty=${pivotsQty}"

echo ".. done"


while read title
do
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/sequentialSearch?radio=$radio&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/rankSearch?radio=$radio&neighbors=$neigh&categ=$title"
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnSearch?radio=$radio&neighbors=$neigh&categ=$title"
done < item_titles.txt
