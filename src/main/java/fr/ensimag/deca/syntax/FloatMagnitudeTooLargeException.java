package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.Token;

public class FloatMagnitudeTooLargeException extends DecaRecognitionException{
	public FloatMagnitudeTooLargeException(DecaParser recognizer, Token offendingToken) {
		super(recognizer, offendingToken);
	}
	
	public String getMessage() {
		return this.getOffendingToken().getText() +
				" n'est pas un flottant codable sur 32 bits (valeur extrêmes : +/-" +
				Float.MAX_VALUE + ")";
	}
}
