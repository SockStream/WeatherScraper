package output;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import datas.DayData;
import datas.DoubleColorData;
import datas.IssDatas;
import datas.LieuData;
import input.GoogleDriveInput;
import utils.ColorsEnum;
import utils.CustomUtils;

public class ExcelFile {
	private static String _filename;
	private CellStyle wrapStyle;
	private CellStyle redStyle;
	private CellStyle orangeStyle;
	private CellStyle greenStyle;
	private CellStyle lightGreenStyle;
	private CellStyle lightOrangeStyle;
	private CellStyle lightRedStyle;
	private CellStyle boldStyle;
	private CellStyle lightStyle;
	private CellStyle centeredBoldStyle;
	private CellStyle vCenteredBoldStyle;
	private CellStyle rightAlignStyle;
	private CellStyle centeredStyle;
	
	
	public ExcelFile(String filename) {
		_filename = filename;
	}

	public void Generate(List<LieuData> listeLieuxData) throws FileNotFoundException, IOException {
		List<String> lieuxParOrdreAlpha = new ArrayList<String>();
		Workbook wb = new HSSFWorkbook();
		CreationHelper factory = wb.getCreationHelper();

		String iss_IconFile = GoogleDriveInput.getFile("iss_icon.png");
		int pictureIndex = -1;
		if (iss_IconFile != null)
		{
			FileInputStream stream = new FileInputStream(iss_IconFile);
			pictureIndex = wb.addPicture(IOUtils.toByteArray(stream), Workbook.PICTURE_TYPE_PNG);
			stream.close();
			Files.delete(Paths.get("iss_icon.png"));
		}
		CreerStyles(wb);
		
		for (LieuData data : listeLieuxData)
		{
			lieuxParOrdreAlpha.add(data.getNom());
			Sheet sheet = wb.createSheet(data.getNom().split(",")[0]);
			InscrireNomLieu(sheet,data);
			InscrireBortle(sheet,data);
			InscrireMagnitude(sheet,data);
			
			int compteur = 4;
			for(DayData day : data.getDayData())
			{

				Row rowDate = sheet.createRow(compteur);
				InscrireDate(rowDate, day);
				compteur++;
				InscrireDewPoint(sheet,compteur,day);
				compteur++;
				InscrireHumidity(sheet,compteur,day);
				compteur ++;
				InscrireVent(sheet,compteur,day);
				compteur++;
				
				InscrireTotalClouds(sheet, compteur,day);
				compteur ++;
				InscrireLowClouds(sheet, compteur, day);
				compteur ++;
				InscrireMediumClouds(sheet, compteur, day);
				compteur ++;
				InscrireHighClouds(sheet, compteur, day);
				compteur++;
				InscrirePassagesISS(sheet,factory,pictureIndex, compteur, day);
				sheet.groupRow(rowDate.getRowNum() + 1, compteur);
				sheet.setRowGroupCollapsed(rowDate.getRowNum() + 1, true);
				compteur += 2;
			}
			
			sheet.setColumnWidth(1, 4000);
			sheet.autoSizeColumn(0, true);
		}
		Collections.sort(lieuxParOrdreAlpha);
		//CreerPageRaccourcis(wb,lieuxParOrdreAlpha);
		FermerWorkBook(wb);
	}

