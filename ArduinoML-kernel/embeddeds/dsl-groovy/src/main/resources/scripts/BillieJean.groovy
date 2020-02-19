package scripts

track "Billie Jean"

instrument "ch" type "closed hithat"
instrument "sd" type "snare drum"
instrument "bd" type "bass drum"

section "Main"
tempo 118
beats 4

instr "ch" bar "e e e e e e e e" bar "e e e e e e e e"
instr "sd" bar "_ q _ q" bar "_ q _ q"
instr "bd" bar "q _ q _" bar "q _ q _"

play "Main"
export "Billie Jean"
