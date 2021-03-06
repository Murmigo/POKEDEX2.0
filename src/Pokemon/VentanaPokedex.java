package Pokemon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luis Guillermo Abarca
 */
public class VentanaPokedex extends javax.swing.JFrame {

        //conectar a la base de datos
    private Statement estado;
    private ResultSet resultadoConsulta;
    private Connection conexion;
    
    //Botones para el buscador (Lo ideal es cambiarlo a array pero falta tiempo)
    int contadorABC =0;
    int contadorDEF =0;
    int contadorGHI =0;
    int contadorJKL =0;
    int contadorMNO =0;
    int contadorPQR =0;
    int contadorSTU =0;
    int contadorVWX =0;
    int contadorYZ =0;
    char letra;
    int numeroLetras_contador =0;
    //////////////////////////////////////////////////7
    
    //hashmap para almacenar el resultado
    HashMap<String,PokemonValues> listaPokemon = new HashMap();
    //instancia global
    Pokemon poke = new Pokemon();
    /**
     * Creates new form VentanaPokedex
     */
    public VentanaPokedex() {
        initComponents();
        //Poner fondo rojo, se contempla la imagen de la pokedex del fondo? PD:Espacios en blanco y falta de tiempo, mejor no
        getContentPane().setBackground(Color.RED);
        //Poner un marco para el jPanel. PD: No quedaba nunca centrado mejor sin él PD2: Intentar implementar en 3.0
        
 //Musica, empezara a sonar theme1
        poke.theme1.loop(1000);
        poke.theme1.start();

        
        //Conexion a base de datos
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/test", "root", "");
            estado = conexion.createStatement();
            resultadoConsulta = estado.executeQuery("Select * from pokemon");
            
            while(resultadoConsulta.next()){
                PokemonValues pkm = new PokemonValues();
                pkm.nombre = resultadoConsulta.getString(2);
                pkm.generation_id = resultadoConsulta.getInt(5);
                pkm.evolution_chain_id = resultadoConsulta.getInt(6);
                pkm.species= resultadoConsulta.getString(12);
                pkm.height =resultadoConsulta.getInt(10);
                pkm.weight = resultadoConsulta.getInt(11);
                pkm.habitat = resultadoConsulta.getString(15);
            
            listaPokemon.put(resultadoConsulta.getString(1), pkm);
            }
        }
        catch (Exception e){
            System.err.println("No hay Conexion por: " +e);
        }
        
        
        /////////////////////////////////////////////
    }
