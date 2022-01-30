/*
 * Copyright (C) 2021 Jorge R Garcia de Alba <jorge.r.garciadealba@gmail.com>
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

import io.github.xjrga.colorscheme.utilities.Message;
import io.github.xjrga.colorscheme.utilities.NumberCheck;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import io.github.xjrga.looks.harmonic.Categorizer;
import io.github.xjrga.looks.harmonic.Color_harmonic;
import io.github.xjrga.looks.themes.Dawn_150;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import io.github.xjrga.looks.harmonic.Harmonic_color;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.math.BigDecimal;
import javax.swing.JTextField;

/**
 * This class builds palette viewer application
 *
 * @author Jorge R Garcia de Alba &lt;jorge.r.garciadealba@gmail.com&gt;
 */
public class Palette_viewer {

    private final JFrame frame;
    private final JRadioButton optionBackground;
    private final JRadioButton optionFont;
    private final JRadioButton optionHarmonic;
    private final JRadioButton optionBorder;
    private Color selectedColor;
    private Color borderColor;
    private Color fontColor;
    private Color backgroundColor;
    private final Palette_chooser_panel paletteChooserPanel;
    private JColorChooser colorChooser;
    private JTabbedPane chooserTabbedPane;
    private final JFileChooser fileChooser;
    private final Categorizer categorizer;
    private final JPanel panelColorsRight;
    private final JPanel panelColorsLeft;
    private final JPanel panelColorsBottom;
    private final JPanel panelColorsTop;
    private final JPanel panelOriginal;
    private final JPanel panelColors;
    private boolean has_completed = true;

