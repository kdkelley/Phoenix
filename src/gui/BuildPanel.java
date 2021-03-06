/*
 * Copyright (C) 2015 joulupunikki joulupunikki@gmail.communist.invalid.
 *
 *  Disclaimer of Warranties and Limitation of Liability.
 *
 *     The creators and distributors offer this software as-is and
 *     as-available, and make no representations or warranties of any
 *     kind concerning this software, whether express, implied, statutory,
 *     or other. This includes, without limitation, warranties of title,
 *     merchantability, fitness for a particular purpose, non-infringement,
 *     absence of latent or other defects, accuracy, or the presence or
 *     absence of errors, whether or not known or discoverable.
 *
 *     To the extent possible, in no event will the creators or distributors
 *     be liable on any legal theory (including, without limitation,
 *     negligence) or otherwise for any direct, special, indirect,
 *     incidental, consequential, punitive, exemplary, or other losses,
 *     costs, expenses, or damages arising out of the use of this software,
 *     even if the creators or distributors have been advised of the
 *     possibility of such losses, costs, expenses, or damages.
 *
 *     The disclaimer of warranties and limitation of liability provided
 *     above shall be interpreted in a manner that, to the extent possible,
 *     most closely approximates an absolute disclaimer and waiver of
 *     all liability.
 *
 */
package gui;

import dat.UnitType;
import galaxyreader.Structure;
import galaxyreader.Unit;
import game.Game;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.apache.commons.math3.util.FastMath;
import state.PW1;
import util.C;
import util.Comp;
import util.FN;
import util.Util;
import util.UtilG;
import util.WindowSize;

/**
 * Handles unit building orders.
 *
 * @author joulupunikki <joulupunikki@gmail.communist.invalid>
 */
public class BuildPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Gui gui;
    private Game game;
    private WindowSize ws;
    private JList planet_list;
    private JList city_list;
    private JTable city_table;
    private JTable build_table;
    private JTable queue_table;
    private JButton exit;
    private JTextField[] res_display;
    private static Object[] build_table_header = {"Units Available", "Time"};
    private static Object[] queue_table_header = {"Build Queue", "Time"};
    private static Object[] city_table_header = {"City", "Building"};
//    private Object[][] city_table_data;
//    private Object[][] build_table_data;
    private int input_unit_nr = -1;
    private UnitStats.Left left_stats;
    private UnitStats.Right right_stats;
    private UnitStats.Attack attack_stats;
    BufferedImage bi;
    public BuildPanel(Gui gui) {
        this.gui = gui;
        ws = Gui.getWindowSize();
        game = gui.getGame();
        this.bi = Util.loadImage(FN.S_UNITBG2_PCX, ws.is_double, gui.getPallette(), 504, 209);
        addLists();
        setUpButtons();
        setUpResDisplay();
        setUpStatsDisplay(gui);
//        List<Planet> planets = game.getPlanets();
        setUpCoordinateListener(); // for testing positions on panel
    }

    // for testing positions on panel
    public void setUpCoordinateListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
