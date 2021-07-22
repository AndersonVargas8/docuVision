package gui.servicios.serviciosLogicos;

import datos.Documento;
import estructuras.arboles.AVL;
import estructuras.listas.ListaEncadenadaSimple;
import logica.ControlDocumentos;

public class DocumentosService {
    private static DocumentosService servicio;
    private ControlDocumentos cDocumentos;
    private AVL<Documento> documentos;
    private ListaEncadenadaSimple<Documento> elementos;
    private ListaEncadenadaSimple<Documento> impresion;
    private Documento documento;
    public DocumentosService(){
        cDocumentos = new ControlDocumentos();
        documentos = cDocumentos.getDocumentos();
        elementos = documentos.inOrden();
    }

    public static DocumentosService getServicio(){
        if(servicio == null)
            servicio = new DocumentosService();

        return servicio;
    }

    public Documento devolverDocumento(int id){
        return (Documento)elementos.leerDato(id);
    }

    public Documento getDocumento(int id){
        if(documento == null)
            this.documento = new Documento();
        documento.setId(id);
        return documentos.buscar(documento);
    }

    public Documento devolverMayorDocumento(){
        return documentos.buscarMax();
    }

    public void agregarDocumento(Documento documento){
        this.documentos.insertar(documento);
        this.elementos = documentos.inOrden();
    }

    public int devolverCantidadDocumentos(){
        return this.documentos.cantidadNodos();
    }

    public void eliminarDocumento(Documento documento){
        this.documentos.eliminar(documento);
    }

    public ListaEncadenadaSimple imprimirTodo(){
        impresion = documentos.inOrden();
        return impresion;
    }

    public void nuevoFiltro(){
        if(this.impresion == null)
            this.impresion = new ListaEncadenadaSimple<>();
        elementos = documentos.inOrden();
        impresion.vaciar();
    }

    public void agregarAImpresion(Documento documento){
        this.impresion.insertarAlFinal(documento);
    }

    public ListaEncadenadaSimple<Documento> getImpresion(){
        return this.impresion;
    }
}
