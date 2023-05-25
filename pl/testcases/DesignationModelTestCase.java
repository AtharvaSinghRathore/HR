import java.awt.*;
import javax.swing.*;
import com.thinking.machines.hr.pl.model.*;
class DesignationModelTestCase extends JFrame
{
private DesignationModel designationModel;
private JTable tb;
private JScrollPane jsp;
private Container container;
DesignationModelTestCase()
{
designationModel=new DesignationModel();
tb=new JTable(designationModel);
jsp=new JScrollPane(tb,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(jsp);
setLocation(300,400);
setSize(200,300);
setVisible(true);
}
public static void main(String gg[])
{
DesignationModelTestCase dmtc=new DesignationModelTestCase();
}
}