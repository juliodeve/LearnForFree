package com.vazquez.julio.proyectofarru;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParserCursos {
    public static List<Curso> parser (String content){

        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Curso> cursosList = new ArrayList<>();

            for (int i =0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Curso usuario = new Curso();

                usuario.setNombre(jsonObject.getString("NombreProd"));
                usuario.setImagen(jsonObject.getString("Imagen"));
                usuario.setMarca(jsonObject.getString("Marca"));
                usuario.setPrecio(jsonObject.getDouble("Precio"));
                usuario.setCodigo(jsonObject.getString("CodigoProd"));

                cursosList.add(usuario);
            }
            return cursosList;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
