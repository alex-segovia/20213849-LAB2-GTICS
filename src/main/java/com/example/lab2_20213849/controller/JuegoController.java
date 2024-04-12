package com.example.lab2_20213849.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class JuegoController {

    @GetMapping(value = "/buscaminas")
    public String buscaminas(){
        return "buscaminas";
    }

    @PostMapping(value = "/jugar")
    public String juego(Model model, @RequestParam(name = "posiciones") String posiciones){
        model.addAttribute("posiciones",posiciones);
        ArrayList<Integer> posicion1 = new ArrayList<>();
        ArrayList<Integer> posicion2 = new ArrayList<>();
        ArrayList<Integer> posicion3 = new ArrayList<>();
        ArrayList<Integer> posicion4 = new ArrayList<>();
        ArrayList<Integer> posicion5 = new ArrayList<>();

        int p1 = Integer.parseInt(posiciones.split(" ")[0].split(",")[0].split("\\(")[1]);
        int p2 = Integer.parseInt(posiciones.split(" ")[0].split(",")[1].split("\\)")[0]);
        posicion1.add(p1-1);
        posicion1.add(p2-1);

        int p3 = Integer.parseInt(posiciones.split(" ")[1].split(",")[0].split("\\(")[1]);
        int p4 = Integer.parseInt(posiciones.split(" ")[1].split(",")[1].split("\\)")[0]);
        posicion2.add(p3-1);
        posicion2.add(p4-1);

        int p5 = Integer.parseInt(posiciones.split(" ")[2].split(",")[0].split("\\(")[1]);
        int p6 = Integer.parseInt(posiciones.split(" ")[2].split(",")[1].split("\\)")[0]);
        posicion3.add(p5-1);
        posicion3.add(p6-1);

        int p7 = Integer.parseInt(posiciones.split(" ")[3].split(",")[0].split("\\(")[1]);
        int p8 = Integer.parseInt(posiciones.split(" ")[3].split(",")[1].split("\\)")[0]);
        posicion4.add(p7-1);
        posicion4.add(p8-1);

        int p9 = Integer.parseInt(posiciones.split(" ")[4].split(",")[0].split("\\(")[1]);
        int p10 = Integer.parseInt(posiciones.split(" ")[4].split(",")[1].split("\\)")[0]);
        posicion5.add(p9-1);
        posicion5.add(p10-1);

        int[][] tableroJuego = generarTablero(posicion1,posicion2,posicion3,posicion4,posicion5);

        model.addAttribute("tablero",tableroJuego);
        return "juego";
    }

    @PostMapping(value = "/minar")
    public String minar(@RequestParam(name = "tablero")int[][] tablero, @RequestParam(name = "posiciones") String posiciones, @RequestParam(name = "coordenada") String coordenada){
        explotar(tablero,Integer.parseInt(coordenada.split(" ")[0])-1,Integer.parseInt(coordenada.split(" ")[1])-1);
        return "forward:/jugar";
    }

    public void explotar(int[][] tablero, int coordenada1, int coordenada2){
        for(int i=0;i<6;i++) {
            for (int j = 0; j < 6; j++) {
                if(i==coordenada1 && j == coordenada2){
                    tablero[i][j] += 100;
                }
            }
        }
    }

    public int[][] generarTablero(ArrayList<Integer> posicion1,ArrayList<Integer> posicion2,ArrayList<Integer> posicion3,ArrayList<Integer> posicion4,ArrayList<Integer> posicion5){
        int[][] tablero = new int[6][6];

        int fila1 = posicion1.get(0);
        int columna1 = posicion1.get(1);
        int fila2 = posicion2.get(0);
        int columna2 = posicion2.get(1);
        int fila3 = posicion3.get(0);
        int columna3 = posicion3.get(1);
        int fila4 = posicion4.get(0);
        int columna4 = posicion4.get(1);
        int fila5 = posicion5.get(0);
        int columna5 = posicion5.get(1);

        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                if((fila1==i && columna1==j) || (fila2==i && columna2==j) || (fila3==i && columna3==j) || (fila4==i && columna4==j) || (fila5==i && columna5==j)){
                    tablero[i][j] = -1;
                }else{
                    tablero[i][j] = 0;
                }
            }
        }

        int [][] tablero2 = new int[6][6];

        for(int i=0;i<6;i++){
            for (int j=0;j<6;j++){
                if((fila1==i && columna1==j) || (fila2==i && columna2==j) || (fila3==i && columna3==j) || (fila4==i && columna4==j) || (fila5==i && columna5==j)){
                    continue;
                }
                int cantidad = 0;
                if(j-1>=0){
                    cantidad += tablero[i][j-1];
                    if(i-1>=0){
                        cantidad += tablero[i-1][j-1] + tablero[i-1][j];
                    }
                    if(i+1<=5){
                        cantidad += tablero[i+1][j-1] + tablero[i+1][j];
                    }
                }
                if(j+1<=5){
                    cantidad += tablero[i][j+1];
                    if(i-1>=0){
                        cantidad += tablero[i-1][j+1];
                    }
                    if(i+1<=5){
                        cantidad += tablero[i+1][j+1];
                    }
                }
                tablero2[i][j] = -1*cantidad;
            }
        }

        for(int i=0;i<6;i++){
            for (int j=0;j<6;j++) {
                tablero[i][j] += tablero2[i][j];
            }
        }
        return tablero;
    }
}
