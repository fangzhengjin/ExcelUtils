/*
 * FileName: MergeTag.java 2006-10-13
 * Copyright (c) 2003-2005 try2it.com 
 */
package net.sf.excelutils.tags;

import java.util.StringTokenizer;

import net.sf.excelutils.ExcelParser;
import net.sf.excelutils.WorkbookUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * <p>
 * <b>MergeTag</b> is a class which merge cell by condition example: #merge columnIndex ${value}
 * </p>
 * 
 * @author <a href="mailto:wangzp@try2it.com">rainsoft</a>
 * @version $Revision$ $Date$
 */
public class MergeTag implements ITag {

	private Log LOG = LogFactory.getLog(MergeTag.class);

	public static String KEY_MERGE = "#merge";

	public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
		String column = "";
		String value = "";
		String merge = curCell.getStringCellValue();
		StringTokenizer st = new StringTokenizer(merge, " ");
		int pos = 0;
		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			if (pos == 1) {
				column = str;
			}
			if (pos == 2) {
				value = str;
			}
			pos++;
		}

		// curCell.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
		curCell.setCellValue(value);
		ExcelParser.parseCell(context, sheet, curRow, curCell);

		// last row
		HSSFRow lastRow = WorkbookUtils.getRow(curRow.getRowNum() - 1, sheet);

		// can merge flag
		boolean canMerge = true;

		// compare columns
		String[] columns = column.split(",");
		for (int index = 0; index < columns.length; index++) {
			int columnIndex = Integer.parseInt(columns[index]);
			LOG.debug("#merge compare column index " + columnIndex);

			// compare the cell
			if (curRow.getRowNum() - 1 >= sheet.getFirstRowNum()) {
				HSSFCell lastCell = WorkbookUtils.getCell(lastRow, columnIndex);
				HSSFCell compCell = WorkbookUtils.getCell(curRow, columnIndex);
				if (lastCell.getCellType() == compCell.getCellType()) {
					switch (compCell.getCellType()) {
					case HSSFCell.CELL_TYPE_STRING:
						canMerge &= lastCell.getStringCellValue().equals(compCell.getStringCellValue());
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN:
						canMerge &= lastCell.getBooleanCellValue() == compCell.getBooleanCellValue();
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						canMerge &= lastCell.getNumericCellValue() == compCell.getNumericCellValue();
						break;
					default:
						canMerge &= false;
					}
				} else {
					canMerge &= false;
				}
			}
		}

		if (canMerge) {
			sheet.addMergedRegion(new CellRangeAddress(lastRow.getRowNum(), curRow.getRowNum(), curCell.getColumnIndex(),
					curCell.getColumnIndex()));
		}

		return new int[] { 0, 0, 0 };
	}

	public boolean hasEndTag() {
		return false;
	}

	public String getTagName() {
		return KEY_MERGE;
	}
}
