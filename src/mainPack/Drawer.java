/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mainPack;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author PriceG
 */
public class Drawer extends JPanel
{
    double[][] en;
    public Drawer(int w, int h, double[][] b)
    {
        en = b;
        this.setSize(w, h);
    }

    public void paint(Graphics g)
    {
        for(int x = 0; x < this.getHeight()-1; x++)
        {
            for(int y = 0; y < this.getHeight()-1; y++)
            {
                g.setColor(new Color((int)Math.floor(en[x][y]),(int)Math.floor(en[x][y]),(int)Math.floor(en[x][y])));
                g.drawOval(x,y,1,1);
            }
        }
    }
}
