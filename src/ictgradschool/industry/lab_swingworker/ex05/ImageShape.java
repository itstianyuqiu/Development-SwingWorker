package ictgradschool.industry.lab_swingworker.ex05;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

/**
 * A shape which is capable of loading and rendering images from files / Uris / URLs / etc.
 */
public class ImageShape extends Shape {

    private Image image;

    public ImageShape(int x, int y, int deltaX, int deltaY, int width, int height, String fileName) throws MalformedURLException {
        this(x, y, deltaX, deltaY, width, height, new File(fileName).toURI());

    }

    public ImageShape(int x, int y, int deltaX, int deltaY, int width, int height, URI uri) throws MalformedURLException {
        this(x, y, deltaX, deltaY, width, height, uri.toURL());

    }

    public ImageShape(int x, int y, int deltaX, int deltaY, int width, int height, URL url) {
        super(x, y, deltaX, deltaY, width, height);

        mySwing ms = new mySwing(width, height, url);
        ms.execute();

    }

    private class mySwing extends SwingWorker<Image, Void>{
        int width;
        int height;
        URL url;

        public mySwing (int width, int height, URL url){
            this.width = width;
            this.height = height;
            this.url = url;
        }
        @Override
        protected Image doInBackground() throws Exception {
            Image image = null;
            try {
                image = ImageIO.read(this.url);
                if (width != image.getWidth(null) || height != image.getHeight(null)) {
                    image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void done(){
            try {
                image = get();
            } catch (InterruptedException e) {
                System.out.println("was interrupted");
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("some weird stuff " + url);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Painter painter) {
        if (this.image==null){
            painter.drawRect(fX, fY, fWidth, fHeight);
            painter.drawCenteredText("Loading...",fX, fY, fWidth, fHeight);
        }

        painter.drawImage(this.image, fX, fY, fWidth, fHeight);

    }
}
