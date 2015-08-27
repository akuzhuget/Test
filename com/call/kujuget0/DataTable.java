package com.call.kujuget0;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataTable {
	String _name;
	List<String> _headers;
	List<List<String>> _cells;
	public DataTable(String name) {
		super();
		this._name = name;
		_headers = new ArrayList<String>();
		_cells = new ArrayList<List<String>>();
	}
	public String getFileName() {
		return _name + "table.csv";
	}
	public void AddHeaders(List<String> headers) {
		_headers = headers;
	}
	
	public void AddRow(List<String> row) {
		_cells.add(row);
	}

	
	public StringBuffer toStringBuffer() {
		StringBuffer result = new StringBuffer();
		result.append("Table " + _name + Util.EOL);
		result.append(Util.listToSrting(_headers)+ Util.EOL);
		for (List<String> a:_cells) {
			result.append(Util.listToSrting(a)+ Util.EOL);
		}
		return result;
	}
	public StringBuffer toHtml() {
		StringBuffer result = new StringBuffer();
		result.append("<TABLE BORDER>");		
		result.append("<CAPTION ALIGN=top> Table "+_name+"</CAPTION>");
		// adding headers
		result.append("<TR>");
		for (String head:_headers) {
			result.append("<TH>"+head+"</TH>");
		}		
		result.append("</TR>");
		for (List<String> cell:_cells) {
			result.append("<TR>");
			for (String a:cell) {
				result.append("<TD>"+a+"</TD>");
			}		
			result.append("</TR>");			
		}		
		result.append("</TABLE> ");
		return result;
	}
	public StringBuffer toCSV() {
		StringBuffer result = new StringBuffer();
		//result.append("CSV: " + _name + Util.EOL);
		result.append(Util.listToStringDelim(_headers)+ Util.EOL);
		for (List<String> a:_cells) {
			result.append(Util.listToStringDelim(a)+ Util.EOL);
		}
		return result;
	}
	

}
