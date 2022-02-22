package com.example.linkshare.Models;

public class Enlaces {

    private String titulo;
    private String descripcion;
    private String img;
    private String url;


    public Enlaces(){}

    public Enlaces(String titulo, String descripcion, String img, String url) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.img = img;
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImg() {
        return img;
    }

    public String getUrl() {
        return url;
    }


}
