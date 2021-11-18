/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package grafolistas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author federico
 */
class Registro {
     String origen;
     String destino;
     int peso;

    public Registro(String origen, String destino, String peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = Integer.parseInt(peso);
    }
    public void display(){
        System.out.println(this.origen+"->"+this.destino+":"+this.peso);
    } 
}
class Arco {
    String nombre_destino;
    int peso;

    public Arco(String nombre_destino, int peso) {
        this.nombre_destino = nombre_destino;
        this.peso = peso;
    }
    
}
class Vertice {
    String nombre;
    boolean visto;
    LinkedList<Arco> arcos;

    public Vertice(String nombre) {
        this.nombre = nombre;
        this.visto  = false;
        this.arcos  = new LinkedList<>();
    }
    public void display() {
        System.out.print(this.nombre + " >>|" );
        displayArcos();
        //System.out.println("\n");
    }

    public void addEdge(Vertice destino, int peso) {
        Arco arco = new Arco(destino.nombre, peso);
        this.arcos.add(arco);
    }
    public void displayArcos() {
        for (Arco arco : arcos) {
            System.out.print(arco.nombre_destino + ":" +arco.peso + " |");
        }
        System.out.println("");
    }
}
class Grafo {
    LinkedList<Vertice> vertices;

    public Grafo() {
        this.vertices = new LinkedList<>();
    }
    public void limpiarVisto(){
        for (Vertice vertice : vertices) {
            vertice.visto = false;
        }
    } 
    public void addVertice(String nombre) {
        if (verticeExists(nombre)==false){
           Vertice v = new Vertice(nombre);
           this.vertices.add(v);
        }
    }
    public boolean verticeExists(String nombre){
        for (Vertice vertice : this.vertices) {
            if (vertice.nombre.equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }
    public void crearVertices(Queue<Registro> registros) {
    //ciclo
       //addVertice
        for (Registro registro : registros) {
            addVertice(registro.origen);
            addVertice(registro.destino);
        }
    }
    public void displayVertices() {
        for (Vertice vertice : this.vertices) {
            vertice.display();
        }
    } 
    public void crearArcos(Queue<Registro> registros){
        for (Registro registro : registros) {
            Vertice origen = buscarVertice(registro.origen);
            Vertice destino = buscarVertice(registro.destino);
            origen.addEdge(destino,registro.peso);
        }
    }
    public Vertice buscarVertice(String nombre_vertice) {
        for (Vertice vertice : this.vertices) {
            if (vertice.nombre.equalsIgnoreCase(nombre_vertice)) {
                return vertice;
            }
        }
        return null;        
    }
    public String generaGrafo() {
        String cadena = "digraph G {\n";
        for (Vertice vertice : vertices) {
            for (Arco arco : vertice.arcos) {
                 cadena = cadena + vertice.nombre;
                 cadena = cadena + " -> ";
                 cadena = cadena + arco.nombre_destino;
                 cadena = cadena + " [label= \""+arco.peso+"\"];";
                 cadena = cadena + "\n";
            }
        }
        cadena = cadena + "}\n";
        return cadena;
    }
    public void grabaGrafo(String archivo_salida, String contenido_grafo) {
        try {
             BufferedWriter bf = new BufferedWriter(new FileWriter(new File(archivo_salida),false));
             bf.write(contenido_grafo);
             bf.flush();
             bf.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public void DepthFirstSearch(String inicio, String destino) {
        limpiarVisto(); //Ponemos en false los campos "visto" de vertices
        boolean ENCONTRADO = false;
        Stack<Vertice> pila = new Stack<>();
        Vertice v = buscarVertice(inicio);
        pila.push(v);
        do {
            despliegaStack(pila);
            v = pila.pop();
            if (v.nombre.equals(destino)) {
               ENCONTRADO = true; 
            } else {
               v.visto = true;
               agregarVecinos(pila, v);
            }    
        } while (pila.isEmpty()==false && ENCONTRADO==false) ;
        System.out.println("DEPTH FIRST SEARCH");
        if (ENCONTRADO==true) {
            System.out.println("Existe un camino entre "+inicio+" y "+destino);
        } else {
            System.out.println("NO HAY un camino entre "+inicio+" y "+destino);
        }
    }

    public void BreadthFirstSearch(String inicio, String destino) {
        limpiarVisto(); //Ponemos en false los campos "visto" de vertices
        boolean ENCONTRADO = false;
        Queue<Vertice> cola = new LinkedList<>();
        Vertice v = buscarVertice(inicio);
        cola.add(v);
        do {
            despliegaQueue(cola);
            v = cola.remove();
            if (v.nombre.equals(destino)) {
               ENCONTRADO = true; 
            } else {
               v.visto = true;
               agregarVecinosQ(cola, v);
            }
            //despliegaStack(pila);
        } while (cola.isEmpty()==false && ENCONTRADO==false) ;
        System.out.println("BREADTH FIRST SEARCH");
        if (ENCONTRADO==true) {
            System.out.println("Existe un camino entre "+inicio+" y "+destino);
        } else {
            System.out.println("NO HAY un camino entre "+inicio+" y "+destino);
        }

    }
    public void agregarVecinosQ(Queue<Vertice> q, Vertice vertice) {
        for (Arco arco : vertice.arcos) {
            Vertice vecino = buscarVertice(arco.nombre_destino);
            if (vecino.visto==false) {
               q.add(vecino);
            }
        }
    }


    public void agregarVecinos(Stack<Vertice> s, Vertice vertice) {
        for (Arco arco : vertice.arcos) {
            Vertice vecino = buscarVertice(arco.nombre_destino);
            if (vecino.visto==false) {
               s.push(vecino);
            }
        }
    }
    public static void despliegaStack(Stack<Vertice> stack) {
        if (stack.isEmpty()==false) {
            System.out.print("Stack:");
            for (Vertice vertice : stack) {
                System.out.print(" " + vertice.nombre);
            }
            System.out.println("");
        }
    }
    public static void despliegaQueue(Queue<Vertice> queue) {
        if (queue.isEmpty()==false) {
            System.out.print("Queue:");
            for (Vertice vertice : queue) {
                System.out.print(" " + vertice.nombre);
            }
            System.out.println("");
        }
    }
}
public class GrafoListas {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length>0) {
           Queue<Registro> registros = cargaArchivo(args[0]);
           //Crear grafo
           Grafo grafo = new Grafo();
           //Cargar vertices
           grafo.crearVertices(registros);
           //Cargar arcos
           grafo.crearArcos(registros);
           //Desplegar vertices
           grafo.displayVertices();
           String s = grafo.generaGrafo();
           //System.out.println(s);
           if (args.length>1) {    //Si hay nombre de archivo, escribimos a disco
              grafo.grabaGrafo(args[1], s);
               System.out.println("Se escribió el archivo:"+args[1]);
           } else {                //en caso contrario escribimos en pantalla
               System.out.println(s);
           } 
           if (args.length>3) {
              grafo.DepthFirstSearch(args[2], args[3]);
              grafo.BreadthFirstSearch(args[2],args[3]);
           } 
        } 
    }

    public static Queue<Registro> cargaArchivo(String archivo){
        Queue<Registro> queue = new LinkedList<>();
        try {
            Scanner scan = new Scanner(new File(archivo));
            while(scan.hasNext()) {
                String linea = scan.nextLine();
                String[] arreglo = linea.split(",");
                Registro reg = new Registro(arreglo[0],arreglo[1],arreglo[2]);
                queue.add(reg);
            } 
            scan.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return queue;
    }
}
