package co.edu.uniquindio.compiladores.proyecto.sintaxis

import co.edu.uniquindio.compiladores.proyecto.lexico.Categoria
import co.edu.uniquindio.compiladores.proyecto.lexico.Token

class ExpresionRelacional(var expresiones: ArrayList<Expresion>, var operadorRelacional: Token) : Expresion()
{
}