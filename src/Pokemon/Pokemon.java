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
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author xp
 */
public class Pokemon {
    public int contador = 0;
    public int fila = 0;
    public int columna = 0;
    private int pokemonAnchura;
    private int pokemonAltura;
    
    private int x = 0;
    private int y = 0;
    
    public Image pokeImagen;
    Clip theme1;
    Clip theme2;
    Clip botonPulsar;
    Clip word;
    public Pokemon()
    {
        try {
            
        } catch (Exception ex) {
        }
            
        try {
            pokeImagen = ImageIO.read((getClass().getResource("black-white.png")));
            theme1 = AudioSystem.getClip();
            theme1.open(AudioSystem.getAudioInputStream(getClass().getResource("menu.wav")));
             theme2 = AudioSystem.getClip();
            theme2.open(AudioSystem.getAudioInputStream(getClass().getResource("Kecleon.wav")));
            botonPulsar = AudioSystem.getClip();
            botonPulsar.open(AudioSystem.getAudioInputStream(getClass().getResource("button.wav")));
            word = AudioSystem.getClip();
            word.open(AudioSystem.getAudioInputStream(getClass().getResource("word.wav")));
        } catch (Exception ex) {
        }
        
    }
    
    public void dibujaPokemon(Graphics2D g2)
    {
        g2.drawImage(pokeImagen,x,y,pokemonAnchura,pokemonAltura,columna*96,fila*96,columna*96+96,fila*96+96,null);                              
    }
        public void dibujaEvo(Graphics2D g2)
        {
        g2.drawImage(pokeImagen,0,0,pokemonAnchura,pokemonAltura,columna*96,fila*96,columna*96+96,fila*96+96,null);
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
