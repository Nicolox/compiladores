package co.edu.uniquindio.compiladores.proyecto.sintaxis

import co.edu.uniquindio.compiladores.proyecto.lexico.Categoria
import co.edu.uniquindio.compiladores.proyecto.lexico.Error
import co.edu.uniquindio.compiladores.proyecto.lexico.Token

class AnalizadorSintactico(var listaTokens:ArrayList<Token>)
{
    var posicionActual=0
    var tokenActual=listaTokens[0]
    var listaErrores = ArrayList<Error>()

    fun reportarError(mensaje:String)
    {
        listaErrores.add (Error(mensaje, tokenActual.fila, tokenActual.columna ))
    }

    fun obtenerSiguienteToken()
    {
        posicionActual++

        if(posicionActual<listaTokens.size)
        {
            tokenActual=listaTokens[posicionActual]
        }
    }

    /**
     * UnidadDeCompilacion ::= app{listaDeSentencias}
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion?
    {
        if (tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="app")
        {
            obtenerSiguienteToken()

            if(tokenActual.categoria==Categoria.LLAVE_IZQUIERDA)
            {
                val listaDeSentencias:ArrayList<Sentencia> = esListaDeSentencias()

                if (listaDeSentencias.size>0)
                {
                    obtenerSiguienteToken()
                    return if (tokenActual.categoria==Categoria.LLAVE_DERECHA)
                    {
                        UnidadDeCompilacion(listaDeSentencias)
                    }

                    else null
                }
            }
        }

        reportarError("No es una unidad de compilacion valida")
        return null
    }

    /**
     * <ListaDeSentencias> ::= <Sentencia> <ListaDeSentencias>
     */
    fun esListaDeSentencias(): ArrayList<Sentencia>
    {
        var listaSentencias=ArrayList<Sentencia>()
        var sentencia=esSentencia()

        while(sentencia!=null)
        {
            listaSentencias.add(sentencia)
            sentencia=esSentencia()
        }

        return listaSentencias
    }

    /**
     * <Sentencia> ::= <Decision> | <DeclaraciónVariables> | <Asignacion> | <Impresion> | <CicloFor> |
     * <retorno> | <Lectura> | <InvocacionFuncion> | <Incremento/Decremento> | <Funcion>
     */
    fun esSentencia():Sentencia?
    {
        return null
    }

    /**
     *<Funcion> ::= met <IdentificadorMetodo> [<ListaArgumentos>] “{“ <ListaDeSentencias> “}”
     */
    fun esFuncion(): Funcion?
    {
        if (tokenActual.categoria==Categoria.PALABRA_RESERVADA && tokenActual.lexema=="met")
        {
            obtenerSiguienteToken()
            val identificador=esIdentificadorMetodo()
            if (identificador!=null)
            {
                var nombreFun=tokenActual
                obtenerSiguienteToken()

                val listaDeArgumentos:ArrayList<Argumento>? = esListaDeArgumentos()

                if (tokenActual.categoria==Categoria.LLAVE_IZQUIERDA)
                {
                    obtenerSiguienteToken()

                    val listaDeSentencias:ArrayList<Sentencia> = esListaDeSentencias()

                    obtenerSiguienteToken()
                    if (tokenActual.categoria==Categoria.LLAVE_DERECHA)
                    {
                        return Funcion(nombreFun, listaDeSentencias, listaDeArgumentos)
                    }
                    else
                    {
                        reportarError("Falta cerrar la funcion con llave derecha")
                    }
                }
                else
                {
                    reportarError("Falta la llave izquierda")
                }
            }
            else
            {
                reportarError("El identificador de metodo no es valido")
            }
        }

        reportarError("La estructura no es valida para un metodo")
        return null
    }

    /**
     * <Identificador> ::=  <IdentificadorVariable> | <IdentificadorMetodo> | <IdentificadorClase>
     */
    fun esIdentificador(): Identificador?
    {
        return null
    }

    /**
     * <IdentificadorMetodo> ::=  "$" <ListaDeCaracteres> "$"
     */
    fun esIdentificadorMetodo(): IdentificadorMetodo?
    {
        if (tokenActual.categoria==Categoria.IDENTIFICADOR_METODO)
        {
            return IdentificadorMetodo(tokenActual)
        }
        else
        {
            reportarError("No es un identificador de metodo valido")
        }

        return null
    }

    /**
     * <IdentificadorVariable> ::=  "#" <ListaDeCaracteres> "#"
     */
    fun esIdentificadorVariable(): IdentificadorVariable?
    {
        return null
    }

    /**
     * <IdentificadorClase> ::=  "%" <ListaDeCaracteres> "%"
     */
    fun esIdentificadorClase(): IdentificadorClase?
    {
        return null
    }

    /**
     * <ListaArgumentos> ::= “(“ [<Argumento> [<ListaArgumentos>]] “)”
     */
    fun esListaDeArgumentos(): ArrayList<Argumento>?
    {
        if (tokenActual.categoria==Categoria.PARENTESIS_IZQUIERDO)
        {
            var listaArgumentos = ArrayList<Argumento>()
            var argumento = esArgumento()

            while (argumento != null)
            {
                listaArgumentos.add(argumento)
                argumento = esArgumento()
            }

            if (tokenActual.categoria==Categoria.PARENTESIS_DERECHO)
            {
                return listaArgumentos
            }
        }

        return null
    }

    fun esArgumento(): Argumento?
    {
        return null
    }

    /**
     * <ListaDeCaracteres> ::= <Caracter> <ListaDeCaracteres>
     */
    fun esListaDeCaracteres(): ArrayList<Caracter>?
    {
        return null
    }

    /**
     * <Caracter> ::= ASCII
     */
    fun esCaracter(): Caracter?
    {
        return null
    }
}