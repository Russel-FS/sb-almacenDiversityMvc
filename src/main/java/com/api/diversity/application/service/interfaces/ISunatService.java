package com.api.diversity.application.service.interfaces;

import com.api.diversity.application.dto.SunatResponseDto;

public interface ISunatService {

    /**
     * Genera una boleta electrónica para persona natural
     * 
     * @param numeroDocumento Número del documento interno
     * @param clienteNombre   Nombre del cliente
     * @param clienteDni      DNI del cliente
     * @param totalVenta      Total de la venta
     * @return Respuesta de SUNAT con código de autorización
     */
    SunatResponseDto generarBoletaElectronica(String numeroDocumento,
            String clienteNombre,
            String clienteDni,
            double totalVenta);

    /**
     * Verifica si el sistema tiene credenciales SUNAT configuradas
     * 
     * @return true si tiene credenciales válidas
     */
    boolean tieneCredencialesSUNAT();
}