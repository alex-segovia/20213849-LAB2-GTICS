package com.example.lab2_20213849.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class JuegoController {
    private int intentos;
    private int cantBombas;
    private int cantMinasDescubiertas;
    private int[][] tablero;
    private int cantidadFilas;
    private int cantidadColumnas;
    private int casillaActual;
    private int estadoJuego;
    @GetMapping(value = "/buscaminas")
    public String buscaminas(){
        return "buscaminas";
    }

    @PostMapping(value = "/jugar")
    public String juego(Model model,
                        @RequestParam(name = "posiciones") String posiciones,
                        @RequestParam(name = "filas")String fila,
                        @RequestParam(name = "columnas")String columna,
                        @RequestParam(name = "intentos")String cantidadIntentos,
                        @RequestParam(name = "bombas")String cantidadBombas){
        tablero = new int[Integer.parseInt(fila)][Integer.parseInt(columna)];
        intentos = Integer.parseInt(cantidadIntentos);
        cantidadFilas=Integer.parseInt(fila);
        cantidadColumnas=Integer.parseInt(columna);
        cantBombas=Integer.parseInt(cantidadBombas);
        cantMinasDescubiertas=0;
        estadoJuego=0;
        ArrayList<ArrayList<Integer>> posicionesBombas = new ArrayList<>();

        for(int i=0;i<Integer.parseInt(cantidadBombas);i++){
            ArrayList<Integer> posicion = new ArrayList<>();
            posicion.add(Integer.parseInt(posiciones.split(" ")[i].split(",")[0].split("\\(")[1])-1);
            posicion.add(Integer.parseInt(posiciones.split(" ")[i].split(",")[1].split("\\)")[0])-1);
            posicionesBombas.add(posicion);
        }

        generarTablero(posicionesBombas);
        model.addAttribute("tablero",tablero);
        model.addAttribute("filas",cantidadFilas);
        model.addAttribute("columnas",cantidadColumnas);
        model.addAttribute("estadoJuego",estadoJuego);
        return "juego";
    }

    @PostMapping(value = "/minar")
    public String minar(Model model, @RequestParam(name = "coordenada") String coordenada){
        explotar(Integer.parseInt(coordenada.split(" ")[0])-1,Integer.parseInt(coordenada.split(" ")[1])-1);
        model.addAttribute("tablero",tablero);
        model.addAttribute("filas",cantidadFilas);
        model.addAttribute("columnas",cantidadColumnas);
        String mensaje = "";

        if(casillaActual==0){
            mensaje = "";
        }else{
            mensaje = "Encontró una bomba, le queda(n) " + intentos + " intento(s) !";
        }

        if(intentos==0){
            mensaje="Usted ha perdido el juego !";
            estadoJuego=1;
        }

        if(cantidadColumnas*cantidadFilas-cantBombas == cantMinasDescubiertas){
            mensaje="Usted ha ganado el juego !";
            estadoJuego=1;
        }
        model.addAttribute("estadoJuego",estadoJuego);
        model.addAttribute("mensaje",mensaje);
        return "juego";
    }

    public void explotar(int coordenada1, int coordenada2){
        buclePrincipal:
        for(int i=0;i<cantidadFilas;i++) {
            for (int j = 0; j < cantidadColumnas; j++) {
                if(i==coordenada1 && j == coordenada2){
                    if(tablero[i][j]>=99){
                        break buclePrincipal;
                    }
                    if(tablero[i][j]==-1){
                        intentos -= 1;
                        casillaActual = -1;
                    }else{
                        casillaActual = 0;
                    }
                    tablero[i][j] += 100;
                    cantMinasDescubiertas += 1;
                    if(tablero[i][j]==100){
                        if(j-1>=0){
                            if(tablero[i][j-1]<99){
                                explotar(i,j-1);
                            }
                            if(i-1>=0){
                                if(tablero[i-1][j-1]<99){
                                    explotar(i-1,j-1);
                                }
                            }
                            if(i+1<=cantidadFilas-1){
                                if(tablero[i+1][j-1]<99){
                                    explotar(i+1,j-1);
                                }
                            }
                        }
                        if(i-1>=0){
                            if(tablero[i-1][j]<99){
                                explotar(i-1,j);
                            }
                        }
                        if(i+1<=cantidadFilas-1){
                            if(tablero[i+1][j]<99){
                                explotar(i+1,j);
                            }
                        }
                        if(j+1<=cantidadColumnas-1){
                            if(tablero[i][j+1]<99){
                                explotar(i,j+1);
                            }
                            if(i-1>=0){
                                if(tablero[i-1][j+1]<99){
                                    explotar(i-1,j+1);
                                }
                            }
                            if(i+1<=cantidadFilas-1){
                                if(tablero[i+1][j+1]<99){
                                    explotar(i+1,j+1);
                                }
                            }
                        }
                    }
                    break buclePrincipal;
                }
            }
        }
    }

    public void generarTablero(ArrayList<ArrayList<Integer>> posicionesBombas){
        for(int i=0; i<cantidadFilas; i++){
            for(int j=0; j<cantidadColumnas; j++){
                int valorBomba = 0;
                for(ArrayList<Integer> coordenadas: posicionesBombas){
                    if(coordenadas.get(0)==i && coordenadas.get(1)==j){
                        valorBomba = -1;
                        break;
                    }
                }
                tablero[i][j] = valorBomba;
            }
        }

        int [][] tablero2 = new int[cantidadFilas][cantidadColumnas];

        for(int i=0;i<cantidadFilas;i++){
            bucle:
            for (int j=0;j<cantidadColumnas;j++){
                for(ArrayList<Integer> coordenadas: posicionesBombas){
                    if(coordenadas.get(0)==i && coordenadas.get(1)==j){
                        continue bucle;
                    }
                }
                int cantidad = 0;
                if(j-1>=0){
                    cantidad += tablero[i][j-1];
                    if(i-1>=0){
                        cantidad += tablero[i-1][j-1];
                    }
                    if(i+1<=cantidadFilas-1){
                        cantidad += tablero[i+1][j-1];
                    }
                }

                if(i-1>=0){
                    cantidad += tablero[i-1][j];
                }
                if(i+1<=cantidadFilas-1){
                    cantidad += tablero[i+1][j];
                }

                if(j+1<=cantidadColumnas-1){
                    cantidad += tablero[i][j+1];
                    if(i-1>=0){
                        cantidad += tablero[i-1][j+1];
                    }
                    if(i+1<=cantidadFilas-1){
                        cantidad += tablero[i+1][j+1];
                    }
                }
                tablero2[i][j] = -1*cantidad;
            }
        }

        for(int i=0;i<cantidadFilas;i++){
            for (int j=0;j<cantidadColumnas;j++) {
                tablero[i][j] += tablero2[i][j];
            }
        }
    }
}
