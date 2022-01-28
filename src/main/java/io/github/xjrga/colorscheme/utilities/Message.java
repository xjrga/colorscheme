package io.github.xjrga.colorscheme.utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public enum Message {

    INSTANCE;

    //private BufferedImage logo = readImage("resources/images/logo.png");
    private final Image logo = new ImageIcon(getClass().getResource("/logo.png")).getImage();

    public void showMessage(String message, String title) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setIconImage(logo);
        dialog.setVisible(true);
    }

    public int showOptionDialog(JComponent[] inputs, String title) {
        JOptionPane optionPane = new JOptionPane(inputs, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setIconImage(logo);
        dialog.setVisible(true);
        //0 - Ok
        //2 - Cancel
        //null - x
        Object optionValue = optionPane.getValue();
        int value = -1;
        if (optionValue != null) {
            value = (int) optionValue;
        }
        return value;
    }

    public int showOptionDialogYesNo(JComponent[] inputs, String title) {
        JOptionPane optionPane = new JOptionPane(inputs, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setIconImage(logo);
        dialog.setVisible(true);
        //0 - Ok
        //2 - Cancel
        //null - x
        Object optionValue = optionPane.getValue();
        int value = -1;
        if (optionValue != null) {
            value = (int) optionValue;
        }
        return value;
    }

    public int showOptionDialogOkCancel(JComponent[] inputs, String title) {
        JOptionPane optionPane = new JOptionPane(inputs, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setIconImage(logo);
        dialog.setVisible(true);
        //0 - Ok
        //2 - Cancel
        //null - x
        Object optionValue = optionPane.getValue();
        int value = -1;
        if (optionValue != null) {
            value = (int) optionValue;
        }
        return value;
    }

    public void showMessage(JComponent[] inputs, String title) {
        JOptionPane optionPane = new JOptionPane(inputs, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setIconImage(logo);
        dialog.setVisible(true);
    }

    public BufferedImage readImage(String pathname) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pathname));
        } catch (IOException e) {
        }
        return img;
    }
}
