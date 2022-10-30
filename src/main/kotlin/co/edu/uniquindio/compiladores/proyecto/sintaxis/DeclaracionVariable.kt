package co.edu.uniquindio.compiladores.proyecto.sintaxis

import co.edu.uniquindio.compiladores.proyecto.lexico.Token

class DeclaracionVariable(var nombreIdentificador: Token, var constante: Boolean) :Sentencia()
{
}