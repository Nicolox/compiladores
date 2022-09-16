package co.edu.uniquindio.compiladores.proyecto.lexico

class AnalizadorLexico(var codigoFuente: String) {

    var posicionActual = 0
    var carActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0

    fun almacenarToken(lexema: String, cat: Categoria, fila: Int, columna: Int) =
        listaTokens.add(Token(lexema, cat, fila, columna))

    fun hacerBT(posicionInicial: Int, filaInical: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInical
        columnaActual = columnaInicial

        carActual = codigoFuente[posicionActual]
    }

    fun analizar() {

        while (carActual != finCodigo) {

            if (carActual == ' ' || carActual == '\t' || carActual == '\n') {
                obtenerSiguienteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esDecimal()) continue
            if (esParentesisIzq()) continue
            if (esParentesisDer()) continue
            if (esLlaveIzq()) continue
            if (esLlaveDer()) continue
            if (esCorcheteIzq()) continue
            if (esCorcheteDer()) continue
            if (esFinSentencia()) continue
            if (esComa()) continue
            if (esPunto()) continue
//            if (esDosPunto()) continue
            if (esIdentificadorVariable()) continue

            almacenarToken("" + carActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()
        }
    }

    //Automata para determinar si es un número entero
    fun esEntero(): Boolean {

        if (carActual.isDigit()) {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual
            val posicionInicial = posicionActual

            lexema += carActual
            obtenerSiguienteCaracter()

            while (carActual.isDigit()) {
                lexema += carActual
                obtenerSiguienteCaracter()
            }
            if (carActual == '.') {
                hacerBT(posicionInicial, filaInical, columnaInicial)
                return false
            }
            almacenarToken(lexema, Categoria.ENTERO, filaInical, columnaInicial)
            return true
        }
        return false
    }

    //Automata para determinar si es un identificador de variable
    //Toma como variabletodo lo que tenga un #
    //Token(Lexema='#casa', Categoria=IDENTIFICADOR_VARIABLE, Fila=0, Columna=15), Token(Lexema='#', Categoria=IDENTIFICADOR_VARIABLE, Fila=0, Columna=20)
    fun esIdentificadorVariable(): Boolean {

        if (carActual == '#') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            while ((carActual.isLetter() || carActual.isDigit() || (carActual == '_'))) {
                lexema += carActual
                obtenerSiguienteCaracter()
            }
            if (carActual == '#') {
                lexema += carActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema, Categoria.IDENTIFICADOR_VARIABLE, filaInical, columnaInicial)
            return true
        }

        return false
    }

    fun esDecimal(): Boolean {


        if (carActual == '.' || carActual.isDigit()) {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual
            if (carActual == '.') {
                lexema += carActual
                obtenerSiguienteCaracter()

                if (carActual.isDigit()) {
                    lexema += carActual
                    obtenerSiguienteCaracter()
                }
            } else {
                lexema += carActual
                obtenerSiguienteCaracter()

                while (carActual.isDigit()) {
                    lexema += carActual
                    obtenerSiguienteCaracter()
                }
                if (carActual == '.') {
                    lexema += carActual
                    obtenerSiguienteCaracter()
                }
            }
            while (carActual.isDigit()) {
                lexema += carActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema, Categoria.DECIMAL, filaInical, columnaInicial)
            return true
        }
        return false


    }

    fun esParentesisIzq(): Boolean {
        if (carActual == '(') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.PARENTESIS, filaInical, columnaInicial)
            return true
        }

        return false
    }

    fun esParentesisDer(): Boolean {
        if (carActual == ')') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.PARENTESIS, filaInical, columnaInicial)
            return true
        }

        return false
    }
    fun esLlaveIzq(): Boolean {
        if (carActual == '{') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.LLAVES, filaInical, columnaInicial)
            return true
        }
        return false
    }
    fun esLlaveDer(): Boolean {
        if (carActual == '}') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.LLAVES, filaInical, columnaInicial)
            return true
        }
        return false
    }
    fun esCorcheteIzq(): Boolean {
        if (carActual == '[') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.CORCHETES_IZQ, filaInical, columnaInicial)
            return true
        }
        return false
    }
    fun esCorcheteDer(): Boolean {
        if (carActual == ']') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.CORCHETES_DER, filaInical, columnaInicial)
            return true
        }
        return false
    }
    fun esFinSentencia(): Boolean {
        if (carActual == '*') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.FIN_SENTENCIA, filaInical, columnaInicial)
            return true
        }
        return false

    }
    fun esComa(): Boolean {
        if (carActual == ',') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.SEPARADOR_COMA, filaInical, columnaInicial)
            return true
        }
        return false

    }
    fun esPunto(): Boolean {
        if (carActual == '.') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema += carActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.SEPARADOR_PUNTO, filaInical, columnaInicial)
            return true
        }
        return false

    }

//    fun esDosPunto(): Boolean {
//        if (carActual == ':') {
//            var lexema = ""
//            var filaInical = filaActual
//            var columnaInicial = columnaActual
//
//            lexema += carActual
//            obtenerSiguienteCaracter()
//
//            almacenarToken(lexema, Categoria.SEPARADOR_COMA, filaInical, columnaInicial)
//            return true
//        }
//        return false
//
//    }

    fun obtenerSiguienteCaracter() {
        carActual = if (posicionActual == codigoFuente.length - 1) {
            finCodigo
        } else {
            if (carActual == '\n') {
                filaActual++
                columnaActual = 0
            } else {
                columnaActual++
            }
            posicionActual++
            codigoFuente[posicionActual]
        }
    }

}