package com.example.demo.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import javax.management.StringValueExp;

public class TagTypeConverter implements Converter<String> {


    @Override
    public Class supportJavaTypeKey() {
        return String.class;

    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;

    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        // tagType 17 -> 50
        if("17".equals(stringValue)){
            return "50";
        }
        // tagType 18 -> 51
        if("18".equals(stringValue)){
            return "51";
        }
        // tagType 19 -> 51
        if("19".equals(stringValue)){
            return "52";
        }
        return null;
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }


}