//                clickOnPlanetMap(e);
                Point p = e.getPoint();
                System.out.println("build panel (x,y): " + p.x + ", " + p.y);
            }
        });
    }

    public void setUpStatsDisplay(Gui gui) {
        left_stats = new UnitStats.Left(gui);
        left_stats.setBounds(ws.bp_lsp_x, ws.sw_lsp_y, ws.sw_lsp_w, ws.sw_lsp_h);
        this.add(left_stats);

        right_stats = new UnitStats.Right(gui);
        right_stats.setBounds(ws.bp_rsp_x, ws.sw_lsp_y, ws.sw_lsp_w, ws.sw_lsp_h);
        this.add(right_stats);

        attack_stats = new UnitStats.Attack(gui);
        attack_stats.setBounds(ws.bp_asp_x, ws.sw_lsp_y, ws.sw_ap_w, ws.sw_ap_h);
        this.add(attack_stats);
    }

    public void setUpResDisplay() {
        res_display = new JTextField[C.REQUIRED_RESOURCES.length];
        for (int i = 0; i < res_display.length; i++) {
            res_display[i] = new JTextField();
            this.add(res_display[i]);
            res_display[i].setBounds(ws.bp_res_display_x_offset + i * ws.pw_res_display_x_gap, ws.bp_res_display_y_offset, ws.bp_res_display_w, ws.bp_res_display_h);
//            res_display[i].setBackground(Color.WHITE);
            res_display[i].setOpaque(false);
            res_display[i].setForeground(C.COLOR_RES_DISP_GREEN);
            res_display[i].setEditable(false);
            res_display[i].setHorizontalAlignment(JTextField.CENTER);
            res_display[i].setBorder(null);
            res_display[i].setFont(ws.font_default);
//            res_display[i].setText("123");
        }

    }


    /**
     * Show res amounts for planet
     */
    public void drawResAmounts() {
        Integer val = (Integer) planet_list.getSelectedValue();
        if (val == null) {
            return;
        }
        int planet = val;
        //System.out.println("Planet name = " + game.getPlanet(planet).name);
        int[] res_avail = game.getResources().getResourcesAvailable(planet, game.getTurn());
        for (int i = 0; i < res_display.length; i++) {
            res_display[i].setForeground(C.COLOR_RES_DISP_GREEN);
            res_display[i].setText(Util.c4Display(res_avail[C.REQUIRED_RESOURCES[i]]));
        }
    }

    public void setUpButtons() {
        exit = new JButton("Exit");
        exit.setFont(ws.font_default);
        exit.setBorder(BorderFactory.createLineBorder(C.COLOR_GOLD));
        this.add(exit);
        exit.setBounds(ws.build_exit_button_x_offset, ws.build_exit_button_y_offset,
                ws.build_exit_button_w, ws.build_exit_button_h);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSelection();
                zeroLists();
                gui.getBuildWindow().setVisible(false);
            }
        });
    }

    public JList getPlanetList() {
        return planet_list;
    }

    public void zeroResources() {
        left_stats.setValues(null);
        right_stats.setValues(null);
        attack_stats.setValues(null);
        for (JTextField tf : res_display) {
            tf.setText("");
        }
        input_unit_nr = -1;
    }

    public void zeroBuild() {
        if (build_table.getRowCount() != 0) {
            ((BuildTableModel) build_table.getModel()).setRowCount(0);
        }
    }

    public void zeroQueue() {
        if (queue_table.getRowCount() != 0) {
            ((BuildTableModel) queue_table.getModel()).setRowCount(0);
        }
    }

    public void zeroCities() {
        if (city_table.getRowCount() != 0) {
            ((CityTableModel) city_table.getModel()).setRowCount(0);
        }
    }

    public void zeroLists() {
        zeroCities();
        zeroBuild();
        zeroQueue();
        zeroResources();
    }

    public void addLists() {

        addPlanetList();
        addCityTable();
        addBuildTable();
        addQueueTable();
    }

    public void addQueueTable() {
        queue_table = new JTable();
        queue_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane queue_table_view = new JScrollPane(queue_table);
        queue_table.setFillsViewportHeight(true);
        queue_table.setBackground(Color.BLACK);
        queue_table_view.setPreferredSize(new Dimension(250, 80));
        this.add(queue_table_view);
        queue_table_view.setBounds(ws.queue_table_x_offset, ws.queue_table_y_offset,
                ws.queue_table_width, ws.queue_table_height);
        JTableHeader header = queue_table.getTableHeader();
        header.setFont(ws.font_default);
        header.setBackground(Color.black);
        header.setForeground(C.COLOR_GOLD);
        queue_table.setRowHeight(ws.city_table_row_height);
        queue_table.setDefaultRenderer(Object.class, new QueueTableRenderer());
        queue_table.setDefaultRenderer(Integer.class, new QueueTableRenderer());
        queue_table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                if (row == -1) {
                    return;
                }
                if (e.getClickCount() == 1) {
                    //System.out.println("Single clicked row " + row);
                }
                if (e.getClickCount() == 2) {
                    //System.out.println("Double clicked row " + row);
                    int selected_city = city_table.getSelectedRow();
                    Structure city = (Structure) city_table.getValueAt(selected_city, 0);
                    city.removeFromQueue(row, game.getUnitTypes(), game);
                    setQueueData(null, city);
                    planetSelected(null, -1);
                    city_table.setRowSelectionInterval(selected_city, selected_city);
                }
            }
        });
    }

    public void addBuildTable() {
        build_table = new JTable();
        build_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane build_table_view = new JScrollPane(build_table);
        build_table.setFillsViewportHeight(true);
        build_table.setBackground(Color.BLACK);
        build_table_view.setPreferredSize(new Dimension(250, 80));
        this.add(build_table_view);
        build_table_view.setBounds(ws.build_table_x_offset, ws.build_table_y_offset,
                ws.build_table_width, ws.build_table_height);
//        ListSelectionModel list_selection_model;
//        list_selection_model = build_table.getSelectionModel();
//        list_selection_model.addListSelectionListener(
//                new ListSelectionListener() {
//                    public void valueChanged(ListSelectionEvent e) {
//                        buildSelected(e);
//                    }
//                });
        JTableHeader header = build_table.getTableHeader();
        header.setFont(ws.font_default);
        header.setBackground(Color.black);
        header.setForeground(C.COLOR_GOLD);
        build_table.setRowHeight(ws.city_table_row_height);
        build_table.setDefaultRenderer(Object.class, new BuildTableRenderer());
        build_table.setDefaultRenderer(Integer.class, new BuildTableRenderer());

        build_table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                if (row == -1) {
                    return;
                }
                if (e.getClickCount() == 1) {
                    //System.out.println("Single clicked row " + row);
                    int[] unit = (int[]) build_table.getValueAt(row, 0);
                    int input = game.getUnitTypes()[unit[0]][unit[1]].unit;
                    if (input > -1) {
                        input_unit_nr = input;
                    } else {
                        input_unit_nr = -1;
                    }
                    UtilG.drawResAmounts(unit, (Integer) planet_list.getSelectedValue(),
                            game, res_display);
                    UnitType ut = game.getUnitTypes()[unit[0]][unit[1]];
                    left_stats.setValues(ut);
                    right_stats.setValues(ut);
                    attack_stats.setValues(ut);
                    repaint();
                }
                if (e.getClickCount() == 2) {
                    //System.out.println("Double clicked row " + row);
                    int selected_city = city_table.getSelectedRow();
                    //System.out.println("selected_city = " + selected_city);
                    Structure city = (Structure) city_table.getValueAt(selected_city, 0);
                    int[] tmp = (int[]) build_table.getValueAt(row, 0);
                    int[] unit = {tmp[0], tmp[1]};
                    // autobuy from agora here, but only if this is the first unit in queue
                    if (city.build_queue.isEmpty()) {
                        int[] res_needed = game.getUnitTypes()[tmp[0]][tmp[1]].reqd_res;
                        int planet = (Integer) planet_list.getSelectedValue();
                        //System.out.println("Planet name = " + game.getPlanet(planet).name);
                        int[] res_avail = game.getResources().getResourcesAvailable(planet, game.getTurn());
                        int[] res_missing = new int[C.NR_RESOURCES];
                        boolean need_more_res = false;
                        for (int i = 0; i < res_display.length; i++) {
                            res_missing[C.REQUIRED_RESOURCES[i]] = FastMath.max(0, res_needed[C.REQUIRED_RESOURCES[i]] - res_avail[C.REQUIRED_RESOURCES[i]]);
                            if (res_missing[C.REQUIRED_RESOURCES[i]] > 0) {
                                need_more_res = true;
                            }
                        }
                        boolean[] response = {true};
                        if (need_more_res) {
                            gui.showAgoraAutobuyDialog(res_missing, planet, tmp[0], tmp[1], response);
                        }
                        if (!response[0]) {
                            return;
                        }
                    }
                    city.addToQueue(unit, game.getUnitTypes(), game);
                    // if input unit was alone in selected stack
                    Point q = game.getSelectedPoint();
                    if (q != null) {
                        List<Unit> stack = game.getSelectedStack();
                        if (stack.isEmpty()) {
                            game.setSelectedPoint(null, -1);
                            game.setSelectedFaction(-1);
                            gui.setCurrentState(PW1.get());
                        }
                    }
                    setQueueData(null, city);
                    planetSelected(null, -1);
                    city_table.setRowSelectionInterval(selected_city, selected_city);
                }
            }
        });
    }

    public void addCityTable() {
        city_table = new JTable();
//        city_table.setAutoCreateRowSorter(true);
        city_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane city_table_view = new JScrollPane(city_table);
        city_table.setFillsViewportHeight(true);
        city_table_view.setPreferredSize(new Dimension(250, 80));
//        city_table_view.setBackground(Color.BLACK);
        city_table.setBackground(Color.BLACK);
        this.add(city_table_view);
        city_table_view.setBounds(ws.city_table_x_offset, ws.planet_list_y_offset,
                ws.planet_list_width, ws.planet_list_height);
        JTableHeader header = city_table.getTableHeader();
        header.setFont(ws.font_default);
        header.setBackground(Color.BLACK);
        header.setForeground(C.COLOR_GOLD);
        city_table.setGridColor(Color.BLACK);
        city_table.setRowHeight(ws.city_table_row_height);
        city_table.setDefaultRenderer(Object.class, new CityTableRenderer());
        DefaultListSelectionModel list_selection_model;
        list_selection_model = (DefaultListSelectionModel) city_table.getSelectionModel();
        list_selection_model.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            citySelected(e);
                            zeroResources();
                            drawResAmounts();
                        }
                    }
                });
    }

    public void addPlanetList() {
        planet_list = new JList();
        planet_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        planet_list.setLayoutOrientation(JList.VERTICAL);
        CustomRendererInt renderer = new CustomRendererInt();
//        renderer.setPreferredSize(new Dimension(ws.planet_list_cell_w, ws.planet_list_cell_h));
        planet_list.setCellRenderer(renderer);
        JScrollPane planet_view = new JScrollPane(planet_list);
        planet_view.setPreferredSize(new Dimension(250, 80));
        planet_view.setBackground(Color.BLACK);
        planet_list.setBackground(Color.BLACK);

        this.add(planet_view);
        planet_view.setBounds(ws.planet_list_x_offset, ws.planet_list_y_offset,
                ws.planet_list_width, ws.planet_list_height);
        DefaultListSelectionModel list_selection_model;
        list_selection_model = (DefaultListSelectionModel) planet_list.getSelectionModel();
        ListSelectionListener[] lsl_list = list_selection_model.getListSelectionListeners();
//        for (ListSelectionListener lsl : lsl_list) {
//            System.out.println("lsl = " + lsl);
//        }
        list_selection_model.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            planetSelected(e, -1);
                            zeroResources();
                            drawResAmounts();
                        }
                    }
                });
    }

    public void clearSelection() {
        planet_list.clearSelection();
    }

    public void setSelectedPlanet(int planet) {
        ListModel lm = planet_list.getModel();
        int len = lm.getSize();
        int selected = -1;
        for (int i = 0; i < len; i++) {
            if (((Integer) lm.getElementAt(i)).intValue() == planet) {
                selected = i;
                break;
            }

        }
        planet_list.setSelectedIndex(selected);
    }

    public void setSelectedCity(Structure city) {
        TableModel tm = city_table.getModel();
        int len = tm.getRowCount();
        int selected = -1;
        for (int i = 0; i < len; i++) {
            if (((Structure) tm.getValueAt(i, 0)) == city) {
                selected = i;
            }

        }

        city_table.addRowSelectionInterval(selected, selected);

    }

    /**
     * If nr != -1 use nr as value of selected planet else read value from list.
     *
     * @param e the value of e
     * @param nr the value of nr
     */
    public void planetSelected(ListSelectionEvent e, int nr) {
        //System.out.println("planet_list selected value = " + planet_list.getSelectedValue());

        int planet = -1;
        if (nr == -1) {
            Object tmp = planet_list.getSelectedValue();

            if (tmp == null) {
                return;
            }
            planet = ((Integer) tmp).intValue();
            if (planet == -1) {
                return;
            }
        } else {
            planet = nr;
        }
        List<Structure> cl = game.getStructures();
        List<Structure> cl2 = new LinkedList<>();
        for (Structure structure : cl) {
            if (structure.owner == game.getTurn() && structure.p_idx == planet) {
                if (!game.getFaction(game.getTurn()).getResearch().can_build.get(structure.type).isEmpty()) {
                    cl2.add(structure);
                }
            }
        }
        Collections.sort(cl2, Comp.city_name);

        Structure[] cities = new Structure[cl2.size()];
        int idx = 0;
        for (Structure structure : cl2) {
            cities[idx++] = structure;
        }
//        city_list.setListData(cities);
        int data_len = cities.length;
//        int padded_len = 18;
//        data_len = data_len < padded_len ? padded_len : data_len;
        UnitType[][] unit_types = game.getUnitTypes();
        Object[][] city_table_data = new Object[data_len][];
        for (int i = 0; i < data_len; i++) {

            city_table_data[i] = new Object[2];
            if (i < cities.length) {
                city_table_data[i][0] = cities[i];
                if (cities[i].build_queue.isEmpty()) {
                    city_table_data[i][1] = "";
                } else {
                    int[] t = cities[i].build_queue.getFirst();
                    city_table_data[i][1] = unit_types[t[0]][t[1]].abbrev;
                }
            } else {
                city_table_data[i][0] = null;
                city_table_data[i][1] = null;
            }
        }

        CityTableModel city_model = new CityTableModel(city_table_data, city_table_header);
        city_table.setModel(city_model);

        zeroBuild();
        zeroQueue();
        //System.out.println("row height" + city_table.getRowHeight());
    }

    public void setBuildData(ListSelectionEvent e, Structure city) {

        ArrayList<int[]> al = game.getFaction(game.getTurn()).getResearch().can_build.get(city.type);
        int nr_units = al.size();
        UnitType[][] unit_types = game.getUnitTypes();
        int data_len = nr_units;
//        int padded_len = 9;
//        data_len = data_len < padded_len ? padded_len : data_len;
        Object[][] build_table_data = new Object[data_len][];
        for (int i = 0; i < data_len; i++) {
            build_table_data[i] = new Object[2];
            if (i < nr_units) {
                int[] unit_type = al.get(i);
                build_table_data[i][0] = unit_type;
                build_table_data[i][1] = new Integer(unit_types[unit_type[0]][unit_type[1]].turns_2_bld);
            } else {
                build_table_data[i][0] = null;
                build_table_data[i][1] = null;
            }
        }

        BuildTableModel build_model = new BuildTableModel(build_table_data,
                build_table_header);
        build_table.setModel(build_model);
//        System.out.println("CellRend 0 " + build_table.getCellRenderer(0, 0));
//        System.out.println("CellRend 1 " + build_table.getCellRenderer(0, 1));
        TableColumn column = build_table.getColumnModel().getColumn(0);
        column.setPreferredWidth(ws.build_table_cell_0_width);
        column = build_table.getColumnModel().getColumn(1);
        column.setPreferredWidth(ws.build_table_cell_1_width);
    }

    public void setQueueData(ListSelectionEvent e, Structure city) {
        List<int[]> queue = city.build_queue;
        int nr_units = queue.size();
        if (nr_units == 0) {
            if (queue_table.getRowCount() != 0) {
                ((BuildTableModel) queue_table.getModel()).setRowCount(nr_units);
            }
            return;
        }
        UnitType[][] unit_types = game.getUnitTypes();
        int data_len = nr_units;
//        int padded_len = 9;
//        data_len = data_len < padded_len ? padded_len : data_len;
        Object[][] queue_table_data = new Object[data_len][];
        ListIterator<int[]> iter = queue.listIterator();
        int[] unit_type = null;
        if (iter.hasNext()) {
            unit_type = iter.next();
        }
        for (int i = 0; i < data_len; i++) {
            queue_table_data[i] = new Object[2];
//            if (i == 0 && nr_units == 0) {
//                int[] dummy = {-1, -1};
//                queue_table_data[i][0] = dummy;
//                queue_table_data[i][1] = new Integer(-1);
//            } else 
            if (i < nr_units) {

                queue_table_data[i][0] = unit_type;
                if (i != 0) {
                    queue_table_data[i][1] = new Integer(unit_types[unit_type[0]][unit_type[1]].turns_2_bld);
                } else {
                    queue_table_data[i][1] = city.turns_left;
                }

                if (iter.hasNext()) {
                    unit_type = iter.next();
                }
            } else {
                queue_table_data[i][0] = null;
                queue_table_data[i][1] = null;
            }
        }

        BuildTableModel queue_model = new BuildTableModel(queue_table_data,
                queue_table_header);
        queue_table.setModel(queue_model);

        //System.out.println("CellRend 0 " + queue_table.getCellRenderer(0, 0));
        //System.out.println("CellRend 1 " + queue_table.getCellRenderer(0, 1));
        TableColumn column = queue_table.getColumnModel().getColumn(0);
        column.setPreferredWidth(ws.queue_table_cell_0_width);
        column = queue_table.getColumnModel().getColumn(1);
        column.setPreferredWidth(ws.queue_table_cell_1_width);
    }

    public void citySelected(ListSelectionEvent e) {

        int row = city_table.getSelectedRow();
        if (row == -1) {
            return;
        }
        Object tmp = city_table.getValueAt(row, 0);
        if (tmp == null) {
            return;
        }

        Structure city = (Structure) tmp;

        setBuildData(e, city);
        setQueueData(e, city);
    }

