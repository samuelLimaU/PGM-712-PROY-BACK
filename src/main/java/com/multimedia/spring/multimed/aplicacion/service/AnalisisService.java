package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.aplicacion.dto.AprioriRuleDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalisisService {

    public List<AprioriRuleDTO> generarReglasApriori(double minSupport, double minConfidence) {
        List<AprioriRuleDTO> reglas = new ArrayList<>();
        
        try {
            // 1. Configurar el proceso para ejecutar el script de Python
            // Usamos 'python' y pasamos el flag '--json' que acabamos de implementar
            ProcessBuilder pb = new ProcessBuilder("python", "G:/MULTIMEDIA 2/analisis_apriori.py", "--json");
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            // 2. Leer la salida del script
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // 3. Parsear el JSON devuelto por Python a nuestra lista de DTOs
                ObjectMapper mapper = new ObjectMapper();
                String jsonResult = output.toString().trim();
                
                // Si hay texto antes del JSON (ej: advertencias), buscamos el inicio del array [
                if (jsonResult.contains("[")) {
                    jsonResult = jsonResult.substring(jsonResult.indexOf("["));
                }
                
                reglas = mapper.readValue(jsonResult, new TypeReference<List<AprioriRuleDTO>>() {});
            } else {
                System.err.println("El script de Python falló con código: " + exitCode);
                System.err.println("Salida: " + output);
            }
            
        } catch (Exception e) {
            System.err.println("Error al ejecutar el puente con Python: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reglas;
    }
}

