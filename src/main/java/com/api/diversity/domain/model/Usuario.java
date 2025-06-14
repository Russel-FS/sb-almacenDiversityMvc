package com.api.diversity.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "Usuarios") 
public class Usuario {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "ID_Usuario") 
    private Integer idUsuario; 

    @Column(name = "Nombre_Usuario", nullable = false, length = 50) 
    private String nombreUsuario;

    @Column(name = "Rol", nullable = false, length = 50) 
    private String rol;

    @Column(name = "Contraseña", nullable = false, length = 255) 
    private String contrasena;


    // --- Constructores ---
   
    public Usuario() {
    }

  
    public Usuario(String nombreUsuario, String rol, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.contrasena = contrasena;
    }


    // --- Métodos Getters y Setters ---
   

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}