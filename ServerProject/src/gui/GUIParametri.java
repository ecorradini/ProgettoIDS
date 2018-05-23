package gui;

import entita.DAOEdificio;
import entita.DAOParametri;
import entita.DAOPiano;

import javax.swing.*;
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
        tableParametri = new JTable(info.size(), 6);

    }
}
