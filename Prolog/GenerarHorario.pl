profesor(juan, matematicas).
profesor(maria, sociales).
profesor(luis, quimica).
profesor(viviana, espannol).
profesor(laura, ingles).
profesor(ligia, biologia).

asignatura(matematicas, 1).
asignatura(sociales, 2).
asignatura(quimica, 3).
asignatura(espannol, 4).
asignatura(ingles, 5).
asignatura(biologia, 6).

aula(101).
aula(102).
aula(103).
aula(104).
aula(105).
aula(106).

dia(lunes).
dia(martes).
dia(miercoles).
dia(jueves).
dia(viernes).
dia(sabado).

horario(1, "08:00-09:30").
horario(2, "09:45-11:15").
horario(3, "11:30-13:00").
horario(4, "13:10-14:40").
horario(5, "15:00-16:30").
horario(6, "16:45-18:15").

preferencia(juan, lunes, 1).
preferencia(maria, martes, 2).
preferencia(luis, miercoles, 3).
preferencia(viviana, jueves, 4).
preferencia(laura, viernes, 5).
preferencia(ligia, sabado, 6).

horario_aula(1, 101).
horario_aula(2, 102).
horario_aula(3, 103).
horario_aula(4, 104).
horario_aula(5, 105).
horario_aula(6, 106).

% Reglas
asignar_profesor_con_preferencia(Profesor, Asignatura, Dia, Horario, Aula) :-
    profesor(Profesor, Asignatura),
    preferencia(Profesor, Dia, Horario),
    horario_aula(Horario, Aula).

horario_asignatura(Asignatura, Dia, Horario) :-
    asignar_profesor_con_preferencia(Profesor, Asignatura, Dia, Horario, Aula),
    write('Asignatura: '), write(Asignatura), nl,
    write('Profesor: '), write(Profesor), nl,
    write('Dia: '), write(Dia), nl,
    write('Horario: '), write(Horario), nl,
    write('Aula: '), write(Aula), nl.

generar_horario_completo :-
    asignatura(Asignatura, _),
    dia(Dia),
    horario(Horario, _),
    horario_asignatura(Asignatura, Dia, Horario),
    nl,
    fail.
    
generar_horario_completo.
