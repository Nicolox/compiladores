package co.edu.uniquindio.compiladores.proyecto

import co.edu.uniquindio.compiladores.proyecto.lexico.AnalizadorLexico

fun main() {

    val lexico = AnalizadorLexico("2345.4534 546345 #casa# 123")
    lexico.analizar()
    print(lexico.listaTokens)
}
    fun esPrimo(numero: Int): Boolean {
        for (i in 2..numero / 2) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true
    }

    fun esPrimo(numero: Int, i: Int): Boolean {
        return if (numero / 2 < i) {
            true
        } else if (numero % i == 0) {
            false
        } else {
            esPrimo(numero, i + 1)
        }

    }

    fun operar(a: Int, b: Int, fn: (Int, Int) -> Int): Int {
        return fn(a, b)
    }

    fun sumar(a: Int, b: Int) = a + b