//Busca si el pokemon tiene alguna evolucion, se produce fallo al BUSCAR un pokemon sin evolución
    private int buscarEvolucion(PokemonValues pk)
    {
        int contador= 0;
        //Almacenamos las posiciones de sus evoluciones en un array list por si se usan más tarde las demás, en principio solo mostrara la ultima de la lista
        ArrayList<Integer> listaEvoluciones = new ArrayList<Integer>();
        boolean encontrado = false;
        PokemonValues pkm = new PokemonValues();
         //mas generico? usar listapokemon.size()?  PD: ERROR, la base de datos esta incompleta. 
         while(!encontrado && contador< 649)
        {
            pkm = listaPokemon.get(String.valueOf(contador));
            if(pkm!=null){
                //Los encontraremos por el evolution_id y excluiremos a si mismo por el nombre
                if(pk.evolution_chain_id == pkm.evolution_chain_id && !pk.nombre.equalsIgnoreCase(pkm.nombre)){
                    listaEvoluciones.add(contador);
                    contador++;
                }
                else
                    contador++;
            }else 
                contador++;
        }
         //Dos casos: con evolucion y sin ella
        if(listaEvoluciones.size()== 0 )
                 return poke.contador-1;
         else
         return listaEvoluciones.get(listaEvoluciones.size()-1)-1;
    }
    private void dibujaPokemon(){//Necesitamos la altura y la anchura del jPanel: por si hace un resize
    Graphics2D g2 = (Graphics2D) jPanel1.getGraphics();
    g2.setColor(Color.BLACK);
    g2.fillRect(0,0,jPanel1.getWidth(),jPanel1.getHeight());
    poke.setPokemonAnchura(jPanel1.getWidth());
    poke.setPokemonAltura(jPanel1.getHeight());
    poke.dibujaPokemon(g2);
    //jPanel1.setBorder(null);
    }
    private void dibujaEvolucion(Pokemon evo)
    {//evo?_En principio se puede hacer con poke, pero ponemos una variable local mejor para no liarnos
    Graphics2D g2 = (Graphics2D) jPanel3.getGraphics();
    g2.setColor(Color.BLACK);
    g2.fillRect(0,0,jPanel3.getWidth(),jPanel3.getHeight());
    evo.setPokemonAnchura(jPanel3.getWidth());
    evo.setPokemonAltura(jPanel3.getHeight());
    evo.dibujaEvo(g2);
    }
    @Override
    public void paint(Graphics g){//LLamaremos a dibujaPokemon, encontrado aqui el fallo al iniciar el programa
    super.paintComponents(g);
    //dibujaPokemon();
    }
    private void mostrarPokemon(int numeroPokemon){ //Muestra por pantalla todo lo que tiene que ver con datos e imagenes
        poke.setFila((numeroPokemon) / 31);
        poke.setColumna((numeroPokemon) %31);
        dibujaPokemon();
        jLabel1.setText("Nº:"+(numeroPokemon+1));
        PokemonValues pkm = listaPokemon.get(String.valueOf(numeroPokemon+1));
        if(pkm!=null){
           muestraDatosPokemon(pkm);
           poke.setFila( buscarEvolucion(pkm) /31);
           poke.setColumna( buscarEvolucion(pkm) %31);
           dibujaEvolucion(poke); 
        }
        else{
            muestraNoEncontrado();
            Graphics2D g2 = (Graphics2D) jPanel3.getGraphics();
            g2.setColor(Color.BLACK);
            g2.fillRect(0,0,jPanel3.getWidth(),jPanel3.getHeight());
        }
    }
    private void muestraNoEncontrado()
    {//Muestra Vacios
       jLabel3.setText("NOT FOUND");
       jLabel6.setText("SPECIE: NOT FOUND");
       jLabel7.setText("HEIGHT: NOT FOUND");
       jLabel8.setText("WEIGHT: NOT FOUND");
       jLabel9.setText("HABITAT: NOT FOUND");
       
    }
    private void muestraDatosPokemon(PokemonValues pkm)
    {//Muestra los datos
       jLabel3.setText(""+pkm.nombre);
       jLabel7.setText("SPECIE:         "+pkm.species);
       jLabel8.setText("HEIGHT:         "+pkm.height +" dm");
       jLabel9.setText("WEIGHT:         "+pkm.weight+ " hg");
       jLabel6.setText("HABITAT:        " +pkm.habitat);
    }
    private void compruebaLimitesListaPokemon(){//Para que salga siempre un pokemon añadimos limites al contador
            if(poke.contador <=0)
            poke.contador= 1;
        else if(poke.contador >=650)
            poke.contador= 649;
    }
    private int contadoresACero(int contadorUsado){//Pone los contadores a 0. ARRAY?
    contadorABC =0;
    contadorDEF =0;
    contadorGHI =0;
    contadorJKL =0;
    contadorMNO =0;
    contadorPQR =0;
    contadorSTU =0;
    contadorVWX =0;
    contadorYZ =0;
    
    return contadorUsado;
    }
    //Los dos siguientes metodos se podrían resumir en uno, pero no quiero tocarlos para no fastidiar el programa el ultimo dia. simplemente se pone un Clip local
    private void suenaBoton()
    {
        poke.botonPulsar.setFramePosition(0);
        poke.botonPulsar.start();
    }
        private void suenaBeep()
    {
        poke.word.setFramePosition(0);
        poke.word.start();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 0, 0));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Wide Latin", 0, 11)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 50, 20));

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Wide Latin", 0, 11)); // NOI18N
        jButton2.setMaximumSize(new java.awt.Dimension(71, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(71, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 50, 20));

        jLabel1.setFont(new java.awt.Font("Showcard Gothic", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nº:");
        jLabel1.setToolTipText("");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 280, -1));

        jLabel2.setFont(new java.awt.Font("Showcard Gothic", 0, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 51, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("POKÉDEX");
        jLabel2.setToolTipText("");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 257, 89));

        jLabel3.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("NAME");
        jLabel3.setToolTipText("");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 290, -1));

        jButton3.setBackground(new java.awt.Color(102, 153, 255));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton3.setText("ABC");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 350, 50, -1));

        jButton4.setBackground(new java.awt.Color(102, 153, 255));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton4.setText("DEF");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 350, -1, -1));

        jButton5.setBackground(new java.awt.Color(102, 153, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton5.setText("GHI");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, -1, -1));

        jButton6.setBackground(new java.awt.Color(102, 153, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton6.setText("PQR");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 380, 50, -1));

        jButton7.setBackground(new java.awt.Color(102, 153, 255));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton7.setText("MNO");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 350, -1, -1));

        jButton8.setBackground(new java.awt.Color(102, 153, 255));
        jButton8.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton8.setText("JKL");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 350, -1, -1));

        jButton9.setBackground(new java.awt.Color(102, 153, 255));
        jButton9.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton9.setText("YZ");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, -1, -1));

        jButton10.setBackground(new java.awt.Color(102, 153, 255));
        jButton10.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton10.setText("VWX");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 380, 50, -1));

        jButton11.setBackground(new java.awt.Color(102, 153, 255));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton11.setText("STU");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 380, 50, -1));

        jLabel4.setBackground(new java.awt.Color(0, 255, 0));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("POKÉMON");
        jLabel4.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 51, 0)));
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, 290, 40));

        jButton12.setBackground(new java.awt.Color(255, 102, 51));
        jButton12.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton12.setText("BUSCAR");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 420, 90, 20));

        jButton13.setBackground(new java.awt.Color(102, 153, 255));
        jButton13.setFont(new java.awt.Font("Tahoma", 1, 6)); // NOI18N
        jButton13.setText("INTRO");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 380, -1, 20));

        jLabel5.setBackground(new java.awt.Color(0, 255, 0));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 51, 0)));
        jLabel5.setOpaque(true);
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 410, 40, 30));

        jButton14.setBackground(new java.awt.Color(0, 204, 0));
        jButton14.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jButton14.setText("REINICIA");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 420, 80, 20));

        jButton15.setBackground(new java.awt.Color(0, 0, 0));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 410, 20, 50));

        jButton16.setBackground(new java.awt.Color(0, 0, 0));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 340, 20, 50));

        jButton18.setBackground(new java.awt.Color(0, 204, 0));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 50, 10));

        jButton17.setBackground(new java.awt.Color(255, 102, 51));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, 50, 10));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(153, 0, 0)));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jPanel1.setMinimumSize(new java.awt.Dimension(236, 193));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 236, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 236, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 240, 240));

        jLabel6.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("HABITAT: ");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 290, 30));

        jLabel7.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("SPECIE: ");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 290, 30));

        jLabel8.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("HEIGHT: ");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 290, 30));

        jLabel9.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("WEIGHT: ");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 180, 290, 30));

        jPanel2.setBackground(new java.awt.Color(0, 255, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(51, 51, 0)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 304, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 310, 270));

        jPanel3.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, -1, -1));

        jLabel11.setBackground(new java.awt.Color(0, 255, 0));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 6)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("EVOLUTION BRANCH");
        jLabel11.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 51, 0)));
        jLabel11.setOpaque(true);
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 480, 80, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents
//CRUCETA IZQUIERDA
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        poke.contador-=10;
        compruebaLimitesListaPokemon();
        suenaBoton();
        mostrarPokemon(poke.contador-1);   
    }//GEN-LAST:event_jButton1ActionPerformed
