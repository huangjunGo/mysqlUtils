package util.mysql;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Font;

/**
 * mysql��ع�����
 * <br>�������ڣ�2016��5��25��
 * <br><b>Copyright 2016 UTOUU All Rights Reserved</b>
 * @author huangjun
 * @since 1.0
 * @version 1.0
 */
public class MysqlUtil {

	/**
	 * �������ݿ�url���ɱ���Ϣ  xls
	 * @since 1.0 
	 * @param url ���ݿ�·��
	 * @param userName �˻�
	 * @param pwd ����
	 * @param filePath ����xls·��
	 * @throws Exception
	 * <br><b>���ߣ� @author huangjun</b>
	 * <br>����ʱ�䣺2016��5��25�� ����7:43:13
	 */
	public static void getTableInfo(String url,String userName,String pwd,String filePath) throws Exception{
		Connection connection = null;
		//String url = "jdbc:mysql://db.dev.utouu.com:3999/utouu-union?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf-8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, userName,
					pwd);
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet tables = metaData.getTables(null, "%", "%",
					new String[] { "TABLE" });
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("sheet1");
			sheet.setDefaultColumnWidth(50);
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell(0);
			int i = 1;
			HSSFCellStyle createCellStyle = null;
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				row = sheet.createRow(i);
				cell = row.createCell(0);
				CellRangeAddress crs = new CellRangeAddress(i, i, 0, 2);
				sheet.addMergedRegion(crs);
				cell.setCellValue(tableName);
				createCellStyle = wb.createCellStyle();
				Font font = wb.createFont();
				font.setFontHeightInPoints((short) 15);
				createCellStyle.setFont(font);
				cell.setCellStyle(createCellStyle);

				i = i + 1;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("�ֶ�����");
				cell = row.createCell(1);
				cell.setCellValue("�ֶ�����");
				cell = row.createCell(2);
				cell.setCellValue("�ֶ�����");
				ResultSet columns = metaData.getColumns(null, null, tableName,
						null);
				while (columns.next()) {
					i = i + 1;

					String name = columns.getString("COLUMN_NAME");
					String type = columns.getString("TYPE_NAME");
					String remark = columns.getString("REMARKS");
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue(name);
					cell = row.createCell(1);
					cell.setCellValue(type);
					cell = row.createCell(2);
					cell.setCellValue(remark);
				}
				i = i + 2;
			}

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			byte[] content = os.toByteArray();

			File file = new File(filePath);// Excel�ļ����ɺ�洢��λ�á�
			if (!file.exists()) {
				file.createNewFile();
			}

			OutputStream fos = null;

			try {
				fos = new FileOutputStream(file);
				fos.write(content);
				os.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			
		}
		finally{
			connection.close();
		}
	}
}

