package gui;

import connessioni.Server;
        import entita.*;

import java.awt.*;
        import java.awt.event.*;
import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;
        import java.sql.SQLException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import javax.imageio.ImageIO;
        import javax.swing.*;

public class GUIAmministrazione extends JFrame {

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
    private ImagePanel mapView;
    private JButton caricamento;
    private JLabel labelCoordinate;

    private JPanel aggEdificio;
    private JPanel aggPiano;
    private JPanel aggAula;
    private JPanel aggTronco;
    private JPanel aggBeacon;

    public GUIAmministrazione() {
        super("Interfaccia di amministrazione");
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
        definisciListeners();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        btnAggEdificio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(aggPiano!=null) {
                    aggPiano.setVisible(false);
                }
                if(aggAula!=null) {
                    aggAula.setVisible(false);
                }
                if(aggTronco!=null) {
                    aggTronco.setVisible(false);
                }
                if(aggBeacon!=null) {
                    aggBeacon.setVisible(false);
                }
                aggiungiEdificio();
            }
        });
        mainPanel.add(labels[0], gridBagConstraints);
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
        btnAggPiano.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(aggEdificio!=null) {
                    aggEdificio.setVisible(false);
                }
                if(aggAula!=null) {
                    aggAula.setVisible(false);
                }
                if(aggTronco!=null) {
                    aggTronco.setVisible(false);
                }
                if(aggBeacon!=null) {
                    aggBeacon.setVisible(false);
                }
                aggiungiPiano();
            }
        });
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
        btnAggTronco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(aggPiano!=null) {
                    aggPiano.setVisible(false);
                }
                if(aggAula!=null) {
                    aggAula.setVisible(false);
                }
                if(aggEdificio!=null) {
                    aggEdificio.setVisible(false);
                }
                if(aggBeacon!=null) {
                    aggBeacon.setVisible(false);
                }
                aggiungiTronco();
            }
        });
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
        btnAggAula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(aggPiano!=null) {
                    aggPiano.setVisible(false);
                }
                if(aggEdificio!=null) {
                    aggEdificio.setVisible(false);
                }
                if(aggTronco!=null) {
                    aggTronco.setVisible(false);
                }
                if(aggBeacon!=null) {
                    aggBeacon.setVisible(false);
                }
                aggiungiAula();
            }
        });
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
        btnAggBeacon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(aggPiano!=null) {
                    aggPiano.setVisible(false);
                }
                if(aggAula!=null) {
                    aggAula.setVisible(false);
                }
                if(aggTronco!=null) {
                    aggTronco.setVisible(false);
                }
                if(aggEdificio!=null) {
                    aggEdificio.setVisible(false);
                }
                aggiungiBeacon();
            }
        });
        mainPanel.add(labels[4], gridBagConstraints);
        gridBagConstraints.gridy = 1;
        mainPanel.add(comboBeacons, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        mainPanel.add(btnAggBeacon, gridBagConstraints);
        aggiornaUI();
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
                    if (mapView != null) {
                        mainPanel.remove(mapView);
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
                    aggiornaUI();
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
                    if (caricamento != null) {
                        mainPanel.remove(caricamento);
                    }
                    if (mapView != null) {
                        mainPanel.remove(mapView);
                    }
                    mapView=null;
                    if(labelCoordinate!=null) {
                        mainPanel.remove(labelCoordinate);
                    }
                    labelCoordinate=null;
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
                            BufferedImage mappa = getMappa(piano);
                            mapView = new ImagePanel(mappa);
                            gridBagConstraints.fill = GridBagConstraints.NONE;
                            gridBagConstraints.ipady = 40;
                            gridBagConstraints.weightx = 0.0;
                            gridBagConstraints.gridwidth = 5;
                            gridBagConstraints.gridx = 0;
                            gridBagConstraints.gridy = 3;
                            mainPanel.add(mapView, gridBagConstraints);
                            mapView.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    Point panelPoint = e.getPoint();

                                    if(labelCoordinate==null) {
                                        labelCoordinate = new JLabel("(X=" + panelPoint.x + "; Y:" + ((panelPoint.y-17) > 0 ? (panelPoint.y-17) : 0) + ")");
                                        labelCoordinate.setFont(defaultFont);
                                        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
                                        gridBagConstraints.weightx = 0.0;
                                        gridBagConstraints.gridwidth = 1;
                                        gridBagConstraints.gridx = 0;
                                        gridBagConstraints.gridy = 4;
                                        mainPanel.add(labelCoordinate,gridBagConstraints);
                                    }
                                    else {
                                        labelCoordinate.setText("(X=" + panelPoint.x + "; Y:" + ((panelPoint.y-17) > 0 ? (panelPoint.y-17) : 0) + ")");
                                    }

                                    aggiornaUI();
                                }
                            });
                            aggiornaUI();
                        }
                        catch (IOException e1) {
                            gridBagConstraints.fill = GridBagConstraints.NONE;
                            gridBagConstraints.ipady = 40;
                            gridBagConstraints.weightx = 0.0;
                            gridBagConstraints.gridwidth = 5;
                            gridBagConstraints.gridx = 0;
                            gridBagConstraints.gridy = 3;
                            caricamento = new JButton("Carica una mappa...");
                            caricamento.setFont(defaultFont);
                            caricamento.addActionListener(new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    JFileChooser imgOpen = new JFileChooser();
                                    int returnVal = imgOpen.showOpenDialog(GUIAmministrazione.this);
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
                                            BufferedImage mappa = null;
                                            try {
                                                mappa = getMappa(piano);
                                                mapView = new ImagePanel(mappa);
                                                gridBagConstraints.fill = GridBagConstraints.CENTER;
                                                gridBagConstraints.insets = new Insets(0,0,0,0);
                                                gridBagConstraints.ipady = 40;
                                                gridBagConstraints.weightx = 0.0;
                                                gridBagConstraints.gridwidth = 5;
                                                gridBagConstraints.gridx = 0;
                                                gridBagConstraints.gridy = 3;
                                                mainPanel.add(mapView,gridBagConstraints);
                                                mapView.addMouseListener(new MouseAdapter() {
                                                    @Override
                                                    public void mouseClicked(MouseEvent e) {
                                                        Point panelPoint = e.getPoint();

                                                        if(labelCoordinate==null) {
                                                            labelCoordinate = new JLabel("(X=" + panelPoint.x + "; Y:" + ((panelPoint.y-17) > 0 ? (panelPoint.y-17) : 0) + ")");
                                                            labelCoordinate.setFont(defaultFont);
                                                            gridBagConstraints.weightx = 0.0;
                                                            gridBagConstraints.gridwidth = 1;
                                                            gridBagConstraints.gridx = 0;
                                                            gridBagConstraints.gridy = 4;
                                                            mainPanel.add(labelCoordinate,gridBagConstraints);
                                                        }
                                                        else {
                                                            labelCoordinate.setText("(X=" + panelPoint.x + "; Y:" + ((panelPoint.y-17) > 0 ? (panelPoint.y-17) : 0) + ")");
                                                        }

                                                        aggiornaUI();
                                                    }
                                                });
                                                aggiornaUI();
                                            }
                                            catch (IOException e2) {
                                                e2.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                            mainPanel.add(caricamento, gridBagConstraints);
                        }
                    } else {
                        comboAule.setEnabled(false);
                        btnAggAula.setEnabled(false);
                        comboTronchi.setEnabled(false);
                        btnAggTronco.setEnabled(false);
                    }
                    aggiornaUI();
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
                        if(mapView!=null) {
                            int[] coordinate = DAOTronco.selectCoordinateTronco(tronco);
                            mapView.paintTronco(coordinate[0], coordinate[1], coordinate[2], coordinate[3]);
                        }
                    } else {
                        comboBeacons.setEnabled(false);
                        btnAggBeacon.setEnabled(false);
                    }
                    aggiornaUI();
                }
            }
        });
    }

    private BufferedImage getMappa(String piano) throws IOException {
        String path = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/ServerProject.jar", "");
        String link = DAOMappa.selectMappaByPiano(piano);
        File mappa = new File(path + link);
        BufferedImage imgMappa = ImageIO.read(mappa);
        Image tmp = imgMappa.getScaledInstance(MIN_MAP_WIDTH * uiScaling, MIN_MAP_HEIGHT * uiScaling, 4);
        BufferedImage resized = new BufferedImage(MIN_MAP_WIDTH * uiScaling, MIN_MAP_HEIGHT * uiScaling,BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp,0,0,null);
        g2d.dispose();

        return resized;
    }

    private void aggiungiEdificio() {
        if(aggEdificio==null) {
            aggEdificio = new JPanel(new GridBagLayout());

            //Definisco gli elementi che fanno parte della form
            GridBagConstraints constraintsInnner = new GridBagConstraints();
            JLabel lNome = new JLabel("Nome:");
            lNome.setFont(defaultFont);
            JTextField tNome = new JTextField(20);
            tNome.setFont(defaultFont);
            JLabel lNomeError = new JLabel("Per favore inserisci il nome!");
            lNomeError.setFont(defaultFont);
            lNomeError.setForeground(Color.RED);
            lNomeError.setVisible(false);
            JButton confirm = new JButton("Conferma");
            confirm.setFont(defaultFont);
            confirm.setSize(componentDimension);

            //Aggiungo gli elementi al panel
            //LABEL NOME
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggEdificio.add(lNome, constraintsInnner);
            //TEXT NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggEdificio.add(tNome, constraintsInnner);
            //TEXT ERRORE NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 1;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggEdificio.add(lNomeError, constraintsInnner);
            //BUTTON CONFERMA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            constraintsInnner.insets = new Insets(25, 0, 0, 10);
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(tNome.getText().equals("")) {
                        lNomeError.setVisible(true);
                    }
                    else {
                        lNomeError.setVisible(false);
                        try {
                            DAOEdificio.insertEdificio(tNome.getText());
                        } catch (SQLException e1) {
                        }
                        finally {
                            comboEdifici.addItem(tNome.getText());
                            aggEdificio.setVisible(false);
                            tNome.setText("");
                            aggiornaUI();
                        }
                    }
                }
            });
            aggEdificio.add(confirm, constraintsInnner);


            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.ipady = 40;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridheight = 4;
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new Insets(10 * uiScaling, 17 * uiScaling, 0, 17 * uiScaling);
            mainPanel.add(aggEdificio,gridBagConstraints);

            aggEdificio.setVisible(true);
        }
        else if(aggEdificio.isVisible()) {
            aggEdificio.setVisible(false);
        }
        else {
            aggEdificio.setVisible(true);
        }
        aggiornaUI();
    }

    private void aggiungiPiano() {
        if(aggPiano==null) {
            aggPiano = new JPanel(new GridBagLayout());

            //Definisco gli elementi che fanno parte della form
            GridBagConstraints constraintsInnner = new GridBagConstraints();
            JLabel lNome = new JLabel("Nome:");
            lNome.setFont(defaultFont);
            JTextField tNome = new JTextField(20);
            tNome.setFont(defaultFont);
            JLabel lNomeError = new JLabel("Per favore inserisci il nome!");
            lNomeError.setFont(defaultFont);
            lNomeError.setForeground(Color.RED);
            lNomeError.setVisible(false);
            JButton confirm = new JButton("Conferma");
            confirm.setFont(defaultFont);
            confirm.setSize(componentDimension);

            //Aggiungo gli elementi al panel
            //LABEL NOME
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggPiano.add(lNome, constraintsInnner);
            //TEXT NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggPiano.add(tNome, constraintsInnner);
            //TEXT ERRORE NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 1;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggPiano.add(lNomeError, constraintsInnner);
            //BUTTON CONFERMA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            constraintsInnner.insets = new Insets(25, 0, 0, 10);
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(tNome.getText().equals("")) {
                        lNomeError.setVisible(true);
                    }
                    else {
                        lNomeError.setVisible(false);
                        try {
                            DAOPiano.insertPiano(tNome.getText(),(String)comboEdifici.getSelectedItem());
                        } catch (SQLException e1) {
                        }
                        finally {
                            comboPiani.addItem(tNome.getText());
                            aggPiano.setVisible(false);
                            tNome.setText("");
                            aggiornaUI();
                        }
                    }
                }
            });
            aggPiano.add(confirm, constraintsInnner);


            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.ipady = 40;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridheight = 4;
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new Insets(10 * uiScaling, 17 * uiScaling, 0, 17 * uiScaling);
            mainPanel.add(aggPiano,gridBagConstraints);

            aggPiano.setVisible(true);
        }
        else if(aggPiano.isVisible()) {
            aggPiano.setVisible(false);
        }
        else {
            aggPiano.setVisible(true);
        }
        aggiornaUI();
    }

    private void aggiungiAula() {
        if(aggAula==null) {
            aggAula = new JPanel(new GridBagLayout());

            //Definisco gli elementi che fanno parte della form
            GridBagConstraints constraintsInnner = new GridBagConstraints();
            JLabel lNome = new JLabel("Nome:");
            lNome.setFont(defaultFont);
            JTextField tNome = new JTextField(20);
            tNome.setFont(defaultFont);
            JLabel lNomeError = new JLabel("Per favore inserisci il nome!");
            lNomeError.setFont(defaultFont);
            lNomeError.setForeground(Color.RED);
            lNomeError.setVisible(false);

            JLabel lX = new JLabel("X:");
            lX.setFont(defaultFont);
            JTextField tX = new JTextField(6);
            tX.setFont(defaultFont);
            JLabel lXError = new JLabel("Per favore inserisci la coordinata X!");
            lXError.setFont(defaultFont);
            lXError.setForeground(Color.RED);
            lXError.setVisible(false);

            JLabel lY = new JLabel("Y:");
            lY.setFont(defaultFont);
            JTextField tY = new JTextField(6);
            tY.setFont(defaultFont);
            JLabel lYError = new JLabel("Per favore inserisci la coordinata Y!");
            lYError.setFont(defaultFont);
            lYError.setForeground(Color.RED);
            lYError.setVisible(false);

            JLabel lEntrata = new JLabel("Entrata:");
            lEntrata.setFont(defaultFont);
            JTextField tEntrata = new JTextField(20);
            tEntrata.setFont(defaultFont);
            JLabel lEntrataError = new JLabel("Per favore inserisci l'id del beacon di entrata!");
            lEntrataError.setFont(defaultFont);
            lEntrataError.setForeground(Color.RED);
            lEntrataError.setVisible(false);


            JButton confirm = new JButton("Conferma");
            confirm.setFont(defaultFont);
            confirm.setSize(componentDimension);

            //Aggiungo gli elementi al panel
            //LABEL NOME
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lNome, constraintsInnner);
            //TEXT NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(tNome, constraintsInnner);
            //TEXT ERRORE NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 1;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lNomeError, constraintsInnner);
            //LABEL X
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lX, constraintsInnner);
            //TEXT X
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(tX, constraintsInnner);
            //TEXT ERRORE X
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 3;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lXError, constraintsInnner);
            //LABEL Y
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 4;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lY, constraintsInnner);
            //TEXT Y
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 4;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(tY, constraintsInnner);
            //TEXT ERROR Y
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 5;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lYError, constraintsInnner);
            //LABEL ENTRATA
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 6;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lEntrata, constraintsInnner);
            //TEXT ENTRATA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 6;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(tEntrata, constraintsInnner);
            //TEXT ERRORE ENTRATA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 7;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggAula.add(lEntrataError, constraintsInnner);
            //BUTTON CONFERMA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 8;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            constraintsInnner.insets = new Insets(25, 0, 0, 10);
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(tNome.getText().equals("")) {
                        lNomeError.setVisible(true);
                    }
                    else if(tX.getText().equals("")) {
                        lXError.setVisible(true);
                    }
                    else if(tY.getText().equals("")) {
                        lYError.setVisible(true);
                    }
                    else if(tEntrata.getText().equals("")) {
                        lEntrataError.setVisible(true);
                    }
                    else {
                        lNomeError.setVisible(false);
                        lXError.setVisible(false);
                        lYError.setVisible(false);
                        lEntrataError.setVisible(false);
                        try {
                            DAOAula.insertAula(tNome.getText(),(String)comboPiani.getSelectedItem(),
                                    tX.getText(),tY.getText(),tEntrata.getText());
                        } catch (SQLException e1) {
                        }
                        finally {
                            comboAule.addItem(tNome.getText());
                            aggAula.setVisible(false);
                            tNome.setText("");
                            tX.setText("");
                            tY.setText("");
                            tEntrata.setText("");
                            aggiornaUI();
                        }
                    }
                }
            });
            aggAula.add(confirm, constraintsInnner);


            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.ipady = 40;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridheight = 4;
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new Insets(10 * uiScaling, 17 * uiScaling, 0, 17 * uiScaling);
            mainPanel.add(aggAula,gridBagConstraints);

            aggAula.setVisible(true);
        }
        else if(aggAula.isVisible()) {
            aggAula.setVisible(false);
        }
        else {
            aggAula.setVisible(true);
        }
        aggiornaUI();
    }

    private void aggiungiTronco() {
        if(aggTronco==null) {
            aggTronco = new JPanel(new GridBagLayout());

            //Definisco gli elementi che fanno parte della form
            GridBagConstraints constraintsInnner = new GridBagConstraints();
            JLabel lX = new JLabel("X:");
            lX.setFont(defaultFont);
            JTextField tX = new JTextField(6);
            tX.setFont(defaultFont);
            JLabel lXError = new JLabel("Per favore inserisci la X!");
            lXError.setFont(defaultFont);
            lXError.setForeground(Color.RED);
            lXError.setVisible(false);

            JLabel lY = new JLabel("Y:");
            lY.setFont(defaultFont);
            JTextField tY = new JTextField(6);
            tY.setFont(defaultFont);
            JLabel lYError = new JLabel("Per favore inserisci la Y!");
            lYError.setFont(defaultFont);
            lYError.setForeground(Color.RED);
            lYError.setVisible(false);

            JLabel lXF = new JLabel("XF:");
            lXF.setFont(defaultFont);
            JTextField tXF = new JTextField(6);
            tXF.setFont(defaultFont);
            JLabel lXFError = new JLabel("Per favore inserisci la X finale!");
            lXFError.setFont(defaultFont);
            lXFError.setForeground(Color.RED);
            lXFError.setVisible(false);

            JLabel lYF = new JLabel("YF:");
            lYF.setFont(defaultFont);
            JTextField tYF = new JTextField(6);
            tYF.setFont(defaultFont);
            JLabel lYFError = new JLabel("Per favore inserisci Y finale!");
            lYFError.setFont(defaultFont);
            lYFError.setForeground(Color.RED);
            lYFError.setVisible(false);

            JLabel lLarghezza = new JLabel("Larghezza:");
            lLarghezza.setFont(defaultFont);
            JTextField tLarghezza = new JTextField(20);
            tLarghezza.setFont(defaultFont);
            JLabel lLarghezzaError = new JLabel("Per favore inserisci la larghezza!");
            lLarghezzaError.setFont(defaultFont);
            lLarghezzaError.setForeground(Color.RED);
            lLarghezzaError.setVisible(false);

            JLabel lLunghezza = new JLabel("Lunghezza:");
            lLunghezza.setFont(defaultFont);
            JTextField tLunghezza = new JTextField(20);
            tLunghezza.setFont(defaultFont);
            JLabel lLunghezzaError = new JLabel("Per favore inserisci la lunghezza!");
            lLunghezzaError.setFont(defaultFont);
            lLunghezzaError.setForeground(Color.RED);
            lLunghezzaError.setVisible(false);


            JButton confirm = new JButton("Conferma");
            confirm.setFont(defaultFont);
            confirm.setSize(componentDimension);

            //Aggiungo gli elementi al panel
            //LABEL X
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lX, constraintsInnner);
            //TEXT X
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(tX, constraintsInnner);
            //TEXT ERRORE X
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 1;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lXError, constraintsInnner);
            //LABEL Y
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lY, constraintsInnner);
            //TEXT Y
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(tY, constraintsInnner);
            //TEXT ERRORE Y
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 3;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lYError, constraintsInnner);
            //LABEL XF
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 4;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lXF, constraintsInnner);
            //TEXT XF
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 4;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(tXF, constraintsInnner);
            //TEXT ERROR XF
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 5;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lXFError, constraintsInnner);
            //LABEL YF
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 6;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lYF, constraintsInnner);
            //TEXT YF
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 6;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(tYF, constraintsInnner);
            //TEXT ERROR YF
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 7;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lYFError, constraintsInnner);
            //LABEL LUNGHEZZA
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 8;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lLunghezza, constraintsInnner);
            //TEXT LUNGHEZZA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 8;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(tLunghezza, constraintsInnner);
            //TEXT ERRORE ENTRATA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 9;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lLunghezzaError, constraintsInnner);
            //LABEL LARGHEZZA
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 10;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lLarghezza, constraintsInnner);
            //TEXT LUNGHEZZA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 10;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(tLarghezza, constraintsInnner);
            //TEXT ERRORE ENTRATA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 11;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggTronco.add(lLarghezzaError, constraintsInnner);
            //BUTTON CONFERMA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 12;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            constraintsInnner.insets = new Insets(25, 0, 0, 10);
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(tX.getText().equals("")) {
                        lXError.setVisible(true);
                    }
                    else if(tY.getText().equals("")) {
                        lYError.setVisible(true);
                    }
                    else if(tY.getText().equals("")) {
                        lYError.setVisible(true);
                    }
                    else if(tXF.getText().equals("")) {
                        lXFError.setVisible(true);
                    }
                    else if(tYF.getText().equals("")) {
                        lYFError.setVisible(true);
                    }
                    else if(tLunghezza.getText().equals("")) {
                        lLunghezzaError.setVisible(true);
                    }
                    else if(tLarghezza.getText().equals("")) {
                        lLarghezzaError.setVisible(true);
                    }
                    else {
                        lXError.setVisible(false);
                        lYError.setVisible(false);
                        lXFError.setVisible(false);
                        lYFError.setVisible(false);
                        lLarghezzaError.setVisible(false);
                        lLunghezzaError.setVisible(false);
                        int id = 0;
                        try {
                            id = DAOTronco.insertTronco(tX.getText(),tY.getText(),tXF.getText(),tYF.getText(),tLunghezza.getText(),tLarghezza.getText(),(String)comboPiani.getSelectedItem());
                        } catch (SQLException e1) {
                        }
                        finally {
                            comboTronchi.addItem(id);
                            aggTronco.setVisible(false);
                            tX.setText("");
                            tY.setText("");
                            tXF.setText("");
                            tYF.setText("");
                            tLarghezza.setText("");
                            tLunghezza.setText("");
                            aggiornaUI();
                        }
                    }
                }
            });
            aggTronco.add(confirm, constraintsInnner);


            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.ipady = 40;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridheight = 4;
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new Insets(10 * uiScaling, 17 * uiScaling, 0, 17 * uiScaling);
            mainPanel.add(aggTronco,gridBagConstraints);

            aggTronco.setVisible(true);
        }
        else if(aggTronco.isVisible()) {
            aggTronco.setVisible(false);
        }
        else {
            aggTronco.setVisible(true);
        }
        aggiornaUI();
    }

    private void aggiungiBeacon() {
        if(aggBeacon==null) {
            aggBeacon = new JPanel(new GridBagLayout());

            //Definisco gli elementi che fanno parte della form
            GridBagConstraints constraintsInnner = new GridBagConstraints();
            JLabel lID = new JLabel("ID:");
            lID.setFont(defaultFont);
            JTextField tID = new JTextField(20);
            tID.setFont(defaultFont);
            JLabel lIDError = new JLabel("Per favore inserisci l'ID!");
            lIDError.setFont(defaultFont);
            lIDError.setForeground(Color.RED);
            lIDError.setVisible(false);

            JLabel lX = new JLabel("X:");
            lX.setFont(defaultFont);
            JTextField tX = new JTextField(6);
            tX.setFont(defaultFont);
            JLabel lXError = new JLabel("Per favore inserisci la coordinata X!");
            lXError.setFont(defaultFont);
            lXError.setForeground(Color.RED);
            lXError.setVisible(false);

            JLabel lY = new JLabel("Y:");
            lY.setFont(defaultFont);
            JTextField tY = new JTextField(6);
            tY.setFont(defaultFont);
            JLabel lYError = new JLabel("Per favore inserisci la coordinata Y!");
            lYError.setFont(defaultFont);
            lYError.setForeground(Color.RED);
            lYError.setVisible(false);

            JCheckBox cUscita = new JCheckBox("Uscita");
            cUscita.setFont(defaultFont);


            JButton confirm = new JButton("Conferma");
            confirm.setFont(defaultFont);
            confirm.setSize(componentDimension);

            //Aggiungo gli elementi al panel
            //LABEL ID
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(lID, constraintsInnner);
            //TEXT NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 0;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(tID, constraintsInnner);
            //TEXT ERRORE NOME
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 1;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(lIDError, constraintsInnner);
            //LABEL X
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(lX, constraintsInnner);
            //TEXT X
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 2;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(tX, constraintsInnner);
            //TEXT ERRORE X
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 3;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(lXError, constraintsInnner);
            //LABEL Y
            constraintsInnner.gridx = 0;
            constraintsInnner.gridy = 4;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(lY, constraintsInnner);
            //TEXT Y
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 4;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(tY, constraintsInnner);
            //TEXT ERROR Y
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 5;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(lYError, constraintsInnner);
            //CHECKUSCITA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 6;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            aggBeacon.add(cUscita, constraintsInnner);

            //BUTTON CONFERMA
            constraintsInnner.gridx = 1;
            constraintsInnner.gridy = 7;
            constraintsInnner.anchor = GridBagConstraints.LINE_START;
            constraintsInnner.insets = new Insets(25, 0, 0, 10);
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(tID.getText().equals("")) {
                        lIDError.setVisible(true);
                    }
                    else if(tX.getText().equals("")) {
                        lXError.setVisible(true);
                    }
                    else if(tY.getText().equals("")) {
                        lYError.setVisible(true);
                    }
                    else {
                        lIDError.setVisible(false);
                        lXError.setVisible(false);
                        lYError.setVisible(false);
                        try {
                            DAOBeacon.insertBeacon(tID.getText(),((Integer)comboTronchi.getSelectedItem()).toString(),tX.getText(),tY.getText(),cUscita.isSelected() ? "1" : "0");
                        } catch (SQLException e1) {
                        }
                        finally {
                            comboBeacons.addItem(tID.getText());
                            aggBeacon.setVisible(false);
                            tID.setText("");
                            tX.setText("");
                            tY.setText("");
                            cUscita.setSelected(false);
                            aggiornaUI();
                        }
                    }
                }
            });
            aggBeacon.add(confirm, constraintsInnner);


            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.ipady = 40;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.gridheight = 4;
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new Insets(10 * uiScaling, 17 * uiScaling, 0, 17 * uiScaling);
            mainPanel.add(aggBeacon,gridBagConstraints);

            aggBeacon.setVisible(true);
        }
        else if(aggBeacon.isVisible()) {
            aggBeacon.setVisible(false);
        }
        else {
            aggBeacon.setVisible(true);
        }
        aggiornaUI();
    }

    private void aggiornaUI() {
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public class ImagePanel extends JPanel {

        private BufferedImage img;

        ImagePanel(BufferedImage img) {
            setPreferredSize(new Dimension(MIN_MAP_WIDTH*uiScaling,MIN_MAP_HEIGHT*uiScaling));
            this.img = img;
        }

        @Override
        public Dimension getPreferredSize() {
            return img == null ? super.getPreferredSize() : new Dimension(img.getWidth(), img.getHeight());
        }

        Point getImageLocation() {

            Point p = null;
            if (img != null) {
                int x = (getWidth() - img.getWidth()) / 2;
                int y = (getHeight() - img.getHeight()) / 2;
                p = new Point(x, y);
            }
            return p;

        }

        void paintTronco(int x, int y, int xf, int yf) {
            getGraphics().drawLine(x,y,xf,yf);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                Point p = getImageLocation();
                g.drawImage(img, p.x, p.y, this);
            }
        }

    }
}