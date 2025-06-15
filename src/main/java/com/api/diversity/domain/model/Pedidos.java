package com.api.diversity.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "pedidos")

@AllArgsConstructor
@NoArgsConstructor

public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Pedido")
    private Long ID_Pedido;

    @Column(name = "ID_Cliente")
    private Integer ID_Cliente;
    @Column(name = "Fecha_Pedido")
    private LocalDateTime Fecha_Pedido;
    @Column(name = "Total_Pedido")  
    private BigDecimal Total_Pedido;
    @Column(name = "Estado_Pedido")
    private String Estado_Pedido;
    @Column(name = "ID_Usuario")
    private Integer ID_Usuario;

}