    /**
     * This class creates a palette viewer
     *
     */
    public Palette_viewer() {
        fileChooser = new JFileChooser();
        frame = new JFrame("Color Scheme");
        Image image = new ImageIcon(getClass().getResource("/logo.png")).getImage();
        frame.setIconImage(image);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        colorChooser = new JColorChooser();
        categorizer = new Categorizer();
        paletteChooserPanel = new Palette_chooser_panel();
        colorChooser.addChooserPanel(paletteChooserPanel);
        panelOriginal = new JPanel();
        panelOriginal.setLayout(new GridLayout(0, 13, 10, 10));
        panelOriginal.setBorder(new TitledBorder("Original"));
        panelColorsTop = new JPanel();
        panelColorsTop.setOpaque(true);
        panelColorsTop.setLayout(new GridLayout(0, 13, 10, 10));
        panelColorsTop.setBorder(new TitledBorder("Complementary"));
        panelColorsBottom = new JPanel();
        panelColorsBottom.setOpaque(true);
        panelColorsBottom.setLayout(new GridLayout(0, 13, 10, 10));
        panelColorsBottom.setBorder(new TitledBorder("Analogous"));
        panelColorsLeft = new JPanel();
        panelColorsLeft.setOpaque(true);
        panelColorsLeft.setLayout(new GridLayout(0, 13, 10, 10));
        panelColorsLeft.setBorder(new TitledBorder("Left"));
        panelColorsRight = new JPanel();
        panelColorsRight.setOpaque(true);
        panelColorsRight.setLayout(new GridLayout(0, 13, 10, 10));
        panelColorsRight.setBorder(new TitledBorder("Right"));
        panelColors = new JPanel();
        panelColors.setLayout(new GridLayout(0, 1));
        panelColors.setPreferredSize(new Dimension(0, 0));
        TitledBorder titledBorder = new TitledBorder("Color Wheel (HSV)");
        panelColors.setBorder(titledBorder);
        titledBorder.setTitleJustification(TitledBorder.RIGHT);
        panelColors.add(panelOriginal);
        panelColors.add(panelColorsTop);
        panelColors.add(panelColorsBottom);
        panelColors.add(panelColorsLeft);
        panelColors.add(panelColorsRight);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(panelColors);
        jScrollPane.setPreferredSize(new Dimension(600, 250));
        JPanel previewPanel = new JPanel();
        JButton previewPanelAddButton = new JButton("+");
        JButton previewPanelDeleteButton = new JButton("-");
        JButton previewPanelClearButton = new JButton("Clear");
        JButton previewPanelExportButton = new JButton("Export");
        JButton previewPanelImportButton = new JButton("Import");
        JButton previewPanelPickButton = new JButton("Pick");
        previewPanelClearButton.setToolTipText("Clear palette");
        previewPanelAddButton.setToolTipText("Add color to palette");
        previewPanelDeleteButton.setToolTipText("Delete color from palette");
        previewPanelExportButton.setToolTipText("Export palette");
        previewPanelImportButton.setToolTipText("Import palette");
        previewPanelPickButton.setToolTipText("Click the eyedropper button, the place your mouse anywhere on the screen to select that color");
        previewPanelDeleteButton.setEnabled(false);
        previewPanelExportButton.setEnabled(false);
        previewPanelImportButton.setEnabled(false);
        previewPanel.add(previewPanelClearButton);
        previewPanel.add(previewPanelAddButton);
        previewPanel.add(previewPanelDeleteButton);
        previewPanel.add(previewPanelExportButton);
        previewPanel.add(previewPanelImportButton);
        previewPanel.add(previewPanelPickButton);
        colorChooser.setPreviewPanel(previewPanel);
        JPanel panel00 = new JPanel();
        JPanel panel01 = new JPanel();
        panel00.setLayout(new FlowLayout());
        panel01.setLayout(new BorderLayout());
        optionBackground = new JRadioButton("Background");
        optionFont = new JRadioButton("Font");
        optionHarmonic = new JRadioButton("Harmonic");
        optionBorder = new JRadioButton("Border");
        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(optionBackground);
        group.add(optionFont);
        group.add(optionHarmonic);
        group.add(optionBorder);
        group.setSelected(optionHarmonic.getModel(), true);
        panel00.add(optionBackground);
        panel00.add(optionFont);
        panel00.add(optionBorder);
        panel00.add(optionHarmonic);
        panel01.add(panel00, BorderLayout.NORTH);
        panel01.add(jScrollPane, BorderLayout.CENTER);
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.add(colorChooser, JSplitPane.TOP);
        jSplitPane.add(panel01, JSplitPane.BOTTOM);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(jSplitPane, BorderLayout.CENTER);
        frame.setContentPane(new JScrollPane(mainPanel));
        frame.setPreferredSize(new Dimension(1496, 635));
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        previewPanelClearButton.addActionListener(e -> event_clearRecentColorPanel());
        previewPanelAddButton.addActionListener(e -> event_addColorItem());
        previewPanelDeleteButton.addActionListener(e -> event_deleteColorItem());
        previewPanelExportButton.addActionListener(e -> event_exportColorItems());
        previewPanelImportButton.addActionListener(e -> event_importColorItems());
        previewPanelPickButton.addActionListener(e -> event_pick_color());
        Component[] componentsColorChooser = colorChooser.getComponents();
        for (Component component : componentsColorChooser) {
            if (component instanceof JTabbedPane) {
                chooserTabbedPane = (JTabbedPane) component;
            }
        }
        chooserTabbedPane.addChangeListener((ChangeEvent e) -> {
            switch (chooserTabbedPane.getSelectedIndex()) {
                case 0:
                    previewPanelClearButton.setEnabled(true);
                    previewPanelAddButton.setEnabled(true);
                    previewPanelDeleteButton.setEnabled(false);
                    previewPanelExportButton.setEnabled(false);
                    previewPanelImportButton.setEnabled(false);
                    break;
                case 1:
                    previewPanelClearButton.setEnabled(false);
                    previewPanelAddButton.setEnabled(true);
                    previewPanelDeleteButton.setEnabled(false);
                    previewPanelExportButton.setEnabled(false);
                    previewPanelImportButton.setEnabled(false);
                    break;
                case 2:
                    previewPanelClearButton.setEnabled(false);
                    previewPanelAddButton.setEnabled(true);
                    previewPanelDeleteButton.setEnabled(false);
                    previewPanelExportButton.setEnabled(false);
                    previewPanelImportButton.setEnabled(false);
                    break;
                case 3:
                    previewPanelClearButton.setEnabled(false);
                    previewPanelAddButton.setEnabled(true);
                    previewPanelDeleteButton.setEnabled(false);
                    previewPanelExportButton.setEnabled(false);
                    previewPanelImportButton.setEnabled(false);
                    break;
                case 4:
                    previewPanelClearButton.setEnabled(false);
                    previewPanelAddButton.setEnabled(true);
                    previewPanelExportButton.setEnabled(false);
                    previewPanelDeleteButton.setEnabled(false);
                    previewPanelImportButton.setEnabled(false);
                    break;
                case 5:
                    previewPanelClearButton.setEnabled(true);
                    previewPanelAddButton.setEnabled(true);
                    previewPanelExportButton.setEnabled(true);
                    previewPanelDeleteButton.setEnabled(true);
                    previewPanelImportButton.setEnabled(true);
                    break;
            }
        });
        colorChooser.getSelectionModel().addChangeListener((ChangeEvent arg0) -> {
            selectedColor = colorChooser.getColor();
            if (has_completed) {
                updateGui();
            }
        });
    }

