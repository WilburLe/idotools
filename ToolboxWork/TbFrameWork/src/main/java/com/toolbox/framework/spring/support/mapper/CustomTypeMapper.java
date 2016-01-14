package com.toolbox.framework.spring.support.mapper;

import java.util.ArrayList;
import java.util.List;


public class CustomTypeMapper {
    public static List<CustomTypeProcesor> getProcesors() {
        List<CustomTypeProcesor> procesors = new ArrayList<CustomTypeProcesor>();
        procesors.add(new JsonlibCustomTypeProcesor());
        procesors.add(new BaseEnumCustomTypeProcesor());
        return procesors;
    }
}
