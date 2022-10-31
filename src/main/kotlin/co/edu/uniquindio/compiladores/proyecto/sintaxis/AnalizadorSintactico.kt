package co.edu.uniquindio.compiladores.proyecto.sintaxis

import co.edu.uniquindio.compiladores.proyecto.lexico.Categoria
import co.edu.uniquindio.compiladores.proyecto.lexico.Error
import co.edu.uniquindio.compiladores.proyecto.lexico.Token

class AnalizadorSintactico(var listaTokens:ArrayList<Token>) {
    var posicionActual = 0
    var tokenActual = listaTokens[0]
    var listaErrores = ArrayList<Error>()

    fun reportarError(mensaje: String) {
        listaErrores.add(Error(mensaje, tokenActual.fila, tokenActual.columna))
    }

    fun obtenerSiguienteToken() {
        posicionActual++

        if (posicionActual < listaTokens.size) {
            tokenActual = listaTokens[posicionActual]
        }
    }

    /**
     * UnidadDeCompilacion ::= app{listaDeSentencias}
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "app") {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                val listaDeSentencias: ArrayList<Sentencia> = esListaDeSentencias()

                if (listaDeSentencias.size > 0) {
                    obtenerSiguienteToken()
                    return if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                        UnidadDeCompilacion(listaDeSentencias)
                    } else null
                }
            }
        }

        reportarError("No es una unidad de compilacion valida")
        return null
    }

    /**
     * <ListaDeSentencias> ::= <Sentencia> <ListaDeSentencias>
     */
    fun esListaDeSentencias(): ArrayList<Sentencia> {
        var listaSentencias = ArrayList<Sentencia>()
        var sentencia = esSentencia()

        while (sentencia != null) {
            listaSentencias.add(sentencia)
            sentencia = esSentencia()
        }

        return listaSentencias
    }

    /**
     * <Expresion> ::= <ExpresionAritmetica> | <ExpresionCadena> | <ExpresionLogica> | <ExpresionRelacional>
     */
    fun esExpresion(): Expresion?
    {
        var e: Expresion?
        e = esExpresionLogica()
        if (e != null)
        {
            return e
        }
        e = esExpresionAritmética()
        if (e != null) {
            return e
        }
        e = esExpresionRelacional()
        if (e != null)
        {
            return e
        }
        e = esExpresionCadena()
        if (e != null)
        {
            return e
        }
        return null
    }

    /**
     * <Identificador> ::=  <IdentificadorVariable> | <IdentificadorMetodo> | <IdentificadorClase>
     */
    fun esIdentificador(): Identificador?
    {
        var i: Identificador?
        i = esIdentificadorMetodo()
        if (i != null) {
            return i
        }
        i = esIdentificadorClase()
        if (i != null) {
            return i
        }
        i = esIdentificadorVariable()
        if (i!=null)
        {
            return i
        }
        return null
    }

    /**
     * <Sentencia> ::= <Decision> | <DeclaraciónVariables> | <Asignacion> | <Impresion> | <CicloFor> |
     * <retorno> | <Lectura> | <InvocacionFuncion> | <Incremento/Decremento> | <Funcion>
     */
    fun esSentencia(): Sentencia?
    {
        var s: Sentencia?
        s = esDecision()
        if (s != null) {
            return s
        }
        s = declaracionVariable()
        if (s != null) {
            return s
        }
        s = esAsignacion()
        if (s!=null)
        {
            return s
        }
        s = esImpresion()
        if (s!=null)
        {
            return s
        }
        s = esCicloFor()
        if (s!=null)
        {
            return s
        }
        s = esRetorno()
        if (s!=null)
        {
            return s
        }
        s=esLectura()
        if (s!=null)
        {
            return s
        }
        s=esInvocacionFuncion()
        if (s!=null)
        {
            return s
        }
        s=esIncrementoDecremento()
        if (s!=null)
        {
            return s
        }
        s = esFuncion()
        if (s != null) {
            return s
        }
        return null
    }

