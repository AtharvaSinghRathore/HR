package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exception.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.io.*;
import javax.swing.filechooser.*;
public class DesignationUI extends JFrame 
{
private JLabel titleLabel;
private JLabel searchLabel;
private JTextField searchTextField;
private JButton clearSearchTextFieldButton;
private JLabel searchErrorLabel;
private DesignationModel designationModel;
private JTable designationTable;
private JTableHeader header;
private JScrollPane scrollPane;
private Container container;
private DesignationPanel designationPanel;
private enum MODE{ADD,VEIW,EDIT,DELETE,EXPORT_TO_PDF};
private MODE mode;
private ImageIcon logoIcon;
private ImageIcon addIcon;
private ImageIcon editIcon;
private ImageIcon deleteIcon;
private ImageIcon cancelIcon;
private ImageIcon pdfIcon;
private ImageIcon saveIcon;
private ImageIcon clearTitleTextFieldIcon;
private ImageIcon clearSearchTextFieldIcon;
public DesignationUI()
{
initComponents();
setApperance();
addListeners();
setViewMode();
designationPanel.setViewMode();
}
private void initComponents()
{
logoIcon=new ImageIcon(this.getClass().getResource("/icons/logo.png"));
addIcon=new ImageIcon(getClass().getResource("/icons/add_icon.png"));
editIcon=new ImageIcon(getClass().getResource("/icons/edit_icon.png"));
deleteIcon=new ImageIcon(getClass().getResource("/icons/delete_icon.png"));
cancelIcon=new ImageIcon(getClass().getResource("/icons/cancel_icon.png"));
pdfIcon=new ImageIcon(getClass().getResource("/icons/pdf_icon.png"));
saveIcon=new ImageIcon(getClass().getResource("/icons/save_icon.png"));
clearTitleTextFieldIcon=new ImageIcon(getClass().getResource("/icons/clear_title.png"));
clearSearchTextFieldIcon=new ImageIcon(getClass().getResource("/icons/clear_text.png"));
setIconImage(logoIcon.getImage());
designationModel=new DesignationModel();
titleLabel=new JLabel("Designations");
searchLabel=new JLabel("Search");
searchTextField=new JTextField();
clearSearchTextFieldButton=new JButton(clearSearchTextFieldIcon);
searchErrorLabel=new JLabel("");
designationTable=new JTable(designationModel);
header=designationTable.getTableHeader();
scrollPane=new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
designationPanel=new DesignationPanel();
container=getContentPane();
}
private void setApperance()
{
Font titleFont=new Font("Verdana",Font.BOLD,18);
Font captionFont=new Font("Verdana",Font.BOLD,16);
Font dataFont=new Font("Verdana",Font.PLAIN,16);
Font columnHeaderFont=new Font("Vedana",Font.BOLD,16);
Font searchErrorFont=new Font("Verdana",Font.BOLD,12);
titleLabel.setFont(titleFont);
searchLabel.setFont(captionFont);
searchTextField.setFont(dataFont);
searchErrorLabel.setFont(searchErrorFont);
searchErrorLabel.setForeground(Color.red);
designationTable.setFont(dataFont);
designationTable.setRowHeight(35);
designationTable.getColumnModel().getColumn(0).setPreferredWidth(100);
designationTable.getColumnModel().getColumn(1).setPreferredWidth(450);
designationTable.setRowSelectionAllowed(true);
designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

header.setFont(columnHeaderFont);
header.setReorderingAllowed(false);
header.setResizingAllowed(false);
container.setLayout(null);
//
int lm,tm;
lm=0;
tm=0;
titleLabel.setBounds(lm+10,tm+10,200,40);
searchErrorLabel.setBounds(lm+10+100+5+400+10-75,tm+10+20+5,100,20);
searchLabel.setBounds(lm+10,tm+10+40+10,100,30);
searchTextField.setBounds(lm+10+100+5,tm+10+40+10,400,30);
clearSearchTextFieldButton.setBounds(lm+10+100+5+400+10,tm+10+40+10,30,30);
scrollPane.setBounds(lm+10,tm+10+40+10+30+10,565,300);
designationPanel.setBounds(lm+10,tm+10+40+10+30+10+300+10,565,190);
container.add(titleLabel);
container.add(searchErrorLabel);
container.add(searchLabel);
container.add(searchTextField);
container.add(clearSearchTextFieldButton);
container.add(scrollPane);
container.add(designationPanel);
int w,h;
w=600;
h=650;
setSize(w,h);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation((d.width/2)-(w/2),(d.height/2)-(h/2));
}
private void addListeners()
{
searchTextField.getDocument().addDocumentListener(new DocumentListener(){
public void changedUpdate(DocumentEvent ev)
{
DesignationUI.this.searchDesignation();
}
public void removeUpdate(DocumentEvent ev)
{
DesignationUI.this.searchDesignation();
}
public void insertUpdate(DocumentEvent ev)
{
DesignationUI.this.searchDesignation();
}
});
clearSearchTextFieldButton.addActionListener((ev)->{
searchTextField.setText("");
searchTextField.requestFocus();
});
designationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
public void valueChanged(ListSelectionEvent ev)
{
int selectedRowIndex=DesignationUI.this.designationTable.getSelectedRow();
try
{
DesignationInterface designation=DesignationUI.this.designationModel.getDesignationAt(selectedRowIndex);
designationPanel.setDesignation(designation);
}catch(BLException blException)
{
designationPanel.clearDesignation();
}

}
});
}
private void searchDesignation()
{
searchErrorLabel.setText("");
String title=searchTextField.getText().trim();
if(title.length()==0) return;
int rowIndex;
try
{
rowIndex=designationModel.indexOfTitle(title,true);
}catch(BLException blException)
{
searchErrorLabel.setText("Not Found");
return;
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rec=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rec);
}
private void setViewMode()
{
this.mode=MODE.VEIW;
if(designationModel.getRowCount()==0)
{
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
else
{
searchTextField.setEnabled(true);
clearSearchTextFieldButton.setEnabled(true);
designationTable.setEnabled(true);
}
}
private void setAddMode()
{
this.mode=MODE.ADD;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setEditMode()
{
this.mode=MODE.EDIT;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setDeleteMode()
{
this.mode=MODE.DELETE;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setExportToPDFMode()
{
this.mode=MODE.EXPORT_TO_PDF;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}


private class DesignationPanel extends JPanel
{
private JLabel titleCaptionLabel;
private JLabel titleLabel;
private JTextField titleTextField;
private JButton clearTitleTextFieldButton;
private JPanel buttonsPanel;
private JButton addButton;
private JButton editButton;
private JButton cancelButton;
private JButton deleteButton;
private JButton exportToPDFButton;
private DesignationInterface designation;
DesignationPanel()
{
setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
initComponents();
setApperance();
addListeners();
}
private void initComponents()
{
titleCaptionLabel=new JLabel("Designation");
titleLabel=new JLabel("");
titleTextField=new JTextField();
clearTitleTextFieldButton=new JButton(clearTitleTextFieldIcon);
buttonsPanel=new JPanel();
addButton=new JButton(addIcon);
editButton=new JButton(editIcon);
cancelButton=new JButton(cancelIcon);
deleteButton=new JButton(deleteIcon);
exportToPDFButton=new JButton(pdfIcon);

}
private void setApperance()
{
Font captionFont=new Font("verdana",Font.BOLD,16);
Font dataFont=new Font("verdana",Font.PLAIN,16);
titleCaptionLabel.setFont(captionFont);
titleLabel.setFont(dataFont);
titleTextField.setFont(dataFont);
setLayout(null);
int lm,tm;
lm=0;
tm=0;
titleCaptionLabel.setBounds(lm+10,tm+20,110,30);
titleLabel.setBounds(lm+10+110+10,tm+20,400,30);
titleTextField.setBounds(lm+10+110+10,tm+20,350,30);
clearTitleTextFieldButton.setBounds(lm+10+110+10+350+10,tm+20,50,30);
buttonsPanel.setBounds(lm+50,tm+20+30+30,465,74);
buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
addButton.setBounds(70,12,50,50);
editButton.setBounds(70+50+20,12,50,50);
cancelButton.setBounds(70+50+20+50+20,12,50,50);
deleteButton.setBounds(70+50+20+50+20+50+20,12,50,50);
exportToPDFButton.setBounds(70+50+20+50+20+50+20+50+20,12,50,50);
buttonsPanel.setLayout(null);
buttonsPanel.add(addButton);	
buttonsPanel.add(editButton);	
buttonsPanel.add(cancelButton);	
buttonsPanel.add(deleteButton);	
buttonsPanel.add(exportToPDFButton);	

add(titleCaptionLabel);
add(titleLabel);
add(titleTextField);
add(clearTitleTextFieldButton);
add(buttonsPanel);
}
private boolean addDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation required");
return false;
}
int rowIndex=0;
DesignationInterface d=new Designation();
d.setTitle(title);
try
{
designationModel.add(d);
try
{
rowIndex=designationModel.indexOfTitle(title,true);
}catch(BLException blException)
{
// do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rec=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rec);
return true;
}catch(BLException blException)
{
if(blException.hasGenericException()) JOptionPane.showMessageDialog(this,blException.getGenericException());
else if(blException.hasException("title")) JOptionPane.showMessageDialog(this,blException.getException("title"));
return false;
}
}
private boolean updateDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation required");
return false;
}
int rowIndex=0;
DesignationInterface d=new Designation();
d.setTitle(title);
d.setCode(this.designation.getCode());
try
{
designationModel.update(d);
try
{
rowIndex=designationModel.indexOfTitle(title,true);
}catch(BLException blException)
{
// do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rec=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rec);
return true;
}catch(BLException blException)
{
if(blException.hasGenericException()) JOptionPane.showMessageDialog(this,blException.getGenericException());
else if(blException.hasException("title")) JOptionPane.showMessageDialog(this,blException.getException("title"));
return false;
}
}
private void removeDesignation()
{
try
{
String title=this.designation.getTitle();
int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+title+" ?","Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.NO_OPTION || selectedOption==JOptionPane.CLOSED_OPTION) return;
designationModel.remove(this.designation.getCode());
JOptionPane.showMessageDialog(this,title+" deleted.");
}catch(BLException blException)
{
if(blException.hasGenericException()) JOptionPane.showMessageDialog(this,blException.getGenericException());
else if(blException.hasException("title")) JOptionPane.showMessageDialog(this,blException.getException("title"));
}
}
private void exportToPDF()
{
JFileChooser jfc=new JFileChooser();
jfc.setCurrentDirectory(new File("."));
jfc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter(){
public boolean accept(java.io.File file)
{
if(file.isDirectory()) return true;
if(file.getName().endsWith(".pdf")) return true;
return false;
}
public java.lang.String getDescription()
{
return "PDF Files";
}
});
int selectedOption=jfc.showSaveDialog(DesignationUI.this);
if(selectedOption==jfc.APPROVE_OPTION)
{
try
{
File selectedFile=jfc.getSelectedFile();
String pdfFile=selectedFile.getAbsolutePath();
if(pdfFile.endsWith(".")) pdfFile+="pdf";
else if(pdfFile.endsWith(".pdf")==false) pdfFile+=".pdf";
File file=new File(pdfFile);
File parent=new File(file.getParent());
if(parent.exists()==false || parent.isDirectory()==false)
{
JOptionPane.showMessageDialog(DesignationUI.this,"Incorrect path : "+file.getAbsolutePath());
return;
}
if(file.exists())
{
selectedOption=JOptionPane.showConfirmDialog(DesignationUI.this,file.getName()+" already exists. Do you want to overwrite it ?","Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.NO_OPTION || selectedOption==JOptionPane.CLOSED_OPTION)  return; 
}
designationModel.exportToPdf(file);
JOptionPane.showMessageDialog(DesignationUI.this,"Data exported to : "+file.getAbsolutePath());
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(DesignationUI.this,blException.getGenericException());
}
}
catch(Exception e)
{
System.out.println(e);
}
}
}
private void addListeners()
{
addButton.addActionListener((ev)->{
if(mode==MODE.VEIW) setAddMode();
else 
{
if(addDesignation()) setViewMode();
else this.titleTextField.requestFocus();
}
});
editButton.addActionListener((ev)->{
if(mode==MODE.VEIW) setEditMode();
else 
{
if(updateDesignation()) setViewMode();
else this.titleTextField.requestFocus();
}
});
cancelButton.addActionListener((ev)->{setViewMode();});
deleteButton.addActionListener((ev)->{
setDeleteMode();
});
exportToPDFButton.addActionListener((ev)->{
setExportToPDFMode();
exportToPDF();
setViewMode();
});
clearTitleTextFieldButton.addActionListener((ev)->{
titleTextField.setText("");
titleTextField.requestFocus(); 
});
}
void setViewMode()
{
DesignationUI.this.setViewMode();
this.titleTextField.setVisible(false);
this.clearTitleTextFieldButton.setVisible(false);
this.titleLabel.setVisible(true);
this.addButton.setEnabled(true);
this.cancelButton.setEnabled(false);
this.addButton.setIcon(addIcon);
this.editButton.setIcon(editIcon);
if(designationModel.getRowCount()==0)
{
this.editButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
}
else
{
this.editButton.setEnabled(true);
this.deleteButton.setEnabled(true);
this.exportToPDFButton.setEnabled(true);
}
}
void setAddMode()
{
DesignationUI.this.setAddMode();
this.titleTextField.setText("");
this.titleLabel.setVisible(false);
this.titleTextField.setVisible(true);
this.titleTextField.requestFocus();
this.clearTitleTextFieldButton.setVisible(true);
this.addButton.setIcon(saveIcon);
this.cancelButton.setEnabled(true);
this.editButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
}      
void setEditMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select designation to edit");
return;
}
DesignationUI.this.setEditMode();
this.titleTextField.setText(designation.getTitle().trim());
this.titleLabel.setVisible(false);
this.titleTextField.setVisible(true);
this.titleTextField.requestFocus();
this.clearTitleTextFieldButton.setVisible(true);
this.cancelButton.setEnabled(true);
this.addButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.editButton.setIcon(saveIcon);
}
void setDeleteMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select designation to delete");
return;
}
DesignationUI.this.setDeleteMode();
this.addButton.setEnabled(false);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.removeDesignation();
this.setViewMode();
}
void setExportToPDFMode()
{
DesignationUI.this.setExportToPDFMode();
this.addButton.setEnabled(false);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
}

public void setDesignation(DesignationInterface designation)
{
this.designation=designation;
titleLabel.setText(designation.getTitle());
}
public void clearDesignation()
{
this.designation=null;
titleLabel.setText("");
} 
}//inner class ends here
}