//    public void buildSelected(ListSelectionEvent e) {
//        int row = build_table.getSelectedRow();
//        if (row == -1) {
//            return;
//        }
//        Object tmp = build_table.getValueAt(row, 0);
//    }
    public void setPlanets() {
        boolean[] planets = new boolean[game.getPlanets().size()];
        for (int i = 0; i < planets.length; i++) {
            planets[i] = false;
        }
        List<Structure> cities = game.getStructures();
        for (Structure structure : cities) {
            if (structure.owner == game.getTurn()) {
                planets[structure.p_idx] = true;
            }
        }
        ArrayList<Integer> planet_indexes = new ArrayList();
        for (int i = 0; i < planets.length; i++) {
            if (planets[i]) {
                planet_indexes.add(new Integer(i));
            }
        }
//        int padding = 16 - planet_indexes.size();
//        for (int i = 0; i < padding; i++) {
//            planet_indexes.add(new Integer(-1));
//
//        }

        planet_list.setListData(planet_indexes.toArray());

    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(33, 33, 33));
        g.fillRect(0, 0, ws.planet_map_width, ws.planet_map_height);
        UtilG.drawResourceIcons(bi.getRaster(), input_unit_nr, gui, ws, 11, 166);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bi, null, 0, 0);
    }

    class CustomRendererInt extends JLabel
            implements ListCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public CustomRendererInt() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

