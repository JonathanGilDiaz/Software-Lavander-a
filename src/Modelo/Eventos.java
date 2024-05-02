//Clase para permitir ciertos caracteretis en los JTextField

package Modelo;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author Jonathan Gil
 */
public class Eventos {

   //Este metodo permitira solo Texto
public void textKeyPress(KeyEvent evt) {
    char car = evt.getKeyChar();
    if (!Character.isLetter(car) && (car != (char) KeyEvent.VK_BACK_SPACE) && (car != (char) KeyEvent.VK_SPACE) && car != 'ñ' && car != 'Ñ') {
        // Verificar si es una vocal acentuada (á, é, í, ó, ú) o una letra con diacrítico (ü, ñ, Ñ)
        if (!(car == 'á' || car == 'é' || car == 'í' || car == 'ó' || car == 'ú' ||
              car == 'Á' || car == 'É' || car == 'Í' || car == 'Ó' || car == 'Ú' ||
              car == 'ü' || car == 'Ü')) {
            evt.consume();
        }
    }
}

        //Este metodo permitira solo Numeros Enteros
    public void numberKeyPress(KeyEvent evt) {
// declaramos una variable y le asignamos un evento
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }

        //Este metodo permitira solo numeros enteros con punto decimal
    public char numberDecimalKeyPress(KeyEvent evt, JTextField textField) {
// declaramos una variable y le asignamos un evento
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && textField.getText().contains(".") && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
            car=0;
        } else if ((car < '0' || car > '9') && (car != '.') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
                        car=0;

        }
        return car;
    }
}