    private void updateGui() {
        if (optionFont.isSelected()) {
            fontColor = selectedColor;
            new Thread() {
                @Override
                public void run() {
                    has_completed(false);
                    ((TitledBorder) panelColors.getBorder()).setTitleColor(fontColor);
                    ((TitledBorder) panelOriginal.getBorder()).setTitleColor(fontColor);
                    ((TitledBorder) panelColorsLeft.getBorder()).setTitleColor(fontColor);
                    ((TitledBorder) panelColorsRight.getBorder()).setTitleColor(fontColor);
                    ((TitledBorder) panelColorsTop.getBorder()).setTitleColor(fontColor);
                    ((TitledBorder) panelColorsBottom.getBorder()).setTitleColor(fontColor);
                    panelColors.repaint();
                    Component[] componentsOriginal = panelOriginal.getComponents();
                    for (Component component : componentsOriginal) {
                        if (component instanceof JLabel) {
                            component.setForeground(fontColor);
                        }
                    }
                    Component[] componentsLeft = panelColorsLeft.getComponents();
                    for (Component component : componentsLeft) {
                        if (component instanceof JLabel) {
                            component.setForeground(fontColor);
                        }
                    }
                    Component[] componentsRight = panelColorsRight.getComponents();
                    for (Component component : componentsRight) {
                        if (component instanceof JLabel) {
                            component.setForeground(fontColor);
                        }
                    }
                    Component[] componentsTop = panelColorsTop.getComponents();
                    for (Component component : componentsTop) {
                        if (component instanceof JLabel) {
                            component.setForeground(fontColor);
                        }
                    }
                    Component[] componentsBottom = panelColorsBottom.getComponents();
                    for (Component component : componentsBottom) {
                        if (component instanceof JLabel) {
                            component.setForeground(fontColor);
                        }
                    }
                    has_completed(true);
                }
            }.start();
        } else if (optionBackground.isSelected()) {
            backgroundColor = selectedColor;
            new Thread() {
                @Override
                public void run() {
                    has_completed(false);
                    LineBorder paneLineBorder = new LineBorder(backgroundColor);
                    panelColors.setBackground(backgroundColor);
                    panelOriginal.setBackground(backgroundColor);
                    panelColorsLeft.setBackground(backgroundColor);
                    panelColorsRight.setBackground(backgroundColor);
                    panelColorsTop.setBackground(backgroundColor);
                    panelColorsBottom.setBackground(backgroundColor);
                    ((TitledBorder) panelOriginal.getBorder()).setBorder(paneLineBorder);
                    ((TitledBorder) panelColorsTop.getBorder()).setBorder(paneLineBorder);
                    ((TitledBorder) panelColorsBottom.getBorder()).setBorder(paneLineBorder);
                    ((TitledBorder) panelColorsLeft.getBorder()).setBorder(paneLineBorder);
                    ((TitledBorder) panelColorsRight.getBorder()).setBorder(paneLineBorder);
                    has_completed(true);
                }
            }.start();
        } else if (optionHarmonic.isSelected()) {
            Color_harmonic colorHarmonic = new Color_harmonic(selectedColor);
            Iterator<Harmonic_color> leftIterator = colorHarmonic.get_left_iterator();
            Iterator<Harmonic_color> rightIterator = colorHarmonic.get_right_iterator();
            Iterator<Harmonic_color> topIterator = colorHarmonic.get_top_iterator();
            Iterator<Harmonic_color> bottomIterator = colorHarmonic.get_bottom_iterator();
            new Thread() {
                @Override
                public void run() {
                    has_completed(false);
                    panelOriginal.removeAll();
                    panelOriginal.add(getLabel(colorHarmonic.get_hue_change_0()));
                    panelColorsLeft.removeAll();
                    while (leftIterator.hasNext()) {
                        Harmonic_color next = leftIterator.next();
                        panelColorsLeft.add(getLabel(next));
                    }
                    panelColorsRight.removeAll();
                    while (rightIterator.hasNext()) {
                        Harmonic_color next = rightIterator.next();
                        panelColorsRight.add(getLabel(next));
                    }
                    panelColorsTop.removeAll();
                    while (topIterator.hasNext()) {
                        Harmonic_color next = topIterator.next();
                        panelColorsTop.add(getLabel(next));
                    }
                    panelColorsBottom.removeAll();
                    while (bottomIterator.hasNext()) {
                        Harmonic_color next = bottomIterator.next();
                        panelColorsBottom.add(getLabel(next));
                    }
                    panelColors.revalidate();
                    panelColors.repaint();
                    has_completed(true);
                }

                private JLabel getLabel(Harmonic_color harmonicColor) {
                    JLabel label = new JLabel();
                    label.setOpaque(true);
                    label.setPreferredSize(new Dimension(50, 50));
                    label.setForeground(fontColor);
                    if (borderColor == null) {
                        borderColor = harmonicColor.get_base_color();
                    }
                    label.setBorder(new LineBorder(borderColor, 2));
                    label.setText(harmonicColor.get_angle() + "");
                    label.setBackground(harmonicColor.get_color());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setVerticalAlignment(SwingConstants.CENTER);
                    categorizer.set_harmonic_color(harmonicColor);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>");
                    sb.append("Temperature: ");
                    sb.append(String.valueOf(categorizer.get_color_temperature()));
                    sb.append("<br/>");
                    sb.append("Position: ");
                    sb.append(categorizer.get_color_position_tb());
                    sb.append(", ");
                    sb.append(categorizer.get_color_position_lr());
                    sb.append("<br/>");
                    sb.append("Type: ");
                    sb.append(categorizer.get_color_category());
                    sb.append("<br/>");
                    sb.append("Hue: ");
                    sb.append(harmonicColor.get_angle());
                    sb.append("<br/>");
                    sb.append("\u0394 Hue From Original: ");
                    sb.append(harmonicColor.get_angle_change());
                    label.setToolTipText(sb.toString());
                    return label;
                }
            }.start();
        } else if (optionBorder.isSelected()) {
            borderColor = selectedColor;
            new Thread() {
                @Override
                public void run() {
                    has_completed(false);
                    LineBorder labelLineBorder = new LineBorder(borderColor, 2);
                    Component[] componentsOriginal = panelOriginal.getComponents();
                    for (Component component : componentsOriginal) {
                        if (component instanceof JLabel) {
                            ((JComponent) component).setBorder(labelLineBorder);
                        }
                    }
                    Component[] componentsTop = panelColorsTop.getComponents();
                    for (Component component : componentsTop) {
                        if (component instanceof JLabel) {
                            ((JComponent) component).setBorder(labelLineBorder);
                        }
                    }
                    Component[] componentsBottom = panelColorsBottom.getComponents();
                    for (Component component : componentsBottom) {
                        if (component instanceof JLabel) {
                            ((JComponent) component).setBorder(labelLineBorder);
                        }
                    }
                    Component[] componentsLeft = panelColorsLeft.getComponents();
                    for (Component component : componentsLeft) {
                        if (component instanceof JLabel) {
                            ((JComponent) component).setBorder(labelLineBorder);
                        }
                    }
                    Component[] componentsRight = panelColorsRight.getComponents();
                    for (Component component : componentsRight) {
                        if (component instanceof JLabel) {
                            ((JComponent) component).setBorder(labelLineBorder);
                        }
                    }
                    has_completed(true);
                }
            }.start();
        }
    }

