package scripts

track "Billie Jean with piano"
tempo 118

instrument "ch" type "closed hi hat"
instrument "sd" type "acoustic snare"
instrument "bd" type "bass drum"
instrument "piano" type "piano"

section "No piano"
beats 4

instr "ch" bar "e e e e e e e e"
instr "sd" bar "_ q _ q"
instr "bd" bar "q _ q _"
instr "piano" bar "_ _ _ _"

section "Main"
beats 4

instr "ch" bar "e e e e e e e e" repeat 8
instr "sd" bar "_ q _ q" repeat 8
instr "bd" bar "q _ q _" repeat 8
instr "piano" bar "_ _ _ _" bar "_ e(C#) e(C#) e(C#) e(B) e(A) e(B)" bar "e(B) q(C#) s(B) s(A) e(B) e(A) e(C#) -"
instr "piano" bar "- e(C#) e(C#) e(C#) e(C#) e(B) e(A) e(B)" bar "e(B) e(A) e(C#) q(B) e(A) e(G#-1) e(F#-1)"
instr "piano" bar "q(F#-1) _ _ s(F#) s(G#) e(F#)" bar "e(F#) - s(F#) s(G#) e(F#) e(F#) - s(F#) s(G#) e(F#)" bar "e(F#) - _ _ _"

section "Transition"
beats 4

instr "ch" bar "e e e e e e e e" repeat 4
instr "sd" bar "_ q _ q" repeat 4
instr "bd" bar "q _ q _" repeat 4
instr "piano" bar "- e(F#-1) e(A) q(B) e(A) e(G#-1) e(F#-1)" bar "q(F#-1) _ _ s(F#) s(G#) e(F#)"
instr "piano" bar "e(F#) - s(F#) s(G#) e(F#) e(F#) - s(F#) s(G#) e(F#)" bar "q(F#) _ _ _"


play "No piano", "Main", "Transition", "Main"
