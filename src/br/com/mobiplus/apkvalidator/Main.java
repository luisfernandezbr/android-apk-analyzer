package br.com.mobiplus.apkvalidator;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by luis.fernandez on 10/5/16.
 */
public class Main {

    public static void main (String [] args) throws FileNotFoundException {
        PlayServicesValidator playServicesValidator =  new PlayServicesValidator();
        playServicesValidator.setApkToolJar(args[0]);
        playServicesValidator.setApksDir(args[1]);

        playServicesValidator.run();
    }
}
