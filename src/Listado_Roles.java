import java.util.ArrayList;

public class Listado_Roles {
    ArrayList<Rol>lista;

    public Listado_Roles(){
        lista=new ArrayList();
    }

    public void AgregarRol(Rol r){
        lista.add(r);
    }

}
