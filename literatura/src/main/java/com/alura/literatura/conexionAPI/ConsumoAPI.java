package com.alura.literatura.conexionAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Component
public class ConsumoAPI {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> buscarLibrosPorTitulo(String tituloBusqueda) {
        try {
            String urlStr = "https://gutendex.com/books?search=" + tituloBusqueda;
            URL url = new URL(urlStr);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String inputLine;
            StringBuilder contenido = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                contenido.append(inputLine);
            }
            in.close();
            conexion.disconnect();

            Map<String, Object> response = objectMapper.readValue(contenido.toString(), Map.class);
            return (List<Map<String, Object>>) response.get("results");

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Lista vacía si falla
        }
    }
}
