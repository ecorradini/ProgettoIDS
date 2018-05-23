package gui;

import entita.DAOEdificio;
import entita.DAOParametri;
import entita.DAOPiano;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;

public class GUIParametri extends JFrame {

    private final int MIN_FRAME_WIDTH = 1024;
    private final int MIN_FRAME_HEIGHT = 768;
    private final int MIN_COMPONENT_WIDTH = 90;
    private final int MIN_COMPONENT_HEIGHT = 20;
    private final int MIN_FONT_SIZE = 14;
    private final String DEFAULT_FONT = "Arial";
    private int uiScaling;
    private Font defaultFont;
    private Dimension componentDimension;
    private GridBagConstraints gridBagConstraints;
    private JPanel mainPanel;

    private JTable tableParametri;

    public GUIParametri() {
        try {
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        uiScaling = dim.width / 1024;
        if(uiScaling<=0) uiScaling=1;
        setTitle("Interfaccia di modifica parametri");
        setVisible(true);
        defaultFont = new Font(DEFAULT_FONT, 0, MIN_FONT_SIZE * uiScaling);
        setDefaultCloseOperation(1);
        componentDimension = new Dimension(MIN_COMPONENT_WIDTH * uiScaling, MIN_COMPONENT_HEIGHT * uiScaling);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(MIN_FRAME_WIDTH * uiScaling, MIN_FRAME_HEIGHT * uiScaling);
        gridBagConstraints = new GridBagConstraints();
        add(mainPanel);
        setResizable(false);
        setLocationRelativeTo(null);
        aggiungiElementiIniziali();
    }

    private void aggiungiElementiIniziali() {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = 10;
        gridBagConstraints.fill = 1;
        gridBagConstraints.insets = new Insets(10 * uiScaling, 17 * uiScaling, 0, 17 * uiScaling);


        ArrayList<String[]> info = DAOParametri.selectAllParametri();
        String[][] matriceTabella = new String[info.size()][6];
        for(int i=0; i<info.size(); i++) {
            matriceTabella[i] = info.get(i);
        }
        String[] columnNames = new String[] { "EDIFICIO","PIANO","TRONCO","VULNERABILITA","RISCHIO VITA","PRESENZA FUMO"};
        tableParametri = new JTable(matriceTabella, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || column == 1 || column==2);
            }
        };
        tableParametri.setFont(defaultFont);
        TableColumnModel columnModel = tableParametri.getColumnModel();
        columnModel.getColumn(0).setMinWidth(150*uiScaling);
        columnModel.getColumn(1).setMinWidth(150*uiScaling);
        columnModel.getColumn(2).setMinWidth(150*uiScaling);
        columnModel.getColumn(3).setMinWidth(150*uiScaling);
        columnModel.getColumn(4).setMinWidth(150*uiScaling);
        columnModel.getColumn(5).setMinWidth(150*uiScaling);
        JTableHeader header = tableParametri.getTableHeader();
        header.setFont(defaultFont);
        tableParametri.setRowHeight(defaultFont.getSize()+5);
        tableParametri.setPreferredSize(new Dimension(920*uiScaling,300*uiScaling));

        tableParametri.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int firstRow = e.getFirstRow();
                int lastRow = e.getLastRow();

                switch (e.getType()) {
                    case TableModelEvent.UPDATE:
                        for (int i = firstRow; i <= lastRow; i++) {
                            int tronco = Integer.parseInt((String)tableParametri.getValueAt(i,0));
                            float vulnerabilita = Float.parseFloat((String)tableParametri.getValueAt(i,3));
                            float rischiovita = Float.parseFloat((String)tableParametri.getValueAt(i,4));
                            float presenzafumo = Float.parseFloat((String)tableParametri.getValueAt(i,5));

                            DAOParametri.updateTronco(tronco,vulnerabilita,rischiovita,presenzafumo);
                        }
                        break;
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableParametri);
        scrollPane.setPreferredSize(new Dimension(920*uiScaling,300*uiScaling));
        mainPanel.add(scrollPane, gridBagConstraints);
        pack();
    }
}
