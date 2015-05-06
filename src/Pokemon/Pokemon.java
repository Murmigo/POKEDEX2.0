/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokemon;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author xp
 */
public class Pokemon {
    public int contador = 1;
    public int fila = 0;
    public int columna = 0;
    private int pokemonAnchura;
    private int pokemonAltura;
    
    private int x = 0;
    private int y = 0;
    
    public Image pokeImagen;
    public Pokemon()
    {
     
        try {
            pokeImagen = ImageIO.read((getClass().getResource("black-white.png")));
        } catch (IOException ex) {
            Logger.getLogger(Pokemon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void dibujaPokemon(Graphics2D g2)
    {
        g2.drawImage(pokeImagen,x,y,pokemonAnchura,pokemonAltura,columna*96,fila*96,columna*96+96,fila*96+96,null);                              
    }

    public int getPokemonAnchura() {
        return pokemonAnchura;
    }

    public void setPokemonAnchura(int pokemonAnchura) {
        this.pokemonAnchura = pokemonAnchura;
    }

    public int getPokemonAltura() {
        return pokemonAltura;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setPokemonAltura(int pokemonAltura) {
        this.pokemonAltura = pokemonAltura;
    }

}
