package co.edu.uniquindio.compiladores.proyecto.sintaxis

import co.edu.uniquindio.compiladores.proyecto.lexico.Token

class IdentificadorMetodo(val nombre:Token)
{
    override fun toString(): String
    {
        return "IdentificadorMetodo(nombre=$nombre)"
    }
}