    /**
     *<Funcion> ::= met <IdentificadorMetodo> [<ListaArgumentos>] “{“ <ListaDeSentencias> “}”
     */
    fun esFuncion(): Funcion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "met") {
            obtenerSiguienteToken()
            val identificador = esIdentificadorMetodo()
            if (identificador != null) {
                var nombreFun = tokenActual
                obtenerSiguienteToken()

                val listaDeArgumentos: ArrayList<Argumento>? = esListaDeArgumentos()

                if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                    obtenerSiguienteToken()

                    val listaDeSentencias: ArrayList<Sentencia> = esListaDeSentencias()

                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                        return Funcion(nombreFun, listaDeSentencias, listaDeArgumentos)
                    } else {
                        reportarError("Falta cerrar la funcion con llave derecha")
                    }
                } else {
                    reportarError("Falta la llave izquierda")
                }
            } else {
                reportarError("El identificador de metodo no es valido")
            }
        }

        reportarError("La estructura no es valida para un metodo")
        return null
    }

    /**
     * <IdentificadorMetodo> ::=  "$" <ListaDeCaracteres> "$"
     */
    fun esIdentificadorMetodo(): IdentificadorMetodo? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_METODO) {
            return IdentificadorMetodo(tokenActual)
        } else {
            reportarError("No es un identificador de metodo valido")
        }

        return null
    }

    /**
     * <IdentificadorVariable> ::=  "#" <ListaDeCaracteres> "#"
     */
    fun esIdentificadorVariable(): IdentificadorVariable?
    {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
            return IdentificadorVariable(tokenActual)
        } else {
            reportarError("No es un identificador de variable valido")
        }

        return null
    }

    /**
     * <IdentificadorClase> ::=  "%" <ListaDeCaracteres> "%"
     */
    fun esIdentificadorClase(): IdentificadorClase?
    {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_CLASE) {
            return IdentificadorClase(tokenActual)
        } else {
            reportarError("No es un identificador de metodo valido")
        }

        return null
    }

    /**
     * <ListaArgumentos> ::= “(“ [<Argumento> [<ListaArgumentos>]] “)”
     */
    fun esListaDeArgumentos(): ArrayList<Argumento>? {
        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            var listaArgumentos = ArrayList<Argumento>()
            var argumento = esArgumento()

            while (argumento != null) {
                listaArgumentos.add(argumento)
                argumento = esArgumento()
            }

            if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                return listaArgumentos
            }
        }

        return null
    }

    /**
     * <Argumento> ::= <Identificador>
     */
    fun esArgumento(): Argumento? {
        var s: Identificador?
        s = esIdentificadorMetodo()
        if (s != null) {
            return Argumento(s)
        }
        s = esIdentificadorClase()
        if (s != null) {
            return Argumento(s)
        }
        s = esIdentificadorVariable()
        if (s!=null)
        {
            return Argumento(s)
        }
        return null
    }

    /**
     * <Decision> ::= “dsc” Si “(“ <ExpresiónLogica> “)” “{“ <ListaDeSentencias> “}” [Sino “{“
     * <ListaDeSentencias> “}” ]
     */
    fun esDecision(): Decision?
    {
        if (tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="dsc")
        {
            obtenerSiguienteToken()
            if (tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="Si")
            {
                obtenerSiguienteToken()
                if (tokenActual.categoria==Categoria.PARENTESIS_IZQUIERDO)
                {
                    obtenerSiguienteToken()
                    val expresion=esExpresionLogica()
                    if (expresion!=null)
                    {
                        obtenerSiguienteToken()
                        if (tokenActual.categoria==Categoria.PARENTESIS_DERECHO)
                        {
                            obtenerSiguienteToken()
                            if(tokenActual.categoria==Categoria.LLAVE_IZQUIERDA)
                            {
                                obtenerSiguienteToken()
                                val listaSentencias=esListaDeSentencias()
                                if (listaSentencias!=null)
                                {
                                    obtenerSiguienteToken()
                                    if(tokenActual.categoria==Categoria.LLAVE_DERECHA)
                                    {
                                        return Decision(expresion, listaSentencias)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * mut <PalabrasReservadas>  <IdentificadorVariable> [<Asignación>] |
     * inmut  <PalabrasReservadas>  <IdentificadorVariable> [<Asignación>]
     */
    fun declaracionVariable(): DeclaracionVariable?
    {
        return null
    }

    /**
     * <Impresion> ::= print “(” <Expresión> “)” “¬”
     */
    fun esImpresion(): Impresion?
    {
        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="print")
        {
            obtenerSiguienteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQUIERDO)
            {
                obtenerSiguienteToken()
                val expresion=esExpresion()
                if(expresion!=null)
                {
                    obtenerSiguienteToken()
                    if(tokenActual.categoria==Categoria.PARENTESIS_DERECHO)
                    {
                        obtenerSiguienteToken()
                        if(tokenActual.categoria==Categoria.FIN_SENTENCIA)
                        {
                            return Impresion(expresion)
                        }
                        else
                        {
                            reportarError("Falta el símbolo de fin sentencia '¬'")
                        }
                    }
                    else
                    {
                        reportarError("Falta el paréntesis derecho ')'")
                    }
                }
                else
                {
                    reportarError("No hay una expresión válida")
                }
            }
            else
            {
                reportarError("Falta el paréntesis izquierdo '('")
            }
        }

        reportarError("Para imprimir necesita la palabra 'print' al principio")
        return null
    }

    /**
     *  <Incremento|Decremento> ::= “ss” | “rr”
     */
    fun esIncrementoDecremento(): IncrementoDecremento?
    {
        if (tokenActual.categoria==Categoria.INCREMENTO&&tokenActual.lexema=="ss")
        {
            return IncrementoDecremento(true)
        }
        else if(tokenActual.categoria==Categoria.DECREMENTO&&tokenActual.lexema=="rr")
        {
            return IncrementoDecremento(false)
        }

        reportarError("No es un indicador válido de incremento o decremento")
        return null
    }

    /**
     * <InvocacionFuncion> ::= <IdentificadorMétodo> <ListaArgumentos>
     */
    fun esInvocacionFuncion(): InvocacionFuncion?
    {
        return null
    }

    /**
     * <CicloFor> ::= “crc” “(“ <DeclaraciónVariable> “¬” <ExpresionLogica>“¬”
     * <Incremento/Decremento> “¬” “)” “{“ <ListaSentencias> “}”
     */
    fun esCicloFor(): CicloFor?
    {
        return null
    }

    /**
     * <Lectura> ::= read “(” <Expresión> “)” “¬”
     */
    fun esLectura(): Lectura?
    {
        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="read")
        {
            obtenerSiguienteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQUIERDO)
            {
                obtenerSiguienteToken()
                val expresion=esExpresion()
                if(expresion!=null)
                {
                    obtenerSiguienteToken()
                    if(tokenActual.categoria==Categoria.PARENTESIS_DERECHO)
                    {
                        obtenerSiguienteToken()
                        if(tokenActual.categoria==Categoria.FIN_SENTENCIA)
                        {
                            return Lectura(expresion)
                        }
                        else
                        {
                            reportarError("Falta el símbolo de fin sentencia '¬'")
                        }
                    }
                    else
                    {
                        reportarError("Falta el paréntesis derecho ')'")
                    }
                }
                else
                {
                    reportarError("No hay una expresión válida")
                }
            }
            else
            {
                reportarError("Falta el paréntesis izquierdo '('")
            }
        }

        reportarError("Para leer necesita la palabra 'read' al principio")
        return null
    }

    /**
     * <Retorno> ::= return <Expresión> “¬”
     */
    fun esRetorno(): Retorno?
    {
        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="read")
        {
            obtenerSiguienteToken()
            val expresion=esExpresion()
            if(expresion!=null)
            {
                obtenerSiguienteToken()
                if(tokenActual.categoria==Categoria.FIN_SENTENCIA)
                {
                    return Retorno(expresion)
                }
                else
                {
                    reportarError("Falta el símbolo de fin sentencia '¬'")
                }
            }
            else
            {
                reportarError("No hay una expresión válida")
            }
        }

        reportarError("Para hacer un retorno necesita la palabra 'return' al principio")
        return null
    }

    /**
     * <Asignación> ::= “:” | <número> | <cadena> | <InvocaciónFunción> “¬”
     */
    fun esAsignacion(): Asignacion?
    {
        return null
    }

    /**
     * <Sumatoria> ::= summ “(“ <IdentificadorVariable> “)” “¬”
     */
    fun esSumatoria(): Sumatoria?
    {
        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="summ")
        {
            obtenerSiguienteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQUIERDO)
            {
                obtenerSiguienteToken()
                val identificador=esIdentificadorVariable()
                if(identificador!=null)
                {
                    obtenerSiguienteToken()
                    if(tokenActual.categoria==Categoria.PARENTESIS_DERECHO)
                    {
                        obtenerSiguienteToken()
                        if(tokenActual.categoria==Categoria.FIN_SENTENCIA)
                        {
                            return Sumatoria(identificador)
                        }
                        else
                        {
                            reportarError("Falta el símbolo de fin sentencia '¬'")
                        }
                    }
                    else
                    {
                        reportarError("Falta el paréntesis derecho ')'")
                    }
                }
                else
                {
                    reportarError("No existe un identificador de variable válido")
                }
            }
            else
            {
                reportarError("Falta el paréntesis izquierdo '('")
            }
        }

        reportarError("Para realizar una sumatoria necesita la palabra 'summ' al principio")
        return null
    }

    /**
     * <ExpresiónRelacional> ::= <Expresión> <operadorRelacional> <Expresión>
     */
    fun esExpresionRelacional(): ExpresionRelacional?
    {
        return null
    }

    /**
     * <ExpresiónAritmetica> ::= <numero> | <identificadorVariable> | <ExpresiónAritmetica> <operadorAritmetico>
     *     <ExpresiónAritmetica> |“(” <ExpresiónAritmetica>”)”
     */
    fun esExpresionAritmética(): ExpresionAritmetica?
    {
        return null
    }

    /**
     *<ExpresiónCadena> ::= <cadena> [“+” <identificadorVariable>]
     */
    fun esExpresionCadena(): ExpresionCadena?
    {
        return null
    }

    /**
     * <ExpresiónLogica> ::= <ExpresiónRelacional> <operadorLogico> <ExpresiónLogica>
     */
    fun esExpresionLogica(): ExpresionLogica?
    {
        return null
    }

    /**
     * <DeclaraciónArreglo> ::= array <PalabrasReservadas> <IdentificadorVariable> [“:” array
     * <PalabrasReservadas>]  “¬”
     */
    fun esDeclaracionArreglo(): DeclaracionArreglo?
    {
        return null
    }


    /**
     * <InicializacionArreglo> ::=<IdentificadorVariable> “:” array <PalabrasReservadas> “¬”
     */
    fun esInicializacionArreglo(): InicializacionArreglo?
    {
        return null
    }

}