//CRUCETA DERECHA
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        poke.contador+=10;
        compruebaLimitesListaPokemon();
        suenaBoton();
        mostrarPokemon(poke.contador-1);   
    }//GEN-LAST:event_jButton2ActionPerformed
//Boton ABC
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       contadorABC++;
       
       if(contadorABC > 3 || contadorABC < 0)
       {
           contadorABC = 1;
       }
       if(contadorABC == 1){
           letra = 'A';   
       }else if(contadorABC == 2){
           letra = 'B';   
       } else if(contadorABC == 3){
           letra = 'C';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();
       contadorABC = contadoresACero(contadorABC);
    }//GEN-LAST:event_jButton3ActionPerformed
//BOTON DE INTRO
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        suenaBeep();
        if(numeroLetras_contador == 0){
            numeroLetras_contador++;
            jLabel4.setText(jLabel5.getText());
        } else{
            numeroLetras_contador++;
            jLabel4.setText(jLabel4.getText() + jLabel5.getText());
        }
        jLabel5.setText("");
        contadoresACero(0);
    }//GEN-LAST:event_jButton13ActionPerformed
//BOTONES DE ESCRITURA
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        contadorDEF++;
       
       if(contadorDEF > 3 || contadorDEF < 0)
       {
           contadorDEF = 1;
       }
       if(contadorDEF == 1){
           letra = 'D';   
       }else if(contadorDEF == 2){
           letra = 'E';   
       } else if(contadorDEF == 3){
           letra = 'F';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();
       contadorDEF = contadoresACero(contadorDEF);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       contadorGHI++;
       
       if(contadorGHI > 3 || contadorGHI < 0)
       {
           contadorGHI = 1;
       }
       if(contadorGHI == 1){
           letra = 'G';   
       }else if(contadorGHI == 2){
           letra = 'H';   
       } else if(contadorGHI == 3){
           letra = 'I';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();
       contadorGHI = contadoresACero(contadorGHI);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        contadorJKL++;
       
       if( contadorJKL > 3 ||  contadorJKL < 0)
       {
            contadorJKL = 1;
       }
       if( contadorJKL == 1){
           letra = 'J';   
       }else if( contadorJKL == 2){
           letra = 'K';   
       } else if( contadorJKL == 3){
           letra = 'L';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();
       contadorJKL = contadoresACero(contadorJKL);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        contadorMNO++;
       
       if( contadorMNO > 3 ||  contadorMNO < 0)
       {
            contadorMNO = 1;
       }
       if( contadorMNO == 1){
           letra = 'M';   
       }else if( contadorMNO == 2){
           letra = 'N';   
       } else if( contadorMNO == 3){
           letra = 'O';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();
       contadorMNO = contadoresACero(contadorMNO);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
         contadorPQR++;
       
       if( contadorPQR > 3 ||  contadorPQR < 0)
       {
            contadorPQR = 1;
       }
       if( contadorPQR == 1){
           letra = 'P';   
       }else if( contadorPQR == 2){
           letra = 'Q';   
       } else if( contadorPQR == 3){
           letra = 'R';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();       
       contadorPQR = contadoresACero(contadorPQR);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
         contadorSTU++;
       
       if( contadorSTU > 3 ||  contadorSTU < 0)
       {
            contadorSTU = 1;
       }
       if( contadorSTU == 1){
           letra = 'S';   
       }else if( contadorSTU == 2){
           letra = 'T';   
       } else if( contadorSTU == 3){
           letra = 'U';   
       }
       jLabel5.setText(""+letra);
        suenaBeep();      
       contadorSTU = contadoresACero(contadorSTU);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
         contadorVWX++;
       
       if( contadorVWX > 3 ||  contadorVWX < 0)
       {
            contadorVWX = 1;
       }
       if( contadorVWX == 1){
           letra = 'V';   
       }else if( contadorVWX == 2){
           letra = 'W';   
       } else if( contadorVWX == 3){
           letra = 'X';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();       
       contadorVWX = contadoresACero(contadorVWX);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        contadorYZ++;
       
       if( contadorYZ > 2 ||  contadorYZ< 0)
       {
            contadorYZ = 1;
       }
       if( contadorYZ == 1){
           letra = 'Y';   
       }else if( contadorYZ == 2){
           letra = 'Z';   
       }
       jLabel5.setText(""+letra);
       suenaBeep();       
       contadorYZ = contadoresACero(contadorYZ);
    }//GEN-LAST:event_jButton9ActionPerformed
//REINICIAR
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
       numeroLetras_contador = contadoresACero(0);
       jLabel4.setText("POKÉMON");
       jLabel5.setText("");
       suenaBeep();
    }//GEN-LAST:event_jButton14ActionPerformed
//CRUCETA ARRIBA
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        poke.contador--;
        compruebaLimitesListaPokemon();
        suenaBoton();
        mostrarPokemon(poke.contador-1);   
    }//GEN-LAST:event_jButton16ActionPerformed
//CRUCETA ABAJO
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        poke.contador++;
        compruebaLimitesListaPokemon();
        suenaBoton();
        mostrarPokemon(poke.contador-1);
    }//GEN-LAST:event_jButton15ActionPerformed

//BUSCAR
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        int contador =1;
        boolean encontrado = false;
        String pokemonABuscar = jLabel4.getText();
        PokemonValues pkm = new PokemonValues();
        //Busca al pokemon por nombre ignorando mayus o minus
        while(encontrado == false && contador< 649)
        {
            pkm = listaPokemon.get(String.valueOf(contador));
            if(pkm!=null){
            if(pokemonABuscar.equalsIgnoreCase(pkm.nombre))
                encontrado = true;
            else
                contador++;
            }else 
                contador++;
        }
        if(encontrado){
            mostrarPokemon(contador-1);
            jLabel4.setText("POKÉMON");
        }else 
            jLabel4.setText("NO ENCONTRADO");
        numeroLetras_contador = 0;
            
        suenaBeep();
                
    }//GEN-LAST:event_jButton12ActionPerformed
//QuitarMusica
    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        suenaBeep();
        if(poke.theme1.isRunning()){
            poke.theme1.setFramePosition(0);
            poke.theme1.stop();
        }else
        if(poke.theme2.isRunning()){
            poke.theme2.setFramePosition(0);
            poke.theme2.stop();
        }else
            poke.theme1.start();
        
    }//GEN-LAST:event_jButton17ActionPerformed
//PONER MUSICA
    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
      suenaBeep();
        if(poke.theme1.isRunning()){
            poke.theme1.setFramePosition(0);
            poke.theme1.stop();
            poke.theme2.start();
        } else if(poke.theme2.isRunning()){
            poke.theme2.setFramePosition(0);
            poke.theme2.stop();
            poke.theme1.start();
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPokedex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPokedex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPokedex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPokedex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPokedex().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
