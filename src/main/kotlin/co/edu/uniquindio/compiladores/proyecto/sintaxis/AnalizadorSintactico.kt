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
                obtenerSiguienteToken()
                val listaDeSentencias: ArrayList<Sentencia> = esListaDeSentencias()

                if (listaDeSentencias.size > 0) {
                    return if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                        obtenerSiguienteToken()
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
        e = esExpresionAritm??tica()
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
     * <Sentencia> ::= <Decision> | <Declaraci??nVariables> | <Asignacion> | <Impresion> | <CicloFor> |
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
     *<Funcion> ::= met <IdentificadorMetodo> [<ListaArgumentos>] ???{??? <ListaDeSentencias> ???}???
     */
    fun esFuncion(): Funcion? {
        print(tokenActual)
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "met") {
            obtenerSiguienteToken()
            val identificador = esIdentificadorMetodo()
            if (identificador != null) {
                var nombreFun = tokenActual
                obtenerSiguienteToken()

                //val listaDeArgumentos: ArrayList<Argumento>? = esListaDeArgumentos()
                print(tokenActual)

                if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                    obtenerSiguienteToken()
                    print(tokenActual)

                    val listaDeSentencias: ArrayList<Sentencia> = esListaDeSentencias()

                    if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                        obtenerSiguienteToken()
                        return Funcion(nombreFun, listaDeSentencias, ArrayList())
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
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE)
        {
            return IdentificadorVariable(tokenActual)
        }
        else
        {
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
     * <ListaArgumentos> ::= ???(??? [<Argumento> [<ListaArgumentos>]] ???)???
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
     * <Decision> ::= ???dsc??? Si ???(??? <Expresi??nLogica> ???)??? ???{??? <ListaDeSentencias> ???}??? [Sino ???{???
     * <ListaDeSentencias> ???}??? ]
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
     * mut <PalabrasReservadas>  <IdentificadorVariable> [<Asignaci??n>] |
     * inmut  <PalabrasReservadas>  <IdentificadorVariable> [<Asignaci??n>]
     */
    fun declaracionVariable(): DeclaracionVariable?
    {
        return null
    }

    /**
     * <Impresion> ::= print ???(??? <Expresi??n> ???)??? ????????
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
                            reportarError("Falta el s??mbolo de fin sentencia '??'")
                        }
                    }
                    else
                    {
                        reportarError("Falta el par??ntesis derecho ')'")
                    }
                }
                else
                {
                    reportarError("No hay una expresi??n v??lida")
                }
            }
            else
            {
                reportarError("Falta el par??ntesis izquierdo '('")
            }
        }

        reportarError("Para imprimir necesita la palabra 'print' al principio")
        return null
    }

    /**
     *  <Incremento|Decremento> ::= ???ss??? | ???rr???
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

        reportarError("No es un indicador v??lido de incremento o decremento")
        return null
    }

    /**
     * <InvocacionFuncion> ::= <IdentificadorM??todo> <ListaArgumentos>
     */
    fun esInvocacionFuncion(): InvocacionFuncion?
    {
        return null
    }

    /**
     * <CicloFor> ::= ???crc??? ???(??? <Declaraci??nVariable> ???????? <ExpresionLogica>????????
     * <Incremento/Decremento> ???????? ???)??? ???{??? <ListaSentencias> ???}???
     */
    fun esCicloFor(): CicloFor?
    {
        return null
    }

    /**
     * <Lectura> ::= read ???(??? <Expresi??n> ???)??? ????????
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
                            reportarError("Falta el s??mbolo de fin sentencia '??'")
                        }
                    }
                    else
                    {
                        reportarError("Falta el par??ntesis derecho ')'")
                    }
                }
                else
                {
                    reportarError("No hay una expresi??n v??lida")
                }
            }
            else
            {
                reportarError("Falta el par??ntesis izquierdo '('")
            }
        }

        reportarError("Para leer necesita la palabra 'read' al principio")
        return null
    }

    /**
     * <Retorno> ::= return <Expresi??n> ????????
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
                    reportarError("Falta el s??mbolo de fin sentencia '??'")
                }
            }
            else
            {
                reportarError("No hay una expresi??n v??lida")
            }
        }

        reportarError("Para hacer un retorno necesita la palabra 'return' al principio")
        return null
    }

    /**
     * <Asignaci??n> ::= ???:??? | <n??mero> | <cadena> | <Invocaci??nFunci??n> ????????
     */
    fun esAsignacion(): Asignacion?
    {
        return null
    }

    /**
     * <Sumatoria> ::= summ ???(??? <IdentificadorVariable> ???)??? ????????
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
                            reportarError("Falta el s??mbolo de fin sentencia '??'")
                        }
                    }
                    else
                    {
                        reportarError("Falta el par??ntesis derecho ')'")
                    }
                }
                else
                {
                    reportarError("No existe un identificador de variable v??lido")
                }
            }
            else
            {
                reportarError("Falta el par??ntesis izquierdo '('")
            }
        }

        reportarError("Para realizar una sumatoria necesita la palabra 'summ' al principio")
        return null
    }

    /**
     * <Expresi??nRelacional> ::= <Expresi??n> <operadorRelacional> <Expresi??n>
     */
    fun esExpresionRelacional(): ExpresionRelacional?
    {
        return null
    }

    /**
     * <Expresi??nAritmetica> ::= <numero> | <identificadorVariable> | <Expresi??nAritmetica> <operadorAritmetico>
     *     <Expresi??nAritmetica> |???(??? <Expresi??nAritmetica>???)???
     *
     *     Sin recursividad por la izquierda:
     *
     *     <ExpAritmetica> ::= "("<ExpAritmetica>")"[operadorAritmetico <ExpAritmetica>] |
     *     <ValorNumerico>[operadorAritmetico <ExpAritmetica>]
     *
     */
    fun esExpresionAritm??tica(): ExpresionAritmetica?
    {
        if(tokenActual.categoria==Categoria.PARENTESIS_IZQUIERDO)
        {
            obtenerSiguienteToken()
            val exp1: ExpresionAritmetica?= esExpresionAritm??tica()
            if(exp1!=null)
            {
                if(tokenActual.categoria==Categoria.PARENTESIS_DERECHO)
                {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria==Categoria.OPERADOR_ARITMETICO)
                    {
                        val operador: Token=tokenActual
                        obtenerSiguienteToken()
                        val exp2: ExpresionAritmetica?= esExpresionAritm??tica()
                        if(exp2!=null)
                        {
                            return ExpresionAritmetica(exp1, operador, exp2)
                        }
                    }
                    else
                    {
                        return ExpresionAritmetica(exp1)
                    }
                }
            }
        }
        else
        {
            val valor = esValorNumerico()
            if (valor!=null)
            {
                obtenerSiguienteToken()
                if (tokenActual.categoria==Categoria.OPERADOR_ARITMETICO)
                {
                    val operador: Token=tokenActual
                    obtenerSiguienteToken()
                    val exp2: ExpresionAritmetica?= esExpresionAritm??tica()
                    if(exp2!=null)
                    {
                        return ExpresionAritmetica(valor, operador, exp2)
                    }
                }
                else
                {
                    return ExpresionAritmetica(valor)
                }
            }
        }

        return null
    }

    /**
     *<Expresi??nCadena> ::= <cadena> [???+??? <identificadorVariable>]
     */
    fun esExpresionCadena(): ExpresionCadena?
    {
        return null
    }

    /**
     * <Expresi??nLogica> ::= <Expresi??nRelacional> <operadorLogico> <Expresi??nLogica>
     */
    fun esExpresionLogica(): ExpresionLogica?
    {
        return null
    }

    /**
     * <Declaraci??nArreglo> ::= array <PalabrasReservadas> <IdentificadorVariable> [???:??? array
     * <PalabrasReservadas>]  ????????
     */
    fun esDeclaracionArreglo(): DeclaracionArreglo?
    {
        return null
    }


    /**
     * <InicializacionArreglo> ::=<IdentificadorVariable> ???:??? array <PalabrasReservadas> ????????
     */
    fun esInicializacionArreglo(): InicializacionArreglo?
    {
        return null
    }

    fun esValorNumerico(): ValorNumerico?
    {
        var signo: Token?=null
        if (tokenActual.categoria==Categoria.OPERADOR_ARITMETICO&&(tokenActual.lexema=="+"||tokenActual.lexema=="-"))
        {
            signo=tokenActual
            obtenerSiguienteToken()
        }
        if(tokenActual.categoria==Categoria.ENTERO||tokenActual.categoria==Categoria.DECIMAL||tokenActual.categoria==Categoria.IDENTIFICADOR)
        {
            val termino=tokenActual
            return ValorNumerico(signo,termino)
        }

        return null
    }

}