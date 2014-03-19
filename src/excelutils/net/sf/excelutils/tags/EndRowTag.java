/*
 * FileName: EndRowTag.java 2005-12-25
 * Copyright (c) 2003-2005 try2it.com 
 */
package net.sf.excelutils.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <p>
 * <b>EndRowTag</b> is a class which will delete rows after endExcelTag
 * </p>
 * 
 * @author <a href="mailto:wangzp@try2it.com">rainsoft</a>
 * @version $Revision: 127 $ $Date: 2006-10-13 23:48:46 +0800 (星期五, 13 十月 2006) $
 */
public class EndRowTag implements ITag {

	private Log LOG = LogFactory.getLog(EndRowTag.class);

	public static final String KEY_ENDROW = "#endRow";

	public static final String KEY_ENDCOLUMN = "#endColumn";

	public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
		// remove the rowBreaks after #endRow
		int breaks[] = sheet.getRowBreaks();
		for (int i = 0; null != breaks && i < breaks.length; i++) {
			if (breaks[i] >= curRow.getRowNum()) {
				sheet.removeRowBreak(breaks[i]);
			}
		}
		LOG.debug("EndRowTag at " + curRow.getRowNum());
		// remove the blank row after #endRow
		for (int rownum = sheet.getLastRowNum(); rownum > curRow.getRowNum(); rownum--) {
			HSSFRow row = sheet.getRow(rownum);
			sheet.removeRow(row);
		}

		return new int[] { 0, 0, 0 };
	}

	public boolean hasEndTag() {
		return false;
	}

	public String getTagName() {
		return KEY_ENDROW;
	}
}