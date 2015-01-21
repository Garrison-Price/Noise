/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPack;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author priceg3037
 */

public class Main extends JFrame
{
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    ImprovedNoise iNoise;
    Drawer jp;
    
    public Main(int iterations)
    {
        super("Noise");
        iNoise = new ImprovedNoise();
        iNoise.setup(WIDTH);
        iNoise.generateHeightMap(WIDTH, HEIGHT);
        iNoise.Erode(iterations);
        
        setSize(WIDTH,HEIGHT);
        //noise = new Perlin(WIDTH);
        //System.out.println(noise.n.size());
        jp = new Drawer(WIDTH,HEIGHT,iNoise.noiseArray[3]);
        this.add(jp);
        setVisible(true);
        
    }
    
    public static void main(String args[])
    {
        Main app = new Main(0);
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Main app2 = new Main(10);
        app2.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
