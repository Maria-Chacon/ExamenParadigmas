% Predicado para calcular la cantidad de dígitos en un número
num_digitos(0, 0).
num_digitos(Numero, CantidadDigitos) :-
    Numero > 0,
    Cociente is Numero // 10,
    num_digitos(Cociente, CantidadDigitosAnterior),
    CantidadDigitos is CantidadDigitosAnterior + 1.

% Predicado para calcular la potencia de un número
elevar(_, 0, 1).
elevar(Base, Exponente, Resultado) :-
    Exponente > 0,
    Exponente1 is Exponente - 1,
    elevar(Base, Exponente1, Resultado1),
    Resultado is Base * Resultado1.

% Predicado para verificar si un número es un número de Armstrong
es_armstrong_aux(0, _, Suma, Suma).
es_armstrong_aux(Numero, NumDigitos, SumaParcial, Resultado) :-
    Digito is Numero mod 10,
    elevar(Digito, NumDigitos, DigitoElevado),
    Cociente is Numero // 10,
    SumaParcial1 is SumaParcial + DigitoElevado,
    (Cociente = 0 -> SumaParcialFinal = SumaParcial1; es_armstrong_aux(Cociente, NumDigitos, SumaParcial1, SumaParcialFinal)),
    Resultado is SumaParcialFinal.

es_armstrong(Numero) :-
    num_digitos(Numero, NumDigitos),
    es_armstrong_aux(Numero, NumDigitos, 0, Numero).
