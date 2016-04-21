package qualteh.com.scrollviewprototype.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import qualteh.com.scrollviewprototype.Consts;

/**
 * Created by Virgil Tanase on 20.04.2016.
 */
public class Commission {

    private Map additionalProperties;
    private List<Double> coordinates = new ArrayList<>(  );
    private String id;
    private String strokeColor;
    private Integer strokeWidth;

    public void adjustCoordinates(double d)
    {
        for (int i = 0; i < coordinates.size(); i++)
        {
            coordinates.set(i, Double.valueOf(((Double)coordinates.get(i)).doubleValue() * d * Consts.SIZE_MULTIPLIER));
        }

        for (int j = 1; j < coordinates.size(); j += 2)
        {
            coordinates.set(j, Double.valueOf(((double)Consts.DIAGRAM_HEIGHT * Consts.SIZE_MULTIPLIER * d - ((Double)coordinates.get(j)).doubleValue()) + 10D * Consts.SIZE_MULTIPLIER * d));
        }

    }

    public void adjustCoordinates(int i)
    {
        for (int j = 0; j < coordinates.size(); j++)
        {
            coordinates.set(j, Double.valueOf(((Double)coordinates.get(j)).doubleValue() * (double)i * Consts.SIZE_MULTIPLIER));
        }

        for (int k = 1; k < coordinates.size(); k += 2)
        {
            coordinates.set(k, Double.valueOf(((double)Consts.DIAGRAM_HEIGHT * Consts.SIZE_MULTIPLIER * (double)i - ((Double)coordinates.get(k)).doubleValue()) + 10D * Consts.SIZE_MULTIPLIER * (double)i));
        }

    }

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
