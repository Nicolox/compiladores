package co.edu.uniquindio.compiladores.proyecto.lexico

class AnalizadorLexico(var codigoFuente: String) {

    var posicionActual = 0
    var caracterActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0
    var listaPalabrasReservadas = listOf(
        "etr",
        "rls",
        "cdn",
        "crt",
        "dsc",
        "crc",
        "cls",
        "return",
        "print",
        "true"
    )

    fun almacenarToken(lexema: String, cat: Categoria, fila: Int, columna: Int) =
        listaTokens.add(Token(lexema, cat, fila, columna))

    fun hacerBT(posicionInicial: Int, filaInical: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInical
        columnaActual = columnaInicial

        caracterActual = codigoFuente[posicionActual]
    }

    fun analizar() {

        while (caracterActual != finCodigo) {

            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSiguienteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esDecimal()) continue
            if (esParentesisDer()) continue
            if (esParentesisIzq()) continue
            if (esLlaveDer()) continue
            if (esLlaveIzq()) continue
            if (esCorcheteIzq()) continue
            if (esCorcheteDer()) continue
            if (esFinSentencia()) continue
            if (esComa()) continue
            if (esPunto()) continue
//            if (esDosPunto()) continue
            if (esIdentificadorVariable()) continue

            almacenarToken("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()
        }
    }

    //Automata para determinar si es un número entero
    fun esEntero(): Boolean {
        if (caracterActual == '[') {
            var lexema = ""
            var filaInical = filaActual
            var columnaInicial = columnaActual
            val posicionInicial = posicionActual

            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual == ']') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.ENTERO, filaInicial, columnaInicial)
                return true
            } else {
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false
            }

        }
        return false
    }

    fun esDecimal(): Boolean {
        if (caracterActual == '<') {
            var lexema = ""
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual
            val filaInicial = filaActual

            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                while (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                if (caracterActual == '.') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    while (caracterActual.isDigit()) {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    }
                }
                return if (caracterActual == '>') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    almacenarToken(lexema, Categoria.DECIMAL, filaInicial, columnaInicial)
                    true
                } else {
                    hacerBT(posicionInicial, filaInicial, columnaInicial)
                    false
                }
            }
        }
        return false
    }

    //Automata para determinar si es un identificador de variable
    fun esIdentificadorVariable(): Boolean {
        if (caracterActual == '#') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual
            var cantidadCaracteres = 0

            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isDigit() || caracterActual.isLetter() || caracterActual == '_') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                cantidadCaracteres++
            }
            return if (caracterActual == '#' && cantidadCaracteres <= 10) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.IDENTIFICADOR_VARIABLE, filaInicial, columnaInicial)
                true
            } else {
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                false
            }
        }
        return false
    }

    fun esIdentificadorClase(): Boolean {
        if (caracterActual == '%') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual

            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isDigit() || caracterActual.isLetter() || caracterActual == '_') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            return if (caracterActual == '%') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.IDENTIFICADOR_CLASE, filaInicial, columnaInicial)
                true
            } else {
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                false
            }
        }
        return false
    }

    fun esIdentificadorMetodos(): Boolean {
        if (caracterActual == '$') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual

            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isDigit() || caracterActual.isLetter() || caracterActual == '_') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            return if (caracterActual == '$') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.IDENTIFICADOR_METODO, filaInicial, columnaInicial)
                true
            } else {
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                false
            }
        }
        return false
    }

    fun esCadenaCaracteres(): Boolean {
        if (caracterActual == '*') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual

            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual != finCodigo) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == '*') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    almacenarToken(lexema, Categoria.CADENA_CARACTERES, filaInicial, columnaInicial)
                    return true
                }
            }
            lexema += caracterActual
            obtenerSiguienteCaracter()
            hacerBT(posicionInicial, filaInicial, columnaInicial)
        }
        return false
    }

    fun esComentarioBloque(): Boolean {
        if (caracterActual == 'ñ') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual

            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '.') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                while (caracterActual != finCodigo) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    if (caracterActual == '.') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        if (caracterActual == 'ñ') {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                            almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE, filaInicial, columnaInicial)
                            return true
                        } else {
                            lexema += caracterActual
                            obtenerSiguienteCaracter()
                        }
                    }
                }

            }
            lexema += caracterActual
            obtenerSiguienteCaracter()
            hacerBT(posicionInicial, filaInicial, columnaInicial)
        }
        return false
    }

    fun esComentarioLinea(): Boolean {
        if (caracterActual == 'ñ') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual

            while (caracterActual != '\n' && caracterActual != finCodigo) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema, Categoria.COMENTARIO_LINEA, filaInicial, columnaInicial)
            return true
        }

        return false
    }

    fun esParentesisIzq(): Boolean {
        if (caracterActual == '(') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.PARENTESIS, filaInicial, columnaInicial)
            return true
        }

        return false
    }

    fun esParentesisDer(): Boolean {
        if (caracterActual == ')') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual

            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.PARENTESIS, filaInicial, columnaInicial)
            return true
        }

        return false
    }

    fun esLlaveIzq(): Boolean {
        if (caracterActual == '{') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual

            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.LLAVES, filaInicial, columnaInicial)
            return true

        }
        return false
    }

    fun esLlaveDer(): Boolean {
        if (caracterActual == '}') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.LLAVES, filaInicial, columnaInicial)
            return true
        }
        return false
    }

    fun esCorcheteIzq(): Boolean {
        if (caracterActual == '[') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual

            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.CORCHETES, filaInicial, columnaInicial)
            return true
        }
        return false
    }

    fun esCorcheteDer(): Boolean {
        if (caracterActual == ']') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual

            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.CORCHETES, filaInicial, columnaInicial)
            return true
        }
        return false
    }

    fun esFinSentencia(): Boolean {
        if (caracterActual == '¬') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.FIN_SENTENCIA, filaInicial, columnaInicial)
            return true
        }

        return false

    }

    fun esComa(): Boolean {
        if (caracterActual == ',') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual

            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.SEPARADOR_COMA, filaInical, columnaInicial)
            return true
        }
        return false

    }

    fun esPunto(): Boolean {
        if (caracterActual == '.') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.SEPARADOR_PUNTO, filaInicial, columnaInicial)
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

    fun esPalabraReservada(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        for (palabra in listaPalabrasReservadas) {
            if (caracterActual == palabra[0]) {
                var centinela = true
                for (caracter in palabra) {
                    if (caracter == caracterActual) {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    } else {
                        centinela = false
                        lexema =""
                        hacerBT(posicionInicial, filaInicial, columnaInicial)
                        break
                    }
                }
                if (centinela) {
                    almacenarToken(lexema, Categoria.PALABRA_RESERVADA, filaInicial, columnaInicial)
                    return true
                }
            }
        }
        return false
    }

}