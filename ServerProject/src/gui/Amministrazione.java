package gui;

        import connessioni.Server;
        import entita.*;
        import gui.Amministrazione;
        import java.awt.Component;
        import java.awt.Dimension;
        import java.awt.Font;
        import java.awt.GridBagConstraints;
        import java.awt.GridBagLayout;
        import java.awt.Image;
        import java.awt.Insets;
        import java.awt.LayoutManager;
        import java.awt.Toolkit;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.ItemEvent;
        import java.awt.event.ItemListener;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import javax.imageio.ImageIO;
        import javax.swing.*;

public class Amministrazione
        extends JFrame {
    private final int MIN_FRAME_WIDTH = 1024;
    private final int MIN_FRAME_HEIGHT = 768;
    private final int MIN_COMPONENT_WIDTH = 90;
    private final int MIN_COMPONENT_HEIGHT = 20;
    private final int MIN_FONT_SIZE = 14;
    private final String DEFAULT_FONT = "Arial";
    private final int MIN_MAP_WIDTH = 640;
    private final int MIN_MAP_HEIGHT = 360;
    private int uiScaling;
    private Font defaultFont;
    private Dimension componentDimension;
    private GridBagConstraints gridBagConstraints;
    private JPanel mainPanel;
    private JComboBox<String> comboEdifici;
    private JButton btnAggEdificio;
    private JComboBox<String> comboPiani;
    private JButton btnAggPiano;
    private JComboBox<Integer> comboTronchi;
    private JButton btnAggTronco;
    private JComboBox<String> comboAule;
    private JButton btnAggAula;
    private JComboBox<String> comboBeacons;
    private JButton btnAggBeacon;
    private ImageIcon mapView;
    private JLabel mapLabel;
    private JButton caricamento;

    public Amministrazione() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
        setSize(MIN_FRAME_WIDTH * uiScaling, MIN_FRAME_HEIGHT * uiScaling);
        setTitle("Interfaccia di amministrazione");
        setVisible(true);
        defaultFont = new Font(DEFAULT_FONT, 0, MIN_FONT_SIZE * uiScaling);
        setDefaultCloseOperation(1);
        componentDimension = new Dimension(MIN_COMPONENT_WIDTH * uiScaling, MIN_COMPONENT_HEIGHT * uiScaling);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(MIN_FRAME_WIDTH * uiScaling, MIN_FRAME_HEIGHT * uiScaling);
        gridBagConstraints = new GridBagConstraints();
        add(mainPanel);
        setResizable(false);
        aggiungiElementiIniziali();
        definisciListeners();
    }

    private void aggiungiElementiIniziali() {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = 10;
        gridBagConstraints.fill = 1;
        gridBagConstraints.insets = new Insets(10 * uiScaling, 17 * uiScaling, 0, 17 * uiScaling);
        JLabel[] labels = new JLabel[5];
        labels[0] = new JLabel("Seleziona edificio:");
        labels[0].setFont(defaultFont);
        labels[1] = new JLabel("Seleziona piano:");
        labels[1].setFont(defaultFont);
        labels[2] = new JLabel("Seleziona tronco:");
        labels[2].setFont(defaultFont);
        labels[3] = new JLabel("Seleziona aula:");
        labels[3].setFont(defaultFont);
        labels[4] = new JLabel("Seleziona beacon:");
        labels[4].setFont(defaultFont);
        ArrayList edificiList = DAOEdificio.selectEdifici();
        String[] edifici = new String[edificiList.size()];
        for (int i = 0; i < edifici.length; ++i) {
            edifici[i] = (String) edificiList.get(i);
        }
        comboEdifici = new JComboBox<String>(edifici);
        comboEdifici.setSize(componentDimension);
        comboEdifici.setFont(defaultFont);
        comboEdifici.setSelectedIndex(-1);
        btnAggEdificio = new JButton("+ edificio");
        btnAggEdificio.setPreferredSize(componentDimension);
        btnAggEdificio.setFont(defaultFont);
        mainPanel.add((Component) labels[0], gridBagConstraints);
        gridBagConstraints.gridy = 1;
        mainPanel.add(comboEdifici, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        mainPanel.add((Component) btnAggEdificio, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = 18;
        comboPiani = new JComboBox();
        comboPiani.setSize(componentDimension);
        comboPiani.setFont(defaultFont);
        comboPiani.setEnabled(false);
        btnAggPiano = new JButton("+ piano");
        btnAggPiano.setPreferredSize(componentDimension);
        btnAggPiano.setFont(defaultFont);
        btnAggPiano.setEnabled(false);
        mainPanel.add((Component) labels[1], gridBagConstraints);
        gridBagConstraints.gridy = 1;
        mainPanel.add(comboPiani, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        mainPanel.add((Component) btnAggPiano, gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = 18;
        comboTronchi = new JComboBox();
        comboTronchi.setSize(componentDimension);
        comboTronchi.setFont(defaultFont);
        comboTronchi.setSelectedIndex(-1);
        comboTronchi.setEnabled(false);
        btnAggTronco = new JButton("+ tronco");
        btnAggTronco.setPreferredSize(componentDimension);
        btnAggTronco.setFont(defaultFont);
        btnAggTronco.setEnabled(false);
        mainPanel.add((Component) labels[2], gridBagConstraints);
        gridBagConstraints.gridy = 1;
        mainPanel.add(comboTronchi, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        mainPanel.add((Component) btnAggTronco, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = 18;
        comboAule = new JComboBox();
        comboAule.setSize(componentDimension);
        comboAule.setFont(defaultFont);
        comboAule.setSelectedIndex(-1);
        comboAule.setEnabled(false);
        btnAggAula = new JButton("+ aula");
        btnAggAula.setPreferredSize(componentDimension);
        btnAggAula.setFont(defaultFont);
        btnAggAula.setEnabled(false);
        mainPanel.add((Component) labels[3], gridBagConstraints);
        gridBagConstraints.gridy = 1;
        mainPanel.add(comboAule, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        mainPanel.add((Component) btnAggAula, gridBagConstraints);
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = 18;
        comboBeacons = new JComboBox();
        comboBeacons.setSize(componentDimension);
        comboBeacons.setFont(defaultFont);
        comboBeacons.setSelectedIndex(-1);
        comboBeacons.setEnabled(false);
        btnAggBeacon = new JButton("+ beacon");
        btnAggBeacon.setPreferredSize(componentDimension);
        btnAggBeacon.setFont(defaultFont);
        btnAggBeacon.setEnabled(false);
        mainPanel.add(labels[4], gridBagConstraints);
        gridBagConstraints.gridy = 1;
        mainPanel.add(comboBeacons, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        mainPanel.add(btnAggBeacon, gridBagConstraints);
        pack();
        setResizable(false);
    }

    private void definisciListeners() {
        comboEdifici.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    comboPiani.removeAllItems();
                    comboTronchi.removeAllItems();
                    comboAule.removeAllItems();
                    comboBeacons.removeAllItems();
                    if (caricamento != null) {
                        mainPanel.remove(caricamento);
                    }
                    if (mapLabel != null) {
                        mainPanel.remove(mapLabel);
                    }
                    mapView = null;
                    String edificio = (String)comboEdifici.getSelectedItem();
                    if (!edificio.isEmpty()) {
                        ArrayList pianiList = DAOPiano.selectListaPianiByEdificio(edificio);
                        for (int i = 0; i < pianiList.size(); ++i) {
                            comboPiani.addItem((String)pianiList.get(i));
                        }
                        comboPiani.setEnabled(true);
                        btnAggPiano.setEnabled(true);
                    } else {
                        comboPiani.setEnabled(false);
                        btnAggPiano.setEnabled(false);
                    }
                    pack();
                    setResizable(false);
                }
            }
        });
        comboPiani.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    String piano;
                    comboAule.removeAllItems();
                    comboTronchi.removeAllItems();
                    comboBeacons.removeAllItems();
                    mapView = null;
                    if (caricamento != null) {
                        mainPanel.remove(caricamento);
                    }
                    if (mapLabel != null) {
                        mainPanel.remove(mapLabel);
                    }
                    if (!(piano = (String)comboPiani.getSelectedItem()).isEmpty()) {
                        comboAule.removeAllItems();
                        ArrayList auleList = DAOAula.selectListaAuleByPiano((String)piano);
                        for (int i = 0; i < auleList.size(); ++i) {
                            comboAule.addItem((String)auleList.get(i));
                        }
                        comboAule.setEnabled(true);
                        btnAggAula.setEnabled(true);
                        HashMap<Integer, Tronco> tronchiMap = DAOTronco.selectTronchiDelPiano((String)piano);
                        for (Integer key : tronchiMap.keySet()) {
                            comboTronchi.addItem(key);
                        }
                        comboTronchi.setEnabled(true);
                        btnAggTronco.setEnabled(true);
                        try {
                            Image mappa = getMappa(piano);
                            mapView = new ImageIcon(mappa);
                            gridBagConstraints.fill = 2;
                            gridBagConstraints.ipady = 40;
                            gridBagConstraints.weightx = 0.0;
                            gridBagConstraints.gridwidth = 5;
                            gridBagConstraints.gridx = 0;
                            gridBagConstraints.gridy = 5;
                            mapLabel = new JLabel(mapView);
                            mainPanel.add((Component)mapLabel, gridBagConstraints);
                        }
                        catch (IOException e1) {
                            gridBagConstraints.fill = 2;
                            gridBagConstraints.ipady = 40;
                            gridBagConstraints.weightx = 0.0;
                            gridBagConstraints.gridwidth = 5;
                            gridBagConstraints.gridx = 0;
                            gridBagConstraints.gridy = 5;
                            caricamento = new JButton("Carica una mappa...");
                            caricamento.setFont(defaultFont);
                            caricamento.addActionListener(new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    JFileChooser imgOpen = new JFileChooser();
                                    int returnVal = imgOpen.showOpenDialog(Amministrazione.this);
                                    if (returnVal == 0) {
                                        String pathS = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/ServerProject.jar", "")+"/Mappe/q"+piano+".jpg";
                                        File file = imgOpen.getSelectedFile();
                                        try {
                                            BufferedImage imgMappa = ImageIO.read(file);
                                            File outputfile = new File((String)((Object)pathS));
                                            ImageIO.write(imgMappa, "jpg", outputfile);
                                            try {
                                                DAOMappa.insertMappaPiano((String)piano);
                                            }
                                            catch (Exception exception) {
                                                // empty catch block
                                            }
                                        }
                                        catch (IOException e2) {
                                            e2.printStackTrace();
                                        }
                                        finally {
                                            mainPanel.remove(caricamento);
                                            Image mappa = null;
                                            try {
                                                mappa = getMappa(piano);
                                                mapView = new ImageIcon(mappa);
                                                gridBagConstraints.fill = 2;
                                                gridBagConstraints.ipady = 40;
                                                gridBagConstraints.weightx = 0.0;
                                                gridBagConstraints.gridwidth = 5;
                                                gridBagConstraints.gridx = 0;
                                                gridBagConstraints.gridy = 5;
                                                mapLabel = new JLabel(mapView);
                                                mainPanel.add(mapLabel,gridBagConstraints);
                                                pack();
                                                setResizable(false);
                                            }
                                            catch (IOException e2) {
                                                e2.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                            mainPanel.add((Component)caricamento, gridBagConstraints);
                        }
                    } else {
                        comboAule.setEnabled(false);
                        btnAggAula.setEnabled(false);
                        comboTronchi.setEnabled(false);
                        btnAggTronco.setEnabled(false);
                    }
                    pack();
                    setResizable(false);
                }
            }
        });
        comboTronchi.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    comboBeacons.removeAllItems();
                    Integer tronco = (Integer)comboTronchi.getSelectedItem();
                    if (tronco != null) {
                        ArrayList beaconsList = DAOBeacon.selectListaBeaconsByTronco((int)tronco);
                        for (int i = 0; i < beaconsList.size(); ++i) {
                            comboBeacons.addItem((String)beaconsList.get(i));
                        }
                        comboBeacons.setEnabled(true);
                        btnAggBeacon.setEnabled(true);
                    } else {
                        comboBeacons.setEnabled(false);
                        btnAggBeacon.setEnabled(false);
                    }
                    pack();
                    setResizable(false);
                }
            }
        });
    }

    private Image getMappa(String piano) throws IOException {
        String path = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/ServerProject.jar", "");
        String link = DAOMappa.selectMappaByPiano((String) piano);
        File mappa = new File(path + link);
        BufferedImage imgMappa = null;
        imgMappa = ImageIO.read(mappa);
        return imgMappa.getScaledInstance(MIN_MAP_WIDTH * uiScaling, MIN_MAP_HEIGHT * uiScaling, 4);
    }
}