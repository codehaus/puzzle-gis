/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.context.gui.datadialog;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.JComponent;

/**
 *
 * @author  Johann Sorel
 */
public class WaitingGlass extends JComponent {

    private static final int BAR_WIDTH = 200;
    private static final int BAR_HEIGHT = 10;
    private static final Color TEXT_COLOR = new Color(0x333333);
    private static final Color BORDER_COLOR = new Color(0x333333);
    private static final float[] GRADIENT_FRACTIONS = new float[]{
        0.0f, 0.499f, 0.5f, 1.0f
    };
    private static final Color[] GRADIENT_COLORS = new Color[]{
        Color.GRAY, Color.DARK_GRAY, Color.BLACK, Color.GRAY
    };
    private static final Color GRADIENT_COLOR2 = Color.WHITE;
    private static final Color GRADIENT_COLOR1 = Color.GRAY;
    private String message = "Downloading file...";
    private int progress = 0;

    public WaitingGlass() {
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent evt) {
                requestFocusInWindow();
            }
        });

        setFocusTraversalKeysEnabled(false);

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        int oldProgress = this.progress;
        this.progress = progress;

        // computes the damaged area
        FontMetrics metrics = getGraphics().getFontMetrics(getFont());
        int w = (int) (BAR_WIDTH * ((float) oldProgress / 100.0f));
        int x = w + (getWidth() - BAR_WIDTH) / 2;
        int y = (getHeight() - BAR_HEIGHT) / 2;
        y += metrics.getDescent() / 2;

        w = (int) (BAR_WIDTH * ((float) progress / 100.0f)) - w;
        int h = BAR_HEIGHT;

        repaint(x, y, w, h);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle clip = g.getClipBounds();

        AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
        g2.setComposite(alpha);

        g2.setColor(new Color(1, 1, 1, 0.30f));
        g.fillRect(clip.x, clip.y, clip.width, clip.height);


        int width = BAR_WIDTH+50;
        int height = BAR_HEIGHT+50;
        g2.setColor(new Color(0, 0, 0, 0.65f));
        g2.fillRoundRect((getWidth() - width) / 2, (getHeight() - height) / 2, width, height, 8, 8);



        // centers the progress bar on screen
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - BAR_WIDTH) / 2;
        int y = (getHeight() - BAR_HEIGHT - metrics.getDescent()) / 2;

        // draws the text
        g2.setColor(TEXT_COLOR);
        g2.drawString(message, x, y);

        // goes to the position of the progress bar
        y += metrics.getDescent();

        // computes the size of the progress indicator
        int w = (int) (BAR_WIDTH * ((float) progress / 100.0f));
        int h = BAR_HEIGHT;

        // draws the content of the progress bar
        Paint paint = g2.getPaint();

        // bar's background
        Paint gradient = new GradientPaint(x, y, GRADIENT_COLOR1, x, y + h, GRADIENT_COLOR2);
        g2.setPaint(gradient);
        g2.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        // actual progress
        gradient = new LinearGradientPaint(x, y, x, y + h, GRADIENT_FRACTIONS, GRADIENT_COLORS);
        g2.setPaint(gradient);
        g2.fillRect(x, y, w, h);

        g2.setPaint(paint);

        // draws the progress bar border
        g2.drawRect(x, y, BAR_WIDTH, BAR_HEIGHT);



    }

    public static ConvolveOp getBlurFilter(int radius) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];
        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }
        Kernel kernel = new Kernel(size, size, data);
        return new ConvolveOp(kernel);
    }
}
