package aed;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BerretaCoinTestsPropiosDatoComplejo {

    /* Prueba que sucede cuando se agregan transacciones con montos distintos para verificar el heapify.*/
    @Test
    public void empateMontosTx() {
        Berretacoin testcoin = new Berretacoin(10);

        Transaccion[] creacion = new Transaccion[]{
            new Transaccion(0, 0, 1, 1),
            new Transaccion(25, 0, 2, 1),
            new Transaccion(45, 0, 3, 1),
            new Transaccion(5000, 0, 4, 1),
            new Transaccion(1, 0, 5, 1),
            new Transaccion(46, 0, 6, 1)
        };

        testcoin.agregarBloque(creacion);
        assertEquals(new Transaccion(5000, 0, 4, 1), testcoin.txMayorValorUltimoBloque());
    }

    /* El sistema ordena correctamente transacciones sin un orden preestablecido.*/
    @Test
    public void transaccionesDesorden() {
        Berretacoin testcoin = new Berretacoin(10);

        Transaccion[] creacion = new Transaccion[]{
            new Transaccion(0, 0, 1, 5),
            new Transaccion(1, 0, 2, 8),
            new Transaccion(2, 0, 3, 3),
            new Transaccion(3, 0, 4, 9),
            new Transaccion(4, 0, 5, 6),
            new Transaccion(5, 0, 6, 6)
        };

        testcoin.agregarBloque(creacion);
        assertEquals(new Transaccion(3, 0, 4, 9), testcoin.txMayorValorUltimoBloque());
    }

    /* Al empatar todos los usuarios en monto, el sistema elija correctamente el de menor ID. */
    @Test 
    public void empateMontosU() {
        Berretacoin testcoin = new Berretacoin(10);
        assertEquals(new Usuario(1, 0).id(), testcoin.maximoTenedor());
    }

    /* Revisa que se pueda eliminar transacciones de una lista enlazada por su handle y que el contenido esta bien.*/
    @Test
    public void eliminarTransacciones() {
        Berretacoin testcoin = new Berretacoin(10);

        Transaccion[] transacciones = new Transaccion[]{
            new Transaccion(0, 0, 1, 10),
            new Transaccion(1, 1, 2, 2),
            new Transaccion(2, 2, 3, 2),
            new Transaccion(3, 1, 4, 3),
            new Transaccion(4, 4, 1, 1),
            new Transaccion(5, 1, 2, 5)
        };

        Transaccion[] transaccionesCopia1 = new Transaccion[]{
            new Transaccion(1, 1, 2, 2),
            new Transaccion(2, 2, 3, 2),
            new Transaccion(3, 1, 4, 3),
            new Transaccion(4, 4, 1, 1),
            new Transaccion(5, 1, 2, 5)
        };

        testcoin.agregarBloque(transacciones);

        ListaEnlazada<Transaccion> listaTx = new ListaEnlazada<>();
        for (Transaccion t : transacciones) {
            ListaEnlazada<Transaccion>.Handle handleLista = listaTx.agregarAtras(t);
            t.setearHandleLL(handleLista);
        }

        ListaEnlazada<Transaccion>.Handle hCabeza = testcoin.txMayorValorUltimoBloque().obtenerHandleLL();
        listaTx.eliminar(hCabeza);

        Transaccion[] array1 = new Transaccion[transaccionesCopia1.length];
        int i = 0;
        Iterador<Transaccion> itLista = listaTx.iterador();
        while (itLista.haySiguiente()) {
            array1[i] = itLista.siguiente();
            i++;
        }
        for (int j = 0; j < transaccionesCopia1.length - 1; j++) {
            assertEquals(transaccionesCopia1[j], array1[j]);
        }

        ListaEnlazada<Transaccion>.Handle hCola = testcoin.txMayorValorUltimoBloque().obtenerHandleLL();
        listaTx.eliminar(hCola);
        ArrayList<Transaccion> array2 = new ArrayList<>();
        Iterador<Transaccion> it2 = listaTx.iterador();
        while (it2.haySiguiente()) {
            array2.add(it2.siguiente());
        }

        ListaEnlazada<Transaccion>.Handle hMedio = testcoin.txMayorValorUltimoBloque().obtenerHandleLL();
        listaTx.eliminar(hMedio);
        ArrayList<Transaccion> array3 = new ArrayList<>();
        Iterador<Transaccion> it3 = listaTx.iterador();
        while (it3.haySiguiente()) {
            array3.add(it3.siguiente());
        }
    }

    /* Verifica que una transacción con saldo insuficiente no se agregue y no modifique balances.*/
    @Test
    public void transaccionSaldoInsuficciente() {
        Berretacoin sistema = new Berretacoin(3);

        Transaccion[] bloque = {
            new Transaccion(0, 1, 2, 50)
        };

        sistema.agregarBloque(bloque);

        assertEquals(1, sistema.maximoTenedor());
        assertEquals(0, sistema.txUltimoBloque().length);
    }
}
