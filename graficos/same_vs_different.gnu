set terminal postscript  eps color "times" 20
set xlabel "Tamaño de base de datos"
set ylabel "Ratio de comparaciones"
set key outside right top
set yrange [0:1]

set title "Selección incremental - 16 Pivotes"
set xrange [1034:15964]
set output "same_vs_different/16p.incremental.ps"


plot "same_vs_different/16p.incremental.tsv" using ($1):($2) title "different" w lp,\
"same_vs_different/16p.incremental.tsv" using ($1):($3) title "same" w lp


set title "Selección random - 16 Pivotes"
set xrange [1034:15964]
set output "same_vs_different/16p.random.ps"


plot "same_vs_different/16p.random.tsv" using ($1):($2) title "different" w lp,\
"same_vs_different/16p.random.tsv" using ($1):($3) title "same" w lp

set title "Selección incremental - 64 Pivotes"
set xrange [1034:46530]
set output "same_vs_different/64p.incremental.ps"


plot "same_vs_different/64p.incremental.tsv" using ($1):($2) title "differentpivots" w lp,\
"same_vs_different/64p.incremental.tsv" using ($1):($3) title "samepivots" w lp


set title "Selección random - 64 Pivotes"
set xrange [1034:46530]
set output "same_vs_different/64p.random.ps"


plot "same_vs_different/64p.random.tsv" using ($1):($2) title "differentpivots" w lp,\
"same_vs_different/64p.random.tsv" using ($1):($3) title "samepivots" w lp

