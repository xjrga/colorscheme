/*
 * Copyright (C) 2021 Jorge R Garcia de Alba &lt;jorge.r.garciadealba@gmail.com&gt;
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.github.xjrga.colorscheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.InputStream;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * This class provides a custom palette chooser panel
 *
 * @author Jorge R Garcia de Alba &lt;jorge.r.garciadealba@gmail.com&gt;
 */
public class Palette_chooser_panel extends AbstractColorChooserPanel {

    private final JPanel panelResult;
    private JTable table;
    private final DefaultTableModel model;
    private final Data_transfer transfer;
    private final JTextField textFieldPaletteName;
    private final Xml_to_html xmlToHtml;
    private Color selectedColor;
    private ColorSelectionModel colorSelectionModel;

    /**
     * Constructs PaletteChooserPanel class
     */
    public Palette_chooser_panel() {
        xmlToHtml = new Xml_to_html();
        transfer = new Data_transfer();
        setLayout(new BorderLayout(10, 10));
        textFieldPaletteName = new JTextField();
        panelResult = new JPanel();
        textFieldPaletteName.setPreferredSize(new Dimension(375, 28));
        add(panelResult, BorderLayout.CENTER);
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.add(new JLabel("Palette: "));
        textFieldPanel.add(textFieldPaletteName);
        add(textFieldPanel, BorderLayout.SOUTH);
        table = new JTable();
        model = new Table_model_color();
        table.setModel(model);
        table.setDefaultRenderer(Color.class, new Color_renderer());
        table.getTableHeader().setToolTipText("Sort by hue");
        TableRowSorter tableSorter = new TableRowSorter<>(model);
        table.setRowSorter(tableSorter);
        Color_comparator colorComparator = new Color_comparator();
        tableSorter.setComparator(0, colorComparator);
        panelResult.add(new JScrollPane(table));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    selectedColor = (Color) table.getValueAt(row, 0);
                    colorSelectionModel = getColorSelectionModel();
                    if (selectedColor.equals(colorSelectionModel.getSelectedColor())) {
                        colorSelectionModel.setSelectedColor(selectedColor.brighter());
                        colorSelectionModel.setSelectedColor(selectedColor);
                    } else {
                        colorSelectionModel.setSelectedColor(selectedColor);
                    }
                }
            }
        });
    }

    @Override
    public void buildChooser() {

    }

    @Override
    public void updateChooser() {
    }

    @Override
    public String getDisplayName() {
        return "Palette";
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return null;
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return null;
    }

    /**
     *
     * @param selectedColor
     */
    public void addColorItem(Color selectedColor) {
        Vector row = new Vector();
        row.add(selectedColor);
        model.addRow(row);
        panelResult.revalidate();
        panelResult.repaint();
    }

    /**
     * Clears table model
     */
    public void clearAllColorItems() {
        model.setRowCount(0);
    }

    /**
     * Deletes selected color from data model
     */
    public void deleteSelectedColorItem() {
        model.removeRow(table.convertRowIndexToModel(table.getSelectedRow()));
    }

    /**
     *
     * @param selectedFile
     */
    public void exportColorItems(File selectedFile) {
        //path
        String selectedFileParentPath = selectedFile.getParent();
        //name.xml or name
        String selectedFileName = selectedFile.getName();
        //name
        String selectedFileNameNoExtension = Xml_to_html.trimFileNameExtension(selectedFileName);
        //name.xml
        String xmlFileName = new StringBuilder(selectedFileNameNoExtension).append(".xml").toString();
        //name.html
        String htmlFileName = new StringBuilder(selectedFileNameNoExtension).append(".html").toString();
        //path/name.xml
        String selectedFilePath = new StringBuilder(selectedFileParentPath).append(File.separator).append(xmlFileName).toString();
        //
        transfer.exportColors(textFieldPaletteName.getText(), model, selectedFilePath);
        InputStream xslStream = getClass().getResourceAsStream("/style.xsl");
        File xmlFile = new File(selectedFileParentPath, xmlFileName);
        File htmlFile = new File(selectedFileParentPath, htmlFileName);
        xmlToHtml.setXmlFile(xmlFile);
        xmlToHtml.setXslStream(xslStream);
        xmlToHtml.setHtmlFile(htmlFile);
        xmlToHtml.transform();
    }

    /**
     *
     * @param path
     */
    public void importColorItems(String path) {
        transfer.importColors(model, path);
        textFieldPaletteName.setText(transfer.getPaletteName());
    }

    void clearPaletteName() {
        textFieldPaletteName.setText("");
    }
}
