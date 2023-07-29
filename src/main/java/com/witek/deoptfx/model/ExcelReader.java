package com.witek.deoptfx.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.Iterator;
public class ExcelReader
{

    public static ObjectProperties[] getObjectPropertiesExcel(String path)
    {
        ObjectProperties[] tableOfObjectProperties = null;
        try
        {
            File file = new File(path);   //creating a new file instance
            //FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(file);
            tableOfObjectProperties = new ObjectProperties[wb.getNumberOfSheets()];
              //iterating over excel file
            int numberOfSheet = 0;
            for(int i = 0 ; i < wb.getNumberOfSheets() ; i++) {
                ObjectProperties tempObj = new ObjectProperties();
                XSSFSheet sheet = wb.getSheetAt(i);     //creating a Sheet object to retrieve object
                Iterator<Row> itr = sheet.iterator();
                itr.next(); //Skip row of titles
                int numberOfRow = 0;
                while (itr.hasNext()) {
                    Row row = itr.next();
                    Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                    int numberOfCell = 0;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case NUMERIC:    //field that represents number cell type
                                if(numberOfCell==0) tempObj.epsilon[numberOfRow] = cell.getNumericCellValue();
                                if(numberOfCell==1) tempObj.sigma[numberOfRow] = cell.getNumericCellValue();
                                if(numberOfCell==3) tempObj.temperature = cell.getNumericCellValue();
                                if(numberOfCell==4) tempObj.dot_epsilon = cell.getNumericCellValue();
                                break;
                            default:
                        }
                    numberOfCell++;
                    }
                    numberOfRow++;
                }
                tableOfObjectProperties[numberOfSheet]=tempObj;
                numberOfSheet++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return tableOfObjectProperties;
    }
}