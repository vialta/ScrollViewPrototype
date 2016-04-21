package qualteh.com.scrollviewprototype.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Virgil Tanase on 20.04.2016.
 */
public class Comission {

    private Map additionalProperties;
    private List<Double> coordinates = new ArrayList<>(  );
    private String id;
    private String strokeColor;
    private Integer strokeWidth;

    public Map getAdditionalProperties () {
        return additionalProperties;
    }

    public void setAdditionalProperties ( Map additionalProperties ) {
        this.additionalProperties = additionalProperties;
    }

    public List<Double> getCoordinates () {
        return coordinates;
    }

    public void setCoordinates ( List<Double> coordinates ) {
        this.coordinates = coordinates;
    }

    public String getId () {
        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }

    public String getStrokeColor () {
        return strokeColor;
    }

    public void setStrokeColor ( String strokeColor ) {
        this.strokeColor = strokeColor;
    }

    public Integer getStrokeWidth () {
        return strokeWidth;
    }

    public void setStrokeWidth ( Integer strokeWidth ) {
        this.strokeWidth = strokeWidth;
    }
}
