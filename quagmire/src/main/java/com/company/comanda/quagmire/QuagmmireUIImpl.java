package com.company.comanda.quagmire;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.jgntp.Gntp;
import com.google.code.jgntp.GntpApplicationInfo;
import com.google.code.jgntp.GntpClient;
import com.google.code.jgntp.GntpNotificationInfo;
import com.google.common.io.Closeables;
import static java.util.concurrent.TimeUnit.*;


public class QuagmmireUIImpl implements QuagmireUI {
    
    private static final Logger log = LoggerFactory.getLogger(QuagmmireUIImpl.class);
    
    private static final String APPLICATION_ICON = "images/comanda_icon.png";
    public static final String RING_ICON = "images/ring.png";
    private GntpClient client;
    private GntpNotificationInfo notif1;
    
    private boolean configured;

    @Inject
    public QuagmmireUIImpl(){
        
    }
    
    private void configure() throws IOException{
        GntpApplicationInfo info = Gntp.appInfo("Comanda").icon(getImage(APPLICATION_ICON)).build();
        client = Gntp.client(info).build();
        notif1 = Gntp.notificationInfo(info, "Pedidos pendientes").icon(getImage(APPLICATION_ICON)).build();
        
        client.register();
    }
    
    public void notifyPendingBills(int pendingBills) {
        if(!configured){
            try {
                configure();
                configured = true;
            } catch (IOException e) {
                log.error("Could not configure Growl notifications",e);
            }
        }
        try {
            client.notify(Gntp.notification(notif1, "Title")
                    .text("Message")
                    .withCallback()
                    .context(12345)
                    .header(Gntp.APP_SPECIFIC_HEADER_PREFIX + "Filename", "file.txt")
                    .build(), 5, SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected RenderedImage getImage(String name) throws IOException {
        InputStream is = getClass().getResourceAsStream(name);
        try {
                return ImageIO.read(is);
        } finally {
                Closeables.closeQuietly(is);
        }
}

}
