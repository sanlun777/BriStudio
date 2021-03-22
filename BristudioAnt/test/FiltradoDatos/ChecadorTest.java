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
public class ChecadorTest {
    
    public ChecadorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of checa method, of class Checador.
     */
    @Test
    public void testCheca() {
        System.out.println("checa con objetos tipo LETRA, esperamos true");
        Checador.datosTipo[] tipos = {Checador.datosTipo.LETRA};
        int[][] tamanos = {{2,30}};
        String[] params = {"curso"};
        Object[] datos = new Object[params.length];
        datos[0] = "prueba"; 
        Checador instance = new Checador();
        boolean expResult = true;
        boolean result = instance.checa(tipos, tamanos, datos);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of checaLength method, of class Checador.
     */
    @Test
    public void testChecaLength() {
        System.out.println("checaLength, cadena mayor a 2 y menor o igual a 30 esperamos true");
        int i = 0;
        String analiza = "pedro alberto aragon limantour";
        Checador instance = new Checador();
        instance.tamanos = new int[][]{{2,30}};
        boolean expResult = true;
        boolean result = instance.checaLength(i, analiza);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
