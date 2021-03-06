/*
 * Copyright 2003-2005 try2it.com.
 * Created on 2005-7-7
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package net.sf.excelutils.tags;

import net.sf.excelutils.ExcelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import net.sf.excelutils.ExcelParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * <p>
 * <b>FormulaTag</b> is a class which can output excel formula
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 127 $ $Date: 2006-10-13 23:48:46 +0800 (星期五, 13 十月 2006) $
 */
public class FormulaTag implements ITag {

	private Log LOG = LogFactory.getLog(FormulaTag.class);

	public static final String KEY_FORMULA = "#formula";

	public int[] parseTag(Object context, Workbook wb, Sheet sheet, Row curRow, Cell curCell) throws ExcelException {

		String cellstr = curCell.getStringCellValue();
		if (null == cellstr || "".equals(cellstr)) {
			return new int[] { 0, 0, 0 };
		}
		LOG.debug("FormulaTag:" + cellstr);
		cellstr = cellstr.substring(KEY_FORMULA.length()).trim();

		Object formula = ExcelParser.parseStr(context, cellstr);

		if (null != formula) {
			curCell.setCellFormula(formula.toString());
		}

		return new int[] { 0, 0, 0 };
	}

	public boolean hasEndTag() {
		return false;
	}

	public String getTagName() {
		return KEY_FORMULA;
	}

}