	private void CreerPageRaccourcis(Workbook wb, List<String> lieuxParOrdreAlpha) {
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();


		hlink_font.setUnderline(Font.U_SINGLE);


		hlink_font.setColor(IndexedColors.BLUE.getIndex());


		hlink_style.setFont(hlink_font);
		
		Sheet sheet = wb.createSheet("Menu");
		int i = 0;
		CreationHelper createHelper = wb.getCreationHelper();
		for(String lieu : lieuxParOrdreAlpha)
		{
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0);
			cell.setCellValue(lieu);
			HSSFHyperlink hyperlink = (HSSFHyperlink) createHelper.createHyperlink(HyperlinkType.DOCUMENT);
			hyperlink.setAddress("'"+ lieu.split(",")[0] +"'!A2");
			cell.setHyperlink(hyperlink);
			cell.setCellStyle(hlink_style);
			i++;
		}
		wb.setSheetOrder("Menu", 0);
		wb.setActiveSheet(0);
	}

	private void CreerStyles(Workbook wb) {
		Font boldFont = wb.createFont();
		boldFont.setBold(true);
		
		wrapStyle = wb.createCellStyle();
		wrapStyle.setWrapText(true);
		wrapStyle.setBorderBottom(BorderStyle.HAIR);
		wrapStyle.setBorderTop(BorderStyle.HAIR);
		wrapStyle.setBorderRight(BorderStyle.HAIR);
		wrapStyle.setBorderLeft(BorderStyle.HAIR);
		
		redStyle = wb.createCellStyle();
		redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		redStyle.setFont(boldFont);
		redStyle.setAlignment(HorizontalAlignment.CENTER);
		redStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		redStyle.setBorderBottom(BorderStyle.HAIR);
		redStyle.setBorderTop(BorderStyle.HAIR);
		redStyle.setBorderRight(BorderStyle.HAIR);
		redStyle.setBorderLeft(BorderStyle.HAIR);
		
		orangeStyle = wb.createCellStyle();
		orangeStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		orangeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		orangeStyle.setFont(boldFont);
		orangeStyle.setAlignment(HorizontalAlignment.CENTER);
		orangeStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		orangeStyle.setBorderBottom(BorderStyle.HAIR);
		orangeStyle.setBorderTop(BorderStyle.HAIR);
		orangeStyle.setBorderRight(BorderStyle.HAIR);
		orangeStyle.setBorderLeft(BorderStyle.HAIR);
		
		greenStyle = wb.createCellStyle();
		greenStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		greenStyle.setFont(boldFont);
		greenStyle.setAlignment(HorizontalAlignment.CENTER);
		greenStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		greenStyle.setBorderBottom(BorderStyle.HAIR);
		greenStyle.setBorderTop(BorderStyle.HAIR);
		greenStyle.setBorderRight(BorderStyle.HAIR);
		greenStyle.setBorderLeft(BorderStyle.HAIR);
		
		lightRedStyle = wb.createCellStyle();
		lightRedStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		lightRedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//lightRedStyle.setFont(boldFont);
		lightRedStyle.setAlignment(HorizontalAlignment.CENTER);
		lightRedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		lightRedStyle.setBorderBottom(BorderStyle.HAIR);
		lightRedStyle.setBorderTop(BorderStyle.HAIR);
		lightRedStyle.setBorderRight(BorderStyle.HAIR);
		lightRedStyle.setBorderLeft(BorderStyle.HAIR);
		
		lightOrangeStyle = wb.createCellStyle();
		lightOrangeStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		lightOrangeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//lightOrangeStyle.setFont(boldFont);
		lightOrangeStyle.setAlignment(HorizontalAlignment.CENTER);
		lightOrangeStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		lightOrangeStyle.setBorderBottom(BorderStyle.HAIR);
		lightOrangeStyle.setBorderTop(BorderStyle.HAIR);
		lightOrangeStyle.setBorderRight(BorderStyle.HAIR);
		lightOrangeStyle.setBorderLeft(BorderStyle.HAIR);
		
		lightGreenStyle = wb.createCellStyle();
		lightGreenStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		lightGreenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//lightGreenStyle.setFont(boldFont);
		lightGreenStyle.setAlignment(HorizontalAlignment.CENTER);
		lightGreenStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		lightGreenStyle.setBorderBottom(BorderStyle.HAIR);
		lightGreenStyle.setBorderTop(BorderStyle.HAIR);
		lightGreenStyle.setBorderRight(BorderStyle.HAIR);
		lightGreenStyle.setBorderLeft(BorderStyle.HAIR);
		
		boldStyle = wb.createCellStyle();
		boldStyle.setFont(boldFont);
		boldStyle.setBorderBottom(BorderStyle.HAIR);
		boldStyle.setBorderTop(BorderStyle.HAIR);
		boldStyle.setBorderRight(BorderStyle.HAIR);
		boldStyle.setBorderLeft(BorderStyle.HAIR);
		
		lightStyle = wb.createCellStyle();
		lightStyle.setBorderBottom(BorderStyle.HAIR);
		lightStyle.setBorderTop(BorderStyle.HAIR);
		lightStyle.setBorderRight(BorderStyle.HAIR);
		lightStyle.setBorderLeft(BorderStyle.HAIR);
		
		centeredBoldStyle = wb.createCellStyle();
		centeredBoldStyle.setFont(boldFont);
		centeredBoldStyle.setAlignment(HorizontalAlignment.CENTER);
		centeredBoldStyle.setBorderBottom(BorderStyle.HAIR);
		centeredBoldStyle.setBorderTop(BorderStyle.HAIR);
		centeredBoldStyle.setBorderRight(BorderStyle.HAIR);
		centeredBoldStyle.setBorderLeft(BorderStyle.HAIR);
		
		vCenteredBoldStyle = wb.createCellStyle();
		vCenteredBoldStyle.setFont(boldFont);
		vCenteredBoldStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		vCenteredBoldStyle.setBorderBottom(BorderStyle.HAIR);
		vCenteredBoldStyle.setBorderTop(BorderStyle.HAIR);
		vCenteredBoldStyle.setBorderRight(BorderStyle.HAIR);
		vCenteredBoldStyle.setBorderLeft(BorderStyle.HAIR);
		
		centeredStyle = wb.createCellStyle();
		centeredStyle.setAlignment(HorizontalAlignment.CENTER);
		centeredStyle.setBorderBottom(BorderStyle.HAIR);
		centeredStyle.setBorderTop(BorderStyle.HAIR);
		centeredStyle.setBorderRight(BorderStyle.HAIR);
		centeredStyle.setBorderLeft(BorderStyle.HAIR);
		
		rightAlignStyle = wb.createCellStyle();
		rightAlignStyle.setAlignment(HorizontalAlignment.RIGHT);
		rightAlignStyle.setBorderBottom(BorderStyle.HAIR);
		rightAlignStyle.setBorderTop(BorderStyle.HAIR);
		rightAlignStyle.setBorderRight(BorderStyle.HAIR);
		rightAlignStyle.setBorderLeft(BorderStyle.HAIR);
	}

	private void InscrireHumidity(Sheet sheet, int compteur, DayData day) {
		Row rowHumidity = sheet.createRow(compteur);
		Cell humidityHeader = rowHumidity.createCell(0);

		humidityHeader.setCellValue("Relative Humidity");
		humidityHeader.setCellStyle(rightAlignStyle);
		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		
		int cpt = 0;
		for(DoubleColorData humidityData : day.getRelativeHumidity())
		{
			Cell humidityCell = rowHumidity.createCell(cpt+2);
			humidityCell.setCellValue(humidityData.getValeur());
			switch(humidityData.getCouleur())
			{
			case GREEN:
				humidityCell.setCellStyle(lightGreenStyle);
				break;
			case ORANGE:
				humidityCell.setCellStyle(lightOrangeStyle);
				break;
			case RED:
				humidityCell.setCellStyle(lightRedStyle);
				break;
			case WHITE:
			default:
				humidityCell.setCellStyle(lightStyle);
			
			}
			cpt ++;
		}
	}
	
	private void InscrireVent(Sheet sheet, int compteur, DayData day) {
		Row rowWind = sheet.createRow(compteur);
		Cell windHeader = rowWind.createCell(0);

		windHeader.setCellValue("Wind Speed (Km/h)");
		windHeader.setCellStyle(rightAlignStyle);
		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		
		int cpt = 0;
		for(DoubleColorData windData : day.getWindSpeed())
		{
			Cell windCell = rowWind.createCell(cpt+2);
			windCell.setCellValue(CustomUtils.ConvertMphToKmh(windData.getValeur()));
			switch(windData.getCouleur())
			{
			case GREEN:
				windCell.setCellStyle(lightGreenStyle);
				break;
			case ORANGE:
				windCell.setCellStyle(lightOrangeStyle);
				break;
			case RED:
				windCell.setCellStyle(lightRedStyle);
				break;
			case WHITE:
			default:
				windCell.setCellStyle(lightStyle);
			
			}
			cpt ++;
		}
	}

	private void InscrireDewPoint(Sheet sheet, int compteur, DayData day) {
		Row rowDew = sheet.createRow(compteur);
		Cell dewHeader = rowDew.createCell(0);

		dewHeader.setCellValue("Dew Point");
		dewHeader.setCellStyle(rightAlignStyle);
		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		
		int cpt = 0;
		for(ColorsEnum dewColor : day.getDewPoint()) {
			Cell cellDew = rowDew.createCell(2+cpt);
			switch(dewColor)
			{
			case GREEN:
				cellDew.setCellStyle(lightGreenStyle);
				break;
			case ORANGE:
				cellDew.setCellStyle(lightOrangeStyle);
				break;
			case RED:
				cellDew.setCellStyle(lightRedStyle);
				break;
			case WHITE:
			default:
				cellDew.setCellStyle(lightStyle);
			
			}
			cpt++;
		}
	}

	private void InscrirePassagesISS(Sheet sheet, CreationHelper factory, int pictureIndex, int compteur, DayData day) {
		Row rowISS = sheet.createRow(compteur);
		Cell issHeader = rowISS.createCell(0);
		issHeader.setCellValue("ISS Passover");
		issHeader.setCellStyle(rightAlignStyle);

		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		
		int cpt = 0;
		for(IssDatas issDatas : day.getISSPassOver()) {
			Cell cellIss = rowISS.createCell(2+cpt);
			if (issDatas.hasDatas())
			{
				ClientAnchor anchor = factory.createClientAnchor();
				anchor.setCol1(cellIss.getColumnIndex() + 1);
				anchor.setCol2(cellIss.getColumnIndex() + 3);
				anchor.setRow1(compteur + 1);
				anchor.setRow2(compteur + 5);
				Drawing drawing = sheet.createDrawingPatriarch();
				Comment comment = drawing.createCellComment(anchor);
				comment.setString(factory.createRichTextString(issDatas.getDatas()));
				
				cellIss.setCellComment(comment);
				
				if (pictureIndex != -1)
				{
					Drawing issDrawing = sheet.createDrawingPatriarch();
					ClientAnchor issAnchor = factory.createClientAnchor();
					issAnchor.setCol1(cellIss.getColumnIndex());
					issAnchor.setCol2(cellIss.getColumnIndex());
					issAnchor.setRow1(compteur);
					issAnchor.setRow2(compteur+1);
					Picture pict = issDrawing.createPicture( issAnchor, pictureIndex );
					//pict.resize();
				}
				else
				{
					cellIss.setCellValue("X");
				}
			}
			cellIss.setCellStyle(centeredStyle);
			cpt++;
		}
	}

	private void InscrireHighClouds(Sheet sheet, int compteur, DayData day) {
		Row rowHighClouds = sheet.createRow(compteur);
		Cell highCloudsHeader = rowHighClouds.createCell(0);
		highCloudsHeader.setCellValue("High Clouds (% Sky Obscured)");
		highCloudsHeader.setCellStyle(rightAlignStyle);
		//highCloudsHeader.setCellStyle(boldStyle);
		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		
		int cpt = 0;
		for(Integer value : day.getHighClouds()) {
			Cell cellClouds = rowHighClouds.createCell(2+cpt);
			cellClouds.setCellValue(value);
			cellClouds.setCellStyle(centeredStyle);
			cpt++;
		}
	}

	private void InscrireMediumClouds(Sheet sheet, int compteur, DayData day) {
		Row rowMediumClouds = sheet.createRow(compteur);
		Cell mediumCloudsHeader = rowMediumClouds.createCell(0);
		mediumCloudsHeader.setCellValue("Medium Clouds (% Sky Obscured)");
		//mediumCloudsHeader.setCellStyle(boldStyle);
		mediumCloudsHeader.setCellStyle(rightAlignStyle);
		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		
		int cpt = 0;
		for(Integer value : day.getMediumClouds()) {
			Cell cellClouds = rowMediumClouds.createCell(2+cpt);
			cellClouds.setCellValue(value);
			cellClouds.setCellStyle(centeredStyle);
			cpt++;
		}
	}

	private void InscrireLowClouds(Sheet sheet, int compteur, DayData day) {
		Row rowLowClouds = sheet.createRow(compteur);
		Cell lowCloudsHeader = rowLowClouds.createCell(0);
		lowCloudsHeader.setCellValue("Low Clouds (% Sky Obscured)");
		lowCloudsHeader.setCellStyle(rightAlignStyle);
		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		//lowCloudsHeader.setCellStyle(boldStyle);
		
		int cpt = 0;
		for(Integer value : day.getLowClouds()) {
			Cell cellClouds = rowLowClouds.createCell(2+cpt);
			cellClouds.setCellValue(value);
			cellClouds.setCellStyle(centeredStyle);
			cpt++;
		}
	}

	private void InscrireTotalClouds(Sheet sheet, int compteur, DayData day) {
		Row rowTotalClouds = sheet.createRow(compteur);
		Cell totalCloudsHeader = rowTotalClouds.createCell(0);
		totalCloudsHeader.setCellValue("Total Clouds (% Sky Obscured)");
		totalCloudsHeader.setCellStyle(rightAlignStyle);
		//totalCloudsHeader.setCellStyle(boldStyle);
		sheet.addMergedRegion(new CellRangeAddress(compteur, compteur, 0, 1));
		int cpt = 0;
		for(Integer value : day.getTotalClouds()) {
			Cell cellClouds = rowTotalClouds.createCell(2+cpt);
			cellClouds.setCellValue(value);
			cellClouds.setCellStyle(centeredStyle);
			cpt++;
		}
	}

	private void InscrireDate(Row rowDate, DayData day) {
		Cell cellDate = rowDate.createCell(0);
		cellDate.setCellValue(day.getDate());
		cellDate.setCellStyle(vCenteredBoldStyle);
		Cell cellLune = rowDate.createCell(1);
		cellLune.setCellValue(day.getMoonPhase() + "\n" + day.getMoonPercent() + "%" + "\n" + day.getMoonRise() + "  " + day.getMoonSet());
		cellLune.setCellStyle(vCenteredBoldStyle);
		cellLune.setCellStyle(wrapStyle);
		int index = 0;
		for (int heure : day.getHeures())
		{
			Cell hourCell = rowDate.createCell(index+2);
			hourCell.setCellValue(String.format("%02d",heure));
			/*if (day.getSkyQuality().size() == 24)
			{*/
				switch(day.getSkyQuality().get(index))
				{
				case GREEN:
					hourCell.setCellStyle(greenStyle);
					break;
				case ORANGE:
					hourCell.setCellStyle(orangeStyle);
					break;
				case RED:
					hourCell.setCellStyle(redStyle);
					break;
				case WHITE:
				default:
					hourCell.setCellStyle(boldStyle);
				
				}
			/*}
			else
			{*/
			//	hourCell.setCellStyle(boldStyle);
			//}
				index+=1;
		}
		rowDate.setHeight((short) 800);
	}

	private void InscrireMagnitude(Sheet sheet, LieuData data) {
		Row rowMagnitude = sheet.createRow(2);
		Cell cellMagnitude = rowMagnitude.createCell(0);
		cellMagnitude.setCellValue("Magnitude :");
		cellMagnitude.setCellStyle(boldStyle);
		Cell cellMagnitudeValue = rowMagnitude.createCell(1);
		cellMagnitudeValue.setCellValue(data.getMagnitude());
		cellMagnitudeValue.setCellStyle(rightAlignStyle);
	}

	private void InscrireBortle(Sheet sheet, LieuData data) {
		Row rowBortle = sheet.createRow(1);
		Cell cellBortle = rowBortle.createCell(0);
		cellBortle.setCellValue("Bortle :");
		cellBortle.setCellStyle(boldStyle);
		Cell cellBortleValue = rowBortle.createCell(1);
		cellBortleValue.setCellValue(data.getBortle());
		cellBortleValue.setCellStyle(rightAlignStyle);
	}

	private void InscrireNomLieu(Sheet sheet, LieuData data) {
		Row rowNom = sheet.createRow(0);
		Cell cellNom = rowNom.createCell(0);
		cellNom.setCellValue(data.getNom());
		cellNom.setCellStyle(centeredBoldStyle);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
	}

	private void FermerWorkBook(Workbook wb) throws FileNotFoundException, IOException {
		// Write the output to a file
	    try {
	    	OutputStream fileOut = new FileOutputStream(_filename + ".xls");
	        wb.write(fileOut);
	    }
	    catch(Exception e)
	    {
	    	
	    }
	    wb.close();
	}

}
