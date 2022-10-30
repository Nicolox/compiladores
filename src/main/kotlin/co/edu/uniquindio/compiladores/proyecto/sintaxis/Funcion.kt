package co.edu.uniquindio.compiladores.proyecto.sintaxis

import co.edu.uniquindio.compiladores.proyecto.lexico.Token

class Funcion (var nombreFuncion: Token, var listaSentencia: ArrayList<Sentencia>, var listaArgumentos:
    ArrayList<Argumento>?): Sentencia()
{
    override fun toString(): String
    {
        return "Funcion(nombreFuncion=$nombreFuncion, listaSentencia=$listaSentencia, " +
                "listaArgumentos=$listaArgumentos)"
    }
}