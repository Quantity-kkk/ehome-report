package report.java.jrreport.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageGraphicsServlet extends HttpServlet
{
  public void init()
    throws ServletException
  {
    super.init();
  }

  public void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0L);

    response.setContentType("image/Png");

    String str = request.getParameter("str");
    if ((str == null) && (str.equals("")))
      str = "测试,测试";
    else {
      str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
    }
    String lstr = str;
    int n = 1;
    if (str.indexOf(",") != -1) {
      n = str.split(",").length;
      lstr = str.split(",")[0];
      for (int i = 0; i < str.split(",").length; i++) {
        if (str.split(",")[i].length() > lstr.length()) {
          lstr = str.split(",")[i];
        }
      }
    }
    String fontsize = request.getParameter("font-size");
    if ((fontsize == null) || (fontsize.length() < 1))
      fontsize = "12";
    else {
      fontsize = fontsize.replace("px", "").trim();
    }

    String fontname = request.getParameter("font-family");
    if ((fontname == null) || (fontname.length() < 1))
      fontname = "宋体";
    String bgcolor = request.getParameter("background-color");
    String fontcolor = request.getParameter("font-color");
    String rwidth = request.getParameter("width");
    if ((rwidth != null) && (rwidth.length() > 0))
      rwidth = rwidth.replace("px", "").trim();
    String rheight = request.getParameter("height");
    if ((rheight != null) && (rheight.length() > 0))
      rheight = rheight.replace("px", "").trim();
    Font font1 = new Font(fontname, 0, Integer.parseInt(fontsize));
    Rectangle2D r = font1.getStringBounds(lstr, 
      new FontRenderContext(AffineTransform.getScaleInstance(1.0D, 1.0D), false, false));
    int unitHeight = (int)Math.floor(r.getHeight());

    int width = (int)Math.round(r.getWidth() * n) + 1;
    if ((rwidth != null) && (rwidth.length() > 0) && (Integer.parseInt(rwidth) > width)) width = Integer.parseInt(rwidth);
    int height = unitHeight * n + 3;
    if ((rheight != null) && (rheight.length() > 0) && (Integer.parseInt(rheight) > height)) height = Integer.parseInt(rheight);

    BufferedImage image = new BufferedImage(width, height, 
      1);
    Graphics2D g2d = image.createGraphics();

    if ((bgcolor != null) && (bgcolor.length() > 0) && (bgcolor.indexOf("rgb") != -1))
    {
      String[] bgarr = bgcolor.replace("rgb(", "").replace(")", "").split(",");
      g2d.setBackground(new Color(Integer.parseInt(bgarr[0].trim()), Integer.parseInt(bgarr[1].trim()), Integer.parseInt(bgarr[2].trim())));
    } else {
      g2d.setBackground(new Color(255, 255, 255));
    }

    if ((fontcolor != null) && (fontcolor.length() > 0) && (fontcolor.indexOf("rgb") != -1))
    {
      String[] ftarr = fontcolor.replace("rgb(", "").replace(")", "").split(",");
      g2d.setPaint(new Color(Integer.parseInt(ftarr[0].trim()), Integer.parseInt(ftarr[1].trim()), Integer.parseInt(ftarr[2].trim())));
    } else {
      g2d.setPaint(new Color(0, 0, 0));
    }
    g2d.clearRect(0, 0, width, height);
    g2d.setFont(font1);
    if (n == 2) {
      g2d.drawLine(0, 0, width, height);
      int x1 = (int)Math.round(width - font1.getStringBounds(str.split(",")[0], 
        new FontRenderContext(AffineTransform.getScaleInstance(1.0D, 1.0D), false, false)).getWidth());
      g2d.drawString(str.split(",")[0], x1, font1.getSize());
      int y1 = (int)Math.round(height - font1.getStringBounds(str.split(",")[0], 
        new FontRenderContext(AffineTransform.getScaleInstance(1.0D, 1.0D), false, false)).getHeight()) + font1.getSize();
      g2d.drawString(str.split(",")[1], 0, y1);
    } else if (n == 3) {
      g2d.drawLine(Math.round(width / 2), 0, Math.round(width), Math.round(height));
      g2d.drawLine(0, Math.round(height / 2), Math.round(width), Math.round(height));
      int x1 = (int)Math.round(width - font1.getStringBounds(str.split(",")[0], 
        new FontRenderContext(AffineTransform.getScaleInstance(1.0D, 1.0D), false, false)).getWidth());
      g2d.drawString(str.split(",")[0], x1, font1.getSize() + 3);
      g2d.drawString(str.split(",")[1], font1.getSize() * 2 + 3, font1.getSize() * 2 + 5);
      int y1 = (int)Math.round(height - font1.getStringBounds(str.split(",")[0], 
        new FontRenderContext(AffineTransform.getScaleInstance(1.0D, 1.0D), false, false)).getHeight()) + font1.getSize();
      g2d.drawString(str.split(",")[2], 3, y1);
    } else {
      g2d.drawString(str, 0, font1.getSize());
    }
    ImageIO.write(image, "png", response.getOutputStream());
  }
}

/* 
 * Qualified Name:     report.java.jrreport.util.ImageGraphicsServlet
*
 */