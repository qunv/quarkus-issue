package org.acme.getting.started;

import org.acme.getting.started.export.ExcelExportUtility;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Path("/")
public class GreetingResource {

    @GET
    @Path("/passive")
    public Response issue() throws IllegalAccessException {
        List<Issue> revList = new ArrayList<>();
        var issue = new Issue();
        issue.setAlive("true");
        issue.setData("123");
        revList.add(issue);

        StreamingOutput output = outStream -> {
            XSSFWorkbook wb = null;
            try {
                wb = ExcelExportUtility.exportExcel(new String[]{"Alive", "Data"}, revList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            wb.write(outByteStream);
            byte[] outArray = outByteStream.toByteArray();
            outStream.write(outArray);
            outStream.flush();
            outStream.close();
            wb.close();
        };

        return Response.ok(output)
                .header("Content-Disposition", "attachment; filename=export.xlsx")
                .build();
    }
}