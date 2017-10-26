package org.klawx3.iot.thingspeak;

import org.klawx3.iot.thingspeak.model.ChannelFeed;
import org.klawx3.iot.thingspeak.model.ChannelModel;
import org.klawx3.iot.thingspeak.model.FeedModel;


/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        ThingSpeakRest ts = new ThingSpeakRest("348240","CIMLIVXPCNQT2QOQ","194YK29J566NMS9V");
        FeedModel model = new FeedModel();
        model.setField2("123");
        int serverResponse = ts.setChannelFeed(model);
        switch(serverResponse){
            case ThingSpeakRest.ERROR:
                System.err.println("solicitud incorrecta :'c"); break;
            case ThingSpeakRest.NO_CHANGE:
                System.out.println("Solicitud no ha efecuado cambios"); break;
            default:
                System.out.println("Solicitud correcta!!:"+ serverResponse);

        }
//        ThingSpeakFieldChecker f1_checker = new ThingSpeakFieldChecker(ts,2000,1);
//        f1_checker.addListener( (newValue,oldValue) -> System.out.println(newValue));
//        f1_checker.start();
    }
}
