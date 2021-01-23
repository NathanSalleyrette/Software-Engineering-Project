package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.Token;

/** 
 * Classe de l'exception levée si un flottant est en-deça de la
 * précision minimum sur les flottants en 32 bits
 */
public class FloatPrecisionException extends DecaRecognitionException {

	public FloatPrecisionException(DecaParser recognizer, Token offendingToken) {
		super(recognizer, offendingToken);
	}
	
	public String getMessage() {
		return this.getOffendingToken().getText() +
				" est plus petit que la précision minimale pour les flottants qui vaut : " +
				Float.MIN_VALUE;
	}
}