//            int selectedIndex = ((Integer) value).intValue();
            Color c_f = C.COLOR_GOLD;
            Color c_b = Color.BLACK;
            int val = ((Integer) value).intValue();
            String s_val;
            if (val > -1) {
                s_val = game.getPlanet(val).name;
            } else {
                s_val = " ";
            }
            if (isSelected) {
                setBackground(c_f);
                setForeground(c_b);
            } else {
                setBackground(c_b);
                setForeground(c_f);
            }

//            String planet = planets_strings[selectedIndex];
            setText(s_val);
            setFont(ws.font_default);

            return this;
        }

    }

    class CustomRendererStruct extends JLabel
            implements ListCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public CustomRendererStruct() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

//            int selectedIndex = ((Integer) value).intValue();
            Color c_f = C.COLOR_GOLD;
            Color c_b = Color.BLACK;
            Structure str = (Structure) value;
//            String val = Structure.getName(str.type);
            String val = game.getStrBuild(str.type).name;
            if (isSelected) {
                setBackground(c_f);
                setForeground(c_b);
            } else {
                setBackground(c_b);
                setForeground(c_f);
            }

//            String planet = planets_strings[selectedIndex];
            setText(val);
            setFont(list.getFont());

            return this;
        }

    }

    public class CityTableRenderer extends JLabel
            implements TableCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public CityTableRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            Color c_b = Color.BLACK;
            Color c_f = C.COLOR_GOLD;
            if (isSelected) {
                setBackground(c_f);
                setForeground(c_b);
            } else {
                setBackground(c_b);
                setForeground(c_f);
            }
            String val = null;
            if (value == null) {
                val = " ";
            } else if (value instanceof Structure) {
                Structure str = (Structure) value;
//            String val = Structure.getName(str.type);
                val = game.getStrBuild(str.type).name;
            } else {
                val = (String) value;
            }
            setFont(ws.font_default);
            setText(val);
            return this;
        }
    }

    public class BuildTableRenderer extends JLabel
            implements TableCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public BuildTableRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            Color c_b = Color.BLACK;
            Color c_f = C.COLOR_GOLD;

            String val = null;
            if (value == null) {
                val = " ";
            } else if (value instanceof int[]) {
                int[] tmp = (int[]) value;
//            String val = Structure.getName(str.type);
                if (tmp[0] == -1) {
                    val = " ";
                }
                UnitType[][] unit_types = game.getUnitTypes();
                val = unit_types[tmp[0]][tmp[1]].name;
            } else {
                val = "" + ((Integer) value).intValue();
                setHorizontalTextPosition(RIGHT);
            }
            // is input unit needed and available
            int[] tmp = (int[]) table.getValueAt(row, 0);
            UnitType[][] unit_types = game.getUnitTypes();
            if (unit_types[tmp[0]][tmp[1]].unit > -1) {
                Structure city = (Structure) city_table.getValueAt(city_table.getSelectedRow(), 0);

                Unit unit = Structure.findInputUnit(tmp, unit_types, game, city);
                if (unit == null) {
                    c_f = Color.WHITE;
                }
            }
            // are resources available
            int[] res_needed = game.getUnitTypes()[tmp[0]][tmp[1]].reqd_res;
            int planet = (Integer) planet_list.getSelectedValue();
            //System.out.println("Planet name = " + game.getPlanet(planet).name);
            int[] res_avail = game.getResources().getResourcesAvailable(planet, game.getTurn());
            for (int i = 0; i < res_display.length; i++) {
                if (res_avail[C.REQUIRED_RESOURCES[i]] - res_needed[C.REQUIRED_RESOURCES[i]] < 0) {
                    c_f = Color.RED;
                    break;
                }
            }
            if (isSelected) {
                setBackground(c_f);
                setForeground(c_b);
            } else {
                setBackground(c_b);
                setForeground(c_f);
            }
            setFont(ws.font_default);
            setText(val);

            return this;
        }
    }

    public class QueueTableRenderer extends JLabel
            implements TableCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public QueueTableRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            Color c_b = Color.BLACK;
            Color c_f = C.COLOR_GOLD;

            String val = null;
            if (value == null) {
                val = " ";
            } else if (value instanceof int[]) {
                int[] tmp = (int[]) value;
//            String val = Structure.getName(str.type);
                if (tmp[0] == -1) {
                    val = " ";
                }
                UnitType[][] unit_types = game.getUnitTypes();
                val = unit_types[tmp[0]][tmp[1]].name;
            } else {
                val = "" + ((Integer) value).intValue();
                setHorizontalTextPosition(RIGHT);
            }

            Structure city = (Structure) city_table.getValueAt(city_table.getSelectedRow(), 0);
            if (city.on_hold_no_res && row == 0) {
                c_f = Color.LIGHT_GRAY;
            }
            if (isSelected) {
                setBackground(c_f);
                setForeground(c_b);
            } else {
                setBackground(c_b);
                setForeground(c_f);
            }
            setFont(ws.font_default);
            setText(val);

            return this;
        }
    }

