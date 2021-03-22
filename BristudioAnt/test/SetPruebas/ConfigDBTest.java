/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SetPruebas;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


import Controlador.ConfigDB;

/**
 *
 * @author axotl
 */
public class ConfigDBTest {
    
    public ConfigDBTest() {
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
    public void testInicializa() {
        System.out.println("Checa inicializado");
        ConfigDB configa = new ConfigDB("bd","usuario","pass");
        assertEquals("bd",configa.getBd());
        assertEquals("usuario",configa.getUser());
        assertEquals("pass",configa.getPass());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of checaLength method, of class Checador.
     */
}
