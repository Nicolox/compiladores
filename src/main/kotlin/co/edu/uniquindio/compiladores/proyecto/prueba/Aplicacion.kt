package co.edu.uniquindio.compiladores.proyecto

import co.edu.uniquindio.compiladores.proyecto.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.proyecto.sintaxis.AnalizadorSintactico

fun main() {

    val lexico = AnalizadorLexico("met $$ (){}")
    lexico.analizar()
    print(lexico.listaTokens)

    val sintaxis=AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esFuncion())
}
//    fun esPrimo(numero: Int): Boolean {
//        for (i in 2..numero / 2) {
//            if (numero % i == 0) {
//                return false;
//            }
//        }
//        return true
//    }
//
//    fun esPrimo(numero: Int, i: Int): Boolean {
//        return if (numero / 2 < i) {
//            true
//        } else if (numero % i == 0) {
//            false
//        } else {
//            esPrimo(numero, i + 1)
//        }
//
//    }
//
//    fun operar(a: Int, b: Int, fn: (Int, Int) -> Int): Int {
//        return fn(a, b)
//    }
//
//    fun sumar(a: Int, b: Int) = a + b
