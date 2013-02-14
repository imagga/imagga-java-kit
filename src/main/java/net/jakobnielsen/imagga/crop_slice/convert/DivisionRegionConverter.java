package net.jakobnielsen.imagga.crop_slice.convert;

import net.jakobnielsen.imagga.convert.Converter;
import net.jakobnielsen.imagga.convert.ConverterException;
import net.jakobnielsen.imagga.crop_slice.bean.DivisionRegion;
import net.jakobnielsen.imagga.crop_slice.bean.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

import static net.jakobnielsen.imagga.convert.ConverterTools.getLong;
import static net.jakobnielsen.imagga.convert.ConverterTools.getString;

public class DivisionRegionConverter implements Converter<List<DivisionRegion>> {

    public static final String DIVISION_REGIONS = "division_regions";

    public static final String REGIONS = "regions";

    @Override
    public List<DivisionRegion> convert(String jsonString) {
        if (jsonString == null) {
            throw new ConverterException("The given JSON string is null");
        }

        JSONObject json = (JSONObject) JSONValue.parse(jsonString);
        if (!json.containsKey(DIVISION_REGIONS)) {
            throw new ConverterException(DIVISION_REGIONS + " key missing from json : " + jsonString);
        }
        JSONArray jsonArray = (JSONArray) json.get(DIVISION_REGIONS);
        List<DivisionRegion> divisionRegions = new ArrayList<DivisionRegion>();

        for (Object aJsonArray : jsonArray) {

            JSONObject divisionRegionObject = (JSONObject) aJsonArray;

            String url = getString("url", divisionRegionObject);

            List<Region> regions = new ArrayList<Region>();

            if (divisionRegionObject.containsKey(REGIONS)) {

                if (divisionRegionObject.get(REGIONS) != null &&
                        divisionRegionObject.get(REGIONS) instanceof JSONArray) {
                    JSONArray regionsArrays = (JSONArray) divisionRegionObject.get(REGIONS);

                    for (Object aRegionsArray : regionsArrays) {

                        JSONObject regionObject = (JSONObject) aRegionsArray;

                        regions.add(new Region(
                                getLong("x1", regionObject),
                                getLong("y1", regionObject),
                                getLong("x2", regionObject),
                                getLong("y2", regionObject)
                        ));
                    }
                    divisionRegions.add(new DivisionRegion(url, regions));
                }
            }
        }

        return divisionRegions;
    }
}