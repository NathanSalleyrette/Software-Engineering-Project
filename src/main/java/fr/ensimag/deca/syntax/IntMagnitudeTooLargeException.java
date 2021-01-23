package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.Token;

/**
 * Classe de l'exception levée si un entier dépasse la valeur entière codable en 32 bits
 *
 */
public class IntMagnitudeTooLargeException extends DecaRecognitionException {
	public IntMagnitudeTooLargeException(DecaParser recognizer, Token offendingToken) {
		super(recognizer, offendingToken);
	}
	
	public String getMessage() {
		return this.getOffendingToken().getText() +
				" n'est pas un entier codable sur 32 bits (valeur max : " +
				Integer.MAX_VALUE + ", valeur min : " + Integer.MIN_VALUE + ")";
	}
}
