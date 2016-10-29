package cz.fidentis.renderer;

import LocalAreas.Area;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;




/**
 *
 * @author zanri
 */
public class LocalAreasRender{
    private Boolean isSetUp;
    private List<Area> area;
    
    public LocalAreasRender(){
        this.isSetUp = false;
        area = new ArrayList<>();
    }
    
    public void SetUp(List<Area> area){
        this.area = area;
        this.isSetUp = true;
    }
    
    public Boolean IsSetUp(){
        return isSetUp;
    }

    public void HideLocalAreas(){
        this.isSetUp = false;
    }
    
    public GL2 DrawLocalAreas(GL2 gl){
        return gl;
    }
    
}
