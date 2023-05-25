package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exception.*;
import javax.swing.table.*;
import java.util.*;
//L-85 assignment 
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.borders.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.image.*;
import com.itextpdf.kernel.geom.*;
import java.io.*;
//l-85 assignment 
public class DesignationModel extends AbstractTableModel
{
private DesignationManagerInterface designationManager;
private java.util.List<DesignationInterface> designations;
private String []columnTitle;
public DesignationModel()
{
populateDataStructures();
}
private void populateDataStructures()
{
this.columnTitle=new String[2];
this.columnTitle[0]="S.No.";
this.columnTitle[1]="Designation";
try
{
this.designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
//???????? 
}
Set<DesignationInterface> blDesignations=designationManager.getDesignations();
this.designations=new LinkedList<>();
for(DesignationInterface blDesignation:blDesignations)
{
this.designations.add(blDesignation);
}
Collections.sort(designations,(first,second)->{return  first.getTitle().compareToIgnoreCase(second.getTitle());});
}
public int getRowCount()
{
return this.designations.size();
}
public int getColumnCount()
{
return this.columnTitle.length;
}
public String getColumnName(int columnIndex)
{
return this.columnTitle[columnIndex];
}
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0) return Integer.class; //something special
return String.class;
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0) return rowIndex+1;
return this.designations.get(rowIndex).getTitle();
}
public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}
//application specific methods
public void add(DesignationInterface designation) throws BLException
{
designationManager.addDesignation(designation);
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface first,DesignationInterface second)
{
return first.getTitle().toUpperCase().compareTo(second.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public int indexOfDesignation(DesignationInterface designation) throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(d.equals(designation)) return index;
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid designation : "+designation.getTitle());
throw blException;
}
public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(partialLeftSearch) if(d.getTitle().toUpperCase().startsWith(title.toUpperCase())) return index;
if(d.getTitle().equalsIgnoreCase(title)) return index;
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid designation : "+title);
throw blException;
}
public void update(DesignationInterface designation) throws BLException
{
designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));
this.designations.add(designation);
Collections.sort(designations,(first,second)->{return  first.getTitle().toUpperCase().compareTo(second.getTitle().toUpperCase());});
fireTableDataChanged();
}
public void remove(int code) throws BLException
{
designationManager.removeDesignation(code);
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
if(iterator.next().getCode()==code) break;
index++;
}
if(index==this.designations.size())
{
BLException blException=new BLException();
blException.setGenericException("Invalid designation code : "+code);
throw blException;
}
this.designations.remove(index);
fireTableDataChanged();
}
public DesignationInterface getDesignationAt(int index) throws BLException
{
if(index<0 || index>this.designations.size())
{
BLException blException=new BLException();
blException.setGenericException("Invalid index : "+index);
throw blException;
}
return this.designations.get(index);
}
public void exportToPdf(File file) throws BLException
{
try
{
PdfWriter pdfWriter=new PdfWriter(file);
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument,PageSize.A4);
int pageNumber=0;
int sNo=0;
boolean newPage=true;
int pageSize=5;
int numberOfPages=this.designations.size()/pageSize;
if((this.designations.size()%pageSize)!=0) numberOfPages++; 
//columnWidthsArray
float []designationTableColumnWidth={1,3};
float []headerTableColumnWidth={0.00001f,0.99999f};

//fonts
PdfFont companyNameFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
PdfFont titleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
PdfFont dataFont=PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
PdfFont designationTitleFont=PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
PdfFont signatureFont=PdfFontFactory.createFont(StandardFonts.HELVETICA);	

//images
Image companyLogo=new Image(ImageDataFactory.create(this.getClass().getResource("/icons/logo.png")));

//paragraphs
Paragraph companyNameParagraph=new Paragraph("Global Company").setFont(companyNameFont).setFontSize(30);
Paragraph title1=new Paragraph("S. No.").setFont(titleFont).setFontSize(16);
Paragraph title2=new Paragraph("Designations").setFont(titleFont).setFontSize(16);
Paragraph designationTitleParagraph=new Paragraph("List of designations").setFont(designationTitleFont).setFontSize(20).setTextAlignment(TextAlignment.CENTER);
Paragraph columnOneData,columnTwoData;
Paragraph signature=new Paragraph("Software By : Atharva Singh Rathore").setFontSize(12).setFont(signatureFont);
Paragraph pageNumberParagraph;
// title cells
Cell titleCellOne=new Cell().add(title1).setTextAlignment(TextAlignment.CENTER);
Cell titleCellTwo=new Cell().add(title2).setTextAlignment(TextAlignment.CENTER);
Cell columnOneCell;
Cell columnTwoCell;
Cell logoCell;
Cell companyNameCell;
Cell designationTitleCell=new Cell(1,2).add(designationTitleParagraph).setTextAlignment(TextAlignment.CENTER);
//tables
Table designationTable=null;
Table headerTable=null;
for(int r=0;r<designations.size();r++)
{
//header
if(newPage)
{
pageNumber++;
pageNumberParagraph=new Paragraph("Page : "+pageNumber+"/"+numberOfPages).setFontSize(12).setMultipliedLeading(4).setTextAlignment(TextAlignment.RIGHT);
headerTable=new Table(UnitValue.createPercentArray(headerTableColumnWidth)).setWidth(UnitValue.createPercentValue(100));
logoCell=new Cell().add(companyLogo);
logoCell.setPadding(0);
logoCell.setBorder(Border.NO_BORDER);
logoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
companyNameCell=new Cell().add(companyNameParagraph);
companyNameCell.setPadding(0);
companyNameCell.setBorder(Border.NO_BORDER);
companyNameCell.setTextAlignment(TextAlignment.CENTER);

companyNameCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
headerTable.addCell(logoCell);
headerTable.addCell(companyNameCell);
designationTable=new Table(UnitValue.createPercentArray(designationTableColumnWidth)).setWidth(UnitValue.createPercentValue(80)).setHorizontalAlignment(HorizontalAlignment.CENTER);
designationTable.addHeaderCell(designationTitleCell);
designationTable.addHeaderCell(titleCellOne);
designationTable.addHeaderCell(titleCellTwo);
document.add(headerTable);
document.add(pageNumberParagraph);
newPage=false;
}
// designationTable
sNo++;
columnOneData=new Paragraph(sNo+"").setFont(dataFont).setFontSize(16);
columnTwoData=new Paragraph(designations.get(r).getTitle()).setFont(dataFont).setFontSize(16);
columnOneCell=new Cell().add(columnOneData).setTextAlignment(TextAlignment.RIGHT);
columnTwoCell=new Cell().add(columnTwoData).setTextAlignment(TextAlignment.LEFT);
designationTable.addCell(columnOneCell);
designationTable.addCell(columnTwoCell);
//footer
if(sNo%pageSize==0 || sNo==designations.size())
{
//create footer
document.add(designationTable);
document.add(signature);
if (sNo<designations.size())
{
document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
newPage=true;
}
}
}
document.close();
}catch(Exception e)
{
BLException pdfException=new BLException();
pdfException.setGenericException(e.getMessage());
throw pdfException;
}

}

//ends Here

}