    private synchronized void has_completed(boolean b) {
        has_completed = b;
    }

    /**
     * Disposes application
     */
    public void exit() {
        frame.dispose();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
            MetalLookAndFeel.setCurrentTheme(new Dawn_150(font));
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        SwingUtilities.invokeLater(() -> {
            Palette_viewer palette = new Palette_viewer();
        });
    }

    /**
     * Resets recent color panel
     */
    public void resetRecentPanel() {
        for (AbstractColorChooserPanel p : colorChooser.getChooserPanels()) {
            if (p.getClass().getSimpleName().equals("DefaultSwatchChooserPanel")) {
                Field recentPanelField;
                try {
                    recentPanelField = p.getClass().getDeclaredField("recentSwatchPanel");
                    recentPanelField.setAccessible(true);
                    Object recentPanel = recentPanelField.get(p);
                    Method recentColorMethod = recentPanel.getClass().getMethod("setMostRecentColor", Color.class);
                    recentColorMethod.setAccessible(true);
                    for (int i = 0; i < 35; i++) {
                        recentColorMethod.invoke(recentPanel, new Color(95, 99, 102));
                    }
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                }
                break;
            }

        }
    }

    /**
     * Adds selected color to palette
     */
    public void event_addColorItem() {
        Color color = colorChooser.getColor();
        paletteChooserPanel.addColorItem(color);
    }

