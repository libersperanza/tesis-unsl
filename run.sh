title1="Libro%20Te%20Amo%20Pero%20Soy%20Feliz%20Sin%20Ti.%20Jaime%20Jaramillo"
title2="El%20Secreto%20Rhonda%20Byrne%20Lvbp13"
title3="Libro%20De%20Italiano%20Forza%202"
title4="Libro%20La%20Magia%20Rhonda%20Byrne%20El%20Secreto%20Lvbp13"

# incremental_samePivotes - 4
# load data
#pivotStrategy="incremental_differentPivotes"
#pivotsQty="128"

pivotStrategy=$1
pivotsQty=$2

echo "init ... $pivotStrategy $pivotsQty"

curl "http://localhost:8080/TesisFullGroovy/basicImplementation/initIndex?initMode=load&pivotStrategy=${pivotStrategy}&pivotsQty=${pivotsQty}"

echo ".. done"

LISTA="$title1 $title2 $title3 $title4"

for var in $LISTA 
do 
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?method=knnRankSearch&categ=1227Libros_Otros&neighbors=32&radio=2&itemTitle=$var"
	curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?method=knnRankSearch&categ=1227Libros_Otros&neighbors=316&radio=2&itemTitle=$var"
	curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnRankSearch?method=knnRankSearch&categ=1227Libros_Otros&neighbors=3163&radio=2&itemTitle=$var"
done

