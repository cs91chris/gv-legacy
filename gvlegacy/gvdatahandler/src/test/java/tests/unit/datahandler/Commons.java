/*******************************************************************************
 * Copyright (c) 2009, 2016 GreenVulcano ESB Open Source Project.
 * All rights reserved.
 *
 * This file is part of GreenVulcano ESB.
 *
 * GreenVulcano ESB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GreenVulcano ESB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GreenVulcano ESB. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package tests.unit.datahandler;

import java.sql.Connection;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import it.greenvulcano.gvesb.j2ee.JNDIHelper;

public class Commons {
    
    static {
        JDBCDataSource dataSource = new JDBCDataSource();

        dataSource.setDatabase("jdbc:hsqldb:mem:testdb");
         
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind("java:comp/env/jdbc/testDHDataSource", dataSource);
        try {
            builder.activate();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
   public static Connection getConnection() throws Exception {
        
        Connection c = null;
        
        JNDIHelper context = new JNDIHelper();
        try {
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/testDHDataSource");
            c = ds.getConnection();
        } finally {
            context.close();
        }
        
        return c;
    }

    public static void createDB() throws Exception {

        Connection connection = getConnection();
        
        connection.prepareStatement("create table testtable (id INTEGER primary key, field1 VARCHAR(30), field2 TIMESTAMP, field3 NUMERIC(8,3));").execute();
        connection.prepareStatement("insert into testtable (id, field1, field2, field3) values (1, 'testvalue', '2000-01-01 12:30:45', 123.456);").execute();

        connection.prepareStatement("create table testtablemulti (id INTEGER primary key, field1 VARCHAR(30), field2 TIMESTAMP, field3 NUMERIC(8,3), field4 DATE, field5 TIME, field6 BOOLEAN, field7 SMALLINT, field8 BIGINT, field9 FLOAT, field10 DOUBLE);")
                  .execute();
        connection.prepareStatement("insert into testtablemulti (id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10) values (1, 'testvalue', '2000-01-01 12:30:45', 123.456, '2000-01-01', '12:30:45', TRUE, 30000, 123456789012345, 3.1234567890, 4.123456789012345);")
                  .execute();

    }

    public static void clearDB() throws Exception {

        Connection connection = getConnection();
        
        connection.prepareStatement("drop table testtable;").execute();
        connection.prepareStatement("drop table testtablemulti;").execute();
    }

    public static Document createInsertMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        data.appendChild(row);
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("2"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue2"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col4);

        row = doc.createElement("row");
        data.appendChild(row);
        col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("3"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue3"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("10/01/2011 00:00:00"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ".");
        col4.setAttribute("grouping-separator", ",");
        col4.setAttribute("number-format", "#0.00");
        col4.appendChild(doc.createTextNode("5.12"));
        row.appendChild(col4);
        return doc;
    }

    public static Document createInsertMultiMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        data.appendChild(row);
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("2"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue2"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col4);
        Element col5 = doc.createElement("col");
        col5.setAttribute("type", "date");
        col5.setAttribute("format", "dd/MM/yyyy");
        col5.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(col5);
        Element col6 = doc.createElement("col");
        col6.setAttribute("type", "time");
        col6.setAttribute("format", "HH:mm:ss");
        col6.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(col6);
        Element col7 = doc.createElement("col");
        col7.setAttribute("type", "boolean");
        col7.appendChild(doc.createTextNode("on"));
        row.appendChild(col7);
        Element col8 = doc.createElement("col");
        col8.setAttribute("type", "small-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(col8);
        Element col9 = doc.createElement("col");
        col9.setAttribute("type", "big-int");
        col9.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(col9);
        Element col10 = doc.createElement("col");
        col10.setAttribute("type", "float");
        col10.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(col10);
        Element col11 = doc.createElement("col");
        col11.setAttribute("type", "double");
        col11.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(col11);

        row = doc.createElement("row");
        data.appendChild(row);
        col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("3"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue3"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("10/01/2011 00:00:00"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "float");
        col4.setAttribute("decimal-separator", ".");
        col4.setAttribute("grouping-separator", ",");
        col4.setAttribute("number-format", "#0.00");
        col4.appendChild(doc.createTextNode("5.12"));
        row.appendChild(col4);
        col5 = doc.createElement("col");
        col5.setAttribute("type", "date");
        col5.setAttribute("format", "dd/MM/yyyy");
        col5.appendChild(doc.createTextNode("10/01/2011"));
        row.appendChild(col5);
        col6 = doc.createElement("col");
        col6.setAttribute("type", "time");
        col6.setAttribute("format", "HH:mm:ss");
        col6.appendChild(doc.createTextNode("00:00:00"));
        row.appendChild(col6);
        col7 = doc.createElement("col");
        col7.setAttribute("type", "boolean");
        col7.appendChild(doc.createTextNode("TRUE"));
        row.appendChild(col7);
        col8 = doc.createElement("col");
        col8.setAttribute("type", "small-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(col8);
        col9 = doc.createElement("col");
        col9.setAttribute("type", "big-int");
        col9.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(col9);
        col10 = doc.createElement("col");
        col10.setAttribute("type", "float");
        col10.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(col10);
        col11 = doc.createElement("col");
        col11.setAttribute("type", "double");
        col11.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(col11);
        return doc;
    }

    public static Document createInsertNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("6"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue6"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);

        row = doc.createElement("row");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("7"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue7"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/01/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);
        return doc;
    }

    public static Document createInsertMultiNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("6"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue6"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);
        Element field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(field4);
        Element field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(field5);
        Element field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("on"));
        row.appendChild(field6);
        Element field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(field7);
        Element field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(field8);
        Element field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(field9);
        Element field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(field10);

        row = doc.createElement("row");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("7"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue7"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/01/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);
        field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("10/01/2011"));
        row.appendChild(field4);
        field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("00:00:00"));
        row.appendChild(field5);
        field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("1"));
        row.appendChild(field6);
        field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(field7);
        field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(field8);
        field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(field9);
        field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(field10);
        return doc;
    }

    public static Document createUpdateNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("6"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue6b"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/11/2010 23:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);

        row = doc.createElement("row");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("7"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue7b"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/02/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);
        return doc;
    }

    public static Document createUpdateMultiNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("6"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue6b"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/11/2010 12:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);
        Element field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("31/11/2010"));
        row.appendChild(field4);
        Element field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("12:59:59"));
        row.appendChild(field5);
        Element field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("off"));
        row.appendChild(field6);
        Element field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(field7);
        Element field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(field8);
        Element field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(field9);
        Element field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(field10);

        row = doc.createElement("row");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("7"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue7b"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/02/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);
        field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("10/02/2011"));
        row.appendChild(field4);
        field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("15:00:15"));
        row.appendChild(field5);
        field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("1"));
        row.appendChild(field6);
        field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(field7);
        field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(field8);
        field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(field9);
        field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(field10);
        return doc;
    }

    public static Document createInsertMixNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("8"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue8"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("9"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue9"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col4);

        row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("10"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue10"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/01/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("11"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue11"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col4);

        return doc;
    }

    public static Document createInsertMultiMixNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("8"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue8"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);
        Element field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(field4);
        Element field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(field5);
        Element field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("on"));
        row.appendChild(field6);
        Element field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(field7);
        Element field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(field8);
        Element field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(field9);
        Element field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(field10);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("9"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue9"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col4);
        Element col5 = doc.createElement("col");
        col5.setAttribute("type", "date");
        col5.setAttribute("format", "dd/MM/yyyy");
        col5.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(col5);
        Element col6 = doc.createElement("col");
        col6.setAttribute("type", "time");
        col6.setAttribute("format", "HH:mm:ss");
        col6.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(col6);
        Element col7 = doc.createElement("col");
        col7.setAttribute("type", "boolean");
        col7.appendChild(doc.createTextNode("on"));
        row.appendChild(col7);
        Element col8 = doc.createElement("col");
        col8.setAttribute("type", "small-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(col8);
        Element col9 = doc.createElement("col");
        col9.setAttribute("type", "big-int");
        col9.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(col9);
        Element col10 = doc.createElement("col");
        col10.setAttribute("type", "float");
        col10.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(col10);
        Element col11 = doc.createElement("col");
        col11.setAttribute("type", "double");
        col11.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(col11);

        row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("10"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue10"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/01/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);
        field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("10/01/2011"));
        row.appendChild(field4);
        field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("00:00:00"));
        row.appendChild(field5);
        field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("1"));
        row.appendChild(field6);
        field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(field7);
        field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(field8);
        field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(field9);
        field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(field10);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("11"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue11"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 23:59:59"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col4);
        col5 = doc.createElement("col");
        col5.setAttribute("type", "date");
        col5.setAttribute("format", "dd/MM/yyyy");
        col5.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(col5);
        col6 = doc.createElement("col");
        col6.setAttribute("type", "time");
        col6.setAttribute("format", "HH:mm:ss");
        col6.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(col6);
        col7 = doc.createElement("col");
        col7.setAttribute("type", "boolean");
        col7.appendChild(doc.createTextNode("on"));
        row.appendChild(col7);
        col8 = doc.createElement("col");
        col8.setAttribute("type", "small-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(col8);
        col9 = doc.createElement("col");
        col9.setAttribute("type", "big-int");
        col9.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(col9);
        col10 = doc.createElement("col");
        col10.setAttribute("type", "float");
        col10.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(col10);
        col11 = doc.createElement("col");
        col11.setAttribute("type", "double");
        col11.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(col11);

        return doc;
    }

    public static Document createUpdateMixNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("8"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue8b"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/11/2010 23:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "string");
        col1.appendChild(doc.createTextNode("testvalue9b"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "timestamp");
        col2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col2.appendChild(doc.createTextNode("31/11/2010 23:59:59"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "decimal");
        col3.setAttribute("decimal-separator", ",");
        col3.setAttribute("grouping-separator", ".");
        col3.setAttribute("number-format", "#,##0.000");
        col3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "integer");
        col4.appendChild(doc.createTextNode("9"));
        row.appendChild(col4);

        row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("10"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue10b"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/02/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        col1 = doc.createElement("col");
        col1.setAttribute("type", "string");
        col1.appendChild(doc.createTextNode("testvalue11b"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "timestamp");
        col2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col2.appendChild(doc.createTextNode("31/11/2010 23:59:59"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "decimal");
        col3.setAttribute("decimal-separator", ",");
        col3.setAttribute("grouping-separator", ".");
        col3.setAttribute("number-format", "#,##0.000");
        col3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "integer");
        col4.appendChild(doc.createTextNode("11"));
        row.appendChild(col4);

        return doc;
    }

    public static Document createUpdateMultiMixNPMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        Element row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        Element id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("8"));
        row.appendChild(id);
        Element field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue8b"));
        row.appendChild(field1);
        Element field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("31/11/2010 23:59:59"));
        row.appendChild(field2);
        Element field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ",");
        field3.setAttribute("grouping-separator", ".");
        field3.setAttribute("number-format", "#,##0.000");
        field3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(field3);
        Element field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(field4);
        Element field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(field5);
        Element field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("on"));
        row.appendChild(field6);
        Element field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(field7);
        Element field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(field8);
        Element field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(field9);
        Element field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(field10);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "string");
        col1.appendChild(doc.createTextNode("testvalue9b"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "timestamp");
        col2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col2.appendChild(doc.createTextNode("31/11/2010 23:59:59"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "decimal");
        col3.setAttribute("decimal-separator", ",");
        col3.setAttribute("grouping-separator", ".");
        col3.setAttribute("number-format", "#,##0.000");
        col3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "date");
        col4.setAttribute("format", "dd/MM/yyyy");
        col4.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(col4);
        Element col5 = doc.createElement("col");
        col5.setAttribute("type", "time");
        col5.setAttribute("format", "HH:mm:ss");
        col5.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(col5);
        Element col6 = doc.createElement("col");
        col6.setAttribute("type", "boolean");
        col6.appendChild(doc.createTextNode("on"));
        row.appendChild(col6);
        Element col7 = doc.createElement("col");
        col7.setAttribute("type", "small-int");
        col7.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(col7);
        Element col8 = doc.createElement("col");
        col8.setAttribute("type", "big-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(col8);
        Element col9 = doc.createElement("col");
        col9.setAttribute("type", "float");
        col9.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(col9);
        Element col10 = doc.createElement("col");
        col10.setAttribute("type", "double");
        col10.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(col10);
        Element col11 = doc.createElement("col");
        col11.setAttribute("type", "integer");
        col11.appendChild(doc.createTextNode("9"));
        row.appendChild(col11);

        row = doc.createElement("row");
        row.setAttribute("id", "1");
        data.appendChild(row);
        id = doc.createElement("id");
        id.setAttribute("type", "integer");
        id.appendChild(doc.createTextNode("10"));
        row.appendChild(id);
        field1 = doc.createElement("field1");
        field1.setAttribute("type", "string");
        field1.appendChild(doc.createTextNode("testvalue10b"));
        row.appendChild(field1);
        field2 = doc.createElement("field2");
        field2.setAttribute("type", "timestamp");
        field2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        field2.appendChild(doc.createTextNode("10/02/2011 00:00:00"));
        row.appendChild(field2);
        field3 = doc.createElement("field3");
        field3.setAttribute("type", "decimal");
        field3.setAttribute("decimal-separator", ".");
        field3.setAttribute("grouping-separator", ",");
        field3.setAttribute("number-format", "#0.00");
        field3.appendChild(doc.createTextNode("5.12"));
        row.appendChild(field3);
        field4 = doc.createElement("field4");
        field4.setAttribute("type", "date");
        field4.setAttribute("format", "dd/MM/yyyy");
        field4.appendChild(doc.createTextNode("10/02/2011"));
        row.appendChild(field4);
        field5 = doc.createElement("field5");
        field5.setAttribute("type", "time");
        field5.setAttribute("format", "HH:mm:ss");
        field5.appendChild(doc.createTextNode("12:00:00"));
        row.appendChild(field5);
        field6 = doc.createElement("field6");
        field6.setAttribute("type", "boolean");
        field6.appendChild(doc.createTextNode("on"));
        row.appendChild(field6);
        field7 = doc.createElement("field7");
        field7.setAttribute("type", "small-int");
        field7.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(field7);
        field8 = doc.createElement("field8");
        field8.setAttribute("type", "big-int");
        field8.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(field8);
        field9 = doc.createElement("field9");
        field9.setAttribute("type", "float");
        field9.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(field9);
        field10 = doc.createElement("field10");
        field10.setAttribute("type", "double");
        field10.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(field10);

        row = doc.createElement("row");
        row.setAttribute("id", "0");
        data.appendChild(row);
        col1 = doc.createElement("col");
        col1.setAttribute("type", "string");
        col1.appendChild(doc.createTextNode("testvalue11b"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "timestamp");
        col2.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col2.appendChild(doc.createTextNode("31/11/2010 23:59:59"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "decimal");
        col3.setAttribute("decimal-separator", ",");
        col3.setAttribute("grouping-separator", ".");
        col3.setAttribute("number-format", "#,##0.000");
        col3.appendChild(doc.createTextNode("12.345,123"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "date");
        col4.setAttribute("format", "dd/MM/yyyy");
        col4.appendChild(doc.createTextNode("31/11/2010"));
        row.appendChild(col4);
        col5 = doc.createElement("col");
        col5.setAttribute("type", "time");
        col5.setAttribute("format", "HH:mm:ss");
        col5.appendChild(doc.createTextNode("15:00:15"));
        row.appendChild(col5);
        col6 = doc.createElement("col");
        col6.setAttribute("type", "boolean");
        col6.appendChild(doc.createTextNode("on"));
        row.appendChild(col6);
        col7 = doc.createElement("col");
        col7.setAttribute("type", "small-int");
        col7.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(col7);
        col8 = doc.createElement("col");
        col8.setAttribute("type", "big-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(col8);
        col9 = doc.createElement("col");
        col9.setAttribute("type", "float");
        col9.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(col9);
        col10 = doc.createElement("col");
        col10.setAttribute("type", "double");
        col10.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(col10);
        col11 = doc.createElement("col");
        col11.setAttribute("type", "integer");
        col11.appendChild(doc.createTextNode("11"));
        row.appendChild(col11);

        return doc;
    }

    public static Document createInsertOrUpdateMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        // row
        Element row = doc.createElement("row");
        data.appendChild(row);

        // column
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("2"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue2-new"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 15:00:00"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("10.000,000"));
        row.appendChild(col4);

        // column update
        Element col_update2 = doc.createElement("col-update");
        col_update2.setAttribute("type", "string");
        col_update2.appendChild(doc.createTextNode("testvalue2-new"));
        row.appendChild(col_update2);
        Element col_update3 = doc.createElement("col-update");
        col_update3.setAttribute("type", "timestamp");
        col_update3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col_update3.appendChild(doc.createTextNode("31/12/2010 15:00:00"));
        row.appendChild(col_update3);
        Element col_update4 = doc.createElement("col-update");
        col_update4.setAttribute("type", "decimal");
        col_update4.setAttribute("decimal-separator", ",");
        col_update4.setAttribute("grouping-separator", ".");
        col_update4.setAttribute("number-format", "#,##0.000");
        col_update4.appendChild(doc.createTextNode("10.000,000"));
        row.appendChild(col_update4);
        Element col_update1 = doc.createElement("col-update");
        col_update1.setAttribute("type", "integer");
        col_update1.appendChild(doc.createTextNode("2"));
        row.appendChild(col_update1);

        // row
        row = doc.createElement("row");
        data.appendChild(row);

        // column
        col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("4"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue4"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("20/10/2010 16:00:00"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("13.456,004"));
        row.appendChild(col4);

        // column update
        col_update2 = doc.createElement("col-update");
        col_update2.setAttribute("type", "string");
        col_update2.appendChild(doc.createTextNode("testvalue4"));
        row.appendChild(col_update2);
        col_update3 = doc.createElement("col-update");
        col_update3.setAttribute("type", "timestamp");
        col_update3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col_update3.appendChild(doc.createTextNode("20/10/2010 16:00:00"));
        row.appendChild(col_update3);
        col_update4 = doc.createElement("col-update");
        col_update4.setAttribute("type", "decimal");
        col_update4.setAttribute("decimal-separator", ",");
        col_update4.setAttribute("grouping-separator", ".");
        col_update4.setAttribute("number-format", "#,##0.000");
        col_update4.appendChild(doc.createTextNode("13.456,004"));
        row.appendChild(col_update4);
        col_update1 = doc.createElement("col-update");
        col_update1.setAttribute("type", "integer");
        col_update1.appendChild(doc.createTextNode("4"));
        row.appendChild(col_update1);

        return doc;
    }

    public static Document createInsertOrUpdateMultiMessage() throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("RowSet");
        doc.appendChild(root);
        Element data = doc.createElement("data");
        root.appendChild(data);

        // row
        Element row = doc.createElement("row");
        data.appendChild(row);

        // column
        Element col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("2"));
        row.appendChild(col1);
        Element col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue2-new"));
        row.appendChild(col2);
        Element col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("31/12/2010 15:00:00"));
        row.appendChild(col3);
        Element col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("10.000,000"));
        row.appendChild(col4);
        Element col5 = doc.createElement("col");
        col5.setAttribute("type", "date");
        col5.setAttribute("format", "dd/MM/yyyy");
        col5.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(col5);
        Element col6 = doc.createElement("col");
        col6.setAttribute("type", "time");
        col6.setAttribute("format", "HH:mm:ss");
        col6.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(col6);
        Element col7 = doc.createElement("col");
        col7.setAttribute("type", "boolean");
        col7.appendChild(doc.createTextNode("on"));
        row.appendChild(col7);
        Element col8 = doc.createElement("col");
        col8.setAttribute("type", "small-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(col8);
        Element col9 = doc.createElement("col");
        col9.setAttribute("type", "big-int");
        col9.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(col9);
        Element col10 = doc.createElement("col");
        col10.setAttribute("type", "float");
        col10.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(col10);
        Element col11 = doc.createElement("col");
        col11.setAttribute("type", "double");
        col11.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(col11);

        // column update
        Element col_update2 = doc.createElement("col-update");
        col_update2.setAttribute("type", "string");
        col_update2.appendChild(doc.createTextNode("testvalue2-new"));
        row.appendChild(col_update2);
        Element col_update3 = doc.createElement("col-update");
        col_update3.setAttribute("type", "timestamp");
        col_update3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col_update3.appendChild(doc.createTextNode("31/12/2010 15:00:00"));
        row.appendChild(col_update3);
        Element col_update4 = doc.createElement("col-update");
        col_update4.setAttribute("type", "decimal");
        col_update4.setAttribute("decimal-separator", ",");
        col_update4.setAttribute("grouping-separator", ".");
        col_update4.setAttribute("number-format", "#,##0.000");
        col_update4.appendChild(doc.createTextNode("10.000,000"));
        row.appendChild(col_update4);
        Element col_update5 = doc.createElement("col-update");
        col_update5.setAttribute("type", "date");
        col_update5.setAttribute("format", "dd/MM/yyyy");
        col_update5.appendChild(doc.createTextNode("31/12/2010"));
        row.appendChild(col_update5);
        Element col_update6 = doc.createElement("col-update");
        col_update6.setAttribute("type", "time");
        col_update6.setAttribute("format", "HH:mm:ss");
        col_update6.appendChild(doc.createTextNode("23:59:59"));
        row.appendChild(col_update6);
        Element col_update7 = doc.createElement("col-update");
        col_update7.setAttribute("type", "boolean");
        col_update7.appendChild(doc.createTextNode("on"));
        row.appendChild(col_update7);
        Element col_update8 = doc.createElement("col-update");
        col_update8.setAttribute("type", "small-int");
        col_update8.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(col_update8);
        Element col_update9 = doc.createElement("col-update");
        col_update9.setAttribute("type", "big-int");
        col_update9.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(col_update9);
        Element col_update10 = doc.createElement("col-update");
        col_update10.setAttribute("type", "float");
        col_update10.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(col_update10);
        Element col_update11 = doc.createElement("col-update");
        col_update11.setAttribute("type", "double");
        col_update11.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(col_update11);
        Element col_update1 = doc.createElement("col-update");
        col_update1.setAttribute("type", "integer");
        col_update1.appendChild(doc.createTextNode("2"));
        row.appendChild(col_update1);

        // row
        row = doc.createElement("row");
        data.appendChild(row);

        // column
        col1 = doc.createElement("col");
        col1.setAttribute("type", "integer");
        col1.appendChild(doc.createTextNode("4"));
        row.appendChild(col1);
        col2 = doc.createElement("col");
        col2.setAttribute("type", "string");
        col2.appendChild(doc.createTextNode("testvalue4"));
        row.appendChild(col2);
        col3 = doc.createElement("col");
        col3.setAttribute("type", "timestamp");
        col3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col3.appendChild(doc.createTextNode("20/10/2010 16:00:00"));
        row.appendChild(col3);
        col4 = doc.createElement("col");
        col4.setAttribute("type", "decimal");
        col4.setAttribute("decimal-separator", ",");
        col4.setAttribute("grouping-separator", ".");
        col4.setAttribute("number-format", "#,##0.000");
        col4.appendChild(doc.createTextNode("13.456,004"));
        row.appendChild(col4);
        col5 = doc.createElement("col");
        col5.setAttribute("type", "date");
        col5.setAttribute("format", "dd/MM/yyyy");
        col5.appendChild(doc.createTextNode("10/01/2011"));
        row.appendChild(col5);
        col6 = doc.createElement("col");
        col6.setAttribute("type", "time");
        col6.setAttribute("format", "HH:mm:ss");
        col6.appendChild(doc.createTextNode("00:00:00"));
        row.appendChild(col6);
        col7 = doc.createElement("col");
        col7.setAttribute("type", "boolean");
        col7.appendChild(doc.createTextNode("TRUE"));
        row.appendChild(col7);
        col8 = doc.createElement("col");
        col8.setAttribute("type", "small-int");
        col8.appendChild(doc.createTextNode(String.valueOf(Short.MIN_VALUE)));
        row.appendChild(col8);
        col9 = doc.createElement("col");
        col9.setAttribute("type", "big-int");
        col9.appendChild(doc.createTextNode(String.valueOf(Long.MIN_VALUE)));
        row.appendChild(col9);
        col10 = doc.createElement("col");
        col10.setAttribute("type", "float");
        col10.appendChild(doc.createTextNode(String.valueOf(Float.MIN_VALUE)));
        row.appendChild(col10);
        col11 = doc.createElement("col");
        col11.setAttribute("type", "double");
        col11.appendChild(doc.createTextNode(String.valueOf(Double.MIN_VALUE)));
        row.appendChild(col11);

        // column update
        col_update2 = doc.createElement("col-update");
        col_update2.setAttribute("type", "string");
        col_update2.appendChild(doc.createTextNode("testvalue4"));
        row.appendChild(col_update2);
        col_update3 = doc.createElement("col-update");
        col_update3.setAttribute("type", "timestamp");
        col_update3.setAttribute("format", "dd/MM/yyyy HH:mm:ss");
        col_update3.appendChild(doc.createTextNode("20/10/2010 16:00:00"));
        row.appendChild(col_update3);
        col_update4 = doc.createElement("col-update");
        col_update4.setAttribute("type", "decimal");
        col_update4.setAttribute("decimal-separator", ",");
        col_update4.setAttribute("grouping-separator", ".");
        col_update4.setAttribute("number-format", "#,##0.000");
        col_update4.appendChild(doc.createTextNode("13.456,004"));
        row.appendChild(col_update4);
        col_update5 = doc.createElement("col-update");
        col_update5.setAttribute("type", "date");
        col_update5.setAttribute("format", "dd/MM/yyyy");
        col_update5.appendChild(doc.createTextNode("20/10/2010"));
        row.appendChild(col_update5);
        col_update6 = doc.createElement("col-update");
        col_update6.setAttribute("type", "time");
        col_update6.setAttribute("format", "HH:mm:ss");
        col_update6.appendChild(doc.createTextNode("16:00:00"));
        row.appendChild(col_update6);
        col_update7 = doc.createElement("col-update");
        col_update7.setAttribute("type", "boolean");
        col_update7.appendChild(doc.createTextNode("on"));
        row.appendChild(col_update7);
        col_update8 = doc.createElement("col-update");
        col_update8.setAttribute("type", "small-int");
        col_update8.appendChild(doc.createTextNode(String.valueOf(Short.MAX_VALUE)));
        row.appendChild(col_update8);
        col_update9 = doc.createElement("col-update");
        col_update9.setAttribute("type", "big-int");
        col_update9.appendChild(doc.createTextNode(String.valueOf(Long.MAX_VALUE)));
        row.appendChild(col_update9);
        col_update10 = doc.createElement("col-update");
        col_update10.setAttribute("type", "float");
        col_update10.appendChild(doc.createTextNode(String.valueOf(Float.MAX_VALUE)));
        row.appendChild(col_update10);
        col_update11 = doc.createElement("col-update");
        col_update11.setAttribute("type", "double");
        col_update11.appendChild(doc.createTextNode(String.valueOf(Double.MAX_VALUE)));
        row.appendChild(col_update11);
        col_update1 = doc.createElement("col-update");
        col_update1.setAttribute("type", "integer");
        col_update1.appendChild(doc.createTextNode("4"));
        row.appendChild(col_update1);

        return doc;
    }
}
