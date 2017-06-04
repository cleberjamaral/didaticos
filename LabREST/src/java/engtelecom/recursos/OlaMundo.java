/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engtelecom.recursos;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author cleber
 */
@Path("olamundo")
public class OlaMundo {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of OlaMundo
     */
    public OlaMundo() {

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String olaTexto() {
        return "Ola Mundo!";
    }

    /*
     * Via curl será necessário especificar o formato
     * curl -H "Accept: application/xml" -X GET http://localhost:8080/LabREST/webresources/olamundo/
     * Ou simplesmente...
     * curl -H "Accept: application/xml" http://localhost:8080/LabREST/webresources/olamundo/
     * Via browser: http://localhost:8080/LabREST/webresources/olamundo/
    */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response olaXml() {
        try {
            Contato c = new Contato("joao","123456","joao@email.com");
            //return "<?xml version=\"1.0\"?>" + "<mensagem>Ola Mundo" + "</mensagem>";
            return Response.ok(c).build();
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * PUT method for updating or creating an instance of OlaMundo
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    /*
     * Via curl para forçar o get do json:
     * curl -H "accept:application/json" -X GET http://localhost:8080/LabREST/webresources/olamundo/

     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response olaJSON() {
        Contato c = new Contato("Joao","123456","joao@email.com");
        //return "<?xml version=\"1.0\"?>" + "<mensagem>" + nome + "</mensagem>";
        return Response.ok(c, MediaType.APPLICATION_JSON).build();
        //return "{ \"mensagem\": \"Ola Mundo\" }";
    }

    /*
     * Via curl será necessário especificar o formato
     * curl -H "Accept: application/xml" -X GET http://localhost:8080/LabREST/webresources/olamundo/joao
     * Ou simplesmente...
     * curl -H "Accept: application/xml" http://localhost:8080/LabREST/webresources/olamundo/joao
     * Via browser: http://localhost:8080/LabREST/webresources/olamundo/joao
    */
    @Path("/{nome}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response olaOutroXML(@PathParam("nome") String nome) {
        Contato c = new Contato(nome,"123456","joao@email.com");
        //return "<?xml version=\"1.0\"?>" + "<mensagem>" + nome + "</mensagem>";
        return Response.ok(c).build();
    }

    @Path("/{nome}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String olaOutroJSON(@PathParam("nome") String nome) {
        return "{ \"mensagem\": \"" + nome + "\" }";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_XML)
    public String receberParametroViaFORM(@FormParam("nome") String nome) {
        try {
            return "<?xml version=\"1.0\"?>" + "<mensagem>" + nome + "</mensagem>";
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }
}
