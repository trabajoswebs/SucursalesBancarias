/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sucursalesbancarias;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer
 */
public class SucursalesBancarias {
    
    public static final String DIRECTORY = "..\\Profesores\\";
    public static final String SUCRBANCFILENAME = "SucursalesBancarias.txt";
    public static final String SUCRBANCFILEPATH = DIRECTORY + SUCRBANCFILENAME;
    public static final String ENTBANCARIASFILEPATH = DIRECTORY + "EntidadesBancarias.txt";
    
    
    static Scanner sc = new Scanner(System.in);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        subMenuSucursalesBancarias();
    }
    
    
   
    /**
     * Se da de alta a un curso en el fichero SucursalesBancarias.txt
     * 
     */
    public static void altaSucursalBancaria() {
        
        String codEntidadBancaria, codSucurBancaria, nombreSucursalBancaria, cadena, continuar;        
        RandomAccessFile fichero = null; 
        long size = 0;
        int indice;
        boolean repetir = false;
        
        do {                          
                System.out.println("\nListado de las sucursales bancarias:\n");
                System.out.println(imprimeSucursalesBancarias());

            try {
                System.out.println("Introduzca el código de la Sucursal Bancaria:");
                codSucurBancaria = sc.nextLine();
                
                 if (codSucurBancaria.isEmpty()) {
                    throw new Exception("Debe introducir el código de la Sucursal Bancaria.");
                }
                codEntidadBancaria = codSucurBancaria.substring(0, 4); // Obtenemos el código de la Entidad Bancaria
              
                
                System.out.println("Introduzca el nombre de la Sucursal Bancaria:");
                
                nombreSucursalBancaria = sc.nextLine();                
               
                if (nombreSucursalBancaria.isEmpty()) {
                    throw new Exception("Debe introducir el nombre de la Sucursal Bancaria.");
                }

                crearFichero(DIRECTORY, SUCRBANCFILENAME); //Se crea el fichero si no existe
                
                fichero = new RandomAccessFile(SUCRBANCFILEPATH, "rw");
                cadena = fichero.readLine();
                
                while(cadena != null){
                    indice = cadena.indexOf(",");
                    if(indice != -1){                        
                        if (cadena.substring(0, indice).equalsIgnoreCase(codSucurBancaria)) //Obtenemos el código del curso
                            throw new Exception("El código de la Sucursal Bancaria ya se encuentra en la lista");
                    }
                    cadena = fichero.readLine();                    
                }                
                size = fichero.length(); 
                fichero.seek(size);// nos situamos al final del fichero
                cadena = codSucurBancaria.toUpperCase() + "," + nombreSucursalBancaria + "\n";
                fichero.writeBytes(cadena);
                
                System.out.println("Se ha añadido correctamente la Sucursal Bancaria en el fichero.");
                System.out.println("Si desea añadir más Sucursales Bancarias al fichero introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir =(continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo sucursal bancaria
                
            } catch (FileNotFoundException ex) {     //Si no se encuentra el fichero            
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
                sc.nextLine();
            } catch (IOException ex) {
                repetir = true;
                Logger.getLogger("Ha ocurrido una excepción: " + SucursalesBancarias.class.getName()).log(Level.SEVERE, null, ex);
                sc.nextLine();
            } catch (Exception ex) {                
                repetir = true;
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
                System.out.println("Si desea añadir más Sucursales Bancarias al fichero introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir =(continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo sucursal bancaria
            } finally{
                if (fichero != null) {
                    try {
                        fichero.close();
                    } catch (IOException ex) {
                        Logger.getLogger("Ha ocurrido una excepción: " + SucursalesBancarias.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } while (repetir);
        
    }
    
    
    /**
     * Elimina la sucursal bancaria en el fichero Profesores/SucursalesBancarias.txt
     */
    
    public static void bajaSucursalBancaria(){
        
        File ficheroActualizado = new File(DIRECTORY + "sucursalesActualizadas.txt");
        String codSucursal, cadena, continuar;
        int indice;
        RandomAccessFile fichero = null;
        RandomAccessFile ficheroNuevo = null;
        boolean repetir = false;
        
       
        do {      
            
            try {
                       
                boolean existeCodigo = false;
                System.out.println("\nListado de las sucursales bancarias:\n");
                System.out.println(imprimeSucursalesBancarias());
                System.out.print("Indique el código de la sucursal que desea eliminar: ");
                codSucursal = sc.nextLine();
                
                if (codSucursal.trim().isEmpty()) {
                    throw new Exception("Debe introducir un código de asignatura válido.");
                }
                
                fichero = new RandomAccessFile(SUCRBANCFILEPATH, "r");
                
                if(fichero.length() == 0) throw new Exception("El fichero de las sucursales se encuentra vacio.");
                
                cadena = fichero.readLine();
                
                while(cadena != null){
                    indice = cadena.indexOf(",");
                    if (indice != -1) {
                        if(cadena.substring(0, indice).equalsIgnoreCase(codSucursal)){
                            existeCodigo = true;
                            break; // dejamos de recorrer el fichero saliendo del while
                        } 
                            
                    }                    
                    cadena = fichero.readLine();
                }
                
                if (! existeCodigo) // Si el código no existe en el fichero lanzamos la exepción
                        throw new Exception("El código de la sucursal (" + codSucursal.toUpperCase() + ") que se desea eliminar no existe en el fichero.");
                
                crearFichero(DIRECTORY, "sucursalesActualizadas.txt"); //creamos un nuevo fichero
                
                ficheroNuevo = new RandomAccessFile(ficheroActualizado, "rw");
                
                fichero.seek(0); //Llevamos el puntero al inicio                
                
                cadena = fichero.readLine();
                
                while (cadena != null) {
                    indice = cadena.indexOf(",");
                    if(indice != -1){  
                        if (! cadena.substring(0, indice).equalsIgnoreCase(codSucursal)) {

                            ficheroNuevo.seek(ficheroNuevo.getFilePointer());
                            ficheroNuevo.writeBytes(cadena + "\n");
                        }
                    }
                    cadena = fichero.readLine();
                }
             
                ficheroNuevo.close(); //Cerramos el fichero actualizado
                fichero.close(); //Debemos cerrar el fichero antes de eliminarlo
                
                File ficheroOriginal = new File(DIRECTORY + "sucursalesActualizadas.txt");
                File destFichero = new File(SUCRBANCFILEPATH);
                
                 if (destFichero.delete()) { //Borramos el fichero anterior
                     
                        if (ficheroOriginal.renameTo(destFichero)) {//Renombramos el fichero  
                        System.out.println("Se ha eliminado correctamente la sucursal bancaria  " + codSucursal + " del fichero.");
                        System.out.println("Si desea eliminar más sucursales de la lista introduzca la letra: \"S\"");

                        continuar = sc.nextLine();
                        repetir = (continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo cursos

                    } else {
                        throw new Exception("No se ha podido renombrar el archivo " + ficheroActualizado.getName() + " a " + SUCRBANCFILENAME); 
                    }

                } else {
                    throw new Exception("No se ha podido eliminar el fichero " + destFichero.getName() + " necesario para reeditarlo.");
                }
                

            } catch (FileNotFoundException ex) {//Si no se encuentra el fichero            
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }catch(IOException ieo){
                System.out.println("Se ha producido un error: " + ieo.getMessage());
                sc.nextLine();
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Si desea eliminar alguna sucursal bancaria de la lista introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir = (continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo sucursales bancarias
                
            } finally {
                try {
                    if (fichero != null) {
                        fichero.close();
                    }
                    if (ficheroNuevo != null) {
                        ficheroNuevo.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }                
            }

        } while (repetir);
        
    }
    
    /**
     * Imprime por pantalla Sucursales bancarias que se encuentran en el fichero
     * @return String codigo y nombre de las sucursales bancarias
     */
    
    public static String imprimeSucursalesBancarias() {
        String cadena;
        FileReader fr = null;
        BufferedReader entrada = null;
        StringBuilder cursos = new StringBuilder();
        
        try {            
            fr = new FileReader(SUCRBANCFILEPATH);
            entrada = new BufferedReader(fr);
            cadena = entrada.readLine();
            while(cadena != null){
                cursos.append(cadena);
                cursos.append("\n");
                cadena = entrada.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SucursalesBancarias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SucursalesBancarias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SucursalesBancarias.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return cursos.toString();
    }
    
    /**
     * SUBMENU
     */
    public static void subMenuSucursalesBancarias(){
        int opcion = 0;
        boolean continuar = true;
        
        do{             
            System.out.println("\n*************** MANTENIMIENTO DE LAS SUCURSALES BANCARIAS ***************\n");
            System.out.println("\t1. ALTA DE SUCURSALES BANCARIAS");
            System.out.println("\t2. BAJA DE SUCURSALES BANCARIAS");
            System.out.println("\t3. MOSTRAR SUCURSALES");            
            System.out.println("\t0. SALIR SUCURSALES");            
            System.out.print("\n\t   Opcion seleccionada: ");
            
            opcion = sc.nextInt();
            
            switch(opcion){
                case 0: default:
                    sc.nextLine(); //Limpiamos el Buffer
                    continuar = false;
                    break;
                case 1:
                    sc.nextLine(); //Limpiamos el Buffer
                    System.out.println("\n1. ALTA DE SUCURSALES BANCARIAS");
                    altaSucursalBancaria();
                    break;
                case 2:
                    sc.nextLine(); //Limpiamos el Buffer
                    System.out.println("\n2. BAJA DE SUCURSALES BANCARIAS");
                    bajaSucursalBancaria();
                    break;
                case 3:
                    System.out.println("\n3. MOSTRAR SUCURSALES");
                    System.out.println(imprimeSucursalesBancarias());                   
                    break;
            }
            
        }while(continuar);
        
    }
    
    /**
     * Crea el fichero, pasado por parametro, si no existe
     * @param ruta direccion de la ubicacion del fichero
     * @param fichero nombre del fichero
     * @throws IOException 
     */
    public static void crearFichero(String ruta, String fichero) throws IOException {
        File cursoRuta = new File(ruta);
        File cursoFichero = new File(cursoRuta, fichero);
        
        if (! cursoFichero.exists()) {

            System.out.println("El fichero " + cursoFichero.getAbsolutePath() + " no existe.");

            if (!cursoRuta.exists()) {
                System.out.println("El directorio " + cursoRuta.getAbsolutePath() + " no existe.");

                if (cursoRuta.mkdir()) {
                    System.out.println("Se ha creado el directorio " + cursoRuta.getAbsolutePath());

                    if (cursoFichero.createNewFile()) {
                        System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                    } else {
                        throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                    }

                } else {
                    throw new IOException("No se ha podido crear la ruta " + cursoRuta.getAbsolutePath());
                }
            } else {

                if (cursoFichero.createNewFile()) {
                    System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                } else {
                    throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                }

            }
        }
    }
}