//    class CityTableModel extends AbstractTableModel {
//
//        private String[] columnNames = {"City", "Building"};
//        private Object[][] data;
//
//        public CityTableModel(Object[][] data) {
//            this.data = data;
//        }
//
//        public int getColumnCount() {
//            return columnNames.length;
//        }
//
//        public int getRowCount() {
//            return data.length;
//        }
//
//        public String getColumnName(int col) {
//            return columnNames[col];
//        }
//
//        public Object getValueAt(int row, int col) {
//            return data[row][col];
//        }
//
//        public Class getColumnClass(int c) {
//            return getValueAt(0, c).getClass();
//        }
//
//        /*
//         * Don't need to implement this method unless your table's
//         * editable.
//         */
//        public boolean isCellEditable(int row, int col) {
//
//            return false;
//
//        }
//
//    }
    class QueueTableModel extends AbstractTableModel {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private String[] columnNames = {"Unit", "Turns"};
        private Object[][] data;

        public QueueTableModel(Object[][] data) {
            this.data = data;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {

            return false;

        }
    }

    class CityTableModel extends DefaultTableModel {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        //        public BuildTableModel(Object[][] data) {
//            Object[] column_names = {"Unit", "Turns Left"};
//            BuildTableModel(data, column_names);
//        }
        public CityTableModel(Object[][] data, Object[] column_names) {
            super(data, column_names);
        }

        public boolean isCellEditable(int row, int col) {

            return false;

        }
    }

    class BuildTableModel extends DefaultTableModel {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        //        public BuildTableModel(Object[][] data) {
//            Object[] column_names = {"Unit", "Turns Left"};
//            BuildTableModel(data, column_names);
//        }
        public BuildTableModel(Object[][] data, Object[] column_names) {
            super(data, column_names);
        }

        public boolean isCellEditable(int row, int col) {

            return false;

        }
    }

//    class BuildTableModel extends AbstractTableModel {
//
//        private String[] columnNames = {"Unit", "Turns Left"};
//        private Object[][] data;
//
//        public BuildTableModel(Object[][] data) {
//            this.data = data;
//        }
//
//        public int getColumnCount() {
//            return columnNames.length;
//        }
//
//        public int getRowCount() {
//            return data.length;
//        }
//
//        public String getColumnName(int col) {
//            return columnNames[col];
//        }
//
//        public Object getValueAt(int row, int col) {
//            return data[row][col];
//        }
//
//        public Class getColumnClass(int c) {
//            return getValueAt(0, c).getClass();
//        }
//
//        /*
//         * Don't need to implement this method unless your table's
//         * editable.
//         */
//        public boolean isCellEditable(int row, int col) {
//
//            return false;
//
//        }
//
//    }
}
