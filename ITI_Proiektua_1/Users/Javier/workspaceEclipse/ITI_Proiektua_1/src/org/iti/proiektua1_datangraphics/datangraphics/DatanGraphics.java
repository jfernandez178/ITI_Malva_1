// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DatanGraphics.java

package org.iti.proiektua1_datangraphics.datangraphics;

import datan.DatanUserFunction;
import datan.Histogram;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;
import javax.swing.*;

// Referenced classes of package datangraphics:
//            DatanGraphicsFrame

public final class DatanGraphics
{
    private static class CloseButtonListener
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            System.out.println("AL: close button activated");
            for(int i = 0; i < DatanGraphics.frames.size(); i++)
                if(DatanGraphics.frames.elementAt(i) != null)
                    ((DatanGraphicsFrame)DatanGraphics.frames.elementAt(i)).dispose();

            DatanGraphics.closeFrame.dispose();
        }

        private CloseButtonListener()
        {
        }

    }


    public DatanGraphics()
    {
    }

    private static void open()
    {
        if(!open)
        {
            open = true;
            workstation1Open = false;
            workstation2Open = false;
            dGFrame = new JFrame();
            setWindowInWorldCoordinates(0.0D, 0.0D, 0.0D, 0.0D);
            setViewportInWorldCoordinates(0.0D, 0.0D, 0.0D, 0.0D);
            setWindowInComputingCoordinates(0.0D, 0.0D, 0.0D, 0.0D);
            setFormat(0.0D, 0.0D);
            screendim = Toolkit.getDefaultToolkit().getScreenSize();
            JFrame jframe = new JFrame();
            jframe.setVisible(true);
            startPoint = jframe.getLocation();
            wlisttop = startPoint.y + 1;
            Dimension dimension = jframe.getSize();
            Dimension dimension1 = jframe.getContentPane().getSize();
            wlisthdel = (dimension.width - dimension1.width) / 2;
            if(wlisthdel < 2)
                wlisthdel = 2;
            wlistvdel = dimension.height - dimension1.height - wlisthdel;
            jframe.dispose();
            numpolys = 0;
            bufPointsLengthFilled = 0;
            bufPolyLengthFilled = 0;
            frames = new Vector();
            frameIndex = -1;
            bufPointsLengthTotal = 0x27100;
            bufPoints = new short[bufPointsLengthTotal];
            bufPolyLengthTotal = 1000;
            bufPoly = new int[bufPolyLengthTotal];
            numpolys = 0;
            qac[0] = 0.0D;
            qbc[0] = 1.0D;
            qac[1] = 0.0D;
            qbc[1] = 1.0D;
            qaw2[0] = 0.0D;
            qbw2[0] = 1.0D;
            qaw2[1] = 0.0D;
            qbw2[1] = 1.0D;
            qawp[0] = 0.0D;
            qbwp[0] = 1.0D;
            qawp[0] = 0.0D;
            qbwp[0] = 1.0D;
            iqplcl = 1;
            lwrel = new boolean[1];
            lwrel[0] = false;
        }
    }

    private static void close()
    {
        if(workstation1Open)
            closeWorkstation();
        if(!frames.isEmpty())
        {
            closeFrame = new JFrame("Close graphics frame(s)");
            closeFrame.setSize(300, 100);
            closeFrame.setResizable(false);
            Container container = closeFrame.getContentPane();
            container.setLayout(new FlowLayout());
            JButton jbutton = new JButton("close graphics frame(s)");
            CloseButtonListener closebuttonlistener = new CloseButtonListener();
            jbutton.addActionListener(closebuttonlistener);
            container.add(jbutton);
            closeFrame.setVisible(true);
        }
        if(workstation2Open)
            closeWorkstation();
        open = false;
    }

    public static void openWorkstation(String s)
    {
        openWorkstation(s, "");
    }

    public static void openWorkstation(String s, String s1)
    {
        boolean flag = false;
        boolean flag1 = false;
        if(!s.equals("") && !s.equals(" "))
            flag = true;
        if(!s1.equals("") && !s1.equals(" "))
            flag1 = true;
        if(!open)
            open();
        if(workstation1Open || workstation2Open)
        {
            JOptionPane _tmp = pane;
            JOptionPane _tmp1 = pane;
            JOptionPane.showMessageDialog(dGFrame, "Cannot open workstation; open already", "", 0);
        }
        if(flag)
        {
            workstation1Open = true;
            workstation1Title = s;
        }
        if(flag1)
        {
            workstation2Open = true;
            String s2 = getExtension(s1);
            psModus = 0;
            if(s2.equals("ps"))
                psModus = 0;
            else
            if(s2.equals("eps"))
            {
                psModus = 1;
            } else
            {
                JOptionPane _tmp2 = pane;
                JOptionPane _tmp3 = pane;
                JOptionPane.showMessageDialog(dGFrame, "File extension must be .ps or .eps", "", 0);
            }
        }
        psFilename = s1;
    }

    public static void closeWorkstation()
    {
        if(workstation1Open)
        {
            showPlot();
            workstation1Open = false;
        }
        if(workstation2Open)
        {
            writePSFile(psFilename);
            workstation2Open = false;
        }
        numpolys = 0;
    }

    private static void showPlot()
    {
        flushPolyline();
        frameIndex = frames.size();
        boolean flag = false;
        if(frames.contains(null))
        {
            flag = true;
            frameIndex = frames.indexOf(null);
        }
        DatanGraphicsFrame datangraphicsframe = new DatanGraphicsFrame(workstation1Title, psFilename, frameIndex, qform[0], qform[1], numpolys, bufPoly, bufPoints);
        bufPointsLengthFilled = 0;
        if(flag)
            frames.setElementAt(datangraphicsframe, frameIndex);
        else
            frames.addElement(datangraphicsframe);
    }

    private static void writePSFile(String s)
    {
        flushPolyline();
        FileOutputStream afileoutputstream[] = new FileOutputStream[1];
        PrintStream aprintstream[] = new PrintStream[1];
        if(!openPrintStream(s, afileoutputstream, aprintstream))
        {
            return;
        } else
        {
            pr = aprintstream[0];
            exportPS(true);
            closePrintStream(s, afileoutputstream[0], pr);
            bufPointsLengthFilled = 0;
            return;
        }
    }

    private static boolean openPrintStream(String s, FileOutputStream afileoutputstream[], PrintStream aprintstream[])
    {
        try
        {
            afileoutputstream[0] = new FileOutputStream(s);
        }
        catch(IOException ioexception)
        {
            JOptionPane _tmp = pane;
            JOptionPane _tmp1 = pane;
            JOptionPane.showMessageDialog(dGFrame, "Cannot open file (E)PS File\n", "Error in DatanGraphics", 0);
            System.out.println((new StringBuilder()).append("Cannot open file (E)PS File\n").append(ioexception.getMessage()).toString());
            return false;
        }
        aprintstream[0] = new PrintStream(afileoutputstream[0]);
        return true;
    }

    private static void closePrintStream(String s, FileOutputStream fileoutputstream, PrintStream printstream)
    {
        printstream.close();
        try
        {
            fileoutputstream.close();
        }
        catch(IOException ioexception)
        {
            JOptionPane _tmp = pane;
            JOptionPane _tmp1 = pane;
            JOptionPane.showMessageDialog(dGFrame, "Cannot close file (E)PS File\n", "Error in DatanGraphics", 0);
            System.out.println((new StringBuilder()).append("Cannot close file\n").append(ioexception.getMessage()).toString());
        }
    }

    private static void exportPS(boolean flag)
    {
        char ac[] = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'
        };
        float af[] = new float[3];
        short word1 = 1;
        int l2 = 0;
        int i3 = 0;
        framecoords[0] = qad[0];
        framecoords[1] = qad[1];
        framecoords[2] = qbd[0];
        framecoords[3] = qbd[1];
        calculatePlotSize();
        float f = 118.1102F * (float)(plsizval[0] / ((framecoords[2] - framecoords[0]) + 1.0D) + plsizval[1] / ((framecoords[3] - framecoords[1]) + 1.0D));
        float f1 = 64F * f;
        if(psModus == 0)
        {
            pr.println("%!PS-Adobe-2.0");
            pr.println("initgraphics");
        } else
        {
            pr.println("%!PS-Adobe-2.0 EPSF-1.2");
            pr.println("%%Creator: DATAN");
            pr.print("%%CreationDate: ");
            pr.println();
            pr.println("%%DocumentFonts:");
            pr.println("%%Pages: 1");
            pr.print("%%BoundingBox: 0 0 ");
            pr.print((int)Math.ceil((framecoords[2] - framecoords[0]) * (double)f * 0.12D));
            pr.print(' ');
            pr.println((int)Math.ceil((framecoords[3] - framecoords[1]) * (double)f * 0.12D));
            pr.print("%%EndComments\n%%EndProlog\n%%Page: 1\n");
        }
        lwrelPS = false;
        if(lwrelPS)
        {
            pr.print("/p {");
            pr.print(f1);
            pr.print(" mul setlinewidth\n");
        } else
        {
            pr.print("/p {2.3622 mul setlinewidth\n");
        }
        pr.print("moveto dup 0 eq {0 1}if {rlineto}repeat stroke}bind def\n");
        pr.print("/z { ");
        ctab[0].getRGBColorComponents(af);
        for(int i1 = 0; i1 < 3; i1++)
        {
            pr.print(af[i1]);
            pr.print(' ');
        }

        pr.print("setrgbcolor} def\n");
        if(lwrelPS)
        {
            pr.print("/t {");
            pr.print(f1);
            pr.print(" mul setlinewidth\n");
        } else
        {
            pr.print("/t {2.3622 mul setlinewidth\n");
        }
        pr.print("moveto dup 0 eq {0 1}if {rlineto}repeat gsave z closepath\nfill grestore stroke} bind def\n.12 .12 scale\n1 setlinecap\n1 setlinejoin\n");
        for(int i = 0; i < 8; i++)
        {
            pr.print("/");
            pr.print(ac[i]);
            pr.print(" { ");
            ctab[i + 1].getRGBColorComponents(af);
            for(int j1 = 0; j1 < 3; j1++)
            {
                pr.print(af[j1]);
                pr.print(' ');
            }

            pr.print("setrgbcolor ");
            pr.print(linw[i]);
            pr.print("} def\n");
        }

        if(psModus == 0)
        {
            pr.print(psOffsets[0] * 236.22047424316406D);
            pr.print(" ");
            pr.print(psOffsets[1] * 236.22047424316406D);
            pr.println(" translate");
            if(rotatePlot)
            {
                pr.print(plsizval[1] * 236.22047424316406D);
                pr.println(" 0 translate");
                pr.println("90 rotate");
            }
        }
        short word2 = -1;
        int j2 = -1;
        int k2 = -1;
        outpst(1, 0);
        for(int k1 = 0; k1 < numpolys; k1++)
        {
            int l1 = bufPoly[k1];
            short word0 = bufPoints[l1 + 1];
            int i2 = 0;
            if(word0 > 0)
            {
                word1 = bufPoints[l1];
                tracoords(bufPoints[l1 + 2], bufPoints[l1 + 3], f, ixyc);
                l2 = ixyc[0];
                i3 = ixyc[1];
                boolean flag1 = word1 == word2 && l2 == j2 && i3 == k2;
                for(int l = 1; l < word0; l++)
                {
                    tracoords(bufPoints[l1 + 2 * l + 2], bufPoints[l1 + 2 * l + 3], f, ixyc);
                    int j3 = ixyc[0];
                    int k3 = ixyc[1];
                    int l3 = l2 - j3;
                    int i4 = i3 - k3;
                    if(l3 != 0 || i4 != 0)
                    {
                        i2++;
                        outpst(0, l3);
                        outpst(0, i4);
                        flag1 = false;
                        l2 = j3;
                        i3 = k3;
                    }
                    if((i2 >= 50 || l == word0 - 1) && !flag1)
                    {
                        outpst(0, i2);
                        outpst(0, j3);
                        outpst(0, k3);
                        outpst(5, word1);
                        flag1 = true;
                        i2 = 0;
                    }
                }

            }
            word2 = word1;
            j2 = l2;
            k2 = i3;
        }

        outpst(3, 0);
        if(psModus != 0)
            pr.print("%%Trailer\n");
        pr.print("showpage\n");
        pr.flush();
    }

    private static void calculatePlotSize()
    {
        double d = papersize[0] - paperborders[0] - paperborders[1];
        double d1 = papersize[1] - paperborders[2] - paperborders[3];
        double d2 = d1 / d;
        double d3 = qform[1] / qform[0];
        plsizval[0] = qform[0];
        plsizval[1] = qform[1];
        rotatePlot = d3 > 1.0D && d2 < 1.0D || d3 < 1.0D && d2 > 1.0D;
        if(psModus == 0)
        {
            double d4;
            double d5;
            if(rotatePlot)
            {
                d4 = plsizval[1] / d;
                d5 = plsizval[0] / d1;
            } else
            {
                d4 = plsizval[0] / d;
                d5 = plsizval[1] / d1;
            }
            double d6 = Math.max(d4, d5);
            if(d6 > 1.0D)
            {
                plsizval[0] = plsizval[0] / d6;
                plsizval[1] = plsizval[1] / d6;
            }
            psOffsets[0] = paperborders[0];
            psOffsets[1] = paperborders[2];
            if(!rotatePlot)
            {
                psOffsets[0] += (d - plsizval[0]) * 0.5D;
                psOffsets[1] += (d1 - plsizval[1]) * 0.5D;
            } else
            {
                psOffsets[0] += (d - plsizval[1]) * 0.5D;
                psOffsets[1] += (d1 - plsizval[0]) * 0.5D;
            }
        }
    }

    private static void outpst(int i, int l)
    {
        if(i == 1)
        {
            istr = 0;
            return;
        }
        if(i == 3)
        {
            pr.write(bstrng, 0, istr);
            pr.println();
            istr = 0;
            return;
        }
        int k1 = inttobytes(l, bcmstr, 12);
        ilen = 12 - k1;
        if(i == 1 || i == 5)
        {
            if(i == 1)
            {
                if(istr > 0)
                {
                    bstrng[istr] = 32;
                    istr++;
                }
                for(int i1 = 0; i1 < ilen; i1++)
                    bstrng[istr + i1] = bcmstr[k1 + i1];

                istr += ilen;
            } else
            {
                bstrng[istr] = 32;
                bstrng[istr + 1] = (byte)((97 + l) - 1);
                istr += 2;
            }
            bstrng[istr] = 32;
            istr++;
            if(iqpsfp)
                bstrng[istr] = 116;
            else
                bstrng[istr] = 112;
            istr++;
            return;
        }
        if(istr + ilen >= 70)
        {
            pr.write(bstrng, 0, istr);
            pr.println();
            istr = 0;
        }
        if(istr > 0)
        {
            bstrng[istr] = 32;
            istr++;
        }
        for(int j1 = 0; j1 < ilen; j1++)
            bstrng[istr + j1] = bcmstr[k1 + j1];

        istr += ilen;
    }

    private static void tracoords(short word0, short word1, float f, int ai[])
    {
        ai[0] = (int)((float)((double)word0 - framecoords[0]) * f + 0.5F);
        ai[1] = (int)((float)((double)word1 - framecoords[1]) * f + 0.5F);
    }

    private static int inttobytes(int i, byte abyte0[], int l)
    {
        char ac[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        };
        int j1 = i < 0 ? -i : i;
        int i1;
        for(i1 = l; i1 > 0 && j1 != 0; j1 /= 10)
        {
            i1--;
            int k1 = j1 % 10;
            abyte0[i1] = (byte)ac[k1];
        }

        if(i == 0)
        {
            i1--;
            abyte0[i1] = (byte)ac[0];
        } else
        if(i1 > 0 && i < 0)
        {
            i1--;
            abyte0[i1] = 45;
        }
        return i1;
    }

    public static void setWindowInComputingCoordinates(double d, double d1, double d2, double d3)
    {
        qac[0] = d;
        qac[1] = d2;
        qbc[0] = d1;
        qbc[1] = d3;
        if(qac[0] == qbc[0])
            qbc[0] = qac[0] + 1.0D;
        if(qac[1] == qbc[1])
            qbc[1] = qac[1] + 1.0D;
        setTransformations();
    }

    public static void setWindowInWorldCoordinates(double d, double d1, double d2, double d3)
    {
        if(d > d1)
        {
            qawp[0] = d1;
            qbwp[0] = d;
        } else
        {
            qawp[0] = d;
            qbwp[0] = d1;
        }
        if(d2 > d3)
        {
            qawp[1] = d3;
            qbwp[1] = d2;
        } else
        {
            qawp[1] = d2;
            qbwp[1] = d3;
        }
        if(qawp[0] == qbwp[0])
            qbwp[0] = qawp[0] + 1.0D;
        if(qawp[1] == qbwp[1])
            qbwp[1] = qawp[1] + 1.0D;
        setTransformations();
    }

    public static void setViewportInWorldCoordinates(double d, double d1, double d2, double d3)
    {
        qaw2[0] = d;
        qaw2[1] = d2;
        qbw2[0] = d1;
        qbw2[1] = d3;
        if(qaw2[0] == qbw2[0])
            qbw2[0] = qaw2[0] + 1.0D;
        if(qaw2[1] == qbw2[1])
            qbw2[1] = qaw2[1] + 1.0D;
        setTransformations();
    }

    private static void setTransformations()
    {
        for(int i = 0; i < 2; i++)
        {
            qdc[i] = qbc[i] - qac[i];
            qdci[i] = 1.0D / qdc[i];
            qdw2[i] = qbw2[i] - qaw2[i];
            qdw2i[i] = 1.0D / qdw2[i];
            qdwp[i] = qbwp[i] - qawp[i];
            qdwpi[i] = 1.0D / qdwp[i];
        }

        double d = qdwp[1] / qdwp[0];
        double d1 = qdd[1] / qdd[0];
        if(d1 > d)
        {
            double d2 = (qdd[1] * d) / d1;
            double d4 = 0.5D * (qdd[1] - d2);
            qadp[1] = qad[1] + d4;
            qbdp[1] = qbd[1] - d4;
            qadp[0] = qad[0];
            qbdp[0] = qbd[0];
        } else
        {
            double d3 = (qdd[0] * d1) / d;
            double d5 = 0.5D * (qdd[0] - d3);
            qadp[0] = qad[0] + d5;
            qbdp[0] = qbd[0] - d5;
            qadp[1] = qad[1];
            qbdp[1] = qbd[1];
        }
        qddp[0] = qbdp[0] - qadp[0];
        qddp[1] = qbdp[1] - qadp[1];
        qddpi[0] = 1.0D / qddp[0];
        qddpi[1] = 1.0D / qddp[1];
    }

    private static void writeWindows()
    {
        System.out.println((new StringBuilder()).append("c : ").append(qac[0]).append(" , ").append(qac[1]).append(" , ").append(qbc[0]).append(" , ").append(qbc[1]).append(" , ").append(qdc[0]).append(" , ").append(qdc[1]).append(" , ").append(qdci[0]).append(" , ").append(qdci[1]).toString());
        System.out.println((new StringBuilder()).append("w2 : ").append(qaw2[0]).append(" , ").append(qaw2[1]).append(" , ").append(qbw2[0]).append(" , ").append(qbw2[1]).append(" , ").append(qdw2[0]).append(" , ").append(qdw2[1]).append(" , ").append(qdw2i[0]).append(" , ").append(qdw2i[1]).toString());
        System.out.println((new StringBuilder()).append("wp : ").append(qawp[0]).append(" , ").append(qawp[1]).append(" , ").append(qbwp[0]).append(" , ").append(qbwp[1]).append(" , ").append(qdwp[0]).append(" , ").append(qdwp[1]).append(" , ").append(qdwpi[0]).append(" , ").append(qdwpi[1]).toString());
        System.out.println((new StringBuilder()).append("d : ").append(qad[0]).append(" , ").append(qad[1]).append(" , ").append(qbd[0]).append(" , ").append(qbd[1]).append(" , ").append(qdd[0]).append(" , ").append(qdd[1]).append(" , ").append(qddi[0]).append(" , ").append(qddi[1]).toString());
        System.out.println((new StringBuilder()).append("dp : ").append(qadp[0]).append(" , ").append(qadp[1]).append(" , ").append(qbdp[0]).append(" , ").append(qbdp[1]).append(" , ").append(qddp[0]).append(" , ").append(qddp[1]).append(" , ").append(qddpi[0]).append(" , ").append(qddpi[1]).toString());
        System.out.println((new StringBuilder()).append("clipping : ").append(qadcl[0]).append(" , ").append(qadcl[1]).append(" , ").append(qbdcl[0]).append(" , ").append(qbdcl[1]).toString());
        System.out.println((new StringBuilder()).append("form : ").append(qform[0]).append(" , ").append(qform[1]).toString());
        System.out.println((new StringBuilder()).append("framecoords : ").append(framecoords[0]).append(" , ").append(framecoords[1]).append(" , ").append(framecoords[2]).append(" , ").append(framecoords[3]).toString());
    }

    public static void chooseColor(int i)
    {
        flushPolyline();
        iqplcl = i;
    }

    static void move(double d, double d1)
    {
        double d2 = transformSingleWorldToDevice(d, 0);
        double d3 = transformSingleWorldToDevice(d1, 1);
        if(iqnplc > 1)
            flushPolyline();
        if(inClippingWindow(d2, d3))
        {
            iqnplc = 1;
            qoutx[0] = d2;
            qouty[0] = d3;
        } else
        {
            iqnplc = 0;
        }
        lastPoint[0] = d2;
        lastPoint[1] = d3;
    }

    static void draw(double d, double d1)
    {
        double d2 = transformSingleWorldToDevice(d, 0);
        double d3 = transformSingleWorldToDevice(d1, 1);
        boolean flag1 = inClippingWindow(lastPoint[0], lastPoint[1]);
        boolean flag2 = inClippingWindow(d2, d3);
        if(iqnplc >= iqnplm - 1)
        {
            flushPolyline();
            iqnplc = 1;
            qoutx[0] = lastPoint[0];
            qouty[0] = lastPoint[1];
        } else
        if(flag1 && flag2)
        {
            qoutx[iqnplc] = d2;
            qouty[iqnplc] = d3;
            iqnplc++;
            lastPoint[0] = d2;
            lastPoint[1] = d3;
        } else
        if(flag1 && !flag2)
        {
            double ad[] = borderPoint(lastPoint[0], lastPoint[1], d2, d3);
            qoutx[iqnplc] = ad[0];
            qouty[iqnplc] = ad[1];
            iqnplc++;
            lastPoint[0] = d2;
            lastPoint[1] = d3;
        } else
        if(!flag1 && flag2)
        {
            double ad1[] = borderPoint(d2, d3, lastPoint[0], lastPoint[1]);
            flushPolyline();
            iqnplc = 1;
            qoutx[0] = ad1[0];
            qouty[0] = ad1[1];
            qoutx[iqnplc] = d2;
            qouty[iqnplc] = d3;
            iqnplc++;
            lastPoint[0] = d2;
            lastPoint[1] = d3;
        } else
        {
            boolean flag;
            if(flag = lineCrossesClippingWindow(lastPoint[0], lastPoint[1], d2, d3, borderPoints))
            {
                flushPolyline();
                iqnplc = 1;
                qoutx[0] = borderPoints[0];
                qouty[0] = borderPoints[1];
                qoutx[iqnplc] = borderPoints[2];
                qouty[iqnplc] = borderPoints[3];
                iqnplc++;
            }
            lastPoint[0] = d2;
            lastPoint[1] = d3;
        }
    }

    private static boolean lineCrossesClippingWindow(double d, double d1, double d2, double d3, 
            double ad[])
    {
        boolean flag = false;
        if((d >= qadcl[0] || d2 >= qadcl[0]) && (d <= qbdcl[0] || d2 <= qbdcl[0]) && (d1 >= qadcl[1] || d3 >= qadcl[1]) && (d1 <= qbdcl[1] || d3 <= qbdcl[1]))
            if(qadcl[0] < d && d < qbdcl[0] && qadcl[0] < d2 && d2 < qbdcl[0])
            {
                ad[1] = qadcl[1];
                ad[3] = qbdcl[1];
                ad[0] = d + ((qadcl[1] - d1) * (d2 - d)) / (d3 - d1);
                ad[2] = d + ((qbdcl[1] - d1) * (d2 - d)) / (d3 - d1);
                flag = true;
            } else
            if(qadcl[1] < d1 && d1 < qbdcl[1] && qadcl[1] < d3 && d3 < qbdcl[1])
            {
                ad[0] = qadcl[0];
                ad[2] = qbdcl[0];
                ad[1] = d1 + ((qadcl[0] - d) * (d3 - d1)) / (d2 - d);
                ad[3] = d1 + ((qadcl[0] - d) * (d3 - d1)) / (d2 - d);
                flag = true;
            } else
            {
                int i = 0;
                ad[2 * i] = d + ((qadcl[1] - d1) * (d2 - d)) / (d3 - d1);
                ad[2 * i + 1] = qadcl[1];
                if(qadcl[0] <= ad[2 * i] && ad[2 * i] <= qbdcl[0])
                    i++;
                ad[2 * i] = d + ((qbdcl[1] - d1) * (d2 - d)) / (d3 - d1);
                ad[2 * i + 1] = qbdcl[1];
                if(qadcl[0] <= ad[2 * i] && ad[2 * i] <= qbdcl[0])
                    i++;
                if(i < 2)
                {
                    ad[2 * i] = qadcl[0];
                    ad[2 * i + 1] = d1 + ((qadcl[0] - d) * (d3 - d1)) / (d2 - d);
                    if(qadcl[1] <= ad[2 * i + 1] && ad[2 * i + 1] <= qbdcl[1])
                        i++;
                }
                if(i < 2)
                {
                    ad[2 * i] = qbdcl[0];
                    ad[2 * i + 1] = d1 + ((qbdcl[0] - d) * (d3 - d1)) / (d2 - d);
                    if(qadcl[1] <= ad[2 * i + 1] && ad[2 * i + 1] <= qbdcl[1])
                        i++;
                }
                if(i == 2)
                    flag = true;
            }
        return flag;
    }

    private static double[] borderPoint(double d, double d1, double d2, double d3)
    {
        double ad[] = new double[2];
        int i = regionOfExternalPoint(d2, d3);
        switch(i)
        {
        default:
            break;

        case 1: // '\001'
            double d10 = qadcl[1];
            ad[0] = d + ((d10 - d1) * (d2 - d)) / (d3 - d1);
            ad[1] = d10;
            break;

        case 2: // '\002'
            double d11 = qbdcl[1];
            ad[0] = d + ((d11 - d1) * (d2 - d)) / (d3 - d1);
            ad[1] = d11;
            break;

        case 3: // '\003'
            double d4 = qadcl[0];
            ad[0] = d4;
            ad[1] = d1 + ((d4 - d) * (d3 - d1)) / (d2 - d);
            break;

        case 4: // '\004'
            double d5 = qbdcl[0];
            ad[0] = d5;
            ad[1] = d1 + ((d5 - d) * (d3 - d1)) / (d2 - d);
            break;

        case 5: // '\005'
            double d12 = qadcl[1];
            double d6 = qadcl[0];
            ad[0] = d + ((d12 - d1) * (d2 - d)) / (d3 - d1);
            ad[1] = d12;
            if(ad[0] < d6)
            {
                ad[0] = d6;
                ad[1] = d1 + ((d6 - d) * (d3 - d1)) / (d2 - d);
            }
            break;

        case 6: // '\006'
            double d13 = qbdcl[1];
            double d7 = qadcl[0];
            ad[0] = d + ((d13 - d1) * (d2 - d)) / (d3 - d1);
            ad[1] = d13;
            if(ad[0] > d7)
            {
                ad[0] = d7;
                ad[1] = d1 + ((d7 - d) * (d3 - d1)) / (d2 - d);
            }
            break;

        case 7: // '\007'
            double d14 = qadcl[1];
            double d8 = qadcl[0];
            ad[0] = d + ((d14 - d1) * (d2 - d)) / (d3 - d1);
            ad[1] = d14;
            if(ad[0] < d8)
            {
                ad[0] = d8;
                ad[1] = d1 + ((d8 - d) * (d3 - d1)) / (d2 - d);
            }
            break;

        case 8: // '\b'
            double d15 = qbdcl[1];
            double d9 = qbdcl[0];
            ad[0] = d + ((d15 - d1) * (d2 - d)) / (d3 - d1);
            ad[1] = d15;
            if(ad[0] > d9)
            {
                ad[0] = d9;
                ad[1] = d1 + ((d9 - d) * (d3 - d1)) / (d2 - d);
            }
            break;
        }
        return ad;
    }

    private static boolean inClippingWindow(double d, double d1)
    {
        boolean flag = false;
        if(qadcl[0] <= d && d <= qbdcl[0] && qadcl[1] <= d1 && d1 <= qbdcl[1])
            flag = true;
        return flag;
    }

    private static int regionOfExternalPoint(double d, double d1)
    {
        byte byte0 = 0;
        if(d < qadcl[0])
        {
            if(d1 < qadcl[1])
                byte0 = 5;
            else
            if(d1 > qbdcl[1])
                byte0 = 7;
            else
                byte0 = 3;
        } else
        if(d > qbdcl[0])
        {
            if(d1 < qadcl[1])
                byte0 = 6;
            else
            if(d1 > qbdcl[1])
                byte0 = 8;
            else
                byte0 = 4;
        } else
        if(d1 < qadcl[1])
            byte0 = 1;
        else
        if(d1 > qbdcl[1])
            byte0 = 2;
        else
            byte0 = 0;
        return byte0;
    }

    private static void drawLine(double ad[], double ad1[], double d)
    {
        double ad2[] = new double[2];
        double d2 = ad1[0] - ad[0];
        double d3 = ad1[1] - ad[1];
        double d1 = Math.sqrt(d2 * d2 + d3 * d3);
        int l = 0;
        if(d > 0.0D)
            l = (int)(d1 / d + 0.5D);
        int i1 = 2 * l + 1;
        d2 /= i1;
        d3 /= i1;
        int i = 1;
        ad2[0] = ad[0];
        ad2[1] = ad[1];
        move(ad2[0], ad2[1]);
        for(int j1 = 0; j1 < i1; j1++)
        {
            i = -i;
            ad2[0] = ad2[0] + d2;
            ad2[1] = ad2[1] + d3;
            if(i < 0)
                draw(ad2[0], ad2[1]);
            else
                move(ad2[0], ad2[1]);
        }

    }

    public static void drawContour(double d, double d1, double d2, double d3, 
            int i, int l, double d4, DatanUserFunction datanuserfunction)
    {
        double ad[] = new double[500];
        setSmallClippingWindow();
        double d13 = 0.0D;
        double d14 = 0.0D;
        for(int i1 = 0; i1 < i; i1++)
        {
            double d5 = d + (double)i1 * d2;
            double d6 = d5 + d2;
            for(int j1 = 0; j1 < l; j1++)
            {
                double d7 = d1 + (double)j1 * d3;
                double d8 = d7 + d3;
                double d9;
                double d10;
                if(j1 == 0)
                {
                    d9 = datanuserfunction.getValue(d5, d7);
                    d10 = datanuserfunction.getValue(d6, d7);
                } else
                {
                    d9 = d13;
                    d10 = d14;
                }
                double d11;
                if(i1 != 0 && j1 < 500)
                    d11 = ad[j1];
                else
                    d11 = datanuserfunction.getValue(d5, d8);
                double d12 = datanuserfunction.getValue(d6, d8);
                if(j1 < 500)
                    ad[j1] = d12;
                d13 = d11;
                d14 = d12;
                drawContourForPixel(d5, d6, d7, d8, d9, d11, d10, d12, d4);
            }

        }

        setBigClippingWindow();
    }

    private static void drawContourForPixel(double d, double d1, double d2, double d3, 
            double d4, double d5, double d6, double d7, double d8)
    {
        double ad[] = {
            0.0D, 0.0D
        };
        double ad1[] = {
            0.0D, 0.0D
        };
        double d23 = 1E-027D;
        double d15 = 0.0D;
        double d16 = 0.0D;
        double d17 = 0.0D;
        double d18 = 0.0D;
        double d19 = 0.0D;
        double d20 = 0.0D;
        double d21 = 0.0D;
        double d22 = 0.0D;
        double d9 = d1 - d;
        double d10 = d3 - d2;
        double d11 = d8 - d4;
        double d12 = d5 - d8;
        double d13 = d8 - d7;
        double d14 = d6 - d8;
        boolean flag;
        if(d11 * d14 < 0.0D)
        {
            flag = false;
        } else
        {
            flag = true;
            if(Math.abs(d11 + d14) < d23)
                d15 = d;
            else
                d15 = d + (d9 * d11) / (d14 + d11);
            d19 = d2;
        }
        boolean flag1;
        if(d12 * d11 < 0.0D)
        {
            flag1 = false;
        } else
        {
            flag1 = true;
            d16 = d;
            if(Math.abs(d11 + d12) < d23)
                d20 = d2;
            else
                d20 = d2 + (d10 * d11) / (d11 + d12);
        }
        boolean flag2;
        if(d13 * d12 < 0.0D)
        {
            flag2 = false;
        } else
        {
            flag2 = true;
            if(Math.abs(d13 + d12) < d23)
                d17 = d;
            else
                d17 = d + (d9 * d12) / (d13 + d12);
            d21 = d3;
        }
        boolean flag3;
        if(d14 * d13 < 0.0D)
        {
            flag3 = false;
        } else
        {
            flag3 = true;
            d18 = d1;
            if(Math.abs(d14 + d13) < d23)
                d22 = d2;
            else
                d22 = d2 + (d10 * d14) / (d14 + d13);
        }
        if(flag)
        {
            ad[0] = d15;
            ad1[0] = d19;
            if(flag1)
            {
                ad[1] = d16;
                ad1[1] = d20;
                drawPolyline(ad, ad1);
            }
            if(flag2)
            {
                ad[1] = d17;
                ad1[1] = d21;
                drawPolyline(ad, ad1);
            }
            if(flag3)
            {
                ad[1] = d18;
                ad1[1] = d22;
                drawPolyline(ad, ad1);
            }
        }
        if(flag1)
        {
            ad[0] = d16;
            ad1[0] = d20;
            if(flag2)
            {
                ad[1] = d17;
                ad1[1] = d21;
                drawPolyline(ad, ad1);
            }
            if(flag3)
            {
                ad[1] = d18;
                ad1[1] = d22;
                drawPolyline(ad, ad1);
            }
        }
        if(flag2 && flag3)
        {
            ad[0] = d17;
            ad1[0] = d21;
            ad[1] = d18;
            ad1[1] = d22;
            drawPolyline(ad, ad1);
        }
    }

    public static void drawCaption(double d, String s)
    {
        double ad[] = {
            0.0D, 0.0D
        };
        if(d <= 0.0D)
            d = 1.0D;
        double d1 = qbwp[0] - qawp[0];
        double d2 = qbwp[1] - qawp[1];
        d = 0.014999999999999999D * d * Math.sqrt(d1 * d1 + d2 * d2);
        double d3 = qawp[0] + 0.5D * d1;
        double d4 = qawp[1] + 0.90000000000000002D * d2;
        text(3, d3, d4, 1.0D, 0.0D, d, s, ad);
    }

    public static void drawText(double d, double d1, double d2, String s)
    {
        double ad[] = {
            0.0D, 0.0D
        };
        if(d2 <= 0.0D)
            d2 = 1.0D;
        d2 = 0.014999999999999999D * d2 * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        double d3 = transformSingleComputingToWorld(d, 0);
        double d4 = transformSingleComputingToWorld(d1, 1) + 0.5D * d2;
        text(1, d3, d4, 1.0D, 0.0D, d2, s, ad);
    }

    public static void drawHistogram(Histogram histogram)
    {
        double ad[] = new double[3];
        double ad1[] = new double[3];
        double ad2[] = new double[2];
        double ad3[] = new double[2];
        setSmallClippingWindow();
        double d = histogram.getLowerBoundary();
        double d1 = histogram.getBinSize();
        int i = histogram.getNumberOfBins();
        double ad4[] = histogram.getContents();
        for(int l = 0; l < i; l++)
        {
            ad[0] = d + (double)l * d1;
            ad[1] = ad[0];
            ad[2] = ad[0] + d1;
            if(l == 0)
                ad1[0] = 0.0D;
            else
                ad1[0] = ad4[l - 1];
            ad1[1] = ad4[l];
            ad1[2] = ad1[1];
            drawPolyline(ad, ad1);
        }

        ad2[0] = ad[2];
        ad3[0] = ad1[2];
        ad2[1] = ad[2];
        ad3[1] = 0.0D;
        drawPolyline(ad2, ad3);
        setBigClippingWindow();
    }

    public static void drawDatapoint(int i, double d, double d1, double d2, double d3, double d4, double d5)
    {
        double ad[] = new double[91];
        double ad1[] = new double[91];
        double d16 = d;
        if(d16 <= 0.0D)
            d16 = 1.0D;
        d16 = d16 * 0.01D * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        int l = Math.abs(i);
        boolean flag2 = false;
        if(i < 0)
            flag2 = true;
        if(l <= 1 || l > 9)
            l = 1;
        drawMark(l, d, d1, d2);
        setSmallClippingWindow();
        double d15 = d16;
        if(l >= 7)
            d15 = 0.0D;
        if(l == 3 || l == 4)
            d15 /= 1.4139999999999999D;
        double d27 = transformSingleComputingToWorld(d1, 0);
        double d28 = transformSingleComputingToWorld(d2, 1);
        double d41 = 0.0D;
        double d43 = 0.0D;
        double d37 = 0.0D;
        double d39 = 0.0D;
        boolean flag = false;
        boolean flag1 = false;
        if(d3 > 0.0D)
        {
            double d12 = d27 + d15;
            double d10 = d27 - d15;
            double d25 = d1 + d3;
            double d42 = transformSingleComputingToWorld(d25, 0);
            if(d42 > d12)
                flag = true;
            if(flag)
            {
                move(d12, d28);
                draw(d42, d28);
                double d34 = d28 - d16;
                double d35 = d28 + d16;
                move(d42, d34);
                draw(d42, d35);
                double d23 = d1 - d3;
                double d38 = transformSingleComputingToWorld(d23, 0);
                move(d10, d28);
                draw(d38, d28);
                move(d38, d34);
                draw(d38, d35);
            }
        }
        if(d4 > 0.0D)
        {
            double d13 = d28 + d15;
            double d11 = d28 - d15;
            double d26 = d2 + d4;
            double d44 = transformSingleComputingToWorld(d26, 1);
            if(d44 > d13)
                flag1 = true;
            if(flag1)
            {
                move(d27, d13);
                draw(d27, d44);
                double d33 = d27 - d16;
                double d36 = d27 + d16;
                move(d33, d44);
                draw(d36, d44);
                double d24 = d2 - d4;
                double d40 = transformSingleComputingToWorld(d24, 1);
                move(d27, d11);
                draw(d27, d40);
                move(d33, d40);
                draw(d36, d40);
            }
        }
        d15 = d5;
        if(d3 > 0.0D && d4 > 0.0D && Math.abs(d15) < 1.0D && flag && flag1)
        {
            double d32 = d5 * d3 * d4;
            if(flag2 || Math.abs(d15) > 9.9999997473787516E-005D)
            {
                double d7 = d3 * d3;
                double d8 = d4 * d4;
                double d45 = d15 * d15;
                boolean flag3 = false;
                if(Math.abs((d3 - d4) / (d3 + d4)) < 0.0001D)
                    flag3 = true;
                double d18;
                if(flag3)
                    d18 = 0.78539816339744828D;
                else
                    d18 = 0.5D * Math.atan2(d32 * 2D, d7 - d8);
                double d17 = Math.sin(d18);
                double d14 = Math.cos(d18);
                double d22 = d17 * d17;
                double d19 = d14 * d14;
                double d6 = d7 * d8 * (1.0D - d45);
                double d9 = (d8 * d19 - d32 * 2D * d17 * d14) + d7 * d22;
                double d20 = Math.sqrt(Math.abs(d6 / d9));
                d9 = d8 * d22 + d32 * 2D * d17 * d14 + d7 * d19;
                double d21 = Math.sqrt(Math.abs(d6 / d9));
                for(int i1 = 0; i1 < 91; i1++)
                {
                    double d31 = (double)i1 * 0.069809999999999997D;
                    double d29 = d20 * Math.cos(d31);
                    double d30 = d21 * Math.sin(d31);
                    ad[i1] = (d1 + d29 * d14) - d30 * d17;
                    ad1[i1] = d2 + d29 * d17 + d30 * d14;
                }

                drawPolyline(ad, ad1);
            }
        }
        setBigClippingWindow();
    }

    public static void drawMark(int i, double d, double d1, double d2)
    {
        setSmallClippingWindow();
        double d4 = d;
        if(d4 <= 0.0D)
            d4 = 1.0D;
        d4 = d4 * 0.01D * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        int j1 = i;
        if(j1 < 1 || j1 > 9)
            j1 = 1;
        double d5 = transformSingleComputingToWorld(d1, 0);
        double d6 = transformSingleComputingToWorld(d2, 1);
        if(j1 <= 6)
        {
            int i1 = 1;
            byte byte0;
            double d16;
            if(j1 <= 2)
            {
                if(j1 == 2)
                    i1 = 10;
                d16 = 0.0D;
                byte0 = 46;
            } else
            if(j1 == 3 || j1 == 4)
            {
                if(j1 == 4)
                    i1 = 10;
                d16 = 0.78500000000000003D;
                byte0 = 5;
            } else
            {
                if(j1 == 6)
                    i1 = 10;
                d16 = 0.0D;
                byte0 = 5;
            }
            for(int k1 = 1; k1 <= i1; k1++)
            {
                double d9 = 6.2830000000000004D / (double)(byte0 - 1);
                double d3 = d4 * (1.0D - (double)(k1 - 1) / (double)i1);
                for(int i2 = 1; i2 <= byte0; i2++)
                {
                    double d14 = d16 + (double)(i2 - 1) * d9;
                    double d10 = d5 + d3 * Math.cos(d14);
                    double d12 = d6 + d3 * Math.sin(d14);
                    if(i2 == 1)
                        move(d10, d12);
                    else
                        draw(d10, d12);
                }

            }

        } else
        {
            int l;
            double d17;
            if(j1 == 7)
            {
                d17 = 0.0D;
                l = 2;
            } else
            if(j1 == 8)
            {
                d17 = 0.78500000000000003D;
                l = 2;
            } else
            {
                d17 = 0.0D;
                l = 4;
            }
            for(int l1 = 1; l1 <= l; l1++)
            {
                double d15 = d17 + ((double)(l1 - 1) * 6.2830000000000004D) / ((double)l * 2D);
                double d7 = d4 * Math.cos(d15);
                double d8 = d4 * Math.sin(d15);
                double d11 = d5 + d7;
                double d13 = d6 + d8;
                move(d11, d13);
                d11 = d5 - d7;
                d13 = d6 - d8;
                draw(d11, d13);
            }

        }
        setBigClippingWindow();
    }

    public static void drawPolyline(double ad[], double ad1[])
    {
        setSmallClippingWindow();
        int i = ad.length;
        for(int l = 0; l < i; l++)
        {
            double d = qaw2[0] + (ad[l] - qac[0]) * qdw2[0] * qdci[0];
            double d1 = qaw2[1] + (ad1[l] - qac[1]) * qdw2[1] * qdci[1];
            if(l == 0)
                move(d, d1);
            else
                draw(d, d1);
        }

    }

    public static void drawBrokenPolyline(int i, double d, double ad[], double ad1[])
    {
        setSmallClippingWindow();
        double d13 = d;
        if(d13 < 0.0D)
            d13 = 1.0D;
        int l = i;
        if(l < 1 || l > 3)
            l = 1;
        d13 = 0.01D * d13 * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        double d1;
        double d2;
        double d3;
        if(l == 1)
        {
            d1 = d13;
            d2 = d13;
            d3 = d13;
        } else
        if(l == 2)
        {
            d2 = 0.10000000000000001D * d13;
            d1 = d2;
            d3 = d13;
        } else
        {
            d1 = d13;
            d2 = 0.10000000000000001D * d13;
            d3 = 0.5D * d13;
        }
        double d7 = 0.0D;
        boolean flag = true;
        double d4 = d1;
        d7 = 0.0D;
        double d9 = 0.0D;
        double d10 = 0.0D;
        boolean flag2 = false;
label0:
        for(int i1 = 0; i1 < ad.length; i1++)
        {
            boolean flag1 = true;
            do
            {
                if(!flag1)
                    continue label0;
                double d11 = qaw2[0] + (ad[i1] - qac[0]) * qdw2[0] * qdci[0];
                double d12 = qaw2[1] + (ad1[i1] - qac[1]) * qdw2[1] * qdci[1];
                if(i1 == 0)
                {
                    d9 = d11;
                    d10 = d12;
                    move(d9, d10);
                    flag1 = false;
                } else
                {
                    double d5 = Math.sqrt((d11 - d9) * (d11 - d9) + (d12 - d10) * (d12 - d10));
                    double d8 = d7 + d5;
                    double d6 = d8 - d4;
                    if(d6 <= 0.0D)
                    {
                        d7 += d5;
                        flag1 = false;
                    } else
                    {
                        d11 -= ((d11 - d9) * d6) / d5;
                        d12 -= ((d12 - d10) * d6) / d5;
                        d7 = 0.0D;
                        flag1 = true;
                    }
                    if(flag)
                        draw(d11, d12);
                    else
                        move(d11, d12);
                    d9 = d11;
                    d10 = d12;
                    if(flag1)
                    {
                        flag = !flag;
                        if(flag)
                        {
                            flag2 = !flag2;
                            if(flag2)
                                d4 = d2;
                            else
                                d4 = d1;
                        } else
                        {
                            d4 = d3;
                        }
                    }
                }
            } while(true);
        }

        flushPolyline();
        setBigClippingWindow();
    }

    public static void drawBoundary()
    {
        double ad[] = new double[5];
        double ad1[] = new double[5];
        ad[0] = qac[0];
        ad1[0] = qac[1];
        ad[1] = qbc[0];
        ad1[1] = ad1[0];
        ad[2] = ad[1];
        ad1[2] = qbc[1];
        ad[3] = ad[0];
        ad1[3] = ad1[2];
        ad[4] = ad[0];
        ad1[4] = ad1[0];
        drawPolylineInBigClippingWindow(ad, ad1);
    }

    private static void drawPolylineInBigClippingWindow(double ad[], double ad1[])
    {
        setBigClippingWindow();
        int i = ad.length;
        for(int l = 0; l < i; l++)
        {
            double d = qaw2[0] + (ad[l] - qac[0]) * qdw2[0] * qdci[0];
            double d1 = qaw2[1] + (ad1[l] - qac[1]) * qdw2[1] * qdci[1];
            if(l == 0)
                move(d, d1);
            else
                draw(d, d1);
        }

    }

    private static void flushPolyline()
    {
        if(iqnplc > 0)
        {
            if(bufPolyLengthFilled == bufPolyLengthTotal)
            {
                newBufPoly = new int[2 * bufPolyLengthTotal];
                for(int i = 0; i < bufPolyLengthFilled; i++)
                    newBufPoly[i] = bufPoly[i];

                bufPoly = newBufPoly;
                bufPolyLengthTotal = 2 * bufPolyLengthTotal;
            }
            if(bufPointsLengthFilled + iqnplc + 2 > bufPointsLengthTotal)
            {
                newBufPoints = new short[2 * bufPointsLengthTotal];
                for(int l = 0; l < bufPointsLengthFilled; l++)
                    newBufPoints[l] = bufPoints[l];

                bufPoints = newBufPoints;
                bufPointsLengthTotal = 2 * bufPointsLengthTotal;
            }
            int i1 = bufPointsLengthFilled;
            bufPoly[numpolys] = i1;
            numpolys++;
            bufPoints[i1] = (short)iqplcl;
            bufPoints[i1 + 1] = (short)iqnplc;
            i1 += 2;
            for(int j1 = 0; j1 < iqnplc; j1++)
            {
                bufPoints[i1] = (short)(int)qoutx[j1];
                bufPoints[i1 + 1] = (short)(int)qouty[j1];
                i1 += 2;
            }

            bufPolyLengthFilled = numpolys;
            bufPointsLengthFilled = i1;
            iqnplc = 0;
        }
    }

    private static void drawArrow(double ad[], int i, String s, double d)
    {
        double ad1[] = new double[2];
        double ad2[] = new double[4];
        double ad3[] = {
            0.0D, 0.0D
        };
        int i1 = Math.abs(i);
        int l;
        if(i1 == 1)
            l = (3 + i) / 2;
        else
            l = 2;
        boolean flag = false;
        for(int k1 = 0; k1 < s.length(); k1++)
            flag = flag || s.charAt(k1) != ' ';

        if(flag)
        {
            text(l, ad[0], ad[1], 1.0D, 0.0D, d, s, ad3);
            double d3;
            if(i < 1)
                d3 = -1D;
            else
                d3 = 1.0D;
            int j1 = 3 - i1;
            double d2 = 0.80000000000000004D * d;
            double d1;
            if(i1 == 1)
            {
                ad1[1] = ad[1];
                d1 = Math.max((Math.abs(qbw2[0] - qaw2[0]) - Math.abs(ad3[0] - ad[0])) * 0.25D, 0.0D);
                ad1[0] = ad3[0] - d3 * (d1 + d2);
            } else
            {
                ad1[0] = ad[0] - Math.abs(ad[0] - ad3[0]) * 0.5D;
                d1 = Math.max((Math.abs(qbw2[1] - qaw2[1]) - d) * 0.25D, 0.0D);
                ad1[1] = ad3[1] - d3 * (d2 + d1 + d * 0.5D);
            }
            if(d1 <= 0.0D)
                d2 = 0.0D;
            if(d1 > 0.0D)
            {
                if(d2 > d1)
                    d1 = d2;
                for(int l1 = 1; l1 <= 5; l1++)
                {
                    ad2[0] = ad1[0];
                    ad2[1] = ad1[1];
                    if(l1 == 2 || l1 == 3 || l1 == 5)
                        ad2[i1 - 1] = ad1[i1 - 1] + d3 * (d1 - d2);
                    if(l1 == 4)
                        ad2[i1 - 1] = ad1[i1 - 1] + d3 * d1;
                    if(l1 == 3)
                        ad2[j1 - 1] = ad1[j1 - 1] + d2 * 0.40000000000000002D;
                    if(l1 == 5)
                        ad2[j1 - 1] = ad1[j1 - 1] - d2 * 0.40000000000000002D;
                    if(l1 == 1)
                        move(ad2[0], ad2[1]);
                    else
                        draw(ad2[0], ad2[1]);
                    if(l1 == 2)
                    {
                        ad2[2] = ad2[0];
                        ad2[3] = ad2[1];
                    }
                }

                draw(ad2[2], ad2[3]);
            }
        }
    }

    public static void drawCoordinateCross()
    {
        setSmallClippingWindow();
        double ad[] = new double[2];
        double ad1[] = new double[2];
        ad[0] = qac[0];
        ad[1] = qbc[0];
        ad1[0] = 0.0D;
        ad1[1] = 0.0D;
        if(qac[1] * qbc[1] < 0.0D)
            drawBrokenPolyline(1, 1.0D, ad, ad1);
        ad[0] = 0.0D;
        ad[1] = 0.0D;
        ad1[0] = qac[1];
        ad1[1] = qbc[1];
        if(qac[0] * qbc[0] < 0.0D)
            drawBrokenPolyline(1, 1.0D, ad, ad1);
        setBigClippingWindow();
    }

    public static void drawFrame()
    {
        move(qawp[0], qawp[1]);
        draw(qbwp[0], qawp[1]);
        draw(qbwp[0], qbwp[1]);
        draw(qawp[0], qbwp[1]);
        draw(qawp[0], qawp[1]);
    }

    public static void setFormat(double d, double d1)
    {
        if(d == 0.0D && d1 == 0.0D)
            d = 5D;
        if(d1 == 0.0D)
        {
            idin = (int)d;
            if(idin < 1)
                idin = 1;
            if(idin > 10)
                idin = 10;
            qform[0] = cdin / Math.pow(root2, idin);
            qform[1] = qform[0] / root2;
        } else
        if(d == 0.0D)
        {
            idin = (int)d1;
            if(idin < 1)
                idin = 1;
            if(idin > 10)
                idin = 10;
            qform[1] = cdin / Math.pow(root2, idin);
            qform[0] = qform[1] / root2;
        } else
        {
            qform[0] = d;
            qform[1] = d1;
        }
        rformt = qform[0] / qform[1];
        if(rformt > 1.0D)
        {
            qad[0] = 0.0D;
            qbd[0] = 32767D;
            qad[1] = 0.5D * (1.0D - 1.0D / rformt) * 32767D;
            qbd[1] = 0.5D * (1.0D + 1.0D / rformt) * 32767D;
        } else
        {
            qad[1] = 0.0D;
            qbd[1] = 32767D;
            qad[0] = 0.5D * (1.0D - rformt) * 32767D;
            qbd[0] = 0.5D * (1.0D + rformt) * 32767D;
        }
        for(int i = 0; i < 2; i++)
        {
            qdd[i] = qbd[i] - qad[i];
            qddi[i] = 1.0D / qdd[i];
            qadc[i] = 0.0D;
            qbdc[i] = qform[i];
            qadp[i] = qad[i];
            qbdp[i] = qbd[i];
            qddp[i] = qdd[i];
            qddpi[i] = qddi[i];
        }

        setTransformations();
    }

    private static void writePolylineBuffer()
    {
        if(numpolys > 0)
        {
            int i = 0;
            for(int l = 0; l < numpolys; l++)
            {
                short word0 = bufPoints[i + 1];
                i += 2;
                for(int i1 = 0; i1 < word0; i1++)
                    i += 2;

            }

        }
    }

    public static void setSmallClippingWindow()
    {
        flushPolyline();
        qadcl[0] = transformSingleWorldToDevice(qaw2[0], 0);
        qadcl[1] = transformSingleWorldToDevice(qaw2[1], 1);
        qbdcl[0] = transformSingleWorldToDevice(qbw2[0], 0);
        qbdcl[1] = transformSingleWorldToDevice(qbw2[1], 1);
    }

    public static void setBigClippingWindow()
    {
        flushPolyline();
        qadcl[0] = qadp[0];
        qadcl[1] = qadp[1];
        qbdcl[0] = qbdp[0];
        qbdcl[1] = qbdp[1];
    }

    private void closeFrame(JFrame jframe)
    {
        jframe.dispose();
    }

    private static void text(int i, double d, double d1, double d2, double d3, double d4, String s, double ad[])
    {
        rv[0] = d2;
        rv[1] = d3;
        gv[0] = -d3;
        gv[1] = d2;
        gvabs2 = gv[0] * gv[0] + gv[1] * gv[1];
        gvabs = Math.sqrt(gv[0] * gv[0] + gv[1] * gv[1]);
        rvabs2 = 0.0D;
        rvabs2 = rv[0] * rv[0] + rv[1] * rv[1];
        rvabs = Math.sqrt(rv[0] * rv[0] + rv[1] * rv[1]);
        avin[0] = d;
        avin[1] = d1;
        for(int l = 0; l < 2; l++)
        {
            rve[l] = rv[l] / rvabs;
            gve[l] = gv[l] / gvabs;
        }

        gr = d4;
        c = s.getBytes();
        grlett();
        for(int i1 = 0; i1 < 2; i1++)
        {
            if(i == 1)
            {
                av[i1] = avin[i1] - gr * 0.5D * gve[i1];
                op[i1] = avin[i1] + alengt * rve[i1];
                continue;
            }
            if(i == 2)
            {
                av[i1] = avin[i1] - gr * 0.5D * gve[i1] - alengt * rve[i1];
                op[i1] = avin[i1] - alengt * rve[i1];
                continue;
            }
            if(i == 3)
            {
                av[i1] = avin[i1] - alengt * 0.5D * rve[i1];
                op[i1] = avin[i1] + gr * 0.5D * gve[i1];
            } else
            {
                av[i1] = avin[i1] - gr * gve[i1] - alengt * 0.5D * rve[i1];
                op[i1] = avin[i1] - gr * 0.5D * gve[i1];
            }
        }

        ad[0] = op[0];
        ad[1] = op[1];
        for(int j1 = 0; j1 < nuj; j1++)
        {
            nu = (int)((double)j[j1] / 1000D + 0.050000000000000003D);
            if(nu >= 0)
                ga = gr;
            if(nu == 1 || nu == 4)
                ga = gr * fg1;
            if(nu == 2 || nu == 3 || nu == 5 || nu == 6)
                ga = gr * fg1 * fg2;
            if(idop[j1 + 1] == 1)
                g1 = ga;
            if(nu >= 0)
                gz = 0.0D;
            if(nu >= 1)
                gz = gr - (gr * fg1) / 2D;
            if(nu == 4)
                gz = -(gr * fg1) / 2D;
            if(nu == 2)
                gz = gr + (gr * fg1 - gr * fg1 * fg2) / 2D;
            if(nu == 3)
                gz = gr - (gr * fg1 + gr * fg1 * fg2) / 2D;
            if(nu == 5)
                gz = (gr * fg1 - gr * fg1 * fg2) / 2D;
            if(nu == 6)
                gz = -(gr * fg1 + gr * fg1 * fg2) / 2D;
            for(int k1 = 0; k1 < 2; k1++)
                avnext[k1] = av[k1] + gz * gve[k1];

            id = j[j1] - nu * 1000;
            grsymb();
            if(idop[j1 + 1] == 1)
                continue;
            if(idop[j1] == 1)
                ga = g1;
            for(int l1 = 0; l1 < 2; l1++)
                av[l1] += ga * rve[l1] * (((double)iga / utx + z / utx) - (double)miga / utx);

        }

    }

    static void grlett()
    {
        int i = c.length;
        i = c.length;
        nuj = 0;
        nunu[0] = 0;
        nunu[1] = 0;
        nunu[2] = 0;
        na1 = 1;
        na5 = 0;
        for(no = 0; no < i; no++)
        {
            idop[no] = 0;
            j[no] = 0;
        }

        alengt = 0.0D;
        for(nur = 0; nur < i; nur++)
        {
            iascii = c[nur];
            na2 = 0;
            for(nul = 0; nul < 3; nul++)
                if(iascii == k[nul])
                    na2 = nul + 1;

            boolean flag;
            if(na2 != 0)
            {
                na1 = na2;
                flag = false;
            } else
            {
                flag = true;
            }
            if(flag)
            {
                na4 = 0;
                for(nul = 0; nul < 3; nul++)
                    if(iascii == kk[nul])
                        na4 = nul + 1;

                if(na4 != 0)
                {
                    if(na4 == 2 && na5 == 0)
                        na5 = 2;
                    na5 = na4 + na5;
                    if(na4 == 3)
                        na5 = 0;
                    if(na5 > 6)
                        na5 = 0;
                    flag = false;
                } else
                {
                    flag = true;
                }
            }
            if(flag)
                if(iascii == kkk)
                {
                    idop[nuj] = 1;
                    flag = false;
                } else
                {
                    flag = true;
                }
            if(flag)
            {
                na3 = 63;
                for(nul = 0; nul < 94; nul++)
                    if(iascii == m[nul])
                        na3 = nul + 1;

                nuj++;
                if(na1 == 1)
                    j[nuj - 1] = mm[na3 - 1];
                if(na1 == 2)
                    j[nuj - 1] = ll[na3 - 1];
                if(na1 == 3)
                    j[nuj - 1] = lll[na3 - 1];
                id = j[nuj - 1];
                j[nuj - 1] += na5 * 1000;
                if(idop[nuj - 1] == 1)
                    flag = false;
                else
                    flag = true;
            }
            if(!flag)
                continue;
            grleng();
            if(na5 == 0)
            {
                alengt += gr * (((double)iga / utx + z / utx) - (double)miga / utx);
                continue;
            }
            if(na5 == 1 || na5 == 4)
            {
                alengt += gr * fg1 * (((double)iga / utx + z / utx) - (double)miga / utx);
                continue;
            }
            if(na5 == 2 || na5 == 3 || na5 >= 5)
                alengt += gr * fg1 * fg2 * (((double)iga / utx + z / utx) - (double)miga / utx);
        }

    }

    static void grsymb()
    {
        move(avnext[0], avnext[1]);
        grleng();
        for(int i = 0; i < ko; i++)
        {
            int l = (int)(((double)Math.abs(idat[i]) / 100D + 0.10000000000000001D) - (double)miga);
            int i1 = (int)((double)Math.abs(idat[i]) - (double)(l + miga) * 100D);
            for(int j1 = 0; j1 < 2; j1++)
            {
                wert2d[j1] = avnext[j1] + ((double)l / utx) * rve[j1] * ga;
                wert2d[j1] = (wert2d[j1] + ((double)i1 / uty) * gve[j1] * ga) - (gve[j1] * 10D * ga) / uty;
            }

            if(idat[i] < 0)
                move(wert2d[0], wert2d[1]);
            else
                draw(wert2d[0], wert2d[1]);
        }

    }

    static void grleng()
    {
        for(int i = 0; i < 25; i++)
            idat[i] = iqlett[i + (id - 1) * 25];

        miga = 0;
        ko = 0;
        for(int l = 0; l < 25 && idat[l] != -999; l++)
            ko++;

        if(ko != 0)
        {
            iga1 = Math.abs(idat[0]);
            miga1 = Math.abs(idat[0]);
            for(ih = 0; ih < ko; ih++)
            {
                if(Math.abs(idat[ih]) > iga1)
                    iga1 = Math.abs(idat[ih]);
                if(Math.abs(idat[ih]) < miga1)
                    miga1 = Math.abs(idat[ih]);
            }

            iga = (int)((double)iga1 / 100D);
            miga = (int)((double)miga1 / 100D);
        } else
        {
            iga = 19;
        }
    }

    static double[] transformWorldToComputing(double ad[])
    {
        double ad1[] = new double[2];
        for(int i = 0; i < 2; i++)
            ad1[i] = qac[i] + (ad[i] - qaw2[i]) * qdc[i] * qdw2i[i];

        return ad1;
    }

    static double[] transformComputingToWorld(double ad[])
    {
        double ad1[] = new double[2];
        for(int i = 0; i < 2; i++)
            ad1[i] = qaw2[i] + (ad[i] - qac[i]) * qdw2[i] * qdci[i];

        return ad1;
    }

    static double transformSingleWorldToComputing(double d, int i)
    {
        double d1 = qac[i] + (d - qaw2[i]) * qdc[i] * qdw2i[i];
        return d1;
    }

    static double transformSingleComputingToWorld(double d, int i)
    {
        double d1 = qaw2[i] + (d - qac[i]) * qdw2[i] * qdci[i];
        return d1;
    }

    static double transformSingleWorldToDevice(double d, int i)
    {
        double d1 = qadp[i] + (d - qawp[i]) * qddp[i] * qdwpi[i];
        return d1;
    }

    private static void ladder(double ad[], double ad1[], int i, int l, int i1, double d, double d1, int j1, double d2, double d3, double d4, boolean flag)
    {
        char ac[] = new char[7];
        double ad2[] = new double[2];
        double ad3[] = new double[2];
        double ad4[] = new double[2];
        double ad5[] = new double[2];
        double ad6[] = new double[2];
        double ad7[] = new double[2];
        double ad8[] = new double[2];
        boolean aflag[] = new boolean[1];
        double ad9[] = new double[1];
        int ai[] = new int[3];
        NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
        numberformat.setGroupingUsed(false);
        numberformat.setMaximumIntegerDigits(9);
        NumberFormat numberformat1 = NumberFormat.getNumberInstance(Locale.US);
        numberformat1.setGroupingUsed(false);
        numberformat.setMaximumIntegerDigits(9);
        numberformat1.setMaximumIntegerDigits(9);
        numberformat1.setMaximumFractionDigits(5);
        NumberFormat numberformat2 = NumberFormat.getNumberInstance(Locale.US);
        if(numberformat2 instanceof DecimalFormat)
            ((DecimalFormat)numberformat2).applyPattern("0.#####E0");
        int k4 = j1;
        double d5 = d1;
        xmovtx = 0.0D;
        if(k4 <= 0)
            k4 = 1;
        ad2 = transformComputingToWorld(ad);
        ad3 = transformComputingToWorld(ad1);
        int l1;
        if(i == 1 && Math.abs(i1) == 2)
        {
            l1 = (14 - i1) / 4;
            ad4[0] = 1.0D;
            ad4[1] = 0.0D;
        } else
        {
            l1 = (3 - i1) / 2;
            ad4[0] = 1.0D;
            ad4[1] = 0.0D;
        }
        for(int k2 = 0; k2 < 2; k2++)
        {
            ad6[k2] = ad2[k2];
            ad7[k2] = ad3[k2];
            ad8[k2] = ad[k2];
        }

        double d9 = (Math.abs(ad[i - 1]) + Math.abs(ad1[i - 1])) * 0.5D;
        int l4;
        if(d9 > 0.0D)
            l4 = 5 - (int)(Math.log(d9) / Math.log(10D) + 0.5D);
        else
            l4 = 5;
        boolean flag3;
        if(l4 >= 0 && l4 <= 9)
        {
            numberformat.setMaximumFractionDigits(l4);
            flag3 = true;
        } else
        {
            flag3 = false;
            numberformat.setMaximumFractionDigits(0);
        }
        double d12 = Math.abs(ad1[i - 1] - ad[i - 1]);
        int i5;
        if(d12 != 0.0D)
            i5 = (int)Math.rint(Math.log(d12) / Math.log(10D) - 0.5D);
        else
            i5 = 0;
        double d10 = Math.pow(10D, i5);
        int j2 = 0;
        if(d5 > 0.0D)
            j2 = (int)(d12 / d5) + 1;
        if(d5 <= 0.0D || j2 > 50)
        {
            double d11 = d12 / d10;
            if(d11 <= 1.5D)
            {
                d5 = d10 * 0.20000000000000001D;
                k4 = 2;
            } else
            if(d11 <= 3.5D)
            {
                d5 = d10 * 0.5D;
                k4 = 5;
            } else
            if(d11 <= 7.5D)
            {
                d5 = d10;
                k4 = 5;
            } else
            {
                d5 = d10 * 2D;
                k4 = 2;
            }
            double d8 = Math.abs(transformSingleWorldToComputing(d4, i - 1) - transformSingleWorldToComputing(0.0D, i - 1));
            int k1;
            if(l1 == 1 || l1 == 2)
            {
                k1 = (int)((d8 * 1.3999999999999999D) / d5) + 1;
            } else
            {
                double d16 = Math.max(Math.abs(ad[i - 1]), Math.abs(ad1[i - 1]));
                int l3 = (int)(Math.log(d16) / Math.log(10D));
                int i2;
                int k3;
                String s2;
                if(flag && d9 > 0.050000000000000003D)
                {
                    k3 = Math.max(l3, 0) + 3;
                    double d6 = Math.abs(d - Math.rint(d));
                    i2 = Math.max(-(int)(Math.log(d5) / Math.log(10D)), 0);
                    s2 = numberformat.format(d6);
                    int l2 = l4;
                } else
                {
                    k3 = 7;
                    if(Math.abs(l3) >= 9)
                        k3++;
                    double d7 = Math.pow(10D, l3);
                    if(d5 <= d7 * 0.99999000000000005D)
                        k3++;
                    d7 = Math.abs(Math.rint(d / d7) - d / d7);
                    s2 = numberformat1.format(d7);
                    i2 = 0;
                    byte byte0 = 5;
                }
                int i3 = s2.length();
                i2 = Math.max(i3, i2);
                k3 += i2;
                k1 = (int)((d8 * 0.80000000000000004D * (double)k3) / d5) + 1;
            }
            k4 = k1;
            d5 *= k1;
        }
        if(d3 > 0.0D || d4 > 0.0D)
        {
            int j5 = 0;
            do
            {
                boolean flag2;
                if(!(flag2 = scale(ad[i - 1], ad1[i - 1], d, d5, k4, aflag, ad9, ai) && j5 < 10))
                    break;
                boolean flag1 = aflag[0];
                double d14 = ad9[0];
                int j3 = ai[0];
                int j4 = ai[1];
                int i4 = ai[2];
                double d15 = Math.abs(d14);
                double d13;
                if(j3 == 1)
                    d13 = d3;
                else
                    d13 = d3 * 0.5D;
                String s = " ";
                boolean flag4 = false;
                String s3 = " ";
                ad8[i - 1] = d14;
                ad6[i - 1] = transformSingleComputingToWorld(d14, i - 1);
                ad7[i - 1] = ad6[i - 1];
                if(d3 > 0.0D)
                {
                    ad7[Math.abs(l) - 1] = ad6[Math.abs(l) - 1] + d13 * (double)(l / Math.abs(l));
                    drawLine(ad6, ad7, 0.0D);
                }
                ad7[Math.abs(l) - 1] = ad6[Math.abs(l) - 1];
                if(d4 > 0.0D && j3 == 1)
                {
                    ad7[Math.abs(i1) - 1] = ad6[Math.abs(i1) - 1] + d13 * 2D * (double)(i1 / Math.abs(i1));
                    String s1;
                    if(flag && flag3)
                    {
                        if(flag1 && (d9 > 0.05000000074505806D || d15 < d9 * 9.9999997473787516E-006D) && d15 <= 500D)
                        {
                            if(i4 == 1)
                            {
                                if(j4 == 0)
                                {
                                    s1 = "0";
                                } else
                                {
                                    String s4 = numberformat1.format(j4);
                                    s1 = (new StringBuilder()).append(s4).append("&p").toString();
                                    if(j4 == 1)
                                        s1 = "&p";
                                    else
                                    if(j4 == -1)
                                        s1 = "-&p";
                                }
                            } else
                            {
                                String s5 = numberformat1.format(j4);
                                s1 = (new StringBuilder()).append(s5).append("&p").toString();
                                s5 = numberformat1.format(i4);
                                s1 = (new StringBuilder()).append(s1).append("@/").append(s5).toString();
                                if(j4 == 1)
                                    s1 = (new StringBuilder()).append("&p@/").append(s5).toString();
                                else
                                if(j4 == -1)
                                    s1 = (new StringBuilder()).append("-&p@/").append(s5).toString();
                            }
                        } else
                        if(Math.abs(d14 - Math.rint(d14)) < Math.max(Math.abs(ad1[i - 1] - ad[i - 1]) * 1.0000000000000001E-005D, 0.0001D) && d9 > 0.050000000000000003D)
                        {
                            String s6 = numberformat1.format((int)Math.rint(d14));
                            s1 = s6;
                        } else
                        {
                            String s7 = numberformat.format(d14);
                            s1 = s7;
                        }
                    } else
                    {
                        if(d15 <= d9 * 9.9999997473787516E-006D)
                            d14 = 0.0D;
                        String s8 = numberformat2.format(d14);
                        StringBuffer stringbuffer = new StringBuffer(s8.length());
                        StringBuffer stringbuffer1 = new StringBuffer(s8.length());
                        StringBuffer stringbuffer2 = new StringBuffer(s8.length());
                        for(int k5 = 0; k5 < s8.length() && s8.charAt(k5) != 'E'; k5++)
                            stringbuffer1.append(s8.charAt(k5));

                        String s9 = stringbuffer1.toString();
                        for(int l5 = s8.indexOf('E') + 1; l5 < s8.length(); l5++)
                            stringbuffer2.append(s8.charAt(l5));

                        String s10 = stringbuffer2.toString();
                        if(s10.charAt(0) == '0')
                            s1 = s9;
                        else
                            s1 = (new StringBuilder()).append("%").append(s9).append("*10^").append(s10).toString();
                    }
                    text(l1, ad7[0], ad7[1], ad4[0], ad4[1], d4, s1, ad5);
                    if(l1 == 1 || l1 == 2)
                    {
                        xmovtx = Math.max(Math.abs(ad5[0] - ad3[0]), xmovtx);
                        ad7[Math.abs(i1) - 1] = ad6[Math.abs(i1) - 1];
                    }
                }
            } while(true);
        }
    }

    private static boolean scale(double d, double d1, double d2, double d3, 
            int i, boolean aflag[], double ad[], int ai[])
    {
        boolean flag = false;
        if(!rnt)
        {
            nc = 0;
            an = Math.min(d, d1);
            en = Math.max(d, d1);
            int i1 = (int)Math.rint((d2 - an) / d3) + 1;
            atims = d2 - (double)i1 * d3;
            atdel = d3 / (double)i;
            eps = (en - an) * 0.001D;
            aneps = an - eps;
            eneps = en + eps;
            eta = Math.max((en - an) * 1.0000000000000001E-005D, 0.0001D);
        }
        byte byte0 = 0;
        int k1 = 0;
        double d4;
        int j1;
        boolean flag1;
        do
        {
            flag1 = false;
            rnt = atims < eneps;
            d4 = atims;
            if(atims > aneps && rnt)
                flag = true;
            if(nc == 0 || nc == i)
            {
                j1 = 1;
                nc = 0;
                if(Math.abs(d4) < 1000000D)
                {
                    int l = (int)Math.rint(d4 / piqurt);
                    if(Math.abs(d4 - (double)l * piqurt) <= eta)
                    {
                        flag1 = true;
                        if(l - 4 * (l / 4) == 0)
                        {
                            k1 = l / 4;
                            byte0 = 1;
                        } else
                        if(l - 2 * (l / 2) == 0)
                        {
                            k1 = l / 2;
                            byte0 = 2;
                        } else
                        {
                            k1 = l;
                            byte0 = 4;
                        }
                    }
                }
            } else
            {
                j1 = 0;
            }
            atims += atdel;
            nc++;
        } while(rnt && !flag);
        if(!flag)
            nc = 0;
        aflag[0] = flag1;
        ad[0] = d4;
        ai[0] = j1;
        ai[1] = k1;
        ai[2] = byte0;
        return flag;
    }

    public static void drawScaleX(String s)
    {
        double ad[] = new double[2];
        double ad1[] = new double[2];
        double ad2[] = new double[2];
        ad[0] = qac[0];
        ad[1] = qac[1];
        ad1[0] = qbc[0];
        ad1[1] = qac[1];
        double d1 = Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1])) * 0.014999999664723873D;
        double d;
        if(lscset)
        {
            d = grdef * d1;
            d1 = ticdef * d1;
            ladder(ad, ad1, 1, 2, -2, fixdef, deldef, ntcdef, 0.0D, d1, d, numdef);
        } else
        {
            d = d1;
            ladder(ad, ad1, 1, 2, -2, 0.0D, 0.0D, 0, 0.0D, d1, d, true);
        }
        byte byte0;
        if(qbc[0] - qac[0] >= 0.0D)
            byte0 = 1;
        else
            byte0 = -1;
        ad2[0] = Math.max(qaw2[0], qbw2[0]);
        ad2[1] = Math.min(qaw2[1], qbw2[1]) - 2D * d1 - 3D * d;
        drawArrow(ad2, byte0, s, d);
        ad[1] = qbc[1];
        ad1[1] = qbc[1];
        if(lscset)
            ladder(ad, ad1, 1, -2, -2, fixdef, deldef, ntcdef, 0.0D, d1, 0.0D, numdef);
        else
            ladder(ad, ad1, 1, -2, -2, 0.0D, 0.0D, 0, 0.0D, d1, 0.0D, true);
        lscset = false;
    }

    public static void drawScaleY(String s)
    {
        double ad[] = new double[2];
        double ad1[] = new double[2];
        double ad2[] = new double[2];
        ad[0] = qac[0];
        ad[1] = qac[1];
        ad1[0] = qac[0];
        ad1[1] = qbc[1];
        double d1 = Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1])) * 0.014999999664723873D;
        double d;
        if(lscset)
        {
            d = grdef * d1;
            d1 = ticdef * d1;
            ladder(ad, ad1, 2, 1, -1, fixdef, deldef, ntcdef, 0.0D, d1, d, numdef);
        } else
        {
            d = d1;
            ladder(ad, ad1, 2, 1, -1, 0.0D, 0.0D, 0, 0.0D, d1, d, true);
        }
        byte byte0;
        if(qbc[1] - qac[1] >= 0.0D)
            byte0 = 2;
        else
            byte0 = -2;
        ad2[0] = Math.min(qaw2[0], qbw2[0]) - 2D * d - xmovtx;
        ad2[1] = Math.max(qaw2[1], qbw2[1]) - 0.5D * d;
        drawArrow(ad2, byte0, s, d);
        ad[0] = qbc[0];
        ad1[0] = qbc[0];
        if(lscset)
            ladder(ad, ad1, 2, -1, -1, fixdef, deldef, ntcdef, 0.0D, d1, 0.0D, numdef);
        else
            ladder(ad, ad1, 2, -1, -1, 0.0D, 0.0D, 0, 0.0D, d1, 0.0D, true);
        lscset = false;
    }

    public static void setParametersForScale(double d, double d1, int i, double d2, double d3, boolean flag)
    {
        lscset = true;
        fixdef = d;
        deldef = d1;
        ntcdef = i;
        if(d2 > 0.0D)
            ticdef = d2;
        else
            ticdef = 1.0D;
        if(d3 > 0.0D)
            grdef = d3;
        else
            grdef = 1.0D;
        numdef = flag;
    }

    public static void setStandardPaperSizeAndBorders()
    {
        papersize[0] = 21.022410000000001D;
        papersize[1] = 29.730177000000001D;
        paperborders[0] = 0.5D;
        paperborders[1] = 0.5D;
        paperborders[2] = 0.5D;
        paperborders[3] = 0.5D;
    }

    public static void setPaperSizeAndBorders(double d, double d1, double d2, double d3, 
            double d4, double d5)
    {
        papersize[0] = d;
        papersize[1] = d1;
        paperborders[0] = d2;
        paperborders[1] = d3;
        paperborders[2] = d5;
        paperborders[3] = d4;
    }

    public static void setStandardScreenColors()
    {
        iqpsfp = true;
        ct[0] = new Color(0.0F, 0.0F, 0.9F);
        ct[1] = new Color(1.0F, 1.0F, 0.0F);
        ct[2] = new Color(1.0F, 1.0F, 1.0F);
        ct[3] = new Color(0.0F, 1.0F, 1.0F);
        ct[4] = new Color(1.0F, 0.784F, 0.0F);
        ct[5] = new Color(1.0F, 0.0F, 0.0F);
        ct[6] = new Color(0.753F, 0.753F, 0.753F);
        ct[7] = new Color(0.0F, 1.0F, 0.0F);
        ct[8] = new Color(1.0F, 0.0F, 1.0F);
        lw[0] = 70F;
        lw[1] = 70F;
        lw[2] = 70F;
        lw[3] = 70F;
        lw[4] = 70F;
        lw[5] = 70F;
        lw[6] = 70F;
        lw[7] = 70F;
        for(int i = 0; i < 8; i++)
            strokes[i] = new BasicStroke(lw[i], 1, 1);

    }

    public static void setScreenColor(int i, Color color, float f)
    {
        if(i > 0 && i <= 8)
        {
            flushPolyline();
            ct[i] = color;
            lw[i - 1] = 35F * f;
            strokes[i - 1] = new BasicStroke(lw[i - 1], 1, 1);
        }
    }

    public static void setScreenBackground(Color color)
    {
        iqpsfp = false;
        ct[0] = color;
    }

    public static void setStandardPSColors()
    {
        ctab[0] = Color.white;
        ctab[1] = new Color(0.0F, 0.0F, 1.0F);
        ctab[2] = new Color(0.0F, 0.0F, 0.0F);
        ctab[3] = new Color(0.0F, 1.0F, 0.0F);
        ctab[4] = new Color(1.0F, 0.0F, 0.0F);
        ctab[5] = new Color(1.0F, 0.0F, 0.0F);
        ctab[6] = new Color(0.7F, 0.0F, 0.75F);
        ctab[7] = new Color(0.0F, 1.0F, 0.0F);
        ctab[8] = new Color(1.0F, 0.0F, 1.0F);
        for(int i = 0; i < 8; i++)
            linw[i] = 2.0F;

    }

    public static void setPSColor(int i, Color color, float f)
    {
        if(i > 0 && i <= 8)
        {
            ctab[i] = color;
            linw[i - 1] = f;
        }
    }

    public static void setStandardPSColorsBlackAndWhite()
    {
        iqpsfp = false;
        ctab[0] = Color.white;
        for(int i = 1; i < 9; i++)
        {
            ctab[i] = Color.black;
            linw[i - 1] = 2.0F;
        }

    }

    public static double roundUp(double d)
    {
        double d4 = 1.0D;
        if(d < 0.0D)
            d4 = -1D;
        d = Math.abs(d);
        double d5;
        if(d == 0.0D)
        {
            d5 = 0.0D;
        } else
        {
            double d6 = Math.log10(d);
            double d1 = Math.floor(d6);
            double d2 = Math.pow(10D, d1);
            double d3 = Math.ceil((10D * d) / d2);
            int i = (int)d3;
            int l = i % 10;
            if(d4 == 1.0D)
                d3++;
            else
                d3--;
            d5 = d4 * d3 * 0.10000000000000001D * d2;
        }
        return d5;
    }

    public static double roundDown(double d)
    {
        double d4 = 1.0D;
        if(d < 0.0D)
            d4 = -1D;
        d = Math.abs(d);
        double d5;
        if(d == 0.0D)
        {
            d5 = 0.0D;
        } else
        {
            double d6 = Math.log10(d);
            double d1 = Math.floor(d6);
            double d2 = Math.pow(10D, d1);
            double d3 = Math.ceil((10D * d) / d2);
            int i = (int)d3;
            int l = i % 10;
            if(d4 == 1.0D)
                d3--;
            else
                d3++;
            d5 = d4 * d3 * 0.10000000000000001D * d2;
        }
        return d5;
    }

    private static String getExtension(String s)
    {
        String s1 = null;
        int i = s.lastIndexOf('.');
        if(i > 0 && i < s.length() - 1)
            s1 = s.substring(i + 1).toLowerCase();
        return s1;
    }

    private static JFrame dGFrame;
    private static JFrame closeFrame;
    private static JOptionPane pane = new JOptionPane();
    private static JDialog dialog;
    private static PrintStream pr;
    static int frameIndex;
    static Vector frames;
    static int wlisttop;
    static int wlisthdel;
    static int wlistvdel;
    static Dimension screendim;
    static Point startPoint;
    static Color ct[] = {
        new Color(0.0F, 0.0F, 0.9F), new Color(1.0F, 1.0F, 0.0F), new Color(1.0F, 1.0F, 1.0F), new Color(0.0F, 1.0F, 1.0F), new Color(1.0F, 0.784F, 0.0F), new Color(1.0F, 0.0F, 0.0F), new Color(0.753F, 0.753F, 0.753F), new Color(0.0F, 1.0F, 0.0F), new Color(1.0F, 0.0F, 1.0F)
    };
    static float lw[] = {
        70F, 70F, 70F, 70F, 70F, 70F, 70F, 70F
    };
    static BasicStroke strokes[];
    static double framecoords[] = new double[4];
    private static int istr;
    private static int ilen;
    private static byte bstrng[] = new byte[80];
    private static byte bcmstr[] = new byte[12];
    private static final byte numbytes = 12;
    private static boolean iqpsfp = false;
    private static int ixyc[] = new int[2];
    private static boolean lwrelPS;
    private static Color ctab[] = {
        new Color(1.0F, 1.0F, 0.902F), new Color(0.0F, 0.0F, 1.0F), new Color(0.0F, 0.0F, 0.0F), new Color(0.0F, 1.0F, 0.0F), new Color(1.0F, 0.0F, 0.0F), new Color(1.0F, 0.0F, 0.0F), new Color(0.7F, 0.0F, 0.75F), new Color(0.0F, 1.0F, 0.0F), new Color(1.0F, 0.0F, 1.0F)
    };
    private static float linw[] = {
        2.0F, 2.0F, 2.0F, 2.0F, 2.0F, 2.0F, 2.0F, 2.0F
    };
    static int idin;
    static double rformt;
    static boolean lwrel[];
    static double cdin = 118.92D;
    static double root2 = 1.4141999999999999D;
    static double qac[] = new double[2];
    static double qbc[] = new double[2];
    static double qdc[] = new double[2];
    static double qdci[] = new double[2];
    static double qaw2[] = new double[2];
    static double qbw2[] = new double[2];
    static double qdw2[] = new double[2];
    static double qdw2i[] = new double[2];
    static double qcaw2[] = new double[2];
    static double qcbw2[] = new double[2];
    static double qawp[] = new double[2];
    static double qbwp[] = new double[2];
    static double qdwp[] = new double[2];
    static double qdwpi[] = new double[2];
    static double qad[] = {
        0.0D, 0.0D
    };
    static double qbd[] = {
        32767D, 32767D
    };
    static double qdd[] = {
        32767D, 32767D
    };
    static double qddi[] = {
        3.0518500000000003E-005D, 3.0518500000000003E-005D
    };
    static double qform[] = new double[2];
    static double qadc[] = new double[2];
    static double qbdc[] = new double[2];
    static double qadp[] = new double[2];
    static double qbdp[] = new double[2];
    static double qddp[] = new double[2];
    static double qddpi[] = new double[2];
    static int iqad[] = new int[2];
    static int iqbd[] = new int[2];
    static double qadcl[] = new double[2];
    static double qbdcl[] = new double[2];
    static int iqadp[] = new int[2];
    static int iqbdp[] = new int[2];
    static double papersize[] = {
        21.022410000000001D, 29.730177000000001D
    };
    static double paperborders[] = {
        0.5D, 0.5D, 0.5D, 0.5D
    };
    static double psOffsets[] = new double[2];
    static double plsizval[] = new double[2];
    static int iqdvsi[] = {
        0, 32767
    };
    static boolean rotatePlot;
    static int iqplcl;
    static int iqnplc;
    static int iqnplm;
    static int iqoutx[];
    static int iqouty[];
    static int iqclix[];
    static int iqcliy[];
    static int iqclda[];
    static double qoutx[];
    static double qouty[];
    static short bufPoints[];
    static short newBufPoints[];
    static int bufPoly[];
    static int newBufPoly[];
    static int numpolys;
    static int bufPointsLengthFilled;
    static int bufPointsLengthTotal;
    static int bufPolyLengthFilled;
    static int bufPolyLengthTotal;
    static boolean open = false;
    static boolean workstation1Open = false;
    static boolean workstation2Open = false;
    static String psFilename;
    static String workstation1Title;
    static int maxadev = 2;
    static int iqnadv = 0;
    static int psModus;
    static int aqadid[] = {
        0, 0
    };
    static int k[] = {
        64, 38, 37
    };
    static int kk[] = {
        94, 95, 35
    };
    static int kkk = 34;
    static int ll[] = {
        1, 96, 96, 4, 96, 96, 95, 62, 165, 3, 
        108, 12, 109, 14, 60, 16, 17, 18, 19, 20, 
        21, 22, 23, 24, 25, 26, 27, 156, 29, 153, 
        97, 96, 33, 34, 56, 177, 37, 184, 176, 40, 
        41, 41, 43, 179, 45, 46, 186, 181, 178, 48, 
        182, 52, 47, 96, 185, 180, 183, 58, 6, 96, 
        32, 96, 96, 95, 91, 175, 93, 94, 122, 123, 
        143, 125, 128, 142, 124, 127, 130, 130, 131, 132, 
        133, 134, 145, 137, 129, 138, 139, 140, 79, 96, 
        144, 135, 141, 126
    };
    static int lll[] = {
        1, 96, 96, 4, 96, 96, 7, 63, 147, 148, 
        11, 173, 155, 14, 5, 16, 17, 18, 19, 20, 
        21, 22, 23, 24, 25, 26, 27, 161, 146, 162, 
        94, 96, 166, 34, 159, 154, 37, 38, 150, 40, 
        152, 42, 43, 175, 151, 46, 167, 167, 49, 163, 
        169, 92, 168, 168, 157, 56, 164, 58, 59, 96, 
        61, 96, 96, 95, 91, 175, 93, 94, 170, 66, 
        67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 
        77, 78, 171, 80, 81, 82, 83, 84, 172, 86, 
        87, 88, 89, 90
    };
    static int mm[] = {
        1, 96, 96, 4, 96, 96, 7, 8, 9, 10, 
        11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 
        31, 96, 33, 34, 35, 36, 37, 38, 39, 40, 
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 
        51, 52, 53, 54, 55, 56, 57, 58, 59, 96, 
        61, 96, 96, 95, 91, 175, 93, 94, 65, 66, 
        67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 
        77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 
        87, 88, 89, 90
    };
    static int m[] = {
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 
        53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 
        63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 
        73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 
        83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 
        93, 94, 95, 96, 123, 124, 125, 126, 97, 98, 
        99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 
        109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 
        119, 120, 121, 122
    };
    static final short iqlett[] = {
        -810, 811, 911, 910, 810, -835, 935, 918, 818, 835, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -535, 835, 828, 535, -1335, 
        1535, 1528, 1335, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -313, 833, -1013, 1533, -1828, 328, -1518, 18, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1228, 1129, 830, 530, 229, 
        127, 125, 224, 523, 823, 1122, 1220, 1218, 1016, 815, 
        515, 216, 118, -735, 710, -999, 0, 0, 0, 0, 
        1535, -34, 135, 335, 434, 432, 331, 131, 32, 34, 
        -1410, 1210, 1111, 1113, 1214, 1414, 1513, 1511, 1410, -999, 
        0, 0, 0, 0, 0, -1810, 530, 533, 835, 1335, 
        1533, 1530, 518, 513, 810, 1310, 1815, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -534, 434, 435, 535, 532, 430, 329, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1535, 1233, 1030, 925, 920, 
        1015, 1212, 1510, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -35, 333, 530, 625, 620, 515, 312, 10, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -118, 1518, -1313, 323, -825, 
        810, -313, 1323, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -118, 1518, -825, 810, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -210, 110, 111, 211, 207, 
        105, 4, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -18, 1518, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -811, 911, 910, 810, 811, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 1535, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -27, 131, 233, 334, 535, 
        635, 834, 933, 1031, 1127, 1117, 1014, 912, 811, 610, 
        510, 311, 212, 114, 17, 27, -999, 0, 0, 0, 
        -26, 635, 610, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -28, 130, 333, 535, 835, 
        1034, 1232, 1228, 1124, 10, 1210, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -31, 234, 535, 835, 1034, 1231, 1228, 1125, 1024, 524, 
        -1024, 1222, 1320, 1316, 1213, 1011, 710, 410, 211, 13, 
        -999, 0, 0, 0, 0, -835, 14, 1314, -1020, 1010, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1235, 35, 24, 525, 825, 1024, 1222, 1319, 1316, 1213, 
        1111, 810, 510, 311, 13, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1232, 1134, 935, 535, 334, 
        132, 29, 17, 114, 212, 311, 510, 910, 1111, 1212, 
        1316, 1320, 1222, 1123, 924, 524, 323, 121, 18, -999, 
        -31, 35, 1335, 410, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -535, 233, 30, 28, 423, 
        823, 1228, 1230, 1033, 735, 535, -423, 18, 15, 212, 
        510, 710, 1012, 1215, 1218, 823, -999, 0, 0, 0, 
        -12, 111, 310, 710, 1011, 1113, 1217, 1232, 1034, 835, 
        535, 234, 30, 26, 124, 322, 521, 721, 1022, 1223, 
        -999, 0, 0, 0, 0, -819, 820, 920, 919, 819, 
        -811, 911, 910, 810, 811, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -210, 110, 111, 211, 207, 105, 4, -119, 120, 220, 
        219, 119, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1510, 18, 1523, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -23, 1523, -1515, 15, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 1518, 23, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -333, 535, 1035, 1333, 1328, 823, 820, -811, 911, 910, 
        810, 811, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1510, 310, 13, 23, 325, 
        1325, 1523, 1518, 1315, 515, 318, 520, 820, 1018, 815, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 635, 1210, -219, 1019, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 35, 735, 934, 1132, 
        1229, 1227, 1125, 1024, 723, 23, -723, 1121, 1218, 1216, 
        1012, 911, 710, 10, -999, 0, 0, 0, 0, 0, 
        -1131, 1033, 934, 735, 535, 334, 233, 131, 27, 17, 
        114, 212, 311, 510, 710, 911, 1012, 1114, -999, 0, 
        0, 0, 0, 0, 0, -10, 35, 735, 1033, 1229, 
        1218, 1114, 1012, 811, 710, 10, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 35, 1235, -23, 923, -10, 1210, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 35, 1235, -23, 923, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1131, 1033, 934, 735, 535, 334, 233, 131, 27, 17, 
        114, 212, 311, 510, 710, 911, 1012, 1114, 1217, 1221, 
        821, -999, 0, 0, 0, -10, 35, -1235, 1210, -23, 
        1223, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 610, -310, 335, -35, 635, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -14, 112, 310, 410, 611, 
        712, 815, 835, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 35, -19, 1235, -424, 1210, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 10, 1210, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 35, 718, 1435, 1410, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 35, 1210, 1235, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -27, 131, 233, 334, 535, 735, 934, 1033, 1131, 1227, 
        1217, 1114, 1012, 911, 710, 510, 311, 212, 114, 17, 
        27, -999, 0, 0, 0, -10, 35, 735, 1034, 1232, 
        1329, 1327, 1224, 1022, 721, 21, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -27, 131, 233, 334, 535, 735, 934, 1033, 1131, 1227, 
        1217, 1114, 1012, 911, 710, 510, 311, 212, 114, 17, 
        27, -618, 1210, -999, 0, -10, 35, 735, 1034, 1232, 
        1329, 1327, 1224, 1022, 721, 21, -721, 1310, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1232, 1034, 735, 635, 234, 132, 29, 27, 125, 324, 
        823, 1021, 1119, 1217, 1215, 1112, 810, 510, 211, 112, 
        14, -999, 0, 0, 0, -610, 635, -35, 1235, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -35, 17, 114, 212, 311, 510, 610, 811, 912, 1014, 
        1117, 1135, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 610, 1235, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -35, 410, 828, 1210, 1635, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 1210, -10, 1235, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -35, 623, 1235, -623, 610, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 1235, 10, 1210, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -910, 310, 335, 935, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 1510, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -510, 1110, 1135, 535, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -810, 835, 1030, 530, 835, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1523, 23, 525, 520, 23, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -828, 1028, 1330, 1333, 1035, 
        835, 533, 530, 828, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 
        410, 710, 911, 1012, -1025, 1011, 1110, -999, 0, 0, 
        0, 0, 0, 0, 0, -35, 10, -23, 124, 325, 
        625, 824, 922, 1019, 1016, 913, 811, 610, 310, 11, 
        12, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 
        410, 710, 911, 1012, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 
        122, 19, 16, 113, 211, 410, 710, 911, 1012, -1035, 
        1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 
        410, 710, 911, 1012, -18, 1118, 1120, 1023, -999, 0, 
        0, 0, 0, 0, 0, -735, 535, 334, 232, 210, 
        -26, 626, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 
        410, 710, 911, 1012, -1025, 1006, 904, 603, 303, 4, 
        -999, 0, 0, 0, 0, -35, 10, -23, 124, 325, 
        525, 724, 823, 921, 910, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -110, 125, -31, 29, 229, 231, 31, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -525, 509, 407, 306, 105, 
        5, -431, 429, 629, 631, 431, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -35, 10, -13, 1024, -619, 1010, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 12, 111, 310, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -25, 10, -23, 124, 325, 525, 724, 823, 810, -823, 
        924, 1125, 1325, 1524, 1623, 1610, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, -25, 10, -23, 124, 325, 
        525, 724, 823, 921, 910, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -425, 224, 122, 19, 16, 113, 211, 410, 610, 811, 
        913, 1016, 1019, 922, 824, 625, 425, -999, 0, 0, 
        0, 0, 0, 0, 0, -25, 3, -23, 124, 325, 
        625, 824, 922, 1019, 1016, 913, 811, 610, 310, 11, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1025, 1003, -1023, 924, 725, 425, 224, 122, 19, 16, 
        113, 211, 410, 710, 911, 1012, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, -25, 10, -23, 124, 325, 
        525, 724, 823, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1223, 1124, 825, 525, 324, 223, 121, 120, 219, 518, 
        818, 1117, 1215, 1213, 1011, 810, 510, 211, 113, -999, 
        0, 0, 0, 0, 0, -23, 823, -431, 412, 511, 
        710, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -810, 825, -812, 711, 510, 310, 211, 112, 14, 25, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -25, 510, 1025, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -25, 410, 721, 1010, 1425, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -25, 1010, -10, 1025, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -3, 203, 304, 1025, -25, 615, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -25, 1025, 10, 1010, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1535, 1335, 1033, 1025, 823, 1020, 1013, 1310, 1510, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1310, 1320, -1325, 1335, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -835, 1035, 1333, 1325, 1523, 1320, 1313, 1010, 810, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -18, 320, 820, 1316, 1816, 
        2118, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -534, 634, 635, 535, 532, 630, 729, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1030, 833, 333, 30, 28, 325, 825, 1023, 1020, 818, 
        318, 20, 23, 325, -818, 1015, 1013, 810, 310, 13, 
        -999, 0, 0, 0, 0, -520, 823, 810, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -20, 323, 1023, 1320, 10, 1310, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -23, 1323, 818, 1315, 810, 
        310, 13, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1010, 1023, 315, 1315, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1323, 323, 318, 820, 1318, 
        1313, 810, 310, 13, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1323, 323, 318, 1318, 1313, 810, 510, 318, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -323, 1323, 310, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -310, 315, 1318, 1323, 323, 318, 1315, 1310, 310, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -313, 510, 1010, 1313, 1320, 
        1023, 523, 320, 318, 1318, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -313, 510, 1010, 1313, 1320, 1023, 523, 320, 313, -1323, 
        10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -118, 1518, -810, 825, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -318, 1318, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -533, 835, 823, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -33, 335, 1035, 1333, 23, 1323, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 1335, 830, 1328, 823, 
        323, 325, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1023, 1035, 328, 1328, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1335, 335, 330, 833, 1330, 
        1325, 823, 323, 25, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1335, 335, 330, 1330, 1325, 823, 523, 330, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -335, 1335, 323, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -323, 328, 1330, 1335, 335, 330, 1328, 1323, 323, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -325, 523, 1023, 1325, 1333, 
        1035, 535, 333, 330, 1330, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -325, 523, 1023, 1325, 1333, 1035, 535, 333, 325, -1335, 
        23, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -318, 1318, -813, 823, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -318, 1318, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1225, 1013, 811, 610, 410, 
        211, 113, 16, 19, 122, 224, 425, 625, 824, 1022, 
        1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        -3, 28, 131, 334, 535, 735, 934, 1033, 1131, 1129, 
        1127, 1025, 924, 623, 223, -623, 922, 1120, 1218, 1215, 
        1011, 710, 510, 212, -999, -24, 125, 225, 324, 422, 
        710, 1225, -710, 703, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -626, 325, 224, 122, 19, 16, 113, 211, 410, 610, 
        811, 913, 1016, 1019, 922, 824, 725, 130, 133, 434, 
        1134, -999, 0, 0, 0, -35, 34, 133, 332, 432, 
        1035, -431, 229, 127, 23, 17, 113, 311, 710, 810, 
        1009, 1006, 805, 5, -999, 0, 0, 0, 0, 0, 
        -25, 125, 224, 321, 310, -321, 524, 725, 925, 1124, 
        1221, 1202, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 
        123, 21, 20, 119, 318, 618, -318, 117, 16, 14, 
        112, 211, 410, 710, 911, 1012, -999, 0, 0, 0, 
        -21, 124, 225, 325, 422, 413, 511, 610, 810, 1111, 
        1213, 1229, 1033, 835, 635, 434, 431, 529, 728, 1527, 
        -999, 0, 0, 0, 0, -25, 13, 111, 310, 410, 
        611, 713, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -25, 10, -925, 725, 18, 710, 910, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 235, 334, 1110, -10, 
        621, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1011, 910, 811, 825, -812, 711, 510, 310, 211, 112, 
        14, 25, -14, 2, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -25, 225, 10, 311, 512, 
        714, 1020, 1125, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -35, 34, 132, 231, 331, 1035, -331, 230, 128, 126, 
        325, 1029, -325, 123, 20, 14, 211, 711, 910, 1009, 
        1007, 906, 705, 5, -999, -425, 224, 122, 19, 16, 
        113, 211, 410, 610, 811, 913, 1016, 1019, 922, 824, 
        625, 425, -999, 0, 0, 0, 0, 0, 0, 0, 
        -25, 1125, -410, 425, -825, 811, 910, 1010, 1111, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -425, 225, 122, 19, 16, 
        113, 211, 410, 610, 811, 913, 1016, 1019, 922, 824, 
        625, 425, -16, 7, 105, 304, 904, 1003, -999, 0, 
        -325, 224, 122, 19, 17, 113, 211, 310, 710, 811, 
        913, 1017, 1019, 922, 824, 725, 325, -725, 1125, -999, 
        0, 0, 0, 0, 0, -625, 613, 711, 910, 1010, 
        1212, -125, 1125, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1225, 1212, 1111, 910, 710, 611, 512, 414, 424, 325, 
        225, 124, 22, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -225, 123, 20, 17, 114, 
        212, 510, 710, 1012, 1114, 1217, 1220, 1123, 925, 725, 
        624, 602, -999, 0, 0, 0, 0, 0, 0, 0, 
        -25, 225, 1005, 1205, -5, 1225, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1225, 1212, 1111, 910, 710, 
        611, 512, 414, 424, 325, 225, 124, 22, -825, 802, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -325, 123, 20, 16, 113, 211, 410, 510, 611, 713, 
        720, -713, 811, 910, 1010, 1211, 1313, 1416, 1420, 1323, 
        1125, -999, 0, 0, 0, -23, 1523, -18, 1518, -13, 
        1513, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -23, 1523, 1025, 1020, 1523, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -25, 1510, -1525, 10, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -18, 1318, -523, 525, 825, 823, 523, -513, 813, 810, 
        510, 513, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -20, 1520, -15, 1515, -1325, 
        310, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -20, 1520, -828, 813, -1510, 10, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -13, 310, 510, 1335, 1535, 
        1833, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -23, 1223, 1422, 1520, 1515, 1413, 1212, 12, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 820, 1510, 10, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -18, 1818, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1523, 323, 122, 20, 15, 
        113, 312, 1512, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -20, 810, 835, 1535, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -15, 18, 320, 820, 1813, 
        2313, 2515, 2518, 2320, 1820, 813, 313, 15, -999, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -35, 1835, 1810, -999, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1520, 1323, 1023, 813, 313, 
        15, 20, 323, 823, 1013, 1313, 1515, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1528, 20, 1513, -1510, 10, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1510, 10, -13, 1520, 28, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -1323, 1025, 1028, 1330, 1530, 1828, 1825, 1523, 1323, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 635, 1210, -219, 1019, 
        -635, 437, 639, 837, 635, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -835, 810, 1015, 515, 810, -999, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 635, 1210, -219, 1019, 
        -337, 334, -937, 934, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -337, 334, 535, 735, 934, 937, -934, 1033, 1131, 1227, 
        1217, 1114, 1012, 911, 710, 510, 311, 212, 114, 17, 
        27, 131, 233, 334, -999, -35, 17, 114, 212, 311, 
        510, 610, 811, 912, 1014, 1117, 1135, -337, 334, -737, 
        734, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        510, -310, 333, 535, 1035, 1333, 1330, 1025, 1318, 1310, 
        810, 513, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 
        122, 19, 16, 113, 211, 410, 710, 911, 1012, -1025, 
        1011, 1110, -333, 330, -733, 730, -999, 0, 0, 0, 
        -425, 224, 122, 19, 16, 113, 211, 410, 610, 811, 
        913, 1016, 1019, 922, 824, 625, 425, -333, 330, -733, 
        730, -999, 0, 0, 0, -810, 825, -812, 711, 510, 
        310, 211, 112, 14, 25, -233, 230, -633, 630, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -506, 813, 1313, 506, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -530, 533, -1030, 1033, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 35, -999, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -10, 35, 1035, 1034, -999, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -10, 535, 1010, 10, -999, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -27, 131, 233, 334, 535, 
        735, 934, 1033, 1131, 1227, 1217, 1114, 1012, 911, 710, 
        510, 311, 212, 114, 17, 27, -223, 1023, -999, 0, 
        -310, 10, -110, 635, 1110, -910, 1210, -999, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -35, 1035, -323, 723, -10, 
        1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -210, 235, -810, 835, -35, 1035, -999, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -1035, 35, 523, 10, 1010, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        -610, 631, 434, 235, 135, 34, -631, 834, 1035, 1135, 
        1234, -999, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, -430, 229, 127, 24, 22, 
        118, 216, 415, 615, 816, 918, 1022, 1024, 927, 829, 
        630, 430, -435, 635, -535, 510, -410, 610, -999, 0, 
        -35, 27, 124, 222, 321, 520, 620, 821, 922, 1024, 
        1127, 1135, -435, 635, -535, 510, -410, 610, -999, 0, 
        0, 0, 0, 0, 0, -210, 510, 515, 316, 119, 
        23, 27, 131, 334, 535, 735, 934, 1131, 1227, 1223, 
        1119, 916, 715, 710, 1010, -999, 0, 0, 0, 0, 
        -310, 10, 3, 303, 310, -301, 13, 0, 300, 301, 
        -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0
    };
    static int id;
    static int ih;
    static int iga;
    static int iga1;
    static int miga;
    static int miga1;
    static int ko;
    static int no;
    static int nu;
    static int nuj;
    static int na1;
    static int na2;
    static int na4;
    static int na5;
    static int na3;
    static int nul;
    static int nur;
    static double alengt;
    static double gvabs;
    static double rvabs;
    static double g1;
    static double gvabs2;
    static double rvabs2;
    static double ga;
    static double gr;
    static double gz;
    static byte c[];
    static int iascii;
    static double fg1 = 0.59999999999999998D;
    static double fg2 = 0.59999999999999998D;
    static double utx = 25D;
    static double uty = 25D;
    static double z = 8D;
    static int idop[] = new int[100];
    static int j[] = new int[100];
    static int idat[] = new int[25];
    static int nunu[] = new int[3];
    static double avin[] = new double[2];
    static double avnext[] = new double[2];
    static double rv[] = new double[2];
    static double av[] = new double[2];
    static double op[] = new double[2];
    static double gv[] = new double[2];
    static double gve[] = new double[2];
    static double rve[] = new double[2];
    static double wert2d[] = new double[2];
    static int ix;
    static int iy;
    static double xmovtx;
    static double piqurt = 0.78539820000000005D;
    static boolean rnt = false;
    static int nc = 0;
    static double an = 0.0D;
    static double en = 0.0D;
    static double atims = 0.0D;
    static double eps = 0.0D;
    static double atdel = 0.0D;
    static double aneps = 0.0D;
    static double eneps = 0.0D;
    static double eta = 0.0D;
    static double fixdef;
    static double deldef;
    static double ticdef;
    static double grdef;
    static int ntcdef;
    static boolean numdef;
    static boolean lscset;
    static double lastPoint[] = new double[2];
    static double borderPoints[] = new double[4];

    static 
    {
        strokes = (new BasicStroke[] {
            new BasicStroke(lw[0], 1, 1), new BasicStroke(lw[1], 1, 1), new BasicStroke(lw[2], 1, 1), new BasicStroke(lw[3], 1, 1), new BasicStroke(lw[4], 1, 1), new BasicStroke(lw[5], 1, 1), new BasicStroke(lw[6], 1, 1), new BasicStroke(lw[7], 1, 1)
        });
        iqnplm = 200;
        iqoutx = new int[iqnplm];
        iqouty = new int[iqnplm];
        iqclix = new int[2 * iqnplm];
        iqcliy = new int[2 * iqnplm];
        iqclda = new int[2 * iqnplm];
        qoutx = new double[iqnplm];
        qouty = new double[iqnplm];
    }

}
