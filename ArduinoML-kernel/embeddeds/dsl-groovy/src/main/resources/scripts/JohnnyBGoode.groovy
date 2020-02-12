package scripts

track "Johnny B Goode"

instrument "rc" type "ride cymbal"
instrument "sd" type "snare drum"
instrument "bd" type "bass drum"

section "Main"
tempo 170
beats 4

instr "rc" bar "_ _ _ _" bar "_ _ _ _" bar "e e e e e e e e" bar "e e e e e e e e"
instr "sd" bar "q _ _ _" bar "_ _ _ q" bar "_ q _ q" bar "_ q _ q"
instr "bd" bar "_ _ _ _" bar "_ _ _ _" bar "q _ q _" bar "q _ q _"

play "Main"
