package mainPack;

import java.util.ArrayList;


public final class ImprovedNoise {
    public ArrayList<Integer> p;
    double noiseArray[][][];
    double HeightDifferences[];
    double C = 0.1;
    double T = 0.01;
    public void setup(int SIZE)
    {
        p = new ArrayList<Integer>();
        for(int i = 0; i < 512; i++)
        {
            p.add((int)(Math.random()*15));
        }
        for(int i = 0; i < 512; i++)
        {
            int j = (int) (Math.random()*256 % 256);
            int nSwap = p.get(i);
            p.set(i, p.get(j));
            p.set(j, nSwap);
        }
    }
    
    public void generateHeightMap(int WIDTH, int HEIGHT)
    {
        noiseArray = new double[4][WIDTH][HEIGHT];
        HeightDifferences = new double[8];
        double increment = 0.02;
        double xoff = 0.0; // Start xoff at 0
        for (int i = 0; i < WIDTH-1;i++)
        {
            xoff += increment;   // Increment xoff 
            double yoff = 0.0;
            for(int j = 0; j < HEIGHT-1; j++)
            {
                yoff += increment;
                noiseArray[0][i][j] = ((noise(xoff,yoff,0)+1)*128);               
            }
        }
        
        increment = 0.02;
        xoff = 0.0; // Start xoff at 0
        for (int i = 0; i < WIDTH-1;i++)
        {
            xoff += increment;   // Increment xoff 
            double yoff = 0.0;
            for(int j = 0; j < HEIGHT-1; j++)
            {
                yoff += increment;
                noiseArray[1][i][j] = ((noise(xoff,yoff,0)+1)*128);
            }
        }
        
        increment = 0.5;
        xoff = 0.0; // Start xoff at 0
        for (int i = 0; i < WIDTH-1;i++)
        {
            xoff += increment;   // Increment xoff 
            double yoff = 0.0;
            for(int j = 0; j < HEIGHT-1; j++)
            {
                yoff += increment;
                noiseArray[2][i][j] = ((noise(xoff,yoff,0)+1)*128);
            }
        }
        
        for (int i = 0; i < WIDTH-1;i++)
        {
            for(int j = 0; j < HEIGHT-1; j++)
            {
                if(noiseArray[2][i][j]> 128)
                    noiseArray[3][i][j] = noiseArray[1][i][j];
                else
                    noiseArray[3][i][j] = noiseArray[0][i][j];
            }
        }
    }
    public void Erode(int iterations)
    {
        for (int i = 0; i < iterations; ++i)
        {
            for (int x = 0; x < noiseArray[0].length; ++x)
            {
                for (int y = 0; y < noiseArray[0].length; ++y)
                {
                    int X1 = x - 1;
                    int Y1 = y - 1;
                    X1=(int)clamp(X1, noiseArray[0].length-1, 0);
                    Y1=(int)clamp(Y1, noiseArray[0].length-1, 0);
                    int X2 = x + 1;
                    int Y2 = y + 1;
                    X2=(int)clamp(X2, noiseArray[0].length-1, 0);
                    Y2=(int)clamp(Y2, noiseArray[0].length-1, 0);
                    HeightDifferences[0] = GetHeightDifferences(x, y, X1, Y1);
                    HeightDifferences[1] = GetHeightDifferences(x, y, x, Y1);
                    HeightDifferences[2] = GetHeightDifferences(x, y, X2, Y1);
                    HeightDifferences[3] = GetHeightDifferences(x, y, X1, y);
                    HeightDifferences[4] = GetHeightDifferences(x, y, X2, y);
                    HeightDifferences[5] = GetHeightDifferences(x, y, X1, Y2);
                    HeightDifferences[6] = GetHeightDifferences(x, y, x, Y2);
                    HeightDifferences[7] = GetHeightDifferences(x, y, X2, Y2);
                    double MaxDifference  = 0.0;
                    double DifferenceTotal  = 0.0;
                    double NumberOver  = 0;
                    for (int z = 0; z < 8; z++)
                    {
                        if (HeightDifferences[z] > MaxDifference)
                        {
                           MaxDifference = HeightDifferences[z];
                        }
                        if (HeightDifferences[z] > T)
                        {
                            DifferenceTotal += HeightDifferences[z];
                            NumberOver++;
                        }
                    }
                    double Height = noiseArray[3][x][y];
                    if (X1 != x && Y1 != y && HeightDifferences[0] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3][X1][Y1];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3][X1][Y1] = Height;
                    }
                    if (Y1 != y && HeightDifferences[1] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3][x][Y1];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3][x][Y1] = Height;
                    }
                    if (X2 != x && Y1 != y && HeightDifferences[2] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3] [X2] [Y1];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3] [X2] [Y1] = Height;
                    }
                    if (X1 != x && HeightDifferences[3] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3] [X1] [y];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3] [X1] [y] = Height;
                    }
                    if (X2 != x && HeightDifferences[4] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3] [X2] [y];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3] [X2] [y] = Height;
                    }
                    if (X1 != x && Y2 != y && HeightDifferences[5] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3] [X1] [Y2];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3] [X1] [Y2] = Height;
                    }
                    if (Y2 != y && HeightDifferences[6] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3] [x] [Y2];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3] [x] [Y2] = Height;
                    }
                    if (X2 != x && Y2 != y && HeightDifferences[7] != 0.0f && DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3] [X2] [Y2];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3] [x] [Y2] = Height;
                    }
                    if (DifferenceTotal != 0.0f)
                    {
                        Height = noiseArray[3] [x] [Y2];
                        Height = Height + C * (MaxDifference - T) * (HeightDifferences[0] / DifferenceTotal);
                        Height = clamp(Height, 256, 0);
                        noiseArray[3] [x] [y] = Height;
                    }
                }
            }
        }
    }
    private double GetHeightDifferences(int x,int y,int X1,int Y1)
    {
        double Height = noiseArray[3][x][y];
        double Height2 = noiseArray[3][X1][Y1];
        return Height - Height2;
    }
    public double clamp (double i, double high, double low) 
    {
        return Math.max (Math.min (i, high), low);
    }
    public double noise(double x, double y, double z) 
    {
        
        int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
        Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
        Z = (int)Math.floor(z) & 255;
        x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
        y -= Math.floor(y);                                // OF POINT IN CUBE.
        z -= Math.floor(z);
        double u = fade(x),                                // COMPUTE FADE CURVES
        v = fade(y),                                // FOR EACH OF X,Y,Z.
        w = fade(z);
        
        int A = p.get(X)+Y, AA = p.get(A)+Z, AB = p.get(A+1)+Z,      // HASH COORDINATES OF
            B = p.get(X+1)+Y, BA = p.get(B) +Z, BB = p.get(B+1) +Z;      // THE 8 CUBE CORNERS,

        return lerp(w, lerp(v, lerp(u, grad(p.get(AA), x  , y  , z   ),  // AND ADD
                grad(p.get(BA), x-1, y, z)), // BLENDED
                lerp(u, grad(p.get(AB), x, y-1, z),  // RESULTS
                grad(p.get(BB), x-1, y-1, z))),// FROM  8
                lerp(v, lerp(u, grad(p.get(AA+1), x, y, z-1),  // CORNERS
                grad(p.get(BA+1), x-1, y, z-1)), // OF CUBE
                lerp(u, grad(p.get(AB+1), x, y-1, z-1),
                grad(p.get(BB+1), x-1, y-1, z-1))));
    }
    double fade(double t) { return  t * t * t * (t * (t * 6 - 15) + 10); }
    double lerp(double t, double a, double b) { return a + t * (b - a); }
    double grad(int hash, double x, double y, double z) {
    int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
    double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
    v = h<4 ? y : h==12||h==14 ? x : z;
    return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
    }
   
}

