/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiltradoDatos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author axotl
 */
public class AntiInjectionTest {
    
    AntiInjection instance;
    
    @Before
    public void init() {
        instance = new AntiInjection();
    }
    /**
     * Test of isSafe method, of class AntiInjection.
     */
    @Test
    public void testIsSafe() {
        System.out.println("isSafe con palabra reservada");        
        String revisar = "inner";
        boolean expResult = false;//false
        boolean result = instance.isSafe(revisar);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testIsSafeNoReservada() {
        System.out.println("isSafe sin palabra reservada");        
        String revisar = "computadora";
        boolean expResult = true;
        boolean result = instance.isSafe(revisar);
        assertEquals(expResult, result);        
    }
    /**
     * Test of safe method, of class AntiInjection.
     */
    @Test
    public void testIsSafeSafeConNulo() {
        System.out.println("safe con nulo, devuelve falso");
        Object revisar = null;
        boolean expResult = false;
        boolean result = instance.safe(revisar);
        assertEquals(expResult, result);
    }
    @Test
    public void testIsSafeSafeConObjeto() {
        System.out.println("safe con objeto vacio, verdadero");
        Object revisar = new Object();
        boolean expResult = true;
        boolean result = instance.safe(revisar);
        assertEquals(expResult, result);
    }
    @Test
    public void testIsSafeSafeConObjetoNoSafe() {
        System.out.println("safe con objeto nosafe, falso");
        System.out.println("Falta crear un bjeto nosafe, falso") ;
        Object[] revisar = new Object[]{"select","inner","join","delete","update"};
        boolean expResult = false;
        boolean result = instance.safeArray(revisar);
        assertEquals(expResult, result);
    }
    /**
     * Test of safeArray method, of class AntiInjection.
     */
    @Test
    public void testIsSafesafeArrayConObjetoNoSafe() {
        System.out.println("safeArray con objeto nosafe, esperando falso");
        Object[] revisar = new Object[]{"select","inner","join","delete","update"};
        boolean expResult = false;
        boolean result = instance.safeArray(revisar);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsSafesafeArrayConObjetoSafe() {
        System.out.println("safeArray con objeto safe, esperando verdadero");
        String[] revisar = new String[]{"pedro","alberto","aragon","limantour","pete"};
        boolean expResult = true;
        boolean result = instance.safeArray(revisar);
        assertEquals(expResult, result);
    }
    
    
}
