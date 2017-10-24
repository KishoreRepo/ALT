package utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class GetExcelCellVaueUtil {

	public String getCellValue(Cell cell) {

		if (null != cell) {
			if (CellType._NONE == cell.getCellTypeEnum()) {
				return "";
			} else if (CellType.BLANK == cell.getCellTypeEnum()) {
				return "";
			} else if (CellType.BOOLEAN == cell.getCellTypeEnum()) {
				return String.valueOf(cell.getBooleanCellValue());
			} else if (CellType.ERROR == cell.getCellTypeEnum()) {
				return String.valueOf(cell.getErrorCellValue());
			} else if (CellType.FORMULA == cell.getCellTypeEnum()) {
				return String.valueOf(cell.getCellFormula());
			} else if (CellType.NUMERIC == cell.getCellTypeEnum()) {
				return String.valueOf(cell.getNumericCellValue());
			} else if (CellType.STRING == cell.getCellTypeEnum()) {
				return String.valueOf(cell.getStringCellValue());
			}

		}
		return "";
	}

	public String getCellValueForFirstColumn(Cell cell) {
		if (null != cell && CellType.NUMERIC == cell.getCellTypeEnum()) {
			return String.valueOf((long) cell.getNumericCellValue());
		} else {
			return cell.getStringCellValue();
		}
	}

}
