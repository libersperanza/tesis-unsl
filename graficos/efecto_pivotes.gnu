set terminal postscript  eps color "times" 20
set xlabel "Cantidad de Pivotes"
set ylabel "Ratio de comparaciones"
set key outside right top
set yrange [0:1]

set title "Selección incremental - Grupo 1"
set xrange [16:256]
set output "efecto_pivotes/g1_incremental_ep.eps"


plot "efecto_pivotes/g1_incremental_ep.tsv" using ($1):($2) title "1034" w lp,\
"efecto_pivotes/g1_incremental_ep.tsv" using ($1):($3) title "2943" w lp,\
"efecto_pivotes/g1_incremental_ep.tsv" using ($1):($4) title "12354" w lp,\
"efecto_pivotes/g1_incremental_ep.tsv" using ($1):($5) title "13093" w lp,\
"efecto_pivotes/g1_incremental_ep.tsv" using ($1):($6) title "15796" w lp,\
"efecto_pivotes/g1_incremental_ep.tsv" using ($1):($7) title "15964" w lp

set title "Selección incremental - Grupo 2"
set xrange [64:1024]
set output "efecto_pivotes/g2_incremental_ep.eps"

plot "efecto_pivotes/g2_incremental_ep.tsv" using ($1):($2) title "19032" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($3) title "22421" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($4) title "26903" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($5) title "27788" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($6) title "29426" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($7) title "29475" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($8) title "30420" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($9) title "34603" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($10) title "36280" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($11) title "36957" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($12) title "39612" w lp,\
"efecto_pivotes/g2_incremental_ep.tsv" using ($1):($13) title "46530" w lp

set title "Selección incremental - Grupo 3"
set xrange [256:2048]
set output "efecto_pivotes/g3_incremental_ep.eps"

plot "efecto_pivotes/g3_incremental_ep.tsv" using ($1):($2) title "57198" w lp,\
"efecto_pivotes/g3_incremental_ep.tsv" using ($1):($3) title "77264" w lp,\
"efecto_pivotes/g3_incremental_ep.tsv" using ($1):($4) title "77720" w lp,\
"efecto_pivotes/g3_incremental_ep.tsv" using ($1):($5) title "86447" w lp,\
"efecto_pivotes/g3_incremental_ep.tsv" using ($1):($6) title "87108" w lp,\
"efecto_pivotes/g3_incremental_ep.tsv" using ($1):($7) title "93014" w lp,\
"efecto_pivotes/g3_incremental_ep.tsv" using ($1):($8) title "106511" w lp,\
"efecto_pivotes/g3_incremental_ep.tsv" using ($1):($9) title "136323" w lp

set title "Selección incremental - Grupo 4"
set xrange [512:4096]
set output "efecto_pivotes/g4_incremental_ep.eps"

plot "efecto_pivotes/g4_incremental_ep.tsv" using ($1):($2) title "167995" w lp,\
"efecto_pivotes/g4_incremental_ep.tsv" using ($1):($3) title "194687" w lp,\
"efecto_pivotes/g4_incremental_ep.tsv" using ($1):($4) title "200375" w lp,\
"efecto_pivotes/g4_incremental_ep.tsv" using ($1):($5) title "213578" w lp

set title "Selección random - Grupo 1"
set xrange [16:256]
set output "efecto_pivotes/g1_random_ep.eps"


plot "efecto_pivotes/g1_random_ep.tsv" using ($1):($2) title "1034" w lp,\
"efecto_pivotes/g1_random_ep.tsv" using ($1):($3) title "2943" w lp,\
"efecto_pivotes/g1_random_ep.tsv" using ($1):($4) title "12354" w lp,\
"efecto_pivotes/g1_random_ep.tsv" using ($1):($5) title "13093" w lp,\
"efecto_pivotes/g1_random_ep.tsv" using ($1):($6) title "15796" w lp,\
"efecto_pivotes/g1_random_ep.tsv" using ($1):($7) title "15964" w lp

set title "Selección random - Grupo 2"
set xrange [64:1024]
set output "efecto_pivotes/g2_random_ep.eps"

plot "efecto_pivotes/g2_random_ep.tsv" using ($1):($2) title "19032" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($3) title "22421" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($4) title "26903" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($5) title "27788" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($6) title "29426" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($7) title "29475" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($8) title "30420" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($9) title "34603" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($10) title "36280" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($11) title "36957" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($12) title "39612" w lp,\
"efecto_pivotes/g2_random_ep.tsv" using ($1):($13) title "46530" w lp

set title "Selección random - Grupo 3"
set xrange [256:2048]
set output "efecto_pivotes/g3_random_ep.eps"

plot "efecto_pivotes/g3_random_ep.tsv" using ($1):($2) title "57198" w lp,\
"efecto_pivotes/g3_random_ep.tsv" using ($1):($3) title "77264" w lp,\
"efecto_pivotes/g3_random_ep.tsv" using ($1):($4) title "77720" w lp,\
"efecto_pivotes/g3_random_ep.tsv" using ($1):($5) title "86447" w lp,\
"efecto_pivotes/g3_random_ep.tsv" using ($1):($6) title "87108" w lp,\
"efecto_pivotes/g3_random_ep.tsv" using ($1):($7) title "93014" w lp,\
"efecto_pivotes/g3_random_ep.tsv" using ($1):($8) title "106511" w lp,\
"efecto_pivotes/g3_random_ep.tsv" using ($1):($9) title "136323" w lp

set title "Selección random - Grupo 4"
set xrange [512:4096]
set output "efecto_pivotes/g4_random_ep.eps"

plot "efecto_pivotes/g4_random_ep.tsv" using ($1):($2) title "167995" w lp,\
"efecto_pivotes/g4_random_ep.tsv" using ($1):($3) title "194687" w lp,\
"efecto_pivotes/g4_random_ep.tsv" using ($1):($4) title "200375" w lp,\
"efecto_pivotes/g4_random_ep.tsv" using ($1):($5) title "213578" w lp