    private void event_deleteColorItem() {
        paletteChooserPanel.deleteSelectedColorItem();
    }

    private void event_clearRecentColorPanel() {
        if (chooserTabbedPane.getSelectedIndex() == 5) {
            paletteChooserPanel.clearAllColorItems();
            paletteChooserPanel.clearPaletteName();
        } else if (chooserTabbedPane.getSelectedIndex() == 0) {
            resetRecentPanel();
        }
    }

    private void event_exportColorItems() {
        int returnVal = fileChooser.showDialog(frame, "Export");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            fileChooser.setCurrentDirectory(new File(path));
            paletteChooserPanel.exportColorItems(fileChooser.getSelectedFile());
        }
    }

    private void event_importColorItems() {
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Xml Document", "xml"));
        int returnVal = fileChooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            fileChooser.setCurrentDirectory(new File(path));
            paletteChooserPanel.importColorItems(path);
        }
    }

    private void event_pick_color() {
        NumberCheck check = new NumberCheck();
        try {
            JTextField delay_text_field = new JTextField();
            delay_text_field.setText("2");
            JComponent[] inputs = new JComponent[]{
                new JLabel("Please set delay"),
                delay_text_field
            };
            int option = Message.INSTANCE.showOptionDialogOkCancel(inputs, "Eyedropper");
            check.addToUncheckedList(delay_text_field.getText());
            if (check.pass()) {
                if (option == 0) {
                    Robot robot = new Robot();
                    robot.delay(Integer.valueOf(delay_text_field.getText()) * 1000);
                    Point p = MouseInfo.getPointerInfo().getLocation();
                    BigDecimal x = new BigDecimal(p.getX());
                    BigDecimal y = new BigDecimal(p.getY());
                    Color color = robot.getPixelColor(x.intValue(), y.intValue());
                    paletteChooserPanel.addColorItem(color);
                    Message.INSTANCE.showMessage("Color saved to palette.", "Eyedropper");
                }
            }
        } catch (AWTException e) {
        }

    }
}
